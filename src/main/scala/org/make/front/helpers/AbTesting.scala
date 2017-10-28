package org.make.front.helpers

import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape

import scala.util.Random

sealed case class IntroTesting(id: Int,
                               name: String,
                               title: Option[String],
                               explanation1: Option[String],
                               explanation2: Option[String]) {
  def isIntroNull: Boolean = {
    0 == id
  }

  def isIntroDefault: Boolean = {
    1 == id
  }

  def isIntroLight: Boolean = {
    2 == id
  }
}

object AbTesting {

  private val abTestingMode: Int = Random.nextInt(3)
  private val defaultTitle: Option[String] = Some(unescape(I18n.t("operation.sequence.introduction.title")))
  private val defaultExplanation1: Option[String] = Some(
    unescape(I18n.t("operation.sequence.introduction.explanation-1"))
  )
  private val defaultExplanation2: Option[String] = Some(
    unescape(I18n.t("operation.sequence.introduction.explanation-2"))
  )

  val introTesting = IntroTesting(
    id = abTestingMode,
    name = abTestingMode match {
      case 0 => "intro-null"
      case 1 => "intro-default"
      case 2 => "intro-light"
    },
    title = abTestingMode match {
      case 0 => defaultTitle
      case 1 => defaultTitle
      case 2 => None
    },
    explanation1 = abTestingMode match {
      case 0 => defaultExplanation1
      case 1 => defaultExplanation1
      case 2 => defaultExplanation1
    },
    explanation2 = abTestingMode match {
      case 0 => defaultExplanation2
      case 1 => Some("Votez sur ces solutions et proposez les vôtres.")
      case 2 => Some("Les plus soutenues détermineront nos actions.")
    }
  )

}
