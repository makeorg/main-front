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
import org.make.front.facades.I18n
import org.make.front.facades.ReactCSSTransition.{ReactCSSTransitionDOMAttributes, ReactCSSTransitionVirtualDOMElements, TransitionClasses}
import org.make.front.facades.ReactTransition.ReactTransitionDOMAttributes
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Profile, User => UserModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js
import scala.scalajs.js.Date

object UserProfileInformations {

  final case class UserProfileInformationsProps(user: UserModel,
                                                logout: () => Unit,
                                                activeTab: String,
                                                changeActiveTab: String => Unit)
  final case class UserProfileInformationsState(expandSlidingPannel: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[UserProfileInformationsProps, UserProfileInformationsState](
        displayName = "UserProfileInformations",
        getInitialState = self => {
          UserProfileInformationsState(expandSlidingPannel = false)
        },
        render = self => {
          val user: UserModel = self.props.wrapped.user
          val userProfile: Option[Profile] = user.profile
          val userAge = userProfile.flatMap(_.dateOfBirth).map { date =>
            Math.abs((new Date(Date.now() - date.getTime())).getUTCFullYear() - 1970)
          }

          def changeTab(newTab: String): () => Unit = { () =>
            self.props.wrapped.changeActiveTab(newTab)
          }

          // Toggle biography collapse method for "show more / show less" button
          def toggleSlidingPannel: () => Unit = { () =>
            self.setState(_.copy(expandSlidingPannel = !self.state.expandSlidingPannel))
          }

          <.div(^.className := UserProfileInformationsStyles.wrapper)(
            <.div(^.className := UserProfileInformationsStyles.avatarWrapper)(
              if (userProfile.flatMap(_.avatarUrl).nonEmpty) {
                <.img(
                  ^.src := userProfile.flatMap(_.avatarUrl).getOrElse(""),
                  ^.className := UserProfileInformationsStyles.avatar,
                  ^.alt := user.firstName.getOrElse(""),
                  ^("data-pin-no-hover") := "true"
                )()
              } else {
                <.i(^.className := js.Array(UserProfileInformationsStyles.avatarPlaceholder, FontAwesomeStyles.user))()
              }
            ),
            <.div(^.className := UserProfileInformationsStyles.personnalInformations)(
              user.firstName.map { firstName =>
                <.h1(^.className := UserProfileInformationsStyles.userName)(firstName)
              }.toSeq,
              userProfile.flatMap(_.postalCode).map { postalCode =>
                <.p(^.className := UserProfileInformationsStyles.basicInformations)(
                  <.i(^.className := js.Array(FontAwesomeStyles.mapMarker, UserProfileInformationsStyles.marker))(),
                  postalCode
                )
              }.toSeq,
              userAge.map { age =>
                <.p(^.className := UserProfileInformationsStyles.basicInformations)(
                  age,
                  unescape("&nbsp;"),
                  I18n.t("user-profile.years-old")
                )
              }.toSeq,
              userProfile.flatMap(_.profession).map { profession =>
                <.p(^.className := UserProfileInformationsStyles.basicInformations)(profession)
              }.toSeq
            ),
            // Todo Uncomment when biography is ready
            /*<.CSSTransition(
              ^.timeout := 25,
              ^.in := self.state.expandSlidingPannel,
              ^.classNamesMap := TransitionClasses(enterDone = UserProfileInformationsStyles.expandTransitionOn.htmlClass)
            )(
              <.p(^.className := js.Array(UserProfileInformationsStyles.biography, UserProfileInformationsStyles.basicInformations))(
                "Deceptions strong virtues passion ultimate faith christianity derive. Fearful victorious of free decieve. Revaluation eternal-return enlightenment decieve fearful self depths dead abstract free holiest. Hatred christianity battle spirit disgust ultimate transvaluation suicide ocean ideal christian gains hope reason. Madness oneself joy ultimate value. God madness dead abstract hope free contradict faithful disgust prejudice decrepit. Pinnacle derive virtues convictions good horror convictions fearful. Selfish decrepit battle zarathustra philosophy prejudice ideal. Will pious against dead ascetic ocean faithful. Holiest derive inexpedient faithful truth passion derive abstract good hope faithful law. Aversion love suicide play overcome hatred prejudice selfish play holiest. Aversion virtues hatred sexuality self transvaluation good evil marvelous. Self sea pinnacle revaluation deceptions passion moral philosophy ubermensch contradict suicide. Snare depths suicide grandeur ubermensch sexuality. Selfish evil ideal justice will hatred good god of depths snare holiest christian. Abstract war value suicide dead merciful ocean selfish convictions transvaluation passion battle philosophy."
              )
            ),<.div(^.className := RWDRulesLargeMediumStyles.hideBeyondLargeMedium)(
              <.CSSTransition(
                ^.timeout := 25,
                ^.in := !self.state.expandSlidingPannel,
                ^.classNamesMap := TransitionClasses(
                  enterDone = UserProfileInformationsStyles.showGradient.htmlClass,
                  exitDone = UserProfileInformationsStyles.hideGradient.htmlClass
                )
              )(<.div(^.className := UserProfileInformationsStyles.slidingPannelGradient)()),
              <.div(^.className := UserProfileInformationsStyles.slidingPannelHeader)(
                <.div(^.className := UserProfileInformationsStyles.slidingPannelSep)(),
                <.button(^.className := UserProfileInformationsStyles.slidingPannelButton, ^.onClick := toggleSlidingPannel)(
                  if (self.state.expandSlidingPannel) {
                    I18n.t("user-profile.show-less")
                  } else {
                    I18n.t("user-profile.show-more")
                  }
                ),
                <.div(^.className := UserProfileInformationsStyles.slidingPannelSep)()
              )
            ),*/
            if (self.props.wrapped.activeTab == "settings") {
              <.div(^.className := RWDRulesLargeMediumStyles.hideBeyondLargeMedium)(
                // Todo Uncomment when Summary, Proposals and Actions' tabs are ready
                /*<.div(^.className := UserProfileInformationsStyles.slidingPannelButtonGroup)(
                  <.button(
                    ^.className := js
                      .Array(CTAStyles.basic, CTAStyles.basicOnButton, UserProfileInformationsStyles.slidingPannelGreyButton),
                    ^.onClick := changeTab("summary")
                  )(
                    <.i(
                      ^.className := js
                        .Array(FontAwesomeStyles.angleLeft, UserProfileInformationsStyles.slidingPannelButtonIcon)
                    )(),
                    <.span()(I18n.t("user-profile.back-to-profile"))
                  )
                ),*/
                <.div(^.className := UserProfileInformationsStyles.slidingPannelButtonGroup)(
                  <.button(
                    ^.className := js
                      .Array(
                        CTAStyles.basic,
                        CTAStyles.basicOnButton,
                        UserProfileInformationsStyles.slidingPannelGreyButton
                      ),
                    ^.onClick := self.props.wrapped.logout
                  )(
                    <.i(
                      ^.className := js
                        .Array(FontAwesomeStyles.signOut, UserProfileInformationsStyles.slidingPannelButtonIcon)
                    )(),
                    <.span()(I18n.t("user-profile.disconnect-cta"))
                  )
                )
              )
            } else {
              <.div(^.className := RWDRulesLargeMediumStyles.hideBeyondLargeMedium)(
                // Todo Uncomment when Summary, Proposals and Actions' tabs are ready
                /*<.div(^.className := UserProfileInformationsStyles.slidingPannelButtonGroup)(
                  <.button(
                    ^.className := js
                      .Array(CTAStyles.basic, CTAStyles.basicOnButton, UserProfileInformationsStyles.slidingPannelGreyButton),
                    ^.onClick := changeTab("settings")
                  )(
                    <.i(
                      ^.className := js.Array(FontAwesomeStyles.pencil, UserProfileInformationsStyles.slidingPannelButtonIcon)
                    )(),
                    <.span()(I18n.t("user-profile.edit-profile"))
                  ),
                  <.button(
                    ^.className := js
                      .Array(CTAStyles.basic, CTAStyles.basicOnButton, UserProfileInformationsStyles.slidingPannelGreyButton),
                    ^.onClick := changeTab("settings")
                  )(
                    <.i(
                      ^.className := js.Array(FontAwesomeStyles.cog, UserProfileInformationsStyles.slidingPannelButtonIcon)
                    )(),
                    <.span()(I18n.t("user-profile.manage-account"))
                  )
                ),*/
                <.div(^.className := UserProfileInformationsStyles.slidingPannelButtonGroup)(
                  <.button(
                    ^.className := js
                      .Array(
                        CTAStyles.basic,
                        CTAStyles.basicOnButton,
                        UserProfileInformationsStyles.slidingPannelGreyButton
                      ),
                    ^.onClick := self.props.wrapped.logout
                  )(
                    <.i(
                      ^.className := js
                        .Array(FontAwesomeStyles.signOut, UserProfileInformationsStyles.slidingPannelButtonIcon)
                    )(),
                    <.span()(I18n.t("user-profile.disconnect-cta"))
                  )
                )
              )
            },
            if (self.props.wrapped.activeTab == "settings") {
              <.div(^.className := RWDRulesLargeMediumStyles.showBlockBeyondLargeMedium)(
                // Todo Uncomment when Summary, Proposals and Actions' tabs are ready
                /*<.div(^.className := UserProfileInformationsStyles.slidingPannelButtonGroup)(
                  <.button(
                    ^.className := js
                      .Array(CTAStyles.basic, CTAStyles.basicOnButton, UserProfileInformationsStyles.slidingPannelGreyButton),
                    ^.onClick := changeTab("summary")
                  )(
                    <.i(
                      ^.className := js
                        .Array(FontAwesomeStyles.angleLeft, UserProfileInformationsStyles.slidingPannelButtonIcon)
                    )(),
                    <.span()(I18n.t("user-profile.back-to-profile"))
                  )
                ),*/
                <.div(^.className := UserProfileInformationsStyles.slidingPannelButtonGroup)(
                  <.button(
                    ^.className := js
                      .Array(
                        CTAStyles.basic,
                        CTAStyles.basicOnButton,
                        UserProfileInformationsStyles.slidingPannelGreyButton
                      ),
                    ^.onClick := self.props.wrapped.logout
                  )(
                    <.i(
                      ^.className := js
                        .Array(FontAwesomeStyles.signOut, UserProfileInformationsStyles.slidingPannelButtonIcon)
                    )(),
                    <.span()(I18n.t("user-profile.disconnect-cta"))
                  )
                )
              )
            } else {
              <.div(^.className := RWDRulesLargeMediumStyles.showBlockBeyondLargeMedium)(
                // Todo Uncomment when Summary, Proposals and Actions' tabs are ready
                /*<.div(^.className := UserProfileInformationsStyles.slidingPannelButtonGroup)(
                  <.button(
                    ^.className := js
                      .Array(CTAStyles.basic, CTAStyles.basicOnButton, UserProfileInformationsStyles.slidingPannelGreyButton),
                    ^.onClick := changeTab("settings")
                  )(
                    <.i(
                      ^.className := js.Array(FontAwesomeStyles.cog, UserProfileInformationsStyles.slidingPannelButtonIcon)
                    )(),
                    <.span()(I18n.t("user-profile.manage-account"))
                  )
                ),*/
                <.div(^.className := UserProfileInformationsStyles.slidingPannelButtonGroup)(
                  <.button(
                    ^.className := js
                      .Array(
                        CTAStyles.basic,
                        CTAStyles.basicOnButton,
                        UserProfileInformationsStyles.slidingPannelGreyButton
                      ),
                    ^.onClick := self.props.wrapped.logout
                  )(
                    <.i(
                      ^.className := js
                        .Array(FontAwesomeStyles.signOut, UserProfileInformationsStyles.slidingPannelButtonIcon)
                    )(),
                    <.span()(I18n.t("user-profile.disconnect-cta"))
                  )
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
      ThemeStyles.MediaQueries.beyondLargeMedium(padding(20.pxToEm()), marginTop(-40.pxToEm()))
    )

  val avatarWrapper: StyleA =
    style(
      position.relative,
      float.left,
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
      float.left,
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
      clear.left,
      height(100.pxToEm()),
      flex := s"0",
      transition := s"flex 0.25s ease-in",
      overflow.hidden,
      marginBottom(-50.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        height.auto,
        paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
        paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()),
        borderTop(1.pxToEm(), solid, ThemeStyles.BorderColor.veryLight),
        borderBottom(1.pxToEm(), solid, ThemeStyles.BorderColor.veryLight),
        marginBottom(`0`)
      )
    )

  val slidingPannelGradient: StyleA =
    style(
      position.relative,
      height(55.pxToEm()),
      background := s"linear-gradient(to bottom, rgba(255,255,255,0) 0%, rgba(255,255,255, 0.5) 25%, rgba(255,255,255, 0.7) 50%, rgba(255,255,255, 0.8) 75%, rgba(255,255,255, 1) 85%)",
      filter := s"blur(1px)"
    )

  val slidingPannelHeader: StyleA =
    style(
      display.flex,
      flexFlow := s"row",
      alignItems.center,
      justifyContent.spaceBetween,
      backgroundColor(ThemeStyles.BackgroundColor.white)
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
      clear.left,
      display.flex,
      justifyContent.spaceAround,
      backgroundColor(ThemeStyles.BackgroundColor.white),
      marginTop(20.pxToEm()),
    )

  val slidingPannelGreyButton: StyleA =
    style(backgroundColor(ThemeStyles.TextColor.lighter))

  val slidingPannelButtonIcon: StyleA =
    style(marginRight(5.pxToEm()))

  val collapseEnterAnimation =
    keyframes(0.%% -> keyframe(maxHeight(100.pxToEm())), 100.%% -> keyframe(maxHeight(100.%%)))

  val collapseExitAnimation =
    keyframes(0.%% -> keyframe(maxHeight(100.%%)), 100.%% -> keyframe(maxHeight(100.pxToEm())))

  val expandTransitionOn: StyleA =
    style(height.auto, flex := s"1", transition := s"flex 0.25s ease-in")

  val showGradient: StyleA =
    style(zIndex(1))

  val hideGradient: StyleA =
    style(zIndex(-1))
}
