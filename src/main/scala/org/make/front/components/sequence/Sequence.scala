package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.components.sequence.ProgressBar.ProgressBarProps
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements, Slider}
import org.make.front.helpers.ProposalAuthorInfosFormat
import org.make.front.models.{Proposal => ProposalModel, Sequence => SequenceModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.scalajs.dom.raw.HTMLElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet
import org.make.front.styles.utils._

object Sequence {

  final case class SequenceProps(sequence: SequenceModel, proposals: Future[Seq[ProposalModel]])

  final case class SequenceState(proposals: Seq[ProposalModel], currentSlideIndex: Int)

  lazy val reactClass: ReactClass =
    React.createClass[SequenceProps, SequenceState](
      displayName = "Sequence",
      getInitialState = { self =>
        SequenceState(proposals = Seq.empty, currentSlideIndex = 0)
      },
      render = { self =>
        self.props.wrapped.proposals.onComplete {
          case Success(proposals) => self.setState(_.copy(proposals = proposals))
          case Failure(_)         =>
        }

        var slider: Option[Slider] = None

        def updateCurrentSlideIndex(currentSlide: Int): Unit = {
          scalajs.js.Dynamic.global.console.log(currentSlide.toString)
          self.setState(state => state.copy(currentSlideIndex = currentSlide))
        }

        def proposalContent(proposal: ProposalModel): Seq[ReactElement] = Seq(
          <.header(^.className := ProposalInSlideStyles.infosWrapper)(
            <.h4(^.className := Seq(TextStyles.mediumText, ProposalInSlideStyles.infos))(
              ProposalAuthorInfosFormat.apply(proposal)
            )
          ),
          <.div(^.className := ProposalInSlideStyles.contentWrapper)(
            <.h3(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(proposal.content),
            <.div(^.className := ProposalInSlideStyles.voteWrapper)(
              <.VoteContainerComponent(^.wrapped := VoteContainerProps(proposal = proposal))()
            )
          ),
          <.style()(ProposalInSlideStyles.render[String])
        )

        <.div(^.className := Seq(SequenceStyles.wrapper))(
          <.div(^.className := SequenceStyles.progressBarWrapper)(
            <.div(^.className := SequenceStyles.progressBarInnerWrapper)(
              <.div(^.className := Seq(RowRulesStyles.centeredRow))(
                <.div(^.className := Seq(ColRulesStyles.col))(
                  <.ProgressBarComponent(
                    ^.wrapped := ProgressBarProps(
                      value = self.state.currentSlideIndex,
                      total = self.state.proposals.size,
                      color = "#FF0"
                    )
                  )()
                )
              )
            )
          ),
          <.div(^.className := SequenceStyles.slideshowWrapper)(
            <.div(^.className := SequenceStyles.slideshowInnerWrapper)(
              <.div(^.className := Seq(RowRulesStyles.centeredRow))(
                <.div(^.className := Seq(ColRulesStyles.col))(
                  <.div(^.className := Seq(SequenceStyles.slideshow))(<.Slider(^.ref := ((s: HTMLElement) => {
                    slider = Option(s.asInstanceOf[Slider])
                    slider.foreach(_.slickGoTo(0))
                    slider
                  }), ^.infinite := false, ^.arrows := false, ^.afterChange := updateCurrentSlideIndex)(self.state.proposals.map {
                    proposal: ProposalModel =>
                      <.div(^.className := Seq(SequenceStyles.slideWrapper))(
                        <.article(^.className := Seq(SequenceStyles.slide))(proposalContent(proposal))
                      )
                  }))
                )
              )
            )
          ),
          <.style()(SequenceStyles.render[String])
        )
      }
    )
}

object SequenceStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(display.table, tableLayout.fixed, width(100.%%), height(100.%%))

  val progressBarWrapper: StyleA =
    style(display.tableRow)

  val progressBarInnerWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.bottom,
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.small.pxToEm())
    )

  val slideshowWrapper: StyleA =
    style(display.tableRow, height(100.%%))

  val slideshowInnerWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.top,
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      overflow.hidden
    )

  val slideshow: StyleA =
    style(
      unsafeChild(".slick-slider")(height(100.%%)),
      unsafeChild(".slick-list")(overflow.visible, height(100.%%)),
      unsafeChild(".slick-track")(height(100.%%)),
      unsafeChild(".slick-slide")(height(100.%%), transition := "transform .2s ease-in-out", transform := "scale(0.9)"),
      unsafeChild(".slick-slide.slick-active")(transform := "scale(1)")
    )

  val slideWrapper: StyleA =
    style()

  val slide: StyleA =
    style(
      minHeight(100.%%),
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )
}

object ProposalInSlideStyles extends StyleSheet.Inline {
  import dsl._

  val infosWrapper: StyleA =
    style(
      textAlign.center,
      position.relative,
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      (&.after)(
        content := "''",
        position.absolute,
        top(100.%%),
        left(50.%%),
        transform := s"translateX(-50%)",
        marginTop(-0.5.px),
        height(1.px),
        width(90.pxToEm()),
        backgroundColor(ThemeStyles.BorderColor.lighter)
      )
    )

  val infos: StyleA =
    style(color(ThemeStyles.TextColor.light))

  val contentWrapper: StyleA =
    style(textAlign.center, marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val voteWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))
}
