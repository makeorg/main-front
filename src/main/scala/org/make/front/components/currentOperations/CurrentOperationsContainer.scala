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

package org.make.front.components.currentOperations

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.actions.{SetCountry, SetLanguage}
import org.make.front.components.DataLoader.DataLoaderProps
import org.make.front.components.{AppState, DataLoader}
import org.make.front.facades._
import org.make.front.models.{
  BusinessConfiguration,
  CountryConfiguration,
  Operation         => OperationModel,
  OperationExpanded => OperationExpandedModel,
  Tag               => TagModel
}
import org.make.services.operation.OperationService
import org.make.services.tag.TagService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object CurrentOperationsContainer {

  lazy val reactClass: ReactClass = WithRouter(ReactRedux.connectAdvanced(selectorFactory)(DataLoader.reactClass))

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => DataLoaderProps[js.Array[OperationExpandedModel]] =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {}
      val countryCode: String = props.`match`.params.get("country").getOrElse("FR").toUpperCase

      val supportedCountries: js.Array[CountryConfiguration] = appState.configuration.map {
        businessConfiguration: BusinessConfiguration =>
          businessConfiguration.supportedCountries.map { countryConfiguration: CountryConfiguration =>
            countryConfiguration.defaultLanguage match {
              case "fr" =>
                countryConfiguration.copy(flagUrl = frFlag.toString)
              case "en" =>
                countryConfiguration.copy(flagUrl = gbFlag.toString)
              case "it" =>
                countryConfiguration.copy(flagUrl = itFlag.toString)
              case _ =>
                countryConfiguration.copy(flagUrl = "")
            }
          }
      }.getOrElse(js.Array())

      if (!supportedCountries.exists(country => country.countryCode == countryCode) && appState.language != "en") {
        dispatch(SetLanguage("en"))
      }

      val openCoreCountry = supportedCountries.filter(countryConfiguration => countryConfiguration.coreIsAvailable)

      if (openCoreCountry.exists(country => country.countryCode == countryCode)) {
        props.history.push(s"/$countryCode")
      }

      if (appState.country != countryCode) {
        dispatch(SetCountry(countryCode))
      }

      val operationExpanded: () => Future[Option[js.Array[OperationExpandedModel]]] = () => {
        val operationsAndTags: Future[(js.Array[OperationModel], js.Array[TagModel])] = for {
          operations <- Future.successful(appState.operations.getOperationsByCountry(countryCode).values)
          tags       <- TagService.getTags
        } yield (operations, tags)

        operationsAndTags.map {
          case (operations, tagsList) =>
            operations.flatMap { operation =>
              OperationExpandedModel.getOperationExpandedFromOperation(Some(operation), tagsList, countryCode)
            }
        }.map(Some(_))
      }

      val hasCountryChanged: (Option[js.Array[OperationExpandedModel]]) => Boolean = { maybeOperation =>
        maybeOperation.forall(_.forall(operation => operation.country != countryCode))
      }

      DataLoaderProps[js.Array[OperationExpandedModel]](
        future = operationExpanded,
        shouldComponentUpdate = hasCountryChanged,
        componentDisplayedMeanwhileReactClass = WaitingForCurrentOperations.reactClass,
        componentReactClass = CurrentOperations.reactClass,
        componentProps = { operations =>
          CurrentOperations.CurrentOperationsProps(
            country = appState.country,
            language = appState.language,
            operations = operations,
            supportedCountries = openCoreCountry
          )
        },
        onNotFound = () => {
          props.history.push("/404")
        }
      )
    }
}
