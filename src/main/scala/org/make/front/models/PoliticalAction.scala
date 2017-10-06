package org.make.front.models

final case class PoliticalAction(imageUrl: String,
                                 date: Option[String],
                                 location: Option[String],
                                 text: String,
                                 links: Option[String],
                                 introduction: Option[String],
                                 themeSlug: Option[String])
