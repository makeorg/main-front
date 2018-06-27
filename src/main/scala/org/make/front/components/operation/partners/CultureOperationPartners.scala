package org.make.front.components.operation.partners

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.core.Counter
import org.make.front.facades.Unescape.unescape
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.{I18n, accescultureAvatar, arteAvatar, bsfAvatar, cinemadifferenceAvatar, cssAvatar, cultureprioritaireAvatar, engiefondationAvatar, ministerecultureAvatar, sacemAvatar, telemaqueAvatar}

object CultureOperationPartners {


  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
      displayName = "CultureOperationPartners",

      render = self => {
        <.ul(^.className := PartnersStyles.wrapper)(
          <.li(^.className := PartnersStyles.item)(
            <.div(^.className := PartnersStyles.avatar)(
              <.img(^.src := engiefondationAvatar.toString,^.alt := "Engie Fondation")()
            )
          ),
          <.li(^.className := PartnersStyles.item)(
            <.div(^.className := PartnersStyles.avatar)(
              <.img(^.src := ministerecultureAvatar.toString,^.alt := "Ministère de la Culture")()
            )
          ),
          <.li(^.className := PartnersStyles.item)(
            <.div(^.className := PartnersStyles.avatar)(
              <.img(^.src := arteAvatar.toString,^.alt := "Arte")()
            )
          ),
          <.li(^.className := PartnersStyles.item)(
            <.div(^.className := PartnersStyles.avatar)(
              <.img(^.src := sacemAvatar.toString,^.alt := "Sacem")()
            )
          ),
          <.li(^.className := PartnersStyles.item)(
            <.div(^.className := PartnersStyles.avatar)(
              <.img(^.src := cultureprioritaireAvatar.toString,^.alt := "Culture Prioritaire")()
            )
          ),
          <.li(^.className := PartnersStyles.item)(
            <.div(^.className := PartnersStyles.avatar)(
              <.img(^.src := telemaqueAvatar.toString,^.alt := "Télémaque")()
            )
          ),
          <.li(^.className := PartnersStyles.item)(
            <.div(^.className := PartnersStyles.avatar)(
              <.img(^.src := cssAvatar.toString,^.alt := "Culture & Sport Solidaires")()
            )
          ),
          <.li(^.className := PartnersStyles.item)(
            <.div(^.className := PartnersStyles.avatar)(
              <.img(^.src := bsfAvatar.toString,^.alt := "Bibliothèques Sans Frontières")()
            )
          ),
          <.li(^.className := PartnersStyles.item)(
            <.div(^.className := PartnersStyles.avatar)(
              <.img(^.src := accescultureAvatar.toString,^.alt := "Accès Culture")()
            )
          ),
          <.li(^.className := PartnersStyles.item)(
            <.div(^.className := PartnersStyles.avatar)(
              <.img(^.src := cinemadifferenceAvatar.toString,^.alt := "Cinéma Différence")()
            )
          ),
          <.style()(
            PartnersStyles.render[String]
          )
        )
  })

}
