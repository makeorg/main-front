package org.make.front.components.proposals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.Element
import org.make.front.components.Components._
import org.make.front.components.proposals.proposal.ProposalWithTags.ProposalWithTagsProps
import org.make.front.components.tags.FilterByTags.FilterByTagsProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Proposal => ProposalModel, Tag => TagModel}
import org.make.front.styles.{CTAStyles, LayoutRulesStyles, TextStyles, ThemeStyles}

import org.make.front.Main.CssSettings._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object ProposalsList {

  type ProposalsListSelf = Self[ProposalsListProps, ProposalsListState]

  case class ProposalsListProps(handleSelectedTags: ProposalsListSelf      => Seq[TagModel] => Unit,
                                handleSearchValueChange: ProposalsListSelf => Unit,
                                handleNextResults: ProposalsListSelf       => Unit,
                                tags: Seq[TagModel],
                                showTagsSelect: Boolean = true,
                                noContentText: () => String,
                                searchValue: ()   => Option[String])
  case class ProposalsListState(listProposals: Option[Seq[ProposalModel]] = None,
                                selectedTags: Seq[TagModel],
                                showTagsSelect: Boolean,
                                noContentText: () => String,
                                showSeeMoreButton: Boolean = true,
                                searchValue: Option[String] = None)

  lazy val reactClass: ReactClass =
    React.createClass[ProposalsListProps, ProposalsListState](
      getInitialState = (self) =>
        ProposalsListState(
          selectedTags = self.props.wrapped.tags,
          showTagsSelect = self.props.wrapped.showTagsSelect,
          noContentText = self.props.wrapped.noContentText,
          searchValue = self.props.wrapped.searchValue()
      ),
      componentDidMount = (self) => {
        self.props.wrapped.handleSearchValueChange(self)
      },
      componentDidUpdate = (self, _, _) => {
        if (self.state.searchValue != self.props.wrapped.searchValue()) {
          self.props.wrapped.handleSearchValueChange(self)
        }
      },
      render = (self) => {

        def listProposals(proposals: Seq[ProposalModel]): Seq[Element] = proposals.map(
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

        def onSeeMore = () => self.props.wrapped.handleNextResults(self)

        def proposals(proposals: Seq[ProposalModel]): Element =
          <.div()(<.ul()(listProposals(proposals)), if (self.state.showSeeMoreButton) {
            <.div(^.className := Seq(ProposalsListStyles.seeMoreButtonWrapper, LayoutRulesStyles.col))(
              <.button(^.onClick := onSeeMore, ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
                unescape(I18n.t("content.theme.seeMoreProposals"))
              )
            )
          })

        val noProposal: Element = <.div(^.className := ProposalsListStyles.noProposal)(
          <.p(^.className := ProposalsListStyles.noProposalSmiley)("😞"),
          <.p(
            ^.className := Seq(TextStyles.mediumText, ProposalsListStyles.noProposalMessage),
            ^.dangerouslySetInnerHTML := self.state.noContentText()
          )()
        )

        val proposalsToDisplay: Seq[ProposalModel] = self.state.listProposals.getOrElse(Seq.empty)

        <.div(^.className := LayoutRulesStyles.col)(<.div()(if (self.state.showTagsSelect) {
          <.FilterByTagsComponent(
            ^.wrapped := FilterByTagsProps(self.props.wrapped.tags, self.props.wrapped.handleSelectedTags(self))
          )()
        }, if (proposalsToDisplay.nonEmpty) {
          proposals(proposalsToDisplay)
        } else {
          noProposal
        }), <.style()(ProposalsListStyles.render[String]))
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
    style(unsafeChild("strong")(ThemeStyles.Font.circularStdBold), unsafeChild("em")(TextStyles.title))

  val noProposal: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.larger.pxToEm()),
    marginBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
    textAlign.center
  )
}
