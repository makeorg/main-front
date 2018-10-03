/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM.{RouterDOMVirtualDOMElements, RouterVirtualDOMAttributes}
import org.make.core.Counter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalTile.{PostedIn, ProposalTileProps}
import org.make.front.components.showcase.PromptingToProposeInRelationToThemeTile.PromptingToProposeInRelationToThemeTileProps
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{HexToRgba, I18n, Replacements}
import org.make.front.helpers.{NumberFormat, RouteHelper}
import org.make.front.models.{SequenceId, GradientColor => GradientColorModel, Location => LocationModel, OperationExpanded => OperationModel, Proposal => ProposalModel, TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles._
import org.make.front.styles.base._
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.proposal.SearchResult
import org.make.services.tracking.TrackingLocation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object ThemeShowcase {

  final case class ThemeShowcaseProps(proposals: () => Future[SearchResult],
                                      theme: TranslatedThemeModel,
                                      maybeIntro: Option[String] = None,
                                      maybeNews: Option[String] = None,
                                      maybeOperation: Option[OperationModel] = None,
                                      maybeSequenceId: Option[SequenceId] = None,
                                      maybeLocation: Option[LocationModel] = None,
                                      country: String)

  final case class ThemeShowcaseState(proposals: js.Array[ProposalModel])

  lazy val reactClass: ReactClass =
    WithRouter(React.createClass[ThemeShowcaseProps, ThemeShowcaseState](displayName = "Showcase", getInitialState = { _ =>
      ThemeShowcaseState(js.Array())
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

            // needed to get a different class name by theme
            val gradient: Int => StyleA =
              styleF.int(Range(index, index + 1)) { _ =>
                styleS(
                  background := s"linear-gradient(130deg, ${HexToRgba(gradientValues.from, 0.1F)}, ${HexToRgba(gradientValues.to, 0.1F)})"
                )
              }
          }

          val counter = Counter.showcaseCounter

            def proposalTile(self: Self[ThemeShowcaseProps, ThemeShowcaseState], proposal: ProposalModel) = <.ProposalTileComponent(
              ^.wrapped :=
                ProposalTileProps(
                  proposal = proposal,
                  index = counter.getAndIncrement(),
                  maybeTheme = Some(self.props.wrapped.theme),
                  maybeOperation = self.props.wrapped.maybeOperation,
                  maybeSequenceId = self.props.wrapped.maybeSequenceId,
                  maybeLocation = self.props.wrapped.maybeLocation,
                  trackingLocation = TrackingLocation.showcaseHomepage,
                  country = self.props.wrapped.country,
                  maybePostedIn = Some(
                    PostedIn(
                      name = self.props.wrapped.theme.title,
                      link = RouteHelper.operationRoute(self.props.wrapped.country, self.props.wrapped.theme.slug)
                    )
                  )

                )
            )()

          def sliderContent() = js.Array(
            self.state.proposals.map(
              proposal =>
                <.div(
                  ^.className :=
                    js.Array(ColRulesStyles.col,
                      ThemeShowcaseStyles.propasalItem))(
                  proposalTile(self, proposal)
                )
            ).toSeq,
            <.div(
              ^.className :=
                js.Array(ColRulesStyles.col,
                  ThemeShowcaseStyles.propasalItem))(
              <.PromptingToProposeInRelationToThemeTileComponent(
                ^.wrapped :=
                  PromptingToProposeInRelationToThemeTileProps(
                    theme = self.props.wrapped.theme
                  ))()
            )).toSeq

          if (self.props.wrapped.theme.title.nonEmpty) {
            <.section(^.className := js.Array(ThemeShowcaseStyles.wrapper, DynamicThemeShowcaseStyles.gradient(index)))(
              js.Array(
                <.header(^.className := LayoutRulesStyles.centeredRow)(
                  if (self.props.wrapped.maybeIntro.nonEmpty) {
                    <.p(^.className := js.Array(ThemeShowcaseStyles.intro, TextStyles.mediumText, TextStyles.intro))(
                      self.props.wrapped.maybeIntro
                    )
                  },
                  <.div(^.className := ThemeShowcaseStyles.themeNameWrapper)(
                    <.h2(^.className := js.Array(ThemeShowcaseStyles.themeName, TextStyles.bigTitle))(
                      <.Link(^.to := s"/${self.props.wrapped.country}/theme/${self.props.wrapped.theme.slug}")(
                        self.props.wrapped.theme.title
                      )
                    )
                  ),
                  <.div(^.className := ThemeShowcaseStyles.themeInfo)(
                    if (self.props.wrapped.maybeNews.nonEmpty) {
                      <.p(^.className := ThemeShowcaseStyles.news)(
                        <.span(
                          ^.className := TextStyles.smallerText,
                          ^.dangerouslySetInnerHTML := self.props.wrapped.maybeNews.getOrElse(""))()
                      )
                    } else {
                      <.ul(^.className := js.Array(TableLayoutBeyondMediumStyles.wrapper, TextStyles.title))(
                        if (self.props.wrapped.theme.proposalsCount > 0) {
                          <.li(^.className := js.Array(ThemeShowcaseStyles.themeData, TableLayoutBeyondMediumStyles.cell))(
                            <.em(^.className := ThemeShowcaseStyles.themeDataValue)(NumberFormat.formatToKilo(self.props.wrapped.theme.proposalsCount)),
                            unescape(
                              I18n.t("theme-showcase.data-units.proposals")
                            )
                          )
                        },
                        if (self.props.wrapped.theme.votesCount > 0) {
                          <.li(^.className := js.Array(ThemeShowcaseStyles.themeData, TableLayoutBeyondMediumStyles.cell))(
                            <.em(^.className := ThemeShowcaseStyles.themeDataValue)(NumberFormat.formatToKilo(self.props.wrapped.theme.votesCount)),
                            unescape(
                              I18n.t("theme-showcase.data-units.votes")
                            )
                          )
                        },
                        if (self.props.wrapped.theme.actionsCount > 0) {
                          <.li(^.className := js.Array(ThemeShowcaseStyles.themeData, TableLayoutBeyondMediumStyles.cell))(
                            <.em(^.className := ThemeShowcaseStyles.themeDataValue)(NumberFormat.formatToKilo(self.props.wrapped.theme.actionsCount)),
                            unescape(
                              I18n.t("theme-showcase.data-units.actions")
                            )
                          )
                        }
                      )
                    })
                  /*,
                  <.div(^.className := ThemeShowcaseStyles.filter)(
                    <.p(^.className := ThemeShowcaseStyles.filterIntro)(
                      <.span(^.className := TextStyles.smallerText)(
                        unescape(I18n.t("theme-showcase.filter.intro"))
                      )
                    ),
                    <.button(^.className := ThemeShowcaseStyles.filterCTA)(
                      <.span(^.className := TextStyles.smallerText)(
                        unescape(I18n.t("theme-showcase.filter.the-most-popular"))
                      )
                    ),
                    <.button(^.className := ThemeShowcaseStyles.filterCTA)(
                      <.span(^.className := TextStyles.smallerText)(
                        unescape(I18n.t("theme-showcase.filter.the-most-original"))
                      )
                    )
                  )*/
                  /*TODO: dev filter functions and reactivate template part*/
                ),
                <.div(^.className := js.Array(RWDRulesMediumStyles.hideBeyondMedium, LayoutRulesStyles.centeredRowWithCols, ThemeShowcaseStyles.slideshow))(
                  <.Slider(^.infinite := false, ^.arrows := false)(
                    sliderContent()
                  )
                ),
                <.div(^.className := js.Array(RWDRulesMediumStyles.showBlockBeyondMedium, RWDHideRulesStyles.hideBeyondLarge, LayoutRulesStyles.centeredRowWithCols, ThemeShowcaseStyles.slideshow))(
                  <.Slider(^.infinite := false, ^.arrows := false, ^.slidesToShow := 2, ^.slidesToScroll := 2)(
                    sliderContent()
                  )
                ),
                <.div(^.className := RWDHideRulesStyles.showBlockBeyondLarge)(
                  <.ul(^.className := js.Array(LayoutRulesStyles.centeredRowWithCols, ThemeShowcaseStyles.propasalsList))(
                    self.state.proposals.map(
                      proposal =>
                        <.li(
                          ^.className := js.Array(
                            ThemeShowcaseStyles.propasalItem,
                            ColRulesStyles.col,
                            ColRulesStyles.colQuarterBeyondLarge
                          )
                        )(
                          proposalTile(self, proposal)
                        )
                    ).toSeq,
                    <.li(
                      ^.className := js.Array(
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
                  )
                ),
                <.p(^.className := js.Array(LayoutRulesStyles.centeredRow, ThemeShowcaseStyles.seeMoreLinkWrapper))(
                  <.Link(
                    ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA),
                    ^.to := s"/${self.props.wrapped.country}/theme/${self.props.wrapped.theme.slug}"
                  )(
                    I18n.t(
                        "theme-showcase.see-all",
                        Replacements("themeName" ->  self.props.wrapped.theme.title)
                      )
                  )
                ),
                <.style()(ThemeShowcaseStyles.render[String], DynamicThemeShowcaseStyles.render[String])
              ).toSeq
            )
          } else <.div.empty
      })
    )
}

object ThemeShowcaseStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
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
    ThemeStyles.MediaQueries.beyondSmall(
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(18))
    )
  )

  val themeNameWrapper: StyleA = style(
    ThemeStyles.MediaQueries.beyondMedium(
      display.inlineBlock,
      verticalAlign.top,
      marginRight(ThemeStyles.SpacingValue.medium.pxToEm())
    )
  )

  val themeName: StyleA = style(
    unsafeChild("a")(
      color(ThemeStyles.TextColor.base)
    )
  )

  val themeInfo: StyleA = style(
    display.inlineBlock,
    ThemeStyles.MediaQueries.beyondMedium(
      verticalAlign.top
    )
  )

  val news: StyleA =
    style(
      padding(2.pxToEm(), (ThemeStyles.SpacingValue.small / 2).pxToEm()),
      lineHeight(15.pxToEm()),
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.BackgroundColor.black),
      unsafeChild("*")(
        lineHeight.inherit
      )
    )
  val themeData: StyleA =
    style(
      paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
      &.lastChild(paddingRight(`0`)),
      fontSize(14.pxToEm()),
      color(ThemeStyles.TextColor.lighter),
      ThemeStyles.MediaQueries.belowMedium(
        display.inlineBlock,
        marginRight(ThemeStyles.SpacingValue.small.pxToEm())
      )
    )

  val themeDataValue: StyleA =
    style(
      &.after(content := "' '"),
      ThemeStyles.MediaQueries.beyondMedium(
        display.inlineBlock,
        width(100.%%),
        &.after(content := none),
        fontSize(24.pxToEm(14)))
    )

  val filter: StyleA =
    style(
      ThemeStyles.MediaQueries.belowMedium(
        marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()))
    )

  val filterIntro: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.baseline,
      paddingRight(ThemeStyles.SpacingValue.smaller.pxToEm()),
      color(ThemeStyles.TextColor.lighter)
    )

  val filterCTA: StyleA =
    style(
      position.relative,
      display.inlineBlock,
      verticalAlign.baseline,
      paddingRight((ThemeStyles.SpacingValue.smaller * 2).pxToEm()),
      &.firstChild(paddingLeft(`0`)),
      &.after(
        content := "' '",
        position.absolute,
        top(`0`),
        right(ThemeStyles.SpacingValue.smaller.pxToEm()),
        height(20.pxToEm()),
        width(1.px),
        backgroundColor(ThemeStyles.BorderColor.lighter)
      ),
      &.lastChild(&.after(content := none)),
      color.inherit,
      transition := "color .2s ease-in-out",
      &.hover(color(ThemeStyles.ThemeColor.primary))
    )

  val slideshow: StyleA =
    style(
      width(95.%%),
      unsafeChild(".slick-list")(
        overflow.visible
      ),
      unsafeChild(".slick-slide")(
        height.auto,
        minHeight.inherit
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

  val seeMoreLinkWrapper: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
    marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
    textAlign.center
  )
}
