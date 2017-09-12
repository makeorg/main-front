package org.make.front.components.UserNav

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.presentationals._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.{FontAwesomeStyles, LayoutRulesStyles, TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Length, StyleA}

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
        self.setState(State(avatarUrl = props.wrapped.avatarUrl.getOrElse("")))
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
              LayoutRulesStyles.showInlineBlockBeyondMedium,
              TextStyles.title,
              TextStyles.smallText
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
        <.button(^.onClick := login, ^.className := Seq(UserNavStyles.menuItemLink))(
          <.i(^.className := Seq(UserNavStyles.menuItemIcon, FontAwesomeStyles.user))(),
          <.span(
            ^.className := Seq(LayoutRulesStyles.showInlineBlockBeyondMedium, TextStyles.title, TextStyles.smallText)
          )(I18n.t("content.header.connect"), unescape("&nbsp;/&nbsp;"), I18n.t("content.header.createAccount"))
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
    style(display.inlineBlock, verticalAlign.baseline, margin :=! s"0 ${ThemeStyles.Spacing.small.value}")

  val menuItemLink: StyleA =
    style(color :=! ThemeStyles.ThemeColor.primary)

  val menuItemIcon: StyleA =
    style(
      marginRight(ThemeStyles.Spacing.smaller),
      verticalAlign.baseline,
      fontSize(16.pxToEm()),
      color(ThemeStyles.TextColor.lighter)
    )

  val avatarWrapper: StyleA =
    style(
      position.relative,
      display.inlineBlock,
      verticalAlign.middle,
      width(32.pxToEm()),
      height(32.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(width(40.pxToEm()), height(40.pxToEm())),
      overflow.hidden,
      backgroundColor(ThemeStyles.BackgroundColor.white),
      borderRadius(50.%%),
      border :=! s"2px solid ${ThemeStyles.BorderColor.base.value}"
    )

  val avatar: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      transform := s"translate(-50%, -50%)",
      width(32.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(width(40.pxToEm())),
      minWidth(100.%%),
      minHeight(100.%%),
      maxWidth.none,
      maxHeight.none
    )

  val userNameWrapper: StyleA =
    style(display.inlineBlock, verticalAlign.middle, marginLeft(ThemeStyles.Spacing.smaller))
}
