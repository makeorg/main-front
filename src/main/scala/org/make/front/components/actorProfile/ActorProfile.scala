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
import org.make.front.components.actorProfile.ActorProfileInformations.ActorProfileInformationsProps
import org.make.front.components.actorProfile.ActorProfileProposalsContainer.ActorProposalsContainerProps
import org.make.front.components.actorProfile.navActorProfile.ActorTabNav.ActorTabNavProps
import org.make.front.models.{Organisation => OrganisationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{RWDHideRulesStyles, RWDRulesLargeMediumStyles}
import org.make.front.styles.utils._

object ActorProfile {

  final case class ActorProfileProps(actor: OrganisationModel, activeTab: String = "proposals")
  final case class ActorProfileState(activeTab: String)

  val reactClass: ReactClass =
    React
      .createClass[ActorProfileProps, ActorProfileState](
        displayName = "ActorProfile",
        getInitialState = self => ActorProfileState(self.props.wrapped.activeTab),
        render = self => {
          def changeActiveTab: String => Unit = { newTab =>
            self.setState(ActorProfileState(newTab))
          }

          <("ActorProfile")()(
            <.div(^.className := ActorProfileStyles.specialColorWrapper)(
              <.div(^.className := ActorProfileStyles.regularColorWrapper)(
                <.section(^.className := ActorProfileStyles.wrapper)(
                  <.aside(^.className := ActorProfileStyles.sidebar)(
                    <.ActorProfileInformationsComponent(
                      ^.wrapped := ActorProfileInformationsProps(
                        actor = self.props.wrapped.actor,
                        activeTab = self.state.activeTab,
                        changeActiveTab = changeActiveTab
                      )
                    )(),
                    <.div(^.className := RWDRulesLargeMediumStyles.showBlockBeyondLargeMedium)(
                      // Todo Uncomment when "Follow" Feature is ready
                      // <.ShareActorProfileComponent.empty,
                      <.AltFooterComponent()()
                    )
                  ),
                  <.div(^.className := ActorProfileStyles.main)(
                    <.ActorTabNavComponent(
                      ^.wrapped := ActorTabNavProps(activeTab = self.state.activeTab, changeActiveTab = changeActiveTab)
                    )(),
                    if (self.state.activeTab == "proposals") {
                      <.ActorProfileProposalsContainerComponent(
                        ^.wrapped := ActorProposalsContainerProps(actor = self.props.wrapped.actor)
                      )()
                    }
                    /*
                    //TODO uncomment for #330
                    else if (self.state.activeTab == "contributions") {
                      <.div()(")
                    }*/
                  ),
                  <.style()(ActorProfileStyles.render[String])
                )
              )
            ),
            <.div(^.className := RWDRulesLargeMediumStyles.hideBeyondLargeMedium)(<.MainFooterComponent.empty)
          )
        }
      )
}

object ActorProfileStyles extends StyleSheet.Inline {

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
    style(
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        minHeight :=! s"calc(100vh - ${ThemeStyles.SpacingValue.evenLarger.pxToEm().value} - ${ThemeStyles.mainNavDefaultHeight.value})"
      )
    )

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
    style(
      ThemeStyles.MediaQueries
        .beyondLargeMedium(width(100.%%), maxWidth(750.pxToPercent(1140)), marginLeft(30.pxToPercent(1140)))
    )

  val sidebar: StyleA =
    style(ThemeStyles.MediaQueries.beyondLargeMedium(maxWidth(360.pxToPercent(1140))))
}
