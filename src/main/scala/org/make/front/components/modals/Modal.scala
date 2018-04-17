package org.make.front.components.modals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.facades.ReactModal.{ReactModalVirtualDOMAttributes, ReactModalVirtualDOMElements}
import org.make.front.styles._
import org.make.front.styles.base.{RWDHideRulesStyles, TableLayoutStyles}
import org.make.front.styles.utils._

object Modal {

  case class ModalProps(isModalOpened: Boolean, closeCallback: () => Unit)

  case class ModalState(isModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[ModalProps, ModalState](
      displayName = "Modal",
      getInitialState = { self =>
        ModalState(isModalOpened = self.props.wrapped.isModalOpened)
      },
      componentWillReceiveProps =
        (self, props) => self.setState(state => state.copy(isModalOpened = props.wrapped.isModalOpened)),
      render = (self) => {
        <.ReactModal(
          ^.contentLabel := "",
          ^.isOpen := self.state.isModalOpened,
          ^.onRequestClose := self.props.wrapped.closeCallback,
          ^.shouldCloseOnOverlayClick := true
        )(
          <.div(
            ^.className := Seq(
              TableLayoutStyles.fullHeightWrapper,
              ModalStyles.preventMainScroll(!self.state.isModalOpened)
            )
          )(
            <.div(^.className := TableLayoutStyles.row)(
              <.div(^.className := Seq(TableLayoutStyles.cell, ModalStyles.mainHeaderWrapper))(
                <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty)
              )
            ),
            <.div(^.className := Seq(TableLayoutStyles.row, ModalStyles.contentWrapper))(
              <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                <.div(^.className := ModalStyles.centeredRow)(
                  <.div(^.className := ModalStyles.contentInnerWrapper)(
                    <.button(
                      ^.className := ModalStyles.closeModalButton,
                      ^.onClick := self.props.wrapped.closeCallback
                    )(
                      <("svg")(
                        ^("xmlns") := "http://www.w3.org/2000/svg",
                        ^("x") := "0px",
                        ^("y") := "0px",
                        ^("width") := "25",
                        ^("height") := "25"
                      )(
                        <("path")(
                          ^("d") := "M12.5,9.3L3.9,0.7l0,0c-0.3-0.3-0.8-0.3-1.1,0L0.7,2.9c-0.3,0.3-0.3,0.8,0,1.1l8.6,8.6l-8.6,8.6 c-0.3,0.3-0.3,0.8,0,1.1l2.1,2.1c0.3,0.3,0.8,0.3,1.1,0l8.6-8.6l8.6,8.6c0.3,0.3,0.8,0.3,1.1,0l2.1-2.1c0.3-0.3,0.3-0.8,0-1.1 l-8.6-8.6l8.6-8.6l0,0c0.3-0.3,0.3-0.8,0-1.1l-2.1-2.1c-0.3-0.3-0.8-0.3-1.1,0L12.5,9.3z"
                        )()
                      )
                    ),
                    self.props.children
                  )
                )
              )
            )
          ),
          <.style()(ModalStyles.render[String])
        )

      }
    )

}

object ModalStyles extends StyleSheet.Inline {

  import dsl._

  val preventMainScroll: (Boolean) => StyleA = styleF.bool(
    isPrevented =>
      if (!isPrevented) {
        styleS(
          unsafeRoot("html")(overflow.hidden),
          unsafeRoot("body")(position.fixed, top(`0`), left(`0`), width(100.%%))
        )
      } else styleS()
  )

  val mainHeaderWrapper: StyleA =
    style(
      paddingBottom(50.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
    )

  val contentWrapper: StyleA =
    style(height(100.%%))

  val centeredRow: StyleA = style(
    paddingRight(ThemeStyles.SpacingValue.small.pxToEm()),
    paddingLeft(ThemeStyles.SpacingValue.small.pxToEm()),
    maxWidth(ThemeStyles.modalMaxWidth),
    marginRight.auto,
    marginLeft.auto,
    ThemeStyles.MediaQueries.beyondSmall(
      paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
    )
  )

  val contentInnerWrapper: StyleA =
    style(
      position.relative,
      minHeight(200.pxToEm()),
      margin(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      padding(ThemeStyles.SpacingValue.large.pxToEm(), `0`, ThemeStyles.SpacingValue.largerMedium.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 2px 4px 0 rgba(0,0,0,0.5)" // TODO: create variable for shadows
    )

  val closeModalButton: StyleA = style(
    position.absolute,
    top(`0`),
    right(`0`),
    height(ThemeStyles.SpacingValue.largerMedium.pxToEm()),
    width(ThemeStyles.SpacingValue.largerMedium.pxToEm()),
    lineHeight(ThemeStyles.SpacingValue.largerMedium.pxToEm()),
    unsafeChild("svg")(verticalAlign.bottom, opacity(0.1), transition := "opacity .2s ease-in-out"),
    &.hover(unsafeChild("svg")(opacity(0.3)))
  )

}
