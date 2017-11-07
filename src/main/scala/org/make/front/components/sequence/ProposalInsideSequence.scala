package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.helpers.ProposalAuthorInfosFormat
import org.make.front.models.{Proposal => ProposalModel, Qualification => QualificationModel, Vote => VoteModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

object ProposalInsideSequence {

  final case class ProposalInsideSequenceProps(proposal: ProposalModel,
                                               handleSuccessfulVote: (VoteModel)                           => Unit = (_) => {},
                                               handleSuccessfulQualification: (String, QualificationModel) => Unit =
                                                 (_, _) => {},
                                               handleClickOnCta: () => Unit,
                                               hasBeenVoted: Boolean,
                                               guideToVote: Option[String] = None,
                                               guideToQualification: Option[String] = None,
                                               index: Int)

  final case class ProposalInsideSequenceState(hasBeenVoted: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[ProposalInsideSequenceProps, ProposalInsideSequenceState](
      displayName = "ProposalInsideSequence",
      getInitialState = { self =>
        ProposalInsideSequenceState(hasBeenVoted = false)
      },
      componentWillReceiveProps = { (self, props) =>
        self.setState(ProposalInsideSequenceState(hasBeenVoted = props.wrapped.hasBeenVoted))
      },
      render = { self =>
        <.div(^.className := Seq(RowRulesStyles.row))(
          <.div(^.className := ColRulesStyles.col)(
            <.div(^.className := ProposalInsideSequenceStyles.infosWrapper)(
              <.p(^.className := Seq(TextStyles.mediumText, ProposalInsideSequenceStyles.infos))(
                ProposalAuthorInfosFormat.apply(self.props.wrapped.proposal)
              )
            ),
            <.div(^.className := ProposalInsideSequenceStyles.contentWrapper)(
              <.h3(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(self.props.wrapped.proposal.content),
              <.div(^.className := ProposalInsideSequenceStyles.voteWrapper)(
                <.VoteContainerComponent(
                  ^.wrapped := VoteContainerProps(
                    proposal = self.props.wrapped.proposal,
                    onSuccessfulVote = self.props.wrapped.handleSuccessfulVote,
                    onSuccessfulQualification = self.props.wrapped.handleSuccessfulQualification,
                    guideToVote = self.props.wrapped.guideToVote,
                    guideToQualification = self.props.wrapped.guideToQualification,
                    index = self.props.wrapped.index
                  )
                )(),
                <.div(^.className := ProposalInsideSequenceStyles.ctaWrapper)(
                  <.button(
                    ^.className := Seq(
                      CTAStyles.basic,
                      CTAStyles.basicOnButton,
                      ProposalInsideSequenceStyles.ctaVisibility(self.props.wrapped.hasBeenVoted)
                    ),
                    ^.disabled := !self.props.wrapped.hasBeenVoted,
                    ^.onClick := self.props.wrapped.handleClickOnCta
                  )(
                    unescape(I18n.t("sequence.proposal.next-cta") + "&nbsp;"),
                    <.i(^.className := FontAwesomeStyles.angleRight)()
                  )
                )
              )
            )
          ),
          <.style()(ProposalInsideSequenceStyles.render[String])
        )
      }
    )
}

object ProposalInsideSequenceStyles extends StyleSheet.Inline {
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
