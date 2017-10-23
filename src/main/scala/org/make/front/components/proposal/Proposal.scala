package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.helpers.ProposalAuthorInfosFormat
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet

object Proposal {

  final case class ProposalProps(proposal: Future[ProposalModel], index: Int)

  final case class ProposalState(proposal: ProposalModel)

  lazy val reactClass: ReactClass =
    React.createClass[ProposalProps, ProposalState](displayName = "Proposal", getInitialState = { _ =>
      ProposalState(proposal = null)
    }, componentWillReceiveProps = { (self, props) =>
      self.props.wrapped.proposal.onComplete {
        case Failure(_)        =>
        case Success(proposal) => self.setState(_.copy(proposal = proposal))
      }
    }, render = {
      self =>
        if (self.state.proposal != null) {
          <.div(^.className := ProposalStyles.wrapper)(
            <.div(^.className := Seq(RowRulesStyles.row))(
              <.div(^.className := ColRulesStyles.col)(
                <.article(^.className := ProposalStyles.innerWrapper)(
                  <.div(^.className := ProposalStyles.infosWrapper)(
                    <.p(^.className := Seq(TextStyles.mediumText, ProposalStyles.infos))(
                      ProposalAuthorInfosFormat.apply(self.state.proposal)
                    )
                  ),
                  <.div(^.className := ProposalStyles.contentWrapper)(
                    <.h3(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(self.state.proposal.content),
                    <.div(^.className := ProposalStyles.voteWrapper)(
                      <.VoteContainerComponent(
                        ^.wrapped := VoteContainerProps(
                          proposal = self.state.proposal,
                          index = self.props.wrapped.index
                        )
                      )()
                    )
                  )
                )
              )
            ),
            <.style()(ProposalStyles.render[String])
          )
        } else {
          <.div.empty
        }

    })
}

object ProposalStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      paddingTop((ThemeStyles.SpacingValue.larger + 50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((ThemeStyles.SpacingValue.larger + 80).pxToEm())),
      paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
      minHeight(100.%%),
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent)
    )

  val innerWrapper: StyleA =
    style(
      minHeight(300.pxToEm()),
      paddingTop(ThemeStyles.SpacingValue.larger.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

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
