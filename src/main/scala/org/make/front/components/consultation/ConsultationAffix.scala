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

package org.make.front.components.consultation

import org.make.front.styles.utils._
import org.scalajs.dom
import org.scalajs.dom.raw.ClientRect
import org.scalajs.dom.{window, Element, Event}

object AffixRequiredElements {
  def wrapperContainer: Element = dom.document.getElementById("wrapperContainer")
  def mainContainer: Element = dom.document.getElementById("mainContainer")
  def sidebarAffixContainer: Element = dom.document.getElementById("sidebarAffixContainer")
  def sidebarAffixElement: Element = dom.document.getElementById("sidebarAffixElement")
  def tabAffixContainer: Element = dom.document.getElementById("tabAffixContainer")
  def tabAffixElement: Element = dom.document.getElementById("tabAffixElement")
  def sidebarAffixContainerRect: ClientRect = sidebarAffixContainer.getBoundingClientRect()
  def mainContainerRect: ClientRect = mainContainer.getBoundingClientRect()
  def tabContainerRect: ClientRect = tabAffixContainer.getBoundingClientRect()
  def wrapperContainerRect: ClientRect = wrapperContainer.getBoundingClientRect()
}

object AffixValues {
  def spacingValue: Int = 30
  def mainContainerHeight: Int = AffixRequiredElements.mainContainer.clientHeight
  def sidebarAffixElementHeight: Int = AffixRequiredElements.sidebarAffixElement.clientHeight
  def specialSidebarWidth: Int =
    AffixRequiredElements.wrapperContainer.clientWidth - AffixRequiredElements.mainContainer.clientWidth - spacingValue * 3
  def specialSidebarBottom: Double = window.innerHeight - AffixRequiredElements.mainContainerRect.bottom.intValue()
  def tabAffixElementHeight: Int = AffixRequiredElements.tabAffixElement.clientHeight
}

object affixConditions {
  def widthThresold: Boolean = window.innerWidth > 1000
  def minHeightForSidebarAffix: Boolean = AffixValues.mainContainerHeight > AffixValues.sidebarAffixElementHeight
  def tabAffixThresold: Boolean = AffixRequiredElements.tabContainerRect.top.intValue() < 0
  def sidebarAffixStart: Boolean =
    AffixRequiredElements.sidebarAffixContainerRect.bottom
      .intValue() - window.innerHeight + 13 < 0 && AffixRequiredElements.wrapperContainerRect.bottom
      .intValue() - window.innerHeight - 40 >= 0
  def specialsidebarAffixStart: Boolean =
    AffixRequiredElements.sidebarAffixContainerRect.top.intValue() - 10 < 0
  def sidebarAffixStop: Boolean = AffixRequiredElements.mainContainerRect.bottom.intValue() - window.innerHeight < 0
  def stopThresold: Boolean = window.pageYOffset > AffixRequiredElements.mainContainerRect.bottom.intValue()
}

object AffixMethods {

  /*  Tab Affix Methods   */

  // Optimizing enablingTabAffix with rAF method on scroll event
  def tabAffixScrollrAF: Unit = {
    var ticking: Boolean = false

    def updaterAF(timestamp: Double): Unit = {
      enablingTabAffix
      ticking = false
    }

    def activaterAF: Unit = {
      if (!ticking) {
        window.requestAnimationFrame(updaterAF)
        ticking = true
      }
    }

    window.addEventListener("scroll", { e: Event =>
      activaterAF
    }, false)
  }

  // handling tab Affix Methods on scroll event
  def enablingTabAffix: Unit = {
    if (AffixRequiredElements.tabAffixContainer != null && AffixRequiredElements.tabAffixContainer != null) {
      if (affixConditions.tabAffixThresold) {
        setTabContainerValues
        enablingTabElementAffix
        addGradientClass
      } else {
        unsetTabContainerValues
        disablingTabElementAffix
        removeGradientClass
      }
    }
  }

  /* Declaring private Tab Affix Methods */
  private def enablingTabElementAffix: Unit = {
    AffixRequiredElements.tabAffixElement.classList.add(ConsultationHeaderStyles.affixOn.htmlClass)
  }

  private def disablingTabElementAffix: Unit = {
    AffixRequiredElements.tabAffixElement.classList.remove(ConsultationHeaderStyles.affixOn.htmlClass)
  }

  /*  Sidebar Affix Methods   */

  // Combining handleSidebarAffix with rAF method
  def sidebarAffixrAF(): Unit = {
    sidebarAffixScrollrAF
    sidebarAffixResizerAF
  }

  // Optimizing scrollSidebarAffix with rAF method on scroll event
  def sidebarAffixScrollrAF: Unit = {
    var ticking: Boolean = false

    def updaterAF(timestamp: Double): Unit = {
      enablingSidebarAffix
      ticking = false
    }

    def activaterAF: Unit = {
      if (!ticking) {
        window.requestAnimationFrame(updaterAF)
        ticking = true
      }
    }

    window.addEventListener("scroll", { e: Event =>
      activaterAF
    }, false)
  }

