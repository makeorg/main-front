package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalInfos.ProposalInfosProps
import org.make.front.components.proposal.ShareOwnProposal.ShareOwnProposalProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.models.{
  SequenceId,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.front.styles.base.TextStyles
import org.make.services.tracking.TrackingLocation

import scala.scalajs.js

object ProposalTile {

  final case class ProposalTileProps(proposal: ProposalModel,
                                     index: Int,
                                     maybeTheme: Option[TranslatedThemeModel],
                                     maybeOperation: Option[OperationModel],
                                     maybeSequenceId: Option[SequenceId],
                                     maybeLocation: Option[LocationModel],
                                     trackingLocation: TrackingLocation,
                                     country: String)

  val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[ProposalTileProps, Unit](
          displayName = "ProposalTile",
          render = (self) => {

            val intro: ReactElement = if (self.props.wrapped.proposal.myProposal) {
              <.div(^.className := ProposalTileStyles.shareOwnProposalWrapper)(
                <.ShareOwnProposalComponent(
                  ^.wrapped := ShareOwnProposalProps(proposal = self.props.wrapped.proposal)
                )()
              )
            } else {
              <.div(^.className := ProposalTileStyles.proposalInfosWrapper)(
                <.ProposalInfosComponent(^.wrapped := ProposalInfosProps(proposal = self.props.wrapped.proposal))()
              )
            }

            val proposalLink: String = self.props.wrapped.maybeOperation match {
              case Some(operationExpanded) =>
                s"/${self.props.wrapped.country}/consultation/${operationExpanded.slug}/proposal/${self.props.wrapped.proposal.slug}"
              case _ =>
                self.props.wrapped.maybeTheme match {
                  case Some(theme) =>
                    s"/${self.props.wrapped.country}/theme/${theme.slug}/proposal/${self.props.wrapped.proposal.slug}"
                  case _ => s"/${self.props.wrapped.country}/proposal/${self.props.wrapped.proposal.slug}"
                }

            }

            <.article(^.className := ProposalTileStyles.wrapper)(
              intro,
              <.div(^.className := ProposalTileStyles.contentWrapper)(
                <.h3(^.className := js.Array(TextStyles.mediumText, TextStyles.boldText))(
                  <.Link(^.to := proposalLink, ^.className := ProposalTileStyles.proposalLinkOnTitle)(
                    self.props.wrapped.proposal.content
                  )
                ),
                <.VoteContainerComponent(
                  ^.wrapped := VoteContainerProps(
                    proposal = self.props.wrapped.proposal,
                    index = self.props.wrapped.index,
                    maybeTheme = self.props.wrapped.maybeTheme,
                    maybeOperation = self.props.wrapped.maybeOperation,
                    maybeSequenceId = self.props.wrapped.maybeSequenceId,
                    maybeLocation = self.props.wrapped.maybeLocation,
                    trackingLocation = self.props.wrapped.trackingLocation
                  )
                )()
              ),
              <.style()(ProposalTileStyles.render[String])
            )
          }
        )
    )
}
