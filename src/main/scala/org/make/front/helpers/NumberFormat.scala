package org.make.front.helpers

object NumberFormat {

  def formatToKilo(number: Int): String = {

    number match {
      case _ if number >= 1000 => (Math.ceil(number / 100) / 10).toString+"k"
      case _  => number.toString
    }
  }
}
