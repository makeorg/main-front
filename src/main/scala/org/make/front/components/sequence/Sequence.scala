package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.Proposal.ProposalProps
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements, Slider}
import org.make.front.models.{Proposal => ProposalModel, Sequence => SequenceModel}
import org.make.front.styles.ThemeStyles
import org.scalajs.dom.raw.HTMLElement
import org.make.front.styles.utils._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet

object Sequence {

  final case class SequenceProps(sequence: SequenceModel, proposals: Future[Seq[ProposalModel]])

  final case class SequenceState(proposals: Seq[ProposalModel])

  lazy val reactClass: ReactClass =
    React.createClass[SequenceProps, SequenceState](
      displayName = "Sequence",
      getInitialState = { self =>
        SequenceState(proposals = Seq.empty)
      },
      render = { self =>
        self.props.wrapped.proposals.onComplete {
          case Success(proposals) => self.setState(_.copy(proposals = proposals))
          case Failure(_)         =>
        }
        var slider: Option[Slider] = None
        val size = 2//self.state.proposals.size
        <.div(^.className := SequenceStyles.wrapper)(<.Slider(^.ref := ((s: HTMLElement) => {
          slider = Option(s.asInstanceOf[Slider])
          slider.foreach(_.slickGoTo(0))
          slider
        }), ^.infinite := false, ^.arrows := false)(

          /*self.state.proposals.map { proposal: ProposalModel =>
          <.div(^.className := Seq(SequenceStyles.slideWrapper))(
            <.div(^.className := Seq(SequenceStyles.slide))(
              <.ProposalComponent(
                ^.wrapped :=
                  ProposalProps(proposal = proposal)
              )()
            )
          )
        }*/
          <.p()("1"),
            <.p()("2")

        ), <.style()(SequenceStyles.render[String]))
      }
    )
}

object SequenceStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style()

  val slideWrapper: StyleA =
    style(
      ThemeStyles.MediaQueries
        .belowMedium(padding :=! s"0 ${ThemeStyles.SpacingValue.small.pxToEm().value} 0 0"),
      ThemeStyles.MediaQueries
        .belowSmall(padding :=! s"0 ${ThemeStyles.SpacingValue.smaller.pxToEm().value} 0 0")
    )

  val slide: StyleA =
    style(
      ThemeStyles.MediaQueries
        .belowMedium(
          height(100.%%),
          backgroundColor(ThemeStyles.BackgroundColor.white),
          boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
        )
    )
}
