package org.make.front.helpers

import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{Proposal => ProposalModel}

object ProposalAuthorInfosFormat {

  def apply(proposal: ProposalModel): String = {
    val age: String = if (proposal.author.age.isDefined) {
      I18n
        .t("proposal.author-infos.age", Replacements(("age", s"${proposal.author.age.getOrElse("")}")))
    } else {
      ""
    }
    val postalCode: String = if (proposal.author.postalCode.isDefined) {
      I18n
        .t(
          "proposal.author-infos.postal-code",
          Replacements(("postalCode", s"${proposal.author.postalCode.getOrElse("")}"))
        )
    } else {
      ""
    }
    proposal.author.firstName.getOrElse(I18n.t("proposal.author-infos.anonymous")) + age + postalCode
  }
}
