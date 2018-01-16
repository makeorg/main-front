package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.submitProposal.SubmitProposalAndAuthenticateContainer.SubmitProposalAndAuthenticateContainerProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.I18n
import org.make.front.models.{
  Location,
  GradientColor     => GradientColorModel,
  OperationExpanded => OperationModel,
  Sequence          => SequenceModel
}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingLocation
import org.make.services.tracking.TrackingService.TrackingContext

import scalacss.internal.Attr

object SubmitProposalInRelationToOperation {

  case class SubmitProposalInRelationToOperationProps(operation: OperationModel,
                                                      onProposalProposed: () => Unit,
                                                      maybeSequence: Option[SequenceModel],
                                                      maybeLocation: Option[Location])

  case class SubmitProposalInRelationToOperationState(operation: OperationModel)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[SubmitProposalInRelationToOperationProps, SubmitProposalInRelationToOperationState](
        displayName = "SubmitProposalInRelationToOperation",
        getInitialState = { self =>
          SubmitProposalInRelationToOperationState(operation = self.props.wrapped.operation)
        },
        render = { self =>
          val gradientValues: GradientColorModel =
            self.state.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          object DynamicSubmitProposalInRelationToOperationStyles extends StyleSheet.Inline {
            import dsl._

            val titleBackground = style(
              background := s"-webkit-linear-gradient(94deg, ${gradientValues.from}, ${gradientValues.to})",
              Attr.real("-webkit-background-clip") := "text",
              Attr.real("-webkit-text-fill-color") := "transparent"
            )
          }

          val intro: (ReactElement) => ReactElement = {
            element =>
              <.div()(
                <.p(^.className := SubmitProposalInRelationToOperationStyles.title)(
                  <.span(
                    ^.className := Seq(
                      TextStyles.mediumText,
                      TextStyles.intro,
                      SubmitProposalInRelationToOperationStyles.intro
                    )
                  )(unescape(I18n.t("operation.submit-proposal.intro"))),
                  <.br()(),
                  <.strong(
                    ^.className := Seq(
                      TextStyles.veryBigTitle,
                      SubmitProposalInRelationToOperationStyles.operation,
                      DynamicSubmitProposalInRelationToOperationStyles.titleBackground
                    )
                  )(unescape(self.state.operation.wording.title))
                ),
                element,
                <.style()(
                  SubmitProposalInRelationToOperationStyles.render[String],
                  DynamicSubmitProposalInRelationToOperationStyles.render[String]
                )
              )
          }

          <.SubmitProposalAndAuthenticateContainerComponent(
            ^.wrapped :=
              SubmitProposalAndAuthenticateContainerProps(
                intro = intro,
                trackingContext =
                  TrackingContext(TrackingLocation.operationPage, Some(self.props.wrapped.operation.slug)),
                onProposalProposed = self.props.wrapped.onProposalProposed,
                maybeTheme = None,
                maybeOperation = Some(self.props.wrapped.operation),
                maybeSequence = self.props.wrapped.maybeSequence,
                maybeLocation = self.props.wrapped.maybeLocation
              )
          )()

        }
      )
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
      lineHeight(36.pxToEm(30)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(10.pxToEm(40)), lineHeight(56.pxToEm(40))),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(10.pxToEm(60)), lineHeight(70.pxToEm(60)))
    )
}
