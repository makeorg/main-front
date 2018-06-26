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

package org.make.front.models

import org.make.services.sequence.SequenceResponse

import scala.scalajs.js

final case class Sequence(sequenceId: SequenceId,
                          slug: String,
                          title: String,
                          proposals: js.Array[Proposal] = js.Array())

object Sequence {
  def apply(sequenceResponse: SequenceResponse): Sequence = {
    Sequence(
      sequenceId = SequenceId(sequenceResponse.id),
      slug = sequenceResponse.slug,
      title = sequenceResponse.title,
      proposals = sequenceResponse.proposals.map(Proposal.apply)
    )
  }
}

@js.native
trait SequenceId extends js.Object {
  val value: String
}

object SequenceId {
  def apply(value: String): SequenceId = {
    js.Dynamic.literal(value = value).asInstanceOf[SequenceId]
  }
}
