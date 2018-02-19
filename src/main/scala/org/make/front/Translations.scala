package org.make.front

import org.make.front.facades.I18n.setTranslations

import scala.scalajs.js
import scala.scalajs.js.JSON

object Translations {

  def loadTranslations(): Unit = {
    setTranslations(JSON.parse(translations).asInstanceOf[js.Object])
  }

  val translationsFr: String =
    """
      |{
      |    "common": {
      |       "bait": "Il faut"
      |    },
      |    "error-message": {
      |      "main": "Oups ! Nous rencontrons quelques difficultés avec le chargement de cette page. Merci de bien vouloir la&nbsp;recharger.",
      |      "no-token": "Token absent",
      |      "unexpected-behaviour": "Quelque chose n'a pas&nbsp;fonctionné",
      |      "user-not-found": "Utilisateur non retrouvé"
      |    },
      |    "error": {
      |      "intro": "oups",
      |      "title": "Erreur 404",
      |      "recherche-intro": "La page que vous avez demandé n’existe pas ou plus. Vous pouvez lancer une&nbsp;recherche&hellip;",
      |      "redirection-intro": "&hellip; ou vous laisser tenter par une nouvelle destination&nbsp;:",
      |      "redirect-to-home": "La page d'accueil",
      |      "redirect-to-random-theme": "Un thème choisi au hasard"
      |    },
      |    "maintenance": {
      |      "intro": "oups",
      |      "title": "Maintenance en cours",
      |      "explanation-1": "Pour pouvoir vous proposer un site toujours plus&nbsp;performant,<br>Make.org est actuellement en&nbsp;maintenance.",
      |      "explanation-2": "N’hésitez pas à repasser d’ici quelques minutes."
      |    },
      |    "cookie-alert": "En poursuivant votre navigation sur notre site vous acceptez l'utilisation de cookies pour vous proposer une expérience personnalisée et&nbsp;fluide.",
      |    "home": {
      |      "welcome": {
      |        "baseline": "À propos",
      |        "title": "Proposez, votez, agissons",
      |        "subtitle": "Make.org est une initiative indépendante au service de l'intérêt&nbsp;général.",
      |        "see-more": "En savoir&nbsp;+",
      |        "see-more-link": "https://about.make.org/qui-sommes-nous"
      |      },
      |      "featured-operation": {
      |        "learn-more": "Découvrir les résultats"
      |      },
      |      "explanations": {
      |        "article-1": {
      |          "intro": "la politique ne suffit plus, faisons bouger les lignes&nbsp;ensemble",
      |          "title": "Votez, proposez,&nbsp;agissons",
      |          "item-1": "<strong>Votez</strong> pour les propositions que vous&nbsp;défendez",
      |          "item-2": "<strong>Proposez</strong> vos idées sur des thèmes qui vous&nbsp;inspirent",
      |          "item-3": "Et demain, <strong>initiez des actions</strong> concrètes sur le&nbsp;terrain",
      |          "see-more-link": "/",
      |          "see-more": "En savoir&nbsp;+"
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
      |        "intro": "le thème du mois&nbsp;",
      |        "news": "Budget de la Sécurité sociale, États généraux de l'Alimentation, glyphosate&nbsp;:<br>la santé et l'alimentation sont au coeur de l'actualité, à vous de vous exprimer."
      |      },
      |      "showcase-2": {
      |        "intro": "Exprimez-vous&nbsp;!",
      |        "title": "Les propositions les + populaires sur"
      |      },
      |      "showcase-3": {
      |        "intro": "départagez-les&nbsp;!",
      |        "title": "Les propositions les + débattues sur"
      |      }
      |    },
      |    "theme-showcase": {
      |      "see-all": "Voir toutes les propositions du thème %{themeName}",
      |      "prompting-to-propose-tile": {
      |        "intro": "Et vous, quelle est votre proposition&nbsp;?",
      |        "bait": "Il faut..."
      |      },
      |      "data-units": {
      |        "actions": "actions",
      |        "votes": "votes",
      |        "proposals": "propositions"
      |      },
      |      "filter": {
      |        "intro" : "Afficher les propositions&nbsp;:",
      |        "the-most-popular": "les + populaires",
      |        "the-most-original": "les + originales"
      |      }
      |    },
      |    "nav-in-themes": {
      |      "title": "Tous les&nbsp;thèmes",
      |      "total-of-actions": "%{total} actions en cours",
      |      "total-of-actions_1": "%{total} action en cours",
      |      "total-of-proposals_1": "%{total} proposition",
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
      |    "user-profile": {
      |      "title": "Mes infos personnelles",
      |      "disconnect-cta": "Je me déconnecte"
      |    },
      |    "proposal": {
      |      "bait": "Il faut ",
      |      "created-by-user": "Ma proposition",
      |      "share-intro": "Partagez cette proposition avec votre communauté",
      |      "associated-with-the-theme": "postée dans ",
      |      "proposal-s-operation-infos": {
      |        "intro": "Cette proposition a été soumise dans le cadre de l'opération",
      |        "participate": "Participer",
      |        "see-more": "En savoir&nbsp;+"
      |      },
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
      |      "error-message": "Une erreur s'est produite, réessayez dans quelques minutes.",
      |      "form": {
      |        "proposal-input-placeholder": "Il faut une proposition réaliste et respectueuse de tous",
      |        "limit-of-chars-reached-alert": "Oups&nbsp;! Vous avez dépassé la limite de 140 caractères 😅 Essayez d’être plus concis.e.&nbsp;🙏",
      |        "info": "Ne vous inquiétez pas, nous corrigerons vos éventuelles fautes&nbsp;d'orthographe.",
      |        "moderation-charter": "Pour en savoir plus sur notre charte de modération, <a href=\"https://about.make.org/moderation\" target=\"_blank\">cliquez&nbsp;ici.</a>",
      |        "validate-cta": "Proposer",
      |        "error-message": {
      |          "limit-of-chars-exceeded": "Vous avez dépassé la limite de caractères.",
      |          "not-enough-chars": "Votre proposition doit contenir au moins %{min} caractères."
      |        }
      |      },
      |      "authenticate": {
      |        "intro": "Nous avons besoin de quelques informations",
      |        "title": "Pour valider votre&nbsp;proposition"
      |      },
      |      "confirmation": {
      |         "title": "Merci&nbsp;!",
      |         "info": "Votre proposition a bien été prise en compte, elle va maintenant être relue par notre service modération.<br>Vous recevrez un email lorsqu'elle aura été&nbsp;validée.",
      |         "back-to-theme-cta": "Retourner sur le thème&nbsp;%{theme}",
      |         "back-cta": "Retour",
      |         "new-proposal-cta": "Faire une nouvelle&nbsp;proposition"
      |      }
      |    },
      |    "political-actions": {
      |      "intro": "%{actions} actions issues de vos propositions",
      |      "intro_0": "%{actions} action issue de vos propositions",
      |      "intro_1": "%{actions} action issue de vos propositions"
      |    },
      |    "no-political-action": {
      |      "intro": "Nous n’avons pas d’action à vous proposer sur ce thème pour le moment, mais nous y&nbsp;travaillons&nbsp;!",
      |      "text": "Rendez-vous très prochainement pour agir&nbsp;ensemble&nbsp;!"
      |    },
      |    "theme": {
      |      "results": {
      |         "title": "Votez sur les&nbsp;propositions",
      |         "see-more": "Voir + de propositions",
      |         "no-results": "Nous n’avons trouvé <strong>aucun résultat</strong> correspondant à votre sélection de&nbsp;tag(s). <br> Vous pouvez sélectionner d’autres combinaisons de&nbsp;tags."
      |      },
      |      "proposal-form-in-header": {
      |        "limit-of-chars-info": "8/140"
      |      },
      |      "submit-proposal": {
      |         "intro": "Partagez votre proposition sur le&nbsp;thème"
      |      }
      |    },
      |    "sequence": {
      |      "proposal": {
      |        "next-cta": "Proposition suivante"
      |      },
      |      "guide": {
      |        "vote": "Utilisez les boutons pour voter<br>et passer à la proposition suivante.",
      |        "qualification": "Si vous le souhaitez, vous pouvez préciser votre vote. (Sinon, passez à la proposition suivante.)"
      |      },
      |      "introduction": {
      |        "title": "Des milliers de citoyens proposent des&nbsp;solutions.",
      |        "explanation-1": "Prenez position sur ces solutions et proposez les&nbsp;vôtres.",
      |        "explanation-2": "Les plus soutenues détermineront nos&nbsp;actions.",
      |        "cta": "Démarrer"
      |      },
      |      "prompting-to-propose": {
      |         "intro": "Et vous, avez-vous une solution à proposer sur ce&nbsp;sujet&nbsp;?",
      |         "propose-cta": "Proposer",
      |         "next-cta": "Pas encore, je continue à voter"
      |      },
      |      "prompting-to-continue": {
      |         "intro": "Merci pour votre contribution. Continuez à vous engager sur cette&nbsp;cause&nbsp;:",
      |         "continue": {
      |           "intro": "En poursuivant la consultation&nbsp;:",
      |           "cta": "Démarrer"
      |         },
      |         "learn-more": {
      |           "intro": "En découvrant l'ensemble de&nbsp;l'opération",
      |           "cta": "En savoir&nbsp;+"
      |         },
      |        "share": {
      |           "intro": "En invitant vos proches à participer à la&nbsp;consultation&nbsp;:"
      |         }
      |      },
      |      "prompting-to-connect": {
      |         "title": "Soyez informé(e) des actions mises en&nbsp;oeuvre.",
      |         "intro": "Je m'identifie avec",
      |         "authenticate-with-email-cta": "Mon adresse email",
      |         "login-screen-access": {
      |           "intro": "J’ai déjà un compte&nbsp;!",
      |           "link-support": "Connexion"
      |         },
      |         "next-cta": "Non merci, je ne souhaite pas être informé(e) des résultats"
      |      }
      |    },
      |    "operation": {
      |      "intro": {
      |        "partners": {
      |          "intro": "avec"
      |        },
      |        "article": {
      |          "title": "Pourquoi cette consultation&nbsp;?",
      |          "see-more": {
      |            "label": "En savoir&nbsp;+"
      |          }
      |        }
      |      },
      |      "results": {
      |        "title": "Votez sur les&nbsp;propositions",
      |        "see-more": "Voir + de propositions",
      |        "no-results": "Nous n’avons trouvé <strong>aucun résultat</strong> correspondant à votre sélection de&nbsp;tag(s). <br> Vous pouvez sélectionner d’autres combinaisons de&nbsp;tags."
      |      },
      |      "proposal-form-in-header": {
      |        "intro": "Proposez vos solutions sur le&nbsp;sujet",
      |        "limit-of-chars-info": "8/140"
      |      },
      |      "submit-proposal": {
      |         "intro": "Partagez votre proposition"
      |      },
      |      "sequence": {
      |        "header": {
      |           "total-of-proposals": "%{total} propositions",
      |           "total-of-proposals_1": "%{total} proposition",
      |           "back-cta": "Accéder à<br>l'opération",
      |           "propose-cta": "Proposer",
      |           "guide": {
      |             "propose-cta": "Proposez une solution à tout moment en cliquant sur ce bouton."
      |           }
      |        }
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
      |        "error-message": "Ce lien n'est plus valable."
      |      }
      |    },
      |    "authenticate": {
      |      "forgot-password": {
      |        "intro": "Oups, j'ai",
      |        "link-support": "oublié mon mot de&nbsp;passe&nbsp;?"
      |      },
      |      "switch-to-register-screen": {
      |        "intro": "Je n'ai pas de compte,",
      |        "link-support": "je m'en crée&nbsp;un."
      |      },
      |      "switch-to-login-screen": {
      |        "intro": "J’ai déjà un compte&nbsp;!",
      |        "link-support": "Connexion"
      |      },
      |      "back-to-login-screen": {
      |        "intro": "Revenir à",
      |        "link-support": "l'écran de connexion"
      |      },
      |      "no-account-found": "Nous ne trouvons pas de compte associé à cet email.",
      |      "no-email-found": "Oups ! Nous ne parvenons pas à vous connecter. Merci de bien vouloir réessayer avec un mail valide.",
      |      "error-message": "Quelque chose n'a pas fonctionné. Si le problème persiste n'hésitez pas à nous contacter à <a href=\"mailto:contact@make.org\">contact@make.org</a>",
      |      "inputs": {
      |        "required": "(obligatoire)",
      |        "email": {
      |          "placeholder": "E-mail",
      |          "format-error-message": "Format d'email non&nbsp;reconnu",
      |          "empty-field-error-message": "Veuillez renseigner ce&nbsp;champ."
      |        },
      |        "first-name": {
      |          "placeholder": "Prénom",
      |          "format-error-message": "Format invalide",
      |          "empty-field-error-message": "Veuillez renseigner ce&nbsp;champ."
      |        },
      |        "password": {
      |          "placeholder": "Mot de passe",
      |          "format-error-message": "Votre mot de passe doit contenir au moins %{min}&nbsp;caractères.",
      |          "empty-field-error-message": "Veuillez renseigner ce&nbsp;champ."
      |        },
      |        "age": {
      |          "placeholder": "Âge",
      |          "format-error-message": "Format invalide"
      |        },
      |        "postal-code": {
      |          "placeholder": "Code postal",
      |          "format-error-message": "Format invalide"
      |        },
      |        "job": {
      |          "placeholder": "Profession",
      |          "format-error-message": "Format invalide"
      |        }
      |      },
      |      "recover-password": {
      |         "title": "Je réinitialise mon mot de&nbsp;passe",
      |         "info": "Merci de renseigner l'adresse email liée à votre compte pour recevoir le lien de&nbsp;réinitialisation.",
      |         "send-cta": "Recevoir l'e-mail",
      |         "error-message": {
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
      |          "error-message": "Votre mot de passe n'a pas pu être remplacé."
      |        }
      |      },
      |      "register": {
      |        "with-social-networks": {
      |          "intro": "Je m'inscris avec",
      |          "separator": "ou"
      |        },
      |        "caution": "(Rassurez-vous, nous ne publierons jamais rien sans votre&nbsp;accord.)",
      |        "separator": "ou",
      |        "with-email-intro": "Je m'inscris avec ce&nbsp;formulaire",
      |        "terms": "En vous inscrivant, vous acceptez nos <a href=\"https://about.make.org/conditions-dutilisation\" target=\"_blank\">conditions générales d'utilisation</a> ainsi que de recevoir ponctuellement des emails de&nbsp;Make.org.",
      |        "send-cta": "s'inscrire",
      |        "error-message": {
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
      |        "send-cta": "Se connecter"
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
      |          "label": "Mentions légales",
      |          "link": "https://about.make.org/mentions-legales"
      |        },
      |        "item-6": {
      |          "label": "Conditions d'utilisation",
      |          "link": "https://about.make.org/conditions-dutilisation"
      |        },
      |        "item-7": {
      |          "label": "Politique d'utilisation des données",
      |          "link": "https://about.make.org/politique-donnees"
      |        },
      |        "item-8": {
      |          "label": "Contact",
      |          "link": "https://about.make.org/contact"
      |        },
      |        "item-9": {
      |          "label": "f.a.q.",
      |          "link": "/"
      |        },
      |        "item-10": {
      |          "label": "sitemap",
      |          "link": "/"
      |        }
      |      }
      |    }
      |}
    """.stripMargin

