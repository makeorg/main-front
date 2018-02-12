package org.make.front.actions

import io.github.shogowada.scalajs.reactjs.redux.Action

final case class SetLanguage(language: String) extends Action
final case class SetCountry(country: String) extends Action
