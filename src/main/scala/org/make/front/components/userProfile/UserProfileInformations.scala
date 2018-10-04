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
import org.make.front.components.userProfile.UserDescription.UserDescriptionProps
import org.make.front.components.userProfile.navUserProfile.ButtonNav.ButtonNavProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Profile, User => UserModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js
import scala.scalajs.js.Date

object UserProfileInformations {

  final case class UserProfileInformationsProps(user: UserModel,
                                                logout: () => Unit,
                                                activeTab: String,
                                                changeActiveTab: String => Unit)
  final case class UserProfileInformationsState(expandSlidingPannel: Boolean, previousTab: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[UserProfileInformationsProps, UserProfileInformationsState](
        displayName = "UserProfileInformations",
        getInitialState = self => {
          UserProfileInformationsState(expandSlidingPannel = false, previousTab = self.props.wrapped.activeTab)
        },
        componentWillReceiveProps = { (self, nextProps) =>
          self.setState(
            UserProfileInformationsState(
              expandSlidingPannel = self.state.expandSlidingPannel,
              previousTab = self.props.wrapped.activeTab
            )
          )
        },
        render = self => {
          val user: UserModel = self.props.wrapped.user
          val userProfile: Option[Profile] = user.profile
          val userAge = userProfile.flatMap(_.dateOfBirth).map { date =>
            Math.abs(new Date(Date.now() - date.getTime()).getUTCFullYear() - 1970)
          }
          val descriptionLength = userProfile.flatMap(_.description).map(_.length).getOrElse(-1)

          def changeTab(newTab: String): () => Unit = { () =>
            self.props.wrapped.changeActiveTab(newTab)
          }

          // Toggle biography collapse method for "show more / show less" button
          def toggleSlidingPannel: () => Unit = { () =>
            self.setState(_.copy(expandSlidingPannel = !self.state.expandSlidingPannel))
          }

          <.div(^.className := UserProfileInformationsStyles.wrapper)(
            <.div(^.className := UserProfileInformationsStyles.headerInfosWrapper)(
              <.div(^.className := UserProfileInformationsStyles.avatarWrapper)(
                if (userProfile.flatMap(_.avatarUrl).nonEmpty) {
                  <.img(
                    ^.src := userProfile.flatMap(_.avatarUrl).getOrElse(""),
                    ^.className := UserProfileInformationsStyles.avatar,
                    ^.alt := user.firstName.getOrElse(user.organisationName.getOrElse("")),
                    ^("data-pin-no-hover") := "true"
                  )()
                } else {
                  <.i(
                    ^.className := js.Array(UserProfileInformationsStyles.avatarPlaceholder, FontAwesomeStyles.user)
                  )()
                }
              ),
              <.div(^.className := UserProfileInformationsStyles.personnalInformations)(
                user.firstName
                  .orElse(user.organisationName)
                  .map { name =>
                    <.h1(^.className := UserProfileInformationsStyles.userName)(name)
                  }
                  .toSeq,
                userProfile
                  .flatMap(_.postalCode)
                  .map { postalCode =>
                    if (postalCode.nonEmpty) {
                      <.p(^.className := UserProfileInformationsStyles.basicInformations)(
                        <.i(
                          ^.className := js.Array(FontAwesomeStyles.mapMarker, UserProfileInformationsStyles.marker)
                        )(),
                        postalCode
                      )
                    }
                  }
                  .toSeq,
                userAge.map { age =>
                  <.p(^.className := UserProfileInformationsStyles.basicInformations)(
                    age,
                    unescape("&nbsp;"),
                    I18n.t("user-profile.years-old")
                  )
                }.toSeq,
                userProfile
                  .flatMap(_.profession)
                  .map { profession =>
                    <.p(^.className := UserProfileInformationsStyles.basicInformations)(profession)
                  }
                  .toSeq
              )
            ),
            if (descriptionLength >= 150) {
              <.UserDescriptionComponent(^.wrapped := UserDescriptionProps(user = self.props.wrapped.user))()
            } else if (descriptionLength >= 15) {
              <.p(
                ^.className := js
                  .Array(UserProfileInformationsStyles.biography, UserProfileInformationsStyles.basicInformations)
              )(
                userProfile
                  .flatMap(_.description)
                  .map { description =>
                    description
                  }
                  .toSeq
              )
            },
            if (self.props.wrapped.activeTab == "settings") {
              <("SettingsTab")()(
                <.div(^.className := UserProfileInformationsStyles.slidingPannelButtonGroup)(
                  <.ButtonNavComponent(
                    ^.wrapped := ButtonNavProps(
                      onClickMethod = changeTab("proposals"),
                      icon = FontAwesomeStyles.angleLeft,
                      wording = I18n.t("user-profile.back-to-profile")
                    )
                  )()
                ),
                <.div(^.className := UserProfileInformationsStyles.slidingPannelButtonGroup)(
                  <.ButtonNavComponent(
                    ^.wrapped := ButtonNavProps(
                      onClickMethod = self.props.wrapped.logout,
                      icon = FontAwesomeStyles.signOut,
                      wording = I18n.t("user-profile.disconnect-cta")
                    )
                  )()
                )
              )
            } else {
              <("OtherTabs")()(
                <.div(^.className := UserProfileInformationsStyles.slidingPannelButtonGroup)(
                  <.ButtonNavComponent(
                    ^.wrapped := ButtonNavProps(
                      onClickMethod = changeTab("settings"),
                      icon = FontAwesomeStyles.pencil,
                      wording = I18n.t("user-profile.edit-profile")
                    )
                  )()
                ),
                <.div(
                  ^.className := js.Array(
                    UserProfileInformationsStyles.slidingPannelButtonGroup,
                    UserProfileInformationsStyles.slidingPannelMobileButtonGroup
                  )
                )(
                  <.ButtonNavComponent(
                    ^.wrapped := ButtonNavProps(
                      onClickMethod = changeTab("settings"),
                      icon = FontAwesomeStyles.cog,
                      wording = I18n.t("user-profile.manage-account")
                    )
                  )()
                ),
                <.div(^.className := UserProfileInformationsStyles.slidingPannelButtonGroup)(
                  <.ButtonNavComponent(
                    ^.wrapped := ButtonNavProps(
                      onClickMethod = self.props.wrapped.logout,
                      icon = FontAwesomeStyles.signOut,
                      wording = I18n.t("user-profile.disconnect-cta")
                    )
                  )()
                )
              )
            },
            <.style()(UserProfileInformationsStyles.render[String])
          )
        }
      )
}

object UserProfileInformationsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)",
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(padding(20.pxToEm()), marginTop(-49.pxToEm()))
    )

  val headerInfosWrapper: StyleA =
    style(display.flex, ThemeStyles.MediaQueries.beyondLargeMedium(flexFlow := s"column"))

  val avatarWrapper: StyleA =
    style(
      position.relative,
      width(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
      height(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
      marginTop(-40.pxToEm()),
      marginRight(ThemeStyles.SpacingValue.small.pxToEm()),
      overflow.hidden,
      backgroundColor(ThemeStyles.BackgroundColor.white),
      borderRadius(50.%%),
      border(2.px, solid, ThemeStyles.BorderColor.lighter),
      textAlign.center,
      ThemeStyles.MediaQueries.beyondLargeMedium(
        float.none,
        display.block,
        verticalAlign.middle,
        width(160.pxToEm()),
        height(160.pxToEm()),
        marginTop(-49.pxToEm()),
        borderWidth(5.px),
        marginLeft.auto,
        marginRight.auto
      )
    )

  val avatarPlaceholder: StyleA =
    style(
      width(100.%%),
      lineHeight(76.pxToEm(32)),
      fontSize(32.pxToEm()),
      color(ThemeStyles.TextColor.lighter),
      ThemeStyles.MediaQueries.beyondLargeMedium(lineHeight(150.pxToEm(64)), fontSize(64.pxToEm()))
    )

  val avatar: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      transform := s"translate(-50%, -50%)",
      width(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
      minWidth(100.%%),
      minHeight(100.%%),
      maxWidth.none,
      maxHeight.none,
      ThemeStyles.MediaQueries.beyondLargeMedium(width(160.pxToEm()))
    )

  val personnalInformations: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondLargeMedium(float.none),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(textAlign.center, marginTop(20.pxToEm()))
    )

  val noStyleFields: StyleA =
    style(width.auto, border.none, padding(`0`), margin(`0`))

  val userName: StyleA =
    style(
      TextStyles.title,
      fontSize(15.pxToEm()),
      lineHeight(1),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm()))
    )

  val basicInformations: StyleA =
    style(TextStyles.smallerText, color(ThemeStyles.TextColor.lighter))

  val marker: StyleA =
    style(marginRight(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val biography: StyleA =
    style(
      display.flex,
      transition := s"flex 0.25s ease-in",
      overflow.hidden,
      whiteSpace.preLine,
      ThemeStyles.MediaQueries.beyondLargeMedium(
        height.auto,
        paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
        paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()),
        borderTop(1.pxToEm(), solid, ThemeStyles.BorderColor.veryLight),
        borderBottom(1.pxToEm(), solid, ThemeStyles.BorderColor.veryLight),
        marginBottom(`0`)
      )
    )

  val slidingPannelButton: StyleA =
    style(
      ThemeStyles.Font.tradeGothicLTStd,
      color(ThemeStyles.ThemeColor.primary),
      fontSize(15.pxToEm()),
      lineHeight(17.pxToEm()),
      textTransform.uppercase,
      paddingTop(10.pxToEm(15)),
      paddingBottom(5.pxToEm(15)),
    )

  val slidingPannelSep: StyleA =
    style(height(1.pxToEm()), width(35.%%), backgroundColor(ThemeStyles.BorderColor.veryLight))

  val slidingPannelButtonGroup: StyleA =
    style(
      display.flex,
      justifyContent.spaceAround,
      backgroundColor(ThemeStyles.BackgroundColor.white),
      marginTop(20.pxToEm()),
    )

  val slidingPannelMobileButtonGroup: StyleA =
    style(ThemeStyles.MediaQueries.beyondLargeMedium(display.none))

}
