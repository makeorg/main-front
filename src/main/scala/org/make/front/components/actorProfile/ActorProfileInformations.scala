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

package org.make.front.components.actorProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.actorProfile.ActorDescription.ActorDescriptionProps
import org.make.front.models.{Organisation => OrganisationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object ActorProfileInformations {

  final case class ActorProfileInformationsProps(actor: OrganisationModel,
                                                 activeTab: String,
                                                 changeActiveTab: String => Unit)
  final case class ActorProfileInformationsState(expandSlidingPannel: Boolean, previousTab: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[ActorProfileInformationsProps, ActorProfileInformationsState](
        displayName = "ActorProfileInformations",
        getInitialState = self => {
          ActorProfileInformationsState(expandSlidingPannel = false, previousTab = self.props.wrapped.activeTab)
        },
        componentWillReceiveProps = { (self, nextProps) =>
          self.setState(
            ActorProfileInformationsState(
              expandSlidingPannel = self.state.expandSlidingPannel,
              previousTab = self.props.wrapped.activeTab
            )
          )
        },
        render = self => {
          val actor: OrganisationModel = self.props.wrapped.actor
          val descriptionLength = actor.description.map(_.length).getOrElse(-1)

          <.div(^.className := ActorProfileInformationsStyles.wrapper)(
            <.div(^.className := ActorProfileInformationsStyles.avatarWrapper)(if (actor.avatarUrl.nonEmpty) {
              <.img(
                ^.src := actor.avatarUrl.get,
                ^.className := ActorProfileInformationsStyles.avatar,
                ^.alt := actor.organisationName.getOrElse(""),
                ^("data-pin-no-hover") := "true"
              )()
            } else {
              <.i(^.className := js.Array(ActorProfileInformationsStyles.avatarPlaceholder, FontAwesomeStyles.user))()
            }),
            <.div(^.className := ActorProfileInformationsStyles.personnalInformations)(
              <.h1(^.className := ActorProfileInformationsStyles.actorName)(actor.organisationName.map { name =>
                name
              }.toSeq)
            ),
            if (descriptionLength >= 150) {
              <.ActorDescriptionComponent(^.wrapped := ActorDescriptionProps(actor = self.props.wrapped.actor))()
            } else if (descriptionLength >= 15) {
              <.p(
                ^.className := js
                  .Array(ActorProfileInformationsStyles.biography, ActorProfileInformationsStyles.basicInformations)
              )(actor.description.map { description =>
                description
              }.toSeq)
            },
            <.style()(ActorProfileInformationsStyles.render[String])
          )
        }
      )
}

object ActorProfileInformationsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)",
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(padding(20.pxToEm()), marginTop(-49.pxToEm()))
    )

  val headerInfosWrapper: StyleA =
    style(display.flex, ThemeStyles.MediaQueries.beyondLargeMedium(flexFlow := s"column"))

  val avatarWrapper: StyleA =
    style(
      position.relative,
      width(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
      height(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
      marginTop(-40.pxToEm()),
      marginRight(ThemeStyles.SpacingValue.small.pxToEm()),
      overflow.hidden,
      backgroundColor(ThemeStyles.BackgroundColor.white),
      borderRadius(50.%%),
      border(2.px, solid, ThemeStyles.BorderColor.lighter),
      textAlign.center,
      ThemeStyles.MediaQueries.beyondLargeMedium(
        float.none,
        display.block,
        verticalAlign.middle,
        width(160.pxToEm()),
        height(160.pxToEm()),
        marginTop(-49.pxToEm()),
        borderWidth(5.px),
        marginLeft.auto,
        marginRight.auto
      )
    )

  val avatarPlaceholder: StyleA =
    style(
      width(100.%%),
      lineHeight(76.pxToEm(32)),
      fontSize(32.pxToEm()),
      color(ThemeStyles.TextColor.lighter),
      ThemeStyles.MediaQueries.beyondLargeMedium(lineHeight(150.pxToEm(64)), fontSize(64.pxToEm()))
    )

  val avatar: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      transform := s"translate(-50%, -50%)",
      width(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
      minWidth(100.%%),
      minHeight(100.%%),
      maxWidth.none,
      maxHeight.none,
      ThemeStyles.MediaQueries.beyondLargeMedium(width(160.pxToEm()))
    )

  val personnalInformations: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondLargeMedium(float.none),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(textAlign.center, marginTop(20.pxToEm()))
    )

  val noStyleFields: StyleA =
    style(width.auto, border.none, padding(`0`), margin(`0`))

  val userName: StyleA =
    style(
      TextStyles.title,
      fontSize(15.pxToEm()),
      lineHeight(1),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm()))
    )

  val basicInformations: StyleA =
    style(TextStyles.smallerText, color(ThemeStyles.TextColor.lighter))

  val marker: StyleA =
    style(marginRight(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val biography: StyleA =
    style(
      display.flex,
      transition := s"flex 0.25s ease-in",
      overflow.hidden,
      whiteSpace.preLine,
      ThemeStyles.MediaQueries.beyondLargeMedium(
        height.auto,
        paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
        paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()),
        borderTop(1.pxToEm(), solid, ThemeStyles.BorderColor.veryLight),
        borderBottom(1.pxToEm(), solid, ThemeStyles.BorderColor.veryLight),
        marginBottom(`0`)
      )
    )

  val slidingPannelButton: StyleA =
    style(
      ThemeStyles.Font.tradeGothicLTStd,
      color(ThemeStyles.ThemeColor.primary),
      fontSize(15.pxToEm()),
      lineHeight(17.pxToEm()),
      textTransform.uppercase,
      paddingTop(10.pxToEm(15)),
      paddingBottom(5.pxToEm(15)),
    )

  val slidingPannelSep: StyleA =
    style(height(1.pxToEm()), width(35.%%), backgroundColor(ThemeStyles.BorderColor.veryLight))

  val slidingPannelButtonGroup: StyleA =
    style(
      clear.left,
      display.flex,
      justifyContent.spaceAround,
      backgroundColor(ThemeStyles.BackgroundColor.white),
      marginTop(20.pxToEm()),
    )

  val actorName: StyleA =
    style(
      TextStyles.title,
      fontSize(15.pxToEm()),
      lineHeight(1),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm()))
    )
}
