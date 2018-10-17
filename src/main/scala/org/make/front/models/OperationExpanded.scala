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

package org.make.front.models

import org.make.front.components.sequence.Sequence.ExtraSlide
import org.make.front.operations.Operations

import scala.scalajs.js

final case class OperationStaticData(country: String,
                                     slug: String,
                                     wording: js.Array[OperationWording],
                                     color: String,
                                     gradient: Option[GradientColor],
                                     logoUrl: String,
                                     whiteLogoUrl: String,
                                     logoWidth: Int,
                                     shareUrl: String,
                                     extraSlides: (OperationExtraSlidesParams) => js.Array[ExtraSlide],
                                     startDateActions: Option[js.Date] = None,
                                     isConsultationOnly: Boolean = false,
                                     partners: js.Array[OperationPartner] = js.Array())

object OperationStaticData {
  def findBySlugAndCountry(slug: String, country: String): Option[OperationStaticData] = {
    val resultList: js.Array[OperationStaticData] =
      Operations.operationStaticDataList.filter(operation => operation.slug == slug && operation.country == country)

    if (resultList.size == 1) {
      Some(resultList(0))
    } else {
      None
    }
  }
}

final case class OperationPartner(name: String, imageUrl: String, imageWidth: Int, isFounder: Boolean)

final case class OperationWording(language: String,
                                  title: String,
                                  question: String,
                                  learnMoreUrl: Option[String],
                                  presentation: Option[String])

final case class OperationExtraSlidesParams(operation: OperationExpanded,
                                            isConnected: Boolean,
                                            sequence: Sequence,
                                            maybeLocation: Option[Location],
                                            language: String,
                                            country: String,
                                            handleCanUpdate: (Boolean) => Unit,
                                            closeSequence: ()          => Unit)

final case class OperationIntroWording(title: Option[String] = None,
                                       explanation1: Option[String] = None,
                                       explanation2: Option[String] = None,
                                       duration: Option[String] = None,
                                       cta: Option[String] = None)

final case class OperationExpanded(operationId: OperationId,
                                   startDate: Option[js.Date],
                                   defaultLanguage: String,
                                   endDate: Option[js.Date],
                                   country: String,
                                   slug: String,
                                   label: String,
                                   actionsCount: Int,
                                   proposalsCount: Int,
                                   color: String,
                                   gradient: Option[GradientColor],
                                   logoUrl: String,
                                   whiteLogoUrl: String,
                                   logoWidth: Int,
                                   shareUrl: String,
                                   wordings: js.Array[OperationWording],
                                   extraSlides: (OperationExtraSlidesParams) => js.Array[ExtraSlide],
                                   tags: js.Array[Tag],
                                   landingSequenceId: SequenceId,
                                   startDateActions: Option[js.Date],
                                   isConsultationOnly: Boolean,
                                   partners: js.Array[OperationPartner]) {

  def getWordingByLanguage(language: String): Option[OperationWording] = {
    wordings.find(_.language == language)
  }

  def getWordingByLanguageOrError(language: String): OperationWording = {
    getWordingByLanguage(language: String) match {
      case Some(wording) => wording
      case _             => throw new NotImplementedError(s"Wording not found for language $language")
    }
  }

  val isActive: Boolean = {
    (startDate, endDate) match {
      case (Some(start), _) if start.getTime > js.Date.now => false
      case (_, Some(end)) if end.getTime <= js.Date.now    => false
      case _                                               => true
    }
  }

  val isExpired: Boolean = {
    (startDate, endDate) match {
      case (_, Some(end)) if end.getTime <= js.Date.now => true
      case _                                            => false
    }
  }
}

// @todo: use a sealed trait and case object like Source and Location
object OperationExpanded {

  def getOperationExpandedFromOperation(maybeOperation: Option[Operation],
                                        tagsList: js.Array[Tag],
                                        country: String): Option[OperationExpanded] = {
    for {
      operation <- maybeOperation
      countryConfiguration <- operation.countriesConfiguration.filter(_.countryCode == country) match {
        case config if config.size == 1 => Some(config(0))
        case _                          => None
      }
      operationStaticData <- OperationStaticData.findBySlugAndCountry(slug = operation.slug, country = country)
      operationTags <- countryConfiguration.tagIds.flatMap(
        tag => tagsList.filter(tagToFilter => tag.tagId.value == tagToFilter.tagId.value)
      ) match {
        case tags if tags.nonEmpty => Some(tags)
        case _                     => Some(js.Array[Tag]())
      }
    } yield
      OperationExpanded(
        defaultLanguage = operation.defaultLanguage,
        startDate = operation.countriesConfiguration.find(_.countryCode == country).flatMap(_.startDate),
        endDate = operation.countriesConfiguration.find(_.countryCode == country).flatMap(_.endDate),
        country = country,
        operationId = operation.operationId,
        slug = operation.slug,
        label = operation.slug,
        actionsCount = 0,
        proposalsCount = 0,
        color = operationStaticData.color,
        gradient = operationStaticData.gradient,
        logoUrl = operationStaticData.logoUrl,
        whiteLogoUrl = operationStaticData.whiteLogoUrl,
        logoWidth = operationStaticData.logoWidth,
        shareUrl = operationStaticData.shareUrl,
        wordings = operation.translations.flatMap { translation =>
          val language: String = translation.language.toLowerCase
          operationStaticData.wording.find(_.language == language).map { operationWording =>
            OperationWording(
              language = language,
              title = translation.title,
              question = operationWording.question,
              learnMoreUrl = operationWording.learnMoreUrl,
              presentation = operationWording.presentation
            )
          }
        },
        extraSlides = operationStaticData.extraSlides,
        landingSequenceId = countryConfiguration.landingSequenceId,
        tags = operationTags,
        startDateActions = operationStaticData.startDateActions,
        isConsultationOnly = operationStaticData.isConsultationOnly,
        partners = operationStaticData.partners
      )
  }
}
