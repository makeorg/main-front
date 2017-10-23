package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.operation.IntroOfOperationSequence.IntroOfOperationSequenceProps
import org.make.front.components.operation.PromptingToProposeInsideOperationSequence.PromptingToProposeInsideOperationSequenceProps
import org.make.front.components.sequence.ProgressBar.ProgressBarProps
import org.make.front.components.sequence.ProposalInsideSequence.ProposalInsideSequenceProps
import org.make.front.facades.{FacebookPixel, I18n}
import org.make.front.facades.ReactSlick.{ReactTooltipVirtualDOMAttributes, ReactTooltipVirtualDOMElements, Slider}
import org.make.front.models.{
  Theme       => ThemeModel,
  Operation   => OperationModel,
  OperationId => OperationIdModel
}
import org.make.front.models.{Proposal => ProposalModel, ProposalId => ProposalIdModel, Sequence => SequenceModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._
import org.make.services.proposal.ProposalResponses.{QualificationResponse, VoteResponse}
import org.scalajs.dom.raw.HTMLElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.JSConverters._
import scala.util.{Failure, Success}
import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet

object Sequence {

  final case class SequenceProps(sequence: SequenceModel,
                                 maybeThemeColor: Option[String],
                                 maybeTheme: Option[ThemeModel],
                                 maybeOperation: Option[OperationModel],
                                 proposals: Future[Seq[ProposalModel]],
                                 intro: ReactClass,
                                 conclusion: ReactClass,
                                 promptingToPropose: ReactClass)

  final case class SequenceState(proposals: Seq[ProposalModel],
                                 displayedProposals: Seq[ProposalModel],
                                 currentSlideIndex: Int,
                                 votes: Seq[String])

  private def updateProposals(self: Self[SequenceProps, SequenceState],
                              props: Props[SequenceProps],
                              slider: Option[Slider]): Unit = {
    props.wrapped.proposals.onComplete {
      case Success(proposals) =>
        val votes = proposals.filter(_.votes.exists(_.hasVoted)).map(_.id.value)
        val allProposals = scala.util.Random.shuffle(proposals.toList)
        val index = if (votes.nonEmpty) { votes.size + 1 } else { 0 }
        val displayedProposals = votes.flatMap { id =>
          allProposals.find(_.id.value == id)
        } ++ allProposals.find(proposal => !votes.contains(proposal.id.value))
        self.setState(
          _.copy(
            proposals = allProposals,
            votes = votes,
            displayedProposals = displayedProposals,
            currentSlideIndex = index
          )
        )
        if (votes.nonEmpty) {
          slider.foreach(_.slickGoTo(index))
        }
      case Failure(_) =>
    }
  }

  lazy val reactClass: ReactClass = {
    var slider: Option[Slider] = None

    React.createClass[SequenceProps, SequenceState](displayName = "Sequence", getInitialState = { _ =>
      SequenceState(proposals = Seq.empty, displayedProposals = Seq.empty, currentSlideIndex = 0, votes = Seq(""))
    }, componentWillReceiveProps = { (self, props) =>
      updateProposals(self, props, slider)
    }, componentDidMount = { self =>
      updateProposals(self, self.props, slider)
    }, render = {
      self =>
        def updateCurrentSlideIndex(currentSlide: Int): Unit = {
          self.setState(state => state.copy(currentSlideIndex = currentSlide))
        }

        def next: () => Unit = { () =>
          slider.foreach(_.slickNext())
        }

        def previous: () => Unit = { () =>
          slider.foreach(_.slickPrev())
        }

        def canScrollNext: Boolean = {
          if (self.state.votes.size == self.state.proposals.size) {
            self.state.currentSlideIndex < self.state.proposals.size + 1
          } else {
            self.state.currentSlideIndex < self.state.displayedProposals.size
          }
        }

        def startSequence: () => Unit = { () =>
          next()
          // TODO facebook take sequence id from variable
          FacebookPixel.fbq(
            "trackCustom",
            "click-sequence-launch",
            Map("location" -> "sequence", "sequence-id" -> "1").toJSDictionary
          )
        }

        def nextProposal: () => Unit = { () =>
          next()
          FacebookPixel.fbq("trackCustom", "click-sequence-next-proposal")
        }

        def nextOnSuccessfulVote(proposalId: ProposalIdModel): (VoteResponse) => Unit = {
          (voteResponse) =>
            def mapProposal(proposal: ProposalModel) = {
              if (proposal.id == proposalId) {
                proposal.copy(votes = proposal.votes.map { vote =>
                  if (vote.key == voteResponse.voteKey) {
                    voteResponse.toVote
                  } else {
                    vote
                  }
                })
              } else {
                proposal
              }
            }

            val votes = if (self.state.votes.contains(proposalId.value)) {
              self.state.votes
            } else {
              self.state.votes ++ Seq(proposalId.value)
            }
            val updatedProposals = self.state.proposals.map(mapProposal)
            val updatedDisplayedProposals = if (votes.size == self.state.displayedProposals.size) {
              self.state.displayedProposals.map(mapProposal) ++ self.state.proposals
                .find(proposal => !votes.contains(proposal.id.value))
            } else {
              self.state.displayedProposals.map(mapProposal)
            }

            self.setState(
              _.copy(votes = votes, proposals = updatedProposals, displayedProposals = updatedDisplayedProposals)
            )
        }

        def onSuccessfulQualification(proposalId: ProposalIdModel): (String, QualificationResponse) => Unit = {
          (voteKey, qualificationResponse) =>
            def mapProposal(proposal: ProposalModel) = {
              if (proposal.id == proposalId) {
                proposal.copy(votes = proposal.votes.map { vote =>
                  if (vote.key == voteKey) {
                    vote.copy(qualifications = vote.qualifications.map { qualification =>
                      if (qualification.key == qualificationResponse.qualificationKey) {
                        qualificationResponse.toQualification
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
            val updatedDisplayedProposals = self.state.displayedProposals.map(mapProposal)
            self.setState(_.copy(proposals = updatedProposals, displayedProposals = updatedDisplayedProposals))
        }


        <.div(^.className := Seq(SequenceStyles.wrapper))(
          <.div(^.className := SequenceStyles.progressBarWrapper)(
            <.div(^.className := SequenceStyles.progressBarInnerWrapper)(
              <.div(^.className := Seq(SequenceStyles.centeredRow))(
                <.ProgressBarComponent(
                  ^.wrapped := ProgressBarProps(
                    value = self.state.currentSlideIndex,
                    total = if (self.state.proposals.nonEmpty) { self.state.proposals.size + 3 } else { 0 },
                    maybeThemeColor = self.props.wrapped.maybeThemeColor
                  )
                )()
              )
            )
          ),
          if (self.state.proposals.nonEmpty) {
            <.div(^.className := SequenceStyles.slideshowWrapper)(
              <.div(^.className := SequenceStyles.slideshowInnerWrapper)(
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
                    )(
                      <.div(^.className := SequenceStyles.slideWrapper)(
                        <.article(^.className := SequenceStyles.slide)(
                          <.div(^.className := SequenceStyles.slideInnerWrapper)(
                            <.div(^.className := SequenceStyles.slideInnerSubWrapper)(
                              <(self.props.wrapped.intro)(
                                ^.wrapped := IntroOfOperationSequenceProps(clickOnButtonHandler = startSequence)
                              )()
                            )
                          )
                        )
                      ),
                      self.state.displayedProposals.map {
                        var i: Int = 0
                        proposal: ProposalModel =>
                          i = i + 1
                          Seq(if (i == (self.state.displayedProposals.size / 2) + 1) {
                            <.div(^.className := SequenceStyles.slideWrapper)(
                              <.article(^.className := SequenceStyles.slide)(
                                <.div(^.className := SequenceStyles.slideInnerWrapper)(
                                  <.div(^.className := SequenceStyles.slideInnerSubWrapper)(
                                    <(self.props.wrapped.promptingToPropose)(
                                      ^.wrapped := PromptingToProposeInsideOperationSequenceProps(
                                        operation = self.props.wrapped.maybeOperation
                                          .getOrElse(
                                            OperationModel(OperationIdModel("fake"), "", "", "", 0, 0, "", None)
                                          ),
                                        clickOnButtonHandler = nextProposal
                                      )
                                    )()
                                  )
                                )
                              )
                            )
                          },
                          <.div(^.className := SequenceStyles.slideWrapper)(
                            <.article(^.className := SequenceStyles.slide)(
                              <.div(^.className := SequenceStyles.slideInnerWrapper)(
                                <.div(^.className := SequenceStyles.slideInnerSubWrapper)(
                                  <.ProposalInsideSequenceComponent(
                                    ^.wrapped := ProposalInsideSequenceProps(
                                      proposal = proposal,
                                      handleSuccessfulVote = nextOnSuccessfulVote(proposal.id),
                                      handleSuccessfulQualification = onSuccessfulQualification(proposal.id),
                                      handleClickOnCta = nextProposal,
                                      hasBeenVoted = self.state.votes.contains(proposal.id.value),
                                      /*TODO : guides if first slide*/
                                      guideToVote = Some(I18n.t("sequence.guide.vote")),
                                      guideToQualification = Some(I18n.t("sequence.guide.qualification")),
                                      index = self.state.currentSlideIndex
                                    )
                                  )()
                                )
                              )
                            )
                          ))},

                      if (self.state.votes.size == self.state.proposals.size) {
                        <.div(^.className := SequenceStyles.slideWrapper)(
                          <.article(^.className := SequenceStyles.slide)(
                            <.div(^.className := SequenceStyles.slideInnerWrapper)(
                              <.div(^.className := SequenceStyles.slideInnerSubWrapper)(
                                <(self.props.wrapped.conclusion).empty
                              )
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
            <.div(^.className := SequenceStyles.spinnerWrapper)(
              <.div(^.className := SequenceStyles.spinnerInnerWrapper)(
                <.div(^.className := SequenceStyles.centeredRow)(<.SpinnerComponent.empty)
              )
            )
          },
          <.style()(SequenceStyles.render[String])
        )
    })
  }
}

object SequenceStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(display.table, tableLayout.fixed, width(100.%%), height(100.%%))

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
    style(display.tableRow)

  val progressBarInnerWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.bottom,
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingBottom.`0`,
      ThemeStyles.MediaQueries.beyondSmall(
        paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
        paddingBottom(ThemeStyles.SpacingValue.small.pxToEm())
      )
    )

  val slideshowWrapper: StyleA =
    style(display.tableRow, height(100.%%))

  val slideshowInnerWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.top,
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

  val slideInnerWrapper: StyleA =
    style(display.table, height(100.%%), width(100.%%))

  val slideInnerSubWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm())
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