  val translationsEn: String =
    """
      |{
      |    "common": {
      |       "bait": "We should"
      |    },
      |    "error-message": {
      |      "main": "Oops! We are having difficulties loading this page. Please reload it.",
      |      "no-token": "Token absent",
      |      "unexpected-behaviour": "Something did not work",
      |      "user-not-found": "Utilisateur non retrouvé"
      |    },
      |    "error": {
      |      "intro": "oops",
      |      "title": "Error 404",
      |      "recherche-intro": "We are sorry, the page you have requested no longer exists. You can initiate a search to find the content that interests you&hellip;",
      |      "redirection-intro": "&hellip; or let youself be tempted by a new destination:",
      |      "redirect-to-home": "La page d'accueil",
      |      "redirect-to-random-theme": "Un thème choisi au hasard"
      |    },
      |    "maintenance": {
      |      "intro": "oops",
      |      "title": "Maintenance in progress",
      |      "explanation-1": "Pour pouvoir vous proposer un site toujours plus&nbsp;performant,<br>Make.org est actuellement en&nbsp;maintenance.",
      |      "explanation-2": "Please come back in a few minutes."
      |    },
      |    "cookie-alert": "By continuing your navigation on our site, you hereby accept the use of cookies providing you with a personalised and smooth experience.",
      |    "home": {
      |      "welcome": {
      |        "baseline": "About",
      |        "title": "Propose, vote, act",
      |        "subtitle": "Make.org is an independent initiative in the service of the general&nbsp;public.",
      |        "see-more": "To find out more",
      |        "see-more-link": "https://about.make.org/qui-sommes-nous"
      |      },
      |      "featured-operation": {
      |        "learn-more": "Découvrir les résultats"
      |      },
      |      "explanations": {
      |        "article-1": {
      |          "intro": "la politique ne suffit plus, faisons bouger les lignes&nbsp;ensemble",
      |          "title": "Votez, proposez,&nbsp;agissons",
      |          "item-1": "<strong>Votez</strong> pour les propositions que vous&nbsp;défendez",
      |          "item-2": "<strong>Proposez</strong> vos idées sur des thèmes qui vous&nbsp;inspirent",
      |          "item-3": "Et demain, <strong>initiez des actions</strong> concrètes sur le&nbsp;terrain",
      |          "see-more-link": "/",
      |          "see-more": "To find out more"
      |        },
      |        "article-2": {
      |          "intro": "who are we?",
      |          "title": "Make.org, neutre & indépendant",
      |          "text": "Make.org est une initiative civique, Européenne et&nbsp;indépendante.",
      |          "see-more-link": "https://about.make.org/qui-sommes-nous",
      |          "see-more": "To find out more"
      |        }
      |      },
      |      "showcase-1": {
      |        "intro": "The topic of the&nbsp;month",
      |        "news": ""
      |      },
      |      "showcase-2": {
      |        "intro": "express yourself!",
      |        "title": "The most popular proposals on"
      |      },
      |      "showcase-3": {
      |        "intro": "Decide between them!",
      |        "title": "The most debated proposals on"
      |      }
      |    },
      |    "theme-showcase": {
      |      "see-all": "See all the topic's proposals about %{themeName}",
      |      "prompting-to-propose-tile": {
      |        "intro": "What about you, what is your proposal?",
      |        "bait": "We should..."
      |      },
      |      "data-units": {
      |        "actions": "actions",
      |        "votes": "votes",
      |        "proposals": "proposals"
      |      },
      |      "filter": {
      |        "intro" : "Afficher les propositions&nbsp;:",
      |        "the-most-popular": "les + populaires",
      |        "the-most-original": "les + originales"
      |      }
      |    },
      |    "nav-in-themes": {
      |      "title": "All the&nbsp;topics",
      |      "total-of-actions": "%{total} actions",
      |      "total-of-actions_1": "%{total} action",
      |      "total-of-proposals": "%{total} proposals",
      |      "total-of-proposals_1": "%{total} proposal"
      |    },
      |    "main-header": {
      |      "title": "Make.org",
      |      "menu" : {
      |        "item-1": {
      |          "label": "Who are we?",
      |          "link": "https://about.make.org/qui-sommes-nous"
      |        }
      |      }
      |    },
      |    "search": {
      |      "form":{
      |        "placeholder": "Search for a proposal..."
      |      },
      |      "results": {
      |        "intro": "<strong>%{total} results</strong> for your search",
      |        "see-more": "See more proposals"
      |      },
      |      "no-results": {
      |        "intro": "We did not find <strong>any result</strong> for your search.",
      |        "prompting-to-propose": "Be the first person to formulate a proposal on this subject!",
      |        "propose-cta": "Propose"
      |      }
      |    },
      |    "user-nav": {
      |      "login": "Login",
      |      "register": "Register"
      |    },
      |    "user-profile": {
      |      "title": "My personal information",
      |      "disconnect-cta": "I am logging out"
      |    },
      |    "proposal": {
      |      "created-by-user": "My proposal",
      |      "share-intro": "Share this proposal with your community",
      |      "associated-with-the-theme": "published in ",
      |      "proposal-s-operation-infos": {
      |        "intro": "Cette proposition a été soumise dans le cadre de l'opération",
      |        "participate": "Participer",
      |        "see-more": "To find out more"
      |      },
      |      "author-infos": {
      |        "age": ", %{age} ans",
      |        "postal-code": " (%{postalCode})",
      |        "anonymous": "anonyme"
      |      },
      |      "vote": {
      |       "partOfVotes": "(%{value}%)",
      |        "agree": {
      |          "label": "Agree",
      |          "qualifications":{
      |            "likeIt": "Remarkable",
      |            "doable": "Realistic",
      |            "platitudeAgree": "Commonplace"
      |          }
      |        },
      |        "disagree": {
      |          "label": "Disagree",
      |          "qualifications":{
      |            "noWay": "Definitely not!",
      |            "impossible": "Impractical",
      |            "platitudeDisagree": "Commonplace"
      |          }
      |        },
      |        "neutral": {
      |          "label": "Blank vote",
      |          "qualifications":{
      |            "doNotUnderstand": "Did not understand",
      |            "noOpinion": "No opinion",
      |            "doNotCare": "Indifferent"
      |          }
      |        }
      |      },
      |      "qualificate-vote": {
      |        "increment": "+1"
      |      }
      |    },
      |    "submit-proposal": {
      |      "intro": "Share your proposal",
      |      "error-message": "Une erreur s'est produite, réessayez dans quelques minutes.",
      |      "form": {
      |        "proposal-input-placeholder": "The proposal should be realistic and respectful of everyone",
      |        "limit-of-chars-reached-alert": "Oops! You have exceeded the limit of 140 characters 😅 Try to be more precise.&nbsp;🙏",
      |        "info": "Don't worry, we will correct any spelling mistakes.",
      |        "moderation-charter": "To find out more about our moderation charter, <a href=\"https://about.make.org/moderation\" target=\"_blank\">click&nbsp;here.</a>",
      |        "validate-cta": "Propose",
      |        "error-message": {
      |          "limit-of-chars-exceeded": "You have exceeded the character limit.",
      |          "not-enough-chars": "Your proposal must contain at least %{min} characters."
      |        }
      |      },
      |      "authenticate": {
      |        "intro": "We need some information to validate your proposal",
      |        "title": "To validate your&nbsp;proposal"
      |      },
      |      "confirmation": {
      |         "title": "Thank you!",
      |         "info": "Your proposal has been taken into consideration, it will now be reviewed by our moderation service.<br>You will receive an email when it has been validated.",
      |         "back-to-theme-cta": "Back to the subject&nbsp;%{theme}",
      |         "back-cta": "Retour",
      |         "new-proposal-cta": "Submit a new&nbsp;proposal"
      |      }
      |    },
      |    "political-actions": {
      |      "intro": "%{actions} actions from your proposals",
      |      "intro_0": "%{actions} action from your proposals",
      |      "intro_1": "%{actions} action from your proposals"
      |    },
      |    "no-political-action": {
      |      "intro": "We do not have an action to propose on this topic at the present time, but we are working on&nbsp;it!",
      |      "text": "Come back soon to work together!"
      |    },
      |    "theme": {
      |      "results": {
      |         "title": "Vote on the&nbsp;proposals",
      |         "see-more": "See more proposals",
      |         "no-results": "We did not find <strong>any result</strong> corresponding to your tag selection. <br>Please make another selection."
      |      },
      |      "proposal-form-in-header": {
      |        "limit-of-chars-info": "8/140"
      |      },
      |      "submit-proposal": {
      |         "intro": "Share your proposal on the&nbsp;subject"
      |      }
      |    },
      |    "sequence": {
      |      "proposal": {
      |        "next-cta": "Proposition suivante"
      |      },
      |      "guide": {
      |        "vote": "Use the buttons to vote<br>and to go to the next proposal.",
      |        "qualification": "If you wish, you can clarify your vote. (Otherwise, go to the next proposal.)"
      |      },
      |      "introduction": {
      |        "title": "Des milliers de citoyens proposent des&nbsp;solutions.",
      |        "explanation-1": "Prenez position sur ces solutions et proposez les&nbsp;vôtres.",
      |        "explanation-2": "The ones with the most support will determine our&nbsp;actions.",
      |        "cta": "Start"
      |      },
      |      "prompting-to-propose": {
      |         "intro": "What about you, do you have a solution to propose on this&nbsp;subject?",
      |         "propose-cta": "Propose",
      |         "next-cta": "Not yet, I will continue voting"
      |      },
      |      "prompting-to-continue": {
      |         "intro": "Thank you for your contribution. Continue to be involved with this cause:",
      |         "continue": {
      |           "intro": "By continuing the consultation:",
      |           "cta": "Start"
      |         },
      |         "learn-more": {
      |           "intro": "By discovering the full operation",
      |           "cta": "To find out more"
      |         },
      |        "share": {
      |           "intro": "By inviting your close family members to participate in the consultation:"
      |         }
      |      },
      |      "prompting-to-connect": {
      |         "title": "Be informed about the actions implemented.",
      |         "intro": "I identify myself with",
      |         "authenticate-with-email-cta": "Mon adresse email",
      |         "login-screen-access": {
      |           "intro": "I already have an account!",
      |           "link-support": "Connection"
      |         },
      |         "next-cta": "No thanks, I do not wish to be informed of the results"
      |      }
      |    },
      |    "operation": {
      |      "intro": {
      |        "partners": {
      |          "intro": "with"
      |        },
      |        "article": {
      |          "title": "Why this consultation&nbsp;?",
      |          "see-more": {
      |            "label": "To find out more"
      |          }
      |        }
      |      },
      |      "results": {
      |        "title": "Vote on the&nbsp;proposals",
      |        "see-more": "See more proposals",
      |        "no-results": "We did not find <strong>any result</strong> corresponding to your tag selection. <br>Please make another selection."
      |      },
      |      "proposal-form-in-header": {
      |        "intro": "Propose your solutions on the subject",
      |        "limit-of-chars-info": "8/140"
      |      },
      |      "submit-proposal": {
      |         "intro": "Share your proposal"
      |      },
      |      "sequence": {
      |        "header": {
      |           "total-of-proposals": "%{total} proposals",
      |           "total-of-proposals_1": "%{total} proposal",
      |           "back-cta": "Access<br>the operation",
      |           "propose-cta": "Propose",
      |           "guide": {
      |             "propose-cta": "Submit a solution at any time by clicking this button."
      |           }
      |        }
      |      }
      |    },
      |    "tags": {
      |      "filter": {
      |        "intro": "Filter by tags:"
      |      },
      |      "list": {
      |        "show-all": "See all the&nbsp;tags",
      |        "show-less": "hide"
      |      }
      |    },
      |    "activate-account": {
      |      "notifications": {
      |        "success": "Votre compte vient d'être activé. Vous pouvez vous connecter dès&nbsp;maintenant",
      |        "error-message": "This link is no longer valid."
      |      }
      |    },
      |    "authenticate": {
      |      "forgot-password": {
      |        "intro": "Oops, I",
      |        "link-support": "forgot my password?"
      |      },
      |      "switch-to-register-screen": {
      |        "intro": "I do not have an account,",
      |        "link-support": "je m'en crée&nbsp;un."
      |      },
      |      "switch-to-login-screen": {
      |        "intro": "I already have an account!",
      |        "link-support": "Connection"
      |      },
      |      "back-to-login-screen": {
      |        "intro": "Revenir à",
      |        "link-support": "l'écran de connexion"
      |      },
      |      "no-account-found": "We cannot find an account associated with this email.",
      |      "no-email-found": "Oops! We are unable to get you connected. Please try again with a valid email.",
      |      "error-message": "Something did not work. If the problem persists, do not hesitate to contact us at <a href=\"mailto:contact@make.org\">contact@make.org</a>",
      |      "inputs": {
      |        "required": "(required)",
      |        "email": {
      |          "placeholder": "E-mail",
      |          "format-error-message": "Email format not&nbsp;recognised",
      |          "empty-field-error-message": "Please complete this&nbsp;field."
      |        },
      |        "first-name": {
      |          "placeholder": "Firstname",
      |          "format-error-message": "Invalid format",
      |          "empty-field-error-message": "Please complete this&nbsp;field."
      |        },
      |        "password": {
      |          "placeholder": "Password",
      |          "format-error-message": "Your password must contain at least %{min}&nbsp;characters.",
      |          "empty-field-error-message": "Please complete this&nbsp;field."
      |        },
      |        "age": {
      |          "placeholder": "Age",
      |          "format-error-message": "Invalid format"
      |        },
      |        "postal-code": {
      |          "placeholder": "Post code",
      |          "format-error-message": "Invalid format"
      |        },
      |        "job": {
      |          "placeholder": "Profession",
      |          "format-error-message": "Invalid format"
      |        }
      |      },
      |      "recover-password": {
      |         "title": "I am resetting my&nbsp;password",
      |         "info": "Please enter the email address associated with your account in order to receive the reset&nbsp;link",
      |         "send-cta": "Recevoir l'e-mail",
      |         "error-message": {
      |           "mail-not-found": "We cannot find an account associated with this&nbsp;email."
      |         },
      |         "notifications": {
      |           "success": "Thank you, an email was just sent to you to reset your&nbsp;password."
      |         }
      |      },
      |      "reset-password":{
      |        "title": "I am creating a new&nbsp;password",
      |        "info": "You can choose a new&nbsp;password.",
      |        "send-cta": "Validate",
      |        "success": {
      |          "title": "Thank you, your password has been updated.",
      |          "info": "Vous pouvez vous connecter dès&nbsp;maintenant."
      |        },
      |        "failure" : {
      |          "title": "This link is no longer valid."
      |        },
      |        "notifications": {
      |          "error-message": "Votre mot de passe n'a pas pu être remplacé."
      |        }
      |      },
      |      "register": {
      |        "with-social-networks": {
      |          "intro": "I am registering with",
      |          "separator": "or"
      |        },
      |        "caution": "(Rest assured, nothing will be published without your&nbsp;consent.)",
      |        "separator": "or",
      |        "with-email-intro": "Je m'inscris avec ce&nbsp;formulaire",
      |        "terms": "By registering, you agree to our  <a href=\"https://about.make.org/conditions-dutilisation\" target=\"_blank\">terms of use</a> and to occasionally receive emails from&nbsp;Make.org.",
      |        "send-cta": "register",
      |        "error-message": {
      |          "already-exists": "This account already exists, thank you for logging&nbsp;in."
      |        },
      |        "notifications": {
      |          "success": "Your account has been successfully created. Remember to confirm your email address using the link sent to you - to ensure the account is yours."
      |        }
      |      },
      |      "login": {
      |        "with-social-networks-intro": "I am logging in&nbsp;with",
      |        "separator": "or",
      |        "with-email-intro": "I am logging in with my email&nbsp;address",
      |        "send-cta": "Login"
      |      }
      |    },
      |    "main-footer": {
      |      "menu": {
      |        "item-1": {
      |          "label": "Become a Maker!",
      |          "link": "/"
      |        },
      |        "item-2": {
      |          "label": "Jobs",
      |          "link": "https://about.make.org/jobs"
      |        },
      |        "item-3": {
      |          "label": "Who are we?",
      |          "link": "https://about.make.org/qui-sommes-nous"
      |        },
      |        "item-4": {
      |          "label": "Press area",
      |          "link": "https://about.make.org/category/presse"
      |        },
      |        "item-5": {
      |          "label": "Legal notice",
      |          "link": "https://about.make.org/mentions-legales"
      |        },
      |        "item-6": {
      |          "label": "Terms of use",
      |          "link": "https://about.make.org/conditions-dutilisation"
      |        },
      |        "item-7": {
      |          "label": "Data use policy",
      |          "link": "https://about.make.org/politique-donnees"
      |        },
      |        "item-8": {
      |          "label": "Contact",
      |          "link": "https://about.make.org/contact"
      |        },
      |        "item-9": {
      |          "label": "f.a.q.",
      |          "link": "/"
      |        },
      |        "item-10": {
      |          "label": "sitemap",
      |          "link": "/"
      |        }
      |      }
      |    }
      |}
    """.stripMargin

