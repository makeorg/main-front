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
      |    "content": {
      |       "header": {
      |         "profile": "Mon Profil",
      |         "settings": "Paramètres",
      |         "logout": "Se Déconnecter",
      |         "connect": "Se Connecter",
      |         "createAccount": "Créer un compte"
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
      |         "proposalsCount": "%{proposals} propositions"
      |       }
      |    },
      |    "form": {
      |    }
      |  },
      |  "en": {
      |    "content": {
      |       "header": {
      |         "profile": "My Profile",
      |         "settings": "Settings",
      |         "logout": "Logout",
      |         "connect": "Log In",
      |         "createAccount": "Create account"
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
      |         "proposalsCount": "%{proposals} propositions"
      |       }
      |    },
      |    "form": {
      |    }
      |  }
      |}
    """.stripMargin

}
