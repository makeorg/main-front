package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.sequence.ProgressBar.ProgressBarProps
import org.make.front.components.sequence.contents.ProposalInsideSequence.ProposalInsideSequenceProps
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements, Slider}
import org.make.front.models.{
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  ProposalId        => ProposalIdModel,
  Qualification     => QualificationModel,
  Sequence          => SequenceModel,
  TranslatedTheme   => TranslatedThemeModel,
  Vote              => VoteModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TableLayoutStyles
import org.make.front.styles.utils._
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext
import org.scalajs.dom.raw.HTMLElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}
object Sequence {

  final case class DisplayTracker(name: String, context: TrackingContext, parameters: Map[String, String] = Map.empty)

  final case class ExtraSlide(reactClass: ReactClass,
                              props: (() => Unit) => Any,
                              position: (Seq[Slide]) => Int,
                              displayed: Boolean = true,
                              maybeTracker: Option[DisplayTracker] = None)

  final case class SequenceProps(loadSequence: (Seq[ProposalIdModel]) => Future[SequenceModel],
                                 sequence: Option[SequenceModel],
                                 progressBarColor: Option[String],
                                 shouldReload: Boolean,
                                 extraSlides: Seq[ExtraSlide],
                                 maybeTheme: Option[TranslatedThemeModel],
                                 maybeOperation: Option[OperationModel],
                                 maybeLocation: Option[LocationModel])

  final case class SequenceState(proposals: Seq[ProposalModel],
                                 slides: Seq[Slide],
                                 displayedSlidesCount: Int,
                                 currentSlideIndex: Int,
                                 maybeSequence: Option[SequenceModel] = None)

  trait Slide {
    def component(index: Int): ReactElement
    def optional: Boolean
    def maybeTracker: Option[DisplayTracker] = None
  }

  class EmptyElementSlide(element: ReactClass, override val optional: Boolean = false) extends Slide {
    override def component(index: Int): ReactElement = <(element).empty
  }

  class BasicSlide(element: ReactClass,
                   props: Any,
                   override val optional: Boolean = false,
                   override val maybeTracker: Option[DisplayTracker] = None)
      extends Slide {

    override def component(index: Int): ReactElement =
      <(element)(^.wrapped := props)()
  }

  class ProposalSlide(proposal: ProposalModel,
                      onSuccessfulVote: (ProposalIdModel)        => (VoteModel) => Unit,
                      onSuccessfulQualification: ProposalIdModel => (String, QualificationModel) => Unit,
                      canScrollNext: (Int)                       => Boolean,
                      nextProposal: ()                           => Unit,
                      maybeTheme: Option[TranslatedThemeModel],
                      maybeOperation: Option[OperationModel],
                      maybeSequence: Option[SequenceModel],
                      maybeLocation: Option[LocationModel])
      extends Slide {

    val voted: Boolean = proposal.votes.exists(_.hasVoted)

    override def component(slideIndex: Int): ReactElement =
      <.ProposalInsideSequenceComponent(
        ^.wrapped := ProposalInsideSequenceProps(
          proposal = proposal,
          handleSuccessfulVote = onSuccessfulVote(proposal.id),
          handleSuccessfulQualification = onSuccessfulQualification(proposal.id),
          handleClickOnCta = nextProposal,
          hasBeenVoted = canScrollNext(slideIndex),
          /*TODO : guides if first slide*/
          /*guideToVote = Some(I18n.t("sequence.guide.vote")),
          guideToQualification = Some(I18n.t("sequence.guide.qualification")),*/
          index = slideIndex,
          maybeTheme = maybeTheme,
          maybeOperation = maybeOperation,
          maybeSequence = maybeSequence,
          maybeLocation = maybeLocation
        )
      )()

    override val optional: Boolean = false
  }

