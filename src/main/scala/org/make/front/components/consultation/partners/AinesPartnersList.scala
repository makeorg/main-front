/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.consultation.partners

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.consultation.partners.PartnersItems.PartnersItemProps
import org.make.front.facades.{ageVillage, armeeDuSalut, associationFranceDependance, careit, klesia, korian, laposte, lesTalentsDalphonse, ocirp, siel}
import org.make.front.models.{OperationPartner => OperationPartnerModel}

import scala.scalajs.js

object AinesPartnersList {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "PartnersList",
        render = self => {

          val partners =
            js.Array(
              OperationPartnerModel(
                name = "Klesia",
                imageUrl = klesia.toString,
                imageWidth = 60,
                isFounder = true
              ),
              OperationPartnerModel(
                name = "Korian",
                imageUrl = korian.toString,
                imageWidth = 60,
                isFounder = true
              ),
              OperationPartnerModel(
                name = "Laposte",
                imageUrl = laposte.toString,
                imageWidth = 60,
                isFounder = true
              ),
              OperationPartnerModel(
                name = "OCIRP",
                imageUrl = ocirp.toString,
                imageWidth = 60,
                isFounder = true
              ),
              OperationPartnerModel(
                name = "CAREIT",
                imageUrl = careit.toString,
                imageWidth = 60,
                isFounder = true
              ),
              OperationPartnerModel(
                name = "Les Talents d'Alphonse",
                imageUrl = lesTalentsDalphonse.toString,
                imageWidth = 60,
                isFounder = false
              ),
              OperationPartnerModel(
                name = "Siel Bleu",
                imageUrl = siel.toString,
                imageWidth = 60,
                isFounder = false
              ),
              OperationPartnerModel(
                name = "fondation de l'armée du salut",
                imageUrl = armeeDuSalut.toString,
                imageWidth = 60,
                isFounder = false
              ),
              OperationPartnerModel(
                name = "Age village",
                imageUrl = ageVillage.toString,
                imageWidth = 60,
                isFounder = false
              ),
              OperationPartnerModel(
                name = "Association France Dépendance",
                imageUrl = associationFranceDependance.toString,
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
            <.style()(PartnersStyles.render[String])
          )

        }
      )

}
