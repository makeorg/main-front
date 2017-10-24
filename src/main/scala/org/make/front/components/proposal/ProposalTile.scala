package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalInfos.ProposalInfosProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.base.TextStyles

import scalacss.DevDefaults._

object ProposalTile {

  final case class ProposalTileProps(proposal: ProposalModel, index: Int)

  val reactClass: ReactClass =
    React
      .createClass[ProposalTileProps, Unit](
        displayName = "ProposalTile",
        render = (self) => {

          <.article(^.className := ProposalTileStyles.wrapper)(
            <.div(^.className := ProposalTileStyles.proposalInfosWrapper)(
              <.ProposalInfosComponent(^.wrapped := ProposalInfosProps(proposal = self.props.wrapped.proposal))()
            ),
            <.div(^.className := ProposalTileStyles.contentWrapper)(
              <.h3(^.className := Seq(TextStyles.mediumText, TextStyles.boldText))(
                <.Link(^.to := s"/proposal/${self.props.wrapped.proposal.slug}")(self.props.wrapped.proposal.content)
              ),
              <.VoteContainerComponent(
                ^.wrapped := VoteContainerProps(
                  proposal = self.props.wrapped.proposal,
                  index = self.props.wrapped.index
                )
              )()
            ),
            <.style()(ProposalTileStyles.render[String])
          )
        }
      )
}