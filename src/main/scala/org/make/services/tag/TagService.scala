package org.make.services.tag

import org.make.client.{Client, MakeApiClient}
import org.make.front.models.Tag
import org.make.services.ApiService
import org.make.services.proposal.TagResponse

import scala.concurrent.Future
import scala.scalajs.js
import scala.concurrent.ExecutionContext.Implicits.global

object TagService extends ApiService {

  override val resourceName: String = "tags"
  var client: Client = MakeApiClient

  def getTags: Future[Seq[Tag]] =
    client
      .get[js.Array[TagResponse]](apiEndpoint = resourceName, urlParams = Seq.empty, headers = Map.empty)
      .map(_.map(Tag.apply))
}
