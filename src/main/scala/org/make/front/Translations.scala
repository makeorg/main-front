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
      |       "loginFailed": "La connexion a&nbsp;échoué",
      |       "apiFailure": "Échec de l'appel&nbsp;API",
      |       "unexpectedBehaviour": "Quelque chose n'a pas&nbsp;fonctionné",
      |       "tryAgain": "Si le problème persiste n'hésitez pas à nous contacter à&nbsp;support@make.org."
      |    },
      |    "content": {
      |       "header": {
      |         "presentation": "Qui sommes-nous&nbsp;?",
      |         "profile": "Mon Profil",
      |         "settings": "Paramètres",
      |         "logout": "Se Déconnecter",
      |         "connect": "Se connecter",
      |         "createAccount": "S'inscrire",
      |         "searchPlaceholder": "Recherchez une proposition..."
      |       },
      |       "homepage": {
      |
      |         "baseline": "À la une",
      |         "title": "Qui sommes-nous&nbsp;?",
      |         "subTitle": "Make.org est une initiative neutre et&nbsp;indépendante",
      |         "textSeeMore": "En Savoir&nbsp;+",
      |         "expressYourself": "Exprimez-vous&nbsp;!",
      |         "mostPopular": "Les propositions les + populaires sur&nbsp;Make.org"
      |       },
      |       "footer": {
      |         "presentation": "Qui sommes-nous&nbsp;?",
      |         "jobs": "jobs",
      |         "press": "Espace presse",
      |         "terms": "conditions d'utilisation",
      |         "contact": "contact",
      |         "faq": "f.a.q.",
      |         "sitemap": "sitemap",
      |         "recruitment": "Devenez Maker&nbsp;!",
      |         "title": "Tous les&nbsp;thèmes"
      |       },
      |       "theme": {
      |         "actionsCount": "%{actions} actions en&nbsp;cours",
      |         "proposalsCount": "%{proposals} propositions",
      |         "moreInfos": "En savoir&nbsp;+",
      |         "matrix": {
      |           "title": "votez sur les propositions",
      |           "noContent": "Nous n’avons trouvé <strong>aucun résultat</strong> correspondant à votre sélection de&nbsp;tag(s). <br> Vous pouvez sélectionner d’autres combinaisons de&nbsp;tags.",
      |           "filter": {
      |             "tag": {
      |               "title": "Tags populaire&nbsp;:"
      |             }
      |           },
      |           "seeMoreProposals": "Voir + de propositions"
      |         }
      |       },
      |       "tag": {
      |         "showMore": "afficher tous les&nbsp;tags",
      |         "showLess": "masquer les tags&nbsp;supplémentaires",
      |         "proposalsCount": "%{proposals} propositions",
      |         "moreInfos": "En savoir&nbsp;+"
      |       },
      |       "proposal": {
      |         "agree": "D'accord",
      |         "disagree": "Pas d'accord",
      |         "blank": "Vote Blanc",
      |         "likeIt": "Coup de <i class=\"fa fa-heart\"></i>",
      |         "doable": "Réaliste",
      |         "platitudeAgree": "Platitude",
      |         "noWay": "Surtout pas&nbsp;!",
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
      |         "confirmationThanks": "Merci&nbsp;!",
      |         "confirmationButtonBackTheme": "Retour au thème&nbsp;%{theme}",
      |         "confirmationButtonAnotherProposal": "Faire une nouvelle&nbsp;proposition",
      |         "confirmationContent": "Votre proposition a bien été prise en compte, elle va maintenant être relue par notre service modération. Vous recevrez un email lorsqu'elle aura été&nbsp;validée.",
      |         "isTooLong": "Vous avez dépassé la limite de&nbsp;caractères.",
      |         "isTooShort": "Votre proposition doit faire au moins %{min}&nbsp;caractères",
      |         "titleIntro": "Partagez votre proposition sur le&nbsp;thème",
      |         "help": "Ne vous inquiétez pas, nous corrigerons vos éventuelles fautes&nbsp;d'orthographe.",
      |         "subHelp": "Pour en savoir plus sur notre charte de modération, cliquez&nbsp;ici.",
      |         "postedIn": "postée dans ",
      |         "seeMore":"Voir + de résultats"
      |       },
      |       "account": {
      |         "validationTitle": "Activation du&nbsp;compte",
      |         "validationSuccess": "Votre compte vient d'être activé. vous pouvez vous connectez dès&nbsp;maintenant",
      |         "validationError": "Votre jeton est invalide, votre compte ne peut pas être&nbsp;activé."
      |       },
      |       "search": {
      |         "title": "<strong>%{results} résultats</strong> pour votre recherche ",
      |         "matrix": {
      |           "noContent": "Nous n'avons trouvé <strong> aucun résultat </strong> pour votre recherche<br> «&nbsp;%{search}&nbsp;»"
      |         },
      |         "proposeIntro": "Soyez la première personne à formuler une proposition à ce sujet !",
      |         "propose": "Proposer"
      |       }
      |    },
      |    "form": {
      |       "required": "(obligatoire)",
      |       "connection": "Connexion",
      |       "or": "ou",
      |       "fieldLabelEmail": "E-mail",
      |       "fieldLabelPassword": "Mot de passe",
      |       "fieldLabelFirstName": "Prénom",
      |       "fieldLabelAge": "Âge",
      |       "fieldPostalCode": "Code postal",
      |       "fieldProfession": "Profession",
      |       "login": {
      |         "close": "Fermer",
      |         "socialConnect": "Je me connecte&nbsp;avec",
      |         "socialRegister": "Je m'inscris&nbsp;avec",
      |         "stdConnect": "Je me connecte avec mon adresse&nbsp;e-mail",
      |         "stdRegister": "Je m’inscris avec ce&nbsp;formulaire",
      |         "submitButton": "Je me connecte",
      |         "oupsI": "J'ai ",
      |         "forgotPassword": "oublié mon mot de&nbsp;passe&nbsp;?",
      |         "noAccount": "Je n'ai pas de compte",
      |         "createAccount": "Inscription",
      |         "errorAuthenticationFailed": "Ce compte ne semble pas exister. Merci de bien vouloir vérifier vos&nbsp;informations.",
      |         "proposalIntroFirst": "Nous avons besoin de quelques&nbsp;informations",
      |         "proposalIntroSecond": "Pour valider votre&nbsp;proposition",
      |         "socialInfo": "Rassurez-vous, nous ne publierons jamais rien sans votre&nbsp;accord.",
      |         "errorSignInFailed": "Quelque chose n'a pas fonctionné. Si le problème persiste n'hésitez pas à nous contacter à <a href=\"mailto:contact@make.org\">contact@make.org</a>."
      |       },
      |       "register": {
      |          "withSocial": "Je m'inscris avec",
      |          "noPublishedContent": "(Nous n’y posterons jamais en votre&nbsp;nom)",
      |          "withForm": "Je m'inscris avec mon adresse&nbsp;email",
      |          "termsAgreed": "En vous inscrivant, vous acceptez nos conditions générales d'utilisation ainsi que de recevoir ponctuellement des emails de&nbsp;Make.org.",
      |          "subscribe": "Je m'inscris",
      |          "alreadySubscribed": "J’ai déjà un compte",
      |          "noRegister": "Non merci, je souhaite poursuivre %{break} sans compte&nbsp;finalement.",
      |          "errorRegistrationFailed": "Quelque chose n'a pas fonctionné. Si le problème persiste n'hésitez pas à nous contacter à&nbsp;support@make.org.",
      |          "errorInvalidEmail": "Format d'email non&nbsp;reconnu",
      |          "errorMinPassword": "Votre mot de passe doit contenir au moins %{min}&nbsp;caractères",
      |          "errorChoiceAge": "Valeur invalide",
      |          "errorMaxPostalCode": "Format erronné",
      |          "errorBlankFirstName": "Le prénom est un champ&nbsp;obligatoire",
      |          "errorAlreadyExist": "Cet email est déjà&nbsp;enregistré",
      |          "errorBlankEmail": "L'email est un champ&nbsp;obligatoire",
      |          "errorBlankPassword": "Le mot de passe est un champ&nbsp;obligatoire"
      |       },
      |       "proposal": {
      |         "submit": "Proposer",
      |         "errorSubmitFailed": "Une erreur s'est produite, réessayez dans quelques minutes"
      |       },
      |       "passwordRecovery": {
      |         "title": "Je réinitialise mon mot de&nbsp;passe",
      |         "description": "Merci de renseigner l'adresse email liée à votre compte pour recevoir le lien de&nbsp;réinitialisation.",
      |         "fieldLabelEmail": "Votre adresse email",
      |         "sendEmail": "Envoyer",
      |         "invalidEmail": "Veuillez rentrer un e-mail&nbsp;valide",
      |         "emailDoesNotExist": "L'e-mail que vous avez rentré n'existe&nbsp;pas",
      |         "return": "Revenir à ",
      |         "connectScreen": "l'écran de connexion",
      |         "notification": {
      |           "message" : "Merci, un email vient de vous être envoyé pour vous permettre de mettre à jour votre mot de&nbsp;passe."
      |         }
      |       },
      |       "passwordReset": {
      |         "title": "Je crée un nouveau mot de&nbsp;passe",
      |         "description": "Vous pouvez choisir un nouveau mot de&nbsp;passe.",
      |         "validation": "Valider",
      |         "success": {
      |           "title": "merci, votre mot de passe a bien été&nbsp;changé.",
      |           "description": "Vous pouvez vous connecter dès&nbsp;maintenant."
      |         },
      |         "failed" : {
      |           "title": "Votre jeton est&nbsp;invalide&nbsp;!"
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
      |       "noToken": "No token was&nbsp;provided",
      |       "loginFailed": "Login Failed",
      |       "apiFailure": "API call failed",
      |       "unexpectedBehaviour": "Quelque chose n'a pas&nbsp;fonctionné",
      |       "tryAgain": "Si le problème persiste n'hésitez pas à nous contacter à&nbsp;support@make.org."
      |    },
      |    "content": {
      |       "header": {
      |         "profile": "My Profile",
      |         "settings": "Settings",
      |         "logout": "Logout",
      |         "connect": "Log In",
      |         "createAccount": "Create account",
      |         "searchPlaceholder": "Search for an idea..."
      |       },
      |       "homepage": {
      |         "baseline": "À la&nbsp;une",
      |         "title": "Qui sommes-nous&nbsp;?",
      |         "subTitle": "Make.org est une initiative neutre et&nbsp;indépendante",
      |         "textSeeMore": "En Savoir&nbsp;+",
      |         "expressYourself": "Express yourself&nbsp;!",
      |         "mostPopular": "Proposals the most popular on&nbsp;Make.org"
      |       },
      |       "footer": {
      |         "jobs": "jobs",
      |         "press": "Espace presse",
      |         "terms": "conditions d'utilisation",
      |         "contact": "contact",
      |         "faq": "f.a.q.",
      |         "sitemap": "sitemap",
      |         "recruitment": "Devenez Maker&nbsp;!",
      |         "title": "Tous les&nbsp;thèmes"
      |       },
      |       "theme": {
      |         "actionsCount": "%{actions} actions en&nbsp;cours",
      |         "proposalsCount": "%{proposals} propositions",
      |         "moreInfos": "En savoir&nbsp;+",
      |         "matrix": {
      |           "title": "votez sur les propositions",
      |           "noContent": "Nous n’avons trouvé <strong>aucun résultat</strong> correspondant à votre sélection de&nbsp;tag(s). <br /> Vous pouvez sélectionner d’autres combinaisons de&nbsp;tags.",
      |           "filter": {
      |             "tag": {
      |               "title": "Tags populaire&nbsp;:"
      |             }
      |           }
      |         }
      |       },
      |       "tag": {
      |         "showMore": "show all tags",
      |         "showLess": "hide additional tags",
      |         "proposalsCount": "%{proposals} propositions",
      |         "moreInfos": "En savoir&nbsp;+"
      |       },
      |       "proposal": {
      |         "agree": "Agree",
      |         "disagree": "Disagree",
      |         "blank": "Blank Vote",
      |         "likeIt": "I <i class=\"fa fa-heart\"></i> It",
      |         "doable": "Doable",
      |         "platitudeAgree": "Platitude",
      |         "noWay": "No way&nbsp;!",
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
      |         "confirmationThanks": "Merci&nbsp;!",
      |         "confirmationButtonBackTheme": "Retour au thème&nbsp;%{theme}",
      |         "confirmationButtonAnotherProposal": "Faire une nouvelle&nbsp;proposition",
      |         "confirmationContent": "Votre proposition a bien été prise en compte, elle va maintenant être relue par notre service modération. Vous recevrez un email lorsqu'elle aura été&nbsp;validée.",
      |         "isTooLong": "Vous avez dépassé la limite de&nbsp;caractères.",
      |         "isTooShort": "Votre proposition doit faire au moins %{min}&nbsp;caractères",
      |         "titleIntro": "Partagez votre proposition sur le&nbsp;thème",
      |         "help": "Ne vous inquiétez pas, nous corrigerons vos éventuelles fautes&nbsp;d'orthographe.",
      |         "subHelp": "Pour en savoir plus sur notre charte de modération, cliquez&nbsp;ici.",
      |         "postedIn": "posted in ",
      |         "seeMore":"Voir + de résultats"
      |       },
      |       "account": {
      |         "validationTitle": "Activation du compte",
      |         "validationSuccess": "Votre compte vient d'être activé. vous pouvez vous connectez dès maintenant",
      |         "validationError": "Votre jeton est invalide, votre compte ne peut pas être activé."
      |       },
      |       "search": {
      |         "title": "<strong>%{results} results</strong> for your search ",
      |         "matrix": {
      |           "noContent": "We didn't find <strong> any results </strong> to your search<br> «&nbsp;%{search}&nbsp;»"
      |         },
      |         "proposeIntro": "Be the first one to suggest an idea on this topic !",
      |         "propose": "Propose"
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
      |         "socialConnect": "Je me connecte&nbsp;avec",
      |         "socialRegister": "Je m'inscris&nbsp;avec",
      |         "stdConnect": "Je me connecte avec mon adresse&nbsp;e-mail",
      |         "stdRegister": "Je m’inscris avec ce&nbsp;formulaire",
      |         "submitButton": "se connecter",
      |         "oupsI": "J'ai ",
      |         "forgotPassword": "oublié mon mot de passe&nbsp;?",
      |         "noAccount": "Je n'ai pas de&nbsp;compte",
      |         "createAccount": "Inscription",
      |         "errorAuthenticationFailed": "Ce compte ne semble pas exister. Merci de bien vouloir vérifier vos&nbsp;informations.",
      |         "proposalIntroFirst": "Nous avons besoin de quelques&nbsp;informations",
      |         "proposalIntroSecond": "Pour valider votre&nbsp;proposition",
      |         "socialInfo": "Rassurez-vous, nous ne publierons jamais rien sans votre&nbsp;accord.",
      |         "errorSignInFailed": "Quelque chose n'a pas fonctionné. Si le problème persiste n'hésitez pas à nous contacter à <a href=\"mailto:contact@make.org\">contact@make.org</a>."
      |       },
      |       "register": {
      |          "withSocial": "Je m'inscris&nbsp;avec",
      |          "noPublishedContent": "(Nous n’y posterons jamais en votre&nbsp;nom)",
      |          "withForm": "Je m'inscris avec mon adresse&nbsp;email",
      |          "termsAgreed": "En vous inscrivant, vous acceptez nos conditions générales d'utilisation ainsi que de recevoir ponctuellement des emails de&nbsp;Make.org.",
      |          "subscribe": "Je m'inscris",
      |          "alreadySubscribed": "J’ai déjà un compte",
      |          "noRegister": "Non merci, je souhaite poursuivre %{break} sans compte&nbsp;finalement.",
      |          "errorRegistrationFailed": "Quelque chose n'a pas fonctionné. Si le problème persiste n'hésitez pas à nous contacter à <a href=\"mailto:contact@make.org\">contact@make.org</a>.",
      |          "errorInvalidEmail": "Format d'email non&nbsp;reconnu",
      |          "errorMinPassword": "Le mot de passe doit comporter au moins %{min}&nbsp;caractères",
      |          "errorChoiceAge": "Valeur invalide",
      |          "errorMaxPostalCode": "Format erronné",
      |          "errorBlankFirstName": "Le prénom est un champ&nbsp;obligatoire",
      |          "errorAlreadyExist": "Cet email est déjà&nbsp;enregistré",
      |          "errorBlankEmail": "L'email est un champ&nbsp;obligatoire",
      |          "errorBlankPassword": "Le mot de passe est un champ&nbsp;obligatoire"
      |       },
      |       "proposal": {
      |         "submit": "Proposer",
      |         "errorSubmitFailed": "An error occurred, please try again later"
      |       },
      |       "passwordRecovery": {
      |         "title": "je réinitialise mon mot de&nbsp;passe",
      |         "description": "Merci de renseigner l'adresse email liée à votre compte pour recevoir le lien de&nbsp;réinitialisation.",
      |         "fieldLabelEmail": "votre adresse&nbsp;email",
      |         "sendEmail": "Envoyer",
      |         "invalidEmail": "veuillez rentrer un e-mail&nbsp;valide",
      |         "emailDoesNotExist": "L'e-mail que vous avez rentré n'existe&nbsp;pas",
      |         "return": "Revenir à ",
      |         "connectScreen": "l'écran de connexion",
      |         "notification": {
      |           "message" : "Merci, un email vient de vous être envoyé pour vous permettre de mettre à jour votre mot de&nbsp;passe."
      |         }
      |       },
      |       "passwordReset": {
      |         "title": "Je crée un nouveau mot de&nbsp;passe",
      |         "description": "Vous pouvez choisir un nouveau mot de&nbsp;passe.",
      |         "validation": "Valider",
      |         "success": {
      |           "title": "merci, votre mot de passe a bien été&nbsp;changé.",
      |           "description": "Vous pouvez vous connecter dès&nbsp;maintenant."
      |         },
      |         "failed" : {
      |           "title": "Votre jeton est invalide&nbsp;!"
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
