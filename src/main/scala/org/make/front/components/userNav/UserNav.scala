package org.make.front.components.userNav

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.components.modals.Modal.ModalProps
import org.make.front.components.userNav.UserNav.{UserNavProps, UserNavState}
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles._

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Length, StyleA}

object UserNav {

  case class UserNavProps(isConnected: Boolean,
                          userFirstName: Option[String],
                          avatarUrl: Option[String],
                          logout: () => Unit)

  case class UserNavState(avatarUrl: String, isAuthenticateModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[UserNavProps, UserNavState](
      getInitialState = { _ =>
        UserNavState(avatarUrl = "", isAuthenticateModalOpened = false)
      },
      componentWillReceiveProps = { (self, props) =>
        self.setState(
          UserNavState(avatarUrl = props.wrapped.avatarUrl.getOrElse(""), isAuthenticateModalOpened = false)
        )
      },
      render = self => {

        <.nav(^.className := UserNavStyles.menuWrapper)(if (self.props.wrapped.isConnected) {
          ConnectedUserNavElement(self.props.wrapped.userFirstName.get, self.state.avatarUrl, self.props.wrapped.logout)
        } else {
          UnconnectedUserNavElement(self)
        }, <.style()(UserNavStyles.render[String]))
      }
    )
}

object ConnectedUserNavElement {
  def apply(userFirstName: String, avatarUrl: String, logout: () => Unit): ReactElement =
    <.ul(^.className := UserNavStyles.menu)(
      <.li(^.className := UserNavStyles.menuItem)(
        <.button(^.onClick := logout)(
          <.span(^.className := UserNavStyles.avatarWrapper)(
            <.img(^.src := avatarUrl, ^.className := UserNavStyles.avatar)()
          ),
          <.span(
            ^.className := Seq(
              UserNavStyles.userNameWrapper,
              RWDHideRulesStyles.showInlineBlockBeyondMedium,
              TextStyles.title,
              TextStyles.smallText
            )
          )(userFirstName)
        )
      )
    )
}

object UnconnectedUserNavElement {
  def apply(self: Self[UserNavProps, UserNavState]): ReactElement = {

    def openAuthenticateModal() = () => {
      self.setState(state => state.copy(isAuthenticateModalOpened = true))
    }

    def toggleAuthenticateModal() = () => {
      self.setState(state => state.copy(isAuthenticateModalOpened = !self.state.isAuthenticateModalOpened))
    }

    <.ul(^.className := UserNavStyles.menu)(
      <.li(^.className := UserNavStyles.menuItem)(
        <.button(^.onClick := openAuthenticateModal, ^.className := Seq(UserNavStyles.menuItemLink))(
          <.i(^.className := Seq(UserNavStyles.menuItemIcon, FontAwesomeStyles.user))(),
          <.span(
            ^.className := Seq(RWDHideRulesStyles.showInlineBlockBeyondMedium, TextStyles.title, TextStyles.smallText)
          )(I18n.t("content.header.connect"), unescape("&nbsp;/&nbsp;"), I18n.t("content.header.createAccount"))
        ),
        <.ModalComponent(^.wrapped := ModalProps(self.state.isAuthenticateModalOpened, toggleAuthenticateModal()))(
          <.p()("truc")
        )
      )
    )
  }
}

object UserNavStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val menuWrapper: StyleA =
    style(display.inlineBlock)

  val menu: StyleA =
    style()

  val menuItem: StyleA =
    style(display.inlineBlock, verticalAlign.baseline, margin :=! s"0 ${ThemeStyles.SpacingValue.small.pxToEm().value}")

  val menuItemLink: StyleA =
    style(color :=! ThemeStyles.ThemeColor.primary)

  val menuItemIcon: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondMedium(marginRight(ThemeStyles.SpacingValue.smaller.pxToEm())),
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
    style(display.inlineBlock, verticalAlign.middle, marginLeft(ThemeStyles.SpacingValue.smaller.pxToEm()))
}
