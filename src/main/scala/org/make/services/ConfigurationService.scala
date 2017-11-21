package org.make.services

import org.make.client.MakeApiClient
import org.make.front.models.{BusinessConfiguration, BusinessConfigurationResponse}

import scala.concurrent.Future
import org.make.core.URI._

import scala.concurrent.ExecutionContext.Implicits.global

object ConfigurationService {
  def fetchConfiguration(): Future[BusinessConfiguration] = {
    MakeApiClient.get[BusinessConfigurationResponse]("configurations" / "front").map { response =>
      BusinessConfiguration(response)
    }
  }
}
