package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalWithTags.ProposalWithTagsProps
import org.make.front.components.tags.FilterByTags.FilterByTagsProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Proposal => ProposalModel, Tag => TagModel}
import org.make.front.styles.{CTAStyles, LayoutRulesStyles, TextStyles, ThemeStyles}

import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object ResultsInTheme {

  type ProposalsListSelf = Self[ResultsInThemeProps, ResultsInThemeState]

  case class ResultsInThemeProps(handleSelectedTags: ProposalsListSelf => Seq[TagModel] => Unit,
                                 handleNextResults: ProposalsListSelf  => Unit,
                                 tags: Seq[TagModel])

  case class ResultsInThemeState(listProposals: Option[Seq[ProposalModel]] = None,
                                 selectedTags: Seq[TagModel],
                                 showSeeMoreButton: Boolean = true)

  lazy val reactClass: ReactClass =
    React.createClass[ResultsInThemeProps, ResultsInThemeState](
      getInitialState = (self) => ResultsInThemeState(selectedTags = self.props.wrapped.tags),
      render = { (self) =>
        val onSeeMore: () => Unit = () => self.props.wrapped.handleNextResults(self)

        val noResults: ReactElement =
          <.div(^.className := Seq(LayoutRulesStyles.col, ResultsInThemeStyles.noResults))(
            <.p(^.className := ResultsInThemeStyles.noResultsSmiley)("😞"),
            <.p(
              ^.className := Seq(TextStyles.mediumText, ResultsInThemeStyles.noResultsMessage),
              ^.dangerouslySetInnerHTML := I18n.t("content.theme.matrix.noContent")
            )()
          )

        def proposals(proposals: Seq[ProposalModel]) =
          Seq(
            <.ul()(
              proposals.map(
                proposal =>
                  <.li(
                    ^.className := Seq(
                      ResultsInThemeStyles.item,
                      LayoutRulesStyles.col,
                      LayoutRulesStyles.colHalfBeyondMedium,
                      LayoutRulesStyles.colQuarterBeyondLarge
                    )
                  )(<.ProposalWithTagsComponent(^.wrapped := ProposalWithTagsProps(proposal = proposal))())
              )
            ),
            if (self.state.showSeeMoreButton) {
              <.div(^.className := Seq(ResultsInThemeStyles.seeMoreButtonWrapper, LayoutRulesStyles.col))(
                <.button(^.onClick := onSeeMore, ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
                  unescape(I18n.t("content.theme.seeMoreProposals"))
                )
              )
            }
          )

        val proposalsToDisplay: Seq[ProposalModel] = self.state.listProposals.getOrElse(Seq.empty)

        <.section(^.className := Seq(LayoutRulesStyles.centeredRow, ResultsInThemeStyles.wrapper))(
          <.header(^.className := LayoutRulesStyles.col)(
            <.h2(^.className := TextStyles.bigTitle)(unescape(I18n.t("content.theme.matrix.title")))
          ),
          <.nav(^.className := LayoutRulesStyles.col)(
            <.FilterByTagsComponent(
              ^.wrapped := FilterByTagsProps(self.props.wrapped.tags, self.props.wrapped.handleSelectedTags(self))
            )()
          ),
          if (proposalsToDisplay.nonEmpty) {
            proposals(proposalsToDisplay)
          } else {
            noResults
          },
          <.style()(ResultsInThemeStyles.render[String])
        )
      }
    )
}

object ResultsInThemeStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA =
    style(
      padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0 ${ThemeStyles.SpacingValue.small.pxToEm().value}"
    )

  val item: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val seeMoreButtonWrapper: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
    marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
    textAlign.center
  )

  val noResults: StyleA = style(
    paddingTop(ThemeStyles.SpacingValue.larger.pxToEm()),
    paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
    textAlign.center
  )

  val noResultsSmiley: StyleA =
    style(
      display.inlineBlock,
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm(34)),
      fontSize(34.pxToEm()),
      lineHeight(1),
      ThemeStyles.MediaQueries
        .beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm(48)), fontSize(48.pxToEm()))
    )

  val noResultsMessage: StyleA =
    style(unsafeChild("strong")(ThemeStyles.Font.circularStdBold))

}
