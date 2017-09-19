package org.make.front.components.proposals.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.{TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object ProposalInfos {

  final case class ProposalInfosProps(proposal: ProposalModel)

  val reactClass: ReactClass =
    React.createClass[ProposalInfosProps, Unit](render = (self) => {

      def infos(proposal: ProposalModel): String = {

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

      <.div(^.className := ProposalInfosStyles.wrapper)(
        <.h4(^.className := Seq(TextStyles.smallText, ProposalInfosStyles.infos))(infos(self.props.wrapped.proposal)),
        <.style()(ProposalInfosStyles.render[String])
      )

    })
}

object ProposalInfosStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val infos: StyleA =
    style(color(ThemeStyles.TextColor.light))

  val wrapper: StyleA = style()

}
