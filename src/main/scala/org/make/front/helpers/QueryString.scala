package org.make.front.helpers

object QueryString {
  def parse(search: String): Map[String, String] = {
    if (search.nonEmpty) {
      search
        .split("[?]")
        .last
        .split("=|&")
        .grouped(2)
        .map { case Array(k, v) => k -> v }
        .toMap
    } else {
      Map.empty[String, String]
    }
  }
}
