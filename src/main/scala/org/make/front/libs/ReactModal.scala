package org.make.front.libs

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
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
  * import org.make.front.libs.ReactModal._
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
@JSImport("react-modal", JSImport.Default)
object NativeReactModal extends ReactClass

object ReactModal {

  type OnAfterOpen = js.Function0[Unit]
  type OnRequestClose = js.Function0[Unit]

  implicit class ReactModalVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ReactModal: ReactClassElementSpec = elements(NativeReactModal)
  }

  case class OnAfterOpenAttribute(name: String) extends AttributeSpec {
    def :=(onAfterOpen: () => Unit): Attribute[OnAfterOpen] =
      Attribute(name = name, value = onAfterOpen, AS_IS)
  }

  case class OnRequestCloseAttribute(name: String) extends AttributeSpec {
    def :=(onRequestClose: () => Unit): Attribute[OnRequestClose] =
      Attribute(name = name, value = onRequestClose, AS_IS)
  }

  implicit class ReactModalVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val isOpen = BooleanAttributeSpec("isOpen")
    lazy val closeTimeoutMS = IntegerAttributeSpec("closeTimeoutMS")
    lazy val contentLabel = StringAttributeSpec("contentLabel")
    lazy val shouldCloseOnOverlayClick = BooleanAttributeSpec("shouldCloseOnOverlayClick")
    lazy val onAfterOpen = OnAfterOpenAttribute("onAfterOpen")
    lazy val onRequestClose = OnRequestCloseAttribute("onRequestClose")
    lazy val overlayClassName = SpaceSeparatedStringAttributeSpec("overlayClassName")
  }

}
