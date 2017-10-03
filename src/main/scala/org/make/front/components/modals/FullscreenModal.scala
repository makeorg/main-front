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
      paddingTop((50 + 50).pxToEm()),
      paddingBottom(40.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((50 + 80 + 15).pxToEm()), paddingBottom((40 + 15).pxToEm())),
      ThemeStyles.MediaQueries
        .beyondLarge(paddingTop((60 + 80 + 15).pxToEm()), paddingBottom((50 + 15).pxToEm())) // TODO: dynamise calcul
    )

  // TODO: avoid to much static values
  val closeModalButton: StyleA = style(
    position.absolute,
    top(50.pxToEm()),
    right(`0`),
    height(ThemeStyles.SpacingValue.largerMedium.pxToEm()),
    width(ThemeStyles.SpacingValue.largerMedium.pxToEm()),
    lineHeight :=! s"${ThemeStyles.SpacingValue.largerMedium.pxToEm().value}",
    unsafeChild("svg")(verticalAlign.bottom, opacity(0.3), transition := "opacity .2s ease-in-out"),
    (&.hover)(unsafeChild("svg")(opacity(0.5))),
    ThemeStyles.MediaQueries
      .beyondSmall(
        top(80.pxToEm()),
        marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
        marginRight(ThemeStyles.SpacingValue.small.pxToEm())
      ),
    media.minWidth((1200 + 50 + 15).pxToEm())(
      height(ThemeStyles.SpacingValue.large.pxToEm()),
      width(ThemeStyles.SpacingValue.large.pxToEm()),
      lineHeight :=! s"${ThemeStyles.SpacingValue.large.pxToEm().value}",
      unsafeChild("svg")(width(35.pxToEm()), height(35.pxToEm()))
    )
  )
}
