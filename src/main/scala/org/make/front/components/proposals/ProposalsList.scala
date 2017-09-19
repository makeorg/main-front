package org.make.front.components.proposals

import java.time.ZonedDateTime

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.Element
import org.make.front.components.Components._
import org.make.front.components.proposals.proposal.ProposalWithTags.ProposalWithTagsProps
import org.make.front.components.tags.FilterByTags.FilterByTagsProps
import org.make.front.facades.I18n
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{
  Author          => AuthorModel,
  Proposal        => ProposalModel,
  ProposalContext => ProposalContextModel,
  ProposalId      => ProposalIdModel,
  Qualification   => QualificationModel,
  Tag             => TagModel,
  TagId           => TagIdModel,
  ThemeId         => ThemeIdModel,
  UserId          => UserIdModel,
  Vote            => VoteModel
}
import org.make.front.styles.{CTAStyles, LayoutRulesStyles, TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object ProposalsList {

  val proposals: Seq[ProposalModel] = {
    for (i <- 1 to 2)
      yield
        ProposalModel(
          id = ProposalIdModel("abcd"),
          userId = UserIdModel("asdf"),
          content = "proposal content fetched from API",
          slug = "proposal",
          status = "",
          createdAt = ZonedDateTime.now(),
          updatedAt = None,
          voteAgree = VoteModel(
            key = "agree",
            count = 2500,
            qualifications = Seq(
              QualificationModel(key = "likeIt", count = 952),
              QualificationModel(key = "doable", count = 97),
              QualificationModel(key = "platitudeAgree", count = 7)
            )
          ),
          voteDisagree = VoteModel(
            key = "disagree",
            count = 660,
            qualifications = Seq(
              QualificationModel(key = "noWay", count = 320),
              QualificationModel(key = "impossible", count = 53),
              QualificationModel(key = "platitudeDisagree", count = 9)
            )
          ),
          voteNeutral = VoteModel(
            key = "neutral",
            count = 170,
            qualifications = Seq(
              QualificationModel(key = "doNotUnderstand", count = 74),
              QualificationModel(key = "noOpinion", count = 12),
              QualificationModel(key = "doNotCare", count = 3)
            )
          ),
          proposalContext = ProposalContextModel(operation = None, source = None, location = None, question = None),
          trending = None,
          labels = Seq.empty,
          author = AuthorModel(firstname = Some("Marco"), postalCode = Some("75"), age = Some(42)),
          country = "FR",
          language = "fr",
          themeId = Some(ThemeIdModel(i.toString)),
          tags = Seq(
            TagModel(tagId = TagIdModel("1"), label = "un"),
            TagModel(tagId = TagIdModel("2"), label = "deux"),
            TagModel(tagId = TagIdModel("3"), label = "trois")
          )
        )
  }

  type ProposalsListSelf = Self[ProposalsListProps, ProposalsListState]

  case class ProposalsListProps(handleSelectedTags: ProposalsListSelf => Seq[TagModel] => Unit, tags: Seq[TagModel])
  case class ProposalsListState(listProposals: Seq[ProposalModel], selectedTags: Seq[TagModel])

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
          )(<.ProposalWithTagsComponent(^.wrapped := ProposalWithTagsProps(proposal = proposal))())
      )

      val proposals: Element =
        <.div()(
          <.ul()(listProposals),
          <.div(^.className := Seq(ProposalsListStyles.seeMoreButtonWrapper, LayoutRulesStyles.col))(
            <.button(^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
              unescape(I18n.t("content.theme.seeMoreProposals"))
            )
          )
        )

      val noProposal: Element = <.div(^.className := ProposalsListStyles.noProposal)(
        <.p(^.className := ProposalsListStyles.noProposalSmiley)("😞"),
        <.p(
          ^.className := Seq(TextStyles.mediumText, ProposalsListStyles.noProposalMessage),
          ^.dangerouslySetInnerHTML := (I18n.t("content.theme.matrix.noContent") + "<br>" +
            I18n.t("content.theme.matrix.selectOtherTags"))
        )()
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

  val noProposalSmiley: StyleA =
    style(
      display.inlineBlock,
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm(34)),
      fontSize(34.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm(48)), fontSize(48.pxToEm()))
    )

  val noProposalMessage: StyleA =
    style(unsafeChild("strong")(ThemeStyles.Font.circularStdBold))

  val noProposal: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.larger.pxToEm()),
    marginBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
    textAlign.center
  )
}
