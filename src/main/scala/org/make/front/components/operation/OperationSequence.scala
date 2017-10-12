package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.sequence.SequenceContainer.SequenceContainerProps
import org.make.front.components.submitProposal.SubmitProposal.SubmitProposalProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{
  GradientColor => GradientColorModel,
  Operation     => OperationModel,
  Sequence      => SequenceModel
}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RWDHideRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.mutable.StyleSheet.Inline

object OperationSequence {
  final case class OperationSequenceProps(operation: OperationModel, sequence: SequenceModel)

  final case class OperationSequenceState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[OperationSequenceProps, OperationSequenceState](
      displayName = "OperationSSequence",
      getInitialState = { _ =>
        OperationSequenceState(isProposalModalOpened = false)
      },
      render = { self =>
        val gradientValues: GradientColorModel =
          self.props.wrapped.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

        val closeProposalModal: () => Unit = () => {
          self.setState(state => state.copy(isProposalModalOpened = false))
        }

        val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
          event.preventDefault()
          self.setState(state => state.copy(isProposalModalOpened = true))
        }

        object DynamicOperationSequenceStyles extends Inline {
          import dsl._
          val gradient: StyleA =
            style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
        }

        <.section(^.className := OperationSequenceStyles.wrapper)(
          <.header(^.className := Seq(OperationSequenceStyles.headerWrapper, DynamicOperationSequenceStyles.gradient))(
            <.div(^.className := OperationSequenceStyles.headerInnerWrapper)(
              <.div(^.className := RowRulesStyles.centeredRow)(
                <.div(^.className := ColRulesStyles.col)(
                  <.div(^.className := OperationSequenceStyles.headerInnerSubWrapper)(
                    <.p(^.className := Seq(OperationSequenceStyles.backLinkWrapper))(
                      <.Link(
                        ^.className := OperationSequenceStyles.backLink,
                        ^.to := s"/operation/${self.props.wrapped.operation.slug}"
                      )(
                        <.i(
                          ^.className := Seq(
                            OperationSequenceStyles.backLinkArrow,
                            FontAwesomeStyles.angleLeft,
                            FontAwesomeStyles.fa
                          )
                        )(),
                        <.span(
                          ^.className := Seq(
                            TextStyles.smallText,
                            TextStyles.title,
                            RWDHideRulesStyles.showBlockBeyondMedium
                          ),
                          ^.dangerouslySetInnerHTML := "Accéder à<br>l'opération"
                        )()
                      )
                    ),
                    <.div(^.className := OperationSequenceStyles.titleWrapper)(
                      <.h1(^.className := Seq(OperationSequenceStyles.title, TextStyles.smallTitle))(
                        unescape(self.props.wrapped.sequence.title)
                      ),
                      <.p(
                        ^.className := Seq(
                          OperationSequenceStyles.totalOfPropositions,
                          TextStyles.smallText,
                          TextStyles.boldText
                        )
                      )(self.props.wrapped.sequence.proposalsSlugs.size + " propositions")
                    ),
                    <.div(^.className := OperationSequenceStyles.openProposalModalButtonWrapper)(
                      <.button(
                        ^.className := Seq(
                          CTAStyles.basic,
                          CTAStyles.basicOnButton,
                          OperationSequenceStyles.openProposalModalButton
                        ),
                        ^.onClick := openProposalModal
                      )(
                        <.i(^.className := Seq(FontAwesomeStyles.pencil, FontAwesomeStyles.fa))(),
                        <.span(^.className := RWDHideRulesStyles.showInlineBlockBeyondMedium)(
                          unescape("&nbsp;" + I18n.t("content.search.propose"))
                        )
                      ),
                      <.FullscreenModalComponent(
                        ^.wrapped := FullscreenModalProps(self.state.isProposalModalOpened, closeProposalModal)
                      )(<.SubmitProposalComponent(^.wrapped := SubmitProposalProps(onProposalProposed = () => {
                        self.setState(_.copy(isProposalModalOpened = false))
                      }))())
                    )
                  )
                )
              )
            )
          ),
          <.div(^.className := OperationSequenceStyles.contentWrapper)(
            <.div(^.className := OperationSequenceStyles.contentInnerWrapper)(
              <.SequenceContainerComponent(
                ^.wrapped := SequenceContainerProps(
                  sequence = self.props.wrapped.sequence,
                  maybeThemeColor = Some(gradientValues.from),
                  intro = IntroOfOperationSequence.reactClass,
                  conclusion = ConclusionOfOperationSequenceContainer.reactClass
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

  val contentWrapper: StyleA =
    style(display.tableRow, height(100.%%), backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

  val contentInnerWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val headerWrapper: StyleA =
    style(display.tableRow)

  val headerInnerWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      paddingTop(50.pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries
        .beyondSmall(paddingTop(80.pxToEm()))
    )

  val headerInnerSubWrapper: StyleA =
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
      padding := s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
      verticalAlign.top,
      color(ThemeStyles.TextColor.white),
      ThemeStyles.MediaQueries.beyondMedium(width(150.pxToEm()), verticalAlign.middle)
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
      padding := s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
      ThemeStyles.MediaQueries.beyondMedium(verticalAlign.middle)
    )

  val title: StyleA =
    style(color(ThemeStyles.TextColor.white), ThemeStyles.MediaQueries.beyondMedium(textAlign.center))

  val totalOfPropositions: StyleA =
    style(color(ThemeStyles.TextColor.white), ThemeStyles.MediaQueries.beyondMedium(textAlign.center))

  val openProposalModalButtonWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      width(150.pxToEm()),
      padding := s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
      textAlign.right,
      ThemeStyles.MediaQueries
        .belowMedium(width.auto)
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
}
