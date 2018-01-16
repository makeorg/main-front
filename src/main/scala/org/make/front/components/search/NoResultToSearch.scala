package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.submitProposal.SubmitProposal.SubmitProposalProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.Location
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext

object NoResultToSearch {

  final case class NoResultToSearchProps(searchValue: Option[String], maybeLocation: Option[Location])

  final case class NoResultToSearchState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[NoResultToSearchProps, NoResultToSearchState](
      displayName = "NoResultToSearch",
      getInitialState = (_) => NoResultToSearchState(isProposalModalOpened = false),
      render = { self =>
        val closeProposalModal: () => Unit = () => {
          self.setState(state => state.copy(isProposalModalOpened = false))
        }

        val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
          event.preventDefault()
          TrackingService.track(
            "click-proposal-submit-form-open",
            TrackingContext(TrackingLocation.searchResultsPage),
            self.props.wrapped.searchValue.map(query => Map("query" -> query)).getOrElse(Map.empty)
          )
          self.setState(state => state.copy(isProposalModalOpened = true))
        }
        <.article(^.className := Seq(LayoutRulesStyles.centeredRow, NoResultToSearchStyles.wrapper))(
          <.p(^.className := NoResultToSearchStyles.sadSmiley)("😞"),
          <.h1(
            ^.className := Seq(TextStyles.mediumText, NoResultToSearchStyles.searchedExpressionIntro),
            ^.dangerouslySetInnerHTML := I18n.t("search.no-results.intro")
          )(),
          <.h2(^.className := Seq(TextStyles.mediumTitle, NoResultToSearchStyles.searchedExpression))(
            unescape("«&nbsp;" + self.props.wrapped.searchValue.getOrElse("") + "&nbsp;»")
          ),
          <.hr(^.className := NoResultToSearchStyles.messageSeparator)(),
          <.p(^.className := Seq(TextStyles.mediumText, NoResultToSearchStyles.openProposalModalIntro))(
            unescape(I18n.t("search.no-results.prompting-to-propose"))
          ),
          <.button(
            ^.className := Seq(
              NoResultToSearchStyles.openProposalModalButton,
              CTAStyles.basic,
              CTAStyles.basicOnButton
            ),
            ^.onClick := openProposalModal
          )(
            <.i(^.className := Seq(FontAwesomeStyles.pencil))(),
            unescape("&nbsp;" + I18n.t("search.no-results.propose-cta"))
          ),
          <.FullscreenModalComponent(
            ^.wrapped := FullscreenModalProps(
              isModalOpened = self.state.isProposalModalOpened,
              closeCallback = closeProposalModal
            )
          )(<.SubmitProposalComponent(^.wrapped := SubmitProposalProps(onProposalProposed = () => {
            self.setState(_.copy(isProposalModalOpened = false))
          }, maybeLocation = self.props.wrapped.maybeLocation))()),
          <.style()(NoResultToSearchStyles.render[String])
        )
      }
    )
}

object NoResultToSearchStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(paddingTop(ThemeStyles.SpacingValue.larger.pxToEm()), paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()))

  val sadSmiley: StyleA =
    style(
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm(34)),
      fontSize(34.pxToEm()),
      lineHeight(1),
      textAlign.center,
      ThemeStyles.MediaQueries
        .beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm(48)), fontSize(48.pxToEm()))
    )

  val messageSeparator: StyleA = style(
    display.block,
    maxWidth(235.pxToEm()),
    margin(ThemeStyles.SpacingValue.medium.pxToEm(), auto),
    borderTop(1.px, solid, ThemeStyles.BorderColor.veryLight),
    ThemeStyles.MediaQueries
      .beyondSmall(maxWidth(470.pxToEm()), margin(ThemeStyles.SpacingValue.large.pxToEm(), auto))
  )

  val searchedExpressionIntro: StyleA =
    style(textAlign.center, unsafeChild("strong")(ThemeStyles.Font.circularStdBold))

  val searchedExpression: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm(20)),
      textAlign.center,
      ThemeStyles.MediaQueries
        .beyondSmall(marginTop(ThemeStyles.SpacingValue.small.pxToEm(34)))
    )

  val openProposalModalIntro: StyleA =
    style(textAlign.center)

  val openProposalModalButton: StyleA =
    style(display.block, margin(ThemeStyles.SpacingValue.small.pxToEm(), auto, `0`))
}
