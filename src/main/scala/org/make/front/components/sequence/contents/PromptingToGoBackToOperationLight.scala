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
            <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
              <.div(^.className := TableLayoutStyles.row)(
                <.div(^.className := TableLayoutStyles.cell)(
                  <.div(
                    ^.className := js.Array(LayoutRulesStyles.row, PromptingToGoBackToOperationLightStyles.introWrapper)
                  )(
                    <.p(
                      ^.className := js
                        .Array(PromptingToGoBackToOperationLightStyles.intro, TextStyles.bigText, TextStyles.boldText)
                    )(unescape(I18n.t("sequence.prompting-to-continue.intro-light")))
                  )
                )
              )
            )
          }
        )
    )
}

object PromptingToGoBackToOperationLightStyles extends StyleSheet.Inline {

  import dsl._

  val intro: StyleA =
    style(textAlign.center)

  val introWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()))
}
