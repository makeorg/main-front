package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.components.submitProposal.SubmitProposalAndLoginContainer.SubmitProposalAndLoginContainerProps
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{GradientColor => GradientColorModel, Operation => OperationModel}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._

import scalacss.DevDefaults._
import scalacss.internal.Attr
import scalacss.internal.mutable.StyleSheet

object SubmitProposalInRelationToOperation {

  case class SubmitProposalInRelationToOperationProps(operation: OperationModel, onProposalProposed: () => Unit)

  case class SubmitProposalInRelationToOperationState(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React.createClass[SubmitProposalInRelationToOperationProps, SubmitProposalInRelationToOperationState](
      displayName = "SubmitProposalInRelationToOperation",
      getInitialState = { self =>
        SubmitProposalInRelationToOperationState(operation = self.props.wrapped.operation)
      },
      render = { self =>
        val gradientColor: GradientColorModel =
          self.state.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

        val intro: (ReactElement) => ReactElement = {
          element =>
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
              element,
              <.style()(SubmitProposalInRelationToOperationStyles.render[String])
            )

        }

        <.SubmitProposalAndLoginComponent(
          ^.wrapped :=
            SubmitProposalAndLoginContainerProps(
              intro = intro,
              maybeTheme = None,
              maybeOperation = Some(self.props.wrapped.operation),
              onProposalProposed = self.props.wrapped.onProposalProposed
            )
        )()

      }
    )

}

object SubmitProposalInRelationToOperationStyles extends StyleSheet.Inline {

  import dsl._

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
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(10.pxToEm(40)), lineHeight(56.pxToEm(40))),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(10.pxToEm(60)), lineHeight(83.pxToEm(60)))
    )

  def gradientColor(from: String, to: String): StyleA =
    style(
      background := s"-webkit-linear-gradient(94deg, $from, $to)",
      Attr.real("-webkit-background-clip") := "text",
      Attr.real("-webkit-text-fill-color") := "transparent"
    )

}
