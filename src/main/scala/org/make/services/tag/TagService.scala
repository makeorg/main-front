/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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

  def getTags: Future[js.Array[Tag]] =
    client
      .get[js.Array[TagResponse]](
        apiEndpoint = s"$resourceName?displayed=true",
        urlParams = js.Array(),
        headers = Map.empty
      )
      .map(_.map(Tag.apply))
}
