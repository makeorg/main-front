package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.Proposal.ProposalProps
import org.make.front.components.proposal.ProposalWithThemeContainer.ProposalWithThemeContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.base.TextStyles
import org.make.front.styles._

import scalacss.DevDefaults._

object Showcase {

  final case class ShowcaseProps(proposals: Seq[ProposalModel], introTranslationKey: String)

  lazy val reactClass: ReactClass = React.createClass[ShowcaseProps, Unit](
    displayName = "Showcase",
    render = (self) =>
      <.section(^.className := ShowcaseStyles.wrapper)(
        <.div(^.className := LayoutRulesStyles.centeredRow)(
          <.header(^.className := LayoutRulesStyles.col)(
            <.p(^.className := Seq(ShowcaseStyles.intro, TextStyles.mediumText, TextStyles.intro))(
              unescape(I18n.t(self.props.wrapped.introTranslationKey))
            ),
            <.h2(^.className := Seq(ShowcaseStyles.title, TextStyles.bigTitle))(
              unescape(I18n.t("content.homepage.mostPopular"))
            )
          ),
          <.ul()(
            self.props.wrapped.proposals.map(
              proposal =>
                <.li(
                  ^.className := Seq(
                    ShowcaseStyles.propasalItem,
                    LayoutRulesStyles.col,
                    LayoutRulesStyles.colHalfBeyondMedium
                  )
                )(if (proposal.themeId.getOrElse("") != "") {
                  <.ProposalWithThemeContainerComponent(
                    ^.wrapped :=
                      ProposalWithThemeContainerProps(proposal = proposal)
                  )()
                } else {
                  <.ProposalComponent(
                    ^.wrapped :=
                      ProposalProps(proposal = proposal)
                  )()
                })
            )
          )
        ),
        <.style()(ShowcaseStyles.render[String])
    )
  )

}

object ShowcaseStyles extends StyleSheet.Inline {
  import dsl._

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
