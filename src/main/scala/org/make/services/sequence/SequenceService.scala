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

package org.make.services.sequence

import org.make.client.MakeApiClient
import org.make.core.URI._
import org.make.front.models._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object SequenceService extends ApiService {

  override val resourceName: String = "sequences"

  def startSequenceById(sequenceId: SequenceId, includes: js.Array[ProposalId]): Future[Sequence] = {
    MakeApiClient
      .get[SequenceResponse](
        resourceName / "start" / sequenceId.value,
        includes.map("include" -> _.value),
        Map(MakeApiClient.locationHeader -> Location.Sequence(sequenceId).name)
      )
      .map(Sequence.apply)
  }
}
