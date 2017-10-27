package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalInfos.ProposalInfosProps
import org.make.front.components.proposal.ShareOwnProposal.ShareOwnProposalProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object ProposalTileWithTheme {

  final case class ProposalTileWithThemeProps(proposal: ProposalModel, themeName: String, themeSlug: String, index: Int)

  val reactClass: ReactClass =
    React
      .createClass[ProposalTileWithThemeProps, Unit](
        displayName = "ProposalTileWithTheme",
        render = (self) => {
          <.article(^.className := ProposalTileStyles.wrapper)(
            <.div(^.className := ProposalTileStyles.innerWrapper)(
              <.div(^.className := ProposalTileStyles.row)(
                <.div(^.className := ProposalTileStyles.cell)(if (self.props.wrapped.proposal.myProposal) {
                  <.div(^.className := ProposalTileStyles.shareOwnProposalWrapper)(
                    <.ShareOwnProposalComponent(
                      ^.wrapped := ShareOwnProposalProps(proposal = self.props.wrapped.proposal)
                    )()
                  )
                } else {
                  <.div(^.className := ProposalTileStyles.proposalInfosWrapper)(
                    <.ProposalInfosComponent(^.wrapped := ProposalInfosProps(proposal = self.props.wrapped.proposal))()
                  )
                })
              ),
              <.div(^.className := Seq(ProposalTileStyles.row, ProposalTileStyles.stretchedRow))(
                <.div(^.className := Seq(ProposalTileStyles.cell, ProposalTileStyles.contentWrapper))(
                  <.h3(^.className := Seq(TextStyles.mediumText, TextStyles.boldText))(
                    <.Link(^.to := s"/proposal/${self.props.wrapped.proposal.slug}")(
                      self.props.wrapped.proposal.content
                    )
                  ),
                  <.VoteContainerComponent(
                    ^.wrapped := VoteContainerProps(
                      proposal = self.props.wrapped.proposal,
                      index = self.props.wrapped.index
                    )
                  )()
                )
              ),
              if (Option(self.props.wrapped.themeName).exists(_.nonEmpty) && Option(self.props.wrapped.themeSlug)
                    .exists(_.nonEmpty)) {
                <.div(^.className := ProposalTileStyles.row)(
                  <.div(^.className := ProposalTileStyles.cell)(
                    <.footer(^.className := ProposalTileStyles.footer)(
                      <.p(^.className := Seq(TextStyles.smallerText, ProposalTileWithThemeStyles.themeInfo))(
                        unescape(I18n.t("proposal.associated-with-the-theme")),
                        <.Link(
                          ^.to := s"/theme/${self.props.wrapped.themeSlug}",
                          ^.className := Seq(TextStyles.title, ProposalTileWithThemeStyles.themeName)
                        )(self.props.wrapped.themeName)
                      )
                    )
                  )
                )
              }
            ),
            <.style()(ProposalTileStyles.render[String], ProposalTileWithThemeStyles.render[String])
          )
        }
      )
}

object ProposalTileWithThemeStyles extends StyleSheet.Inline {

  import dsl._

  val themeName: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val themeInfo: StyleA =
    style(color(ThemeStyles.TextColor.light))

}
