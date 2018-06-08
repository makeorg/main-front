package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.Element
import org.make.front.Main.CssSettings._
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.OrganisationInfo

import scala.scalajs.js

object ProposalActorVoted {
  final case class ProposalActorVotedProps(organisations: Seq[OrganisationInfo])

  val reactClass: ReactClass =
    React
      .createClass[ProposalActorVotedProps, Unit](
        displayName = "ProposalActorVoted",
        render = (self) => {
          val organisationElementList: Seq[Any] =
            self.props.wrapped.organisations.flatMap(_.organisationName).map { name =>
              <.span()(name)
            } match {
              case Seq(organisationElement) => Seq(organisationElement)
              case organisationElements if organisationElements.length > 1 =>
                organisationElements
                  .dropRight(1)
                  .foldLeft(Seq.empty[Any]) { (a, b) =>
                    a :+ b :+ ", "
                  }
                  .dropRight(1) :+ I18n.t("proposal.actor-voted-and") :+ organisationElements.last
              case _ => Seq.empty
            }

          <.div()(if (organisationElementList.nonEmpty) {
            <.p()(
              organisationElementList :+ I18n
                .t("proposal.actor-voted", Replacements(("count", self.props.wrapped.organisations.length.toString)))
            )
          }, <.style()(ProposalActorStyles.render[String]))

        }
      )
}

object ProposalActorStyles extends StyleSheet.Inline {}
