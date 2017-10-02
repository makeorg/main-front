package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.helpers.NumberFormat._
import org.make.front.models.{Vote => VoteModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.mutable.StyleSheet.Inline

object ResultsOfVote {

  case class ResultsOfVoteProps(votes: Seq[VoteModel])

  lazy val reactClass: ReactClass =
    React.createClass[ResultsOfVoteProps, Unit](
      displayName = "ResultsOfVote",
      getInitialState = { self =>
        },
      render = (self) => {

        val voteAgree: VoteModel = self.props.wrapped.votes.find(_.key == "agree").get
        val voteDisagree: VoteModel = self.props.wrapped.votes.find(_.key == "disagree").get
        val voteNeutral: VoteModel = self.props.wrapped.votes.find(_.key == "neutral").get
        val totalOfVotes: Int = voteAgree.count +
          voteDisagree.count +
          voteNeutral.count

        val partOfAgreeVote: Int = formatToPercent(voteAgree.count, totalOfVotes.toInt)
        val partOfDisagreeVote: Int = formatToPercent(voteDisagree.count, totalOfVotes.toInt)
        val partOfNeutralVote: Int = formatToPercent(voteNeutral.count, totalOfVotes.toInt)

        object DynamicResultsOfVoteStyles extends Inline {
          import dsl._

          val resultsOfAgreeVote: StyleA =
            style((&.after)(width(partOfAgreeVote.%%)))

          val resultsOfDisagreeVote: StyleA =
            style((&.after)(width(partOfDisagreeVote.%%)))

          val resultsOfNeutralVote: StyleA =
            style((&.after)(width(partOfNeutralVote.%%)))
        }

        <.ul(^.className := ResultsOfVoteStyles.wrapper)(
          <.li(^.className := ResultsOfVoteStyles.item)(
            <.p(
              ^.className := Seq(
                ResultsOfVoteStyles.results,
                ResultsOfVoteStyles.resultsOfAgreeVote,
                DynamicResultsOfVoteStyles.resultsOfAgreeVote
              )
            )(
              <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.thumbsUp))(),
              " ",
              <.em(^.className := TextStyles.boldText)(formatToKilo(voteAgree.count)),
              " (" + partOfAgreeVote + "%)"
            )
          ),
          <.li(^.className := ResultsOfVoteStyles.item)(
            <.p(
              ^.className := Seq(
                ResultsOfVoteStyles.results,
                ResultsOfVoteStyles.resultsOfDisagreeVote,
                DynamicResultsOfVoteStyles.resultsOfDisagreeVote
              )
            )(
              <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.thumbsDown))(),
              " ",
              <.em(^.className := TextStyles.boldText)(formatToKilo(voteDisagree.count)),
              " (" + partOfDisagreeVote + "%)"
            )
          ),
          <.li(^.className := ResultsOfVoteStyles.item)(
            <.p(^.className := Seq(ResultsOfVoteStyles.results, DynamicResultsOfVoteStyles.resultsOfNeutralVote))(
              <.i(
                ^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.thumbsUp, ResultsOfVoteStyles.neutralIcon)
              )(),
              " ",
              <.em(^.className := TextStyles.boldText)(formatToKilo(voteNeutral.count)),
              " (" + partOfNeutralVote + "%)"
            )
          ),
          <.style()(ResultsOfVoteStyles.render[String], DynamicResultsOfVoteStyles.render[String])
        )
      }
    )

}

object ResultsOfVoteStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(backgroundColor(rgb(74, 74, 74)), color(ThemeStyles.TextColor.white), textAlign.left)

  val item: StyleA =
    style(marginTop((ThemeStyles.SpacingValue.smaller / 2).pxToEm()), (&.firstChild)(marginTop(`0`)))

  val results: StyleA =
    style(
      ThemeStyles.Font.circularStdBook,
      fontSize(12.pxToEm()),
      (&.after)(
        content := "' '",
        display.block,
        width(100.%%),
        height(5.pxToEm(12)),
        marginTop(2.pxToEm(12)),
        backgroundColor(ThemeStyles.TextColor.white)
      )
    )

  val resultsOfAgreeVote: StyleA =
    style((&.after)(backgroundColor(ThemeStyles.ThemeColor.positive)))

  val resultsOfDisagreeVote: StyleA =
    style((&.after)(backgroundColor(ThemeStyles.ThemeColor.negative)))

  val neutralIcon: StyleA = style((&.before)(display.block, transform := s"rotate(-90deg)"))

}
