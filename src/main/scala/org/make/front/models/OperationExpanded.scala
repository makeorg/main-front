package org.make.front.models

import org.make.front.components.sequence.Sequence.ExtraSlide
import org.make.front.operations.Operations

import scala.scalajs.js

final case class OperationDesignData(country: String,
                                     startDate: Option[js.Date],
                                     endDate: Option[js.Date],
                                     slug: String,
                                     wording: Seq[OperationWording],
                                     color: String,
                                     gradient: Option[GradientColor],
                                     logoUrl: Option[String],
                                     logoMaxWidth: Option[Int],
                                     darkerLogoUrl: Option[String],
                                     greatCauseLabelAlignment: Option[String],
                                     featuredIllustration: Option[Illustration],
                                     illustration: Option[Illustration],
                                     partners: Seq[OperationPartner],
                                     extraSlides: (OperationExtraSlidesParams) => Seq[ExtraSlide])

object OperationDesignData {
  def findBySlugAndCountry(slug: String, country: String): Option[OperationDesignData] = {
    val resultList: Seq[OperationDesignData] =
      Operations.operationDesignList.filter(operation => operation.slug == slug && operation.country == country)
    resultList match {
      case Seq(operation) => Some(operation)
      case _              => None
    }
  }
}

final case class OperationPartner(name: String, imageUrl: String, imageWidth: Int)

final case class OperationWording(title: String,
                                  question: String,
                                  language: String,
                                  label: Option[String],
                                  purpose: Option[String],
                                  period: Option[String],
                                  mentionUnderThePartners: Option[String],
                                  explanation: Option[String],
                                  learnMoreUrl: Option[String])

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
                                   illustration: Option[Illustration],
                                   featuredIllustration: Option[Illustration],
                                   logoUrl: Option[String],
                                   logoMaxWidth: Option[Int],
                                   darkerLogoUrl: Option[String],
                                   greatCauseLabelAlignment: Option[String],
                                   wordings: Seq[OperationWording],
                                   partners: Seq[OperationPartner],
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
      operationDesignData <- OperationDesignData.findBySlugAndCountry(slug = operation.slug, country = country)
      operationTags <- countryConfiguration.tagIds.flatMap(
        tag => tagsList.filter(tagToFilter => tag.tagId.value == tagToFilter.tagId.value && !tagToFilter.label.contains(":"))
      ) match {
        case tags if tags.nonEmpty => Some(tags)
        case _                     => None
      }
    } yield
      OperationExpanded(
        defaultLanguage = operation.defaultLanguage,
        startDate = operationDesignData.startDate,
        endDate = operationDesignData.endDate,
        country = country,
        operationId = operation.operationId,
        slug = operation.slug,
        label = operation.slug,
        actionsCount = 0,
        proposalsCount = 0,
        color = operationDesignData.color,
        gradient = operationDesignData.gradient,
        logoUrl = operationDesignData.logoUrl,
        logoMaxWidth = operationDesignData.logoMaxWidth,
        darkerLogoUrl = operationDesignData.darkerLogoUrl,
        greatCauseLabelAlignment = operationDesignData.greatCauseLabelAlignment,
        wordings = operation.translations.flatMap { translation =>
          val language: String = translation.language.toLowerCase
          operationDesignData.wording.find(_.language == language).map { operationWording =>
            OperationWording(
              language = language,
              title = translation.title,
              question = operationWording.question,
              label = operationWording.label,
              purpose = operationWording.purpose,
              period = operationWording.period,
              mentionUnderThePartners = operationWording.mentionUnderThePartners,
              explanation = operationWording.explanation,
              learnMoreUrl = operationWording.learnMoreUrl
            )
          }
        },
        partners = operationDesignData.partners,
        illustration = operationDesignData.illustration,
        featuredIllustration = operationDesignData.featuredIllustration,
        extraSlides = operationDesignData.extraSlides,
        landingSequenceId = countryConfiguration.landingSequenceId,
        tagIds = operationTags
      )
  }
}
