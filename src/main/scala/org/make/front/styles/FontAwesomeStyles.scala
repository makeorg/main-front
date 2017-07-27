package org.make.front.styles

import org.make.front.Main.CssSettings._

object FontAwesomeStyles extends StyleSheet.Inline {

  import dsl._

  val remove: StyleA = style(addClassName("fa"), addClassName("fa-remove"))
  val bullhorn: StyleA = style(addClassName("fa"), addClassName("fa-bullhorn"))
  val thumbsUpTransparent: StyleA = style(addClassName("fa"), addClassName("fa-thumbs-o-up"))
  val thumbsDownTransparent: StyleA = style(addClassName("fa"), addClassName("fa-thumbs-o-down"))
  val lightbulbTransparent: StyleA = style(addClassName("fa"), addClassName("fa-lightbulb-o"))
  val search: StyleA = style(addClassName("fa"), addClassName("fa-search"))
  val heart: StyleA = style(addClassName("fa"), addClassName("fa-heart"))
  val play: StyleA = style(addClassName("fa"), addClassName("fa-play"))
  val pause: StyleA = style(addClassName("fa"), addClassName("fa-pause"))
  val group: StyleA = style(addClassName("fa"), addClassName("fa-group"))
  val pencil: StyleA = style(addClassName("fa"), addClassName("fa-pencil"))
  val eraser: StyleA = style(addClassName("fa"), addClassName("fa-eraser"))
  val envelope: StyleA = style(addClassName("fa"), addClassName("fa-envelope"))
  val envelopeTransparent: StyleA = style(addClassName("fa"), addClassName("fa-envelope-o"))
  val envelopeOpen: StyleA = style(addClassName("fa"), addClassName("fa-envelope-open"))
  val envelopeOpenTransparent: StyleA = style(addClassName("fa"), addClassName("fa-envelope-open-o"))
  val lock: StyleA = style(addClassName("fa"), addClassName("fa-lock"))
  val user: StyleA = style(addClassName("fa"), addClassName("fa-user"))
  val eye: StyleA = style(addClassName("fa"), addClassName("fa-eye"))
  val barChart: StyleA = style(addClassName("fa"), addClassName("bar-chart"))
  val lineChart: StyleA = style(addClassName("fa"), addClassName("line-chart"))
  val rotateLeft: StyleA = style(addClassName("fa"), addClassName("rotate-left"))
  val facebook: StyleA = style(addClassName("fa"), addClassName("fa-facebook"))
  val twitter: StyleA = style(addClassName("fa"), addClassName("fa-twitter"))
  val googlePlus: StyleA = style(addClassName("fa"), addClassName("fa-google-plus"))
}


