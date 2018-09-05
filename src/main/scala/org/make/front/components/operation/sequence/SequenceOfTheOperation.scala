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

package org.make.front.components.operation.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.operation.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.components.sequence.Sequence.ExtraSlide
import org.make.front.components.sequence.SequenceContainer.SequenceContainerProps
import org.make.front.facades.ReactCSSTransition.{
  ReactCSSTransitionDOMAttributes,
  ReactCSSTransitionVirtualDOMElements,
  TransitionClasses
}
import org.make.front.facades.ReactTransition.ReactTransitionDOMAttributes
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{
  OperationExtraSlidesParams,
  ProposalId,
  GradientColor     => GradientColorModel,
  OperationExpanded => OperationModel,
  Sequence          => SequenceModel
}
import org.make.front.styles._
import org.make.front.styles.base._
import org.make.front.styles.ui.{AnimationsStyles, CTAStyles, TooltipStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.concurrent.Future
import scala.scalajs.js

object SequenceOfTheOperation {

  final case class SequenceOfTheOperationProps(isConnected: Boolean,
                                               operation: OperationModel,
                                               startSequence: (js.Array[ProposalId]) => Future[SequenceModel],
                                               redirectHome: ()                      => Unit,
                                               sequence: SequenceModel,
                                               language: String,
                                               country: String,
                                               handleCanUpdate: (Boolean) => Unit,
                                               config: Map[String, Boolean])

  final case class SequenceOfTheOperationState(isProposalModalOpened: Boolean,
                                               numberOfProposals: Int,
                                               operation: OperationModel,
                                               extraSlides: js.Array[ExtraSlide],
                                               fadeIn: Boolean)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[SequenceOfTheOperationProps, SequenceOfTheOperationState](
        displayName = "SequenceOfTheOperation",
        getInitialState = { self =>
          SequenceOfTheOperationState(
            isProposalModalOpened = false,
            numberOfProposals = self.props.wrapped.sequence.proposals.size,
            extraSlides = self.props.wrapped.operation
              .extraSlides(
                OperationExtraSlidesParams(
                  operation = self.props.wrapped.operation,
                  isConnected = self.props.wrapped.isConnected,
                  sequence = self.props.wrapped.sequence,
                  maybeLocation = None,
                  language = self.props.wrapped.language,
                  country = self.props.wrapped.country,
                  handleCanUpdate = self.props.wrapped.handleCanUpdate,
                  closeSequence = () => {
                    self.setState(_.copy(fadeIn = false))
                  }
                )
              )
              .map(
                slide => slide.copy(displayed = self.props.wrapped.config.getOrElse(slide.slideName, slide.displayed))
              ),
            operation = self.props.wrapped.operation,
            fadeIn = false
          )
        },
        componentDidMount = { self =>
          self.setState(_.copy(fadeIn = true))
        },
        componentWillReceiveProps = { (self, nextProps) =>
          if (self.props.wrapped.isConnected != nextProps.wrapped.isConnected ||
              self.props.wrapped.operation != nextProps.wrapped.operation ||
              self.props.wrapped.sequence != nextProps.wrapped.sequence ||
              self.props.wrapped.language != nextProps.wrapped.language ||
              self.props.wrapped.country != nextProps.wrapped.country) {
            self.setState(
              _.copy(
                extraSlides = self.props.wrapped.operation
                  .extraSlides(
                    OperationExtraSlidesParams(
                      operation = nextProps.wrapped.operation,
                      isConnected = nextProps.wrapped.isConnected,
                      sequence = nextProps.wrapped.sequence,
                      maybeLocation = None,
                      language = nextProps.wrapped.language,
                      country = nextProps.wrapped.country,
                      handleCanUpdate = self.props.wrapped.handleCanUpdate,
                      closeSequence = () => {
                        self.setState(_.copy(fadeIn = false))
                      }
                    )
                  )
                  .map(
                    slide =>
                      slide.copy(displayed = self.props.wrapped.config.getOrElse(slide.slideName, slide.displayed))
                  )
              )
            )
          }
        },
        render = { self =>
          val operation = self.state.operation
          val guidedState: Boolean = false

          val gradientValues: GradientColorModel =
            operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          val closeProposalModal: () => Unit = () => {
            self.setState(state => state.copy(isProposalModalOpened = false))
          }

          val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
            event.preventDefault()
            self.setState(state => state.copy(isProposalModalOpened = true))
            TrackingService.track(
              eventName = "click-proposal-submit-form-open",
              trackingContext = TrackingContext(TrackingLocation.sequencePage, Some(operation.slug)),
              parameters = Map.empty,
              internalOnlyParameters = Map("sequenceId" -> self.props.wrapped.sequence.sequenceId.value)
            )
          }

          def redirectAfterSequence: () => Unit = () => {
            self.props.history
              .push(s"/${self.state.operation.country}/consultation/${self.state.operation.slug}/consultation")
          }

          object DynamicSequenceOfTheOperationStyles extends StyleSheet.Inline {

            import dsl._

            val gradient: StyleA =
              style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          if (operation.isActive) {
            <.section(
              ^.className := js.Array(TableLayoutStyles.fullHeightWrapper, SequenceOfTheOperationStyles.wrapper)
            )(
              <.div(^.className := js.Array(TableLayoutStyles.row))(
                <.div(^.className := js.Array(TableLayoutStyles.cell, SequenceOfTheOperationStyles.mainHeaderWrapper))(
                  <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty),
                  <.div(^.className := SequenceOfTheOperationStyles.fixedMainHeaderWrapper)(
                    <.CookieAlertContainerComponent.empty,
                    <.MainHeaderContainer.empty
                  )
                )
              ),
              <.div(^.className := js.Array(TableLayoutStyles.row, DynamicSequenceOfTheOperationStyles.gradient))(
                <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                  <.div(^.className := LayoutRulesStyles.centeredRow)(
                    <.header(^.className := js.Array(TableLayoutStyles.wrapper, SequenceOfTheOperationStyles.header))(
                      <.p(
                        ^.className := js
                          .Array(
                            TableLayoutStyles.cellVerticalAlignMiddle,
                            SequenceOfTheOperationStyles.backLinkWrapper
                          )
                      )(
                        <.Link(
                          ^.className := SequenceOfTheOperationStyles.backLink,
                          ^.to := s"/${self.props.wrapped.country}/consultation/${operation.slug}"
                        )(
                          <.i(
                            ^.className := js
                              .Array(SequenceOfTheOperationStyles.backLinkArrow, FontAwesomeStyles.angleLeft)
                          )(),
                          <.span(
                            ^.className := js
                              .Array(
                                TextStyles.smallText,
                                TextStyles.title,
                                RWDRulesMediumStyles.showBlockBeyondMedium
                              ),
                            ^.dangerouslySetInnerHTML := I18n.t("operation.sequence.header.back-cta")
                          )()
                        )
                      ),
                      <.div(^.className := js.Array(TableLayoutStyles.cell, SequenceOfTheOperationStyles.titleWrapper))(
                        <.h1(^.className := js.Array(SequenceOfTheOperationStyles.title, TextStyles.smallTitle))(
                          unescape(
                            self.props.wrapped.operation
                              .getWordingByLanguageOrError(self.props.wrapped.language)
                              .question
                          )
                        ),
                        <.h2(
                          ^.className := js.Array(
                            SequenceOfTheOperationStyles.totalOfPropositions,
                            TextStyles.smallText,
                            TextStyles.boldText
                          )
                        )(
                          unescape(
                            I18n
                              .t(
                                "operation.sequence.header.total-of-proposals",
                                Replacements(
                                  ("total", self.state.numberOfProposals.toString),
                                  ("count", self.state.numberOfProposals.toString)
                                )
                              )
                          )
                        )
                      ),
                      <.div(
                        ^.className := js
                          .Array(TableLayoutStyles.cell, SequenceOfTheOperationStyles.openProposalModalButtonWrapper)
                      )(
                        <.div(^.className := SequenceOfTheOperationStyles.openProposalModalButtonInnerWrapper)(
                          <.button(
                            ^.className := js.Array(
                              CTAStyles.basic,
                              CTAStyles.basicOnButton,
                              SequenceOfTheOperationStyles.openProposalModalButton,
                              SequenceOfTheOperationStyles.openProposalModalButtonExplained(guidedState)
                            ),
                            ^.onClick := openProposalModal
                          )(
                            <.i(^.className := js.Array(FontAwesomeStyles.pencil))(),
                            <.span(^.className := RWDRulesMediumStyles.showInlineBlockBeyondMedium)(
                              unescape("&nbsp;" + I18n.t("operation.sequence.header.propose-cta"))
                            )
                          ),
                          if (guidedState) {
                            <.p(^.className := SequenceOfTheOperationStyles.guideToPropose)(
                              <.span(
                                ^.className := TextStyles.smallerText,
                                ^.dangerouslySetInnerHTML := I18n.t("operation.sequence.header.guide.propose-cta")
                              )()
                            )
                          }
                        ),
                        <.FullscreenModalComponent(
                          ^.wrapped := FullscreenModalProps(
                            isModalOpened = self.state.isProposalModalOpened,
                            closeCallback = closeProposalModal
                          )
                        )(
                          <.SubmitProposalInRelationToOperationComponent(
                            ^.wrapped := SubmitProposalInRelationToOperationProps(
                              operation = operation,
                              onProposalProposed = closeProposalModal,
                              maybeSequence = Some(self.props.wrapped.sequence.sequenceId),
                              maybeLocation = None,
                              language = self.props.wrapped.language
                            )
                          )()
                        )
                      )
                    )
                  )
                )
              ),
              <.CSSTransition(
                ^.timeout := 500,
                ^.in := self.state.fadeIn,
                ^.classNamesMap := TransitionClasses(
                  enter = AnimationsStyles.fadeOn250.htmlClass,
                  exit = AnimationsStyles.fadeOff500.htmlClass,
                  exitDone = AnimationsStyles.hideChildren.htmlClass
                ),
                ^.onCSSTransitionExited := redirectAfterSequence
              )(
                <.div(
                  ^.className := js.Array(TableLayoutStyles.fullHeightRow, SequenceOfTheOperationStyles.contentRow)
                )(
                  <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                    <.SequenceContainerComponent(
                      ^.wrapped := SequenceContainerProps(
                        loadSequence = self.props.wrapped.startSequence,
                        sequence = self.props.wrapped.sequence,
                        progressBarColor = Some(gradientValues.from),
                        extraSlides = self.state.extraSlides,
                        maybeTheme = None,
                        maybeOperation = Some(operation),
                        maybeLocation = None
                      )
                    )()
                  )
                )
              ),
              <.MainFooterComponent.empty,
              <.style()(SequenceOfTheOperationStyles.render[String], DynamicSequenceOfTheOperationStyles.render[String])
            )
          } else {
            <.section()()
          }
        }
      )
    )
}

object SequenceOfTheOperationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent), tableLayout.fixed)

  val contentRow: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

  val mainHeaderWrapper: StyleA =
    style(
      paddingBottom(50.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
    )

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

  val header: StyleA =
    style(
      ThemeStyles.MediaQueries
        .beyondMedium(height(100.pxToEm()))
    )

  val backLinkWrapper: StyleA =
    style(
      width(0.%%),
      padding(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      color(ThemeStyles.TextColor.white),
      ThemeStyles.MediaQueries.beyondMedium(width(150.pxToEm()))
    )

  val backLink: StyleA =
    style(
      position.relative,
      display.inlineBlock,
      color(ThemeStyles.TextColor.white),
      ThemeStyles.MediaQueries
        .beyondMedium(paddingLeft(20.pxToEm()))
    )

  val backLinkArrow: StyleA =
    style(
      paddingRight(ThemeStyles.SpacingValue.small.pxToEm(28)),
      fontSize(28.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondMedium(position.absolute, top(50.%%), left(`0`), paddingRight(`0`), transform := "translateY(-50%)")
    )

  val titleWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondMedium(verticalAlign.middle)
    )

  val title: StyleA =
    style(color(ThemeStyles.TextColor.white), ThemeStyles.MediaQueries.beyondMedium(textAlign.center))

  val totalOfPropositions: StyleA =
    style(
      color(ThemeStyles.TextColor.white),
      ThemeStyles.MediaQueries
        .belowMedium(lineHeight(1)),
      ThemeStyles.MediaQueries.beyondMedium(textAlign.center)
    )

  val openProposalModalButtonWrapper: StyleA =
    style(
      verticalAlign.middle,
      width(150.pxToEm()),
      padding(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      textAlign.right,
      ThemeStyles.MediaQueries
        .belowMedium(verticalAlign.top, width.auto)
    )

  val openProposalModalButton: StyleA =
    style(
      ThemeStyles.MediaQueries
        .belowMedium(
          width(50.pxToEm(25)),
          height(50.pxToEm(25)),
          minHeight.auto,
          maxWidth.none,
          padding(`0`),
          fontSize(25.pxToEm()),
          lineHeight(1)
        )
    )

  val openProposalModalButtonExplained: (Boolean) => StyleA = styleF.bool(
    explained =>
      if (explained) {
        styleS(boxShadow := s"0 1px 1px rgba(0, 0, 0, 0.5), 0 0 20px 0 rgba(255,255,255,0.50)")
      } else {
        styleS()
    }
  )

  val openProposalModalButtonInnerWrapper: StyleA =
    style(
      position.relative,
      ThemeStyles.MediaQueries
        .beyondMedium(display.inlineBlock)
    )

  val guideToPropose: StyleA =
    style(
      TooltipStyles.bottomPositioned,
      ThemeStyles.MediaQueries
        .belowMedium(width(160.pxToEm()), right(`0`), transform := "none", &.after(right(25.pxToEm()))),
      ThemeStyles.MediaQueries
        .beyondMedium(width :=! s"calc(100% + ${40.pxToEm().value})", marginLeft :=! s"calc(0% - ${20.pxToEm().value})")
    )

  val spinnerContainer: StyleA =
    style(height(100.%%))

}
