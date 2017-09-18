package org.make.front.components.Proposals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Tags.TagsListComponent.TagsListComponentProps
import org.make.front.components.Vote.VoteComponent
import org.make.front.components.presentationals._
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{Proposal, Theme}
import org.make.front.styles.{TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

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

      def infos(proposal: Proposal): String = {

        (proposal.author.age, proposal.author.postalCode) match {

          case (Some(age), Some(postalCode)) =>
            unescape(
              I18n.t(
                "content.proposal.fullHeader",
                Replacements(
                  ("firstName", proposal.author.firstname.getOrElse(I18n.t("anonymous"))),
                  ("age", s"${age.toString}"),
                  ("postalCode", s"$postalCode")
                )
              )
            )

          case (Some(age), None) =>
            unescape(
              I18n.t(
                "content.proposal.ageHeader",
                Replacements(
                  ("firstName", proposal.author.firstname.getOrElse(I18n.t("anonymous"))),
                  ("age", s"${age.toString}")
                )
              )
            )

          case (None, Some(postalCode)) =>
            unescape(
              I18n.t(
                "content.proposal.postalCodeHeader",
                Replacements(
                  ("firstName", proposal.author.firstname.getOrElse(I18n.t("anonymous"))),
                  ("postalCode", s"$postalCode")
                )
              )
            )

          case (None, None) =>
            unescape(
              I18n.t(
                "content.proposal.postalCodeHeader",
                Replacements(("firstName", proposal.author.firstname.getOrElse(I18n.t("anonymous"))))
              )
            )
        }
      }

      <.article(^.className := ProposalStyles.wrapper)(
        <.header(^.className := ProposalStyles.header)(
          <.h4(^.className := Seq(TextStyles.smallText, ProposalStyles.infos))(infos(self.props.wrapped.proposal))
        ),
        <.h3(^.className := Seq(TextStyles.mediumText, TextStyles.boldText))(self.props.wrapped.proposal.content),
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

  val infos: StyleA =
    style(color(ThemeStyles.TextColor.light))

  val header: StyleA = style(
    paddingBottom :=! ThemeStyles.SpacingValue.small.pxToEm(),
    borderBottom :=! s"0.1rem solid ${ThemeStyles.BorderColor.light.value}"
  )

}
