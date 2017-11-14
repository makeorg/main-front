package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.sequence.contents.PromptingToConnect.PromptingToConnectProps
import org.make.front.components.sequence.contents.PromptingToProposeSequence.PromptingToProposeProps
import org.make.front.components.sequence.ProgressBar.ProgressBarProps
import org.make.front.components.sequence.contents.IntroductionOfTheSequence.IntroductionOfTheSequenceProps
import org.make.front.components.sequence.contents.PromptingToContinueAfterTheSequence.PromptingToContinueAfterTheSequenceProps
import org.make.front.components.sequence.contents.ProposalInsideSequence.ProposalInsideSequenceProps
import org.make.front.facades.FacebookPixel
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements, Slider}
import org.make.front.models.{
  Operation     => OperationModel,
  OperationId   => OperationIdModel,
  Proposal      => ProposalModel,
  ProposalId    => ProposalIdModel,
  Qualification => QualificationModel,
  Sequence      => SequenceModel,
  Theme         => ThemeModel,
  Vote          => VoteModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TableLayoutStyles
import org.make.front.styles.utils._
import org.scalajs.dom.raw.HTMLElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Sequence {

  final case class SequenceProps(sequence: SequenceModel,
                                 maybeThemeColor: Option[String],
                                 maybeTheme: Option[ThemeModel],
                                 maybeOperation: Option[OperationModel],
                                 proposals: Future[Seq[ProposalModel]],
                                 intro: ReactClass,
                                 conclusion: ReactClass,
                                 promptingToPropose: ReactClass,
                                 promptingToConnect: ReactClass,
                                 promptingToContinueAfterTheSequence: ReactClass,
                                 shouldReload: Boolean)

  final case class SequenceState(proposals: Seq[ProposalModel],
                                 slides: Seq[Slide],
                                 displayedSlidesCount: Int,
                                 currentSlideIndex: Int)

  trait Slide {
    def component(index: Int): ReactElement
    def optional: Boolean
  }

  class EmptyElementSlide(element: ReactClass, override val optional: Boolean = false) extends Slide {
    override def component(index: Int): ReactElement = <(element).empty
  }

  class BasicSlide(element: ReactClass, props: Any, override val optional: Boolean = false) extends Slide {
    override def component(index: Int): ReactElement =
      <(element)(^.wrapped := props)()
  }

  class ProposalSlide(proposal: ProposalModel,
                      onSuccessfulVote: (ProposalIdModel)        => (VoteModel) => Unit,
                      onSuccessfulQualification: ProposalIdModel => (String, QualificationModel) => Unit,
                      canScrollNext: (Int)                       => Boolean,
                      nextProposal: ()                           => Unit)
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
          index = slideIndex
        )
      )()

    override val optional: Boolean = false
  }

  lazy val reactClass: ReactClass = {
    var slider: Option[Slider] = None

    def next: () => Unit = { () =>
      slider.foreach(_.slickNext())
    }

    def nextProposal: () => Unit = { () =>
      next()
      FacebookPixel.fbq("trackCustom", "click-sequence-next-proposal")
    }

    def previous: () => Unit = { () =>
      slider.foreach(_.slickPrev())
    }

    def sortProposal(proposals: Seq[ProposalModel]): Seq[ProposalModel] = {
      scala.util.Random.shuffle(proposals)
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
        def mapProposal(proposal: ProposalModel) = {
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

      def startSequence: () => Unit = { () =>
        next()
      }

      val promptingToPropose = new Slide() {
        override def component(index: Int): ReactElement =
          <(self.props.wrapped.promptingToPropose)(
            ^.wrapped := PromptingToProposeProps(
              operation = self.props.wrapped.maybeOperation
                .getOrElse(OperationModel(OperationIdModel("fake"), "", "", "", "", 0, 0, "", None)),
              clickOnButtonHandler = { () =>
                nextProposal()
              },
              proposeHandler = { () =>
                nextProposal()
              }
            )
          )()

        override val optional = true
      }

      val promptingToConnect = new Slide() {
        override def component(index: Int): ReactElement =
          <(self.props.wrapped.promptingToConnect)(
            ^.wrapped := PromptingToConnectProps(
              operation = self.props.wrapped.maybeOperation
                .getOrElse(OperationModel(OperationIdModel("fake"), "", "", "", "", 0, 0, "", None)),
              clickOnButtonHandler = nextProposal,
              authenticateHandler = { () =>
                nextProposal()
              }
            )
          )()

        override val optional = true
      }

      val promptingToContinueAfterTheSequence = new Slide() {
        override def component(index: Int): ReactElement =
          <(self.props.wrapped.promptingToContinueAfterTheSequence)(
            ^.wrapped := PromptingToContinueAfterTheSequenceProps(
              operation = self.props.wrapped.maybeOperation
                .getOrElse(OperationModel(OperationIdModel("fake"), "", "", "", "", 0, 0, "", None)),
              clickOnButtonHandler = { () =>
                }
            )
          )()

        override val optional = true
      }

      val intro = new Slide() {
        override def component(index: Int): ReactElement =
          <(self.props.wrapped.intro)(
            ^.wrapped := IntroductionOfTheSequenceProps(clickOnButtonHandler = nextProposal)
          )()

        override val optional = true
      }

      val conclusion = new EmptyElementSlide(self.props.wrapped.conclusion)

      val slides = allProposals.map({ proposal =>
        new ProposalSlide(
          proposal,
          onSuccessfulVote(_, self),
          onSuccessfulQualification(_, self),
          canScrollNext = (slideIndex) => {
            slideIndex + 1 < self.state.displayedSlidesCount
          },
          nextProposal = nextProposal
        )
      })

      val cardIndex: Int = pushToProposalIndex(slides)

      Seq(intro) ++ slides.take(cardIndex) ++ Seq(promptingToPropose) ++ slides.drop(cardIndex) ++ Seq(
        promptingToConnect
      ) ++ Seq(promptingToContinueAfterTheSequence) ++ Seq(conclusion)

    }

    def pushToProposalIndex(slides: Seq[Slide]): Int = {
      slides.size / 2
    }

    def onNewProps(self: Self[SequenceProps, SequenceState],
                   props: Props[SequenceProps],
                   slider: Option[Slider]): Unit = {

      props.wrapped.proposals.onComplete {
        case Success(proposals) =>
          val votedProposals = proposals.filter(_.votes.exists(_.hasVoted))
          val otherProposals = sortProposal(proposals.filter(_.votes.forall(!_.hasVoted)))
          val allProposals = votedProposals ++ otherProposals
          val slides: Seq[Slide] = createSlides(self, allProposals)
          // toDo: Manage optional card adding to middle of slides
          //val firstOptionalSlideIndex: Int = slides.indexWhere(_.optional)
          val firstNonVotedSlideIndex: Int = slides.indexWhere { slide =>
            slide.isInstanceOf[ProposalSlide] && !slide.asInstanceOf[ProposalSlide].voted
          }
          val hasVotedProposals: Boolean = votedProposals.nonEmpty
          val lastSlideIndex: Int = slides.size - 1
          val indexToReach =
            if (!hasVotedProposals) 0
            else if (firstNonVotedSlideIndex == -1) lastSlideIndex
            else firstNonVotedSlideIndex
          val showNextSlide: Boolean = slides(indexToReach).optional || (slides(indexToReach)
            .isInstanceOf[ProposalSlide] &&
            slides(indexToReach).asInstanceOf[ProposalSlide].voted)

          val displayedSlidesCount: Int = if (showNextSlide) indexToReach + 2 else indexToReach + 1

          self.setState(
            _.copy(
              slides = slides,
              proposals = allProposals,
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
        if (props.wrapped.shouldReload) {
          onNewProps(self, props, slider)
        }
      },
      componentDidMount = { self =>
        onNewProps(self, self.props, slider)
        FacebookPixel
          .fbq("trackCustom", "display-sequence")

      },
      componentWillUpdate = { (_, _, state) =>
        slider.foreach(_.slickGoTo(state.currentSlideIndex))
      },
      render = { self =>
        def updateCurrentSlideIndex(currentSlide: Int): Unit = {
          self.setState(state => state.copy(currentSlideIndex = currentSlide))
        }

        def canScrollNext: Boolean = {
          self.state.currentSlideIndex + 1 < self.state.displayedSlidesCount
        }

        val visibleSlides = self.state.slides.take(self.state.displayedSlidesCount)

        <.div(^.className := Seq(TableLayoutStyles.fullHeightWrapper, SequenceStyles.wrapper))(
          <.div(^.className := TableLayoutStyles.row)(
            <.div(^.className := Seq(TableLayoutStyles.cellVerticalAlignBottom, SequenceStyles.progressBarWrapper))(
              <.div(^.className := Seq(SequenceStyles.centeredRow))(
                <.ProgressBarComponent(
                  ^.wrapped := ProgressBarProps(
                    value = self.state.currentSlideIndex,
                    total = self.state.slides.size,
                    maybeThemeColor = self.props.wrapped.maybeThemeColor
                  )
                )()
              )
            )
          ),
          if (visibleSlides.nonEmpty) {
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
                    <.Slider(
                      ^.ref := ((slideshow: HTMLElement) => slider = Option(slideshow.asInstanceOf[Slider])),
                      ^.infinite := false,
                      ^.arrows := false,
                      ^.accessibility := true,
                      ^.swipe := true,
                      ^.afterChange := updateCurrentSlideIndex,
                      ^.initialSlide := self.state.currentSlideIndex
                    )(visibleSlides.map { slide =>
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
                    }),
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
