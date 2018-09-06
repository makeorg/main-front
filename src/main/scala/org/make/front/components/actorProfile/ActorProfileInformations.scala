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

package org.make.front.components.actorProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.ReactCSSTransition.{
  ReactCSSTransitionDOMAttributes,
  ReactCSSTransitionVirtualDOMElements,
  TransitionClasses
}
import org.make.front.facades.ReactTransition.ReactTransitionDOMAttributes
import org.make.front.models.{Organisation => OrganisationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object ActorProfileInformations {

  final case class ActorProfileInformationsProps(actor: OrganisationModel,
                                                 activeTab: String,
                                                 changeActiveTab: String => Unit)
  final case class ActorProfileInformationsState(expandSlidingPannel: Boolean, previousTab: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[ActorProfileInformationsProps, ActorProfileInformationsState](
        displayName = "ActorProfileInformations",
        getInitialState = self => {
          ActorProfileInformationsState(expandSlidingPannel = false, previousTab = self.props.wrapped.activeTab)
        },
        componentWillReceiveProps = { (self, nextProps) =>
          self.setState(
            ActorProfileInformationsState(
              expandSlidingPannel = self.state.expandSlidingPannel,
              previousTab = self.props.wrapped.activeTab
            )
          )
        },
        render = self => {
          val actor: OrganisationModel = self.props.wrapped.actor

          def changeTab(newTab: String): () => Unit = { () =>
            self.props.wrapped.changeActiveTab(newTab)
          }

          def backToPreviousTab: () => Unit = { () =>
            self.props.wrapped.changeActiveTab(self.state.previousTab)
          }

          // Toggle biography collapse method for "show more / show less" button
          def toggleSlidingPannel: () => Unit = { () =>
            self.setState(_.copy(expandSlidingPannel = !self.state.expandSlidingPannel))
          }

          <.div(^.className := ActorProfileInformationsStyles.wrapper)(
            <.div(^.className := ActorProfileInformationsStyles.avatarWrapper)(if (actor.avatarUrl.nonEmpty) {
              <.img(
                ^.src := actor.avatarUrl.get,
                ^.className := ActorProfileInformationsStyles.avatar,
                ^.alt := actor.organisationName.getOrElse(""),
                ^("data-pin-no-hover") := "true"
              )()
            } else {
              <.i(^.className := js.Array(ActorProfileInformationsStyles.avatarPlaceholder, FontAwesomeStyles.user))()
            }),
            <.div(^.className := ActorProfileInformationsStyles.personnalInformations)(
              actor.organisationName
                .orElse(actor.organisationName)
                .map { name =>
                  <.h1(^.className := ActorProfileInformationsStyles.actorName)(name)
                }
                .toSeq
            ),
            self.props.wrapped.actor.description.map { description =>
              <.CSSTransition(
                ^.timeout := 25,
                ^.in := self.state.expandSlidingPannel,
                ^.classNamesMap := TransitionClasses(
                  enterDone = ActorProfileInformationsStyles.expandTransitionOn.htmlClass
                )
              )(
                <.p(
                  ^.className := js
                    .Array(ActorProfileInformationsStyles.biography, ActorProfileInformationsStyles.basicInformations)
                )(description)
              )
            },
            <.div(^.className := RWDRulesLargeMediumStyles.hideBeyondLargeMedium)(
              <.CSSTransition(
                ^.timeout := 25,
                ^.in := !self.state.expandSlidingPannel,
                ^.classNamesMap := TransitionClasses(
                  enterDone = ActorProfileInformationsStyles.showGradient.htmlClass,
                  exitDone = ActorProfileInformationsStyles.hideGradient.htmlClass
                )
              )(<.div(^.className := ActorProfileInformationsStyles.slidingPannelGradient)()),
              <.div(^.className := ActorProfileInformationsStyles.slidingPannelHeader)(
                <.div(^.className := ActorProfileInformationsStyles.slidingPannelSep)(),
                <.button(
                  ^.className := ActorProfileInformationsStyles.slidingPannelButton,
                  ^.onClick := toggleSlidingPannel
                )(if (self.state.expandSlidingPannel) {
                  I18n.t("actor-profile.show-less")
                } else {
                  I18n.t("actor-profile.show-more")
                }),
                <.div(^.className := ActorProfileInformationsStyles.slidingPannelSep)()
              )
            ),
            <.style()(ActorProfileInformationsStyles.render[String])
          )
        }
      )
}

object ActorProfileInformationsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)",
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(padding(50.pxToEm()), marginTop(-49.pxToEm()))
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
        marginTop(-69.pxToEm()),
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

  val actorName: StyleA =
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
