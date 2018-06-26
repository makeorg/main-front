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
