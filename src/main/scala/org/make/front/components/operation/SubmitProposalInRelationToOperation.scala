package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.submitProposal.SubmitProposalAndLoginContainer.SubmitProposalAndLoginContainerProps
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{GradientColor => GradientColorModel, Operation => OperationModel}
import org.make.front.styles.{TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}

object SubmitProposalInRelationToOperation {

  case class SubmitProposalInRelationToOperationProps(operation: OperationModel, onProposalProposed: () => Unit)

  case class SubmitProposalInRelationToOperationState(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React.createClass[SubmitProposalInRelationToOperationProps, SubmitProposalInRelationToOperationState](
      displayName = getClass.toString,
      getInitialState = { self =>
        SubmitProposalInRelationToOperationState(operation = self.props.wrapped.operation)
      },
      render = { self =>
        val gradientColor: GradientColorModel =
          self.state.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

        <.article()(
          <.h2(^.className := SubmitProposalInRelationToOperationStyles.title)(
            <.span(
              ^.className := Seq(
                TextStyles.mediumText,
                TextStyles.intro,
                SubmitProposalInRelationToOperationStyles.intro
              )
            )("Partagez votre proposition"),
            <.br()(),
            <.strong(
              ^.className := Seq(
                TextStyles.veryBigTitle,
                SubmitProposalInRelationToOperationStyles.operation,
                SubmitProposalInRelationToOperationStyles.gradientColor(gradientColor.from, gradientColor.to)
              )
            )(unescape(self.state.operation.title))
          ),
          <.SubmitProposalAndLoginComponent(
            ^.wrapped :=
              SubmitProposalAndLoginContainerProps(
                bait = "il faut",
                proposalContentMaxLength = 140,
                maybeTheme = None,
                maybeOperation = Some(self.props.wrapped.operation),
                onProposalProposed = self.props.wrapped.onProposalProposed
              )
          )(),
          <.style()(SubmitProposalInRelationToOperationStyles.render[String])
        )
      }
    )

}

object SubmitProposalInRelationToOperationStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val title: StyleA =
    style(textAlign.center)

  val intro: StyleA =
    style(
      display.inlineBlock,
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(15)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(18)))
    )

  val operation: StyleA =
    style(
      display.inlineBlock,
      marginBottom(15.pxToEm(30)),
      lineHeight(41.pxToEm(30)),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(10.pxToEm(60)), lineHeight(83.pxToEm(60)))
    )

  def gradientColor(from: String, to: String): StyleA =
    style(
      background := s"-webkit-linear-gradient(94deg, $from, $to)",
      Attr.real("-webkit-background-clip") := "text",
      Attr.real("-webkit-text-fill-color") := "transparent"
    )

}
