package org.make.front.components.consultation.partners

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.consultation.partners.PartnersItems.PartnersItemProps
import org.make.front.facades.{accescultureAvatar, arteAvatar, bsfAvatar, cinemadifferenceAvatar, cssAvatar, cultureprioritaireAvatar, engiefondationAvatar, ministerecultureAvatar, sacemAvatar, telemaqueAvatar}
import org.make.front.models.{OperationExpanded => OperationModel, OperationPartner => OperationPartnerModel}

import scala.scalajs.js

object CulturePartnersList {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
      displayName = "PartnersList",
      render = self => {

        val partners =
          js.Array(
            OperationPartnerModel(
              name = "Engie Fondation",
              imageUrl = engiefondationAvatar.toString,
              imageWidth = 60,
              isFounder = true
            ),
            OperationPartnerModel(
              name = "Ministère de la Culture",
              imageUrl = ministerecultureAvatar.toString,
              imageWidth = 60,
              isFounder = true
            ),
            OperationPartnerModel(
              name = "Arte",
              imageUrl = arteAvatar.toString,
              imageWidth = 60,
              isFounder = false
            ),
            OperationPartnerModel(
              name = "Sacem",
              imageUrl = sacemAvatar.toString,
              imageWidth = 60,
              isFounder = false
            ),
            OperationPartnerModel(
              name = "Culture Prioritaire",
              imageUrl = cultureprioritaireAvatar.toString,
              imageWidth = 60,
              isFounder = false
            ),
            OperationPartnerModel(
              name = "Télémaque",
              imageUrl = telemaqueAvatar.toString,
              imageWidth = 60,
              isFounder = false
            ),
            OperationPartnerModel(
              name = "Culture & Sport Solidaires",
              imageUrl = cssAvatar.toString,
              imageWidth = 60,
              isFounder = false
            ),
            OperationPartnerModel(
              name = "Bibliothèques Sans Frontières",
              imageUrl = bsfAvatar.toString,
              imageWidth = 60,
              isFounder = false
            ),
            OperationPartnerModel(
              name = "Accès Culture",
              imageUrl = accescultureAvatar.toString,
              imageWidth = 60,
              isFounder = false
            ),
            OperationPartnerModel(
              name = "Cinéma Différence",
              imageUrl = cinemadifferenceAvatar.toString,
              imageWidth = 60,
              isFounder = false
            )
          )

          <.ul(^.className := PartnersStyles.wrapper)(
            partners
              .map(
                partner =>
                  <.PartnersItemsComponent(
                    ^.wrapped := PartnersItemProps(
                      partnerName = partner.name,
                      partnerAvatar = partner.imageUrl,
                      founder = partner.isFounder
                    )
                  )()
              )
              .toSeq,
            <.style()(
              PartnersStyles.render[String]
            )
          )

      }
    )

}