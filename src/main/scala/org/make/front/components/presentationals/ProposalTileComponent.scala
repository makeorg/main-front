package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.presentationals.TagListComponent.TagListComponentProps
import org.make.front.models.Proposal
import org.make.front.styles.{BulmaStyles, MakeStyles}

import scalacss.DevDefaults._

object ProposalTileComponent {

  final case class ProposalTileProps(proposal: Proposal)

  def reactClass: ReactClass = React.createClass[ProposalTileProps, Unit](
   render = (self) =>
     <.div(^.className := Seq(
       BulmaStyles.Grid.Columns.column,
       BulmaStyles.Grid.Columns.isOneQuarterDesktop,
       BulmaStyles.Grid.Columns.isHalfTablet
     ))(
       <.div(^.className := ProposalTileStyle.proposalCard)(
         <.div(^.className := ProposalTileStyle.proposalUpPart)(
           <.div(^.className := ProposalTileStyle.proposalAuthor)(
             s"${self.props.wrapped.proposal.authorFirstname}, ${self.props.wrapped.proposal.authorAge} ans " +
               s"(${self.props.wrapped.proposal.authorPostalCode})"
           ),
           <.div(^.className := ProposalTileStyle.proposalText)(
             <.span()(
               self.props.wrapped.proposal.content
             )
           ),
           <.ButtonVoteComponent()()
         ),
         <.div(^.className := ProposalTileStyle.proposalTags)(
           <.TagListComponent(
             ^.wrapped := TagListComponentProps(
               tags = self.props.wrapped.proposal.tags,
               toggleShowAll = false
             )
           )()
         ),
         <.style()(ProposalTileStyle.render[String])
       )
     )
  )

}

object ProposalTileStyle extends StyleSheet.Inline {
  import dsl._

  val proposalCard: StyleA = style(
    display.flex,
    flexDirection.column,
    justifyContent.flexStart,
    flex := "1",
    height(100.%%),
    padding(1.5.rem),
    backgroundColor :=! "#fff",
    boxShadow := "0.1rem 0.1rem 0.1rem 0 rgba(0, 0, 0, .5)",
    (&.hover)(boxShadow := "0.1rem 0.1rem 2.5rem 0 rgba(0, 0, 0, .3)")
  )

  val proposalUpPart: StyleA = style(position.relative, flex := "1")

  val proposalAuthor: StyleA = style(
    marginBottom(1.rem),
    paddingBottom(0.6.rem),
    borderBottom :=! "0.1rem solid rgba(0, 0, 0, .1)",
    color :=! "rgba(0, 0, 0, .5)",
    fontSize(1.4.rem),
    MakeStyles.Font.circularStdBook
  )

  val proposalText: StyleA =
    style(marginBottom(1.rem), MakeStyles.Font.circularStdBold, fontWeight :=! "700", fontSize(1.6.rem))

  val proposalTags: StyleA =
    style(borderTop :=! "0.1rem solid rgba(0, 0, 0, .1)", paddingLeft(1.36.rem), textAlign.left)
}
