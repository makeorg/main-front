package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.components.operation.IntroOfOperationSequence.IntroOfOperationSequenceProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.components.sequence.ProgressBar.ProgressBarProps
import org.make.front.facades.FacebookPixel
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements, Slider}
import org.make.front.facades.Unescape.unescape
import org.make.front.helpers.ProposalAuthorInfosFormat
import org.make.front.models.{ProposalId, Proposal => ProposalModel, Sequence => SequenceModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet
import scala.scalajs.js.JSConverters._

object Sequence {

  final case class SequenceProps(sequence: SequenceModel,
                                 maybeThemeColor: Option[String],
                                 proposals: Future[Seq[ProposalModel]],
                                 intro: ReactClass,
                                 conclusion: ReactClass)

  final case class SequenceState(proposals: Seq[ProposalModel], currentSlideIndex: Int, votes: Seq[String])

  lazy val reactClass: ReactClass =
    React.createClass[SequenceProps, SequenceState](
      displayName = "Sequence",
      getInitialState = { _ =>
        SequenceState(proposals = Seq.empty, currentSlideIndex = 0, votes = Seq(""))
      },
      componentWillUpdate = { (self, props, state) =>
        },
      componentDidMount = { self =>
        self.props.wrapped.proposals.onComplete {
          case Success(proposals) =>
            self.setState(
              _.copy(
                proposals = scala.util.Random.shuffle(proposals.toList),
                votes = proposals.filter(_.votes.exists(_.hasVoted)).map(_.id.value)
              )
            )
          case Failure(_) =>
        }
      },
      render = { self =>
        var slider: Option[Slider] = None

        def updateCurrentSlideIndex(previewSlide: Int, currentSlide: Int): Unit = {
          self.setState(state => state.copy(currentSlideIndex = currentSlide))
        }

        def startSequence: () => Unit = { () =>
          slider.foreach(_.slickNext())
          // TODO facebook take sequence id from variable
          FacebookPixel.fbq(
            "trackCustom",
            "click-sequence-launch",
            Map("location" -> "sequence", "sequence-id" -> "1").toJSDictionary
          )
        }

        def nextProposal: () => Unit = { () =>
          slider.foreach(_.slickNext())
          FacebookPixel.fbq("trackCustom", "click-sequence-next-proposal")
        }

        def nextOnSuccessfulVote(proposalId: ProposalId): () => Unit = { () =>
          val nextState = self.state.copy(votes = proposalId.value +: self.state.votes)
          self.setState(nextState)
        }

        def proposalContent(proposal: ProposalModel): Seq[ReactElement] = Seq(
          <.div(^.className := Seq(RowRulesStyles.row))(
            <.div(^.className := ColRulesStyles.col)(
              <.header(^.className := ProposalInSlideStyles.infosWrapper)(
                <.h4(^.className := Seq(TextStyles.mediumText, ProposalInSlideStyles.infos))(
                  ProposalAuthorInfosFormat.apply(proposal)
                )
              ),
              <.div(^.className := ProposalInSlideStyles.contentWrapper)(
                <.h3(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(proposal.content),
                <.div(^.className := ProposalInSlideStyles.voteWrapper)(
                  <.VoteContainerComponent(
                    ^.wrapped := VoteContainerProps(
                      proposal = proposal,
                      onSuccessfulVote = nextOnSuccessfulVote(proposal.id)
                    )
                  )(),
                  <.div(^.className := ProposalInSlideStyles.ctaWrapper)(
                    <.button(
                      ^.className := Seq(
                        CTAStyles.basic,
                        CTAStyles.basicOnButton,
                        ProposalInSlideStyles.ctaVisibility(self.state.votes.contains(proposal.id.value))
                      ),
                      ^.disabled := !self.state.votes.contains(proposal.id.value),
                      ^.onClick := nextProposal
                    )(
                      unescape("Proposition suivante" + "&nbsp;"),
                      <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.angleRight))()
                    )
                  )
                )
              )
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
                      total = if (self.state.proposals.nonEmpty) { self.state.proposals.size + 2 } else { 0 },
                      maybeThemeColor = self.props.wrapped.maybeThemeColor
                    )
                  )()
                )
              )
            )
          ),
          if (self.state.proposals.nonEmpty) {
            <.div(^.className := SequenceStyles.slideshowWrapper)(
              <.div(^.className := SequenceStyles.slideshowInnerWrapper)(
                <.div(^.className := RowRulesStyles.centeredRow)(
                  <.div(^.className := ColRulesStyles.col)(
                    <.div(^.className := SequenceStyles.slideshow)(
                      <.button(^.className := SequenceStyles.showPrevSlideButton)(),
                      <.Slider(^.ref := ((s: HTMLElement) => {
                        slider = Option(s.asInstanceOf[Slider])
                      }), ^.infinite := false, ^.arrows := false, ^.accessibility := false, ^.swipe := false, ^.afterChange := updateCurrentSlideIndex)(
                        <.div(^.className := SequenceStyles.slideWrapper)(
                          <.article(^.className := SequenceStyles.slide)(
                            <(self.props.wrapped.intro)(
                              ^.wrapped := IntroOfOperationSequenceProps(clickOnButtonHandler = startSequence)
                            )()
                          )
                        ),
                        self.state.proposals.map { proposal: ProposalModel =>
                          <.div(^.className := SequenceStyles.slideWrapper)(
                            <.article(^.className := SequenceStyles.slide)(proposalContent(proposal))
                          )
                        },
                        <.div(^.className := SequenceStyles.slideWrapper)(
                          <.article(^.className := SequenceStyles.slide)(<(self.props.wrapped.conclusion).empty)
                        )
                      ),
                      <.button(^.className := SequenceStyles.showNextSlideButton)()
                    )
                  )
                )
              )
            )
          } else {
            <.div(^.className := SequenceStyles.spinnerWrapper)(
              <.div(^.className := SequenceStyles.spinnerInnerWrapper)(
                <.div(^.className := RowRulesStyles.centeredRow)(
                  <.div(^.className := ColRulesStyles.col)(<.SpinnerComponent.empty)
                )
              )
            )
          },
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
      position.relative,
      unsafeChild(".slick-slider")(height(100.%%)),
      unsafeChild(".slick-list")(overflow.visible, height(100.%%)),
      unsafeChild(".slick-track")(height(100.%%), display.flex),
      unsafeChild(".slick-slide")(
        height.auto,
        minHeight.inherit,
        transition := "transform .2s ease-in-out",
        transform := "scale(0.9)"
      ),
      unsafeChild(".slick-slide.slick-active")(transform := "scale(1)")
    )

  val slideWrapper: StyleA =
    style()

  val slide: StyleA =
    style(
      height(100.%%),
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val showPrevSlideButton: StyleA =
    style(position.absolute, top(`0`), right(100.%%), zIndex(1), height(100.%%), width(9999.pxToEm()))

  val showNextSlideButton: StyleA =
    style(position.absolute, top(`0`), left(100.%%), zIndex(1), height(100.%%), width(9999.pxToEm()))

  val spinnerWrapper: StyleA =
    style(display.tableRow, height(100.%%))

  val spinnerInnerWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)
}

object ProposalInSlideStyles extends StyleSheet.Inline {
  import dsl._

  val infosWrapper: StyleA =
    style(
      textAlign.center,
      position.relative,
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
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val ctaWrapper: StyleA =
    style(textAlign.center, marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val ctaVisibility: (Boolean) => StyleA = styleF.bool(
    visible =>
      if (visible) {
        styleS(visibility.visible)
      } else {
        styleS(visibility.hidden)
    }
  )
}
