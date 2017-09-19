package org.make.front.components.proposals.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposals.proposal.ProposalInfos.ProposalInfosProps
import org.make.front.components.proposals.vote.Vote
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.TextStyles

import scalacss.DevDefaults._

trait ProposalLocation

object Proposal {

  final case class ProposalProps(proposal: ProposalModel)

  val reactClass: ReactClass =
    React.createClass[ProposalProps, Unit](render = (self) => {

      <.article(^.className := ProposalStyles.wrapper)(
        <.header(^.className := ProposalStyles.header)(
          <.ProposalInfosComponent(^.wrapped := ProposalInfosProps(proposal = self.props.wrapped.proposal))()
        ),
        <.div(^.className := ProposalStyles.contentWrapper)(
          <.h3(^.className := Seq(TextStyles.mediumText, TextStyles.boldText))(self.props.wrapped.proposal.content),
          <.VoteComponent(
            ^.wrapped := Vote.VoteProps(
              voteAgreeStats = self.props.wrapped.proposal.voteAgree,
              voteDisagreeStats = self.props.wrapped.proposal.voteDisagree,
              voteNeutralStats = self.props.wrapped.proposal.voteNeutral
            )
          )()
        ),
        <.style()(ProposalStyles.render[String])
      )

    })
}
