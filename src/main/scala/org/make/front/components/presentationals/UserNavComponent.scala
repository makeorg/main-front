package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.{LayoutStyleSheet, TextStyleSheet}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{imageAvatar, I18n}
import org.make.front.styles.{FontAwesomeStyles, MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.{Length, StyleA}
import scalacss.internal.mutable.StyleSheet

object UserNavComponent {

  case class WrappedProps(isConnected: Boolean,
                          userFirstName: Option[String],
                          avatarUrl: Option[String],
                          login: ()  => Unit,
                          logout: () => Unit)

  case class State(avatarUrl: String)

  lazy val reactClass: ReactClass =
    React.createClass[WrappedProps, State](
      componentWillReceiveProps = { (self, props) =>
        self.setState(State(avatarUrl = props.wrapped.avatarUrl.getOrElse(imageAvatar.toString)))
      },
      render = self =>
        <.nav(^.className := UserNavStyles.menuWrapper)(if (self.props.wrapped.isConnected) {
          ConnectedUserNavElement(self.props.wrapped.userFirstName.get, self.state.avatarUrl, self.props.wrapped.logout)
        } else {
          UnconnectedUserNavElement(self.props.wrapped.login)
        }, <.style()(UserNavStyles.render[String]))
    )
}

object ConnectedUserNavElement {
  def apply(userFirstName: String, avatarUrl: String, logout: () => Unit): ReactElement =
    <.ul(^.className := UserNavStyles.menu)(
      <.li(^.className := UserNavStyles.menuItem)(
        <.button()(
          <.span(^.className := UserNavStyles.avatarWrapper)(
            <.img(^.src := avatarUrl, ^.className := UserNavStyles.avatar)()
          ),
          <.span(
            ^.className := Seq(
              UserNavStyles.userNameWrapper,
              LayoutStyleSheet.showInlineBlockBeyondMedium,
              TextStyleSheet.title,
              TextStyleSheet.smallText
            )
          )(userFirstName)
        )
      )
    )
}

object UnconnectedUserNavElement {
  def apply(login: () => Unit): ReactElement =
    <.ul(^.className := UserNavStyles.menu)(
      <.li(^.className := UserNavStyles.menuItem)(
        <.button(
          ^.onClick := login,
          ^.className := Seq(UserNavStyles.menuItemLink, TextStyleSheet.title, TextStyleSheet.smallText)
        )(
          <.i(^.className := Seq(UserNavStyles.menuItemIcon, FontAwesomeStyles.user))(),
          unescape("&nbsp;"),
          I18n.t("content.header.connect"),
          unescape("&nbsp;/&nbsp;"),
          I18n.t("content.header.createAccount")
        )
      )
    )
}

object UserNavStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val menuWrapper: StyleA =
    style(display.inlineBlock)

  val menu: StyleA =
    style()

  val menuItem: StyleA =
    style(display.inlineBlock, verticalAlign.baseline, margin :=! s"0 ${MakeStyles.Spacing.small.value}")

  val menuItemLink: StyleA =
    style(color :=! MakeStyles.ThemeColor.primary)

  val menuItemIcon: StyleA =
    style(marginRight(MakeStyles.Spacing.smaller), verticalAlign.baseline, color(MakeStyles.TextColor.lighter))

  val avatarWrapper: StyleA =
    style(
      position.relative,
      display.inlineBlock,
      verticalAlign.middle,
      width(32.pxToEm()),
      height(32.pxToEm()),
      MakeStyles.MediaQueries.beyondSmall(width(40.pxToEm()), height(40.pxToEm())),
      overflow.hidden,
      backgroundColor(MakeStyles.BackgroundColor.white),
      borderRadius(50.%%),
      border :=! s"2px solid ${MakeStyles.BorderColor.base.value}"
    )

  val avatar: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      transform := s"translate(-50%, -50%)",
      width(32.pxToEm()),
      MakeStyles.MediaQueries.beyondSmall(width(40.pxToEm())),
      minWidth(100.%%),
      minHeight(100.%%),
      maxWidth.none,
      maxHeight.none
    )

  val userNameWrapper: StyleA =
    style(display.inlineBlock, verticalAlign.middle, marginLeft(MakeStyles.Spacing.smaller))
}
