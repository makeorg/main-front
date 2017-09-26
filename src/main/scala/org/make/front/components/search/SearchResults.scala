package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.helpers.QueryString
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.components.proposal.ProposalWithTags.ProposalWithTagsProps
import org.make.front.components.submitProposal.SubmitProposal.SubmitProposalProps
import org.make.front.styles._

import scala.scalajs.js.URIUtils
import scalacss.DevDefaults._
import scalacss.internal.Length

object SearchResults {
  final case class SearchResultsProps(searchValue: Option[String])
  case class SearchResultsState(searchValue: Option[String] = None,
                                resultsCount: Option[Int] = None,
                                listProposals: Option[Seq[ProposalModel]] = None,
                                isProposalModalOpened: Boolean = false)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[SearchResultsProps, SearchResultsState](displayName = getClass.toString, getInitialState = {
        (self) =>
          val search = QueryString.parse(self.props.location.search)
          val searchValue: Option[String] = search.get("q").map(URIUtils.decodeURI)
          SearchResultsState(searchValue = searchValue)
      }, componentWillReceiveProps = (self, nextProps) => {
        val search = QueryString.parse(nextProps.location.search)
        val newSearchValue = search.get("q").map(URIUtils.decodeURI)
        self.setState(_.copy(searchValue = newSearchValue))
      }, render = {
        (self) =>
          val closeProposalModal: () => Unit = () => {
            self.setState(state => state.copy(isProposalModalOpened = false))
          }

          val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
            event.preventDefault()
            self.setState(state => state.copy(isProposalModalOpened = true))
          }

          val searchValue: Option[String] = self.state.searchValue

          val noResults: ReactElement = {
            <.div(^.className := LayoutRulesStyles.centeredRow)(
              <.div(^.className := Seq(LayoutRulesStyles.col, SearchResultsStyles.noResults))(
                <.p(^.className := SearchResultsStyles.noResultsSmiley)("😞"),
                <.p(
                  ^.className := Seq(TextStyles.mediumText, SearchResultsStyles.noResultsMessage),
                  ^.dangerouslySetInnerHTML := I18n.t(s"content.search.matrix.noContent")
                )(),
                <.p(^.className := Seq(SearchResultsStyles.searchedExpression, TextStyles.mediumTitle))(
                  unescape("«&nbsp;" + self.state.searchValue.get + "&nbsp;»")
                ),
                <.hr(^.className := SearchResultsStyles.noResultsMessageSeparator)(),
                <.p(^.className := Seq(TextStyles.mediumText))(I18n.t("content.search.proposeIntro")),
                <.button(
                  ^.className := Seq(
                    SearchResultsStyles.openProposalModalButton,
                    CTAStyles.basic,
                    CTAStyles.basicOnButton
                  ),
                  ^.onClick := openProposalModal
                )(
                  <.i(^.className := FontAwesomeStyles.pencil)(),
                  unescape("&nbsp;" + I18n.t("content.search.propose"))
                ),
                <.FullscreenModalComponent(
                  ^.wrapped := FullscreenModalProps(self.state.isProposalModalOpened, closeProposalModal)
                )(<.SubmitProposalComponent(^.wrapped := SubmitProposalProps(onProposalProposed = () => {
                  self.setState(_.copy(isProposalModalOpened = false))
                }))())
              )
            )
          }

          def proposals(proposals: Seq[ProposalModel]) =
            Seq(
              <.ul(^.className := LayoutRulesStyles.centeredRow)(
                proposals.map(
                  proposal =>
                    <.li(
                      ^.className := Seq(
                        LayoutRulesStyles.col,
                        LayoutRulesStyles.colHalfBeyondMedium,
                        LayoutRulesStyles.colQuarterBeyondLarge
                      )
                    )(<.ProposalWithTagsComponent(^.wrapped := ProposalWithTagsProps(proposal = proposal))())
                )
              )
            )

          val proposalsToDisplay: Seq[ProposalModel] = self.state.listProposals.getOrElse(Seq.empty)
          <("search-resulst")()(if (proposalsToDisplay.nonEmpty) {
            <.section(^.className := SearchResultsStyles.resultsWrapper)(
              <.h1(
                ^.dangerouslySetInnerHTML := unescape(
                  I18n
                    .t(
                      s"content.search.title",
                      Replacements(("results", self.state.resultsCount.getOrElse(0).toString))
                    )
                )
              )(),
              proposals(proposalsToDisplay)
            )
          } else {
            <.section(^.className := SearchResultsStyles.noResultsWrapper)(noResults)
          }, <.NavInThemesContainerComponent.empty, <.style()(SearchResultsStyles.render[String]))
      })
    )
}

object SearchResultsStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val resultsWrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      paddingTop((50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((80).pxToEm()))
    )

  val noResultsWrapper: StyleA = style(
    minHeight(100.%%),
    paddingTop((50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
    ThemeStyles.MediaQueries.beyondSmall(paddingTop((80).pxToEm()))
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

  val noResults: StyleA = style(
    paddingTop(ThemeStyles.SpacingValue.larger.pxToEm()),
    paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
    textAlign.center
  )

  val noResultsMessageSeparator: StyleA = style(
    display.block,
    maxWidth(235.pxToEm()),
    margin := s"${ThemeStyles.SpacingValue.medium.pxToEm().value} auto",
    border.none,
    borderTop := s"1px solid ${ThemeStyles.BorderColor.veryLight.value}",
    ThemeStyles.MediaQueries
      .beyondSmall(maxWidth(470.pxToEm()), margin := s"${ThemeStyles.SpacingValue.large.pxToEm().value} auto")
  )

  val noResultsMessage: StyleA =
    style(unsafeChild("strong")(ThemeStyles.Font.circularStdBold))

  val searchedExpression: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm(20)),
      ThemeStyles.MediaQueries
        .beyondSmall(marginTop(ThemeStyles.SpacingValue.small.pxToEm(34)))
    )

  val openProposalModalButton: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))
}
