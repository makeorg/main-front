package org.make.front.components.operation.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
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
  Location,
  OperationDesignData,
  OperationExtraSlidesParams,
  ProposalId,
  SequenceId,
  GradientColor     => GradientColorModel,
  OperationExpanded => OperationModel,
  Sequence          => SequenceModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.front.styles._
import org.make.front.styles.base._
import org.make.front.styles.ui.{CTAStyles, TooltipStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.sequence.SequenceService
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object SequenceOfTheOperation {

  final case class SequenceOfTheOperationProps(maybeFirstProposalSlug: Option[String],
                                               isConnected: Boolean,
                                               operation: Future[Option[OperationModel]],
                                               maybeTheme: Option[TranslatedThemeModel],
                                               maybeOperation: Option[OperationModel],
                                               maybeLocation: Option[Location],
                                               redirectHome: () => Unit)

  final case class SequenceOfTheOperationState(isProposalModalOpened: Boolean,
                                               numberOfProposals: Int,
                                               sequenceTitle: String = "",
                                               sequence: Option[SequenceModel] = None,
                                               operation: OperationModel = OperationModel.empty,
                                               extraSlides: Seq[ExtraSlide])

  lazy val reactClass: ReactClass = {
    def loadSequence(
      self: Self[SequenceOfTheOperationProps, SequenceOfTheOperationState]
    )(proposals: Seq[ProposalId]): Future[SequenceModel] = {

      self.state.sequence match {
        case Some(sequence) =>
          SequenceService.startSequenceById(sequenceId = sequence.sequenceId, includes = proposals).map { seq =>
            self.setState(_.copy(numberOfProposals = seq.proposals.size))
            seq
          }
        case _ =>
          Future.successful(SequenceModel(SequenceId("fake"), "", ""))
      }
    }

    React.createClass[SequenceOfTheOperationProps, SequenceOfTheOperationState](
      displayName = "SequenceOfTheOperation",
      getInitialState = { self =>
        SequenceOfTheOperationState(isProposalModalOpened = false, numberOfProposals = 0, extraSlides = Seq.empty)
      },
      componentDidMount = { (self) =>
        self.props.wrapped.operation.onComplete {
          case Failure(_) => self.props.wrapped.redirectHome()
          case Success(maybeOperation) =>
            val operation: OperationModel = maybeOperation.getOrElse(OperationModel.empty)
            operation.sequence match {
              case Some(sequenceId) =>
                SequenceService.startSequenceById(sequenceId = sequenceId, includes = Seq.empty).onComplete {
                  case Failure(_) => self.setState(_.copy(operation = operation, sequenceTitle = "", sequence = None))
                  case Success(sequence) =>
                    self.setState(
                      _.copy(
                        operation = operation,
                        sequenceTitle = sequence.title,
                        sequence = Some(sequence),
                        numberOfProposals = sequence.proposals.size,
                        extraSlides = operation.extraSlides(
                          OperationExtraSlidesParams(
                            operation,
                            self.props.wrapped.isConnected,
                            Some(sequence),
                            self.props.wrapped.maybeLocation
                          )
                        )
                      )
                    )
                }
              case _ => self.setState(_.copy(operation = operation, sequenceTitle = "", sequence = None))
            }
        }
      },
      render = { self =>
        val guidedState: Boolean = false

        val gradientValues: GradientColorModel =
          self.state.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

        val closeProposalModal: () => Unit = () => {
          self.setState(state => state.copy(isProposalModalOpened = false))
        }

        val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
          event.preventDefault()
          self.setState(state => state.copy(isProposalModalOpened = true))
          TrackingService.track(
            "click-proposal-submit-form-open",
            TrackingContext(TrackingLocation.sequencePage, Some(self.state.operation.slug)),
            Map("sequenceId" -> self.state.sequence.map(_.sequenceId.value).getOrElse(""))
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
                      ^.to := s"/consultation/${self.state.operation.slug}"
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
                      unescape(self.state.sequenceTitle)
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
                          operation = self.state.operation,
                          onProposalProposed = closeProposalModal,
                          maybeSequence = self.state.sequence,
                          maybeLocation = self.props.wrapped.maybeLocation
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
                  loadSequence = loadSequence(self),
                  sequence = self.state.sequence,
                  progressBarColor = Some(gradientValues.from),
                  maybeFirstProposalSlug = self.props.wrapped.maybeFirstProposalSlug,
                  extraSlides = self.state.extraSlides,
                  maybeTheme = self.props.wrapped.maybeTheme,
                  maybeOperation = self.props.wrapped.maybeOperation,
                  maybeLocation = self.props.wrapped.maybeLocation
                )
              )()
            )
          ),
          <.style()(SequenceOfTheOperationStyles.render[String], DynamicSequenceOfTheOperationStyles.render[String])
        )
      }
    )
  }
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
