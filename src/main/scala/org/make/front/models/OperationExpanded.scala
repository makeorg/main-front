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
  val defaultUrl = "consultation/{slug}/selection"

  def findBySlugAndCountry(slug: String, country: String): Option[OperationDesignData] = {
    val resultList: Seq[OperationDesignData] =
      Operations.operationDesignList.filter(operation => operation.slug == slug && operation.country == country)
    resultList match {
      case operations if operations.nonEmpty => Some(operations.head)
      case _                                 => None
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
                                            language: String)

final case class OperationExpanded(operationId: OperationId,
                                   startDate: Option[js.Date],
                                   endDate: Option[js.Date],
                                   country: String,
                                   url: String,
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
    wordings.filter(_.language == language) match {
      case filteredWordings if filteredWordings.nonEmpty => Some(filteredWordings.head)
      case _                                             => None
    }
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
                                        country: String): Option[OperationExpanded] = {
    maybeOperation.flatMap { operation =>
      val maybeOperationDesignData: Option[OperationDesignData] =
        OperationDesignData.findBySlugAndCountry(slug = operation.slug, country = country)
      val countryConfiguration: OperationCountryConfiguration =
        operation.countriesConfiguration.filter(_.countryCode == country).head

      maybeOperationDesignData match {
        case Some(operationDesignData) =>
          Some(
            OperationExpanded(
              startDate = operationDesignData.startDate,
              endDate = operationDesignData.endDate,
              country = country,
              operationId = operation.operationId,
              url = OperationDesignData.defaultUrl.replace("{slug}", operation.slug),
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
              wordings = operation.translations.map { translation =>
                val language: String = translation.language.toLowerCase
                val operationWording: OperationWording = operationDesignData.wording.filter(_.language == language).head
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
              },
              partners = operationDesignData.partners,
              illustration = operationDesignData.illustration,
              featuredIllustration = operationDesignData.featuredIllustration,
              extraSlides = operationDesignData.extraSlides,
              landingSequenceId = countryConfiguration.landingSequenceId,
              tagIds = countryConfiguration.tagIds
            )
          )
        case _ => None
      }
    }
  }
}
