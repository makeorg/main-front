package org.make.front.helpers

import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.Proposal

object ProposalAuthorInfosFormat {

  def apply(proposal: Proposal): String = {
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
          Replacements(("postalCode", s"${proposal.author.postalCode.getOrElse("").substring(0, 2)}"))
        )
    } else {
      ""
    }
    proposal.author.firstName.getOrElse(I18n.t("proposal.author-infos.anonymous")) + age + postalCode
  }
}
