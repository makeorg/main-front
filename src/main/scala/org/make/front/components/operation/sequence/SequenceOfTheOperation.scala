package org.make.front.components.operation.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.operation.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.components.sequence.Sequence.ExtraSlide
import org.make.front.components.sequence.SequenceContainer.SequenceContainerProps
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
import org.make.front.styles.ui.{CTAStyles, TooltipStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.concurrent.Future

object SequenceOfTheOperation {

  final case class SequenceOfTheOperationProps(maybeFirstProposalSlug: Option[String],
                                               isConnected: Boolean,
                                               operation: OperationModel,
                                               startSequence: (Seq[ProposalId]) => Future[SequenceModel],
                                               redirectHome: ()                 => Unit,
                                               sequence: SequenceModel,
                                               language: String)

  final case class SequenceOfTheOperationState(isProposalModalOpened: Boolean,
                                               numberOfProposals: Int,
                                               operation: OperationModel,
                                               extraSlides: Seq[ExtraSlide])

  lazy val reactClass: ReactClass =
    React.createClass[SequenceOfTheOperationProps, SequenceOfTheOperationState](
      displayName = "SequenceOfTheOperation",
      getInitialState = { self =>
        SequenceOfTheOperationState(
          isProposalModalOpened = false,
          numberOfProposals = self.props.wrapped.sequence.proposals.size,
          extraSlides = self.props.wrapped.operation.extraSlides(
            OperationExtraSlidesParams(
              operation = self.props.wrapped.operation,
              isConnected = self.props.wrapped.isConnected,
              sequence = self.props.wrapped.sequence,
              maybeLocation = None,
              language = self.props.wrapped.language
            )
          ),
          operation = self.props.wrapped.operation
        )
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
            "click-proposal-submit-form-open",
            TrackingContext(TrackingLocation.sequencePage, Some(operation.slug)),
            Map("sequenceId" -> self.props.wrapped.sequence.sequenceId.value)
          )
        }

        object DynamicSequenceOfTheOperationStyles extends StyleSheet.Inline {

          import dsl._

          val gradient: StyleA =
            style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
        }

        <.section(^.className := Seq(TableLayoutStyles.fullHeightWrapper, SequenceOfTheOperationStyles.wrapper))(
          <.div(^.className := Seq(TableLayoutStyles.row))(
            <.div(
              ^.className := Seq(
                TableLayoutStyles.cellVerticalAlignMiddle,
                SequenceOfTheOperationStyles.mainHeaderWrapper
              )
            )(<.MainHeaderComponent.empty)
          ),
          <.div(^.className := Seq(TableLayoutStyles.row, DynamicSequenceOfTheOperationStyles.gradient))(
            <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
              <.div(^.className := LayoutRulesStyles.centeredRow)(
                <.header(^.className := Seq(TableLayoutStyles.wrapper, SequenceOfTheOperationStyles.header))(
                  <.p(
                    ^.className := Seq(
                      TableLayoutStyles.cellVerticalAlignMiddle,
                      SequenceOfTheOperationStyles.backLinkWrapper
                    )
                  )(
                    <.Link(
                      ^.className := SequenceOfTheOperationStyles.backLink,
                      ^.to := s"/consultation/${operation.slug}"
                    )(
                      <.i(
                        ^.className := Seq(SequenceOfTheOperationStyles.backLinkArrow, FontAwesomeStyles.angleLeft)
                      )(),
                      <.span(
                        ^.className := Seq(
                          TextStyles.smallText,
                          TextStyles.title,
                          RWDHideRulesStyles.showBlockBeyondMedium
                        ),
                        ^.dangerouslySetInnerHTML := I18n.t("operation.sequence.header.back-cta")
                      )()
                    )
                  ),
                  <.div(^.className := Seq(TableLayoutStyles.cell, SequenceOfTheOperationStyles.titleWrapper))(
                    <.h1(^.className := Seq(SequenceOfTheOperationStyles.title, TextStyles.smallTitle))(
                      unescape(self.props.wrapped.sequence.title)
                    ),
                    <.h2(
                      ^.className := Seq(
                        SequenceOfTheOperationStyles.totalOfPropositions,
                        TextStyles.smallText,
                        TextStyles.boldText
                      )
                    )(
                      unescape(
                        I18n
                          .t(
                            "operation.sequence.header.total-of-proposals",
                            Replacements(("total", self.state.numberOfProposals.toString))
                          )
                      )
                    )
                  ),
                  <.div(
                    ^.className := Seq(
                      TableLayoutStyles.cell,
                      SequenceOfTheOperationStyles.openProposalModalButtonWrapper
                    )
                  )(
                    <.div(^.className := SequenceOfTheOperationStyles.openProposalModalButtonInnerWrapper)(
                      <.button(
                        ^.className := Seq(
                          CTAStyles.basic,
                          CTAStyles.basicOnButton,
                          SequenceOfTheOperationStyles.openProposalModalButton,
                          SequenceOfTheOperationStyles.openProposalModalButtonExplained(guidedState)
                        ),
                        ^.onClick := openProposalModal
                      )(
                        <.i(^.className := Seq(FontAwesomeStyles.pencil))(),
                        <.span(^.className := RWDHideRulesStyles.showInlineBlockBeyondMedium)(
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
          <.div(^.className := Seq(TableLayoutStyles.fullHeightRow, SequenceOfTheOperationStyles.contentRow))(
            <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
              <.SequenceContainerComponent(
                ^.wrapped := SequenceContainerProps(
                  loadSequence = self.props.wrapped.startSequence,
                  sequence = self.props.wrapped.sequence,
                  progressBarColor = Some(gradientValues.from),
                  maybeFirstProposalSlug = self.props.wrapped.maybeFirstProposalSlug,
                  extraSlides = self.state.extraSlides,
                  maybeTheme = None,
                  maybeOperation = Some(operation),
                  maybeLocation = None
                )
              )()
            )
          ),
          <.style()(SequenceOfTheOperationStyles.render[String], DynamicSequenceOfTheOperationStyles.render[String])
        )
      }
    )

}

object SequenceOfTheOperationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(tableLayout.fixed)

  val contentRow: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

  val mainHeaderWrapper: StyleA =
    style(visibility.hidden)

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
}
