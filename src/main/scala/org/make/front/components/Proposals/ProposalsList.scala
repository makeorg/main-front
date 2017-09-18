package org.make.front.components.Proposals

import java.time.ZonedDateTime

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.Element
import org.make.front.components.Tags.FilterByTagsComponent.FilterByTagsProps
import org.make.front.components.presentationals._
import org.make.front.facades.I18n
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.facades.Unescape.unescape
import org.make.front.models._
import org.make.front.styles.TextStyles.title
import org.make.front.styles._

import scalacss.DevDefaults._
import scalacss.internal.Length
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

      val listProposals: Seq[Element] = self.state.listProposals.map(
        proposal =>
          <.li(
            ^.className := Seq(
              ProposalsListStyles.item,
              LayoutRulesStyles.col,
              LayoutRulesStyles.colHalfBeyondMedium,
              LayoutRulesStyles.colQuarterBeyondLarge
            )
          )(
            <.ProposalComponent(
              ^.wrapped := ProposalComponent
                .ProposalProps(proposal = proposal, proposalLocation = ThemePage, isHomePage = false, None)
            )()
        )
      )

      val proposals: Element =
        <.div()(
          <.ul()(listProposals),
          <.div(^.className := Seq(ProposalsListStyles.seeMoreButtonWrapper, LayoutRulesStyles.col))(
            <.button(^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
              unescape(I18n.t("content.theme.matrix.seeMoreProposals"))
            )
          )
        )

      val noProposal: Element = <.div(^.className := ProposalsListStyles.noProposal)(
        <.p(^.className := ProposalsListStyles.smiley)("😞"),
        <.p(^.className := TextStyles.mediumText)(
          unescape(I18n.t("content.theme.matrix.noContent")),
          <.br()(),
          unescape(I18n.t("content.theme.matrix.selectOtherTags"))
        )
      )

      <.section(^.className := ProposalsListStyles.wrapper)(
        <.div(^.className := LayoutRulesStyles.centeredRow)(
          <.header(^.className := LayoutRulesStyles.col)(
            <.h2(^.className := TextStyles.bigTitle)(<.Translate(^.value := "content.theme.matrix.title")())
          ),
          <.div(^.className := LayoutRulesStyles.col)(
            <.FilterByTagsComponent(
              ^.wrapped := FilterByTagsProps(self.props.wrapped.tags, self.props.wrapped.handleSelectedTags(self))
            )()
          ),
          if (self.state.listProposals.nonEmpty) {
            proposals
          } else {
            noProposal
          }
        ),
        <.style()(ProposalsListStyles.render[String])
      )
    }
  )
}

object ProposalsListStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0 ${ThemeStyles.SpacingValue.small.pxToEm().value}"
    )

  val item: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val seeMoreButtonWrapper: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
    marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
    textAlign.center
  )

  val smiley: StyleA =
    style(
      display.inlineBlock,
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm(34)),
      fontSize(34.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm(48)), fontSize(48.pxToEm()))
    )

  val noProposal: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.larger.pxToEm()),
    marginBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
    textAlign.center
  )
}
