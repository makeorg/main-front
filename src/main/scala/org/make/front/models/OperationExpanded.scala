package org.make.front.models

import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.components.AppState
import org.make.front.facades._
import org.make.front.models.{Sequence => SequenceModel}

final case class OperationExpanded(operationId: OperationId,
                                   url: String,
                                   slug: String,
                                   title: String,
                                   question: String,
                                   label: String,
                                   actionsCount: Int,
                                   proposalsCount: Int,
                                   color: String,
                                   gradient: Option[GradientColor] = None,
                                   tags: Seq[Tag] = Seq.empty,
                                   logoUrl: Option[String] = None,
                                   darkerLogoUrl: Option[String] = None,
                                   sequence: Option[SequenceModel] = None,
                                   wording: OperationWording,
                                   theme: OperationTheme)

final case class OperationPartner(name: String, imageUrl: String, imageWidth: Int)

final case class OperationWording(title: String,
                                  question: String,
                                  greatCauseLabel: Option[String] = None,
                                  purpose: Option[String] = None,
                                  period: Option[String] = None,
                                  featuredIll: Option[FeaturedIll] = None,
                                  explanationIll: Option[ExplanationIll] = None,
                                  explanation: Option[String] = None,
                                  learnMoreUrl: Option[String] = None,
                                  partners: Seq[OperationPartner] = Seq.empty)

final case class ExplanationIll(illUrl: String, ill2xUrl: String)

final case class FeaturedIll(smallIllUrl: String,
                             smallIll2xUrl: String,
                             mediumIllUrl: String,
                             mediumIll2xUrl: String,
                             illUrl: String,
                             ill2xUrl: String)

final case class OperationTheme(color: String,
                                gradient: Option[GradientColor] = None,
                                logoUrl: Option[String] = None,
                                darkerLogoUrl: Option[String] = None)

// @todo: use a sealaed trait and case object like Source and Location
object OperationExpanded {
  val vff: String = "vff"

  def getOperationById(id: String, state: AppState): Option[OperationExpanded] = {
    state.operations.find(operation => operation.operationId.value == id)
  }

  val empty = OperationExpanded(
    OperationId("fake"),
    "",
    "",
    "",
    "",
    "",
    0,
    0,
    "",
    None,
    Seq.empty,
    None,
    None,
    None,
    OperationWording("", ""),
    OperationTheme("")
  )

  val defaultOperations = Seq(
    OperationExpanded(
      operationId = OperationId(OperationExpanded.vff),
      url = "consultation/vff/selection",
      slug = "vff",
      title = "Stop aux violences faites aux&nbsp;femmes",
      question = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
      label = OperationExpanded.vff,
      actionsCount = 0,
      proposalsCount = 0,
      color = "#660779",
      gradient = Some(GradientColor("#AB92CA", "#54325A")),
      logoUrl = Some(VFFLogo.toString),
      darkerLogoUrl = Some(VFFDarkerLogo.toString),
      sequence = Some(
        SequenceModel(
          sequenceId = SequenceId("1"),
          slug = "comment-lutter-contre-les-violences-faites-aux-femmes",
          title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?"
        )
      ),
      wording = OperationWording(
        title = "Stop aux Violences Faites aux&nbsp;Femmes",
        question = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
        purpose = Some(
          "Make.org a décidé de lancer sa première Grande Cause en la consacrant à la lutte contre les Violences faites aux&nbsp;femmes."
        ),
        period = Some("Consultation ouverte du 25 nov. 2017 à fin janvier"),
        featuredIll = Some(
          FeaturedIll(
            smallIllUrl = featuredVFFSmall.toString,
            smallIll2xUrl = featuredVFFSmall2x.toString,
            mediumIllUrl = featuredVFFMedium.toString,
            mediumIll2xUrl = featuredVFFMedium2x.toString,
            illUrl = featuredVFF.toString,
            ill2xUrl = featuredVFF2x.toString
          )
        ),
        explanationIll = Some(ExplanationIll(illUrl = VFFIll.toString, ill2xUrl = VFFIll2x.toString)),
        explanation = Some(
          "Les violences faites aux femmes sont au coeur de l’actualité politique et médiatique. Les mentalités sont en train de changer. Mais pour autant tout commence maintenant. À nous de transformer cette prise de conscience généralisée en actions concrètes et d’apporter une réponse décisive face à ce&nbsp;fléau."
        ),
        learnMoreUrl = Some("https://stopvff.make.org/about-vff"),
        partners = Seq(
          OperationPartner(name = "Kering Foundation", imageUrl = keringFoundationLogo.toString, imageWidth = 80),
          OperationPartner(name = "Facebook", imageUrl = facebookLogo.toString, imageWidth = 80)
        )
      ),
      theme = OperationTheme(
        color = "#660779",
        gradient = Some(GradientColor("#AB92CA", "#54325A")),
        logoUrl = Some(VFFLogo.toString),
        darkerLogoUrl = Some(VFFDarkerLogo.toString)
      )
    )
  )
}
/*
import org.make.front.components.AppState
import org.make.front.facades._
import org.make.front.models.{Sequence => SequenceModel}

import scala.scalajs.js

final case class Operation(operationId: OperationId,
                           label: String,
                           slug: String,
                           wording: OperationWording,
                           theme: OperationTheme,
                           sequence: Option[SequenceModel] = None,
                           tags: Seq[Tag] = Seq.empty)

final case class OperationPartner(name: String, imageUrl: String, imageWidth: Int)

final case class OperationWording(title: String,
                                  question: String,
                                  greatCauseLabel: Option[String] = None,
                                  purpose: Option[String] = None,
                                  period: Option[String] = None,
                                  featuredIll: Option[FeaturedIll] = None,
                                  explanationIll: Option[ExplanationIll] = None,
                                  explanation: Option[String] = None,
                                  learnMoreUrl: Option[String] = None,
                                  partners: Seq[OperationPartner] = Seq.empty)

final case class ExplanationIll(illUrl: String, ill2xUrl: String)

final case class FeaturedIll(smallIllUrl: String,
                             smallIll2xUrl: String,
                             mediumIllUrl: String,
                             mediumIll2xUrl: String,
                             illUrl: String,
                             ill2xUrl: String)

final case class OperationTheme(color: String,
                                gradient: Option[GradientColor] = None,
                                logoUrl: Option[String] = None,
                                darkerLogoUrl: Option[String] = None)
 */
