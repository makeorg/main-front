package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.helpers.NumberFormat._
import org.make.front.models.{Proposal => ProposalModel, Vote => VoteModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object ResultsOfVote {

  case class ResultsOfVoteProps(proposal: ProposalModel)

  lazy val reactClass: ReactClass =
    React.createClass[ResultsOfVoteProps, Unit](
      displayName = "ResultsOfVote",
      getInitialState = { self =>
        },
      render = (self) => {

        val voteAgree: VoteModel = self.props.wrapped.proposal.votesAgree
        val voteDisagree: VoteModel = self.props.wrapped.proposal.votesDisagree
        val voteNeutral: VoteModel = self.props.wrapped.proposal.votesNeutral
        val totalOfVotes: Int = self.props.wrapped.proposal.votesAgree.count +
          self.props.wrapped.proposal.votesDisagree.count +
          self.props.wrapped.proposal.votesNeutral.count

        <.ul(^.className := ResultsOfVoteStyles.wrapper)(
          <.li(^.className := ResultsOfVoteStyles.item)(
            <.p(^.className := Seq(ResultsOfVoteStyles.results, TextStyles.mediumText))(
              <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.thumbsUp))(),
              " ",
              <.em(^.className := TextStyles.boldText)(formatToKilo(voteAgree.count)),
              " (" +
                formatToPercent(voteAgree.count, totalOfVotes) + "%)"
            )
          ),
          <.li(^.className := ResultsOfVoteStyles.item)(
            <.p(^.className := Seq(ResultsOfVoteStyles.results, TextStyles.mediumText))(
              <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.thumbsDown))(),
              " ",
              <.em(^.className := TextStyles.boldText)(formatToKilo(voteDisagree.count)),
              " (" +
                formatToPercent(voteDisagree.count, totalOfVotes) + "%)"
            )
          ),
          <.li(^.className := ResultsOfVoteStyles.item)(
            <.p(^.className := Seq(ResultsOfVoteStyles.results, TextStyles.mediumText))(
              <.i(
                ^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.thumbsUp, ResultsOfVoteStyles.neutralIcon)
              )(),
              " ",
              <.em(^.className := TextStyles.boldText)(formatToKilo(voteNeutral.count)),
              " (" +
                formatToPercent(voteNeutral.count, totalOfVotes) + "%)"
            )
          ),
          <.style()(ResultsOfVoteStyles.render[String])
        )
      }
    )

}

object ResultsOfVoteStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(backgroundColor(rgb(74, 74, 74)), color(ThemeStyles.TextColor.white), textAlign.left)

  val item: StyleA =
    style( /*marginTop((ThemeStyles.SpacingValue.smaller).pxToEm()), (&.firstChild)(marginTop(`0`))*/ )

  val results: StyleA =
    style()

  val neutralIcon: StyleA = style((&.before)(display.block, transform := s"rotate(-90deg)"))

}
