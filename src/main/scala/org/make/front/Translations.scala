package org.make.front

import org.make.front.facades.I18n.setTranslations

import scala.scalajs.js.JSON
import scala.scalajs.js

object Translations {

  def loadTranslations(): Unit = {
    setTranslations(JSON.parse(translations).asInstanceOf[js.Object])
  }

  val translations: String =
    """
      |{
      |  "fr": {
      |    "errors": {
      |       "main": "Une erreur est survenue",
      |       "noToken": "Token absent",
      |       "loginFailed": "La connexion a échoué",
      |       "apiFailure": "Échec de l'appel API",
      |       "unexpectedBehaviour": "Comportement innatendu",
      |       "tryAgain": "Veuillez réessayer"
      |    },
      |    "content": {
      |       "header": {
      |         "profile": "Mon Profil",
      |         "settings": "Paramètres",
      |         "logout": "Se Déconnecter",
      |         "connect": "Se Connecter",
      |         "createAccount": "Créer un compte",
      |         "searchPlaceholder": "Chercher des propositions"
      |       },
      |       "homepage": {
      |         "baseline": "À la une",
      |         "title": "Qui sommes-nous ?",
      |         "subTitle": "Make.org est une initiative neture et indépendante",
      |         "textSeeMore": "En Savoir +"
      |       },
      |       "footer": {
      |         "jobs": "jobs",
      |         "press": "Espace presse",
      |         "terms": "conditions d'utilisation",
      |         "contact": "contact",
      |         "faq": "f.a.q.",
      |         "sitemap": "sitemap",
      |         "recruitment": "Devenez Maker!",
      |         "title": "Tous les thèmes"
      |       },
      |       "theme": {
      |         "actionsCount": "%{actions} actions en cours",
      |         "proposalsCount": "%{proposals} propositions",
      |         "moreInfos": "En savoir +",
      |         "matrix": {
      |           "title": "votez sur les propositions",
      |           "filter": {
      |             "tag": {
      |               "title": "Tags populaire :"
      |             }
      |           }
      |         }
      |       },
      |       "tag": {
      |         "showMore": "affichier tous les tags",
      |         "showLess": "masquer les tags supplémentaires",
      |         "proposalsCount": "%{proposals} propositions",
      |         "moreInfos": "En savoir +"
      |       },
      |       "proposal": {
      |         "agree": "D'accord",
      |         "disagree": "Pas d'accord",
      |         "blank": "Vote Blanc",
      |         "likeIt": "Coup de <span class=\"spanHeart icon is-small\"><i class=\"fa fa-heart\"></i></span>",
      |         "doable": "Réaliste",
      |         "platitudeAgree": "Platitude",
      |         "noWay": "Surtout pas !",
      |         "impossible": "Infaisable",
      |         "platitudeDisagree": "Platitide",
      |         "doNotUnderstand": "Pas compris",
      |         "noOpinion": "Pas d'avis",
      |         "doNotCare": "Je m'en moque",
      |         "fullHeader": "%{firstName}, %{age} ans (%{postalCode})",
      |         "ageHeader": "%{firstName}, %{age} ans",
      |         "postalCodeHeader": "%{firstName} (%{postalCode})",
      |         "tinyHeader": "%{firstName}",
      |         "plusOne": "+1",
      |         "anonymous": "anonyme"
      |       }
      |    },
      |    "form": {
      |       "required": "(obligatoire)",
      |       "connection": "Connexion",
      |       "or": "ou",
      |       "fieldLabelEmail": "e-mail",
      |       "fieldLabelPassword": "Mot de passe",
      |       "fieldLabelFirstName": "Prénom",
      |       "login": {
      |         "close": "Fermer",
      |         "socialConnect": "Je me connecte avec",
      |         "socialRegister": "Je m'inscris avec",
      |         "stdConnect": "Je me connecte avec mon adresse e-mail",
      |         "stdRegister": "Je m’inscris avec ce formulaire",
      |         "submitButton": "se connecter",
      |         "oupsI": "Oups, j'ai ",
      |         "forgotPassword": "oublié mon mote de passe ?",
      |         "noAccount": "Je n'ai pas de compte, ",
      |         "createAccount": "je m'en créé un.",
      |         "errorAuthenticationFailed": "Erreur d'authentification"
      |       },
      |       "register": {
      |          "withSocial": "Je m'inscris avec",
      |          "noPublishedContent": "(Nous n’y posterons jamais en votre nom)",
      |          "withForm": "Je m'inscris avec ce formulaire",
      |          "termsAgreed": "En vous inscrivant, vous acceptez nos conditions générales d’utilisation et acceptez de recevoir des e-mails (peu nombreux) de Make.org.",
      |          "subscribe": "S’inscrire",
      |          "alreadySubscribed": "J’ai déjà un compte ! ",
      |          "noRegister": "Non merci, je souhaite poursuivre sans compte finalement."
      |       }
      |    }
      |  },
      |  "en": {
      |    "errors": {
      |       "main": "An error occured",
      |       "noToken": "No token was provided",
      |       "loginFailed": "Login Failed",
      |       "apiFailure": "API call failed",
      |       "unexpectedBehaviour": "Unexpected behaviour",
      |       "tryAgain": "Please try again"
      |    },
      |    "content": {
      |       "header": {
      |         "profile": "My Profile",
      |         "settings": "Settings",
      |         "logout": "Logout",
      |         "connect": "Log In",
      |         "createAccount": "Create account",
      |         "searchPlaceholder": "Lookup proposals"
      |       },
      |       "homepage": {
      |         "baseline": "À la une",
      |         "title": "Qui sommes-nous ?",
      |         "subTitle": "Make.org est une initiative neture et indépendante",
      |         "textSeeMore": "En Savoir +"
      |       },
      |       "footer": {
      |         "jobs": "jobs",
      |         "press": "Espace presse",
      |         "terms": "conditions d'utilisation",
      |         "contact": "contact",
      |         "faq": "f.a.q.",
      |         "sitemap": "sitemap",
      |         "recruitment": "Devenez Maker!",
      |         "title": "Tous les thèmes"
      |       },
      |       "theme": {
      |         "actionsCount": "%{actions} actions en cours",
      |         "proposalsCount": "%{proposals} propositions",
      |         "moreInfos": "En savoir +",
      |         "matrix": {
      |           "title": "votez sur les propositions",
      |           "filter": {
      |             "tag": {
      |               "title": "Tags populaire :"
      |             }
      |           }
      |         }
      |       },
      |       "tag": {
      |         "showMore": "show all tags",
      |         "showLess": "hide additional tags",
      |         "proposalsCount": "%{proposals} propositions",
      |         "moreInfos": "En savoir +"
      |       },
      |       "proposal": {
      |         "agree": "Agree",
      |         "disagree": "Disagree",
      |         "blank": "Blank Vote",
      |         "likeIt": "I <span class=\"spanHeart icon is-small\"><i class=\"fa fa-heart\"></i></span> It",
      |         "doable": "Doable",
      |         "platitudeAgree": "Platitude",
      |         "noWay": "No way !",
      |         "impossible": "Impossible",
      |         "platitudeDisagree": "Platitude",
      |         "doNotUnderstand": "Did not understand",
      |         "noOpinion": "No opinion",
      |         "doNotCare": "Don't care",
      |         "fullHeader": "%{firstName}, %{age} yo (%{postalCode})",
      |         "ageHeader": "%{firstName}, %{age} yo",
      |         "postalCodeHeader": "%{firstName} (%{postalCode})",
      |         "tinyHeader": "%{firstName}",
      |         "plusOne": "+1",
      |         "anonymous": "anonymous"
      |       }
      |    },
      |    "form": {
      |       "required": "(obligatoire)",
      |       "connection": "Connexion",
      |       "or": "ou",
      |       "fieldLabelEmail": "e-mail",
      |       "fieldLabelPassword": "Mot de passe",
      |       "fieldLabelFirstName": "Prénom",
      |       "login": {
      |         "close": "Fermer",
      |         "socialConnect": "Je me connecte avec",
      |         "socialRegister": "Je m'inscris avec",
      |         "stdConnect": "Je me connecte avec mon adresse e-mail",
      |         "stdRegister": "Je m’inscris avec ce formulaire",
      |         "submitButton": "se connecter",
      |         "oupsI": "Oups, j'ai ",
      |         "forgotPassword": "oublié mon mote de passe ?",
      |         "noAccount": "Je n'ai pas de compte, ",
      |         "createAccount": "je m'en créé un.",
      |         "errorAuthenticationFailed": "Erreur d'authentification"
      |       },
      |       "register": {
      |          "withSocial": "Je m'inscris avec",
      |          "noPublishedContent": "(Nous n’y posterons jamais en votre nom)",
      |          "withForm": "Je m'inscris avec ce formulaire",
      |          "termsAgreed": "En vous inscrivant, vous acceptez nos conditions générales d’utilisation et acceptez de recevoir des e-mails (peu nombreux) de Make.org.",
      |          "subscribe": "S’inscrire",
      |          "alreadySubscribed": "J’ai déjà un compte ! ",
      |          "noRegister": "Non merci, je souhaite poursuivre sans compte finalement."
      |       }
      |    }
      |  }
      |}
    """.stripMargin

}
