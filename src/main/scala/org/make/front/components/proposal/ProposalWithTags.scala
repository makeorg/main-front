package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalInfos.ProposalInfosProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.TagStyles
import org.make.front.styles.utils._

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object ProposalWithTags {

  final case class ProposalWithTagsProps(proposal: ProposalModel)

  val reactClass: ReactClass =
    React
      .createClass[ProposalWithTagsProps, Unit](
        displayName = "ProposalWithTags",
        render = (self) => {

          <.article(^.className := ProposalStyles.wrapper)(
            <.div(^.className := ProposalStyles.innerWrapper)(
              <.div(^.className := ProposalStyles.row)(
                <.div(^.className := ProposalStyles.cell)(
                  <.header(^.className := ProposalStyles.proposalInfosWrapper)(
                    <.ProposalInfosComponent(^.wrapped := ProposalInfosProps(proposal = self.props.wrapped.proposal))()
                  )
                  /*<.header(^.className := ProposalStyles.shareOwnProposalWrapper)(
                    <.ShareOwnProposalComponent(^.wrapped := ShareOwnProposalProps(proposal = self.props.wrapped.proposal))()
                  )*/
                )
              ),
              <.div(^.className := Seq(ProposalStyles.row, ProposalStyles.stretchedRow))(
                <.div(^.className := Seq(ProposalStyles.cell, ProposalStyles.contentWrapper))(
                  <.h3(^.className := Seq(TextStyles.mediumText, TextStyles.boldText))(
                    self.props.wrapped.proposal.content
                  ),
                  <.VoteContainerComponent(^.wrapped := VoteContainerProps(proposal = self.props.wrapped.proposal))()
                )
              ),
              if (self.props.wrapped.proposal.tags.nonEmpty) {
                <.div(^.className := ProposalStyles.row)(
                  <.div(^.className := ProposalStyles.cell)(
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
                    )
                  )
                )
              }
            ),
            <.style()(ProposalStyles.render[String], ProposalWithTagsStyles.render[String])
          )
        }
      )
}

object ProposalWithTagsStyles extends StyleSheet.Inline {

  import dsl._

  val tagList: StyleA =
    style(lineHeight(0), margin :=! s"-${(ThemeStyles.SpacingValue.smaller / 2).pxToEm().value}")

  val tagListItem: StyleA =
    style(display.inlineBlock, verticalAlign.middle, margin((ThemeStyles.SpacingValue.smaller / 2).pxToEm()))

}
