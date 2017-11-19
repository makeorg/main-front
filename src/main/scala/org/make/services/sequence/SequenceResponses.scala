package org.make.services.sequence

import org.make.services.proposal.ProposalResponse

import scala.scalajs.js
@js.native
trait SequenceResponse extends js.Object {
  val id: String
  val title: String
  val slug: String
  val proposals: js.Array[ProposalResponse]
  /* toDo : add complementary values
     val translation <=== "translation": [ { "slug": "string", "title": "string", "language": "string"}]
     val tags <==== "tags": [{"tagId": {"value": "string"},"label": "string"}]
     val themes <==== "themes": [{"themeId": {"value": "string"},"translation": [{"slug": "string","title": "string","language": "string"}]}]

 */
}
