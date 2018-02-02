package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.ObjectLoader.ObjectLoaderProps
import org.make.front.components.{AppState, ObjectLoader}
import org.make.front.models.{OperationExpanded => OperationModel}
import org.make.services.operation.OperationService
import org.make.services.tracking.TrackingLocation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object FeaturedOperationContainer {

  case class FeaturedOperationContainerProps(trackingLocation: TrackingLocation, operationSlug: String)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ObjectLoader.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[FeaturedOperationContainerProps]) => ObjectLoaderProps[OperationModel] =
    (_: Dispatch) => { (appState: AppState, props: Props[FeaturedOperationContainerProps]) =>
      {
        val slug = props.wrapped.operationSlug

        def operationExpanded: () => Future[Option[OperationModel]] = () => {
          OperationService
            .getOperationBySlug(slug)
            .map { maybeOperation =>
              OperationModel.getOperationExpandedFromOperation(maybeOperation, appState.country)
            }
        }

        ObjectLoaderProps[OperationModel](
          load = operationExpanded,
          onNotFound = () => {},
          childClass = FeaturedOperation.reactClass,
          createChildProps = { operation =>
            FeaturedOperation
              .FeaturedOperationProps(
                trackingLocation = props.wrapped.trackingLocation,
                operation = operation,
                language = appState.language
              )
          }
        )
      }
    }
}
