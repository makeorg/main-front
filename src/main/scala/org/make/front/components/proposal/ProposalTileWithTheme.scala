package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalInfos.ProposalInfosProps
import org.make.front.components.proposal.ShareOwnProposal.ShareOwnProposalProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{
  SequenceId,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}
import org.make.services.tracking.TrackingLocation

import scala.scalajs.js

object ProposalTileWithTheme {

  final case class ProposalTileWithThemeProps(proposal: ProposalModel,
                                              themeName: String,
                                              themeSlug: String,
                                              index: Int,
                                              maybeTheme: Option[TranslatedThemeModel],
                                              maybeOperation: Option[OperationModel],
                                              maybeSequenceId: Option[SequenceId],
                                              maybeLocation: Option[LocationModel],
                                              trackingLocation: TrackingLocation,
                                              country: String)

  val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[ProposalTileWithThemeProps, Unit](
          displayName = "ProposalTileWithTheme",
          render = (self) => {

            val intro: ReactElement = if (self.props.wrapped.proposal.myProposal) {
              <.div(^.className := ProposalTileStyles.shareOwnProposalWrapper)(
                <.ShareOwnProposalComponent(
                  ^.wrapped := ShareOwnProposalProps(proposal = self.props.wrapped.proposal)
                )()
              )
            } else {
              <.div(^.className := ProposalTileStyles.proposalInfosWrapper)(
                <.ProposalInfosComponent(^.wrapped := ProposalInfosProps(proposal = self.props.wrapped.proposal))()
              )
            }

            val proposalLink: String =
              if (Option(self.props.wrapped.themeName).exists(_.nonEmpty) && Option(self.props.wrapped.themeSlug)
                    .exists(_.nonEmpty)) {
                s"/${self.props.wrapped.country}/theme/${self.props.wrapped.themeSlug}/proposal/${self.props.wrapped.proposal.slug}"
              } else {
                self.props.wrapped.maybeOperation match {
                  case Some(operationExpanded) =>
                    s"/${self.props.wrapped.country}/consultation/${operationExpanded.slug}/proposal/${self.props.wrapped.proposal.slug}"
                  case _ => s"/${self.props.wrapped.country}/proposal/${self.props.wrapped.proposal.slug}"
                }

              }

            <.article(^.className := ProposalTileStyles.wrapper)(
              <.div(^.className := js.Array(TableLayoutStyles.fullHeightWrapper, ProposalTileStyles.innerWrapper))(
                <.div(^.className := TableLayoutStyles.row)(
                  <.div(^.className := TableLayoutStyles.cell)(
                    intro,
                    <.div(^.className := ProposalTileStyles.contentWrapper)(
                      <.h3(^.className := js.Array(TextStyles.mediumText, TextStyles.boldText))(
                        <.Link(^.to := proposalLink, ^.className := ProposalTileStyles.proposalLinkOnTitle)(
                          self.props.wrapped.proposal.content
                        )
                      ),
                      <.VoteContainerComponent(
                        ^.wrapped := VoteContainerProps(
                          proposal = self.props.wrapped.proposal,
                          index = self.props.wrapped.index,
                          maybeTheme = self.props.wrapped.maybeTheme,
                          maybeOperation = self.props.wrapped.maybeOperation,
                          maybeSequenceId = self.props.wrapped.maybeSequenceId,
                          maybeLocation = self.props.wrapped.maybeLocation,
                          trackingLocation = self.props.wrapped.trackingLocation
                        )
                      )()
                    )
                  )
                ),
                if (Option(self.props.wrapped.themeName).exists(_.nonEmpty) && Option(self.props.wrapped.themeSlug)
                      .exists(_.nonEmpty)) {
                  <.div(^.className := TableLayoutStyles.row)(
                    <.div(^.className := TableLayoutStyles.cellVerticalAlignBottom)(
                      <.footer(^.className := ProposalTileStyles.footer)(
                        <.p(^.className := js.Array(TextStyles.smallerText, ProposalTileWithThemeStyles.themeInfo))(
                          unescape(I18n.t("proposal.associated-with-the-theme")),
                          <.Link(
                            ^.to := s"/${self.props.wrapped.country}/theme/${self.props.wrapped.themeSlug}",
                            ^.className := js.Array(TextStyles.title, ProposalTileWithThemeStyles.themeName)
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
    )
}

object ProposalTileWithThemeStyles extends StyleSheet.Inline {

  import dsl._

  val themeName: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val themeInfo: StyleA =
    style(color(ThemeStyles.TextColor.light))

}
