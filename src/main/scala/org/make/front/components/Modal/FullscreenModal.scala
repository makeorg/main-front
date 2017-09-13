package org.make.front.components.Modal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.presentationals._
import org.make.front.facades.ReactModal.{ReactModalVirtualDOMAttributes, ReactModalVirtualDOMElements}
import org.make.front.styles.{LayoutRulesStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object FullscreenModalComponent {

  case class FullscreenModalProps(isModalOpened: Boolean, closeCallback: () => _)

  case class FullscreenModalState(isModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[FullscreenModalProps, FullscreenModalState](
      displayName = getClass.toString,
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
              <.div(^.className := LayoutRulesStyles.centeredRow)(
                <.article(^.className := LayoutRulesStyles.col)(self.props.children)
              )
            )
          ),
          <.style()(FullscreenModalStyles.render[String])
        )

      }
    )
}

object FullscreenModalStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper =
    style(
      display.table,
      width(100.%%),
      height(100.%%),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      backgroundImage := "linear-gradient(155deg, #FFFFFF 0%, #ECECEC 100%)"
    )

  val innerWrapper =
    style(
      display.tableCell,
      verticalAlign.middle,
      paddingTop((40 + 50).pxToEm()),
      paddingBottom(40.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((40 + 80 + 15).pxToEm()), paddingBottom((40 + 15).pxToEm())),
      ThemeStyles.MediaQueries
        .beyondLarge(paddingTop((50 + 80 + 15).pxToEm()), paddingBottom((50 + 15).pxToEm())) // TODO: dynamise calcul
    )

  // TODO: avoid to much static values
  val closeModalButton = style(
    position.absolute,
    top(50.pxToEm()),
    right(0.%%),
    height(ThemeStyles.Spacing.largerMedium),
    width(ThemeStyles.Spacing.largerMedium),
    lineHeight :=! s"${ThemeStyles.Spacing.largerMedium.value}",
    unsafeChild("svg")(verticalAlign.bottom, opacity(0.3), transition := "opacity .2s ease-in-out"),
    (&.hover)(unsafeChild("svg")(opacity(0.5))),
    ThemeStyles.MediaQueries
      .beyondSmall(top(80.pxToEm()), marginTop(ThemeStyles.Spacing.small), marginRight(ThemeStyles.Spacing.small)),
    media.minWidth((1200 + 50 + 15).pxToEm(16))(
      height(ThemeStyles.Spacing.large),
      width(ThemeStyles.Spacing.large),
      lineHeight :=! s"${ThemeStyles.Spacing.large.value}",
      unsafeChild("svg")(width(35.pxToEm()), height(35.pxToEm()))
    )
  )
}
