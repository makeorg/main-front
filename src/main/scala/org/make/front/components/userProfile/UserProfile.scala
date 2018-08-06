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

package org.make.front.components.userProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.userProfile.UserProfileInformations.UserProfileInformationsProps
import org.make.front.components.userProfile.UserProfileNav.UserProfileNavProps
import org.make.front.components.userProfile.UserProfileSettings.UserProfileSettingsProps
import org.make.front.components.userProfile.editingUserProfile.OptinNewsletter.OptinNewsletterProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{User => UserModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.utils._

object UserProfile {

  final case class UserProfileProps(user: Option[UserModel], logout: () => Unit, activeTab: String)
  final case class UserProfileState(activeTab: String)

  val reactClass: ReactClass =
    React
      .createClass[UserProfileProps, UserProfileState](
        displayName = "UserProfile",
        getInitialState = { self =>
          UserProfileState(activeTab = self.props.wrapped.activeTab)
        },
        render = self => {

          def changeActiveTab: String => Unit = { newTab =>
            self.setState(_.copy(activeTab = newTab))
          }

          <("UserProfile")()(
            <.div(^.className := UserProfileStyles.mainHeaderWrapper)(
              <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty),
              <.div(^.className := UserProfileStyles.fixedMainHeaderWrapper)(
                <.CookieAlertContainerComponent.empty,
                <.MainHeaderContainer.empty
              )
            ),
            <.div(^.className := UserProfileStyles.specialColorWrapper)(
              <.div(^.className := UserProfileStyles.regularColorWrapper)(
                <.section(^.className := UserProfileStyles.wrapper)(
                  <.aside(^.className := UserProfileStyles.sidebar)(
                    self.props.wrapped.user.map { user =>
                      <.UserProfileInformationsComponent(
                        ^.wrapped := UserProfileInformationsProps(
                          user = user,
                          logout = self.props.wrapped.logout,
                          activeTab = self.state.activeTab,
                          changeActiveTab = changeActiveTab
                        )
                      )()
                    }.toSeq,
                    <.div(^.className := RWDRulesLargeMediumStyles.showBlockBeyondLargeMedium)(
                      // Todo Uncomment when Summary, Proposals and Actions' tabs are ready
                      // <.ShareUserProfileComponent.empty,
                      <.AltFooterComponent()()
                    )
                  ),
                  <.div(^.className := UserProfileStyles.main)(
                    <.UserProfileNavComponent(
                      ^.wrapped := UserProfileNavProps(
                        activeTab = self.state.activeTab,
                        changeActiveTab = changeActiveTab
                      )
                    )(),
                    if (self.state.activeTab == "summary") {
                      <.UserProfileSummaryComponent()()
                    } else if (self.state.activeTab == "proposals") {
                      <.UserProfileProposalsComponent()()
                    } else if (self.state.activeTab == "actions") {
                      <.UserProfileActionsComponent()()
                    } else if (self.state.activeTab == "settings") {
                      self.props.wrapped.user.map { user =>
                        <.UserProfileSettingsComponent(^.wrapped := UserProfileSettingsProps(user = user))()
                      }.toSeq
                    }
                  ),
                  <.style()(UserProfileStyles.render[String])
                )
              )
            ),
            <.div(^.className := RWDRulesLargeMediumStyles.hideBeyondLargeMedium)(<.MainFooterComponent.empty)
          )
        }
      )
}

object UserProfileStyles extends StyleSheet.Inline {

  import dsl._

  val mainHeaderWrapper: StyleA = style(
    paddingBottom(50.pxToEm()),
    ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
  )

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

  val specialColorWrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.ThemeColor.secondary),
      paddingTop(ThemeStyles.SpacingValue.largerMedium.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(paddingTop(100.pxToEm()))
    )

  val regularColorWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.lightGrey))

  val wrapper: StyleA =
    style(
      display.flex,
      flexFlow := s"column",
      maxWidth(ThemeStyles.containerMaxWidth),
      marginLeft(auto),
      marginRight(auto),
      paddingBottom(20.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        flexFlow := s"row",
        paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
        paddingBottom(40.pxToEm()),
        paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val main: StyleA =
    style(ThemeStyles.MediaQueries.beyondLargeMedium(maxWidth(750.pxToPercent(1140)), marginLeft(30.pxToPercent(1140))))

  val sidebar: StyleA =
    style(ThemeStyles.MediaQueries.beyondLargeMedium(maxWidth(360.pxToPercent(1140))))
}
