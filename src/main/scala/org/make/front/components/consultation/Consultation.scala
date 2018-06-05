package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.consultation.ConsultationHeader.ConsultationHeaderProps
import org.make.front.components.consultation.ConsultationLinkSequence.ConsultationLinkSequenceProps
import org.make.front.components.consultation.ConsultationPresentation.ConsultationPresentationProps
import org.make.front.components.consultation.ConsultationPresentationMobile.ConsultationPresentationMobileProps
import org.make.front.components.consultation.ConsultationProposal.ConsultationProposalProps
import org.make.front.components.consultation.ResultsInConsultationContainer.ResultsInConsultationContainerProps
import org.make.front.facades.DeviceDetect
import org.make.front.models.{OperationWording, Location => LocationModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.RWDHideRulesStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object Consultation {

  final case class ConsultationProps(operation: OperationModel,
                                     countryCode: String,
                                     language: String,
                                     onWillMount: () => Unit)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationProps, Unit](
        displayName = "Consultation",
        componentWillMount = { self =>
          self.props.wrapped.onWillMount()
        },
        componentDidMount = { self =>
          TrackingService
            .track(
              "display-page-operation",
              TrackingContext(TrackingLocation.operationPage, operationSlug = Some(self.props.wrapped.operation.slug)),
              Map("id" -> self.props.wrapped.operation.operationId.value)
            )
        },
        render = self => {
          val consultation: OperationModel = self.props.wrapped.operation
          val maybeWording: Option[OperationWording] =
            consultation.getWordingByLanguage(self.props.wrapped.language)
          val maybePresentationComponent: Option[ReactElement] = maybeWording.flatMap { wording =>
            wording.presentation.map { content =>
              if (DeviceDetect.isMobileOnly) {
                <.ConsultationPresentationMobileComponent(
                  ^.wrapped := ConsultationPresentationMobileProps(
                    content = content,
                    learnMoreUrl = wording.learnMoreUrl
                  )
                )()
              } else {
                <.ConsultationPresentationComponent(
                  ^.wrapped := ConsultationPresentationProps(content = content, learnMoreUrl = wording.learnMoreUrl)
                )()
              }
            }
          }

          if (consultation.isActive) {
            <("consultation")()(
              <.div(^.className := ConsultationStyles.mainHeaderWrapper)(
                <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty),
                <.div(^.className := ConsultationStyles.fixedMainHeaderWrapper)(
                  <.CookieAlertContainerComponent.empty,
                  <.MainHeaderContainer.empty
                )
              ),
              <.ConsultationHeaderComponent(
                ^.wrapped := ConsultationHeaderProps(consultation, self.props.wrapped.language)
              )(),
              <.section(^.className := ConsultationStyles.mainContentWrapper)(
                <.ConsultationProposalComponent(
                  ^.wrapped := ConsultationProposalProps(
                    consultation,
                    maybeLocation = Some(LocationModel.OperationPage(consultation.operationId)),
                    language = self.props.wrapped.language
                  )
                )(),
                <.ResultsInConsultationContainerComponent(
                  ^.wrapped := ResultsInConsultationContainerProps(
                    currentConsultation = consultation,
                    maybeLocation = Some(LocationModel.OperationPage(consultation.operationId))
                  )
                )()
              ),
              <.ConsultationLinkSequenceComponent(
                ^.wrapped := ConsultationLinkSequenceProps(
                  operation = consultation,
                  country = self.props.wrapped.countryCode
                )
              )(),
              maybePresentationComponent,
              <.style()(ConsultationStyles.render[String])
            )
          } else {
            <.div.empty
          }
        }
      )
}

object ConsultationStyles extends StyleSheet.Inline {

  import dsl._

  val mainHeaderWrapper: StyleA = style(
    paddingBottom(50.pxToEm()),
    ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
  )

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

  val contentWrapper: StyleA =
    style(display.block, backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

  val mainContentWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.grey), paddingTop(20.pxToEm()), paddingBottom(20.pxToEm()))
}
