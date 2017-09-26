package org.make.front.reducers

import org.make.front.actions._
import org.make.front.models.BusinessConfiguration

object ConfigurationReducer {

  def reduce(maybeConfiguration: Option[BusinessConfiguration], action: Any): Option[BusinessConfiguration] = {
    action match {
      case SetConfiguration(configuration) => Some(configuration)
      case _                               => maybeConfiguration
    }
  }
}
