package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.components.proposals.ProposalsListContainer.ProposalsListContainerProps
import org.make.front.components.proposals.ProposalsListStyles
import org.make.front.components.theme.ThemeHeader.ThemeHeaderProps
import org.make.front.facades.I18n
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.models.{Theme => ThemeModel}
import org.make.front.styles.{LayoutRulesStyles, TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object Theme {

  final case class ThemeProps(theme: Option[ThemeModel], themeSlug: String)

  lazy val reactClass: ReactClass = React.createClass[ThemeProps, Unit](render = (self) => {

    val noContent: ReactElement = {
      <.div(^.className := Seq(LayoutRulesStyles.centeredRow, ProposalsListStyles.noProposal))(
        <.div(^.className := LayoutRulesStyles.col)(
          <.p(^.className := ProposalsListStyles.noProposalSmiley)("😞"),
          <.p(
            ^.className := Seq(TextStyles.mediumText, ProposalsListStyles.noProposalMessage),
            ^.dangerouslySetInnerHTML := I18n.t("content.theme.matrix.noContent")
          )()
        )
      )
    }

    <("theme")()(
      <.ThemeHeaderComponent(^.wrapped := ThemeHeaderProps(self.props.wrapped.theme))(),
      //TODO: fix 5 errors of setState before render. These are logged in console.
      <.PoliticalActionsContainerComponent()(),
      <.section(^.className := ThemeComponentStyles.wrapper)(
        <.div(^.className := LayoutRulesStyles.centeredRow)(
          <.header(^.className := LayoutRulesStyles.col)(
            <.h2(^.className := TextStyles.bigTitle)(<.Translate(^.value := "content.theme.matrix.title")())
          )
        ),
        <.ProposalsContainerComponent(
          ^.wrapped := ProposalsListContainerProps(
            themeSlug = Some(self.props.wrapped.themeSlug),
            noContent = noContent
          )
        )()
      ),
      <.style()(ThemeComponentStyles.render[String])
    )
  })
}

object ThemeComponentStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0 ${ThemeStyles.SpacingValue.small.pxToEm().value}"
    )

}
