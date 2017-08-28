package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.presentationals.TagListComponent.TagListComponentProps
import org.make.front.facades.I18n
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.models.{Proposal, Theme, ThemeId}
import org.make.front.styles.MakeStyles

import scalacss.DevDefaults._

trait ProposalLocation

case object ThemePage extends ProposalLocation
case object HomePage extends ProposalLocation

object ProposalTileComponent {

  final case class ProposalTileProps(proposal: Proposal,
                                     proposalLocation: ProposalLocation,
                                     isHomePage: Boolean,
                                     associatedTheme: Option[Theme])

  def reactClass: ReactClass =
    React.createClass[ProposalTileProps, Unit](render = (self) => {
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

      <.div(^.className := ProposalTileStyle.proposalCard)(
        <.div(^.className := ProposalTileStyle.proposalUpPart)(
          <.div(^.className := ProposalTileStyle.proposalAuthor)(header(self.props.wrapped.proposal)),
          <.div(^.className := ProposalTileStyle.proposalText(self.props.wrapped.isHomePage))(
            <.span()(self.props.wrapped.proposal.content)
          ),
          <.VoteButtonComponent(
            ^.wrapped := VoteButtonComponent.VoteButtonProps(
              voteAgreeStats = self.props.wrapped.proposal.voteAgree,
              voteDisagreeStats = self.props.wrapped.proposal.voteDisagree,
              voteNeutralStats = self.props.wrapped.proposal.voteNeutral,
              totalVote = self.props.wrapped.proposal.voteAgree.count +
                self.props.wrapped.proposal.voteDisagree.count +
                self.props.wrapped.proposal.voteNeutral.count
            )
          )()
        ),
        <.div(^.className := ProposalTileStyle.proposalTags)(self.props.wrapped.proposalLocation match {
          case ThemePage =>
            <.TagListComponent(
              ^.wrapped := TagListComponentProps(
                tags = self.props.wrapped.proposal.tags,
                handleSelectedTags = _ => (),
                withShowMoreButton = false
              )
            )()
          case HomePage =>
            if (self.props.wrapped.associatedTheme.nonEmpty) {
              <.div()(
                <.Translate(^.className := ProposalTileStyle.postedInText, ^.value := s"content.proposal.postedIn")(),
                <.span(^.className := ProposalTileStyle.postedInTextTheme)(self.props.wrapped.associatedTheme.get.title)
              )
            }
        }),
        <.style()(ProposalTileStyle.render[String])
      )
    })
}

object ProposalTileStyle extends StyleSheet.Inline {
  import dsl._

  val proposalCard: StyleA = style(
    display.flex,
    flexDirection.column,
    justifyContent.flexStart,
    flex := "1",
    height(100.%%),
    padding(1.5.rem),
    backgroundColor :=! "#fff",
    boxShadow := "0.1rem 0.1rem 0.1rem 0 rgba(0, 0, 0, .5)",
    (&.hover)(boxShadow := "0.1rem 0.1rem 2.5rem 0 rgba(0, 0, 0, .3)")
  )

  val proposalUpPart: StyleA = style(position.relative, flex := "1")

  val proposalAuthor: StyleA = style(
    marginBottom(1.rem),
    paddingBottom(0.6.rem),
    borderBottom :=! "0.1rem solid rgba(0, 0, 0, .1)",
    color :=! "rgba(0, 0, 0, .5)",
    fontSize(1.4.rem),
    MakeStyles.Font.circularStdBook
  )

  val proposalText: (Boolean) => StyleA = styleF.bool(
    isHomePage =>
      if (isHomePage) {
        styleS(marginBottom(1.rem), MakeStyles.Font.circularStdBold, fontWeight :=! "700", fontSize(2.8.rem))
      } else {
        styleS(marginBottom(1.rem), MakeStyles.Font.circularStdBold, fontWeight :=! "700", fontSize(1.6.rem))
    }
  )

  val proposalTags: StyleA =
    style(borderTop :=! "0.1rem solid rgba(0, 0, 0, .1)", paddingLeft(1.36.rem), textAlign.left, paddingTop(1.rem))

  val postedInText: StyleA = style(MakeStyles.Font.circularStdBook, fontSize(1.4.rem), color(rgba(0, 0, 0, 0.5)))

  val postedInTextTheme: StyleA =
    style(MakeStyles.Font.tradeGothicLTStd, fontSize(1.4.rem), color(MakeStyles.Color.pink))
}
