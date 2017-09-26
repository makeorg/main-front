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
import org.make.front.components.theme.ResultsInThemeContainer.ResultsInThemeContainerProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.helpers.QueryString
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

          val openProposalModalFromInput: (MouseSyntheticEvent) => Unit = { event =>
            event.preventDefault()
            self.setState(state => state.copy(isProposalModalOpened = true))
          }

          val searchValue: Option[String] = self.state.searchValue

          val noContent: ReactElement = {
            <.div()(
              <.div(^.className := Seq(LayoutRulesStyles.centeredRow, SearchResultsStyles.noProposal))(
                <.div(^.className := LayoutRulesStyles.col)(
                  <.p(^.className := SearchResultsStyles.noProposalSmiley)("😞"),
                  <.p(
                    ^.className := Seq(TextStyles.mediumText, SearchResultsStyles.noProposalMessage),
                    ^.dangerouslySetInnerHTML := unescape(
                      I18n
                        .t(
                          s"content.search.matrix.noContent",
                          Replacements(("search", self.state.searchValue.getOrElse("")))
                        )
                    )
                  )()
                )
              ),
              <.div(^.className := Seq(SearchResultsStyles.newProposal, LayoutRulesStyles.centeredRow))(
                <.div(^.className := LayoutRulesStyles.col)(
                  <.p(^.className := Seq(SearchResultsStyles.newProposalIntro, TextStyles.mediumText))(
                    I18n.t("content.search.proposeIntro")
                  ),
                  <.button(
                    ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton),
                    ^.onClick := openProposalModalFromInput
                  )(
                    <.i(^.className := FontAwesomeStyles.pencil)(),
                    unescape("&nbsp;"),
                    unescape(I18n.t("content.search.propose"))
                  ),
                  <.FullscreenModalComponent(
                    ^.wrapped := FullscreenModalProps(self.state.isProposalModalOpened, closeProposalModal)
                  )()
                )
              )
            )
          }

          <.section(^.className := SearchResultsStyles.wrapper)(
            <.h1(
              ^.dangerouslySetInnerHTML := unescape(
                I18n
                  .t(s"content.search.title", Replacements(("results", self.state.resultsCount.getOrElse(0).toString)))
              )
            )(),
            <.ProposalsContainerComponent(
              ^.wrapped := ResultsInThemeContainerProps(
                themeSlug = None,
                searchValue = searchValue,
                showTagsSelect = false,
                noContent = noContent
              )
            )(),
            <.style()(SearchResultsStyles.render[String])
          )

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

  val noProposalMessage: StyleA =
    style(unsafeChild("strong")(ThemeStyles.Font.circularStdBold), unsafeChild("em")(TextStyles.title))

  val noProposal: StyleA = style(
    paddingTop(ThemeStyles.SpacingValue.larger.pxToEm()),
    paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
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

}
