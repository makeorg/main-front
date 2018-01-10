package org.make.front.models

final case class Illustration(illUrl: String,
                              ill2xUrl: String,
                              smallIllUrl: Option[String] = None,
                              smallIll2xUrl: Option[String] = None,
                              mediumIllUrl: Option[String] = None,
                              mediumIll2xUrl: Option[String] = None)
