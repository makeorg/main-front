package org.make.front.models

import org.make.front.components.sequence.Sequence.ExtraSlide
import org.make.front.operations.Operations

import scala.scalajs.js

final case class OperationStaticData(country: String,
                                     slug: String,
                                     wording: Seq[OperationWording],
                                     color: String,
                                     gradient: Option[GradientColor],
                                     logoUrl: String,
                                     whiteLogoUrl: String,
                                     shareUrl: String,
                                     extraSlides: (OperationExtraSlidesParams) => Seq[ExtraSlide])

object OperationStaticData {
  def findBySlugAndCountry(slug: String, country: String): Option[OperationStaticData] = {
    val resultList: Seq[OperationStaticData] =
      Operations.operationStaticDataList.filter(operation => operation.slug == slug && operation.country == country)
    resultList match {
      case Seq(operation) => Some(operation)
      case _              => None
    }
  }
}

final case class OperationPartner(name: String, imageUrl: String, imageWidth: Int)

final case class OperationWording(language: String,
                                  title: String,
                                  question: String,
                                  learnMoreUrl: Option[String] = None)

final case class OperationExtraSlidesParams(operation: OperationExpanded,
                                            isConnected: Boolean,
                                            sequence: Sequence,
                                            maybeLocation: Option[Location],
                                            language: String,
                                            country: String)

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
                                   shareUrl: String,
                                   wordings: Seq[OperationWording],
                                   extraSlides: (OperationExtraSlidesParams) => Seq[ExtraSlide],
                                   tagIds: Seq[Tag],
                                   landingSequenceId: SequenceId) {

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
                                        tagsList: Seq[Tag],
                                        country: String): Option[OperationExpanded] = {
    for {
      operation <- maybeOperation
      countryConfiguration <- operation.countriesConfiguration.filter(_.countryCode == country) match {
        case Seq(countryConfig) => Some(countryConfig)
        case _                  => None
      }
      operationStaticData <- OperationStaticData.findBySlugAndCountry(slug = operation.slug, country = country)
      operationTags <- countryConfiguration.tagIds.flatMap(
        tag =>
          tagsList.filter(tagToFilter => tag.tagId.value == tagToFilter.tagId.value && !tagToFilter.label.contains(":"))
      ) match {
        case tags if tags.nonEmpty => Some(tags)
        case _                     => None
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
        shareUrl = operationStaticData.shareUrl,
        wordings = operation.translations.flatMap { translation =>
          val language: String = translation.language.toLowerCase
          operationStaticData.wording.find(_.language == language).map { operationWording =>
            OperationWording(
              language = language,
              title = translation.title,
              question = operationWording.question,
              learnMoreUrl = operationWording.learnMoreUrl
            )
          }
        },
        extraSlides = operationStaticData.extraSlides,
        landingSequenceId = countryConfiguration.landingSequenceId,
        tagIds = operationTags
      )
  }
}
