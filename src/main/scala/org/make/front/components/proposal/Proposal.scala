package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.ShareProposal.ShareProposalProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.components.showcase.ThemeShowcaseContainer.ThemeShowcaseContainerProps
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

  final case class ProposalProps(futureProposal: Future[ProposalModel],
                                 themeName: Option[String],
                                 themeSlug: Option[String],
                                 index: Int)

  final case class ProposalState(proposal: ProposalModel, themeName: Option[String], themeSlug: Option[String])

  lazy val reactClass: ReactClass =
    React.createClass[ProposalProps, ProposalState](
      displayName = "Proposal",
      getInitialState = { _ =>
        ProposalState(proposal = null, themeName = None, themeSlug = None)
      },
      componentWillReceiveProps = { (self, props) =>
        self.props.wrapped.futureProposal.onComplete {
          case Failure(_) =>
          case Success(proposal) =>
            self.setState(_.copy(proposal = proposal))
        }
      },
      render = { self =>
        if (self.state.proposal != null) {
          <("proposal")()(
            <.div(^.className := Seq(ProposalStyles.wrapper))(
              <.div(^.className := Seq(ProposalStyles.articleWrapper))(
                <.div(^.className := Seq(ProposalStyles.articleSubWrapper))(
                  <.div(^.className := Seq(RowRulesStyles.centeredRow, ProposalStyles.fullHeight))(
                    <.div(^.className := Seq(ColRulesStyles.col, ProposalStyles.fullHeight))(
                      <.article(^.className := ProposalStyles.article)(
                        <.div(^.className := ProposalStyles.articleInnerWrapper)(
                          <.div(^.className := ProposalStyles.articleInnerSubWrapper)(
                            <.div(^.className := Seq(RowRulesStyles.row))(
                              <.div(^.className := ColRulesStyles.col)(
                                <.div(^.className := ProposalStyles.infosWrapper)(
                                  <.p(^.className := Seq(TextStyles.mediumText, ProposalStyles.infos))(
                                    ProposalAuthorInfosFormat.apply(self.state.proposal)
                                  )
                                ),
                                <.div(^.className := ProposalStyles.contentWrapper)(
                                  <.h3(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(
                                    self.state.proposal.content
                                  ),
                                  <.div(^.className := ProposalStyles.voteWrapper)(
                                    <.VoteContainerComponent(
                                      ^.wrapped := VoteContainerProps(
                                        proposal = self.state.proposal,
                                        index = self.props.wrapped.index
                                      )
                                    )()
                                  )
                                ),
                                self.state.themeName
                              )
                            )
                          )
                        )
                      )
                    )
                  )
                )
              ),
              <.div(^.className := Seq(ProposalStyles.shareArticleWrapper))(
                <.div(^.className := Seq(ProposalStyles.shareArticleSubWrapper))(
                  <.div(^.className := RowRulesStyles.centeredRow)(
                    <.div(^.className := ColRulesStyles.col)(
                      <.ShareProposalComponent(^.wrapped := ShareProposalProps(proposal = self.state.proposal))()
                    )
                  )
                )
              )
            ),
            if (self.state.proposal.themeId.nonEmpty) {
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
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent)
    )

  val articleWrapper: StyleA =
    style(display.tableRow, height(100.%%))

  val articleSubWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      paddingTop((ThemeStyles.SpacingValue.larger + 50).pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((ThemeStyles.SpacingValue.larger + 80).pxToEm())),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm())
    )

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
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
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
    style(textAlign.center, marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val voteWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val ctaWrapper: StyleA =
    style(textAlign.center, marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val ctaVisibility: (Boolean) => StyleA = styleF.bool(
    visible =>
      if (visible) {
        styleS(visibility.visible)
      } else {
        styleS(visibility.hidden)
    }
  )

  val shareArticleWrapper: StyleA =
    style(display.tableRow)

  val shareArticleSubWrapper: StyleA =
    style(display.tableCell, paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()))

}