/*
  val empty = Operation(OperationId("fake"), "", "", OperationWording("", ""), OperationTheme(""), None)

  val defaultOperations = Seq(
    Operation(
      operationId = OperationId(Operation.vff),
      label = Operation.vff,
      slug = "vff",
      wording = OperationWording(
        title = "Stop aux Violences Faites aux&nbsp;Femmes",
        question = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
        greatCauseLabel = Some("Grande cause Make.org"),
        purpose = Some(
          "Make.org a décidé de lancer sa première Grande Cause en la consacrant à la lutte contre les Violences faites aux&nbsp;femmes."
        ),
        period = Some("Consultation ouverte du 25 nov. 2017 à fin janvier"),
        featuredIll = Some(
          FeaturedIll(
            smallIllUrl = featuredVFFSmall.toString,
            smallIll2xUrl = featuredVFFSmall2x.toString,
            mediumIllUrl = featuredVFFMedium.toString,
            mediumIll2xUrl = featuredVFFMedium2x.toString,
            illUrl = featuredVFF.toString,
            ill2xUrl = featuredVFF2x.toString
          )
        ),
        explanationIll = Some(ExplanationIll(illUrl = VFFIll.toString, ill2xUrl = VFFIll2x.toString)),
        explanation = Some(
          "Les violences faites aux femmes sont au coeur de l’actualité politique et médiatique. Les mentalités sont en train de changer. Mais pour autant tout commence maintenant. À nous de transformer cette prise de conscience généralisée en actions concrètes et d’apporter une réponse décisive face à ce&nbsp;fléau."
        ),
        learnMoreUrl = Some("https://stopvff.make.org/about-vff"),
        partners = Seq(
          OperationPartner(name = "Kering Foundation", imageUrl = keringFoundationLogo.toString, imageWidth = 80),
          OperationPartner(name = "Facebook", imageUrl = facebookLogo.toString, imageWidth = 80)
        )
      ),
      theme = OperationTheme(
        color = "#660779",
        gradient = Some(GradientColor("#AB92CA", "#54325A")),
        logoUrl = Some(VFFLogo.toString),
        darkerLogoUrl = Some(VFFDarkerLogo.toString)
      ),
      sequence = Some(
        SequenceModel(
          sequenceId = SequenceId("1"),
          slug = "comment-lutter-contre-les-violences-faites-aux-femmes",
          title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?"
        )
      )
    ),
    Operation(
      operationId = OperationId(Operation.climatparis),
      label = Operation.climatparis,
      slug = "climat-paris",
      wording = OperationWording(
        title = "Climat Paris",
        period = Some("Consultation ouverte du &hellip; au &hellip;"),
        explanationIll = Some(ExplanationIll(illUrl = climatParisIll.toString, ill2xUrl = climatParisIll2x.toString)),
        explanation = Some(
          "Suspendisse placerat magna justo, sit amet efficitur lacus fringilla in. Aliquam lobortis pretium ex id ullamcorper. Interdum et malesuada fames ac ante ipsum primis in faucibus. Fusce fermentum hendrerit quam sit amet tincidunt. Duis laoreet elit."
        ),
        learnMoreUrl = Some("https://about.make.org/about-climatparis"),
        question = "Comment lutter contre le changement climatique à Paris&nbsp;?",
        partners = Seq.empty
      ),
      theme = OperationTheme(
        color = "#459ba6",
        gradient = Some(GradientColor("#bfe692", "#69afde")),
        logoUrl = Some(climatParisLogo.toString),
        darkerLogoUrl = Some(ClimatParisDarkerLogo.toString)
      ),
      sequence = Some(
        SequenceModel(
          sequenceId = SequenceId("2"),
          slug = "comment-lutter-contre-le-changement-climatique-a-paris",
          title = "Comment lutter contre le changement climatique à Paris&nbsp;?"
        )
      )
 */
