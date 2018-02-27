package org.make.front.helpers

import org.make.front.facades.Configuration

object DetectedCountry {

  def getDetectedCountry: String = {
    if (Configuration.forcedCountry.nonEmpty) {
      Configuration.forcedCountry
    } else if (Configuration.detectedCountry.nonEmpty) {
      Configuration.detectedCountry
    } else {
      "FR"
    }
  }

}
