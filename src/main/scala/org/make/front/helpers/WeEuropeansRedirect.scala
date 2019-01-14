/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.helpers

import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext
import org.scalajs.dom

object WeEuropeansRedirect {
  val languageBySupportedCountries: Map[String, String] = Map(
    "DE" -> "de",
    "AT" -> "de",
    "BE" -> "nl",
    "BG" -> "bg",
    "CY" -> "el",
    "HR" -> "hr",
    "DK" -> "da",
    "ES" -> "es",
    "EE" -> "et",
    "FI" -> "fi",
    "GR" -> "el",
    "HU" -> "hu",
    "IE" -> "en",
    "IT" -> "it",
    "LV" -> "lv",
    "LT" -> "lt",
    "LU" -> "fr",
    "MT" -> "mt",
    "NL" -> "nl",
    "PL" -> "pl",
    "PT" -> "pt",
    "CZ" -> "cs",
    "RO" -> "ro",
    "SK" -> "sk",
    "SI" -> "sl",
    "SE" -> "sv"
  )
  def weEuropeanRedirect(country: String): Unit = {
    if (dom.window.location.hash.isEmpty) {
      languageBySupportedCountries.get(country).foreach { language =>
        val link: String = s"https://weeuropeans.eu/${country.toLowerCase}/$language/about"
        TrackingService.track(
          eventName = "redirect-weeuropeans",
          trackingContext = TrackingContext(TrackingLocation.homepage),
          parameters = Map("redirectTo" -> link, "detectedLanguage" -> language, "detectedCountry" -> country)
        )
        dom.window.location.assign(link)
      }
    }
  }
}
