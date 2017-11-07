package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.operation.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.components.sequence.SequenceContainer.SequenceContainerProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{FacebookPixel, I18n, Replacements}
import org.make.front.models.{
  GradientColor => GradientColorModel,
  Operation     => OperationModel,
  Sequence      => SequenceModel
}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RWDHideRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.{CTAStyles, TooltipStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.JSConverters._
import scala.util.{Failure, Success}
import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.mutable.StyleSheet.Inline

object OperationSequence {

  final case class OperationSequenceProps(operation: OperationModel,
                                          sequence: SequenceModel,
                                          numberOfProposals: Future[Int])

  final case class OperationSequenceState(isProposalModalOpened: Boolean, numberOfroposals: Int)

  lazy val reactClass: ReactClass =
    React.createClass[OperationSequenceProps, OperationSequenceState](
      displayName = "OperationSSequence",
      getInitialState = { _ =>
        OperationSequenceState(isProposalModalOpened = false, numberOfroposals = 0)
      },
      componentWillReceiveProps = {
        (self: Self[OperationSequenceProps, OperationSequenceState], props: Props[OperationSequenceProps]) =>
          props.wrapped.numberOfProposals.onComplete {
            case Success(numberOfroposals) =>
              self.setState(_.copy(numberOfroposals = numberOfroposals))
            case Failure(_) =>
          }
      },
      render = { self =>
        val guidedState: Boolean = false

        val gradientValues: GradientColorModel =
          self.props.wrapped.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

        val closeProposalModal: () => Unit = () => {
          self.setState(state => state.copy(isProposalModalOpened = false))
        }

        val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
          event.preventDefault()
          self.setState(state => state.copy(isProposalModalOpened = true))
          FacebookPixel
            .fbq("trackCustom", "click-proposal-submit-form-open", Map("location" -> "sequence-header").toJSDictionary)
        }

        object DynamicOperationSequenceStyles extends Inline {

          import dsl._

          val gradient: StyleA =
            style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
        }

        <.section(^.className := OperationSequenceStyles.wrapper)(
          <.div(^.className := Seq(OperationSequenceStyles.row))(
            <.div(^.className := Seq(OperationSequenceStyles.cell, OperationSequenceStyles.mainHeaderWrapper))(
              <.MainHeaderComponent.empty
            )
          ),
          <.div(^.className := Seq(OperationSequenceStyles.row, DynamicOperationSequenceStyles.gradient))(
            <.div(^.className := OperationSequenceStyles.cell)(
              <.div(^.className := RowRulesStyles.centeredRow)(
                <.div(^.className := ColRulesStyles.col)(
                  <.header(^.className := OperationSequenceStyles.header)(
                    <.p(^.className := Seq(OperationSequenceStyles.backLinkWrapper))(
                      /*<.Link(
                        ^.className := OperationSequenceStyles.backLink,
                        ^.to := s"/operation/${self.props.wrapped.operation.slug}"
                      )(
                        <.i(
                          ^.className := Seq(
                            OperationSequenceStyles.backLinkArrow,
                            FontAwesomeStyles.angleLeft
                          )
                        )(),
                        <.span(
                          ^.className := Seq(
                            TextStyles.smallText,
                            TextStyles.title,
                            RWDHideRulesStyles.showBlockBeyondMedium
                          ),
                          ^.dangerouslySetInnerHTML := I18n.t("operation.sequence.header.back-cta")
                        )()
                      )*/
                    ),
                    <.div(^.className := OperationSequenceStyles.titleWrapper)(
                      <.h1(^.className := Seq(OperationSequenceStyles.title, TextStyles.smallTitle))(
                        unescape(self.props.wrapped.sequence.title)
                      ),
                      <.h2(
                        ^.className := Seq(
                          OperationSequenceStyles.totalOfPropositions,
                          TextStyles.smallText,
                          TextStyles.boldText
                        )
                      )(
                        unescape(
                          I18n
                            .t(
                              "operation.sequence.header.total-of-proposals",
                              Replacements(("total", self.state.numberOfroposals.toString))
                            )
                        )
                      )
                    ),
                    <.div(^.className := OperationSequenceStyles.openProposalModalButtonWrapper)(
                      <.div(^.className := OperationSequenceStyles.openProposalModalButtonInnerWrapper)(
                        <.button(
                          ^.className := Seq(
                            CTAStyles.basic,
                            CTAStyles.basicOnButton,
                            OperationSequenceStyles.openProposalModalButton,
                            OperationSequenceStyles.openProposalModalButtonExplained(guidedState)
                          ),
                          ^.onClick := openProposalModal
                        )(
                          <.i(^.className := Seq(FontAwesomeStyles.pencil))(),
                          <.span(^.className := RWDHideRulesStyles.showInlineBlockBeyondMedium)(
                            unescape("&nbsp;" + I18n.t("operation.sequence.header.propose-cta"))
                          )
                        ),
                        if (guidedState) {
                          <.p(^.className := OperationSequenceStyles.guideToPropose)(
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
                            operation = self.props.wrapped.operation,
                            onProposalProposed = closeProposalModal
                          )
                        )()
                      )
                    )
                  )
                )
              )
            )
          ),
          <.div(^.className := Seq(OperationSequenceStyles.row, OperationSequenceStyles.contentRow))(
            <.div(^.className := OperationSequenceStyles.cell)(
              <.SequenceContainerComponent(
                ^.wrapped := SequenceContainerProps(
                  sequence = self.props.wrapped.sequence,
                  maybeThemeColor = Some(gradientValues.from),
                  maybeOperation = Some(self.props.wrapped.operation),
                  intro = IntroOfOperationSequence.reactClass,
                  conclusion = ConclusionOfOperationSequenceContainer.reactClass,
                  promptingToPropose = PromptingToProposeInsideOperationSequence.reactClass
                )
              )()
            )
          ),
          <.style()(OperationSequenceStyles.render[String], DynamicOperationSequenceStyles.render[String])
        )
      }
    )
}

object OperationSequenceStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(display.table, tableLayout.fixed, height(100.%%), width(100.%%))

  val row: StyleA =
    style(display.tableRow)

  val cell: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val contentRow: StyleA =
    style(display.tableRow, height(100.%%), backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

  val mainHeaderWrapper: StyleA =
    style(visibility.hidden)

  val header: StyleA =
    style(
      display.table,
      width(100.%%),
      ThemeStyles.MediaQueries
        .beyondMedium(height(100.pxToEm()))
    )

  val backLinkWrapper: StyleA =
    style(
      display.tableCell,
      width(0.%%),
      padding :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
      verticalAlign.middle,
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
      display.tableCell,
      verticalAlign.top,
      padding :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
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
      display.tableCell,
      verticalAlign.middle,
      width(150.pxToEm()),
      padding :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
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
        .belowMedium(width(160.pxToEm()), right(`0`), transform := "none", (&.after)(right(25.pxToEm()))),
      ThemeStyles.MediaQueries
        .beyondMedium(width :=! s"calc(100% + ${40.pxToEm().value})", marginLeft :=! s"calc(0% - ${20.pxToEm().value})")
    )
}
