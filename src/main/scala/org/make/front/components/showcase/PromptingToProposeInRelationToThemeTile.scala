package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.theme.SubmitProposalInRelationToTheme.SubmitProposalInRelationToThemeProps
import org.make.front.facades.{FacebookPixel, I18n}
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles._
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.InputStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

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

        def closeProposalModal() = () => {
          self.setState(state => state.copy(isProposalModalOpened = false))
        }

        def openProposalModalFromInput() = () => {
          self.setState(state => state.copy(isProposalModalOpened = true))
          FacebookPixel
            .fbq(
              "trackCustom",
              "click-proposal-submit-form-open",
              js.Dictionary("location" -> "page-home", "themeId" -> self.props.wrapped.theme.id.value.toString)
            )
          proposalInput.foreach(_.blur())
        }

        <.article(
          ^.className := Seq(PromptingToProposeInRelationToThemeTileStyles.wrapper),
          ^.onClick := openProposalModalFromInput
        )(
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
                    ^.readOnly := true
                  )()
                )
              )
            ),
            <.FullscreenModalComponent(
              ^.wrapped := FullscreenModalProps(
                isModalOpened = self.state.isProposalModalOpened,
                closeCallback = closeProposalModal()
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
      ThemeStyles.MediaQueries.belowMedium(minHeight.inherit),
      minWidth(270.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)",
      cursor.pointer
    )

  val row: StyleA =
    style(height(50.%%))

  val cell: StyleA =
    style(padding(`0`, ThemeStyles.SpacingValue.small.pxToEm()))

  val introWrapper: StyleA = style(
    paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
    paddingBottom((ThemeStyles.SpacingValue.small / 2).pxToEm())
  )

  val inputWrapper: StyleA = style(
    paddingTop((ThemeStyles.SpacingValue.small / 2).pxToEm()),
    paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm())
  )

  val picto: StyleA =
    style(fontSize(115.pxToEm()), lineHeight(100.0 / 115.0), color(ThemeStyles.TextColor.veryLight))

  val proposalInputWithIconWrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      unsafeChild("input")(color(ThemeStyles.TextColor.lighter), cursor.pointer)
    )
}
