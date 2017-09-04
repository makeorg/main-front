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
      |       "unexpectedBehaviour": "Quelque chose n'a pas fonctionné",
      |       "tryAgain": "Si le problème persiste n'hésitez pas à nous contacter à <a href=\"mailto:contact@make.org\">contact@make.org</a>."
      |    },
      |    "content": {
      |       "header": {
      |         "profile": "Mon Profil",
      |         "settings": "Paramètres",
      |         "logout": "Se Déconnecter",
      |         "connect": "Se connecter",
      |         "createAccount": "S'inscrire",
      |         "searchPlaceholder": "Chercher des propositions"
      |       },
      |       "homepage": {
      |         "baseline": "À la une",
      |         "title": "Qui sommes-nous ?",
      |         "subTitle": "Make.org est une initiative neture et indépendante",
      |         "textSeeMore": "En Savoir +",
      |         "expressYourself": "Exprimez-vous !",
      |         "mostPopular": "Les propositions les + populaires sur Make.org"
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
      |         "likeIt": "Coup de <i class=\"fa fa-heart\"></i>",
      |         "doable": "Réaliste",
      |         "platitudeAgree": "Platitude",
      |         "noWay": "Surtout pas !",
      |         "impossible": "Infaisable",
      |         "platitudeDisagree": "Platitude",
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
      |         "confirmationButtonBackTheme": "Retour au thème %{theme}",
      |         "confirmationButtonAnotherProposal": "Faire une nouvelle proposition",
      |         "confirmationContent": "Votre proposition a bien été prise en compte, elle va maintenant être relue par notre service modération. Vous recevrez un email lorsqu'elle aura été validée.",
      |         "isTooLong": "Vous avez dépassé la limite de caractères.",
      |         "isTooShort": "Votre proposition doit faire au moins %{min} caractères",
      |         "titleIntro": "Partagez votre proposition sur le thème",
      |         "help": "Ne vous inquiétez pas, nous corrigerons vos éventuelles fautes d'orthographe.",
      |         "subHelp": "Pour en savoir plus sur notre charte de modération, cliquez ici.",
      |         "postedIn": "postée dans "
      |       },
      |       "account": {
      |         "validationTitle": "Activation du compte",
      |         "validationSuccess": "Votre compte vient d'être activé. vous pouvez vous connectez dès maintenant",
      |         "validationError": "Votre jeton est invalide, votre compte ne peut pas être activé."
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
      |         "stdRegister": "Je m’inscris avec mon adresse email",
      |         "submitButton": "Je me connecte",
      |         "oupsI": "J'ai ",
      |         "forgotPassword": "oublié mon mot de passe ?",
      |         "noAccount": "Je n'ai pas de compte | ",
      |         "createAccount": "Inscription",
      |         "errorAuthenticationFailed": "Ce compte ne semble pas exister. Merci de bien vouloir vérifier vos informations. ",
      |         "proposalIntroFirst": "Nous avons besoin de quelques informations",
      |         "proposalIntroSecond": "Pour valider votre proposition",
      |         "socialInfo": "Rassurez-vous, nous ne publierons jamais rien sans votre accord.",
      |         "errorSignInFailed": "Quelque chose n'a pas fonctionné. Si le problème persiste n'hésitez pas à nous contacter à <a href=\"mailto:contact@make.org\">contact@make.org</a>."
      |       },
      |       "register": {
      |          "withSocial": "Je m'inscris avec",
      |          "noPublishedContent": "(Nous n’y posterons jamais en votre nom)",
      |          "withForm": "Je m'inscris avec mon adresse email",
      |          "termsAgreed": "En vous inscrivant, vous acceptez nos conditions générales d'utilisation ainsi que de recevoir ponctuellement des emails de Make.org.",
      |          "subscribe": "Je m'inscris",
      |          "alreadySubscribed": "J’ai déjà un compte | ",
      |          "noRegister": "Non merci, je souhaite poursuivre %{break} sans compte finalement.",
      |          "errorRegistrationFailed": "Quelque chose n'a pas fonctionné. Si le problème persiste n'hésitez pas à nous contacter à <a href=\"mailto:contact@make.org\">contact@make.org</a>.",
      |          "errorInvalidEmail": "Format d'email non reconnu",
      |          "errorMinPassword": "Votre mot de passe doit contenir au moins %{min} caractères",
      |          "errorChoiceAge": "Valeur invalide",
      |          "errorMaxPostalCode": "Format erronné",
      |          "errorBlankFirstName": "Le prénom est un champ obligatoire",
      |          "errorAlreadyExist": "Cet email est déjà enregistré",
      |          "errorBlankEmail": "L'email est un champ obligatoire",
      |          "errorBlankPassword": "Le mot de passe est un champ obligatoire"
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
      |       "unexpectedBehaviour": "Quelque chose n'a pas fonctionné",
      |       "tryAgain": "Si le problème persiste n'hésitez pas à nous contacter à <a href=\"mailto:contact@make.org\">contact@make.org</a>."
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
      |         "textSeeMore": "En Savoir +",
      |         "expressYourself": "Express yourself !",
      |         "mostPopular": "Proposals the most popular on Make.org"
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
      |         "likeIt": "I <i class=\"fa fa-heart\"></i> It",
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
      |         "confirmationButtonBackTheme": "Retour au thème %{theme}",
      |         "confirmationButtonAnotherProposal": "Faire une nouvelle proposition",
      |         "confirmationContent": "Votre proposition a bien été prise en compte, elle va maintenant être relue par notre service modération. Vous recevrez un email lorsqu'elle aura été validée.",
      |         "isTooLong": "Vous avez dépassé la limite de caractères.",
      |         "isTooShort": "Votre proposition doit faire au moins %{min} caractères",
      |         "titleIntro": "Partagez votre proposition sur le thème",
      |         "help": "Ne vous inquiétez pas, nous corrigerons vos éventuelles fautes d'orthographe.",
      |         "subHelp": "Pour en savoir plus sur notre charte de modération, cliquez ici.",
      |         "postedIn": "posted in "
      |       },
      |       "account": {
      |         "validationTitle": "Activation du compte",
      |         "validationSuccess": "Votre compte vient d'être activé. vous pouvez vous connectez dès maintenant",
      |         "validationError": "Votre jeton est invalide, votre compte ne peut pas être activé."
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
      |         "stdRegister": "Je m’inscris avec mon adresse email",
      |         "submitButton": "se connecter",
      |         "oupsI": "J'ai ",
      |         "forgotPassword": "oublié mon mot de passe ?",
      |         "noAccount": "Je n'ai pas de compte | ",
      |         "createAccount": "Inscription",
      |         "errorAuthenticationFailed": "Ce compte ne semble pas exister. Merci de bien vouloir vérifier vos informations. ",
      |         "proposalIntroFirst": "Nous avons besoin de quelques informations",
      |         "proposalIntroSecond": "Pour valider votre proposition",
      |         "socialInfo": "Rassurez-vous, nous ne publierons jamais rien sans votre accord.",
      |         "errorSignInFailed": "Quelque chose n'a pas fonctionné. Si le problème persiste n'hésitez pas à nous contacter à <a href=\"mailto:contact@make.org\">contact@make.org</a>."
      |       },
      |       "register": {
      |          "withSocial": "Je m'inscris avec",
      |          "noPublishedContent": "(Nous n’y posterons jamais en votre nom)",
      |          "withForm": "Je m'inscris avec mon adresse email",
      |          "termsAgreed": "En vous inscrivant, vous acceptez nos conditions générales d'utilisation ainsi que de recevoir ponctuellement des emails de Make.org.",
      |          "subscribe": "Je m'inscris",
      |          "alreadySubscribed": "J’ai déjà un compte | ",
      |          "noRegister": "Non merci, je souhaite poursuivre %{break} sans compte finalement.",
      |          "errorRegistrationFailed": "Quelque chose n'a pas fonctionné. Si le problème persiste n'hésitez pas à nous contacter à <a href=\"mailto:contact@make.org\">contact@make.org</a>.",
      |          "errorInvalidEmail": "Format d'email non reconnu",
      |          "errorMinPassword": "Le mot de passe doit comporter au moins %{min} caractères",
      |          "errorChoiceAge": "Valeur invalide",
      |          "errorMaxPostalCode": "Format erronné",
      |          "errorBlankFirstName": "Le prénom est un champ obligatoire",
      |          "errorAlreadyExist": "Cet email est déjà enregistré",
      |          "errorBlankEmail": "L'email est un champ obligatoire",
      |          "errorBlankPassword": "Le mot de passe est un champ obligatoire"
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
