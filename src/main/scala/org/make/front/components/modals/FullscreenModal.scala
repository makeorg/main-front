package org.make.front.components.modals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.ReactModal.{ReactModalVirtualDOMAttributes, ReactModalVirtualDOMElements}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles}
import org.make.front.styles.utils._

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object FullscreenModal {

  case class FullscreenModalProps(isModalOpened: Boolean, closeCallback: () => _)

  case class FullscreenModalState(isModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[FullscreenModalProps, FullscreenModalState](
      displayName = "FullscreenModal",
      getInitialState = { self =>
        FullscreenModalState(isModalOpened = self.props.wrapped.isModalOpened)
      },
      componentWillReceiveProps =
        (self, props) => self.setState(state => state.copy(isModalOpened = props.wrapped.isModalOpened)),
      render = (self) => {
        <.ReactModal(^.contentLabel := "", ^.isOpen := self.state.isModalOpened, ^.shouldCloseOnOverlayClick := false)(
          <.div(^.className := FullscreenModalStyles.wrapper)(
            <.div(^.className := FullscreenModalStyles.innerWrapper)(
              <.div(^.className := FullscreenModalStyles.closeModalButtonWrapper)(
                <.div(^.className := RowRulesStyles.centeredRow)(
                  <.div(^.className := ColRulesStyles.col)(
                    <.button(
                      ^.className := FullscreenModalStyles.closeModalButton,
                      ^.onClick := self.props.wrapped.closeCallback
                    )(
                      <("svg")(
                        ^("xmlns") := "http://www.w3.org/2000/svg",
                        ^("x") := "0px",
                        ^("y") := "0px",
                        ^("width") := "25",
                        ^("height") := "25",
                        ^("viewBox") := "0 0 25 25"
                      )(
                        <("path")(
                          ^("d") := "M12.5,9.3L3.9,0.7l0,0c-0.3-0.3-0.8-0.3-1.1,0L0.7,2.9c-0.3,0.3-0.3,0.8,0,1.1l8.6,8.6l-8.6,8.6 c-0.3,0.3-0.3,0.8,0,1.1l2.1,2.1c0.3,0.3,0.8,0.3,1.1,0l8.6-8.6l8.6,8.6c0.3,0.3,0.8,0.3,1.1,0l2.1-2.1c0.3-0.3,0.3-0.8,0-1.1 l-8.6-8.6l8.6-8.6l0,0c0.3-0.3,0.3-0.8,0-1.1l-2.1-2.1c-0.3-0.3-0.8-0.3-1.1,0L12.5,9.3z"
                        )()
                      )
                    )
                  )
                )
              ),
              self.props.children
            )
          ),
          <.style()(FullscreenModalStyles.render[String])
        )

      }
    )
}

object FullscreenModalStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      display.table,
      width(100.%%),
      height(100.%%),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      backgroundImage := "linear-gradient(155deg, #FFFFFF 0%, #ECECEC 100%)"
    )

  val innerWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      paddingTop(((ThemeStyles.SpacingValue.medium * 2) + 50 + 25).pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingTop(((ThemeStyles.SpacingValue.medium * 2) + 80 + 25).pxToEm())),
      ThemeStyles.MediaQueries.beyondMedium(paddingTop(((ThemeStyles.SpacingValue.medium * 2) + 80 + 35).pxToEm()))
    )

  val closeModalButtonWrapper: StyleA =
    style(
      position.absolute,
      top((ThemeStyles.SpacingValue.medium + 50).pxToEm()),
      width(100.%%),
      ThemeStyles.MediaQueries.beyondSmall(top((ThemeStyles.SpacingValue.medium + 80).pxToEm()))
    )

  // TODO: avoid too much static values
  val closeModalButton: StyleA = style(
    float.right,
    unsafeChild("svg")(verticalAlign.bottom, opacity(0.3), transition := "opacity .2s ease-in-out"),
    (&.hover)(unsafeChild("svg")(opacity(0.5))),
    ThemeStyles.MediaQueries.beyondMedium(unsafeChild("svg")(width(35.pxToEm()), height(35.pxToEm())))
  )
}
