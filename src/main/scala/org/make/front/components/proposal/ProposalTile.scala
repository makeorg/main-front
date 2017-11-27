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
  Location        => LocationModel,
  Operation       => OperationModel,
  Proposal        => ProposalModel,
  Sequence        => SequenceModel,
  TranslatedTheme => TranslatedThemeModel
}
import org.make.front.styles.base.TextStyles

object ProposalTile {

  final case class ProposalTileProps(proposal: ProposalModel,
                                     index: Int,
                                     maybeTheme: Option[TranslatedThemeModel],
                                     maybeOperation: Option[OperationModel],
                                     maybeSequence: Option[SequenceModel],
                                     maybeLocation: Option[LocationModel])

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

            <.article(^.className := ProposalTileStyles.wrapper)(
              intro,
              <.div(^.className := ProposalTileStyles.contentWrapper)(
                <.h3(^.className := Seq(TextStyles.mediumText, TextStyles.boldText))(
                  <.Link(
                    ^.to := s"/proposal/${self.props.wrapped.proposal.slug}",
                    ^.className := ProposalTileStyles.proposalLinkOnTitle
                  )(self.props.wrapped.proposal.content)
                ),
                <.VoteContainerComponent(
                  ^.wrapped := VoteContainerProps(
                    proposal = self.props.wrapped.proposal,
                    index = self.props.wrapped.index,
                    maybeTheme = self.props.wrapped.maybeTheme,
                    maybeOperation = self.props.wrapped.maybeOperation,
                    maybeSequence = self.props.wrapped.maybeSequence,
                    maybeLocation = self.props.wrapped.maybeLocation
                  )
                )()
              ),
              <.style()(ProposalTileStyles.render[String])
            )
          }
        )
    )
}
