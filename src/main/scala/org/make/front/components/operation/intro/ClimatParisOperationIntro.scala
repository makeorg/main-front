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

package org.make.front.components.operation.intro

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{
  climatParisIll,
  climatParisIll2x,
  climatParisIllMedium,
  climatParisIllMedium2x,
  climatParisIllSmall,
  climatParisIllSmall2x,
  climatParisLogoWhite,
  I18n
}
import org.make.front.models.{GradientColor => GradientColorModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object ClimatParisOperationIntro {

  case class ClimatParisOperationIntroProps(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[ClimatParisOperationIntroProps, Unit](
        displayName = "ClimatParisOperationIntro",
        render = (self) => {

          def onClick: () => Unit = { () =>
            TrackingService.track(
              "click-button-learn-more",
              TrackingContext(TrackingLocation.operationPage, Some(self.props.wrapped.operation.slug))
            )
          }

          val operation: OperationModel =
            self.props.wrapped.operation

          val gradientValues: GradientColorModel =
            operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          object DynamicClimatParisOperationIntroStyles extends StyleSheet.Inline {

            import dsl._

            val gradient: StyleA =
              style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          <.div(
            ^.className := js.Array(
              OperationIntroStyles.wrapper,
              ClimatParisOperationIntroStyles.wrapper,
              DynamicClimatParisOperationIntroStyles.gradient
            )
          )(
            <.img(
              ^.className := ClimatParisOperationIntroStyles.illustration,
              ^.src := climatParisIll.toString,
              ^("srcset") := climatParisIllSmall.toString + " 400w, " + climatParisIllSmall2x.toString + " 800w, " + climatParisIllMedium.toString + " 840w, " + climatParisIllMedium2x.toString + " 1680w, " + climatParisIll.toString + " 1350w, " + climatParisIll2x.toString + " 2700w",
              ^.alt := I18n.t("operation.climatparis.intro.title"),
              ^("data-pin-no-hover") := "true"
            )(),
            <.p(
              ^.className := js.Array(
                OperationIntroStyles.headingWrapper,
                ClimatParisOperationIntroStyles.logoWrapper,
                LayoutRulesStyles.centeredRow
              )
            )(
              <.img(
                ^.src := climatParisLogoWhite.toString,
                ^.alt := unescape(I18n.t("operation.climatparis.intro.title"))
              )()
            ),
            <.div(
              ^.className := js
                .Array(OperationIntroStyles.explanationWrapper, ClimatParisOperationIntroStyles.explanationWrapper)
            )(
              <.div(^.className := LayoutRulesStyles.narrowerCenteredRow)(
                <.p(^.className := TextStyles.label)(unescape(I18n.t("operation.climatparis.intro.article.title"))),
                <.div(^.className := OperationIntroStyles.explanationTextWrapper)(
                  <.p(^.className := js.Array(OperationIntroStyles.explanationText, TextStyles.smallText))(
                    unescape(I18n.t("operation.climatparis.intro.article.text"))
                  )
                ),
                <.p(^.className := OperationIntroStyles.ctaWrapper)(
                  <.a(
                    ^.onClick := onClick,
                    ^.href := unescape(I18n.t("operation.climatparis.intro.article.see-more.link")),
                    ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA),
                    ^.target := "_blank"
                  )(unescape(I18n.t("operation.climatparis.intro.article.see-more.label")))
                )
              )
            ),
            <.style()(
              OperationIntroStyles.render[String],
              ClimatParisOperationIntroStyles.render[String],
              DynamicClimatParisOperationIntroStyles.render[String]
            )
          )
        }
      )
}

object ClimatParisOperationIntroStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA = style(position.relative, overflow.hidden)

  val illustration: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      height.auto,
      maxHeight.none,
      minHeight(100.%%),
      width.auto,
      maxWidth.none,
      minWidth(100.%%),
      transform := s"translate(-50%, -50%)",
      opacity(0.7),
      mixBlendMode := "multiply"
    )

  val logoWrapper: StyleA =
    style(textAlign.center)

  val explanationWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.blackTransparent))

}
