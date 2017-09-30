package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalInfos.ProposalInfosProps
import org.make.front.components.proposal.vote.Vote.VoteProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object ProposalWithTheme {

  final case class ProposalWithThemeProps(proposal: ProposalModel, themeName: String)

  val reactClass: ReactClass =
    React.createClass[ProposalWithThemeProps, Unit](render = (self) => {

      <.article(^.className := ProposalStyles.wrapper)(
        <.header(^.className := ProposalStyles.proposalInfosWrapper)(
          <.ProposalInfosComponent(^.wrapped := ProposalInfosProps(proposal = self.props.wrapped.proposal))()
        ),
        <.div(^.className := ProposalStyles.contentWrapper)(
          <.h3(^.className := Seq(TextStyles.mediumText, TextStyles.boldText))(self.props.wrapped.proposal.content),
          <.VoteComponent(
            ^.wrapped := VoteProps(
              proposalId = self.props.wrapped.proposal.id,
              voteAgreeStats = self.props.wrapped.proposal.votesAgree,
              voteDisagreeStats = self.props.wrapped.proposal.votesDisagree,
              voteNeutralStats = self.props.wrapped.proposal.votesNeutral
            )
          )()
        ),
        <.footer(^.className := ProposalStyles.footer)(
          <.p(^.className := Seq(TextStyles.smallerText, ProposalWithThemeStyles.themeInfo))(
            unescape(I18n.t("content.proposal.postedIn")),
            <.strong(^.className := Seq(TextStyles.title, ProposalWithThemeStyles.themeName))(
              self.props.wrapped.themeName
            )
          )
        ),
        <.style()(ProposalStyles.render[String], ProposalWithThemeStyles.render[String])
      )

    })
}

object ProposalWithThemeStyles extends StyleSheet.Inline {

  import dsl._

  val themeName: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val themeInfo: StyleA =
    style(color(ThemeStyles.TextColor.light))

}
