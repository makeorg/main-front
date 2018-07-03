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

package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.OperationExpanded
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, RWDRulesLargeMediumStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object ConsultationCommunity {

  case class ConsultationCommunityProps(operation: OperationExpanded, language: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationCommunityProps, Unit](
        displayName = "ConsultationCommunity",
        render = self => {

          val consultation: OperationExpanded = self.props.wrapped.operation

          object DynamicConsultationCommunityStyles extends StyleSheet.Inline {
            import dsl._

            val consultationColor = style(color :=! consultation.color)

          }

          def openSlugLink: () => Unit = { () =>
            scalajs.js.Dynamic.global.window
              .open(
                I18n.t(
                  "operation.community.learn-more.link",
                  Replacements(("operation-slug", self.props.wrapped.operation.slug))
                ),
                "_blank"
              )
            TrackingService
              .track(
                "click-participate-community",
                TrackingContext(TrackingLocation.operationPage, operationSlug = Some(consultation.slug)),
                Map("sequenceId" -> consultation.landingSequenceId.value)
              )
          }

          def linkPartner =
            self.props.wrapped.operation.wordings
              .find(_.language == self.props.wrapped.language)
              .flatMap(_.learnMoreUrl)
              .map(_ + "#partenaires")
              .getOrElse("/#/404")

          def trackingPartners: () => Unit = { () =>
            TrackingService
              .track(
                "click-see-more-community",
                TrackingContext(TrackingLocation.operationPage, operationSlug = Some(consultation.slug))
              )
          }

          <.aside(^.className := js.Array(ConsultationCommunityStyles.wrapper, LayoutRulesStyles.centeredRow))(
            <.h3(^.className := ConsultationCommunityStyles.title)(
              unescape(I18n.t("operation.presentation.community"))
            ),
            <.p(^.className := js.Array(TextStyles.smallerText, ConsultationCommunityStyles.communityText))(
              unescape(I18n.t("operation.presentation.community-text"))
            ),
            <.div(^.className := ConsultationCommunityStyles.commitingWrapper)(
              <.button(
                ^.onClick := openSlugLink,
                ^.className := js.Array(
                  RWDRulesLargeMediumStyles.showBlockBeyondLargeMedium,
                  CTAStyles.basic,
                  CTAStyles.basicOnA,
                  ConsultationCommunityStyles.cta
                )
              )(
                <.i(^.className := js.Array(FontAwesomeStyles.play, ConsultationCommunityStyles.ctaIcon))(),
                unescape(I18n.t("operation.community.learn-more.label"))
              )
              /* Todo Counting citizens who participated to the operation
              <.div(^.className := js.Array(TextStyles.smallerText, ConsultationCommunityStyles.communityText))(
                <.p(^.className := js.Array(ConsultationCommunityStyles.titleAlt, DynamicConsultationCommunityStyles.consultationColor))(unescape(I18n.t("operation.community.citizen-count", Replacements(("count", "52341"))))),
                <.p()(unescape(I18n.t("operation.community.already-participate")))
              )*/
            ),
            <.hr(
              ^.className := js
                .Array(ConsultationPresentationStyles.sep, RWDRulesLargeMediumStyles.hideBeyondLargeMedium)
            )(),
            <.h3(^.className := ConsultationCommunityStyles.title)(
              unescape(I18n.t("operation.presentation.also"))
            ),
            <.p(^.className := js.Array(TextStyles.smallerText, ConsultationCommunityStyles.communityText))(
              unescape(I18n.t("operation.presentation.also-text"))
            ),
            <(self.props.wrapped.operation.partnersComponent).empty,
            <.a(
              ^.onClick := trackingPartners,
              ^.href := linkPartner,
              ^.className := js.Array(TextStyles.boldText, ConsultationCommunityStyles.communityLink),
              ^.target := "_blank"
            )(unescape(I18n.t("operation.community.partner.see-more"))),
            <.style()(ConsultationCommunityStyles.render[String], DynamicConsultationCommunityStyles.render[String])
          )

        }
      )

}

object ConsultationCommunityStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.white),
      padding(`0`,`0`, ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        padding(20.pxToEm(),`0`),
        marginTop(20.pxToEm()),
        boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
      )
    )

  val title: StyleA =
    style(
      TextStyles.title,
      fontSize(15.pxToEm()),
      lineHeight(1),
      padding(
        ThemeStyles.SpacingValue.small.pxToEm(15),
        ThemeStyles.SpacingValue.small.pxToEm(15),
        ThemeStyles.SpacingValue.smaller.pxToEm(15)
      ),
      ThemeStyles.MediaQueries.beyondSmall(
        fontSize(18.pxToEm())
      ),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        padding(`0`,`0`,15.pxToEm(18)),
        margin(`0`,20.pxToEm(18), ThemeStyles.SpacingValue.small.pxToEm(18)),
        borderBottom(1.pxToEm(18), solid, ThemeStyles.BorderColor.veryLight)
      )
    )

  val commitingWrapper: StyleA =
    style(
      display.flex,
      ThemeStyles.MediaQueries.beyondLargeMedium(
        padding(
          ThemeStyles.SpacingValue.small.pxToEm(),
          20.pxToEm(),
          ThemeStyles.SpacingValue.medium.pxToEm()
        )
      )
    )

  val cta: StyleA =
    style(ThemeStyles.MediaQueries.beyondLargeMedium(display.flex))

  val ctaIcon: StyleA =
    style(marginRight(ThemeStyles.SpacingValue.small.pxToEm()))

  val communityText: StyleA =
    style(
      display.flex,
      flexFlow := s"column",
      alignItems.flexStart,
      color(ThemeStyles.TextColor.lighter),
      paddingLeft(ThemeStyles.SpacingValue.small.pxToEm(13)),
      paddingRight(ThemeStyles.SpacingValue.small.pxToEm(13)),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        alignItems.center,
        paddingLeft(20.pxToEm(14)),
        paddingRight(20.pxToEm(14))
      )
    )

  val communityLink: StyleA =
    style(
      color(ThemeStyles.ThemeColor.primary),
      marginLeft(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        marginLeft(20.pxToEm(14))
      )
    )

  val sep: StyleA =
    style(
      width(100.%%),
      height(2.pxToEm()),
      backgroundColor(ThemeStyles.BorderColor.veryLight),
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm())
    )

}
