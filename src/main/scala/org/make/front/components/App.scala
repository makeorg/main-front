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

package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.users.authenticate.TriggerSignUp.TriggerSignUpProps
import org.make.front.models.OperationList
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, RWDHideRulesStyles, TextStyles, _}
import org.make.front.styles.ui._

object App {

  final case class AppProps(language: String, country: String, nVotesTriggerConnexion: Int, operations: OperationList)

  final case class AppState(language: String, country: String)

  lazy val reactClass = WithRouter(
    React
      .createClass[AppProps, AppState](
        displayName = "App",
        getInitialState = { self =>
          AppState(self.props.wrapped.language, self.props.wrapped.country)
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(_.copy(language = props.wrapped.language, country = props.wrapped.country))
        },
        render = (self) => {
          <("app-container")(^.className := "App")(
            <.style()(
              Basic.render[String],
              LayoutRulesStyles.render[String],
              ColRulesStyles.render[String],
              FlexLayoutStyles.render[String],
              TableLayoutStyles.render[String],
              TableLayoutBeyondSmallStyles.render[String],
              TableLayoutBeyondMediumStyles.render[String],
              TextStyles.render[String],
              CTAStyles.render[String],
              InputStyles.render[String],
              TagStyles.render[String],
              TooltipStyles.render[String],
              AccordionStyles.render[String],
              AnimationsStyles.render[String],
              RWDRulesSmallStyles.render[String],
              RWDRulesMediumStyles.render[String],
              RWDRulesLargeMediumStyles.render[String],
              RWDHideRulesStyles.render[String]
            ),
            <.ContainerComponent.empty,
            <.TriggerSignUpComponent(
              ^.wrapped := TriggerSignUpProps(
                self.props.wrapped.nVotesTriggerConnexion,
                operations = self.props.wrapped.operations,
                language = self.state.language,
                country = self.state.country
              )
            )()
          )
        }
      )
  )
}
