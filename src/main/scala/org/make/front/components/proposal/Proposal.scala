package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalContainer.ProposalAndThemeInfosModel
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.components.showcase.ThemeShowcaseContainer.ThemeShowcaseContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.helpers.ProposalAuthorInfosFormat
import org.make.front.models.{
  Location        => LocationModel,
  Operation       => OperationModel,
  Proposal        => ProposalModel,
  Sequence        => SequenceModel,
  TranslatedTheme => TranslatedThemeModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Proposal {

  final case class ProposalProps(futureProposalAndThemeInfos: Future[ProposalAndThemeInfosModel],
                                 maybeTheme: Option[TranslatedThemeModel],
                                 maybeOperation: Option[OperationModel],
                                 maybeSequence: Option[SequenceModel],
                                 maybeLocation: Option[LocationModel])

  final case class ProposalState(proposal: Option[ProposalModel] = None,
                                 maybeLocation: Option[LocationModel] = None,
                                 maybeTheme: Option[TranslatedThemeModel] = None,
                                 maybeOperation: Option[OperationModel] = None)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[ProposalProps, ProposalState](
        displayName = "Proposal",
        getInitialState = { _ =>
          ProposalState()
        },
        componentWillReceiveProps = { (self, props) =>
          props.wrapped.futureProposalAndThemeInfos.onComplete {
            case Failure(_) =>
            case Success(futureProposalAndThemeInfos) =>
              self.setState(
                _.copy(
                  proposal = futureProposalAndThemeInfos.proposal,
                  maybeLocation = futureProposalAndThemeInfos.proposal.map { proposal =>
                    LocationModel.ProposalPage(proposal.id)
                  },
                  maybeTheme = futureProposalAndThemeInfos.theme,
                  maybeOperation = futureProposalAndThemeInfos.operation
                )
              )
          }
        },
        render = { self =>
          def onclickSeeMore: () => Unit = { () =>
            scalajs.js.Dynamic.global.window.open(I18n.t("welcome-vff.intro.see-more-link"), "_blank")
          }
          <("proposal")()(
            <.div(
              ^.className := Seq(
                TableLayoutStyles.fullHeightWrapper,
                ProposalStyles.wrapper(self.state.proposal.isDefined)
              )
            )(
              <.div(^.className := TableLayoutStyles.row)(
                <.div(^.className := Seq(TableLayoutStyles.cell, ProposalStyles.mainHeaderWrapper))(
                  <.MainHeaderComponent.empty
                )
              ),
              <.div(^.className := Seq(TableLayoutStyles.row, ProposalStyles.fullHeight))(
                <.div(^.className := Seq(TableLayoutStyles.cell, ProposalStyles.articleCell))(
                  if (self.state.proposal.isDefined) {
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
                                  self.state.proposal.map { proposal =>
                                    ProposalAuthorInfosFormat.apply(proposal)
                                  }
                                )
                              ),
                              <.div(^.className := ProposalStyles.contentWrapper)(
                                <.h1(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(
                                  self.state.proposal.map(_.content)
                                ),
                                <.div(^.className := ProposalStyles.voteWrapper)(self.state.proposal.map { proposal =>
                                  <.VoteContainerComponent(
                                    ^.wrapped := VoteContainerProps(
                                      proposal = proposal,
                                      index = 1,
                                      maybeTheme = self.state.maybeTheme,
                                      maybeOperation = self.state.maybeOperation,
                                      maybeSequence = self.props.wrapped.maybeSequence,
                                      maybeLocation = self.state.maybeLocation
                                    )
                                  )()
                                })
                              ),
                              if (self.state.maybeOperation.isDefined) {
                                <.div(^.className := ProposalStyles.operationInfo)(
                                  <.p(^.className := Seq(TextStyles.mediumText, ProposalStyles.operationText))(
                                    unescape(I18n.t("proposal.associated-with-the-operation")),
                                    <.div(
                                      ^.dangerouslySetInnerHTML := self.state.maybeOperation
                                        .map(_.baselineHTML)
                                        .getOrElse("")
                                    )()
                                  ),
                                  <.p(^.className := ProposalStyles.seeMore)(
                                    <.Link(
                                      ^.to := s"/consultation/${self.state.maybeOperation.map(_.slug).getOrElse("")}",
                                      ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA)
                                    )(unescape(I18n.t("welcome-vff.intro.see-more"))),
                                    <.a(
                                      ^.href := I18n.t("operation.vff-header.article.see-more.link"),
                                      ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA),
                                      ^.target := "_blank"
                                    )(unescape(I18n.t("operation.vff-header.article.see-more.label")))
                                  )
                                )

                              } else if (self.state.maybeTheme.isDefined) {
                                <.p(^.className := Seq(TextStyles.mediumText, ProposalStyles.themeInfo))(
                                  unescape(I18n.t("proposal.associated-with-the-theme")),
                                  <.Link(
                                    ^.to := s"/theme/${self.state.maybeTheme.map(_.slug).getOrElse("")}",
                                    ^.className := Seq(TextStyles.title, ProposalStyles.themeName)
                                  )(self.state.maybeTheme.map(_.title))
                                )
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
                  maybeOperation = self.props.wrapped.maybeOperation,
                  maybeSequence = self.props.wrapped.maybeSequence,
                  maybeLocation = self.state.maybeLocation
                )
              )()
            },
            <.NavInThemesContainerComponent.empty,
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
          height :=! s"calc(100% - ${100.pxToEm().value})",
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
      padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`) /*TODO: restaure medium with reactivation of sharing part*/
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

  val operationInfo: StyleA =
    style(
      textAlign.center,
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
        width(100.%%),
        backgroundColor(ThemeStyles.BorderColor.lighter)
      ),
      ThemeStyles.MediaQueries.beyondMedium(display.flex, paddingTop(10.px), paddingBottom(10.px))
    )

  val operationLogo: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val operationText: StyleA =
    style(
      color(ThemeStyles.TextColor.light),
      width(100.%%),
      ThemeStyles.MediaQueries.beyondMedium(
        width(60.%%),
        (&.after)(
          content := "''",
          position.absolute,
          left(60.%%),
          transform := s"translateY(-100%)",
          marginRight(-0.5.px),
          width(1.px),
          height(80.%%),
          backgroundColor(ThemeStyles.BorderColor.lighter)
        )
      )
    )

  val seeMore: StyleA =
    style(
      margin.auto,
      paddingLeft(1.em),
      width(100.%%),
      display.table,
      unsafeChild("a")(display.tableCell),
      ThemeStyles.MediaQueries
        .beyondMedium(
          display.block,
          width(40.%%),
          textAlign.left,
          unsafeChild("a")(marginLeft(1.em), display.inlineBlock)
        )
    )

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
