package org.make.front.helpers

object NumberFormat {

  def formatToKilo(number: Int): String = {
    number match {
      case _ if number >= 1000 => (Math.ceil(number / 100) / 10).toString + "k"
      case _                   => number.toString
    }
  }

  def formatToPercent(count: Int, total: Int): Int = {
    if (count == 0) 0
    else if (total == 0) 100
    else count * 100 / total
  }
}
