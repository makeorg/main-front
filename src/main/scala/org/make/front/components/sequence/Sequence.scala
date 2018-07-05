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

package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.sequence.ProgressBar.ProgressBarProps
import org.make.front.components.sequence.contents.ProposalInsideSequence.ProposalInsideSequenceProps
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements, Slider}
import org.make.front.models.{
  SequenceId,
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
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.scalajs.dom.raw.HTMLElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}
object Sequence {

  final case class DisplayTracker(name: String, context: TrackingContext, parameters: Map[String, String] = Map.empty)

  final case class ExtraSlide(reactClass: ReactClass,
                              props: (() => Unit) => Any,
                              position: js.Array[Slide] => Int,
                              displayed: Boolean = true,
                              maybeTracker: Option[DisplayTracker] = None,
                              onFocus: () => Unit = () => {},
                              onBlur: ()  => Unit = () => {})

  final case class SequenceProps(loadSequence: js.Array[ProposalIdModel] => Future[SequenceModel],
                                 sequence: SequenceModel,
                                 progressBarColor: Option[String],
                                 isConnected: Boolean,
                                 extraSlides: js.Array[ExtraSlide],
                                 maybeTheme: Option[TranslatedThemeModel],
                                 maybeOperation: Option[OperationModel],
                                 maybeLocation: Option[LocationModel],
                                 endSequence: () => Any)

  final case class SequenceState(proposals: js.Array[ProposalModel],
                                 slides: js.Array[Slide],
                                 displayedSlidesCount: Int,
                                 currentSlideIndex: Int)

  trait Slide {
    def component(index: Int): ReactElement
    def optional: Boolean
    def maybeTracker: Option[DisplayTracker] = None
    def onFocus: () => Unit = () => {}
    def onBlur: ()  => Unit = () => {}
  }

  class SlideCollection {
    protected var currentSlides: js.Array[Slide] = js.Array()
    def getSlides: js.Array[Slide] = currentSlides
    def addSlide(slide: Slide): Unit = currentSlides = currentSlides ++ js.Array(slide)
    def addSlides(slides: Array[Slide]): Unit = slides.foreach(addSlide)
    def addSlideAtIndex(slide: Slide, position: Int): Unit = addSlidesAtPosition(Array(slide), position)
    def addSlidesAtPosition(slides: Array[Slide], position: Int): Unit =
      currentSlides = currentSlides.take(position) ++ slides ++ currentSlides.drop(position)
    def length: Int = currentSlides.length
    def isEmpty: Boolean = currentSlides.isEmpty
    def nonEmpty: Boolean = currentSlides.nonEmpty
    def lastIndex: Int = currentSlides.size - 1
    def getSlide(index: Int): Slide = currentSlides(index)
  }

  class MakeSlideCollection extends SlideCollection {
    def firstNonVotedSlideIndex: Option[Int] = {
      currentSlides.indexWhere { slide =>
        slide.isInstanceOf[ProposalSlide] && !slide.asInstanceOf[ProposalSlide].voted
      } match {
        case -1    => None
        case index => Some(index)
      }
    }

    def slideIsOptional(index: Int): Boolean = {
      val slide = getSlide(index)
      slide.optional || (slide
        .isInstanceOf[ProposalSlide] &&
      slide.asInstanceOf[ProposalSlide].voted)
    }

    def slideCountToDisplay(currentIndex: Int): Int = {
      if (slideIsOptional(currentIndex)) {
        currentIndex + 2
      } else {
        currentIndex + 1
      }
    }

    def hasVotedProposalSlide: Boolean = {
      currentSlides.indexWhere { slide =>
        slide.isInstanceOf[ProposalSlide] && slide.asInstanceOf[ProposalSlide].voted
      } match {
        case -1 => false
        case _  => true
      }
    }

    def nextIndexToReach: Int = {
      if (!hasVotedProposalSlide) 0 else firstNonVotedSlideIndex.getOrElse(lastIndex)
    }
  }

  class EmptyElementSlide(element: ReactClass, override val optional: Boolean = false) extends Slide {
    override def component(index: Int): ReactElement = <(element).empty
  }

  class BasicSlide(element: ReactClass,
                   props: Any,
                   override val optional: Boolean = false,
                   override val maybeTracker: Option[DisplayTracker] = None,
                   override val onFocus: () => Unit = () => {},
                   override val onBlur: ()  => Unit = () => {})
      extends Slide {

    override def component(index: Int): ReactElement = <(element)(^.wrapped := props)()
  }

