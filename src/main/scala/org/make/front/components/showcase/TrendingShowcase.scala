package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.Proposal.ProposalProps
import org.make.front.components.proposal.ProposalWithThemeContainer.ProposalWithThemeContainerProps
import org.make.front.facades.logoMake
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.services.proposal.ProposalResponses.SearchResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults._

object TrendingShowcase {

  final case class TrendingShowcaseProps(proposals: Future[SearchResponse], intro: String, title: String)

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

        <.section(^.className := TrendingShowcaseStyles.wrapper)(if (self.state.proposals.nonEmpty) {
          Seq(
            <.div(^.className := RowRulesStyles.centeredRow)(
              <.header(^.className := ColRulesStyles.col)(
                <.p(^.className := Seq(TrendingShowcaseStyles.intro, TextStyles.mediumText, TextStyles.intro))(
                  self.props.wrapped.intro
                ),
                <.h2(^.className := Seq(TrendingShowcaseStyles.title, TextStyles.mediumTitle))(
                  self.props.wrapped.title,
                  <.img(
                    ^.className := TrendingShowcaseStyles.logo,
                    ^.src := logoMake.toString,
                    ^("data-pin-no-hover") := "true"
                  )()
                )
              ),
              <.ul(^.className := TrendingShowcaseStyles.propasalsList)(
                self.state.proposals.map(
                  proposal =>
                    <.li(
                      ^.className := Seq(
                        TrendingShowcaseStyles.propasalItem,
                        ColRulesStyles.col,
                        ColRulesStyles.colHalfBeyondMedium
                      )
                    )(if (proposal.themeId.getOrElse("") != "") {
                      <.ProposalWithThemeContainerComponent(
                        ^.wrapped :=
                          ProposalWithThemeContainerProps(proposal = proposal)
                      )()
                    } else {
                      <.ProposalComponent(
                        ^.wrapped :=
                          ProposalProps(proposal = proposal)
                      )()
                    })
                )
              )
            ),
            <.style()(TrendingShowcaseStyles.render[String])
          )
        })
      }
    )
}

object TrendingShowcaseStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      padding :=! s"${ThemeStyles.SpacingValue.large.pxToEm().value} 0}",
      ThemeStyles.MediaQueries.beyondSmall(
        padding :=! s"${ThemeStyles.SpacingValue.large.pxToEm().value} 0 ${(ThemeStyles.SpacingValue.large - ThemeStyles.SpacingValue.small).pxToEm().value}"
      )
    )

  val title: StyleA = style()
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
