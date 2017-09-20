package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.home.MainIntroStyles
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.proposals.ProposalsListContainer.MatrixWrappedProps
import org.make.front.components.submitProposal.SubmitProposalInRelationToTheme.SubmitProposalInRelationToThemeProps
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.helpers.QueryString
import org.make.front.models.{Theme, ThemeId, Proposal => ProposalModel}
import org.make.front.styles._

import scala.scalajs.js.URIUtils
import scalacss.DevDefaults._
import scalacss.internal.{Length, StyleA}

object Search {
  final case class SearchProps(searchValue: Option[String])
  case class SearchState(searchValue: Option[String] = None,
                         resultsCount: Option[Int] = None,
                         isProposalModalOpened: Boolean = false)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[SearchProps, SearchState](
        displayName = getClass.toString,
        getInitialState = (self) => {
          val search = QueryString.parse(self.props.location.search)
          val searchValue: Option[String] = search.get("q").map(URIUtils.decodeURI)
          SearchState(searchValue = searchValue)
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
            I18n.t("content.search.matrix.noContent", Replacements(("search", searchContent.toUpperCase)))
          }
          def hasResult = self.state.resultsCount.getOrElse(0) > 0
          def searchValue(): Option[String] = self.state.searchValue

          <("search")(^.className := SearchComponentStyles.wrapper)(
            <.div(^.className := LayoutRulesStyles.centeredRow)(
              <.header(^.className := SearchComponentStyles.innerWrapper(hasResult))(if (hasResult) {
                <.h2(^.className := SearchComponentStyles.title)(
                  <.Translate(
                    ^.value := "content.search.title",
                    ^.dangerousHtml := true,
                    ^("results") := self.state.resultsCount.getOrElse(0).toString
                  )(),
                  <.span(^.className := SearchComponentStyles.result)(s"« ${self.state.searchValue.getOrElse("")} »")
                )
              }),
              <.ProposalsContainerComponent(
                ^.wrapped := MatrixWrappedProps(
                  themeSlug = None,
                  searchValue = searchValue,
                  handleResults = Some(handleResults),
                  showTagsSelect = false,
                  noContentText = noContentText
                )
              )(),
              if (self.state.resultsCount.contains(0)) {
                <.div()(
                  <.p()(I18n.t("content.search.proposeIntro")),
                  <.p(^.className := MainIntroStyles.ctaWrapper)(
                    <.a(
                      ^.href := "#",
                      ^.onClick := openProposalModalFromInput(),
                      ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA)
                    )(unescape(I18n.t("content.search.propose")))
                  )
                )
              }
            ),
            <.FullscreenModalComponent(
              ^.wrapped := FullscreenModalProps(self.state.isProposalModalOpened, toggleProposalModal())
            )(
              <.SubmitProposalInRelationToThemeComponent(
                ^.wrapped := SubmitProposalInRelationToThemeProps(
                  Theme(
                    ThemeId(""),
                    slug = "",
                    title = "",
                    actionsCount = 0,
                    proposalsCount = 0,
                    color = "",
                    gradient = None
                  ),
                  () => {
                    self.setState(_.copy(isProposalModalOpened = false))
                  }
                )
              )()
            ),
            <.style()(SearchComponentStyles.render[String])
          )

        }
      )
    )
}

object SearchComponentStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent), display.block)

  val innerWrapper: (Boolean) => StyleA = styleF.bool(hasResult => {
    val commonStyle: StyleS = styleS(
      position.relative,
      display.block,
      verticalAlign.middle,
      paddingTop((60 + 50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((60 + 80).pxToEm())),
      textAlign.center
    )
    if (hasResult) {
      styleS(commonStyle, paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()))
    } else {
      styleS(commonStyle, paddingBottom.`0`)
    }
  })

  val title: StyleA =
    style( /*Basic.circularStdBook,*/ fontSize(18.pxToEm()), unsafeChild("strong") {
      //Basic.circularStdBold
      fontWeight.bold
    })

  val result: StyleA =
    style(
      /* MakeStyles.Font.tradeGothicLTStd, */
      fontSize(34.pxToEm(18)),
      display.block,
      marginTop(10.pxToEm(18)),
      textTransform.uppercase
    )
}
