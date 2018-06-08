package org.make.front.helpers

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.Proposal
import org.make.front.styles.vendors.FontAwesomeStyles

object ProposalAuthorInfosFormat {

  def apply(proposal: Proposal): ReactElement = {
    val age: String = if (proposal.author.age.get != 0) {
      I18n
        .t("proposal.author-infos.age", Replacements(("age", s"${proposal.author.age.getOrElse("")}")))
    } else {
      ""
    }

    val postalCode: String = if (proposal.author.postalCode.get != null) {
      I18n
        .t(
          "proposal.author-infos.postal-code",
          Replacements(("postalCode", s"${proposal.author.postalCode.map(_.substring(0, 2)).getOrElse("")}"))
        )
    } else {
      ""
    }

    def formatAuthorName(maybeName: Option[String]): String = {
      maybeName.getOrElse(I18n.t("proposal.author-infos.anonymous")).toLowerCase.capitalize
    }

    if (proposal.author.organisationName.isDefined) {
      <.p()(
        formatAuthorName(proposal.author.organisationName) + " ",
        <.i(^.className := FontAwesomeStyles.checkCircle)()
      )
    } else {
      <.p()(formatAuthorName(proposal.author.firstName) + age + postalCode)
    }
  }
}
