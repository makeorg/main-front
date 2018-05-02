package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.SetCountry
import org.make.front.components.DataLoader.DataLoaderProps
import org.make.front.components.{AppState, DataLoader}
import org.make.front.models.{OperationExpanded, Tag, Operation => OperationModel}
import org.make.services.operation.OperationService
import org.make.services.tag.TagService
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object OperationContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(DataLoader.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => DataLoaderProps[OperationExpanded] =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {
        val slug = props.`match`.params("operationSlug")
        // toDo remove default "FR" when backward compatibility not anymore required
        val countryCode: String = props.`match`.params.get("country").getOrElse("FR").toUpperCase
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

        val shouldOperationUpdate: (Option[OperationExpanded]) => Boolean = { maybeOperation =>
          maybeOperation.forall(operation => operation.slug != slug || operation.country != countryCode)
        }

        DataLoaderProps[OperationExpanded](
          future = operationExpanded,
          shouldComponentUpdate = shouldOperationUpdate,
          componentDisplayedMeanwhileReactClass = WaitingForOperation.reactClass,
          componentReactClass = Operation.reactClass,
          componentProps = { operation =>
            Operation.OperationProps(
              operation,
              countryCode,
              appState.language,
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
          },
          onNotFound = () => {
            props.history.push("/404")
          }
        )
      }
    }
}
