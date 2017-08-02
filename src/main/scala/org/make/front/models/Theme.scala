package org.make.front.models

final case class GradientColor(from: String, to: String)

final case class Theme(slug: String,
                       title: String,
                       actionsCount: Int,
                       proposalsCount: Int,
                       color: String,
                       gradient: Option[GradientColor] = None)
