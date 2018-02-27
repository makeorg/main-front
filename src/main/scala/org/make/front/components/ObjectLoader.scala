package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.core.Counter
import org.make.front.components.Components.RichVirtualDOMElements

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ObjectLoader {

  case class ObjectLoaderProps[T](load: ()       => Future[Option[T]],
                                  onNotFound: () => Unit,
                                  childClass: ReactClass,
                                  createChildProps: (T)     => Any,
                                  shouldReload: (Option[T]) => Boolean,
                                  loader: ReactElement = <.div()(<.SpinnerComponent.empty))

  case class ObjectLoaderState[T](counter: Counter, result: Option[T])

  def reactClass[T]: ReactClass =
    React.createClass[ObjectLoaderProps[T], ObjectLoaderState[T]](displayName = "ObjectLoader", getInitialState = { _ =>
      ObjectLoaderState(new Counter(), None)
    }, componentWillReceiveProps = {
      (self, props) =>
        if (self.props.wrapped.shouldReload(self.state.result)) {
          self.setState(_.copy(result = None))
          val requestIdentifier = self.state.counter.incrementAndGet()
          props.wrapped.load().onComplete {
            case Success(Some(result)) =>
              if (requestIdentifier == self.state.counter.get) {
                self.setState(_.copy(result = Some(result)))
              }
            case Success(None) =>
              if (requestIdentifier == self.state.counter.get) {
                self.props.wrapped.onNotFound()
              }
            case Failure(_) => // TODO
          }
        }
    }, render = { self =>
      self.state.result.map { result =>
        <(self.props.wrapped.childClass)(^.wrapped := self.props.wrapped.createChildProps(result))()
      }.getOrElse(self.props.wrapped.loader)
    })

}
