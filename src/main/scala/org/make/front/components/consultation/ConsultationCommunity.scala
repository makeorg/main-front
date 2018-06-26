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
            <.h3(^.className := js.Array(TextStyles.smallerTitle, ConsultationCommunityStyles.title))(
              unescape(I18n.t("operation.presentation.community"))
            ),
            <.p(^.className := js.Array(TextStyles.smallerText, ConsultationCommunityStyles.communityText))(
              unescape(I18n.t("operation.presentation.community-text"))
            ),
            <.div (^.className := ConsultationCommunityStyles.commitingWrapper)(
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
              ),
              <.div(^.className := js.Array(TextStyles.smallerText, ConsultationCommunityStyles.communityText))(
                //                todo count citizen who participate
                <.p(^.className := js.Array(TextStyles.smallerTitle, DynamicConsultationCommunityStyles.consultationColor))(unescape(I18n.t("operation.community.citizen-count", Replacements(("count", "52341"))))),
                <.p()(unescape(I18n.t("operation.community.already-participate")))
              )
            ),
            <.h3(^.className := js.Array(TextStyles.smallerTitle, ConsultationCommunityStyles.title))(
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
      padding(`0`, ThemeStyles.SpacingValue.small.pxToEm(), ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        paddingTop(25.pxToEm()),
        paddingBottom(20.pxToEm()),
        marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
        boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
      )
    )

  val title: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.smaller.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        paddingTop(`0`),
        marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
        borderBottom(1.pxToEm(), solid, ThemeStyles.BorderColor.veryLight)
      )
    )

  val commitingWrapper: StyleA =
    style(
      display.flex,
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.medium.pxToEm())
    )

  val cta: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondLargeMedium(
        display.flex
      )
    )

  val ctaIcon: StyleA =
    style(marginRight(ThemeStyles.SpacingValue.small.pxToEm()))

  val communityText: StyleA =
    style(
      display.flex,
      flexFlow := s"column",
      alignItems.center,
      color(ThemeStyles.TextColor.lighter),
      paddingLeft(ThemeStyles.SpacingValue.smaller.pxToEm()),
      paddingRight(ThemeStyles.SpacingValue.smaller.pxToEm())
    )

  val communityLink: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

}
