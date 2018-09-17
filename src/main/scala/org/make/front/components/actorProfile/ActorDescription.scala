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

import scala.scalajs.js

object ActorDescription {

  final case class ActorDescriptionProps(actor: OrganisationModel)
  final case class ActorDescriptionState(expandSlidingPannel: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[ActorDescriptionProps, ActorDescriptionState](
        displayName = "UserDescription",
        getInitialState = _ => {
          ActorDescriptionState(expandSlidingPannel = false)
        },
        componentWillReceiveProps = { (self, _) =>
          self.setState(ActorDescriptionState(expandSlidingPannel = self.state.expandSlidingPannel))
        },
        render = self => {

          // Toggle biography collapse method for "show more / show less" button
          def toggleSlidingPannel: () => Unit = { () =>
            self.setState(_.copy(expandSlidingPannel = !self.state.expandSlidingPannel))
          }

          <("SlidingPannel")()(
            <.CSSTransition(
              ^.timeout := 25,
              ^.in := self.state.expandSlidingPannel,
              ^.classNamesMap := TransitionClasses(enterDone = ActorDescriptionStyles.expandTransitionOn.htmlClass)
            )(
              <.p(
                ^.className := js
                  .Array(
                    ActorProfileInformationsStyles.biography,
                    ActorDescriptionStyles.slidingBiography,
                    ActorProfileInformationsStyles.basicInformations
                  )
              )(self.props.wrapped.actor.description)
            ),
            <.div(^.className := RWDRulesLargeMediumStyles.hideBeyondLargeMedium)(
              <.CSSTransition(
                ^.timeout := 25,
                ^.in := !self.state.expandSlidingPannel,
                ^.classNamesMap := TransitionClasses(
                  enterDone = ActorDescriptionStyles.showGradient.htmlClass,
                  exitDone = ActorDescriptionStyles.hideGradient.htmlClass
                )
              )(<.div(^.className := ActorDescriptionStyles.slidingPannelGradient)()),
              <.div(^.className := ActorDescriptionStyles.slidingPannelHeader)(
                <.div(^.className := ActorProfileInformationsStyles.slidingPannelSep)(),
                <.button(
                  ^.className := ActorProfileInformationsStyles.slidingPannelButton,
                  ^.onClick := toggleSlidingPannel
                )(if (self.state.expandSlidingPannel) {
                  I18n.t("user-profile.show-less")
                } else {
                  I18n.t("user-profile.show-more")
                }),
                <.div(^.className := ActorProfileInformationsStyles.slidingPannelSep)()
              )
            ),
            <.style()(ActorDescriptionStyles.render[String])
          )
        }
      )
}

object ActorDescriptionStyles extends StyleSheet.Inline {

  import dsl._

  val slidingBiography: StyleA =
    style(height(50.pxToEm()), marginBottom(-25.pxToEm()))

  val slidingPannelGradient: StyleA =
    style(
      position.relative,
      height(25.pxToEm()),
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
