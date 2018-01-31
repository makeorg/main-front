package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.components.ObjectLoader.ObjectLoaderProps
import org.make.front.components.{AppState, ObjectLoader}
import org.make.front.models.OperationExpanded
import org.make.services.operation.OperationService
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object OperationContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ObjectLoader.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => ObjectLoaderProps[OperationExpanded] =
    (_: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {
        val slug = props.`match`.params("operationSlug")

        val operationExpanded: () => Future[Option[OperationExpanded]] = () => {
          OperationService
            .getOperationBySlug(slug)
            .map { operation =>
              OperationExpanded.getOperationExpandedFromOperation(operation, appState.country)
            }
        }

        ObjectLoaderProps[OperationExpanded](
          load = operationExpanded,
          onNotFound = () => {}, // TODO
          childClass = Operation.reactClass,
          createChildProps = { operation =>
            Operation.OperationProps(
              operation,
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
          }
        )
      }
    }
}
