package org.make.front.components.presentationals

import java.time.ZonedDateTime

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.Element
import org.make.front.components.presentationals.TagFilterComponent.TagFilterProps
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.models._
import org.make.front.styles.MakeStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object ProposalMatrixComponent {

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

  type ProposalMatrixSelf = Self[ProposalMatrixProps, ProposalMatrixState]

  case class ProposalMatrixProps(handleSelectedTags: ProposalMatrixSelf => Seq[Tag] => Unit, tags: Seq[Tag])
  case class ProposalMatrixState(listProposals: Seq[Proposal], selectedTags: Seq[Tag])

  lazy val reactClass: ReactClass = React.createClass[ProposalMatrixProps, ProposalMatrixState](
    getInitialState = (_) => ProposalMatrixState(proposals, Seq.empty),
    render = (self) => {
      val noContent: Element = <.div(^.className := ProposalMatrixStyles.noContent)(
        <.span(^.className := ProposalMatrixStyles.sadSmiley)("😞"),
        <.br()(),
        <.Translate(^.value := "content.theme.matrix.noContent", ^.dangerousHtml := true)(),
        <.br()(),
        <.Translate(^.value := "content.theme.matrix.selectOtherTags")()
      )

      val listProposals: Seq[Element] = self.state.listProposals.map(
        proposal =>
          <.li(^.className := ProposalMatrixStyles.proposalItem)(
            <.ProposalTileComponent(
              ^.wrapped := ProposalTileComponent
                .ProposalTileProps(proposal = proposal, proposalLocation = ThemePage, isHomePage = false, None)
            )()
        )
      )

      <.div(^.className := ProposalMatrixStyles.matrix)(
        <.h2(^.className := ProposalMatrixStyles.title2)(<.Translate(^.value := "content.theme.matrix.title")()),
        <.TagFilterComponent(
          ^.wrapped := TagFilterProps(self.props.wrapped.tags, self.props.wrapped.handleSelectedTags(self))
        )(),
        <.div(^.className := ProposalMatrixStyles.proposalList)(if (self.state.listProposals.nonEmpty) {
          <.ul()(listProposals)
        } else {
          noContent
        }),
        <.style()(ProposalMatrixStyles.render[String])
      )
    }
  )
}

object ProposalMatrixStyles extends StyleSheet.Inline {

  import dsl._

  val title2: StyleA = style(MakeStyles.title2, MakeStyles.Font.tradeGothicLTStd, marginTop(3.rem))
  val matrix: StyleA = style(minHeight(50.rem))
  val proposalList: StyleA = style(marginTop(4.rem))
  val proposalItem: StyleA =
    style(display.inlineBlock, verticalAlign.top, width(25.%%), padding :=! MakeStyles.Spacing.small)
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
