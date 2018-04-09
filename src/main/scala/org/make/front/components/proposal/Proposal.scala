package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalContainer.ProposalAndThemeOrOperationModel
import org.make.front.components.proposal.ProposalSOperationInfos.ProposalSOperationInfosProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.components.showcase.ThemeShowcaseContainer.ThemeShowcaseContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.helpers.ProposalAuthorInfosFormat
import org.make.front.models.{
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingLocation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Proposal {

  final case class ProposalProps(futureProposal: Future[ProposalAndThemeOrOperationModel],
                                 language: String,
                                 country: String)

  final case class ProposalState(maybeProposal: Option[ProposalModel] = None,
                                 maybeTheme: Option[TranslatedThemeModel] = None,
                                 maybeOperation: Option[OperationModel] = None,
                                 maybeLocation: Option[LocationModel] = None)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[ProposalProps, ProposalState](
        displayName = "Proposal",
        getInitialState = { _ =>
          ProposalState()
        },
        componentWillReceiveProps = { (self, props) =>
          props.wrapped.futureProposal.onComplete {
            case Failure(_) =>
            case Success(futureProposal) =>
              self.setState(
                _.copy(
                  maybeProposal = futureProposal.maybeProposal,
                  maybeTheme = futureProposal.maybeTheme,
                  maybeOperation = futureProposal.maybeOperation,
                  maybeLocation = futureProposal.maybeProposal.map { proposal =>
                    LocationModel.ProposalPage(proposal.id)
                  }
                )
              )
          }
        },
        render = { self =>
          <("proposal")()(
            <.div(
              ^.className := Seq(
                TableLayoutStyles.fullHeightWrapper,
                ProposalStyles.wrapper(self.state.maybeProposal.isDefined)
              )
            )(
              <.div(^.className := TableLayoutStyles.row)(
                <.div(^.className := Seq(TableLayoutStyles.cell, ProposalStyles.mainHeaderWrapper))(
                  <.MainHeaderContainer.empty
                )
              ),
              <.div(^.className := Seq(TableLayoutStyles.row, ProposalStyles.fullHeight))(
                <.div(^.className := Seq(TableLayoutStyles.cell, ProposalStyles.articleCell))(
                  if (self.state.maybeProposal.isDefined) {
                    <.div(^.className := Seq(LayoutRulesStyles.centeredRow, ProposalStyles.fullHeight))(
                      <.article(^.className := ProposalStyles.article)(
                        <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
                          <.div(
                            ^.className := Seq(
                              TableLayoutStyles.cellVerticalAlignMiddle,
                              ProposalStyles.articleInnerWrapper
                            )
                          )(
                            <.div(^.className := LayoutRulesStyles.row)(
                              <.div(^.className := ProposalStyles.infosWrapper)(
                                <.p(^.className := Seq(TextStyles.mediumText, ProposalStyles.infos))(
                                  self.state.maybeProposal.map { proposal =>
                                    ProposalAuthorInfosFormat.apply(proposal)
                                  }
                                )
                              ),
                              <.div(^.className := ProposalStyles.contentWrapper)(
                                <.h1(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(
                                  self.state.maybeProposal.map(_.content)
                                ),
                                <.div(^.className := ProposalStyles.voteWrapper)(self.state.maybeProposal.map {
                                  proposal =>
                                    <.VoteContainerComponent(
                                      ^.wrapped := VoteContainerProps(
                                        proposal = proposal,
                                        index = 1,
                                        maybeTheme = self.state.maybeTheme,
                                        maybeOperation = self.state.maybeOperation,
                                        maybeSequenceId = None,
                                        maybeLocation = self.state.maybeLocation,
                                        trackingLocation = TrackingLocation.proposalPage
                                      )
                                    )()
                                })
                              ),
                              if (self.state.maybeOperation.isDefined) {
                                <.div(^.className := ProposalStyles.operationInfoWrapper)(
                                  <.ProposalSOperationInfosComponent(
                                    ^.wrapped := ProposalSOperationInfosProps(
                                      operation = self.state.maybeOperation.get,
                                      language = self.props.wrapped.language,
                                      country = self.props.wrapped.country
                                    )
                                  )()
                                )
                              } else {
                                self.state.maybeTheme.map { theme =>
                                  <.div(^.className := ProposalStyles.themeInfoWrapper)(
                                    <.p(^.className := Seq(TextStyles.mediumText, ProposalStyles.themeInfo))(
                                      unescape(I18n.t("proposal.associated-with-the-theme")),
                                      <.Link(
                                        ^.to := s"/${self.props.wrapped.country}/theme/${theme.slug}",
                                        ^.className := Seq(TextStyles.title, ProposalStyles.themeName)
                                      )(theme.title)
                                    )
                                  )
                                }
                              }
                            )
                          )
                        )
                      )
                    )
                  } else {
                    <.SpinnerComponent.empty
                  }
                )
              ) /*,
              <.div(^.className := Seq(TableLayoutStyles.row))(
                <.div(^.className := Seq(TableLayoutStyles.cell, ProposalStyles.shareArticleCell))(
                  <.div(^.className := LayoutRulesStyles.centeredRow)(
                    <.ShareComponent(^.wrapped := ShareProps(intro = Some(unescape(I18n.t("proposal.share-intro")))))()
                  )
                )
              )*/
            ),
            if (self.state.maybeTheme.isDefined) {
              <.ThemeShowcaseContainerComponent(
                ^.wrapped := ThemeShowcaseContainerProps(
                  themeSlug = self.state.maybeTheme.map(_.slug).getOrElse(""),
                  maybeLocation = self.state.maybeLocation
                )
              )()
            },
            if (self.state.maybeOperation.isEmpty) {
              <.NavInThemesContainerComponent.empty
            },
            <.style()(ProposalStyles.render[String])
          )
        }
      )
    )
}

object ProposalStyles extends StyleSheet.Inline {

  import dsl._

  val fullHeight: StyleA =
    style(height(100.%%))

  val wrapper: (Boolean) => StyleA = styleF.bool(
    isLoaded =>
      if (isLoaded) {
        styleS(
          height :=! s"calc(100% - ${ThemeStyles.SpacingValue.evenLarger.pxToEm().value})",
          backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent)
        )
      } else {
        styleS(backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))
    }
  )

  val mainHeaderWrapper: StyleA =
    style(visibility.hidden)

  val articleCell: StyleA =
    style(
      verticalAlign.middle,
      padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`)
      /*TODO: restaure medium with reactivation of sharing part*/
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
    style(paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()), paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

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

  val operationInfoWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val themeInfoWrapper: StyleA =
    style(
      position.relative,
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginTop(ThemeStyles.SpacingValue.medium.pxToEm()),
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

  val themeInfo: StyleA =
    style(textAlign.center, color(ThemeStyles.TextColor.light))

  val themeName: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

}
