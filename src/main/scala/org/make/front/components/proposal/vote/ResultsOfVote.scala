package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.helpers.NumberFormat._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.mutable.StyleSheet.Inline

object ResultsOfVote {

  case class ResultsOfVoteProps(votes: Map[String, Int])
  case class ResultsOfVoteState(votesAgree: Int, votesDisagree: Int, votesNeutral: Int)

  lazy val reactClass: ReactClass =
    React.createClass[ResultsOfVoteProps, ResultsOfVoteState](
      displayName = "ResultsOfVote",
      getInitialState = { self =>
        val votes = self.props.wrapped.votes
        val votesAgree = votes.getOrElse("agree", 0)
        val votesDisagree = votes.getOrElse("disagree", 0)
        val votesNeutral = votes.getOrElse("neutral", 0)

        ResultsOfVoteState(votesAgree, votesDisagree, votesNeutral)
      },
      componentWillReceiveProps = { (self, props) =>
        val votes = props.wrapped.votes
        val votesAgree = votes.getOrElse("agree", 0)
        val votesDisagree = votes.getOrElse("disagree", 0)
        val votesNeutral = votes.getOrElse("neutral", 0)

        self.setState(ResultsOfVoteState(votesAgree, votesDisagree, votesNeutral))
      },
      render = { self =>
        val totalOfVotes: Int = self.state.votesAgree + self.state.votesDisagree + self.state.votesNeutral

        val partOfAgreeVote: Int = formatToPercent(self.state.votesAgree, totalOfVotes)
        val partOfDisagreeVote: Int = formatToPercent(self.state.votesDisagree, totalOfVotes)
        val partOfNeutralVote: Int = formatToPercent(self.state.votesNeutral, totalOfVotes)

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
              <.em(^.className := TextStyles.boldText)(formatToKilo(self.state.votesAgree)),
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
              <.em(^.className := TextStyles.boldText)(formatToKilo(self.state.votesDisagree)),
              " (" + partOfDisagreeVote + "%)"
            )
          ),
          <.li(^.className := ResultsOfVoteStyles.item)(
            <.p(^.className := Seq(ResultsOfVoteStyles.results, DynamicResultsOfVoteStyles.resultsOfNeutralVote))(
              <.i(
                ^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.thumbsUp, ResultsOfVoteStyles.neutralIcon)
              )(),
              " ",
              <.em(^.className := TextStyles.boldText)(formatToKilo(self.state.votesNeutral)),
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
