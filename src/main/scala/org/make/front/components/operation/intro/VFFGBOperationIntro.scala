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
import org.make.front.facades.{facebookLogo, keringFoundationLogo, I18n, VFFGBWhiteLogo, VFFIll, VFFIll2x}
import org.make.front.models.{
  GradientColor     => GradientColorModel,
  OperationExpanded => OperationModel,
  OperationPartner  => OperationPartnerModel
}
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object VFFGBOperationIntro {

  case class VFFGBOperationIntroProps(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[VFFGBOperationIntroProps, Unit](
        displayName = "VFFGBOperationIntro",
        render = (self) => {

          def onClick: () => Unit = { () =>
            TrackingService.track(
              "click-button-learn-more",
              TrackingContext(TrackingLocation.operationPage, Some(self.props.wrapped.operation.slug))
            )
          }

          val operation: OperationModel =
            self.props.wrapped.operation

          val partners = js.Array(
            OperationPartnerModel(
              name = "Kering Foundation",
              imageUrl = keringFoundationLogo.toString,
              imageWidth = 80,
              isFounder = true
            ),
            OperationPartnerModel(
              name = "Facebook",
              imageUrl = facebookLogo.toString,
              imageWidth = 80,
              isFounder = true
            )
          )

          val gradientValues: GradientColorModel =
            operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          object DynamicVFFGBOperationIntroStyles extends StyleSheet.Inline {

            import dsl._

            val gradient: StyleA =
              style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          <.div(^.className := js.Array(OperationIntroStyles.wrapper, DynamicVFFGBOperationIntroStyles.gradient))(
            <.div(^.className := js.Array(OperationIntroStyles.headingWrapper, LayoutRulesStyles.centeredRow))(
              <.div(^.className := VFFGBOperationIntroStyles.logoWrapper)(
                <.p(^.className := OperationIntroStyles.labelWrapper)(
                  <.span(^.className := TextStyles.label)(unescape(I18n.t("operation.vff-gb.intro.label")))
                ),
                <.img(^.src := VFFGBWhiteLogo.toString, ^.alt := unescape(I18n.t("operation.vff-gb.intro.title")))()
              ),
              <.div(^.className := js.Array(TableLayoutStyles.wrapper, OperationIntroStyles.separator))(
                <.div(
                  ^.className := js
                    .Array(TableLayoutStyles.cellVerticalAlignMiddle, OperationIntroStyles.separatorLineWrapper)
                )(
                  <.hr(
                    ^.className := js
                      .Array(OperationIntroStyles.separatorLine, OperationIntroStyles.separatorLineToTheLeft)
                  )()
                ),
                <.div(^.className := js.Array(TableLayoutStyles.cell, OperationIntroStyles.separatorTextWrapper))(
                  <.p(^.className := js.Array(OperationIntroStyles.separatorText, TextStyles.smallerText))(
                    unescape(I18n.t("operation.vff-gb.intro.partners.intro"))
                  )
                ),
                <.div(
                  ^.className := js
                    .Array(TableLayoutStyles.cellVerticalAlignMiddle, OperationIntroStyles.separatorLineWrapper)
                )(
                  <.hr(
                    ^.className := js
                      .Array(OperationIntroStyles.separatorLine, OperationIntroStyles.separatorLineToTheRight)
                  )()
                )
              ),
              <.ul(^.className := OperationIntroStyles.partnersList)(
                partners
                  .map(
                    partner =>
                      <.li(^.className := OperationIntroStyles.partnerItem)(
                        <.img(
                          ^.src := partner.imageUrl,
                          ^.alt := partner.name,
                          ^("width") := partner.imageWidth.toString,
                          ^.className := OperationIntroStyles.partnerLogo
                        )()
                    )
                  )
                  .toSeq
              )
            ),
            <.div(^.className := OperationIntroStyles.explanationWrapper)(
              <.div(^.className := LayoutRulesStyles.narrowerCenteredRowWithCols)(
                <.div(^.className := js.Array(ColRulesStyles.col, ColRulesStyles.colThirdBeyondSmall))(
                  <.img(
                    ^.src := VFFIll.toString,
                    ^("srcset") := VFFIll.toString + " 1x," + VFFIll2x.toString + " 2x",
                    ^.alt := unescape(I18n.t("operation.vff-gb.intro.title")),
                    ^.className := OperationIntroStyles.explanationIll
                  )()
                ),
                <.div(^.className := js.Array(ColRulesStyles.col, ColRulesStyles.colTwoThirdsBeyondSmall))(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("operation.vff-gb.intro.article.title"))),
                  <.div(^.className := OperationIntroStyles.explanationTextWrapper)(
                    <.p(^.className := js.Array(OperationIntroStyles.explanationText, TextStyles.smallText))(
                      unescape(I18n.t("operation.vff-gb.intro.article.text"))
                    )
                  ),
                  <.p(^.className := OperationIntroStyles.ctaWrapper)(
                    <.a(
                      ^.onClick := onClick,
                      ^.href := unescape(I18n.t("operation.vff-gb.intro.article.see-more.link")),
                      ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA),
                      ^.target := "_blank"
                    )(unescape(I18n.t("operation.vff-gb.intro.article.see-more.label")))
                  )
                )
              )
            ),
            <.style()(
              OperationIntroStyles.render[String],
              VFFGBOperationIntroStyles.render[String],
              DynamicVFFGBOperationIntroStyles.render[String]
            )
          )
        }
      )

}

object VFFGBOperationIntroStyles extends StyleSheet.Inline {

  import dsl._

  val logoWrapper: StyleA = style(maxWidth(417.pxToEm()), marginLeft.auto, marginRight.auto)

}