  // Optimizing resizingSidebarAffix with rAF method on resize event
  def sidebarAffixResizerAF: Unit = {
    var ticking: Boolean = false

    def updaterAF(timestamp: Double): Unit = {
      resizingSidebarAffix
      ticking = false
    }

    def activaterAF: Unit = {
      if (!ticking) {
        window.requestAnimationFrame(updaterAF)
        ticking = true
      }
    }

    window.addEventListener("resize", { e: Event =>
      activaterAF
    }, false)
  }

  // Handling Sidebar Affix Methods on resize event
  def resizingSidebarAffix: Unit = {
    if (window.innerWidth > 1000) {
      AffixMethods.enablingSidebarAffix
    } else {
      AffixMethods.disablingSidebarAffix
    }
  }

  // Declaring Sidebar Affix enabling Method
  def enablingSidebarAffix: Unit = {
    if (AffixRequiredElements.sidebarAffixContainer != null && AffixRequiredElements.tabAffixContainer != null) {
      if (affixConditions.sidebarAffixStart && affixConditions.minHeightForSidebarAffix) {
        setSidebarContainerValues
        enablingSidebarElementAffix
      } else if (affixConditions.sidebarAffixStop && affixConditions.minHeightForSidebarAffix) {
        stoppingSidebarElementAffix
      } else {
        unsetSidebarContainerValues
        disablingSidebarElementAffix
      }
    } else if (AffixRequiredElements.sidebarAffixContainer != null && AffixRequiredElements.tabAffixContainer == null) {
      if (affixConditions.specialsidebarAffixStart && affixConditions.minHeightForSidebarAffix) {
        setSidebarContainerValues
        enablingSpecialSidebarElementAffix
      } else if (affixConditions.sidebarAffixStop && affixConditions.minHeightForSidebarAffix) {
        stoppingSidebarElementAffix
      } else {
        unsetSidebarContainerValues
        disablingSidebarElementAffix
      }
    }
  }

  // declaring Sidebar Affix disabling Method
  def disablingSidebarAffix: Unit = {
    unsetSidebarContainerValues
    disablingSidebarElementAffix
  }

  /* Declaring private Sidebar Affix Methods */
  private def setTabContainerValues: Unit = {
    AffixRequiredElements.tabAffixContainer
      .setAttribute("style", "min-height: " + AffixValues.tabAffixElementHeight.pxToEm().value + ";")
  }

  private def unsetTabContainerValues: Unit = {
    AffixRequiredElements.tabAffixContainer
      .setAttribute("style", "min-height: 0;")
  }

  private def addGradientClass: Unit = {
    AffixRequiredElements.tabAffixElement.classList.add("ConsultationHeader_DynamicConsultationHeaderStyles_2-gradient")
  }

  private def removeGradientClass: Unit = {
    AffixRequiredElements.tabAffixElement.classList
      .remove("ConsultationHeader_DynamicConsultationHeaderStyles_2-gradient")
  }

  private def enablingSidebarElementAffix: Unit = {
    AffixRequiredElements.sidebarAffixElement
      .setAttribute(
        "style",
        "width: " + AffixValues.specialSidebarWidth
          .pxToEm()
          .value + "; " + "left: " + AffixRequiredElements.sidebarAffixContainerRect.left
          .intValue()
          .pxToEm()
          .value + "; " + "bottom: " + 13.pxToEm().value + "; z-index: 1; position: fixed;"
      )
  }

  private def enablingSpecialSidebarElementAffix: Unit = {
    AffixRequiredElements.sidebarAffixElement
      .setAttribute(
        "style",
        "width: " + AffixValues.specialSidebarWidth
          .pxToEm()
          .value + "; " + "left: " + AffixRequiredElements.sidebarAffixContainerRect.left
          .intValue()
          .pxToEm()
          .value + "; " + "top: " + 10
          .pxToEm()
          .value + "; z-index: 1; position: fixed;"
      )
  }

  private def stoppingSidebarElementAffix: Unit = {
    AffixRequiredElements.sidebarAffixElement
      .setAttribute(
        "style",
        "width: " + AffixValues.specialSidebarWidth
          .pxToEm()
          .value + "; " + "left: " + AffixRequiredElements.sidebarAffixContainerRect.left
          .intValue()
          .pxToEm()
          .value + "; " + "bottom: " + AffixValues.specialSidebarBottom
          .intValue()
          .pxToEm()
          .value + "; z-index: 1; position: fixed;"
      )
  }

  private def disablingSidebarElementAffix: Unit = {
    AffixRequiredElements.sidebarAffixElement
      .setAttribute("style", "width: auto; position: inherit;")
  }

  private def setSidebarContainerValues: Unit = {
    AffixRequiredElements.sidebarAffixContainer
      .setAttribute(
        "style",
        "min-height: " + AffixValues.sidebarAffixElementHeight
          .pxToEm()
          .value + "; " + "width: " + AffixValues.specialSidebarWidth
          .pxToEm()
          .value + ";"
      )
  }

  private def unsetSidebarContainerValues: Unit = {
    AffixRequiredElements.sidebarAffixContainer
      .setAttribute("style", "min-height: 0; width: auto;")
  }

}
