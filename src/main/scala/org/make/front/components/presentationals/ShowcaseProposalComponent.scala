package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
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
      <.section()(
        <.Translate(^.className := ShowCaseProposalStyles.expressText, ^.value := self.props.wrapped.translationKey)(),
        <.h2(^.className := ShowCaseProposalStyles.mostPopularText)(I18n.t("content.homepage.mostPopular")),
        <.ul()(
          self.props.wrapped.proposals.map(
            proposal =>
              <.li(^.className := ShowCaseProposalStyles.proposalItem)(
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
        <.style()(ShowCaseProposalStyles.render[String])
    )
  )

}

object ShowCaseProposalStyles extends StyleSheet.Inline {
  import dsl._

  val expressText: StyleA =
    style(MakeStyles.Font.playfairDisplayItalic, fontSize(1.8.rem), marginTop(3.rem), display.block)

  val mostPopularText: StyleA = style(MakeStyles.title2)

  val proposalItem: StyleA =
    style(display.inlineBlock, verticalAlign.top, width(50.%%))
}
