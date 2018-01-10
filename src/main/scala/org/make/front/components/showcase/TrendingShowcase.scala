package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.core.Counter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalTile.ProposalTileProps
import org.make.front.components.proposal.ProposalTileWithThemeContainer.ProposalTileWithThemeContainerProps
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements}
import org.make.front.facades.logoMake
import org.make.front.models.{
  Proposal          => ProposalModel,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Sequence          => SequenceModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, RWDHideRulesStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.services.proposal.SearchResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object TrendingShowcase {

  final case class TrendingShowcaseProps(proposals: Future[SearchResult],
                                         intro: String,
                                         title: String,
                                         maybeTheme: Option[TranslatedThemeModel],
                                         maybeOperation: Option[OperationModel],
                                         maybeSequence: Option[SequenceModel],
                                         maybeLocation: Option[LocationModel])

  final case class TrendingShowcaseState(proposals: Seq[ProposalModel])

  lazy val reactClass: ReactClass =
    React.createClass[TrendingShowcaseProps, TrendingShowcaseState](
      displayName = "TrendingShowcase",
      getInitialState = { _ =>
        TrendingShowcaseState(Seq.empty)
      },
      render = { self =>
        self.props.wrapped.proposals.onComplete {
          case Failure(_)       =>
          case Success(results) => self.setState(_.copy(proposals = results.results))
        }

        val counter = Counter.showcaseCounter

        def proposalTile(self: Self[TrendingShowcaseProps, TrendingShowcaseState], proposal: ProposalModel) =
          if (proposal.themeId.isDefined) {
            <.ProposalTileWithThemeContainerComponent(
              ^.wrapped :=
                ProposalTileWithThemeContainerProps(
                  proposal = proposal,
                  index = counter.getAndIncrement(),
                  maybeOperation = self.props.wrapped.maybeOperation,
                  maybeSequence = self.props.wrapped.maybeSequence,
                  maybeLocation = self.props.wrapped.maybeLocation
                )
            )()
          } else {
            <.ProposalTileComponent(
              ^.wrapped :=
                ProposalTileProps(
                  proposal = proposal,
                  index = counter.getAndIncrement(),
                  maybeTheme = self.props.wrapped.maybeTheme,
                  maybeOperation = self.props.wrapped.maybeOperation,
                  maybeSequence = self.props.wrapped.maybeSequence,
                  maybeLocation = self.props.wrapped.maybeLocation
                )
            )()
          }

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
                  LayoutRulesStyles.centeredRow,
                  ThemeShowcaseStyles.slideshow
                )
              )(
                <.Slider(^.infinite := false, ^.arrows := false)(
                  self.state.proposals.map(
                    proposal =>
                      <.div(
                        ^.className :=
                          ThemeShowcaseStyles.propasalItem
                      )(proposalTile(self, proposal))
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
                      )(proposalTile(self, proposal))
                  )
                )
              )
            ),
            <.style()(TrendingShowcaseStyles.render[String])
          )

        } else <.div.empty
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
