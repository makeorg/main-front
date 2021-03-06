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

package org.make.front.components.userNav

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM.{
  RouterDOMVirtualDOMElements,
  RouterVirtualDOMAttributes
}
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.authenticate.LoginOrRegister.LoginOrRegisterProps
import org.make.front.components.modals.Modal.ModalProps
import org.make.front.components.userNav.UserNav.{UserNavProps, UserNavState}
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{RWDHideRulesStyles, RWDRulesMediumStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingLocation
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object UserNav {

  case class UserNavProps(isConnected: Boolean,
                          userFirstName: Option[String],
                          avatarUrl: Option[String],
                          logout: () => Unit,
                          country: String)

  case class UserNavState(avatarUrl: String, isAuthenticateModalOpened: Boolean, loginOrRegisterView: String = "login")

  lazy val reactClass: ReactClass =
    React.createClass[UserNavProps, UserNavState](
      displayName = "UserNav",
      getInitialState = { _ =>
        UserNavState(avatarUrl = "", isAuthenticateModalOpened = false)
      },
      componentWillReceiveProps = { (self, props) =>
        self.setState(
          UserNavState(avatarUrl = props.wrapped.avatarUrl.getOrElse(""), isAuthenticateModalOpened = false)
        )
      },
      render = self => {
        <.nav(^.className := UserNavStyles.menuWrapper)(
          if (self.props.wrapped.isConnected) {
            ConnectedUserNavElement(
              self.props.wrapped.userFirstName.get,
              self.state.avatarUrl,
              self.props.wrapped.logout,
              self.props.wrapped.country
            )
          } else {
            UnconnectedUserNavElement(self)
          },
          <.style()(UserNavStyles.render[String])
        )
      }
    )
}

object ConnectedUserNavElement {
  def apply(userFirstName: String, avatarUrl: String, logout: () => Unit, country: String): ReactElement =
    <.ul(^.className := UserNavStyles.menu)(
      <.li(^.className := UserNavStyles.menuItem)(
        <.Link(^.to := s"/$country/profile")(
          <.div(^.className := UserNavStyles.avatarWrapper)(if (avatarUrl.nonEmpty) {
            <.img(
              ^.src := avatarUrl,
              ^.alt := userFirstName,
              ^.className := UserNavStyles.avatar,
              ^("data-pin-no-hover") := "true"
            )()
          } else {
            <.i(^.className := js.Array(UserNavStyles.avatarPlaceholder, FontAwesomeStyles.user))()
          }),
          <.div(
            ^.className := js.Array(
              UserNavStyles.userNameWrapper,
              RWDRulesMediumStyles.showInlineBlockBeyondMedium,
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

    def openLoginAuthenticateModal() = () => {
      self.setState(_.copy(isAuthenticateModalOpened = true, loginOrRegisterView = "login"))
    }

    def openRegisterAuthenticateModal() = () => {
      self.setState(_.copy(isAuthenticateModalOpened = true, loginOrRegisterView = "register"))
    }

    def toggleAuthenticateModal() = () => {
      self.setState(_.copy(isAuthenticateModalOpened = !self.state.isAuthenticateModalOpened))
    }

    <.ul(^.className := UserNavStyles.menu)(
      <.li(^.className := UserNavStyles.menuItem)(
        <.button(^.onClick := openLoginAuthenticateModal, ^.className := js.Array(UserNavStyles.menuItemLink))(
          <.i(^.className := js.Array(UserNavStyles.menuItemIcon, FontAwesomeStyles.user))(),
          <.span(
            ^.className := js
              .Array(RWDRulesMediumStyles.showInlineBlockBeyondMedium, TextStyles.title, TextStyles.smallText)
          )(I18n.t("user-nav.login"))
        ),
        <.span(
          ^.className := js.Array(
            UserNavStyles.slash,
            RWDRulesMediumStyles.showInlineBlockBeyondMedium,
            TextStyles.title,
            TextStyles.smallText
          )
        )(unescape("&nbsp;/&nbsp;")),
        <.button(^.onClick := openRegisterAuthenticateModal, ^.className := js.Array(UserNavStyles.menuItemLink))(
          <.span(
            ^.className := js
              .Array(RWDRulesMediumStyles.showInlineBlockBeyondMedium, TextStyles.title, TextStyles.smallText)
          )(I18n.t("user-nav.register"))
        ),
        <.ModalComponent(
          ^.wrapped := ModalProps(
            isModalOpened = self.state.isAuthenticateModalOpened,
            closeCallback = toggleAuthenticateModal()
          )
        )(
          <.LoginOrRegisterComponent(
            ^.wrapped := LoginOrRegisterProps(
              displayView = self.state.loginOrRegisterView,
              trackingContext = TrackingContext(TrackingLocation.navBar),
              trackingParameters = Map.empty,
              trackingInternalOnlyParameters = Map.empty,
              onSuccessfulLogin = () => {
                self.setState(_.copy(isAuthenticateModalOpened = false))
              }
            )
          )()
        )
      )
    )
  }
}

object UserNavStyles extends StyleSheet.Inline {

  import dsl._

  val menuWrapper: StyleA =
    style(display.inlineBlock)

  val menu: StyleA =
    style()

  val menuItem: StyleA =
    style(display.inlineBlock, verticalAlign.baseline, margin(`0`, ThemeStyles.SpacingValue.small.pxToEm()))

  val menuItemLink: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

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
      width(30.pxToEm()),
      height(30.pxToEm()),
      overflow.hidden,
      backgroundColor(ThemeStyles.BackgroundColor.white),
      borderRadius(50.%%),
      border(1.pxToEm(), solid, ThemeStyles.BorderColor.lighter),
      textAlign.center
    )

  val avatarPlaceholder: StyleA =
    style(width(100.%%), lineHeight(28.pxToEm(16)), fontSize(16.pxToEm()), color(ThemeStyles.TextColor.lighter))

  val avatar: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      transform := s"translate(-50%, -50%)",
      width(30.pxToEm()),
      minWidth(100.%%),
      minHeight(100.%%),
      maxWidth.none,
      maxHeight.none
    )

  val slash: StyleA =
    style(color(ThemeStyles.TextColor.lighter))

  val userNameWrapper: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.middle,
      marginLeft(ThemeStyles.SpacingValue.smaller.pxToEm()),
      color(ThemeStyles.TextColor.base)
    )
}
