package org.make.front.components.Proposals

import java.time.ZonedDateTime

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.Element
import org.make.front.components.Tags.FilterByTagsComponent.FilterByTagsProps
import org.make.front.components.presentationals._
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.models._
import org.make.front.styles.{MakeStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object ProposalsListComponent {

  val proposals: Seq[Proposal] = {
    for (i <- 1 to 2)
      yield
        Proposal(
          id = ProposalId("abcd"),
          userId = UserId("asdf"),
          content = "proposal content fetched from API",
          slug = "proposal",
          status = "",
          createdAt = ZonedDateTime.now(),
          updatedAt = None,
          voteAgree = Vote(
            key = "agree",
            count = 2500,
            qualifications = Seq(
              Qualification(key = "likeIt", count = 952),
              Qualification(key = "doable", count = 97),
              Qualification(key = "platitudeAgree", count = 7)
            )
          ),
          voteDisagree = Vote(
            key = "disagree",
            count = 660,
            qualifications = Seq(
              Qualification(key = "noWay", count = 320),
              Qualification(key = "impossible", count = 53),
              Qualification(key = "platitudeDisagree", count = 9)
            )
          ),
          voteNeutral = Vote(
            key = "neutral",
            count = 170,
            qualifications = Seq(
              Qualification(key = "doNotUnderstand", count = 74),
              Qualification(key = "noOpinion", count = 12),
              Qualification(key = "doNotCare", count = 3)
            )
          ),
          proposalContext = ProposalContext(operation = None, source = None, location = None, question = None),
          trending = None,
          labels = Seq.empty,
          author = Author(firstname = Some("Marco"), postalCode = Some("75"), age = Some(42)),
          country = "FR",
          language = "fr",
          themeId = Some(ThemeId(i.toString)),
          tags = Seq(
            Tag(tagId = TagId("1"), label = "un"),
            Tag(tagId = TagId("2"), label = "deux"),
            Tag(tagId = TagId("3"), label = "trois")
          )
        )
  }

  type ProposalsListSelf = Self[ProposalsListProps, ProposalsListState]

  case class ProposalsListProps(handleSelectedTags: ProposalsListSelf => Seq[Tag] => Unit, tags: Seq[Tag])
  case class ProposalsListState(listProposals: Seq[Proposal], selectedTags: Seq[Tag])

  lazy val reactClass: ReactClass = React.createClass[ProposalsListProps, ProposalsListState](
    getInitialState = (_) => ProposalsListState(proposals, Seq.empty),
    render = (self) => {
      val noContent: Element = <.div(^.className := ProposalsListStyles.noContent)(
        <.span(^.className := ProposalsListStyles.sadSmiley)("😞"),
        <.br()(),
        <.Translate(^.value := "content.theme.matrix.noContent", ^.dangerousHtml := true)(),
        <.br()(),
        <.Translate(^.value := "content.theme.matrix.selectOtherTags")()
      )

      val listProposals: Seq[Element] = self.state.listProposals.map(
        proposal =>
          <.li(^.className := ProposalsListStyles.proposalItem)(
            <.ProposalComponent(
              ^.wrapped := ProposalComponent
                .ProposalProps(proposal = proposal, proposalLocation = ThemePage, isHomePage = false, None)
            )()
        )
      )

      <.div(^.className := ProposalsListStyles.matrix)(
        <.h2(^.className := ProposalsListStyles.title2)(<.Translate(^.value := "content.theme.matrix.title")()),
        <.FilterByTagsComponent(
          ^.wrapped := FilterByTagsProps(self.props.wrapped.tags, self.props.wrapped.handleSelectedTags(self))
        )(),
        <.div(^.className := ProposalsListStyles.proposalList)(if (self.state.listProposals.nonEmpty) {
          <.ul()(listProposals)
        } else {
          noContent
        }),
        <.style()(ProposalsListStyles.render[String])
      )
    }
  )
}

object ProposalsListStyles extends StyleSheet.Inline {

  import dsl._

  val title2: StyleA = style(MakeStyles.title2, MakeStyles.Font.tradeGothicLTStd, marginTop(3.rem))
  val matrix: StyleA = style(minHeight(50.rem))
  val proposalList: StyleA = style(marginTop(4.rem))
  val proposalItem: StyleA =
    style(display.inlineBlock, verticalAlign.top, width(25.%%), padding :=! ThemeStyles.Spacing.small)
  val sadSmiley: StyleA = style(fontSize(4.8.rem))
  val noContent: StyleA =
    style(
      marginTop(10.rem),
      width(100.%%),
      textAlign.center,
      fontSize(1.8.rem),
      lineHeight(3.4.rem),
      MakeStyles.Font.circularStdBook
    )

}
