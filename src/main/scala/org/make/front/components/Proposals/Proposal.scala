package org.make.front.components.Proposals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Tags.TagsListComponent.TagsListComponentProps
import org.make.front.components.Vote.VoteComponent
import org.make.front.components.presentationals._
import org.make.front.facades.I18n
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.models.{Proposal, Theme}
import org.make.front.styles.{TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length

trait ProposalLocation

case object ThemePage extends ProposalLocation

case object HomePage extends ProposalLocation

object ProposalComponent {

  final case class ProposalProps(proposal: Proposal,
                                 proposalLocation: ProposalLocation,
                                 isHomePage: Boolean,
                                 associatedTheme: Option[Theme])

  val reactClass: ReactClass =
    React.createClass[ProposalProps, Unit](render = (self) => {
      def header(proposal: Proposal): ReactElement = {
        (proposal.author.age, proposal.author.postalCode) match {
          case (Some(age), Some(postalCode)) =>
            <.Translate(
              ^.value := "content.proposal.fullHeader",
              ^("firstName") := proposal.author.firstname.getOrElse(I18n.t("anonymous")),
              ^("age") := s"${age.toString}",
              ^("postalCode") := s"$postalCode"
            )()
          case (Some(age), None) =>
            <.Translate(
              ^.value := "content.proposal.ageHeader",
              ^("firstName") := proposal.author.firstname.getOrElse(I18n.t("anonymous")),
              ^("age") := s"${age.toString}"
            )()
          case (None, Some(postalCode)) =>
            <.Translate(
              ^.value := "content.proposal.postalCodeHeader",
              ^("firstName") := proposal.author.firstname.getOrElse(I18n.t("anonymous")),
              ^("postalCode") := s"$postalCode"
            )()
          case (None, None) =>
            <.Translate(
              ^.value := "content.proposal.tinyHeader",
              ^("firstName") := proposal.author.firstname.getOrElse(I18n.t("anonymous"))
            )()
        }
      }

      <.article(^.className := ProposalStyles.wrapper)(
        <.header()(<.h4()(header(self.props.wrapped.proposal))),
        <.h3(^.className := Seq(TextStyles.boldText, ProposalStyles.proposalContent(self.props.wrapped.isHomePage)))(
          self.props.wrapped.proposal.content
        ),
        <.VoteComponent(
          ^.wrapped := VoteComponent.VoteProps(
            voteAgreeStats = self.props.wrapped.proposal.voteAgree,
            voteDisagreeStats = self.props.wrapped.proposal.voteDisagree,
            voteNeutralStats = self.props.wrapped.proposal.voteNeutral,
            totalVote = self.props.wrapped.proposal.voteAgree.count + self.props.wrapped.proposal.voteDisagree.count + self.props.wrapped.proposal.voteNeutral.count
          )
        )(),
        <.footer()(self.props.wrapped.proposalLocation match {
          case ThemePage =>
            <.TagsListComponent(
              ^.wrapped := TagsListComponentProps(
                tags = self.props.wrapped.proposal.tags,
                handleSelectedTags = _ => (),
                withShowMoreTagsButton = false
              )
            )()
          case HomePage =>
            if (self.props.wrapped.associatedTheme.nonEmpty) {
              <.div()(
                <.Translate(^.value := s"content.proposal.postedIn")(),
                self.props.wrapped.associatedTheme.get.title
              )
            }
        }),
        <.style()(ProposalStyles.render[String])
      )

    })
}

object ProposalStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.white), padding(ThemeStyles.SpacingValue.small.pxToEm()))

  val proposalContent: (Boolean) => StyleA = styleF.bool(
    isHomePage =>
      if (isHomePage) {
        styleS()
      } else {
        styleS()
    }
  )
}
