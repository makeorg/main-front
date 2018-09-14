/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.actions.SetCountry
import org.make.front.components.DataLoader.DataLoaderProps
import org.make.front.components.operation.{Operation, WaitingForOperation}
import org.make.front.components.{AppState, DataLoader}
import org.make.front.models.{
  ConsultationVersion,
  OperationExpanded,
  OperationStaticData,
  Tag,
  Operation => OperationModel
}
import org.make.services.operation.OperationService
import org.make.services.tag.TagService
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object ConsultationContainer {

  lazy val reactClass: ReactClass = WithRouter(ReactRedux.connectAdvanced(selectorFactory)(DataLoader.reactClass))

  def selectorFactory: Dispatch => (AppState, Props[Unit]) => DataLoaderProps[OperationExpanded] =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {
        val slug = props.`match`.params("operationSlug")

        val tabs: Seq[String] = Seq("consultation", "actions")

        val activeTab: String = props.`match`.params.get("activeTab").getOrElse("consultation")
        if (!tabs.contains(activeTab)) {
          props.history.push("/404")
        }

        // toDo remove default "FR" when backward compatibility not anymore required
        val countryCode: String = props.`match`.params.get("country").getOrElse("FR").toUpperCase

        val staticOperation: Option[OperationStaticData] = OperationStaticData.findBySlugAndCountry(slug, countryCode)
        if (appState.country != countryCode) {
          dispatch(SetCountry(countryCode))
        }

        val operationExpanded: () => Future[Option[OperationExpanded]] = () => {
          val operationAndTags: Future[(Option[OperationModel], js.Array[Tag])] = for {
            operation <- OperationService.getOperationBySlugAndCountry(slug, countryCode)
            tags      <- TagService.getTags

          } yield (operation, tags)

          operationAndTags.map {
            case (maybeOperation, tagsList) =>
              OperationExpanded.getOperationExpandedFromOperation(maybeOperation, tagsList, countryCode)
          }
        }

        val shouldOperationUpdate: Option[OperationExpanded] => Boolean = { maybeOperation =>
          maybeOperation.forall(operation => operation.slug != slug || operation.country != countryCode)
        }

        val consultationComponent = staticOperation match {
          case Some(operation) if operation.consultationVersion == ConsultationVersion.V2 => Consultation.reactClass
          case _                                                                          => Operation.reactClass
        }

        DataLoaderProps[OperationExpanded](
          future = operationExpanded,
          shouldComponentUpdate = shouldOperationUpdate,
          componentDisplayedMeanwhileReactClass = WaitingForOperation.reactClass,
          componentReactClass = consultationComponent,
          componentProps = { operation =>
            if (operation.consultationVersion == ConsultationVersion.V2) {
              Consultation.ConsultationProps(operation, countryCode, appState.language, onWillMount = () => {
                if (operation.isExpired) {
                  dom.window.location
                    .assign(operation.getWordingByLanguage(appState.language).flatMap(_.learnMoreUrl).getOrElse("/"))
                } else {
                  if (!operation.isActive) {
                    props.history.push("/404")
                  }
                }
              }, activeTab = activeTab, isSequenceDone = appState.isSequenceDone(operation.landingSequenceId.value))
            } else {
              Operation.OperationProps(
                operation = operation,
                countryCode = countryCode,
                language = appState.language,
                onWillMount = () => {
                  if (operation.isExpired) {
                    dom.window.location
                      .assign(operation.getWordingByLanguage(appState.language).flatMap(_.learnMoreUrl).getOrElse("/"))
                  } else {
                    if (!operation.isActive) {
                      props.history.push("/404")
                    }
                  }
                }
              )
            }
          },
          onNotFound = () => {
            props.history.push("/404")
          }
        )
      }
    }
}
