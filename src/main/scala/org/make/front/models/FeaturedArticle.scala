package org.make.front.models

final case class FeaturedArticle(illUrl: String,
                                 ill2xUrl: String,
                                 imageAlt: Option[String],
                                 label: String,
                                 excerpt: String,
                                 seeMoreLabel: String,
                                 seeMoreLink: String)
