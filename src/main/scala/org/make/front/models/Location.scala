package org.make.front.models

sealed trait Location { val name: String }

object Location {
  case object Sequence extends Location { override val name = "sequence" }
  case object Homepage extends Location { override val name = "homepage" }
  case object ThemePage extends Location { override val name = "theme_page" }
  case object OperationPage extends Location { override val name = "operation_page" }
  case object SearchResultsPage extends Location { override val name = "search_results" }
}
