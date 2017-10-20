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
      |      "main": "Une erreur est&nbsp;survenue",
      |      "noToken": "Token absent",
      |      "loginFailed": "La connexion a&nbsp;échoué",
      |      "apiFailure": "Échec de l'appel&nbsp;API",
      |      "unexpectedBehaviour": "Quelque chose n'a pas&nbsp;fonctionné",
      |      "tryAgain": "Si le problème persiste n'hésitez pas à nous contacter à&nbsp;support@make.org."
      |    },
      |    "home": {
      |      "intro": {
      |        "baseline": "À propos",
      |        "title": "Proposez, votez, agissons",
      |        "subtitle": "Make.org est une initiative indépendante au service de l'intérêt&nbsp;général.",
      |        "see-more": "En savoir&nbsp;+",
      |        "see-more-link": "https://about.make.org/qui-sommes-nous"
      |      },
      |      "explanations": {
      |        "article-1": {
      |          "intro": "la politique ne suffit plus, faisons bouger les lignes&nbsp;ensemble",
      |          "title": "Votez, proposez,&nbsp;agissons",
      |          "item-1": "<strong>Votez</strong> pour les propositions que vous&nbsp;défendez",
      |          "item-2": "<strong>Proposez</strong> vos idées sur des thèmes qui vous&nbsp;inspirent",
      |          "item-3": "Et demain, <strong>initiez des actions</strong> concrètes sur le&nbsp;terrain",
      |          "see-more-link": "/",
      |          "see-more": "En savoir +"
      |        },
      |        "article-2": {
      |          "intro": "qui sommes-nous&nbsp;?",
      |          "title": "Make.org, neutre & indépendant",
      |          "text": "Make.org est une initiative civique, Européenne et&nbsp;indépendante.",
      |          "see-more-link": "https://about.make.org/qui-sommes-nous",
      |          "see-more": "En savoir&nbsp;+"
      |        }
      |      },
      |      "showcase-1": {
      |        "intro": "qui sommes-nous&nbsp;?",
      |        "news": "Le gouvernement prépare un nouveau Grenelle<br>de l'environnement, exprimez vos idées sur le&nbsp;sujet."
      |      },
      |      "showcase-2": {
      |        "intro": "Exprimez-vous&nbsp;!",
      |        "title": "Les propositions les + populaires sur"
      |      },
      |      "showcase-3": {
      |        "intro": "départagez-les&nbsp;!",
      |        "title": "Les propositions les + populaires sur"
      |      }
      |    },
      |    "nav-in-themes": {
      |      "title": "Tous les&nbsp;thèmes",
      |      "total-of-actions": "%{total} actions",
      |      "total-of-proposals": "%{total} propositions"
      |    },
      |    "main-header": {
      |      "title": "Make.org",
      |      "menu" : {
      |        "item-1": {
      |          "label": "Qui sommes-nous&nbsp;?",
      |          "link": "https://about.make.org/qui-sommes-nous"
      |        }
      |      }
      |    },
      |    "about-make-main-header": {
      |      "title": "Make.org",
      |      "menu" : {
      |        "item-1": {
      |          "label": "Qui sommes-nous&nbsp;?",
      |          "link": "https://about.make.org/qui-sommes-nous"
      |        },
      |        "item-2": {
      |          "label": "Notre actu",
      |          "link": "https://about.make.org"
      |        },
      |        "item-3": {
      |          "label": "Notre équipe",
      |          "link": "https://about.make.org/category/videos"
      |        },
      |        "item-4": {
      |          "label": "Presse",
      |          "link": "https://about.make.org/category/presse"
      |        },
      |        "item-5": {
      |          "label": "Nous rejoindre",
      |          "link": "https://about.make.org/jobs"
      |        }
      |      }
      |    },
      |    "search": {
      |      "form":{
      |        "placeholder": "Recherchez une proposition..."
      |      },
      |      "results": {
      |        "intro": "<strong>%{total} résultats</strong> pour votre recherche",
      |        "see-more": "Voir + de propositions"
      |      },
      |      "no-results": {
      |        "intro": "Nous n'avons trouvé <strong>aucun résultat</strong> pour votre&nbsp;recherche.",
      |        "prompting-to-propose": "Soyez la première personne à formuler une proposition à ce sujet&nbsp;!",
      |        "propose-cta": "Proposer"
      |      }
      |    },
      |    "user-nav": {
      |      "login": "Se connecter",
      |      "register": "S'inscrire"
      |    },
      |    "proposal": {
      |      "created-by-user": "Ma proposition",
      |      "associated-with-the-theme": "postée dans ",
      |      "author-infos": {
      |        "age": ", %{age} ans",
      |        "postal-code": " (%{postalCode})",
      |        "anonymous": "anonyme"
      |      },
      |      "vote": {
      |       "partOfVotes": "(%{value}%)",
      |        "agree": {
      |          "label": "D'accord",
      |          "qualifications":{
      |            "likeIt": "Remarquable",
      |            "doable": "Réaliste",
      |            "platitudeAgree": "Banalité"
      |          }
      |        },
      |        "disagree": {
      |          "label": "Pas d'accord",
      |          "qualifications":{
      |            "noWay": "Surtout pas&nbsp;!",
      |            "impossible": "Infaisable",
      |            "platitudeDisagree": "Banalité"
      |          }
      |        },
      |        "neutral": {
      |          "label": "Vote blanc",
      |          "qualifications":{
      |            "doNotUnderstand": "Pas compris",
      |            "noOpinion": "Pas d'avis",
      |            "doNotCare": "Indifférent"
      |          }
      |        }
      |      },
      |      "qualificate-vote": {
      |        "increment": "+1"
      |      }
      |    },
      |    "submit-proposal": {
      |      "intro": "Partagez votre proposition",
      |      "error": "Une erreur s'est produite, réessayez dans quelques minutes.",
      |      "form": {
      |        "proposal-input-placeholder": "Il faut une proposition réaliste et respectueuse de tous",
      |        "limit-of-chars-reached-alert": "Oups&nbsp;! Vous avez dépassé la limite de 140 caractères 😅 Essayez d’être plus concis.e.&nbsp;🙏",
      |        "info": "Ne vous inquiétez pas, nous corrigerons vos éventuelles fautes&nbsp;d'orthographe.",
      |        "moderation-charter": "Pour en savoir plus sur notre charte de modération, <a href=\"https://about.make.org/moderation\" target=\"_blank\">cliquez&nbsp;ici.</a>",
      |        "validate-cta": "Proposer",
      |        "errors": {
      |          "limit-of-chars-exceeded": "Vous avez dépassé la limite de caractères.",
      |          "not-enough-chars": "Votre proposition doit contenir au moins %{min} caractères."
      |        }
      |      },
      |      "confirmation": {
      |         "title": "Merci&nbsp;!",
      |         "info": "Votre proposition a bien été prise en compte, elle va maintenant être relue par notre service modération.<br>Vous recevrez un email lorsqu'elle aura été&nbsp;validée.",
      |         "back-to-theme-cta": "Retour au thème&nbsp;%{theme}",
      |         "back-cta": "Retour",
      |         "new-proposal-cta": "Faire une nouvelle&nbsp;proposition"
      |      }
      |    },
      |    "political-actions": {
      |      "intro": "%{actions} actions issues de vos propositions"
      |    },
      |    "theme": {
      |      "results": {
      |         "title": "Votez sur les&nbsp;propositions",
      |         "see-more": "Voir + de propositions",
      |         "no-results": "Nous n’avons trouvé <strong>aucun résultat</strong> correspondant à votre sélection de&nbsp;tag(s). <br> Vous pouvez sélectionner d’autres combinaisons de&nbsp;tags."
      |      },
      |      "proposal-form-in-header": {
      |        "bait": "Il faut ",
      |        "limit-of-chars-info": "8/140"
      |      },
      |      "submit-proposal": {
      |         "intro": "Partagez votre proposition sur le&nbsp;thème"
      |      }
      |    },
      |    "sequence": {
      |      "next-cta": "Proposition suivante"
      |    },
      |    "operation": {
      |      "vff-header": {
      |        "label": "Grande cause Make.org",
      |        "title": "Stop aux violences faites aux femmes",
      |        "period": "Consultation ouverte du 25 nov. 2017 au 8 mars&nbsp;2018",
      |        "partners": {
      |          "intro": "avec"
      |        },
      |        "article": {
      |          "title": "Pourquoi cette consultation&nbsp;?",
      |          "content": "Chaque année, 216 000 femmes âgées de 18 à 75 ans sont victimes de violences physiques et/ou sexuelles de la part de leur ancien ou actuel partenaire intime (mari, concubin, pacsé, petit-ami…). Agissons&nbsp;!",
      |          "see-more": {
      |            "label": "En savoir +",
      |            "link": "/"
      |          }
      |        }
      |      },
      |      "results": {
      |        "title": "Votez sur les&nbsp;propositions",
      |        "see-more": "Voir + de propositions",
      |        "no-results": "Nous n’avons trouvé <strong>aucun résultat</strong> correspondant à votre sélection de&nbsp;tag(s). <br> Vous pouvez sélectionner d’autres combinaisons de&nbsp;tags."
      |      },
      |      "proposal-form-in-header": {
      |        "intro": "partagez vos propositions",
      |        "bait": "Il faut ",
      |        "limit-of-chars-info": "8/140"
      |      },
      |      "submit-proposal": {
      |         "intro": "Partagez votre proposition"
      |      },
      |      "sequence": {
      |        "header": {
      |           "total-of-proposals": "%{total} propositions",
      |           "back-cta": "Accéder à<br>l'opération",
      |           "propose-cta": "Proposer"
      |        },
      |        "introduction": {
      |          "title": "Des milliers de citoyens proposent des&nbsp;solutions.",
      |          "explanation-1": "Prenez position sur ces solutions et proposez les&nbsp;vôtres.",
      |          "explanation-2": "Les plus soutenues détermineront nos&nbsp;actions.",
      |          "cta": "Démarrer"
      |        },
      |        "conclusion": {
      |          "title": "Merci pour votre contribution&nbsp;!",
      |          "info": "Nous vous tiendrons informé.e de l’avancée et des résultats de la consultation par&nbsp;mail.",
      |          "prompting-to-subscribe-to-newsletter": "Nous vous invitons à saisir votre adresse e-mail pour être informé.e de l’avancée et des résultats de la&nbsp;consultation."
      |        }
      |      }
      |    },
      |    "subscribe-to-newsletter": {
      |      "email-input-placeholder": "votre adresse email",
      |      "send-cta": "Envoyer",
      |      "invalid-email": "veuillez rentrer un e-mail&nbsp;valide",
      |      "error": "Quelque chose n'a pas fonctionné. Si le problème persiste n'hésitez pas à nous contacter à <a href=\"mailto:contact@make.org\">contact@make.org</a>.",
      |      "notifications": {
      |        "success": "Merci, votre adresse email est bien enregistrée dans notre liste de diffusion. Vous recevrez nos prochains mails&nbsp;d'information."
      |      }
      |    },
      |    "tags": {
      |      "filter": {
      |        "intro": "Filtrer par tags&nbsp;:"
      |      },
      |      "list": {
      |        "show-all": "voir tous les&nbsp;tags",
      |        "show-less": "masquer"
      |      }
      |    },
      |    "activate-account": {
      |      "notifications": {
      |        "success": "Votre compte vient d'être activé. Vous pouvez vous connecter dès&nbsp;maintenant",
      |        "failure": "Ce lien n'est plus valable."
      |      }
      |    },
      |    "authenticate": {
      |      "forgot-password": {
      |        "intro": "J'ai ",
      |        "link-support": "oublié mon mot de&nbsp;passe&nbsp;?"
      |      },
      |      "switch-to-register-screen": {
      |        "intro": "Je n'ai pas de compte ",
      |        "link-support": "Inscription"
      |      },
      |      "switch-to-login-screen": {
      |        "intro": "J’ai déjà un compte",
      |        "link-support": "Connexion"
      |      },
      |      "back-to-login-screen": {
      |        "intro": "Revenir à ",
      |        "link-support": "l'écran de connexion"
      |      },
      |      "no-account-found": "Nous ne trouvons pas de compte associé à cet email.",
      |      "failure": "Quelque chose n'a pas fonctionné. Si le problème persiste n'hésitez pas à nous contacter à <a href=\"mailto:contact@make.org\">contact@make.org</a>",
      |      "inputs": {
      |        "required": "(obligatoire)",
      |        "email": {
      |          "placeholder": "E-mail",
      |          "format-error": "Format d'email non&nbsp;reconnu",
      |          "empty-field-error": "Veuillez renseigner ce&nbsp;champ."
      |        },
      |        "first-name": {
      |          "placeholder": "Prénom",
      |          "format-error": "Format invalide",
      |          "empty-field-error": "Veuillez renseigner ce&nbsp;champ."
      |        },
      |        "password": {
      |          "placeholder": "Mot de passe",
      |          "format-error": "Votre mot de passe doit contenir au moins %{min}&nbsp;caractères.",
      |          "empty-field-error": "Veuillez renseigner ce&nbsp;champ."
      |        },
      |        "age": {
      |          "placeholder": "Âge",
      |          "format-error": "Format invalide"
      |        },
      |        "postal-code": {
      |          "placeholder": "Code postal",
      |          "format-error": "Format invalide"
      |        },
      |        "job": {
      |          "placeholder": "Profession",
      |          "format-error": "Format invalide"
      |        }
      |      },
      |      "recover-password": {
      |         "title": "Je réinitialise mon mot de&nbsp;passe",
      |         "info": "Merci de renseigner l'adresse email liée à votre compte pour recevoir le lien de&nbsp;réinitialisation.",
      |         "send-cta": "Envoyer",
      |         "errors": {
      |           "mail-not-found": "Nous ne trouvons pas de compte associé à cet&nbsp;email."
      |         },
      |         "notifications": {
      |           "success": "Merci, un email vient de vous être envoyé pour réinitialiser votre mot de&nbsp;passe."
      |         }
      |      },
      |      "reset-password":{
      |        "title": "Je crée un nouveau mot de&nbsp;passe",
      |        "info": "Vous pouvez choisir un nouveau mot de&nbsp;passe.",
      |        "send-cta": "Valider",
      |        "success": {
      |          "title": "Merci, votre mot de passe a bien été mis à&nbsp;jour.",
      |          "info": "Vous pouvez vous connecter dès&nbsp;maintenant."
      |        },
      |        "failure" : {
      |          "title": "Ce lien n'est plus valable."
      |        },
      |        "notifications": {
      |          "failure": "Votre mot de passe n'a pas pu être remplacé."
      |        }
      |      },
      |      "register": {
      |        "caution": "Rassurez-vous, nous ne publierons jamais rien sans votre&nbsp;accord.",
      |        "with-social-networks-intro": "Je m'inscris avec",
      |        "separator": "ou",
      |        "with-email-intro": "Je m'inscris avec mon adresse&nbsp;email",
      |        "terms": "En vous inscrivant, vous acceptez nos conditions générales d'utilisation ainsi que de recevoir ponctuellement des emails de&nbsp;Make.org.",
      |        "send-cta": "Je m'inscris",
      |        "errors": {
      |          "already-exists": "Ce compte existe déjà, merci de vous&nbsp;connecter."
      |        },
      |        "notifications": {
      |          "success": "Votre compte a été crée avec succès. Pensez à confirmer votre adresse email grâce au lien qui vous a été envoyé - ceci afin de nous assurer qu'il s'agit bien de vous."
      |        }
      |      },
      |      "login": {
      |        "with-social-networks-intro": "Je me connecte&nbsp;avec",
      |        "separator": "ou",
      |        "with-email-intro": "Je me connecte avec mon adresse&nbsp;e-mail",
      |        "send-cta": "Je me connecte"
      |      }
      |    },
      |    "main-footer": {
      |      "menu": {
      |        "item-1": {
      |          "label": "Devenez Maker&nbsp;!",
      |          "link": "/"
      |        },
      |        "item-2": {
      |          "label": "Jobs",
      |          "link": "https://about.make.org/jobs"
      |        },
      |        "item-3": {
      |          "label": "Qui sommes-nous&nbsp;?",
      |          "link": "https://about.make.org/qui-sommes-nous"
      |        },
      |        "item-4": {
      |          "label": "Espace presse",
      |          "link": "https://about.make.org/category/presse"
      |        },
      |        "item-5": {
      |          "label": "Conditions d'utilisation",
      |          "link": "https://about.make.org/conditions-dutilisation"
      |        },
      |        "item-6": {
      |          "label": "Politique d'utilisation des données",
      |          "link": "https://about.make.org/politique-donnees"
      |        },
      |        "item-7": {
      |          "label": "Contact",
      |          "link": "https://about.make.org/contact"
      |        },
      |        "item-8": {
      |          "label": "f.a.q.",
      |          "link": "/"
      |        },
      |        "item-9": {
      |          "label": "sitemap",
      |          "link": "/"
      |        }
      |      }
      |    }
      |  }
      |}
    """.stripMargin

}
