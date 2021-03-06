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

package org.make.front.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/**
  * Facade to the react-modal library
  *
  * Example usage:
  * <code>
  * import io.github.shogowada.scalajs.reactjs.React
  * import io.github.shogowada.scalajs.reactjs.React.Self
  * import io.github.shogowada.scalajs.reactjs.VirtualDOM._
  * import io.github.shogowada.scalajs.reactjs.classes.ReactClass
  * import org.make.front.facades.ReactModal._
  * import scalajs.js.Dynamic.{global => g}
  *
  * object TestReactModal {
  *
  *   case class State(isOpen: Boolean = false)
  *
  *   lazy val reactClass = React.createClass[Unit, State](
  *     getInitialState = _ => State(),
  *     render = (self) =>
  *       <.div()(
  *         <.button(^.onClick := toggleOpenModal(self))("Activate Modal"),
  *         <.ReactModal(
  *           ^.contentLabel := "Test Modal",
  *           ^.isOpen := self.state.isOpen,
  *           ^.onAfterOpen := { () =>
  *             g.console.log("On After Open")
  *           },
  *           ^.onRequestClose := toggleOpenModal(self)
  *         )(
  *           <.div()(
  *             <.h1()("This is a Test Modal"),
  *             <.button(^.onClick := toggleOpenModal(self))("Click to close")
  *           )
  *         )
  *     )
  *   )
  *
  *   private def toggleOpenModal(self: Self[Unit, State]) = () => {
  *     self.setState(_.copy(isOpen = !self.state.isOpen))
  *   }
  *
  * }
  * </code>
  */
@js.native
trait ModalStyles extends js.Object {
  def overlay: js.Dictionary[js.Any] = js.native
  def content: js.Dictionary[js.Any] = js.native
}

@js.native
@JSImport("react-modal", JSImport.Default)
object NativeReactModal extends ReactClass {
  def defaultStyles: ModalStyles = js.native
}

object ReactModal {

  implicit class ReactModalVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ReactModal: ReactClassElementSpec = elements(NativeReactModal)
  }

  implicit class ReactModalVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val isOpen = BooleanAttributeSpec("isOpen")
    lazy val closeTimeoutMS = IntegerAttributeSpec("closeTimeoutMS")
    lazy val contentLabel = StringAttributeSpec("contentLabel")
    lazy val shouldCloseOnOverlayClick = BooleanAttributeSpec("shouldCloseOnOverlayClick")
    lazy val onAfterOpen: NativeFunction0Attribute[Unit] = NativeFunction0Attribute("onAfterOpen")
    lazy val onRequestClose: NativeFunction0Attribute[Unit] = NativeFunction0Attribute("onRequestClose")
    lazy val overlayClassName = SpaceSeparatedStringAttributeSpec("overlayClassName")
  }

}