  val translationsIt: String =
    """
      |{
      |    "common": {
      |       "bait": "C'è bisogno"
      |    },
      |    "error-message": {
      |      "main": "Ops! Abbiamo riscontrato delle difficoltà con il caricamento di questa pagine. Ricarica la pagina per piacere.",
      |      "no-token": "Token absent",
      |      "unexpected-behaviour": "Qualcosa è andato storto",
      |      "user-not-found": "Utilisateur non retrouvé"
      |    },
      |    "error": {
      |      "intro": "ops",
      |      "title": "Errore 404",
      |      "recherche-intro": "Ci dispiace, la pagina che cerchi non esiste più. Puoi provare a cercare il contenuto che ti interessa&hellip;",
      |      "redirection-intro": "&hellip; o lasciarti tentare da una nuova destinazione:",
      |      "redirect-to-home": "La page d'accueil",
      |      "redirect-to-random-theme": "Un thème choisi au hasard"
      |    },
      |    "maintenance": {
      |      "intro": "ops",
      |      "title": "Manutenzione in corso",
      |      "explanation-1": "Pour pouvoir vous proposer un site toujours plus&nbsp;performant,<br>Make.org est actuellement en&nbsp;maintenance.",
      |      "explanation-2": "Non esitare a tornare tra qualche minuto."
      |    },
      |    "cookie-alert": "Continuando la navigazione sul sito accetti l'utilizzo dei cookies per proporti un'esperienza fluida e personalizzata.",
      |    "home": {
      |      "welcome": {
      |        "baseline": "A proposito",
      |        "title": "Proponi, Vota, Agiamo",
      |        "subtitle": "Make.org è un'iniziativa indipendente al servizio dell'interesse&nbsp;generale.",
      |        "see-more": "Per saperne di&nbsp;più ",
      |        "see-more-link": "https://about.make.org/qui-sommes-nous"
      |      },
      |      "featured-operation": {
      |        "learn-more": "Découvrir les résultats"
      |      },
      |      "explanations": {
      |        "article-1": {
      |          "intro": "la politique ne suffit plus, faisons bouger les lignes&nbsp;ensemble",
      |          "title": "Votez, proposez,&nbsp;agissons",
      |          "item-1": "<strong>Votez</strong> pour les propositions que vous&nbsp;défendez",
      |          "item-2": "<strong>Proposez</strong> vos idées sur des thèmes qui vous&nbsp;inspirent",
      |          "item-3": "Et demain, <strong>initiez des actions</strong> concrètes sur le&nbsp;terrain",
      |          "see-more-link": "/",
      |          "see-more": "Per saperne di&nbsp;più"
      |        },
      |        "article-2": {
      |          "intro": "chi siamo?",
      |          "title": "Make.org, neutre & indépendant",
      |          "text": "Make.org est une initiative civique, Européenne et&nbsp;indépendante.",
      |          "see-more-link": "https://about.make.org/qui-sommes-nous",
      |          "see-more": "Per saperne di&nbsp;più"
      |        }
      |      },
      |      "showcase-1": {
      |        "intro": "Tema del&nbsp;mese",
      |        "news": ""
      |      },
      |      "showcase-2": {
      |        "intro": "Esprimiti!",
      |        "title": "Le proposte più popolari su"
      |      },
      |      "showcase-3": {
      |        "intro": "Scegli tra loro",
      |        "title": "Le proposte più popolari su"
      |      }
      |    },
      |    "theme-showcase": {
      |      "see-all": "Vedere tutte le proposte sul tema %{themeName}",
      |      "prompting-to-propose-tile": {
      |        "intro": "E tu? Qual è la tua proposta?",
      |        "bait": "C'è bisogno..."
      |      },
      |      "data-units": {
      |        "actions": "azioni",
      |        "votes": "voti",
      |        "proposals": "proposte"
      |      },
      |      "filter": {
      |        "intro" : "Afficher les propositions&nbsp;:",
      |        "the-most-popular": "les + populaires",
      |        "the-most-original": "les + originales"
      |      }
      |    },
      |    "nav-in-themes": {
      |      "title": "Tutti i&nbsp;temi",
      |      "total-of-actions": "%{total} azioni",
      |      "total-of-actions_1": "%{total} azione",
      |      "total-of-proposals": "%{total} proposte",
      |      "total-of-proposals_1": "%{total} proposte"
      |    },
      |    "main-header": {
      |      "title": "Make.org",
      |      "menu" : {
      |        "item-1": {
      |          "label": "Chi siamo?",
      |          "link": "https://about.make.org/qui-sommes-nous"
      |        }
      |      }
      |    },
      |    "search": {
      |      "form":{
      |        "placeholder": "Ricerca una proposta..."
      |      },
      |      "results": {
      |        "intro": "<strong>%{total} risultati</strong> dalla tua ricerca",
      |        "see-more": "Vedi&nbsp;più proposte"
      |      },
      |      "no-results": {
      |        "intro": "Non abbiamo trovato <strong>nessuno risultato</strong> per la tua ricerca.",
      |        "prompting-to-propose": "Sii la prima persona a formualr euna proposta su questo soggetto!",
      |        "propose-cta": "Proponi"
      |      }
      |    },
      |    "user-nav": {
      |      "login": "Accedi",
      |      "register": "Registrati"
      |    },
      |    "user-profile": {
      |      "title": "Le mie informazioni personali",
      |      "disconnect-cta": "Disconnetti"
      |    },
      |    "proposal": {
      |      "created-by-user": "La mia proposta",
      |      "share-intro": "Condividi questa proposta con la tua communità",
      |      "associated-with-the-theme": "pubblicato in ",
      |      "proposal-s-operation-infos": {
      |        "intro": "Cette proposition a été soumise dans le cadre de l'opération",
      |        "participate": "Participer",
      |        "see-more": "Per saperne di&nbsp;più "
      |      },
      |      "author-infos": {
      |        "age": ", %{age} ans",
      |        "postal-code": " (%{postalCode})",
      |        "anonymous": "anonyme"
      |      },
      |      "vote": {
      |       "partOfVotes": "(%{value}%)",
      |        "agree": {
      |          "label": "D'accordo",
      |          "qualifications":{
      |            "likeIt": "Ammirabile",
      |            "doable": "Realistico",
      |            "platitudeAgree": "Banalità"
      |          }
      |        },
      |        "disagree": {
      |          "label": "Contrario",
      |          "qualifications":{
      |            "noWay": "Assolutamente no!",
      |            "impossible": "Infattibile",
      |            "platitudeDisagree": "Banalità"
      |          }
      |        },
      |        "neutral": {
      |          "label": "Voto Bianco",
      |          "qualifications":{
      |            "doNotUnderstand": "Difficile da capire",
      |            "noOpinion": "Niente da dire",
      |            "doNotCare": "Indifferente"
      |          }
      |        }
      |      },
      |      "qualificate-vote": {
      |        "increment": "+1"
      |      }
      |    },
      |    "submit-proposal": {
      |      "intro": "Condividi la tua proposta",
      |      "error-message": "Une erreur s'est produite, réessayez dans quelques minutes.",
      |      "form": {
      |        "proposal-input-placeholder": "C'è bisogno di una proposta realista e che rispetti tutti",
      |        "limit-of-chars-reached-alert": "Ops! Hai già superato il limite di 140 caratteri. 😅 Prova a essere più concisa/o.&nbsp;🙏",
      |        "info": "Non preoccuparti, correggeremo tutti gli eventuali errori&nbsp;ortografici.",
      |        "moderation-charter": "Per saperne di più sulla nostra carta di moderazione, <a href=\"https://about.make.org/moderation\" target=\"_blank\">clicca&nbsp;qui.</a>",
      |        "validate-cta": "Proponi",
      |        "error-message": {
      |          "limit-of-chars-exceeded": "Hai superato il limite massimo di caratteri.",
      |          "not-enough-chars": "La proposta deve essere di almeno %{min} caratteri."
      |        }
      |      },
      |      "authenticate": {
      |        "intro": "Abbiamo bisogno di quale informazione Per confermare la tua proposta",
      |        "title": "Per confermare la tua&nbsp;proposta"
      |      },
      |      "confirmation": {
      |         "title": "Grazie!",
      |         "info": "La tua proposta è stata ricevuta, sarà ora ispezionata dal nostro servizio di moderazione.<br>Riceverai una e-mail quando sarà&nbsp;validata.",
      |         "back-to-theme-cta": "Ritorna al tema&nbsp;%{theme}",
      |         "back-cta": "Retour",
      |         "new-proposal-cta": "Creare una nuova&nbsp;proposta"
      |      }
      |    },
      |    "political-actions": {
      |      "intro": "%{actions} azioni generate dalle tue proposte",
      |      "intro_0": "%{actions} azione generate dalle tue proposte",
      |      "intro_1": "%{actions} azione generate dalle tue proposte"
      |    },
      |    "no-political-action": {
      |      "intro": "Non abbiamo azioni da proporti su questo tema per il momento, ma ci stiamo lavorando!",
      |      "text": "Ritorna presto per agire insieme"
      |    },
      |    "theme": {
      |      "results": {
      |         "title": "Vota le&nbsp;proposte",
      |         "see-more": "Vedi&nbsp;più proposte",
      |         "no-results": "Non abbiamo trovato <strong>nessuno risultato</strong> corrispondente alla tua selezione di tag. <br>Fai un'altra selezione."
      |      },
      |      "proposal-form-in-header": {
      |        "limit-of-chars-info": "8/140"
      |      },
      |      "submit-proposal": {
      |         "intro": "Condividi la tua proposta sul&nbsp;tema"
      |      }
      |    },
      |    "sequence": {
      |      "proposal": {
      |        "next-cta": "Proposition suivante"
      |      },
      |      "guide": {
      |        "vote": "Usa i bottini per votare <br>e passare alla prossima proposta.",
      |        "qualification": "Si lo desideri, puoi chiarire il tuo voto (Altrimenti, passa alla prossima proposta)"
      |      },
      |      "introduction": {
      |        "title": "Des milliers de citoyens proposent des&nbsp;solutions.",
      |        "explanation-1": "Prenez position sur ces solutions et proposez les&nbsp;vôtres.",
      |        "explanation-2": "Le più sostenute determineranno le nostre&nbsp;azioni",
      |        "cta": "Inizia"
      |      },
      |      "prompting-to-propose": {
      |         "intro": "E tu, hai una soluzione da proporre su questo&nbsp;soggetto?",
      |         "propose-cta": "Proponi",
      |         "next-cta": "Non ancora, contunare a votare"
      |      },
      |      "prompting-to-continue": {
      |         "intro": "Grazie per il tuo contributo. Continua a impegnarti per questa causa:",
      |         "continue": {
      |           "intro": "val la: Nothing = null",
      |           "cta": "Inizia"
      |         },
      |         "learn-more": {
      |           "intro": "Scoprendo l'insieme delle&nbsp;operazioni",
      |           "cta": "Per saperne di&nbsp;più"
      |         },
      |        "share": {
      |           "intro": "Invitando i tuoi amici a partecipare alla consultazione:"
      |         }
      |      },
      |      "prompting-to-connect": {
      |         "title": "Resta infomato sulle azioni messe in&nbsp;atto.",
      |         "intro": "Mi identifico con",
      |         "authenticate-with-email-cta": "Mon adresse email",
      |         "login-screen-access": {
      |           "intro": "Ho già un account!",
      |           "link-support": "Connessione"
      |         },
      |         "next-cta": "No grazie, non voglio tenermi informata/o dei risultati"
      |      }
      |    },
      |    "operation": {
      |      "intro": {
      |        "partners": {
      |          "intro": "con"
      |        },
      |        "article": {
      |          "title": "Perché questa consutazione&nbsp;?",
      |          "see-more": {
      |            "label": "Per saperne di&nbsp;più"
      |          }
      |        }
      |      },
      |      "results": {
      |        "title": "Vota le&nbsp;proposte",
      |        "see-more": "Vedi&nbsp;più proposte",
      |        "no-results": "Non abbiamo trovato <strong>nessuno risultato</strong> corrispondente alla tua selezione di tag. <br>Fai un'altra selezione."
      |      },
      |      "proposal-form-in-header": {
      |        "intro": "Proponi la tua soluzione sul soggetto",
      |        "limit-of-chars-info": "8/140"
      |      },
      |      "submit-proposal": {
      |         "intro": "Condividi la tua proposta"
      |      },
      |      "sequence": {
      |        "header": {
      |           "total-of-proposals": "%{total} proposte",
      |           "total-of-proposals_1": "%{total} proposte",
      |           "back-cta": "Accedi alle<br>operazioni",
      |           "propose-cta": "Proponi",
      |           "guide": {
      |             "propose-cta": "Proponi una solizione in ogni momento cliccando su questo bottone."
      |           }
      |        }
      |      }
      |    },
      |    "tags": {
      |      "filter": {
      |        "intro": "Ordina per tag:"
      |      },
      |      "list": {
      |        "show-all": "Vedi tutti i&nbsp;tag",
      |        "show-less": "nascondi"
      |      }
      |    },
      |    "activate-account": {
      |      "notifications": {
      |        "success": "Votre compte vient d'être activé. Vous pouvez vous connecter dès&nbsp;maintenant",
      |        "error-message": "Questo link non è più valido."
      |      }
      |    },
      |    "authenticate": {
      |      "forgot-password": {
      |        "intro": "Ops, ho",
      |        "link-support": "dimenticato la mia password?"
      |      },
      |      "switch-to-register-screen": {
      |        "intro": "Non ho un account,",
      |        "link-support": "je m'en crée&nbsp;un."
      |      },
      |      "switch-to-login-screen": {
      |        "intro": "Ho già un account!",
      |        "link-support": "Connessione"
      |      },
      |      "back-to-login-screen": {
      |        "intro": "Revenir à",
      |        "link-support": "l'écran de connexion"
      |      },
      |      "no-account-found": "Non troviamo nessun account associato a questa e-mail",
      |      "no-email-found": "Ops! Non riusciamo a connetterti. Sei pregato di riprovare con una e-mail valida.",
      |      "error-message": "Qualcosa è andato storto. Se il problema persiste non esitare a contattarci a <a href=\"mailto:contact@make.org\">contact@make.org</a>",
      |      "inputs": {
      |        "required": "(obbligatorio)",
      |        "email": {
      |          "placeholder": "E-mail",
      |          "format-error-message": "Formato e-mail sconosciuto",
      |          "empty-field-error-message": "Completa questo campo."
      |        },
      |        "first-name": {
      |          "placeholder": "Nome",
      |          "format-error-message": "Fomato sconosciuto",
      |          "empty-field-error-message": "Completa questo campo."
      |        },
      |        "password": {
      |          "placeholder": "Password",
      |          "format-error-message": "La password deve contenere almeno %{min}&nbsp;caratteri.",
      |          "empty-field-error-message": "Completa questo campo."
      |        },
      |        "age": {
      |          "placeholder": "Età",
      |          "format-error-message": "Fomato sconosciuto"
      |        },
      |        "postal-code": {
      |          "placeholder": "Codice postale",
      |          "format-error-message": "Fomato sconosciuto"
      |        },
      |        "job": {
      |          "placeholder": "Professione",
      |          "format-error-message": "Fomato sconosciuto"
      |        }
      |      },
      |      "recover-password": {
      |         "title": "Voglio reinstallare la mia&nbsp;password",
      |         "info": "Inserisci l'indirizzo e-mail collegato al tuo account per ricevere il link&nbsp;di&nbsp;ripristino.",
      |         "send-cta": "Recevoir l'e-mail",
      |         "error-message": {
      |           "mail-not-found": "Non troviamo nessun account associato a questa e-mail"
      |         },
      |         "notifications": {
      |           "success": "Grazie, abbiamo appena inviato una e-mail per ripristinare la tua&nbsp;password."
      |         }
      |      },
      |      "reset-password":{
      |        "title": "Voglio creare una nuova&nbsp;password",
      |        "info": "Scegli la nuova&nbsp;password.",
      |        "send-cta": "Conferma",
      |        "success": {
      |          "title": "Grazie, la tua nuova password è stata registrata.",
      |          "info": "Vous pouvez vous connecter dès&nbsp;maintenant."
      |        },
      |        "failure" : {
      |          "title": "Questo link non è più valido."
      |        },
      |        "notifications": {
      |          "error-message": "Votre mot de passe n'a pas pu être remplacé."
      |        }
      |      },
      |      "register": {
      |        "with-social-networks": {
      |          "intro": "Iscriversi con",
      |          "separator": "o"
      |        },
      |        "caution": "(Non proccuparti, non pubblichiamo niente senza il tuo consenso)",
      |        "separator": "o",
      |        "with-email-intro": "Je m'inscris avec ce&nbsp;formulaire",
      |        "terms": "Iscrivendoti, accetti le nostre <a href=\"https://about.make.org/conditions-dutilisation\" target=\"_blank\">condizioni generali d'utilizzo</a> e la recezione di e-mail da&nbsp;Make.org.",
      |        "send-cta": "registrati",
      |        "error-message": {
      |          "already-exists": "Account già esistente, connettiti."
      |        },
      |        "notifications": {
      |          "success": "Il tuo conto è stato creato con successo. Conferma il tuo indirizzo tramite il link che ti abbiamo inviato - per assicurarci che sei proprio tu."
      |        }
      |      },
      |      "login": {
      |        "with-social-networks-intro": "Accedi&nbsp;con",
      |        "separator": "o",
      |        "with-email-intro": "Accedi con l'indirzzo e-mail",
      |        "send-cta": "Accedi"
      |      }
      |    },
      |    "main-footer": {
      |      "menu": {
      |        "item-1": {
      |          "label": "Divenare Maker!",
      |          "link": "/"
      |        },
      |        "item-2": {
      |          "label": "Lavori",
      |          "link": "https://about.make.org/jobs"
      |        },
      |        "item-3": {
      |          "label": "Chi siamo?",
      |          "link": "https://about.make.org/qui-sommes-nous"
      |        },
      |        "item-4": {
      |          "label": "Spazio stampa",
      |          "link": "https://about.make.org/category/presse"
      |        },
      |        "item-5": {
      |          "label": "Menzioni legali",
      |          "link": "https://about.make.org/mentions-legales"
      |        },
      |        "item-6": {
      |          "label": "Condizioni di utilizzo",
      |          "link": "https://about.make.org/conditions-dutilisation"
      |        },
      |        "item-7": {
      |          "label": "Politica di utilizzo dei dati",
      |          "link": "https://about.make.org/politique-donnees"
      |        },
      |        "item-8": {
      |          "label": "Contact",
      |          "link": "https://about.make.org/contact"
      |        },
      |        "item-9": {
      |          "label": "f.a.q.",
      |          "link": "/"
      |        },
      |        "item-10": {
      |          "label": "sitemap",
      |          "link": "/"
      |        }
      |      }
      |    }
      |}
    """.stripMargin

  val translations: String =
    s"""
       | {
       |   "fr": $translationsFr,
       |   "en": $translationsEn,
       |   "it": $translationsIt
       | }
    """.stripMargin

}
