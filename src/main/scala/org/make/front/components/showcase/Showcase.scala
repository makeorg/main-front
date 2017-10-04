package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.Proposal.ProposalProps
import org.make.front.components.proposal.ProposalWithThemeContainer.ProposalWithThemeContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.services.proposal.ProposalResponses.SearchResponse

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults._

import scala.concurrent.ExecutionContext.Implicits.global

object Showcase {

  final case class ShowcaseProps(proposals: Future[SearchResponse], introTranslationKey: String, title: String)

  final case class ShowcaseState(proposals: Seq[ProposalModel])

  lazy val reactClass: ReactClass =
    React.createClass[ShowcaseProps, ShowcaseState](displayName = "Showcase", getInitialState = { _ =>
      ShowcaseState(Seq.empty)
    }, render = {
      self =>
        self.props.wrapped.proposals.onComplete {
          case Failure(_)       =>
          case Success(results) => self.setState(_.copy(proposals = results.results))
        }

        <.section(^.className := ShowcaseStyles.wrapper)(if (self.state.proposals.nonEmpty) {
          Seq(
            <.div(^.className := RowRulesStyles.centeredRow)(
              <.header(^.className := ColRulesStyles.col)(
                <.p(^.className := Seq(ShowcaseStyles.intro, TextStyles.mediumText, TextStyles.intro))(
                  unescape(I18n.t(self.props.wrapped.introTranslationKey))
                ),
                <.h2(^.className := Seq(ShowcaseStyles.title, TextStyles.bigTitle))(self.props.wrapped.title)
              ),
              <.ul()(
                self.state.proposals.map(
                  proposal =>
                    <.li(
                      ^.className := Seq(
                        ShowcaseStyles.propasalItem,
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
            <.style()(ShowcaseStyles.render[String])
          )
        })
    })

}

object ShowcaseStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      padding :=! s"${ThemeStyles.SpacingValue.medium.pxToEm().value} 0"
    )

  val title: StyleA = style()
  val intro: StyleA = style(
    marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(15)),
    ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm(18)))
  )

  val propasalItem: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

}