  lazy val reactClass: ReactClass = {
    var slider: Option[Slider] = None

    def maxScrollableIndex(slides: List[Slide]): Int = {
      slides match {
        case Nil => 0
        case head :: tail if head.isInstanceOf[ProposalSlide] =>
          if (head.asInstanceOf[ProposalSlide].voted) {
            1 + maxScrollableIndex(tail)
          } else {
            0
          }
        case head :: tail =>
          if (head.optional) {
            1 + maxScrollableIndex(tail)
          } else {
            0
          }

      }
    }

    def next: () => Unit = { () =>
      slider.foreach(_.slickNext())
    }

    def nextProposal: () => Unit = { () =>
      next()
    }

    def previous: () => Unit = { () =>
      slider.foreach(_.slickPrev())
    }

    def onSuccessfulVote(proposalId: ProposalIdModel, self: Self[SequenceProps, SequenceState]): (VoteModel) => Unit = {
      (voteModel) =>
        def mapProposal(proposal: ProposalModel) = {
          if (proposal.id == proposalId) {
            proposal.copy(votes = proposal.votes.map { vote =>
              if (vote.key == voteModel.key) {
                voteModel
              } else {
                vote
              }
            })
          } else {
            proposal
          }
        }

        // toDo: should be fixed with all cases
        val updatedProposals = self.state.proposals.map(mapProposal)
        val isLastSlideDisplayed: Boolean = self.state.currentSlideIndex == self.state.displayedSlidesCount - 1

        val displayedSlidesCount = if (isLastSlideDisplayed && voteModel.hasVoted) {
          self.state.displayedSlidesCount + 1
        } else {
          self.state.displayedSlidesCount
        }

        val slides = createSlides(self, updatedProposals)
        self.setState(
          _.copy(slides = slides, proposals = updatedProposals, displayedSlidesCount = displayedSlidesCount)
        )
    }

    def onSuccessfulQualification(proposalId: ProposalIdModel,
                                  self: Self[SequenceProps, SequenceState]): (String, QualificationModel) => Unit = {
      (voteKey, qualificationModel) =>
        def mapProposal(proposal: ProposalModel): ProposalModel = {
          if (proposal.id == proposalId) {
            proposal.copy(votes = proposal.votes.map { vote =>
              if (vote.key == voteKey) {
                vote.copy(qualifications = vote.qualifications.map { qualification =>
                  if (qualification.key == qualificationModel.key) {
                    qualificationModel
                  } else {
                    qualification
                  }
                })
              } else {
                vote
              }
            })
          } else {
            proposal
          }
        }

        val updatedProposals = self.state.proposals.map(mapProposal)
        val slides = createSlides(self, updatedProposals)
        self.setState(_.copy(slides = slides, proposals = updatedProposals))
    }

    def createSlides(self: Self[SequenceProps, SequenceState], allProposals: Seq[ProposalModel]): Seq[Slide] = {

      var slides: Seq[Slide] = allProposals.map({ proposal =>
        new ProposalSlide(
          proposal,
          onSuccessfulVote(_, self),
          onSuccessfulQualification(_, self),
          canScrollNext = (slideIndex) => {
            val currentSlideIndex = slideIndex
            val slides = self.state.slides
            if (currentSlideIndex + 1 < slides.size) {
              slides(currentSlideIndex) match {
                case slide if slide.isInstanceOf[ProposalSlide] => slide.asInstanceOf[ProposalSlide].voted
                case slide                                      => slide.optional
              }
            } else {
              false
            }
          },
          nextProposal = nextProposal,
          maybeTheme = self.props.wrapped.maybeTheme,
          maybeOperation = self.props.wrapped.maybeOperation,
          maybeSequence = self.state.maybeSequence,
          maybeLocation = self.props.wrapped.maybeLocation
        )
      })

      self.props.wrapped.extraSlides.foreach { extraSlide =>
        if (extraSlide.displayed) {
          val position = extraSlide.position(slides)
          slides = slides.take(position) ++
            Seq(
              new BasicSlide(
                element = extraSlide.reactClass,
                props = extraSlide.props(next),
                optional = true,
                maybeTracker = extraSlide.maybeTracker
              )
            ) ++
            slides.drop(position)
        }
      }

      slides
    }

    def onNewProps(self: Self[SequenceProps, SequenceState],
                   props: Props[SequenceProps],
                   slider: Option[Slider]): Unit = {

      val votedProposalIds = self.state.proposals.filter(_.votes.exists(_.hasVoted)).map(_.id)

      props.wrapped.loadSequence(votedProposalIds).onComplete {
        case Success(sequence) =>
          self.setState(_.copy(maybeSequence = Some(sequence)))
          val slides: Seq[Slide] = createSlides(self, sequence.proposals)
          val firstNonVotedSlideIndex: Int = slides.indexWhere { slide =>
            slide.isInstanceOf[ProposalSlide] && !slide.asInstanceOf[ProposalSlide].voted
          }
          val hasVotedProposals: Boolean = sequence.proposals.headOption.exists(_.votes.exists(_.hasVoted))
          val lastSlideIndex: Int = slides.size - 1
          val indexToReach =
            if (!hasVotedProposals) 0
            else if (firstNonVotedSlideIndex == -1) lastSlideIndex
            else firstNonVotedSlideIndex
          val currentSlideIsOptional: Boolean = slides(indexToReach).optional || (slides(indexToReach)
            .isInstanceOf[ProposalSlide] &&
            slides(indexToReach).asInstanceOf[ProposalSlide].voted)

          val displayedSlidesCount: Int = if (currentSlideIsOptional) {
            indexToReach + 2
          } else {
            indexToReach + 1
          }

          self.setState(
            _.copy(
              slides = slides,
              proposals = sequence.proposals,
              currentSlideIndex = indexToReach,
              displayedSlidesCount = displayedSlidesCount
            )
          )

        case Failure(_) =>
      }
    }

    React.createClass[SequenceProps, SequenceState](
      displayName = "Sequence",
      getInitialState = { _ =>
        SequenceState(slides = Seq.empty, displayedSlidesCount = 0, currentSlideIndex = 0, proposals = Seq.empty)
      },
      componentWillReceiveProps = { (self, props) =>
        if ((props.wrapped.shouldReload && props.wrapped.shouldReload != self.props.wrapped.shouldReload) || props.wrapped.sequence != self.props.wrapped.sequence) {
          onNewProps(self, props, slider)
        }
      },
      componentDidMount = { self =>
        onNewProps(self, self.props, slider)
        TrackingService
          .track(
            "display-sequence",
            TrackingContext(TrackingLocation.sequencePage),
            Map("sequenceId" -> self.state.maybeSequence.map(_.sequenceId.value).getOrElse(""))
          )

      },
      componentWillUpdate = { (_, props, state) =>
        slider.foreach(_.slickGoTo(state.currentSlideIndex))
        val firstIndex = state.slides.takeWhile(!_.isInstanceOf[ProposalSlide]).size
        if (state.currentSlideIndex == firstIndex) {
          TrackingService
            .track(
              "display-sequence-first-proposal",
              TrackingContext(TrackingLocation.sequencePage, props.wrapped.maybeOperation.map(_.slug)),
              Map(
                "sequenceId" -> state.maybeSequence.map(_.sequenceId.value).getOrElse(""),
                "proposalId" -> state.proposals.headOption.map(_.id.value).getOrElse("")
              )
            )
        }
      },
      render = { self =>
        def updateCurrentSlideIndex(currentSlide: Int): Unit = {
          val oldSlideIndex = self.state.currentSlideIndex
          // If user went back, log it
          if (currentSlide < oldSlideIndex) {
            TrackingService.track(
              "click-sequence-previous-card",
              TrackingContext(TrackingLocation.sequencePage, self.props.wrapped.maybeOperation.map(_.slug)),
              Map(
                "sequenceId" -> self.props.wrapped.maybeOperation
                  .flatMap(_.sequence)
                  .map(_.value)
                  .getOrElse(""),
                "card-position" -> oldSlideIndex.toString
              )
            )
          } else if (currentSlide > oldSlideIndex) {
            TrackingService.track(
              "click-sequence-next-card",
              TrackingContext(TrackingLocation.sequencePage, self.props.wrapped.maybeOperation.map(_.slug)),
              Map(
                "sequenceId" -> self.props.wrapped.maybeOperation
                  .flatMap(_.sequence)
                  .map(_.value)
                  .getOrElse(""),
                "card-position" -> oldSlideIndex.toString
              )
            )
          }
          self.setState(state => state.copy(currentSlideIndex = currentSlide))
          self.state.slides(self.state.currentSlideIndex).maybeTracker.foreach { tracker =>
            TrackingService.track(tracker.name, tracker.context, tracker.parameters)
          }
        }

        def canScrollNext: Boolean = {
          val currentSlideIndex = self.state.currentSlideIndex
          val slides = self.state.slides
          if (currentSlideIndex + 1 < slides.size) {
            slides(currentSlideIndex) match {
              case slide if slide.isInstanceOf[ProposalSlide] => slide.asInstanceOf[ProposalSlide].voted
              case slide                                      => slide.optional
            }
          } else {
            false
          }

        }

        def maxIndex = maxScrollableIndex(self.state.slides.toList)

        <.div(^.className := Seq(TableLayoutStyles.fullHeightWrapper, SequenceStyles.wrapper))(
          <.div(^.className := TableLayoutStyles.row)(
            <.div(^.className := Seq(TableLayoutStyles.cellVerticalAlignBottom, SequenceStyles.progressBarWrapper))(
              <.div(^.className := Seq(SequenceStyles.centeredRow))(
                <.ProgressBarComponent(
                  ^.wrapped := ProgressBarProps(
                    value = self.state.currentSlideIndex,
                    total = self.state.slides.size,
                    maybeThemeColor = self.props.wrapped.progressBarColor
                  )
                )()
              )
            )
          ),
          if (self.state.slides.nonEmpty) {
            var counter: Int = -1
            <.div(^.className := TableLayoutStyles.fullHeightRow)(
              <.div(^.className := Seq(TableLayoutStyles.cell, SequenceStyles.slideshowInnerWrapper))(
                <.div(^.className := Seq(SequenceStyles.centeredRow, SequenceStyles.fullHeight))(
                  <.div(^.className := SequenceStyles.slideshow)(
                    if (self.state.currentSlideIndex > 0)
                      <.button(
                        ^.className := SequenceStyles.showPrevSlideButton,
                        ^.onClick := previous,
                        ^.disabled := self.state.currentSlideIndex == 0
                      )(),
                    <.Slider(^.ref := { (slideshow: HTMLElement) =>
                      slider = Option(slideshow.asInstanceOf[Slider])
                      slider.foreach { s =>
                        val oldSlideHandler: js.Function1[Int, Unit] = s.innerSlider.slideHandler
                        s.innerSlider.slideHandler = { index =>
                          if (index > maxIndex) {
                            oldSlideHandler(maxIndex)
                          } else {
                            oldSlideHandler(index)
                          }
                        }
                      }
                    }, ^.infinite := false, ^.arrows := false, ^.accessibility := true, ^.swipe := true, ^.afterChange := updateCurrentSlideIndex, ^.initialSlide := self.state.currentSlideIndex)(
                      self.state.slides.map { slide =>
                        counter += 1
                        <.div(^.className := SequenceStyles.slideWrapper)(
                          <.article(^.className := SequenceStyles.slide)(
                            <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
                              <.div(
                                ^.className := Seq(
                                  TableLayoutStyles.cellVerticalAlignMiddle,
                                  SequenceStyles.slideInnerSubWrapper
                                )
                              )(slide.component(counter))
                            )
                          )
                        )
                      }
                    ),
                    if (canScrollNext) {
                      <.button(^.className := SequenceStyles.showNextSlideButton, ^.onClick := next)()
                    }
                  )
                )
              )
            )
          } else {
            <.div(^.className := TableLayoutStyles.fullHeightRow)(
              <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                <.div(^.className := SequenceStyles.centeredRow)(<.SpinnerComponent.empty)
              )
            )
          },
          <.style()(SequenceStyles.render[String])
        )
      }
    )
  }
}

object SequenceStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(tableLayout.fixed)

  val centeredRow: StyleA =
    style(
      display.block,
      position.relative,
      paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondLarge(maxWidth(ThemeStyles.containerMaxWidth), marginRight.auto, marginLeft.auto)
    )

  val fullHeight: StyleA =
    style(height(100.%%))

  val progressBarWrapper: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingBottom(`0`),
      ThemeStyles.MediaQueries.beyondSmall(
        paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
        paddingBottom(ThemeStyles.SpacingValue.small.pxToEm())
      )
    )

  val slideshowInnerWrapper: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
      overflow.hidden
    )

  val slideshow: StyleA =
    style(
      position.relative,
      height(100.%%),
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
      minWidth(270.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val slideInnerSubWrapper: StyleA =
    style(paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()), paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val showPrevSlideButton: StyleA =
    style(position.absolute, top(`0`), right(100.%%), zIndex(1), height(100.%%), width(9999.pxToEm()))

  val showNextSlideButton: StyleA =
    style(position.absolute, top(`0`), left(100.%%), zIndex(1), height(100.%%), width(9999.pxToEm()))

}
