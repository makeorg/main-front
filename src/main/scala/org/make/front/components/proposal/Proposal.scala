package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalContainer.ProposalAndThemeInfosModel
import org.make.front.components.proposal.ShareProposal.ShareProposalProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.components.showcase.ThemeShowcaseContainer.ThemeShowcaseContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.helpers.ProposalAuthorInfosFormat
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet

object Proposal {

  final case class ProposalProps(futureProposalAndThemeInfos: Future[ProposalAndThemeInfosModel])

  final case class ProposalState(proposal: ProposalModel, themeName: Option[String], themeSlug: Option[String])

  lazy val reactClass: ReactClass =
    React.createClass[ProposalProps, ProposalState](
      displayName = "Proposal",
      getInitialState = { _ =>
        ProposalState(proposal = null, themeName = None, themeSlug = None)
      },
      componentWillReceiveProps = { (self, props) =>
        props.wrapped.futureProposalAndThemeInfos.onComplete {
          case Failure(_) =>
          case Success(futureProposalAndThemeInfos) =>
            self.setState(
              _.copy(
                proposal = futureProposalAndThemeInfos.proposal,
                themeName = futureProposalAndThemeInfos.themeName,
                themeSlug = futureProposalAndThemeInfos.themeSlug
              )
            )
        }
      },
      render = { self =>
        if (self.state.proposal != null) {
          <("proposal")()(
            <.div(^.className := ProposalStyles.wrapper)(
              <.div(^.className := ProposalStyles.row)(
                <.div(^.className := ProposalStyles.cell)(<.MainHeaderComponent.empty)
              ),
              <.div(^.className := Seq(ProposalStyles.row, ProposalStyles.fullHeight))(
                <.div(^.className := Seq(ProposalStyles.cell, ProposalStyles.articleCell))(
                  <.div(^.className := Seq(RowRulesStyles.centeredRow, ProposalStyles.fullHeight))(
                    <.div(^.className := Seq(ColRulesStyles.col, ProposalStyles.fullHeight))(
                      <.article(^.className := ProposalStyles.article)(
                        <.div(^.className := ProposalStyles.articleInnerWrapper)(
                          <.div(^.className := ProposalStyles.articleInnerSubWrapper)(
                            <.div(^.className := RowRulesStyles.row)(
                              <.div(^.className := ColRulesStyles.col)(
                                <.div(^.className := ProposalStyles.infosWrapper)(
                                  <.p(^.className := Seq(TextStyles.mediumText, ProposalStyles.infos))(
                                    ProposalAuthorInfosFormat.apply(self.state.proposal)
                                  )
                                ),
                                <.div(^.className := ProposalStyles.contentWrapper)(
                                  <.h1(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(
                                    self.state.proposal.content
                                  ),
                                  <.div(^.className := ProposalStyles.voteWrapper)(
                                    <.VoteContainerComponent(
                                      ^.wrapped := VoteContainerProps(proposal = self.state.proposal, index = 1)
                                    )()
                                  )
                                ),
                                if (self.state.themeSlug.nonEmpty) {
                                  <.p(^.className := Seq(TextStyles.mediumText, ProposalStyles.themeInfo))(
                                    unescape(I18n.t("proposal.associated-with-the-theme")),
                                    <.Link(
                                      ^.to := s"/theme/${self.state.themeSlug.getOrElse("")}",
                                      ^.className := Seq(TextStyles.title, ProposalStyles.themeName)
                                    )(self.state.themeName.getOrElse(""))
                                  )
                                }
                              )
                            )
                          )
                        )
                      )
                    )
                  )
                )
              ) /*,
              <.div(^.className := Seq(ProposalStyles.row))(
                <.div(^.className := Seq(ProposalStyles.cell, ProposalStyles.shareArticleCell))(
                  <.div(^.className := RowRulesStyles.centeredRow)(
                    <.div(^.className := ColRulesStyles.col)(
                      <.ShareProposalComponent(^.wrapped := ShareProposalProps(proposal = self.state.proposal))()
                    )
                  )
                )
              )*/
            ),
            if (self.state.themeSlug.nonEmpty) {
              <.ThemeShowcaseContainerComponent(
                ^.wrapped := ThemeShowcaseContainerProps(themeSlug = self.state.themeSlug.getOrElse(""))
              )()
            },
            <.NavInThemesContainerComponent.empty,
            <.style()(ProposalStyles.render[String])
          )
        } else {
          <.div.empty
        }
      }
    )
}

object ProposalStyles extends StyleSheet.Inline {
  import dsl._

  val fullHeight: StyleA =
    style(height(100.%%))

  val wrapper: StyleA =
    style(
      display.table,
      width(100.%%),
      height(100.%%),
      height :=! s"calc(100% - ${100.pxToEm().value})",
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent)
    )

  val row: StyleA =
    style(display.tableRow)

  val cell: StyleA =
    style(display.tableCell)

  val articleCell: StyleA =
    style(
      verticalAlign.middle,
      padding :=! s"${ThemeStyles.SpacingValue.larger.pxToEm().value} 0" /*TODO: restaure medium with reactivation of sharing part*/
    )

  val shareArticleCell: StyleA =
    style(paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()))

  val article: StyleA =
    style(
      height(100.%%),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val articleInnerWrapper: StyleA =
    style(display.table, height(100.%%), width(100.%%))

  val articleInnerSubWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm())
    )

  val infosWrapper: StyleA =
    style(
      textAlign.center,
      position.relative,
      paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      (&.after)(
        content := "''",
        position.absolute,
        top(100.%%),
        left(50.%%),
        transform := s"translateX(-50%)",
        marginTop(-0.5.px),
        height(1.px),
        width(90.pxToEm()),
        backgroundColor(ThemeStyles.BorderColor.lighter)
      )
    )

  val infos: StyleA =
    style(color(ThemeStyles.TextColor.light))

  val contentWrapper: StyleA =
    style(textAlign.center, marginTop(ThemeStyles.SpacingValue.medium.pxToEm()), overflow.hidden)

  val voteWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val themeInfo: StyleA =
    style(
      textAlign.center,
      position.relative,
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      color(ThemeStyles.TextColor.light),
      (&.after)(
        content := "''",
        position.absolute,
        bottom(100.%%),
        left(50.%%),
        transform := s"translateX(-50%)",
        marginTop(-0.5.px),
        height(1.px),
        width(90.pxToEm()),
        backgroundColor(ThemeStyles.BorderColor.lighter)
      )
    )

  val themeName: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

}
