package org.make.front.components.operation.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.operation.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.components.sequence.SequenceContainer.SequenceContainerProps
import org.make.front.components.sequence.contents.{
  ConclusionOfTheSequenceContainer,
  IntroductionOfTheSequence,
  PromptingToConnect,
  PromptingToProposeSequence
}
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

object SequenceOfTheOperation {

  final case class SequenceOfTheOperationProps(operation: OperationModel,
                                               sequence: SequenceModel,
                                               numberOfProposals: Future[Int])

  final case class SequenceOfTheOperationState(isProposalModalOpened: Boolean, numberOfroposals: Int)

  lazy val reactClass: ReactClass =
    React.createClass[SequenceOfTheOperationProps, SequenceOfTheOperationState](
      displayName = "SequenceOfTheOperation",
      getInitialState = { _ =>
        SequenceOfTheOperationState(isProposalModalOpened = false, numberOfroposals = 0)
      },
      componentWillReceiveProps = {
        (self: Self[SequenceOfTheOperationProps, SequenceOfTheOperationState],
         props: Props[SequenceOfTheOperationProps]) =>
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

        object DynamicSequenceOfTheOperationStyles extends Inline {

          import dsl._

          val gradient: StyleA =
            style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
        }

        <.section(^.className := SequenceOfTheOperationStyles.wrapper)(
          <.div(^.className := Seq(SequenceOfTheOperationStyles.row))(
            <.div(
              ^.className := Seq(SequenceOfTheOperationStyles.cell, SequenceOfTheOperationStyles.mainHeaderWrapper)
            )(<.MainHeaderComponent.empty)
          ),
          <.div(^.className := Seq(SequenceOfTheOperationStyles.row, DynamicSequenceOfTheOperationStyles.gradient))(
            <.div(^.className := SequenceOfTheOperationStyles.cell)(
              <.div(^.className := RowRulesStyles.centeredRow)(
                <.div(^.className := ColRulesStyles.col)(
                  <.header(^.className := SequenceOfTheOperationStyles.header)(
                    <.p(^.className := Seq(SequenceOfTheOperationStyles.backLinkWrapper))(
                      /*<.Link(
                        ^.className := SequenceOfTheOperationStyles.backLink,
                        ^.to := s"/operation/${self.props.wrapped.operation.slug}"
                      )(
                        <.i(
                          ^.className := Seq(
                            SequenceOfTheOperationStyles.backLinkArrow,
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
                    <.div(^.className := SequenceOfTheOperationStyles.titleWrapper)(
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
                              Replacements(("total", self.state.numberOfroposals.toString))
                            )
                        )
                      )
                    ),
                    <.div(^.className := SequenceOfTheOperationStyles.openProposalModalButtonWrapper)(
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
          <.div(^.className := Seq(SequenceOfTheOperationStyles.row, SequenceOfTheOperationStyles.contentRow))(
            <.div(^.className := SequenceOfTheOperationStyles.cell)(
              <.SequenceContainerComponent(
                ^.wrapped := SequenceContainerProps(
                  sequence = self.props.wrapped.sequence,
                  maybeThemeColor = Some(gradientValues.from),
                  maybeOperation = Some(self.props.wrapped.operation),
                  intro = IntroductionOfTheSequence.reactClass,
                  conclusion = ConclusionOfTheSequenceContainer.reactClass,
                  promptingToPropose = PromptingToProposeSequence.reactClass,
                  promptingToConnect = PromptingToConnect.reactClass
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
