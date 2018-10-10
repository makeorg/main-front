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
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM.{
  RouterDOMVirtualDOMElements,
  RouterVirtualDOMAttributes
}
import org.make.core.Counter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalTile.{PostedIn, ProposalTileProps}
import org.make.front.components.showcase.PromptingToProposeInRelationToOperationTile.PromptingToProposeInRelationToOperationTileProps
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{HexToRgba, I18n}
import org.make.front.helpers.NumberFormat
import org.make.front.models.{
  GradientColor     => GradientColorModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel
}
import org.make.front.styles.OperationStyles
import org.make.front.styles.base._
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.proposal.SearchResult
import org.make.services.tracking.TrackingLocation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object OperationShowcase {

  final case class OperationShowcaseProps(proposals: () => Future[SearchResult],
                                          country: String,
                                          language: String,
                                          operation: OperationModel)

  final case class OperationShowcaseState(proposals: js.Array[ProposalModel])

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[OperationShowcaseProps, OperationShowcaseState](
        displayName = "OperationShowcase",
        getInitialState = { _ =>
          OperationShowcaseState(js.Array())
        },
        componentWillReceiveProps = { (self, props) =>
          props.wrapped.proposals().onComplete {
            case Failure(_)       =>
            case Success(results) => self.setState(_.copy(proposals = results.results))
          }
        },
        render = { self =>
          val gradientValues: GradientColorModel =
            self.props.wrapped.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          // needed to get a different class name by theme
          val index = Counter.showcaseCounter.getAndIncrement()

          object DynamicOperationShowcaseStyles extends StyleSheet.Inline {

            import dsl._

            val gradient: Int => StyleA =
              styleF.int(Range(index, index + 1)) { _ =>
                styleS(
                  background := s"linear-gradient(130deg, ${HexToRgba(gradientValues.from, 0.1F)}, ${HexToRgba(gradientValues.to, 0.1F)})"
                )
              }
          }

          def proposalTile(self: Self[OperationShowcaseProps, OperationShowcaseState],
                           proposalModel: ProposalModel): ReactElement =
            <.ProposalTileComponent(
              ^.wrapped :=
                ProposalTileProps(
                  proposal = proposalModel,
                  index = Counter.showcaseCounter.getAndIncrement(),
                  maybeOperation = Some(self.props.wrapped.operation),
                  maybeSequenceId = None,
                  maybeLocation = None,
                  maybeTheme = None,
                  trackingLocation = TrackingLocation.showcaseHomepage,
                  country = self.props.wrapped.country,
                  maybePostedIn = PostedIn.fromProposal(proposalModel, js.Array(self.props.wrapped.operation))
                )
            )()

          def sliderContent(operation: OperationModel) =
            js.Array(
                self.state.proposals
                  .map(
                    proposal =>
                      <.div(
                        ^.className :=
                          js.Array(ColRulesStyles.col, OperationShowcaseStyles.propasalItem)
                      )(proposalTile(self, proposal))
                  )
                  .toSeq,
                <.div(
                  ^.className :=
                    js.Array(ColRulesStyles.col, OperationShowcaseStyles.propasalItem)
                )(
                  <.PromptingToProposeInRelationToOperationTileComponent(
                    ^.wrapped :=
                      PromptingToProposeInRelationToOperationTileProps(
                        operation = operation,
                        language = self.props.wrapped.language
                      )
                  )()
                )
              )
              .toSeq

          <.section(
            ^.className := js.Array(OperationShowcaseStyles.wrapper, DynamicOperationShowcaseStyles.gradient(index))
          )(
            js.Array(
                <.header(^.className := LayoutRulesStyles.centeredRow)(
                  <.div(^.className := OperationShowcaseStyles.operationNameWrapper)(
                    <.h2(^.className := js.Array(OperationShowcaseStyles.operationName, TextStyles.bigTitle))(
                      <.Link(
                        ^.to := s"/${self.props.wrapped.country}/consultation/${self.props.wrapped.operation.slug}"
                      )(
                        self.props.wrapped.operation.wordings
                          .find(_.language == self.props.wrapped.language)
                          .map(_.title)
                          .getOrElse("-")
                      )
                    )
                  ),
                  <.div(^.className := OperationShowcaseStyles.operationInfo)(
                    <.ul(^.className := js.Array(TableLayoutBeyondMediumStyles.wrapper, TextStyles.title))(
                      if (self.props.wrapped.operation.proposalsCount > 0) {
                        <.li(
                          ^.className := js
                            .Array(OperationShowcaseStyles.operationData, TableLayoutBeyondMediumStyles.cell)
                        )(
                          <.em(^.className := OperationShowcaseStyles.operationDataValue)(
                            NumberFormat.formatToKilo(self.props.wrapped.operation.proposalsCount)
                          ),
                          unescape(I18n.t("operation-showcase.data-units.proposals"))
                        )
                      },
                      if (self.props.wrapped.operation.actionsCount > 0) {
                        <.li(
                          ^.className := js
                            .Array(OperationShowcaseStyles.operationData, TableLayoutBeyondMediumStyles.cell)
                        )(
                          <.em(^.className := OperationShowcaseStyles.operationDataValue)(
                            NumberFormat.formatToKilo(self.props.wrapped.operation.actionsCount)
                          ),
                          unescape(I18n.t("operation-showcase.data-units.actions"))
                        )
                      }
                    )
                  )
                ),
                <.div(
                  ^.className := js.Array(
                    RWDRulesMediumStyles.hideBeyondMedium,
                    LayoutRulesStyles.centeredRowWithCols,
                    OperationShowcaseStyles.slideshow
                  )
                )(<.Slider(^.infinite := false, ^.arrows := false)(sliderContent(self.props.wrapped.operation))),
                <.div(
                  ^.className := js.Array(
                    RWDRulesMediumStyles.showBlockBeyondMedium,
                    RWDHideRulesStyles.hideBeyondLarge,
                    LayoutRulesStyles.centeredRowWithCols,
                    OperationShowcaseStyles.slideshow
                  )
                )(
                  <.Slider(^.infinite := false, ^.arrows := false, ^.slidesToShow := 2, ^.slidesToScroll := 2)(
                    sliderContent(self.props.wrapped.operation)
                  )
                ),
                <.div(^.className := RWDHideRulesStyles.showBlockBeyondLarge)(
                  <.ul(
                    ^.className := js
                      .Array(LayoutRulesStyles.centeredRowWithCols, OperationShowcaseStyles.propasalsList)
                  )(
                    self.state.proposals
                      .map(
                        proposal =>
                          <.li(
                            ^.className := js.Array(
                              OperationShowcaseStyles.propasalItem,
                              ColRulesStyles.col,
                              ColRulesStyles.colQuarterBeyondLarge
                            )
                          )(proposalTile(self, proposal))
                      )
                      .toSeq,
                    <.li(
                      ^.className := js.Array(
                        OperationShowcaseStyles.propasalItem,
                        ColRulesStyles.col,
                        ColRulesStyles.colHalfBeyondMedium,
                        ColRulesStyles.colQuarterBeyondLarge
                      )
                    )(
                      <.PromptingToProposeInRelationToOperationTileComponent(
                        ^.wrapped :=
                          PromptingToProposeInRelationToOperationTileProps(
                            operation = self.props.wrapped.operation,
                            language = self.props.wrapped.language
                          )
                      )()
                    )
                  )
                ),
                <.p(^.className := js.Array(LayoutRulesStyles.centeredRow, OperationShowcaseStyles.seeMoreLinkWrapper))(
                  <.Link(
                    ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA),
                    ^.to := s"/${self.props.wrapped.country}/consultation/${self.props.wrapped.operation.slug}"
                  )(I18n.t("operation-showcase.see-all"))
                ),
                <.style()(OperationShowcaseStyles.render[String], DynamicOperationShowcaseStyles.render[String])
              )
              .toSeq
          )
        }
      )
    )
}

object OperationShowcaseStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      padding(OperationStyles.SpacingValue.medium.pxToEm(), `0`),
      backgroundColor(OperationStyles.BackgroundColor.blackVeryTransparent),
      OperationStyles.MediaQueries.beyondSmall(
        padding(
          OperationStyles.SpacingValue.larger.pxToEm(),
          `0`,
          (OperationStyles.SpacingValue.larger - OperationStyles.SpacingValue.small).pxToEm()
        )
      ),
      overflow.hidden
    )

  val intro: StyleA = style(
    marginBottom(OperationStyles.SpacingValue.smaller.pxToEm(15)),
    OperationStyles.MediaQueries.beyondSmall(marginBottom(OperationStyles.SpacingValue.smaller.pxToEm(18)))
  )

  val operationNameWrapper: StyleA = style(
    OperationStyles.MediaQueries
      .beyondMedium(display.inlineBlock, verticalAlign.top, marginRight(OperationStyles.SpacingValue.medium.pxToEm()))
  )

  val operationName: StyleA = style(unsafeChild("a")(color(OperationStyles.TextColor.base)))

  val operationInfo: StyleA = style(display.inlineBlock, OperationStyles.MediaQueries.beyondMedium(verticalAlign.top))

  val news: StyleA =
    style(
      padding(2.pxToEm(), (OperationStyles.SpacingValue.small / 2).pxToEm()),
      lineHeight(15.pxToEm()),
      color(OperationStyles.TextColor.white),
      backgroundColor(OperationStyles.BackgroundColor.black),
      unsafeChild("*")(lineHeight.inherit)
    )
  val operationData: StyleA =
    style(
      paddingRight(OperationStyles.SpacingValue.medium.pxToEm()),
      &.lastChild(paddingRight(`0`)),
      fontSize(14.pxToEm()),
      color(OperationStyles.TextColor.lighter),
      OperationStyles.MediaQueries
        .belowMedium(display.inlineBlock, marginRight(OperationStyles.SpacingValue.small.pxToEm()))
    )

  val operationDataValue: StyleA =
    style(
      &.after(content := "' '"),
      OperationStyles.MediaQueries
        .beyondMedium(display.inlineBlock, width(100.%%), &.after(content := none), fontSize(24.pxToEm(14)))
    )

  val filter: StyleA =
    style(OperationStyles.MediaQueries.belowMedium(marginTop(OperationStyles.SpacingValue.smaller.pxToEm())))

  val filterIntro: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.baseline,
      paddingRight(OperationStyles.SpacingValue.smaller.pxToEm()),
      color(OperationStyles.TextColor.lighter)
    )

  val filterCTA: StyleA =
    style(
      position.relative,
      display.inlineBlock,
      verticalAlign.baseline,
      paddingRight((OperationStyles.SpacingValue.smaller * 2).pxToEm()),
      &.firstChild(paddingLeft(`0`)),
      &.after(
        content := "' '",
        position.absolute,
        top(`0`),
        right(OperationStyles.SpacingValue.smaller.pxToEm()),
        height(20.pxToEm()),
        width(1.px),
        backgroundColor(OperationStyles.BorderColor.lighter)
      ),
      &.lastChild(&.after(content := none)),
      color.inherit,
      transition := "color .2s ease-in-out",
      &.hover(color(OperationStyles.OperationColor.primary))
    )

  val slideshow: StyleA =
    style(
      width(95.%%),
      unsafeChild(".slick-list")(overflow.visible),
      unsafeChild(".slick-slide")(height.auto, minHeight.inherit),
      unsafeChild(".slick-track")(display.flex)
    )

  val propasalsList: StyleA =
    style(display.flex, flexWrap.wrap, width(100.%%))

  val propasalItem: StyleA =
    style(
      marginTop(OperationStyles.SpacingValue.small.pxToEm()),
      OperationStyles.MediaQueries.beyondSmall(marginBottom(OperationStyles.SpacingValue.small.pxToEm()))
    )

  val seeMoreLinkWrapper: StyleA = style(
    marginTop(OperationStyles.SpacingValue.small.pxToEm()),
    marginBottom(OperationStyles.SpacingValue.small.pxToEm()),
    textAlign.center
  )
}
