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
      |       "unexpectedBehaviour": "Comportement inattendu",
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
      |           "noContent": "Nous n’avons trouvé <strong>aucun résultat</strong> correspondant à votre sélection de tag(s).",
      |           "selectOtherTags": "Vous pouvez sélectionner d’autres combinaisons de tags.",
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
      |         "anonymous": "anonyme",
      |         "confirmationThanks": "Merci !",
      |         "confirmationButtonBackTheme": "Retourner sur le thème %{theme}",
      |         "confirmationButtonAnotherProposal": "Faire une nouvelle proposition",
      |         "confirmationContent": "Votre proposition a été déposée. Elle va maintenant être relue par notre service modération. <br><br>Vous recevrez un e-mail quand elle aura été validée !",
      |         "isTooLong": "Oups ! Vous avez dépassé la limite de %{max} caractères 😅",
      |         "isTooShort": "Oups ! Votre proposition doit contenir au minimum %{min} caractères 😅",
      |         "titleIntro": "Ajoutez votre proposition au thème",
      |         "help": "Ne vous inquiétez pas, nous corrigerons vos éventuelles fautes d'orthographe.",
      |         "subHelp": "Pour en savoir plus sur notre charte de modération, cliquez ici."
      |       }
      |    },
      |    "form": {
      |       "required": "(obligatoire)",
      |       "connection": "Connexion",
      |       "or": "ou",
      |       "fieldLabelEmail": "e-mail",
      |       "fieldLabelPassword": "Mot de passe",
      |       "fieldLabelFirstName": "Prénom",
      |       "fieldLabelAge": "Âge",
      |       "fieldPostalCode": "Code postal",
      |       "fieldProfession": "Profession",
      |       "login": {
      |         "close": "Fermer",
      |         "socialConnect": "Je me connecte avec",
      |         "socialRegister": "Je m'inscris avec",
      |         "stdConnect": "Je me connecte avec mon adresse e-mail",
      |         "stdRegister": "Je m’inscris avec ce formulaire",
      |         "submitButton": "se connecter",
      |         "oupsI": "Oups, j'ai ",
      |         "forgotPassword": "oublié mon mot de passe ?",
      |         "noAccount": "Je n'ai pas de compte, ",
      |         "createAccount": "je m'en créé un.",
      |         "errorAuthenticationFailed": "Erreur d'authentification",
      |         "proposalIntroFirst": "Nous avons besoin de quelques informations",
      |         "proposalIntroSecond": "Pour valider votre proposition"
      |       },
      |       "register": {
      |          "withSocial": "Je m'inscris avec",
      |          "noPublishedContent": "(Nous n’y posterons jamais en votre nom)",
      |          "withForm": "Je m'inscris avec ce formulaire",
      |          "termsAgreed": "En vous inscrivant, vous acceptez nos conditions générales d’utilisation et acceptez de recevoir des e-mails (peu nombreux) de Make.org.",
      |          "subscribe": "S’inscrire",
      |          "alreadySubscribed": "J’ai déjà un compte ! ",
      |          "noRegister": "Non merci, je souhaite poursuivre %{break} sans compte finalement.",
      |          "errorRegistrationFailed": "Erreur d'enregistrement",
      |          "errorInvalidEmail": "Un email valide est requis",
      |          "errorMinPassword": "Le mot de passe doit comporter au moins %{min} caractères",
      |          "errorChoiceAge": "Valeur invalide",
      |          "errorMaxPostalCode": "Le code postal ne doit pas dépasser %{max} caractères",
      |          "errorMinFirstName": "Le prénom est requis",
      |          "errorAlreadyExist": "Cet email est déjà enregistré"
      |       },
      |       "proposal": {
      |         "submit": "Proposer"
      |       },
      |       "passwordRecovery": {
      |         "title": "je réinitialise mon mot de passe",
      |         "description": "Merci de renseigner l'adresse email liée à votre compte pour recevoir le lien de réinitialisation.",
      |         "fieldLabelEmail": "votre adresse email",
      |         "sendEmail": "Envoyer",
      |         "invalidEmail": "veuillez rentrer un e-mail valide",
      |         "emailDoesNotExist": "L'e-mail que vous avez rentré n'existe pas",
      |         "return": "Revenir à ",
      |         "connectScreen": "l'écran de connexion",
      |         "notification": {
      |           "message" : "Merci, un email vient de vous être envoyé pour vous permettre de mettre à jour votre mot de passe."
      |         }
      |       },
      |       "passwordReset": {
      |         "title": "Je crée un nouveau mot de passe",
      |         "description": "Vous pouvez choisir un nouveau mot de passe.",
      |         "validation": "Valider",
      |         "success": {
      |           "title": "merci, votre mot de passe a bien été changé.",
      |           "description": "Vous pouvez vous connecter dès maintenant."
      |         },
      |         "failed" : {
      |           "title": "Votre jeton est invalide!"
      |         }
      |       }
      |     },
      |     "modal": {
      |       "close": "Fermer"
      |     }
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
      |           "noContent": "Nous n’avons trouvé <strong>aucun résultat</strong> correspondant à votre sélection de tag(s).",
      |           "selectOtherTags": "Vous pouvez sélectionner d’autres combinaisons de tags.",
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
      |         "anonymous": "anonymous",
      |         "confirmationThanks": "Merci !",
      |         "confirmationButtonBackTheme": "Retourner sur le thème %{theme}",
      |         "confirmationButtonAnotherProposal": "Faire une nouvelle proposition",
      |         "confirmationContent": "Votre proposition a été déposée. Elle va maintenant être relue par notre service modération. <br><br>Vous recevrez un e-mail quand elle aura été validée !",
      |         "isTooLong": "Oups ! Vous avez dépassé la limite de %{max} caractères 😅",
      |         "isTooShort": "Oups ! Votre proposition doit contenir au minimum %{min} caractères 😅",
      |         "titleIntro": "Ajoutez votre proposition au thème",
      |         "help": "Ne vous inquiétez pas, nous corrigerons vos éventuelles fautes d'orthographe.",
      |         "subHelp": "Pour en savoir plus sur notre charte de modération, cliquez ici."
      |       }
      |    },
      |    "form": {
      |       "required": "(obligatoire)",
      |       "connection": "Connexion",
      |       "or": "ou",
      |       "fieldLabelEmail": "e-mail",
      |       "fieldLabelPassword": "Mot de passe",
      |       "fieldLabelFirstName": "Prénom",
      |       "fieldLabelAge": "Âge",
      |       "fieldPostalCode": "Code postal",
      |       "fieldProfession": "Profession",
      |       "login": {
      |         "close": "Fermer",
      |         "socialConnect": "Je me connecte avec",
      |         "socialRegister": "Je m'inscris avec",
      |         "stdConnect": "Je me connecte avec mon adresse e-mail",
      |         "stdRegister": "Je m’inscris avec ce formulaire",
      |         "submitButton": "se connecter",
      |         "oupsI": "Oups, j'ai ",
      |         "forgotPassword": "oublié mon mot de passe ?",
      |         "noAccount": "Je n'ai pas de compte, ",
      |         "createAccount": "je m'en créé un.",
      |         "errorAuthenticationFailed": "Erreur d'authentification",
      |         "proposalIntroFirst": "Nous avons besoin de quelques informations",
      |         "proposalIntroSecond": "Pour valider votre proposition"
      |       },
      |       "register": {
      |          "withSocial": "Je m'inscris avec",
      |          "noPublishedContent": "(Nous n’y posterons jamais en votre nom)",
      |          "withForm": "Je m'inscris avec ce formulaire",
      |          "termsAgreed": "En vous inscrivant, vous acceptez nos conditions générales d’utilisation et acceptez de recevoir des e-mails (peu nombreux) de Make.org.",
      |          "subscribe": "S’inscrire",
      |          "alreadySubscribed": "J’ai déjà un compte ! ",
      |          "noRegister": "Non merci, je souhaite poursuivre %{break} sans compte finalement.",
      |          "errorRegistrationFailed": "Erreur d'enregistrement",
      |          "errorInvalidEmail": "Un email valide est requis",
      |          "errorMinPassword": "Le mot de passe doit comporter au moins %{min} caractères",
      |          "errorChoiceAge": "Valeur invalide",
      |          "errorMaxPostalCode": "Le code postal ne doit pas dépasser %{max} caractères",
      |          "errorMinFirstName": "Le prénom est requis",
      |          "errorAlreadyExist": "Cet email est déjà enregistré"
      |       },
      |       "proposal": {
      |         "submit": "Proposer"
      |       },
      |       "passwordRecovery": {
      |         "title": "je réinitialise mon mot de passe",
      |         "description": "Merci de renseigner l'adresse email liée à votre compte pour recevoir le lien de réinitialisation.",
      |         "fieldLabelEmail": "votre adresse email",
      |         "sendEmail": "Envoyer",
      |         "invalidEmail": "veuillez rentrer un e-mail valide",
      |         "emailDoesNotExist": "L'e-mail que vous avez rentré n'existe pas",
      |         "return": "Revenir à ",
      |         "connectScreen": "l'écran de connexion",
      |         "notification": {
      |           "message" : "Merci, un email vient de vous être envoyé pour vous permettre de mettre à jour votre mot de passe."
      |         }
      |       },
      |       "passwordReset": {
      |         "title": "Je crée un nouveau mot de passe",
      |         "description": "Vous pouvez choisir un nouveau mot de passe.",
      |         "validation": "Valider",
      |         "success": {
      |           "title": "merci, votre mot de passe a bien été changé.",
      |           "description": "Vous pouvez vous connecter dès maintenant."
      |         },
      |         "failed" : {
      |           "title": "Votre jeton est invalide!"
      |         }
      |       }
      |     },
      |     "modal": {
      |       "close": "Fermer"
      |     }
      |  }
      |}
    """.stripMargin

}
