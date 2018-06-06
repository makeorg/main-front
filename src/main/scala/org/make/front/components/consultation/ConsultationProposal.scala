package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationWording, Location => LocationModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.utils._
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

object ConsultationProposal {

  case class ConsultationProposalProps(operation: OperationModel, maybeLocation: Option[LocationModel], language: String)

  case class ConsultationProposalState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[ConsultationProposalProps, ConsultationProposalState](
      displayName = "ConsultationProposal",
      getInitialState = { _ =>
        ConsultationProposalState(isProposalModalOpened = false)
      },
      render = (self) => {

        val consultation: OperationModel = self.props.wrapped.operation
        val wording: OperationWording = consultation.getWordingByLanguageOrError(self.props.wrapped.language)
        var proposalInput: Option[HTMLElement] = None

        val closeProposalModal: () => Unit = () => {
          self.setState(state => state.copy(isProposalModalOpened = false))
        }

        def openProposalModalFromInput() = () => {
          self.setState(state => state.copy(isProposalModalOpened = true))
          TrackingService
            .track(
              "click-proposal-submit-form-open",
              TrackingContext(TrackingLocation.operationPage, operationSlug = Some(consultation.slug))
            )
          proposalInput.foreach(_.blur())
        }

        <.div(^.className := (ConsultationProposalStyles.Wrapper))(
          <.p()(
            //Todo check translations with product team
            unescape(I18n.t("operation.consultation-proposal.title"))
          ),
          <.h1()(unescape(wording.question)),
          <.span(^.className := TableLayoutStyles.wrapper)(
            <.span(^.className := js.Array(TableLayoutStyles.cell))(
              <.input(
                ^.`type`.text,
                ^.value := I18n.t("common.bait"),
                ^.ref := ((input: HTMLElement) => proposalInput = Some(input)),
                ^.onFocus := openProposalModalFromInput(),
                ^.readOnly := true
              )()
            ),
            <.span(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
              <.span(^.className := js.Array(TextStyles.smallText))(
                I18n.t("operation.proposal-form-in-header.limit-of-chars-info")
              )
            )
          ),
          <.style()(ConsultationProposalStyles.render[String])
        )
      }
    )
}

object ConsultationProposalStyles extends StyleSheet.Inline {
  import dsl._

  val Wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.white),
      padding(30.pxToEm())
    )
}