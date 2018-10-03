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
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.core.Counter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalTile.{PostedIn, ProposalTileProps}
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements}
import org.make.front.facades.logoMake
import org.make.front.helpers.RouteHelper
import org.make.front.models.{
  Operation,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel
}
import org.make.front.styles._
import org.make.front.styles.base._
import org.make.front.styles.utils._
import org.make.services.proposal.SearchResult
import org.make.services.tracking.TrackingLocation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object TrendingShowcase {

  final case class TrendingShowcaseProps(proposals: ()  => Future[SearchResult],
                                         operations: () => Future[js.Array[Operation]],
                                         intro: String,
                                         title: String,
                                         maybeLocation: Option[LocationModel],
                                         country: String)

  final case class TrendingShowcaseState(proposals: js.Array[ProposalModel], operations: js.Array[Operation])

  def proposalTile(proposal: ProposalModel,
                   operation: OperationModel,
                   maybeLocation: Option[LocationModel],
                   counter: Counter,
                   country: String): ReactElement =
    <.ProposalTileComponent(
      ^.wrapped :=
        ProposalTileProps(
          proposal = proposal,
          index = counter.getAndIncrement(),
          maybeTheme = None,
          maybeOperation = None,
          maybeSequenceId = None,
          maybeLocation = maybeLocation,
          trackingLocation = TrackingLocation.showcaseHomepage,
          country = country,
          maybePostedIn = proposal.operationId.map { operationId =>
            PostedIn(
              name = operation.wordings.find(_.language == proposal.language).map(_.title).getOrElse(operation.label),
              link = RouteHelper.operationRoute(proposal.country, operation.slug)
            )

          }
        )
    )()

  lazy val reactClass: ReactClass =
    React.createClass[TrendingShowcaseProps, TrendingShowcaseState](
      displayName = "TrendingShowcase",
      getInitialState = { _ =>
        TrendingShowcaseState(js.Array(), js.Array())
      },
      componentWillReceiveProps = { (self, props) =>
        props.wrapped.proposals().onComplete {
          case Failure(_)       =>
          case Success(results) => self.setState(_.copy(proposals = results.results))
        }
        props.wrapped.operations().onComplete {
          case Failure(_)       =>
          case Success(results) => self.setState(_.copy(operations = results))
        }
      },
      render = { self =>
        val counter = Counter.showcaseCounter
        val maybeLocation = self.props.wrapped.maybeLocation

        if (self.state.proposals.nonEmpty) {
          <.section(^.className := TrendingShowcaseStyles.wrapper)(
            js.Array(
                <.header(^.className := LayoutRulesStyles.centeredRow)(
                  <.p(^.className := js.Array(TrendingShowcaseStyles.intro, TextStyles.mediumText, TextStyles.intro))(
                    self.props.wrapped.intro
                  ),
                  <.h2(^.className := TextStyles.mediumTitle)(
                    self.props.wrapped.title,
                    <.span(^.style := Map("display" -> "none"))("Make.org"),
                    <.img(
                      ^.className := TrendingShowcaseStyles.logo,
                      ^.src := logoMake.toString,
                      ^.alt := "Make.org",
                      ^("data-pin-no-hover") := "true"
                    )()
                  )
                ),
                <.div(
                  ^.className := js.Array(
                    RWDRulesMediumStyles.hideBeyondMedium,
                    LayoutRulesStyles.centeredRowWithCols,
                    TrendingShowcaseStyles.slideshow
                  )
                )(<.Slider(^.infinite := false, ^.arrows := false)(self.state.proposals.flatMap {
                  proposal =>
                    self.state.operations
                      .find(_.operationId.value == proposal.operationId.map(_.value).getOrElse(""))
                      .flatMap { operation =>
                        OperationModel
                          .getOperationExpandedFromOperation(Some(operation), js.Array(), proposal.country)
                          .map { op =>
                            <.div(
                              ^.className :=
                                js.Array(ColRulesStyles.col, TrendingShowcaseStyles.propasalItem)
                            )(proposalTile(proposal, op, maybeLocation, counter, self.props.wrapped.country))
                          }
                      }
                }.toSeq)),
                <.div(^.className := RWDRulesMediumStyles.showBlockBeyondMedium)(
                  <.ul(
                    ^.className := js.Array(TrendingShowcaseStyles.propasalsList, LayoutRulesStyles.centeredRowWithCols)
                  )(self.state.proposals.map {
                    proposal =>
                      self.state.operations
                        .find(_.operationId.value == proposal.operationId.map(_.value).getOrElse(""))
                        .flatMap { operation =>
                          OperationModel
                            .getOperationExpandedFromOperation(Some(operation), js.Array(), proposal.country)
                            .map { op =>
                              <.li(
                                ^.className := js
                                  .Array(
                                    TrendingShowcaseStyles.propasalItem,
                                    ColRulesStyles.col,
                                    ColRulesStyles.colHalfBeyondMedium
                                  )
                              )(proposalTile(proposal, op, maybeLocation, counter, self.props.wrapped.country))
                            }
                        }
                  }.toSeq)
                )
              )
              .toSeq,
            <.style()(TrendingShowcaseStyles.render[String])
          )

        } else {
          <.div.empty
        }
      }
    )
}

object TrendingShowcaseStyles extends StyleSheet.Inline {
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

  val slideshow: StyleA =
    style(
      width(95.%%),
      unsafeChild(".slick-list")(overflow.visible),
      unsafeChild(".slick-slide")(height.auto, minHeight.inherit),
      unsafeChild(".slick-track")(display.flex)
    )

  val propasalsList: StyleA =
    style(display.flex, flexWrap.wrap)

  val propasalItem: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val logo: StyleA =
    style(
      width.auto,
      verticalAlign.baseline,
      height(14.pxToEm(20)),
      marginLeft(5.pxToEm(20)),
      ThemeStyles.MediaQueries.beyondSmall(height(25.pxToEm(34)), marginLeft(10.pxToEm(34)))
    )
}
