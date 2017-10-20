package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.helpers.NumberFormat._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.mutable.StyleSheet.Inline

object ResultsOfVote {

  case class ResultsOfVoteProps(votes: Map[String, Int], index: Int)
  case class ResultsOfVoteState(votesAgree: Int, votesDisagree: Int, votesNeutral: Int)

  object ResultsOfVoteState {
    def apply(votes: Map[String, Int]): ResultsOfVoteState = {
      val votesAgree = votes.getOrElse("agree", 0)
      val votesDisagree = votes.getOrElse("disagree", 0)
      val votesNeutral = votes.getOrElse("neutral", 0)
      ResultsOfVoteState(votesAgree, votesDisagree, votesNeutral)
    }
  }

  lazy val reactClass: ReactClass =
    React.createClass[ResultsOfVoteProps, ResultsOfVoteState](displayName = "ResultsOfVote", getInitialState = { self =>
      ResultsOfVoteState(self.props.wrapped.votes)
    }, componentWillReceiveProps = { (self, props) =>
      self.setState(ResultsOfVoteState(props.wrapped.votes))
    }, render = {
      self =>
        val totalOfVotes: Int = self.state.votesAgree + self.state.votesDisagree + self.state.votesNeutral

        val index: Int = self.props.wrapped.index
        val partOfAgreeVotes: Int = formatToPercent(self.state.votesAgree, totalOfVotes)
        val partOfDisagreeVotes: Int = formatToPercent(self.state.votesDisagree, totalOfVotes)
        val partOfNeutralVotes: Int = formatToPercent(self.state.votesNeutral, totalOfVotes)

        object DynamicResultsOfVoteStyles extends Inline {
          import dsl._

          val resultsOfAgreeVote: Int => StyleA =
            styleF.int(Range(index, index + 1))(_ => styleS((&.after)(width(partOfAgreeVotes.%%))))

          val resultsOfDisagreeVote: Int => StyleA =
            styleF.int(Range(index, index + 1))(_ => styleS((&.after)(width(partOfDisagreeVotes.%%))))

          val resultsOfNeutralVote: Int => StyleA =
            styleF.int(Range(index, index + 1))(_ => styleS((&.after)(width(partOfNeutralVotes.%%))))
        }

        def resultsItem(specificClasses: String, totalOfVotes: Int, partOfVotes: Int): ReactElement = {
          <.li(^.className := ResultsOfVoteStyles.item)(
            <.p(^.className := Seq(ResultsOfVoteStyles.results.htmlClass, specificClasses).mkString(" "))(
              <.i()(),
              " ",
              <.em(^.className := TextStyles.boldText)(formatToKilo(totalOfVotes)),
              " ",
              unescape(
                I18n
                  .t("proposal.vote.partOfVotes", Replacements(("value", partOfVotes.toString)))
              )
            )
          )
        }

        <.ul(^.className := ResultsOfVoteStyles.wrapper)(
          resultsItem(
            Seq(
              ResultsOfVoteStyles.resultsOfAgreeVote.htmlClass,
              DynamicResultsOfVoteStyles.resultsOfAgreeVote(index).htmlClass
            ).mkString(" "),
            self.state.votesAgree,
            partOfAgreeVotes
          ),
          resultsItem(
            Seq(
              ResultsOfVoteStyles.resultsOfDisagreeVote.htmlClass,
              DynamicResultsOfVoteStyles.resultsOfDisagreeVote(index).htmlClass
            ).mkString(" "),
            self.state.votesDisagree,
            partOfDisagreeVotes
          ),
          resultsItem(
            Seq(
              ResultsOfVoteStyles.resultsOfNeutralVote.htmlClass,
              DynamicResultsOfVoteStyles.resultsOfNeutralVote(index).htmlClass
            ).mkString(" "),
            self.state.votesNeutral,
            partOfNeutralVotes
          ),
          <.style()(ResultsOfVoteStyles.render[String], DynamicResultsOfVoteStyles.render[String])
        )
    })

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
        height(5.pxToEm(12)),
        marginTop(2.pxToEm(12)),
        backgroundColor(ThemeStyles.TextColor.white)
      )
    )

  val resultsOfAgreeVote: StyleA =
    style(
      (&.after)(backgroundColor(ThemeStyles.ThemeColor.positive)),
      unsafeChild("i")((&.before)(content := "'\\F087'", ThemeStyles.Font.fontAwesome))
    )

  val resultsOfDisagreeVote: StyleA =
    style(
      (&.after)(backgroundColor(ThemeStyles.ThemeColor.negative)),
      unsafeChild("i")((&.before)(content := "'\\F088'", ThemeStyles.Font.fontAwesome))
    )

  val resultsOfNeutralVote: StyleA = style(
    unsafeChild("i")(
      display.inlineBlock,
      (&.before)(content := "'\\F087'", display.block, ThemeStyles.Font.fontAwesome, transform := s"rotate(-90deg)")
    )
  )
}
