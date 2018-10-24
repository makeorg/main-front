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

package org.make.front.components.sequence.contents

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationExpanded => OperationModel, SequenceId => SequenceIdModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles, _}
import org.make.front.styles.utils._

import scala.scalajs.js

object PromptingToGoBackToOperationLight {

  final case class PromptingToGoBackToOperationLightProps(operation: OperationModel,
                                                          sequenceId: SequenceIdModel,
                                                          clickOnButtonHandler: () => Unit,
                                                          language: String,
                                                          country: String)

  final case class PromptingToGoBackToOperationLightState()

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[PromptingToGoBackToOperationLightProps, PromptingToGoBackToOperationLightState](
          displayName = "PromptingToGoBackToOperationLight",
          getInitialState = { _ =>
            PromptingToGoBackToOperationLightState()
          },
          render = { _ =>
            <.div(^.className := PromptingToGoBackToOperationLightStyles.finalCardWrapper)(
              <.div(
                ^.className := js.Array(LayoutRulesStyles.row, PromptingToGoBackToOperationLightStyles.introWrapper)
              )(
                <.p(
                  ^.className := js
                    .Array(PromptingToGoBackToOperationLightStyles.intro, TextStyles.bigText, TextStyles.boldText)
                )(unescape(I18n.t("sequence.prompting-to-continue.intro-light")))
              )
            )
          }
        )
    )
}

object PromptingToGoBackToOperationLightStyles extends StyleSheet.Inline {

  import dsl._

  val finalCardWrapper: StyleA =
    style(display.flex, flexFlow := "column", alignItems.center, justifyContent.center)

  val intro: StyleA =
    style(textAlign.center)

  val introWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()))
}
