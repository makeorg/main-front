package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.Components._
import org.make.front.components.consultation.ConsultationCommunity.ConsultationCommunityProps
import org.make.front.components.consultation.ConsultationCommunityMobile.ConsultationCommunityMobileProps
import org.make.front.components.consultation.ConsultationLinkSequence.ConsultationLinkSequenceProps
import org.make.front.components.consultation.ConsultationPresentation.ConsultationPresentationProps
import org.make.front.components.consultation.ConsultationPresentationMobile.ConsultationPresentationMobileProps
import org.make.front.components.consultation.ConsultationProposal.ConsultationProposalProps
import org.make.front.components.consultation.ConsultationShare.ConsultationShareProps
import org.make.front.components.consultation.ResultsInConsultationContainer.ResultsInConsultationContainerProps
import org.make.front.facades.DeviceDetect
import org.make.front.models.{OperationWording, Location => LocationModel, OperationExpanded => OperationModel}

object ConsultationSection {

  case class ConsultationSectionState()
  case class ConsultationSectionProps(operation: OperationModel, language: String, countryCode: String)

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[ConsultationSectionProps, ConsultationSectionState](
          displayName = "ConsultationSection",
          getInitialState = { _ =>
            ConsultationSectionState()
          },
          render = { self =>
            val consultation = self.props.wrapped.operation
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

            <.div()(
              <.ConsultationProposalComponent(
                ^.wrapped := ConsultationProposalProps(
                  self.props.wrapped.operation,
                  maybeLocation = Some(LocationModel.OperationPage(self.props.wrapped.operation.operationId)),
                  language = self.props.wrapped.language
                )
              )(),
              <.ConsultationLinkSequenceComponent(
                ^.wrapped := ConsultationLinkSequenceProps(
                  operation = consultation,
                  country = self.props.wrapped.countryCode
                )
              )(),
              <.ResultsInConsultationContainerComponent(
                ^.wrapped := ResultsInConsultationContainerProps(
                  currentConsultation = consultation,
                  maybeLocation = Some(LocationModel.OperationPage(consultation.operationId))
                )
              )(),
              if (DeviceDetect.isMobileOnly) {
                <.ConsultationCommunityMobileComponent(
                  ^.wrapped := ConsultationCommunityMobileProps(
                    self.props.wrapped.operation,
                    self.props.wrapped.language
                  )
                )()
              } else {
                <.ConsultationCommunityComponent(
                  ^.wrapped := ConsultationCommunityProps(self.props.wrapped.operation, self.props.wrapped.language)
                )()
              },
              maybePresentationComponent,
              <.ConsultationFooterComponent()(),
              <.ConsultationShareComponent(^.wrapped := ConsultationShareProps(operation = consultation))()
            )
          }
        )
    )
}
