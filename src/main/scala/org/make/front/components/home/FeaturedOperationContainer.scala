package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadConfiguration
import org.make.front.components.AppState
import org.make.front.models.{OperationExpanded => OperationModel}

object FeaturedOperationContainer {

  case class FeaturedOperationContainerProps(operationSlug: String)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(FeaturedOperation.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[FeaturedOperationContainerProps]) => FeaturedOperation.FeaturedOperationProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[FeaturedOperationContainerProps]) =>
      {
        val slug = props.wrapped.operationSlug

        val OperationsList: Seq[OperationModel] = state.operations.filter(_.slug == slug)

        if (OperationsList.isEmpty) {
          FeaturedOperation.FeaturedOperationProps(OperationModel.empty)
        } else {
          dispatch(LoadConfiguration)
          FeaturedOperation.FeaturedOperationProps(operation = OperationsList.head)
        }
      }
    }
}
