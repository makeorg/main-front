package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.presentationals._
import org.make.front.components.proposals.{HomePage, Proposal}
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Proposal => ProposalModel, Theme => ThemeModel, ThemeId => ThemeIdModel}
import org.make.front.styles.{LayoutRulesStyles, TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length

object Showcase {

  final case class ShowcaseProps(proposals: Seq[ProposalModel],
                                 introTranslationKey: String,
                                 searchThemeByThemeId: (ThemeIdModel) => Option[ThemeModel])

  lazy val reactClass: ReactClass = React.createClass[ShowcaseProps, Unit](
    render = (self) =>
      <.section(^.className := ShowCaseStyles.wrapper)(
        <.div(^.className := LayoutRulesStyles.centeredRow)(
          <.header(^.className := LayoutRulesStyles.col)(
            <.p(^.className := Seq(ShowCaseStyles.intro, TextStyles.mediumText, TextStyles.intro))(
              unescape(I18n.t(self.props.wrapped.introTranslationKey))
            ),
            <.h2(^.className := Seq(ShowCaseStyles.title, TextStyles.bigTitle))(
              unescape(I18n.t("content.homepage.mostPopular"))
            )
          ),
          <.ul()(
            self.props.wrapped.proposals.map(
              proposal =>
                <.li(
                  ^.className := Seq(
                    ShowCaseStyles.propasalItem,
                    LayoutRulesStyles.col,
                    LayoutRulesStyles.colHalfBeyondMedium
                  )
                )(
                  <.ProposalComponent(
                    ^.wrapped := Proposal
                      .ProposalProps(
                        proposal = proposal,
                        proposalLocation = HomePage,
                        isHomePage = true,
                        associatedTheme =
                          self.props.wrapped.searchThemeByThemeId(proposal.themeId.getOrElse(ThemeIdModel("")))
                      )
                  )()
              )
            )
          )
        ),
        <.style()(ShowCaseStyles.render[String])
    )
  )

}

object ShowCaseStyles extends StyleSheet.Inline {
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
      padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0"
    )

  val title: StyleA = style()
  val intro: StyleA = style(
    marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(15)),
    ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(18)))
  )

  val propasalItem: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

}
