package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import org.make.front.components.Components.RichVirtualDOMElements

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ObjectLoader {

  case class ObjectLoaderProps[T](load: ()       => Future[Option[T]],
                                  onNotFound: () => Unit,
                                  childClass: ReactClass,
                                  createChildProps: (T) => Any)
  case class ObjectLoaderState[T](result: Option[T])

  def reactClass[T]: ReactClass =
    React.createClass[ObjectLoaderProps[T], ObjectLoaderState[T]](
      displayName = "ObjectLoader",
      getInitialState = { _ =>
        ObjectLoaderState(None)
      },
      componentWillReceiveProps = { (self, props) =>
        props.wrapped.load().onComplete {
          case Success(Some(result)) => self.setState(_.copy(result = Some(result)))
          case Success(None)         => self.props.wrapped.onNotFound()
          case Failure(e)            => // TODO
        }
      },
      render = { self =>
        self.state.result.map { result =>
          <(self.props.wrapped.childClass)(^.wrapped := self.props.wrapped.createChildProps(result))()
        }.getOrElse(<.div()(<.SpinnerComponent.empty))
      }
    )

}
