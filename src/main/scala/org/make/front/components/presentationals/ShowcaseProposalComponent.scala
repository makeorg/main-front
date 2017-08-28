package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.AppComponentStyles
import org.make.front.facades.I18n
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.models._
import org.make.front.styles.MakeStyles

import scalacss.DevDefaults._

object ShowcaseProposalComponent {

  final case class ShowcaseProps(proposals: Seq[Proposal],
                                 translationKey: String,
                                 searchThemeByThemeId: (ThemeId) => Option[Theme])

  lazy val reactClass: ReactClass = React.createClass[ShowcaseProps, Unit](
    render = (self) =>
      <.div(^.className := AppComponentStyles.container)(
        <.Translate(^.className := ShowCaseProposalStyle.expressText, ^.value := self.props.wrapped.translationKey)(),
        <.h2(^.className := ShowCaseProposalStyle.mostPopularText)(I18n.t("content.homepage.mostPopular")),
        <.ul()(
          self.props.wrapped.proposals.map(
            proposal =>
              <.li(^.className := ShowCaseProposalStyle.proposalItem)(
                <.ProposalTileComponent(
                  ^.wrapped := ProposalTileComponent
                    .ProposalTileProps(
                      proposal = proposal,
                      proposalLocation = HomePage,
                      isHomePage = true,
                      associatedTheme = self.props.wrapped.searchThemeByThemeId(proposal.themeId.getOrElse(ThemeId("")))
                    )
                )()
            )
          )
        ),
        <.style()(ShowCaseProposalStyle.render[String])
    )
  )

}

object ShowCaseProposalStyle extends StyleSheet.Inline {
  import dsl._

  val expressText: StyleA =
    style(MakeStyles.Font.playfairDisplayItalic, fontSize(1.8.rem), marginTop(3.rem), display.block)

  val mostPopularText: StyleA = style(MakeStyles.title2)

  val proposalItem: StyleA =
    style(display.inlineBlock, verticalAlign.top, width(50.%%), padding :=! MakeStyles.Spacing.small)
}
