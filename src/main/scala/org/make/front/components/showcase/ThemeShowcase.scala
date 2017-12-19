package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.core.Counter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalTile.ProposalTileProps
import org.make.front.components.showcase.PromptingToProposeInRelationToThemeTile.PromptingToProposeInRelationToThemeTileProps
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements}
import org.make.front.facades.{HexToRgba, I18n, Replacements}
import org.make.front.models.{SequenceId, GradientColor => GradientColorModel, Location => LocationModel, OperationExpanded => OperationModel, Proposal => ProposalModel, TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, RWDHideRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.proposal.SearchResult
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ThemeShowcase {

  final case class ThemeShowcaseProps(proposals: () => Future[SearchResult],
                                      theme: TranslatedThemeModel,
                                      maybeIntro: Option[String] = None,
                                      maybeNews: Option[String] = None,
                                      maybeOperation: Option[OperationModel] = None,
                                      maybeSequenceId: Option[SequenceId] = None,
                                      maybeLocation: Option[LocationModel] = None)

  final case class ThemeShowcaseState(proposals: Seq[ProposalModel])

  lazy val reactClass: ReactClass =
    WithRouter(React.createClass[ThemeShowcaseProps, ThemeShowcaseState](displayName = "Showcase", getInitialState = { _ =>
      ThemeShowcaseState(Seq.empty)
    },
      componentWillReceiveProps = { (self, props) =>
        props.wrapped.proposals().onComplete {
          case Failure(_) =>
          case Success(results) => self.setState(_.copy(proposals = results.results))
        }
      }, render = {
        self =>
          val gradientValues: GradientColorModel =
            self.props.wrapped.theme.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))
          val index = self.props.wrapped.theme.order

          object DynamicThemeShowcaseStyles extends StyleSheet.Inline {

            import dsl._

            val gradient: (Int) => StyleA =
              styleF.int(Range(index, index + 1)) { index =>
                styleS(
                  background := s"linear-gradient(130deg, ${HexToRgba(gradientValues.from, 0.1F)}, ${HexToRgba(gradientValues.to, 0.1F)})"
                )
              }
          }

          val counter = Counter.showcaseCounter

          if (self.state.proposals.nonEmpty) {

            def proposalTile(self: Self[ThemeShowcaseProps, ThemeShowcaseState], proposal: ProposalModel) = <.ProposalTileComponent(
              ^.wrapped :=
                ProposalTileProps(
                  proposal = proposal,
                  index = counter.getAndIncrement(),
                  maybeTheme = Some(self.props.wrapped.theme),
                  maybeOperation = self.props.wrapped.maybeOperation,
                  maybeSequenceId = self.props.wrapped.maybeSequenceId,
                  maybeLocation = self.props.wrapped.maybeLocation,
                  trackingLocation = TrackingLocation.showcaseHomepage
                )
            )()

            val goToTheme: () => Unit = () => {
              TrackingService.track(
                "click-proposal-viewmore",
                TrackingContext(TrackingLocation.showcaseHomepage),
                Map("themeId" -> self.props.wrapped.theme.id.value)
              )
              self.props.history.push(s"/theme/${self.props.wrapped.theme.slug}")
            }

            <.section(^.className := Seq(ThemeShowcaseStyles.wrapper, DynamicThemeShowcaseStyles.gradient(index)))(
              Seq(
                <.header(^.className := LayoutRulesStyles.centeredRow)(
                  if (self.props.wrapped.maybeIntro.nonEmpty) {
                    <.p(^.className := Seq(ThemeShowcaseStyles.intro, TextStyles.mediumText, TextStyles.intro))(
                      self.props.wrapped.maybeIntro
                    )
                  }, <.h2(^.className := Seq(if (self.props.wrapped.maybeNews.nonEmpty) {
                    ThemeShowcaseStyles.titleBeforeNews
                  } else {
                    ThemeShowcaseStyles.title
                  }, TextStyles.bigTitle))(self.props.wrapped.theme.title), if (self.props.wrapped.maybeNews.nonEmpty) {
                    <.p(
                      ^.className := Seq(ThemeShowcaseStyles.news, TextStyles.smallerText),
                      ^.dangerouslySetInnerHTML := self.props.wrapped.maybeNews.getOrElse("")
                    )()
                  }),
                <.div(^.className := Seq(RWDHideRulesStyles.hideBeyondMedium, LayoutRulesStyles.centeredRow, ThemeShowcaseStyles.slideshow))(
                  <.Slider(^.infinite := false, ^.arrows := false)(
                    self.state.proposals.map(
                      proposal =>
                        <.div(
                          ^.className :=
                            ThemeShowcaseStyles.propasalItem)(
                          proposalTile(self, proposal)
                        )
                    ),
                    <.div(
                      ^.className :=
                        ThemeShowcaseStyles.propasalItem)(
                      <.PromptingToProposeInRelationToThemeTileComponent(
                        ^.wrapped :=
                          PromptingToProposeInRelationToThemeTileProps(
                            theme = self.props.wrapped.theme
                          ))())

                  )
                ),
                <.div(^.className := RWDHideRulesStyles.showBlockBeyondMedium)(
                  <.ul(^.className := Seq(LayoutRulesStyles.centeredRowWithCols, ThemeShowcaseStyles.propasalsList))(
                    self.state.proposals.map(
                      proposal =>
                        <.li(
                          ^.className := Seq(
                            ThemeShowcaseStyles.propasalItem,
                            ColRulesStyles.col,
                            ColRulesStyles.colHalfBeyondMedium,
                            ColRulesStyles.colQuarterBeyondLarge
                          )
                        )(
                          proposalTile(self, proposal)
                        )
                    ),
                    <.li(
                      ^.className := Seq(
                        ThemeShowcaseStyles.propasalItem,
                        ColRulesStyles.col,
                        ColRulesStyles.colHalfBeyondMedium,
                        ColRulesStyles.colQuarterBeyondLarge
                      )
                    )(
                      <.PromptingToProposeInRelationToThemeTileComponent(
                        ^.wrapped :=
                          PromptingToProposeInRelationToThemeTileProps(
                            theme = self.props.wrapped.theme
                          ))()
                    )
                  )),
                <.p(^.className := Seq(LayoutRulesStyles.centeredRow, ThemeShowcaseStyles.SeeMoreLinkWrapper))(
                  <.button(
                    ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton),
                    ^.onClick := goToTheme
                  )(
                    I18n.t(
                      "theme-showcase.see-all",
                      Replacements("themeName" -> self.props.wrapped.theme.title)
                    )
                  )
                )
                ,
                <.style()(ThemeShowcaseStyles.render[String], DynamicThemeShowcaseStyles.render[String])
              )
            )
          } else <.div.empty
      })
    )
}

object ThemeShowcaseStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(
        padding(
          ThemeStyles.SpacingValue.larger.pxToEm(),
          `0`,
          (ThemeStyles.SpacingValue.larger - ThemeStyles.SpacingValue.small).pxToEm()
        )
      ),
      overflow.hidden
    )

  val intro: StyleA = style(
    marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(15)),
    ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(18)))
  )

  val title: StyleA = style(lineHeight(1))

  val titleBeforeNews: StyleA = style(
    display.inlineBlock,
    float.left,
    marginRight(ThemeStyles.SpacingValue.small.pxToEm(20)),
    ThemeStyles.MediaQueries.beyondSmall(marginRight(ThemeStyles.SpacingValue.small.pxToEm(34))),
    ThemeStyles.MediaQueries.beyondMedium(marginRight(ThemeStyles.SpacingValue.small.pxToEm(46))),
    lineHeight(1)
  )

  val news: StyleA =
    style(
      display.inlineBlock,
      float.left,
      padding(2.pxToEm(13), (ThemeStyles.SpacingValue.small / 2).pxToEm(13)),
      lineHeight(15.pxToEm(13)),
      ThemeStyles.MediaQueries
        .beyondSmall(padding(2.pxToEm(14), (ThemeStyles.SpacingValue.small / 2).pxToEm(14)), lineHeight(15.pxToEm(14))),
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.BackgroundColor.black)
    )

  val slideshow: StyleA =
    style(
      unsafeChild(".slick-list")(
        overflow.visible
      ),
      unsafeChild(".slick-slide")(
        height.auto,
        minHeight.inherit,
        paddingRight(10.pxToEm())
      ),
      unsafeChild(".slick-track")(
        display.flex
      )
    )

  val propasalsList: StyleA =
    style(display.flex, flexWrap.wrap, width(100.%%))

  val propasalItem: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val SeeMoreLinkWrapper: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
    marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
    textAlign.center
  )
}
