package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.core.Counter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalTile.ProposalTileProps
import org.make.front.components.proposal.ProposalTileWithThemeContainer.ProposalTileWithThemeContainerProps
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements}
import org.make.front.facades.logoMake
import org.make.front.models.{Location => LocationModel, Proposal => ProposalModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, RWDHideRulesStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.services.proposal.SearchResult
import org.make.services.tracking.TrackingLocation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object TrendingShowcase {

  final case class TrendingShowcaseProps(proposals: () => Future[SearchResult],
                                         intro: String,
                                         title: String,
                                         maybeLocation: Option[LocationModel])

  final case class TrendingShowcaseState(proposals: Seq[ProposalModel])

  def proposalTile(proposal: ProposalModel, maybeLocation: Option[LocationModel], counter: Counter): ReactElement = {
    if (proposal.themeId.isDefined) {
      <.ProposalTileWithThemeContainerComponent(
        ^.wrapped :=
          ProposalTileWithThemeContainerProps(
            proposal = proposal,
            index = counter.getAndIncrement(),
            maybeOperation = None,
            maybeSequenceId = None,
            maybeLocation = maybeLocation,
            trackingLocation = TrackingLocation.showcaseHomepage
          )
      )()
    } else {
      <.ProposalTileComponent(
        ^.wrapped :=
          ProposalTileProps(
            proposal = proposal,
            index = counter.getAndIncrement(),
            maybeTheme = None,
            maybeOperation = None,
            maybeSequenceId = None,
            maybeLocation = maybeLocation,
            trackingLocation = TrackingLocation.showcaseHomepage
          )
      )()
    }
  }

  lazy val reactClass: ReactClass =
    React.createClass[TrendingShowcaseProps, TrendingShowcaseState](
      displayName = "TrendingShowcase",
      getInitialState = { _ =>
        TrendingShowcaseState(Seq.empty)
      },
      componentWillReceiveProps = { (self, props) =>
        props.wrapped.proposals().onComplete {
          case Failure(_)       =>
          case Success(results) => self.setState(_.copy(proposals = results.results))
        }
      },
      render = { self =>
        val counter = Counter.showcaseCounter
        val maybeLocation = self.props.wrapped.maybeLocation

        if (self.state.proposals.nonEmpty) {
          <.section(^.className := TrendingShowcaseStyles.wrapper)(
            Seq(
              <.header(^.className := LayoutRulesStyles.centeredRow)(
                <.p(^.className := Seq(TrendingShowcaseStyles.intro, TextStyles.mediumText, TextStyles.intro))(
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
                ^.className := Seq(
                  RWDHideRulesStyles.hideBeyondMedium,
                  LayoutRulesStyles.centeredRowWithCols,
                  TrendingShowcaseStyles.slideshow
                )
              )(
                <.Slider(^.infinite := false, ^.arrows := false)(
                  self.state.proposals.map(
                    proposal =>
                      <.div(
                        ^.className :=
                          Seq(ColRulesStyles.col, TrendingShowcaseStyles.propasalItem)
                      )(proposalTile(proposal, maybeLocation, counter))
                  )
                )
              ),
              <.div(^.className := RWDHideRulesStyles.showBlockBeyondMedium)(
                <.ul(^.className := Seq(TrendingShowcaseStyles.propasalsList, LayoutRulesStyles.centeredRowWithCols))(
                  self.state.proposals.map(
                    proposal =>
                      <.li(
                        ^.className := Seq(
                          TrendingShowcaseStyles.propasalItem,
                          ColRulesStyles.col,
                          ColRulesStyles.colHalfBeyondMedium
                        )
                      )(proposalTile(proposal, maybeLocation, counter))
                  )
                )
              )
            ),
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
