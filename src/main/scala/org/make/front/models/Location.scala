package org.make.front.models

sealed trait Location { val name: String }

object Location {
  case class Sequence(sequenceId: SequenceId) extends Location { override val name = s"sequence ${sequenceId.value}" }
  case class ThemePage(themeId: ThemeId) extends Location { override val name = s"theme_page ${themeId.value}" }
  case class OperationPage(operationId: OperationId) extends Location {
    override val name = s"operation_page ${operationId.value}"
  }
  case class ProposalPage(proposalId: ProposalId) extends Location {
    override val name = s"proposal_page ${proposalId.value}"
  }
  case object Homepage extends Location { override val name = "homepage" }
  case object SearchResultsPage extends Location { override val name = "search_results" }
  case class UnknownLocation(path: String = "") extends Location { override val name = s"unknown_location $path" }

  def firstByPrecedence(location: Option[Location],
                        sequence: Option[Sequence],
                        themePage: Option[ThemePage],
                        operationPage: Option[OperationPage],
                        fallback: UnknownLocation): Location =
    location.orElse(sequence).orElse(themePage).orElse(operationPage).getOrElse(fallback)
}
