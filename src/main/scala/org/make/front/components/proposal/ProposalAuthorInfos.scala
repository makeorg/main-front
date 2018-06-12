package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Localize.{DateLocalizeOptions}
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

object ProposalAuthorInfos {

  final case class ProposalAuthorInfosProps(proposal: ProposalModel)

  val reactClass: ReactClass =
    React
      .createClass[ProposalAuthorInfosProps, Unit](
      displayName = "ProposalAuthorInfos",
      render = (self) => {

        val proposal: ProposalModel = self.props.wrapped.proposal

        val age: String = if (proposal.author.age.getOrElse(0) != 0) {
          I18n
            .t("proposal.author-infos.age", Replacements(("age", s"${proposal.author.age.getOrElse("")}")))
        } else {
          ""
        }

        def formatAuthorName(maybeName: Option[String]): String = {
          maybeName.getOrElse(I18n.t("proposal.author-infos.anonymous")).toLowerCase.capitalize
        }

        <.p(^.className := ProposalAuthorInfosStyles.infos)(
          if (proposal.author.organisationName.isDefined) {
            formatAuthorName(proposal.author.organisationName) + " "
            <.i(^.className := FontAwesomeStyles.checkCircle)()
          } else {
            formatAuthorName(proposal.author.firstName) + age + unescape("&nbsp;&#8226;&nbsp;") + I18n.l(proposal.createdAt, DateLocalizeOptions("common.date.long"))
          },
          <.style()(ProposalAuthorInfosStyles.render[String])
        )
      }
    )
}


object ProposalAuthorInfosStyles extends StyleSheet.Inline {

  import dsl._

  val infos: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.middle,
      ThemeStyles.Font.circularStdBook,
      fontSize(14.pxToEm()),
      marginLeft(ThemeStyles.SpacingValue.small.pxToEm()),
      color(ThemeStyles.TextColor.lighter)
    )

  val userName: StyleA =
    style(
      color(ThemeStyles.ThemeColor.primary)
    )

}