  class ProposalSlide(proposal: ProposalModel,
                      onSuccessfulVote: ProposalIdModel          => VoteModel => Unit,
                      onSuccessfulQualification: ProposalIdModel => (String, QualificationModel) => Unit,
                      canScrollNext: Int                         => Boolean,
                      nextProposal: ()                           => Unit,
                      maybeTheme: Option[TranslatedThemeModel],
                      maybeOperation: Option[OperationModel],
                      sequenceId: SequenceId,
                      maybeLocation: Option[LocationModel],
                      override val onFocus: () => Unit = () => {},
                      override val onBlur: ()  => Unit = () => {})
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
          sequenceId = sequenceId,
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

    def next(): Unit = {
      slider.foreach(_.slickNext())
    }

    def nextProposal: () => Unit = { () =>
      next()
    }

    def previous: () => Unit = { () =>
      slider.foreach(_.slickPrev())
    }

    def onSuccessfulVote(proposalId: ProposalIdModel, self: Self[SequenceProps, SequenceState]): VoteModel => Unit = {
      voteModel =>
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

        val slideCollection: MakeSlideCollection = createSlides(self, updatedProposals)
        self.setState(
          _.copy(
            slides = slideCollection.getSlides,
            proposals = updatedProposals,
            displayedSlidesCount = displayedSlidesCount
          )
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
        val slideCollection: MakeSlideCollection = createSlides(self, updatedProposals)
        self.setState(_.copy(slides = slideCollection.getSlides, proposals = updatedProposals))
    }

    def createSlides(self: Self[SequenceProps, SequenceState],
                     allProposals: js.Array[ProposalModel]): MakeSlideCollection = {

      val slideCollection = new MakeSlideCollection

      allProposals.foreach({ proposal =>
        slideCollection.addSlide(
          new ProposalSlide(
            proposal,
            onSuccessfulVote(_, self),
            onSuccessfulQualification(_, self),
            canScrollNext = slideIndex => {
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
            sequenceId = self.props.wrapped.sequence.sequenceId,
            maybeLocation = self.props.wrapped.maybeLocation
          )
        )
      })

      self.props.wrapped.extraSlides.foreach { extraSlide =>
        if (extraSlide.displayed) {
          slideCollection.addSlideAtIndex(
            new BasicSlide(
              element = extraSlide.reactClass,
              props = extraSlide.props(() => next()),
              optional = true,
              maybeTracker = extraSlide.maybeTracker,
              onFocus = () => extraSlide.onFocus(),
              onBlur = extraSlide.onBlur
            ),
            extraSlide.position(slideCollection.getSlides)
          )
        }
      }

      slideCollection
    }

    def onSequenceRetrieved(self: Self[SequenceProps, SequenceState], sequence: SequenceModel): Unit = {
      val slideCollection: MakeSlideCollection = createSlides(self, sequence.proposals)
      if (slideCollection.nonEmpty) {
        self.setState(
          _.copy(
            slides = slideCollection.getSlides,
            proposals = sequence.proposals,
            currentSlideIndex = slideCollection.nextIndexToReach,
            displayedSlidesCount = slideCollection.slideCountToDisplay(slideCollection.nextIndexToReach)
          )
        )
      } else {
        self.setState(_.copy(proposals = sequence.proposals))
      }
    }

    WithRouter(
      React.createClass[SequenceProps, SequenceState](
        displayName = "Sequence",
        getInitialState = { _ =>
          SequenceState(slides = js.Array(), displayedSlidesCount = 0, currentSlideIndex = -1, proposals = js.Array())
        },
        componentWillReceiveProps = { (self, props) =>
          if (props.wrapped.sequence.sequenceId.value != self.props.wrapped.sequence.sequenceId.value) {
            onSequenceRetrieved(self, props.wrapped.sequence)
          } else if (self.props.wrapped.isConnected != props.wrapped.isConnected) {
            val votedProposalIds = self.state.proposals.filter(_.votes.exists(_.hasVoted)).map(_.id)
            props.wrapped.loadSequence(votedProposalIds).onComplete {
              case Success(sequence) => onSequenceRetrieved(self, sequence)
              case Failure(_)        =>
            }
          }
        },
        componentDidMount = { self =>
          onSequenceRetrieved(self, self.props.wrapped.sequence)
          TrackingService
            .track(
              "display-sequence",
              TrackingContext(TrackingLocation.sequencePage, self.props.wrapped.maybeOperation.map(_.slug)),
              Map(
                "sequenceId" -> self.props.wrapped.sequence.sequenceId.value,
                "operationId" -> self.props.wrapped.maybeOperation.map(_.operationId.value).getOrElse("")
              )
            )
        },
        componentWillUpdate = { (self, props, state) =>
          slider.foreach { activeSlider =>
            activeSlider.slickGoTo(state.currentSlideIndex)
            self.state.slides(self.state.currentSlideIndex).onBlur()
            state.slides(state.currentSlideIndex).onFocus()
          }

          val firstProposalIndex = state.slides.takeWhile(!_.isInstanceOf[ProposalSlide]).size
          if (self.state.currentSlideIndex != state.currentSlideIndex) {
            // At init
            if (self.state.currentSlideIndex == -1) {
              state.slides(state.currentSlideIndex).onFocus()
            }

            state.slides(state.currentSlideIndex).maybeTracker.foreach { tracker =>
              TrackingService
                .track(
                  tracker.name,
                  tracker.context,
                  tracker.parameters + ("card-position" -> state.currentSlideIndex.toString)
                )
            }
          }
          if (state.currentSlideIndex == firstProposalIndex &&
              state.proposals.nonEmpty &&
              self.state.currentSlideIndex != state.currentSlideIndex) {

            TrackingService
              .track(
                "display-sequence-first-proposal",
                TrackingContext(TrackingLocation.sequencePage, props.wrapped.maybeOperation.map(_.slug)),
                Map(
                  "sequenceId" -> self.props.wrapped.sequence.sequenceId.value,
                  "proposalId" -> state.proposals.headOption.map(_.id.value).getOrElse("")
                )
              )
          }
          if (state.currentSlideIndex == state.slides.size - 1) {
            self.props.wrapped.endSequence()
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
                  "sequenceId" -> self.props.wrapped.sequence.sequenceId.value,
                  "card-position" -> oldSlideIndex.toString
                )
              )
            } else if (currentSlide > oldSlideIndex) {
              TrackingService.track(
                "click-sequence-next-card",
                TrackingContext(TrackingLocation.sequencePage, self.props.wrapped.maybeOperation.map(_.slug)),
                Map(
                  "sequenceId" -> self.props.wrapped.sequence.sequenceId.value,
                  "card-position" -> oldSlideIndex.toString
                )
              )
            }

            self.setState(state => state.copy(currentSlideIndex = currentSlide))
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

          if (self.state.currentSlideIndex == self.state.slides.size - 1 && self.state.currentSlideIndex != -1) {
            self.props.wrapped.endSequence()
          }

          <.div(^.className := js.Array(TableLayoutStyles.fullHeightWrapper, SequenceStyles.wrapper))(
            <.div(^.className := TableLayoutStyles.row)(
              <.div(
                ^.className := js.Array(TableLayoutStyles.cellVerticalAlignBottom, SequenceStyles.progressBarWrapper)
              )(
                <.div(^.className := js.Array(SequenceStyles.centeredRow))(
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
                <.div(^.className := js.Array(TableLayoutStyles.cell, SequenceStyles.slideshowInnerWrapper))(
                  <.div(^.className := js.Array(SequenceStyles.centeredRow, SequenceStyles.fullHeight))(
                    <.div(^.className := SequenceStyles.slideshow)(
                      if (self.state.currentSlideIndex > 0)
                        <.button(
                          ^.className := SequenceStyles.showPrevSlideButton,
                          ^.onClick := previous,
                          ^.disabled := self.state.currentSlideIndex == 0
                        )(),
                      <.Slider(^.ref := { slideshow: HTMLElement =>
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
                              <.div(^.className := js.Array(SequenceStyles.slideInnerSubWrapper))(
                                slide.component(counter)
                              )
                            )
                          )
                        }.toSeq
                      ),
                      if (canScrollNext) {
                        <.button(^.className := SequenceStyles.showNextSlideButton, ^.onClick := (() => next()))()
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
    style(
      ThemeStyles.MediaQueries
        .verySmall(maxHeight(500.pxToEm()), overflow.hidden)
    )

  val slide: StyleA =
    style(
      height(100.%%),
      minWidth(240.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)",
      textAlign.center,
      whiteSpace.nowrap,
      &.before(content := "' '", display.inlineBlock, height(100.%%), verticalAlign.middle),
      ThemeStyles.MediaQueries
        .verySmall(overflow.scroll)
    )

  val slideInnerSubWrapper: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.middle,
      whiteSpace.normal,
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm())
    )

  val showPrevSlideButton: StyleA =
    style(position.absolute, top(`0`), right(100.%%), zIndex(1), height(100.%%), width(9999.pxToEm()))

  val showNextSlideButton: StyleA =
    style(position.absolute, top(`0`), left(100.%%), zIndex(1), height(100.%%), width(9999.pxToEm()))

}
