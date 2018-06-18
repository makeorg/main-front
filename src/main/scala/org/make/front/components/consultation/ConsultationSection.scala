package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.styles.utils._
import org.make.front.components.Components._
import org.make.front.components.consultation.ConsultationCommunity.ConsultationCommunityProps
import org.make.front.components.consultation.ConsultationLinkSequence.ConsultationLinkSequenceProps
import org.make.front.components.consultation.ConsultationPresentation.ConsultationPresentationProps
import org.make.front.components.consultation.ConsultationProposal.ConsultationProposalProps
import org.make.front.components.consultation.ConsultationShare.ConsultationShareProps
import org.make.front.components.consultation.ConsultationShareMobile.ConsultationShareMobileProps
import org.make.front.components.consultation.ResultsInConsultationContainer.ResultsInConsultationContainerProps
import org.make.front.facades.DeviceDetect
import org.make.front.models.{OperationWording, Location => LocationModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{RWDHideRulesStyles}

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
              <.div(^.className := ConsultationSectionStyles.wrapper)(
                <.div(^.className := ConsultationSectionStyles.main)(
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
                  maybeWording.flatMap { wording =>
                    wording.presentation.map { content =>
                      <.div(^.className := RWDHideRulesStyles.hideBeyondLargeMedium)(
                        <.ConsultationPresentationComponent(
                          ^.wrapped := ConsultationPresentationProps(
                            operation = consultation,
                            language = self.props.wrapped.language,
                            content = content,
                            learnMoreUrl = wording.learnMoreUrl,
                            isCollapsed = true
                          )
                        )()
                      )
                    }
                  },
                  <.ResultsInConsultationContainerComponent(
                    ^.wrapped := ResultsInConsultationContainerProps(
                      currentConsultation = consultation,
                      maybeLocation = Some(LocationModel.OperationPage(consultation.operationId))
                    )
                  )()
                ),
                <.aside(^.className := ConsultationSectionStyles.sidebar)(
                maybeWording.flatMap { wording =>
                  wording.presentation.map { content =>
                    <.div(^.className := RWDHideRulesStyles.showBlockBeyondLargeMedium)(
                      <.ConsultationPresentationComponent(
                        ^.wrapped := ConsultationPresentationProps(
                          operation = consultation,
                          language = self.props.wrapped.language,
                          content = content,
                          learnMoreUrl = wording.learnMoreUrl,
                          isCollapsed = false
                        )
                      )()
                    )
                  }
                },
                <.div(^.className := RWDHideRulesStyles.showBlockBeyondLargeMedium)(
                  <.ConsultationShareComponent(
                    ^.wrapped := ConsultationShareProps(operation = consultation)
                  )()
                ),
                <.ConsultationShareMobileComponent(
                  ^.wrapped := ConsultationShareMobileProps(operation = consultation)
                )(),
                <.div(^.className := RWDHideRulesStyles.showBlockBeyondLargeMedium)(
                  <.ConsultationCommunityComponent(
                    ^.wrapped := ConsultationCommunityProps(self.props.wrapped.operation, self.props.wrapped.language)
                  )(),
                  <.ConsultationFooterComponent()()
                )
                ),<.style()(ConsultationSectionStyles.render[String])
            )
          }
        )
    )
}

object ConsultationSectionStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      display.flex,
      flexFlow := s"column",
      maxWidth(ThemeStyles.containerMaxWidth),
      marginLeft(auto),
      marginRight(auto),
      paddingTop(20.pxToEm()),
      paddingBottom(20.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        flexFlow := s"row",
        paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
        paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val main: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondLargeMedium(
        maxWidth(750.pxToPercent(1140)),
        marginRight(30.pxToPercent(1140))
      )
    )

  val sidebar: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondLargeMedium(
        maxWidth(360.pxToPercent(1140))
      )
    )

}