package org.make.front.styles

import org.make.front.Main.CssSettings._

object FontAwesomeStyles extends StyleSheet.Inline {

  import dsl._

  val fa: StyleA = style(addClassName("fa"))

  val remove: StyleA = style(addClassName("fa-remove"))
  val bullhorn: StyleA = style(addClassName("fa-bullhorn"))
  val thumbsUp: StyleA = style(addClassName("fa-thumbs-o-up"))
  val thumbsDown: StyleA = style(addClassName("fa-thumbs-o-down"))
  val lightbulbTransparent: StyleA = style(addClassName("fa-lightbulb-o"))
  val search: StyleA = style(addClassName("fa-search"))
  val heart: StyleA = style(addClassName("fa-heart"))
  val play: StyleA = style(addClassName("fa-play"))
  val pause: StyleA = style(addClassName("fa-pause"))
  val group: StyleA = style(addClassName("fa-group"))
  val pencil: StyleA = style(addClassName("fa-pencil"))
  val eraser: StyleA = style(addClassName("fa-eraser"))
  val envelope: StyleA = style(addClassName("fa-envelope"))
  val envelopeTransparent: StyleA = style(addClassName("fa-envelope-o"))
  val envelopeOpen: StyleA = style(addClassName("fa-envelope-open"))
  val envelopeOpenTransparent: StyleA = style(addClassName("fa-envelope-open-o"))
  val paperPlaneTransparent: StyleA = style(addClassName("fa-paper-plane-o"))
  val lock: StyleA = style(addClassName("fa-lock"))
  val user: StyleA = style(addClassName("fa-user"))
  val eye: StyleA = style(addClassName("fa-eye"))
  val eyeSlash: StyleA = style(addClassName("fa-eye-slash"))
  val barChart: StyleA = style(addClassName("fa-bar-chart"))
  val lineChart: StyleA = style(addClassName("fa-line-chart"))
  val rotateLeft: StyleA = style(addClassName("fa-rotate-left"))
  val child: StyleA = style(addClassName("fa-child"))
  val suitCase: StyleA = style(addClassName("fa-suitcase"))

  val facebook: StyleA = style(addClassName("fa-facebook"))
  val twitter: StyleA = style(addClassName("fa-twitter"))
  val googlePlus: StyleA = style(addClassName("fa-google-plus"))
  val warning: StyleA = style(addClassName("fa-exclamation-triangle"))
  val infoCircle: StyleA = style(addClassName("fa-info-circle"))
  val angleLeft: StyleA = style(addClassName("fa-angle-left"))
  val angleRight: StyleA = style(addClassName("fa-angle-right"))
  val calendarOpen: StyleA = style(addClassName("fa-calendar-o"))
  val mapMarker: StyleA = style(addClassName("fa-map-marker"))
  val handOLeft: StyleA = style(addClassName("fa-hand-o-left"))
  val handPeaceO: StyleA = style(addClassName("fa-hand-peace-o"))
}
