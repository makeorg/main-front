package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.submitProposal.SubmitProposal.SubmitProposalProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scalacss.DevDefaults._

object NoResultToSearch {

  final case class NoResultToSearchProps(searchValue: Option[String])

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
          self.setState(state => state.copy(isProposalModalOpened = true))
        }
        <.div(^.className := Seq(NoResultToSearchStyles.wrapper, RowRulesStyles.centeredRow))(
          <.div(^.className := ColRulesStyles.col)(
            <.p(^.className := NoResultToSearchStyles.sadSmiley)("😞"),
            <.h2(
              ^.className := Seq(TextStyles.mediumText, NoResultToSearchStyles.searchedExpressionIntro),
              ^.dangerouslySetInnerHTML := I18n.t(s"content.search.matrix.noContent")
            )(),
            <.h1(^.className := Seq(NoResultToSearchStyles.searchedExpression, TextStyles.mediumTitle))(
              unescape("«&nbsp;" + self.props.wrapped.searchValue.getOrElse("") + "&nbsp;»")
            ),
            <.hr(^.className := NoResultToSearchStyles.messageSeparator)(),
            <.p(^.className := Seq(TextStyles.mediumText))(I18n.t("content.search.proposeIntro")),
            <.button(
              ^.className := Seq(
                NoResultToSearchStyles.openProposalModalButton,
                CTAStyles.basic,
                CTAStyles.basicOnButton
              ),
              ^.onClick := openProposalModal
            )(
              <.i(^.className := Seq(FontAwesomeStyles.pencil, FontAwesomeStyles.fa))(),
              unescape("&nbsp;" + I18n.t("content.search.propose"))
            ),
            <.FullscreenModalComponent(
              ^.wrapped := FullscreenModalProps(self.state.isProposalModalOpened, closeProposalModal)
            )(<.SubmitProposalComponent(^.wrapped := SubmitProposalProps(onProposalProposed = () => {
              self.setState(_.copy(isProposalModalOpened = false))
            }))())
          ),
          <.style()(NoResultToSearchStyles.render[String])
        )
      }
    )
}

object NoResultToSearchStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA = style(
    paddingTop(ThemeStyles.SpacingValue.larger.pxToEm()),
    paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
    textAlign.center
  )

  val sadSmiley: StyleA =
    style(
      display.inlineBlock,
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm(34)),
      fontSize(34.pxToEm()),
      lineHeight(1),
      ThemeStyles.MediaQueries
        .beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm(48)), fontSize(48.pxToEm()))
    )

  val messageSeparator: StyleA = style(
    display.block,
    maxWidth(235.pxToEm()),
    margin :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} auto",
    border.none,
    borderTop :=! s"1px solid ${ThemeStyles.BorderColor.veryLight.value}",
    ThemeStyles.MediaQueries
      .beyondSmall(maxWidth(470.pxToEm()), margin :=! s"${ThemeStyles.SpacingValue.large.pxToEm().value} auto")
  )

  val searchedExpressionIntro: StyleA =
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
