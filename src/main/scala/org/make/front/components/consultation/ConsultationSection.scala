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
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.consultation.ConsultationCommunity.ConsultationCommunityProps
import org.make.front.components.consultation.ConsultationLinkSequence.ConsultationLinkSequenceProps
import org.make.front.components.consultation.ConsultationPresentation.ConsultationPresentationProps
import org.make.front.components.consultation.ConsultationProposal.ConsultationProposalProps
import org.make.front.components.consultation.ConsultationShare.ConsultationShareProps
import org.make.front.components.consultation.ConsultationShareMobile.ConsultationShareMobileProps
import org.make.front.components.consultation.ResultsInConsultationContainer.ResultsInConsultationContainerProps
import org.make.front.components.mainFooter.AltFooter.AltFooterProps
import org.make.front.models.{OperationWording, Location => LocationModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.RWDRulesLargeMediumStyles
import org.make.front.styles.utils._

object ConsultationSection {
  case class ConsultationSectionProps(operation: OperationModel,
                                      language: String,
                                      countryCode: String,
                                      isSequenceDone: Boolean)

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[ConsultationSectionProps, Unit](displayName = "ConsultationSection", componentDidMount = { _ =>
          AffixMethods.sidebarAffixrAF()
        }, render = {
          self =>
            val consultation = self.props.wrapped.operation
            val maybeWording: Option[OperationWording] =
              consultation.getWordingByLanguage(self.props.wrapped.language)
            val mobilePresentation = maybeWording.flatMap { wording =>
              wording.presentation.map { content =>
                <.div(^.className := RWDRulesLargeMediumStyles.showBlockBeyondLargeMedium)(
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
            }
            <.div(^.className := ConsultationSectionStyles.wrapper, ^.id := "wrapperContainer")(
              <.div(^.className := ConsultationSectionStyles.main)(
                <.div(^.id := "mainContainer")(
                  /** Removed for DITP */
                  /**<.ConsultationProposalComponent(
                    ^.wrapped := ConsultationProposalProps(
                      self.props.wrapped.operation,
                      maybeLocation = Some(LocationModel.OperationPage(self.props.wrapped.operation.operationId)),
                      language = self.props.wrapped.language
                    )
                  )(), */
                  if (!self.props.wrapped.isSequenceDone) {
                    <.ConsultationLinkSequenceComponent(
                      ^.wrapped := ConsultationLinkSequenceProps(
                        operation = consultation,
                        country = self.props.wrapped.countryCode
                      )
                    )()
                  },
                  maybeWording.flatMap { wording =>
                    wording.presentation.map { content =>
                      <.div(^.className := RWDRulesLargeMediumStyles.hideBeyondLargeMedium)(
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
                )
              ),
              <.aside(^.className := ConsultationSectionStyles.sidebar)(
                <.div(^.id := "sidebarAffixContainer")(
                  <.div(^.id := "sidebarAffixElement")(
                    mobilePresentation,
                    if (self.props.wrapped.operation.featureSettings.action) {
                      <.div(^.className := RWDRulesLargeMediumStyles.showBlockBeyondLargeMedium)(
                        <.ConsultationCommunityComponent(
                          ^.wrapped := ConsultationCommunityProps(
                            self.props.wrapped.operation,
                            self.props.wrapped.language
                          )
                        )()
                      )
                    },
                    if (self.props.wrapped.operation.featureSettings.share) {
                      Seq(
                        <.ConsultationShareMobileComponent(
                          ^.wrapped := ConsultationShareMobileProps(operation = consultation)
                        )(),
                        <.div(^.className := RWDRulesLargeMediumStyles.showBlockBeyondLargeMedium)(
                          <.ConsultationShareComponent(^.wrapped := ConsultationShareProps(operation = consultation))(),
                          <.AltFooterComponent(
                            ^.wrapped := AltFooterProps(countryCode = self.props.wrapped.countryCode)
                          )()
                        )
                      )
                    }
                  )
                )
              ),
              <.style()(ConsultationSectionStyles.render[String])
            )
        })
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
      overflowX.hidden,
      ThemeStyles.MediaQueries.beyondLargeMedium(
        flexFlow := s"row",
        paddingTop(40.pxToEm()),
        paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
        paddingBottom(40.pxToEm()),
        paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val main: StyleA =
    style(
      ThemeStyles.MediaQueries
        .beyondLargeMedium(width(100.%%), maxWidth(750.pxToPercent(1140)), marginRight(30.pxToPercent(1140)))
    )

  val sidebar: StyleA =
    style(ThemeStyles.MediaQueries.beyondLargeMedium(maxWidth(360.pxToPercent(1140))))

}
