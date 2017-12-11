package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.theme.SubmitProposalInRelationToTheme.SubmitProposalInRelationToThemeProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles._
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.InputStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLElement

object PromptingToProposeInRelationToThemeTile {

  final case class PromptingToProposeInRelationToThemeTileProps(theme: TranslatedThemeModel)

  case class PromptingToProposeInRelationToThemeTileState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[PromptingToProposeInRelationToThemeTileProps, PromptingToProposeInRelationToThemeTileState](
      displayName = "PromptingToProposeInRelationToThemeTile",
      getInitialState = { _ =>
        PromptingToProposeInRelationToThemeTileState(isProposalModalOpened = false)
      },
      render = { self =>
        var proposalInput: Option[HTMLElement] = None

        def toggleProposalModal() = () => {
          self.setState(state => state.copy(isProposalModalOpened = !self.state.isProposalModalOpened))
        }

        def openProposalModalFromInput() = () => {
          self.setState(state => state.copy(isProposalModalOpened = true))
          proposalInput.foreach(_.blur())
        }

        <.article(^.className := Seq(PromptingToProposeInRelationToThemeTileStyles.wrapper))(
          <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
            <.div(^.className := Seq(TableLayoutStyles.row, PromptingToProposeInRelationToThemeTileStyles.row))(
              <.div(
                ^.className := Seq(
                  TableLayoutStyles.cell,
                  TableLayoutStyles.cellVerticalAlignBottom,
                  PromptingToProposeInRelationToThemeTileStyles.cell,
                  PromptingToProposeInRelationToThemeTileStyles.introWrapper
                )
              )(
                <.div(^.className := TableLayoutStyles.wrapper)(
                  <.div(^.className := Seq(TableLayoutStyles.cell, TableLayoutStyles.cellVerticalAlignBottom))(
                    <.h3(^.className := Seq(TextStyles.mediumText, TextStyles.boldText))(
                      unescape(I18n.t("theme-showcase.prompting-to-propose-tile.intro"))
                    )
                  ),
                  <.div(^.className := Seq(TableLayoutStyles.cell, TableLayoutStyles.cellVerticalAlignBottom))(
                    <.span(
                      ^.className := Seq(
                        FontAwesomeStyles.lightbulbTransparent,
                        PromptingToProposeInRelationToThemeTileStyles.picto
                      )
                    )()
                  )
                )
              )
            ),
            <.div(^.className := Seq(TableLayoutStyles.row, PromptingToProposeInRelationToThemeTileStyles.row))(
              <.div(
                ^.className := Seq(
                  TableLayoutStyles.cell,
                  PromptingToProposeInRelationToThemeTileStyles.cell,
                  PromptingToProposeInRelationToThemeTileStyles.inputWrapper
                )
              )(
                <.p(
                  ^.className := Seq(
                    InputStyles.wrapper,
                    PromptingToProposeInRelationToThemeTileStyles.proposalInputWithIconWrapper
                  )
                )(
                  <.input(
                    ^.`type`.text,
                    ^.value := I18n.t("theme-showcase.prompting-to-propose-tile.bait"),
                    ^.readOnly := true,
                    ^.ref := ((input: HTMLElement) => proposalInput = Some(input)),
                    ^.onFocus := openProposalModalFromInput
                  )()
                )
              )
            ),
            <.FullscreenModalComponent(
              ^.wrapped := FullscreenModalProps(
                isModalOpened = self.state.isProposalModalOpened,
                closeCallback = toggleProposalModal()
              )
            )(
              <.SubmitProposalInRelationToThemeComponent(
                ^.wrapped := SubmitProposalInRelationToThemeProps(
                  theme = self.props.wrapped.theme,
                  onProposalProposed = () => {
                    self.setState(_.copy(isProposalModalOpened = false))
                  },
                  maybeLocation = None
                )
              )()
            )
          ),
          <.style()(PromptingToProposeInRelationToThemeTileStyles.render[String])
        )
      }
    )
}

object PromptingToProposeInRelationToThemeTileStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      height(100.%%),
      minHeight(360.pxToEm()),
      minWidth(270.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val row: StyleA =
    style(height(50.%%))

  val cell: StyleA =
    style(padding(ThemeStyles.SpacingValue.small.pxToEm()))

  val introWrapper: StyleA = style(paddingBottom((ThemeStyles.SpacingValue.small / 2).pxToEm()))

  val inputWrapper: StyleA = style(paddingTop((ThemeStyles.SpacingValue.small / 2).pxToEm()))

  val picto: StyleA =
    style(fontSize(115.pxToEm()), lineHeight(100.0 / 115.0), color(ThemeStyles.TextColor.veryLight))

  val proposalInputWithIconWrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      unsafeChild("input")(color(ThemeStyles.TextColor.lighter), cursor.text)
    )
}
