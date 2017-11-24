package org.make.front.models

sealed trait Location { val name: String }

object Location {
  case class Sequence(sequenceId: SequenceId) extends Location { override val name = s"sequence ${sequenceId.value}" }
  case class ThemePage(themeId: ThemeId) extends Location { override val name = s"theme_page ${themeId.value}" }
  case class OperationPage(operationId: OperationId) extends Location {
    override val name = s"operation_page ${operationId.value}"
  }
  case object Homepage extends Location { override val name = "homepage" }
  case object SearchResultsPage extends Location { override val name = "search_results" }
  case class UnknownLocation(path: String = "") extends Location { override val name = s"unknown_location $path" }
}
