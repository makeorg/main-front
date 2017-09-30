package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalInfos.ProposalInfosProps
import org.make.front.components.proposal.vote.Vote
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.base.TextStyles
import org.make.front.styles._

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object ProposalWithTags {

  final case class ProposalWithTagsProps(proposal: ProposalModel)

  val reactClass: ReactClass =
    React.createClass[ProposalWithTagsProps, Unit](render = (self) => {

      <.article(^.className := ProposalStyles.wrapper)(
        <.header(^.className := ProposalStyles.proposalInfosWrapper)(
          <.ProposalInfosComponent(^.wrapped := ProposalInfosProps(proposal = self.props.wrapped.proposal))()
        ),
        /*<.header(^.className := ProposalStyles.shareOwnProposalWrapper)(
          <.ShareOwnProposalComponent(^.wrapped := ShareOwnProposalProps(proposal = self.props.wrapped.proposal))()
          ),*/
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
        <.footer(^.className := ProposalStyles.footer)(
          <.ul(^.className := ProposalWithTagsStyles.tagList)(
            self.props.wrapped.proposal.tags
              .map(
                tag =>
                  <.li(^.className := ProposalWithTagsStyles.tagListItem)(
                    <.span(^.className := TagStyles.basic)(tag.label)
                )
              )
          )
        ),
        <.style()(ProposalStyles.render[String], ProposalWithTagsStyles.render[String])
      )
    })
}

object ProposalWithTagsStyles extends StyleSheet.Inline {

  import dsl._

  val tagList: StyleA =
    style(lineHeight(0), margin :=! s"-${(ThemeStyles.SpacingValue.smaller / 2).pxToEm().value}")

  val tagListItem: StyleA =
    style(display.inlineBlock, verticalAlign.middle, margin((ThemeStyles.SpacingValue.smaller / 2).pxToEm()))

}
