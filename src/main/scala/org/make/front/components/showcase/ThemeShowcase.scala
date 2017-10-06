package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.Proposal.ProposalProps
import org.make.front.components.proposal.ProposalWithThemeContainer.ProposalWithThemeContainerProps
import org.make.front.facades.HexToRgba
import org.make.front.models.{GradientColor => GradientColorModel, Proposal => ProposalModel, Theme => ThemeModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.proposal.ProposalResponses.SearchResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet.Inline

object ThemeShowcase {

  final case class ThemeShowcaseProps(proposals: Future[SearchResponse], maybeTheme: Option[ThemeModel])

  final case class ThemeShowcaseState(proposals: Seq[ProposalModel])

  lazy val reactClass: ReactClass =
    React.createClass[ThemeShowcaseProps, ThemeShowcaseState](displayName = "Showcase", getInitialState = { _ =>
      ThemeShowcaseState(Seq.empty)
    }, render = {
      self =>
        self.props.wrapped.proposals.onComplete {
          case Failure(_)       =>
          case Success(results) => self.setState(_.copy(proposals = results.results))
        }

        val gradientValues: GradientColorModel =
          self.props.wrapped.maybeTheme.flatMap(_.gradient).getOrElse(GradientColorModel("#FFF", "#FFF"))
        val index = self.props.wrapped.maybeTheme.map(_.order).getOrElse(9999)

        object DynamicThemeShowcaseStyles extends Inline {
          import dsl._

          val gradient: (Int) => StyleA =
            styleF.int(Range(index, index + 1)) { index =>
              styleS(
                background := s"linear-gradient(130deg, ${HexToRgba(gradientValues.from, 0.1F)}, ${HexToRgba(gradientValues.to, 0.1F)})"
              )
            }
        }

        <.section(^.className := Seq(ThemeShowcaseStyles.wrapper, DynamicThemeShowcaseStyles.gradient(index)))(
          if (self.state.proposals.nonEmpty) {
            Seq(
              <.div(^.className := RowRulesStyles.centeredRow)(
                <.header(^.className := ColRulesStyles.col)(
                  <.h2(^.className := Seq(ThemeShowcaseStyles.title, TextStyles.bigTitle))(
                    self.props.wrapped.maybeTheme.map(_.title).getOrElse("")
                  )
                ),
                <.ul(^.className := ThemeShowcaseStyles.propasalsList)(
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
                        <.ProposalComponent(
                          ^.wrapped :=
                            ProposalProps(proposal = proposal)
                        )()
                    )
                  )
                ),
                if (self.props.wrapped.maybeTheme.nonEmpty) {
                  <.p(^.className := Seq(ColRulesStyles.col, ThemeShowcaseStyles.SeeMoreLinkWrapper))(
                    <.Link(
                      ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA),
                      ^.to := s"/theme/${self.props.wrapped.maybeTheme.map(_.slug).getOrElse("")}"
                    )(
                      "Voir toutes les propositions du thème " + self.props.wrapped.maybeTheme
                        .map(_.title)
                        .getOrElse("")
                    )
                  )
                }
              ),
              <.style()(ThemeShowcaseStyles.render[String], DynamicThemeShowcaseStyles.render[String])
            )
          }
        )
    })
}

object ThemeShowcaseStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0",
      borderBottom :=! s"1px solid ${ThemeStyles.BorderColor.white.value}",
      ThemeStyles.MediaQueries.beyondSmall(
        padding :=! s"${ThemeStyles.SpacingValue.larger.pxToEm().value} 0 ${(ThemeStyles.SpacingValue.larger - ThemeStyles.SpacingValue.small).pxToEm().value}"
      )
    )

  val title: StyleA = style()

  val propasalsList: StyleA =
    style(display.flex, flexWrap.wrap)

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