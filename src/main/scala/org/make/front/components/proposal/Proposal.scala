package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalInfos.ProposalInfosProps
import org.make.front.components.proposal.vote.Vote
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.base.TextStyles

import scalacss.DevDefaults._

object Proposal {

  final case class ProposalProps(proposal: ProposalModel)

  val reactClass: ReactClass =
    React.createClass[ProposalProps, Unit](render = (self) => {

      <.article(^.className := ProposalStyles.wrapper)(
        <.header(^.className := ProposalStyles.proposalInfosWrapper)(
          <.ProposalInfosComponent(^.wrapped := ProposalInfosProps(proposal = self.props.wrapped.proposal))()
        ),
        <.div(^.className := ProposalStyles.contentWrapper)(
          <.h3(^.className := Seq(TextStyles.mediumText, TextStyles.boldText))(self.props.wrapped.proposal.content),
          <.VoteComponent(
            ^.wrapped := Vote.VoteProps(
              proposalId = self.props.wrapped.proposal.id,
              voteAgreeStats = self.props.wrapped.proposal.votesAgree,
              voteDisagreeStats = self.props.wrapped.proposal.votesDisagree,
              voteNeutralStats = self.props.wrapped.proposal.votesNeutral
            )
          )()
        ),
        <.style()(ProposalStyles.render[String])
      )

    })
}
