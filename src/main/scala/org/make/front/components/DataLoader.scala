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

object DataLoader {

  case class DataLoaderProps[T](future: ()     => Future[Option[T]],
                                onNotFound: () => Unit,
                                componentReactClass: ReactClass,
                                componentProps: (T)                => Any,
                                shouldComponentUpdate: (Option[T]) => Boolean,
                                componentDisplayedMeanwhileReactClass: ReactClass)

  case class DataLoaderState[T](counter: Counter, result: Option[T])

  def reactClass[T]: ReactClass =
    React.createClass[DataLoaderProps[T], DataLoaderState[T]](displayName = "DataLoader", getInitialState = { _ =>
      DataLoaderState(new Counter(), None)
    }, componentWillReceiveProps = {
      (self, props) =>
        if (self.props.wrapped.shouldComponentUpdate(self.state.result)) {
          self.setState(_.copy(result = None))
          val requestIdentifier = self.state.counter.incrementAndGet()
          props.wrapped.future().onComplete {
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
        <(self.props.wrapped.componentReactClass)(^.wrapped := self.props.wrapped.componentProps(result))()
      }.getOrElse(<(self.props.wrapped.componentDisplayedMeanwhileReactClass).empty)
    })

}
