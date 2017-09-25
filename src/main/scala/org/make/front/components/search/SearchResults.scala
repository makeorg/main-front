package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.proposals.ProposalsListContainer.ProposalsListContainerProps
import org.make.front.facades.{I18n, Replacements}
import org.make.front.facades.Unescape.unescape
import org.make.front.helpers.QueryString
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles._

import scala.scalajs.js.URIUtils
import scalacss.DevDefaults._
import scalacss.internal.Length

object SearchResults {
  final case class SearchResultsProps(searchValue: Option[String])
  case class SearchResultsState(searchValue: Option[String] = None,
                                resultsCount: Option[Int] = None,
                                isProposalModalOpened: Boolean = false)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[SearchResultsProps, SearchResultsState](
        displayName = getClass.toString,
        getInitialState = (self) => {
          val search = QueryString.parse(self.props.location.search)
          val searchValue: Option[String] = search.get("q").map(URIUtils.decodeURI)
          SearchResultsState(searchValue = searchValue)
        },
        componentWillReceiveProps = (self, nextProps) => {
          val search = QueryString.parse(nextProps.location.search)
          val newSearchValue = search.get("q").map(URIUtils.decodeURI)
          if (self.state.searchValue != newSearchValue) {
            self.setState(_.copy(searchValue = newSearchValue))
          }
        },
        render = (self) => {

          def toggleProposalModal() = () => {
            self.setState(state => state.copy(isProposalModalOpened = !self.state.isProposalModalOpened))
          }

          def openProposalModalFromInput() = (event: MouseSyntheticEvent) => {
            event.preventDefault()
            self.setState(state => state.copy(isProposalModalOpened = true))
          }

          def handleResults(proposals: Seq[ProposalModel]): Unit = {
            val resultCount = proposals.length
            self.setState(_.copy(resultsCount = Some(resultCount)))
          }

          def noContentText(): String = {
            val searchContent: String = self.state.searchValue.getOrElse("")
            I18n.t("content.search.matrix.noContent", Replacements(("search", "<em>" + searchContent + "</em>")))
          }

          def hasResult = self.state.resultsCount.getOrElse(0) > 0
          def searchValue(): Option[String] = self.state.searchValue

          <.section(^.className := SearchResultsStyles.wrapper)(
            //<.span()(s"« ${self.state.searchValue.getOrElse("")} »")
            if (hasResult) {
              <.h1(
                ^.dangerouslySetInnerHTML := (I18n
                  .t(s"content.search.title", Replacements(("results", self.state.resultsCount.getOrElse(0).toString))))
              )()
            },
            <.ProposalsContainerComponent(
              ^.wrapped := ProposalsListContainerProps(
                themeSlug = None,
                searchValue = searchValue,
                handleResults = Some(handleResults),
                showTagsSelect = false,
                noContentText = noContentText
              )
            )(),
            if (!hasResult) {
              <.div(^.className := Seq(SearchResultsStyles.newProposal, LayoutRulesStyles.centeredRow))(
                <.div(^.className := LayoutRulesStyles.col)(
                  <.p(^.className := Seq(SearchResultsStyles.newProposalIntro, TextStyles.mediumText))(
                    I18n.t("content.search.proposeIntro")
                  ),
                  <.button(
                    ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton),
                    ^.onClick := openProposalModalFromInput()
                  )(
                    <.i(^.className := FontAwesomeStyles.pencil)(),
                    unescape("&nbsp;"),
                    unescape(I18n.t("content.search.propose"))
                  ),
                  <.FullscreenModalComponent(
                    ^.wrapped := FullscreenModalProps(self.state.isProposalModalOpened, toggleProposalModal())
                  )()
                )
              )
            },
            <.style()(SearchResultsStyles.render[String])
          )

        }
      )
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

  val wrapper: StyleA =
    style(
      paddingTop((50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((80).pxToEm()))
    )
  val newProposal: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.larger.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
      textAlign.center
    )

  val newProposalIntro: StyleA =
    style()

}
