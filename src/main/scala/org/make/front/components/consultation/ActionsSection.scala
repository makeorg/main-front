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

package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.styles.ThemeStyles
import org.make.front.Main.CssSettings._
import org.make.front.styles.utils._
import org.make.front.components.Components._
import org.make.front.components.authenticate.LoginOrRegister.LoginOrRegisterProps
import org.make.front.components.consultation.partners.PartnerList.PartnerListProps
import org.make.front.components.modals.Modal.ModalProps
import org.make.front.facades.{actions, actions2x, actions3x, clapping, logoMake, I18n}
import org.make.front.facades.Unescape.unescape
import org.make.front.models.OperationExpanded
import org.make.front.styles.base.{LayoutRulesStyles, RWDRulesLargeMediumStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object ActionsSection {

  case class ActionsSectionProps(operation: OperationExpanded, language: String, isConnected: Boolean)
  case class ActionsSectionState(isAuthenticateModalOpened: Boolean, loginOrRegisterView: String = "actions")

  lazy val reactClass: ReactClass =
    React
      .createClass[ActionsSectionProps, ActionsSectionState](displayName = "ActionsSection", getInitialState = { _ =>
        ActionsSectionState(isAuthenticateModalOpened = false)
      }, render = {
        self =>
          def linkPartner =
            self.props.wrapped.operation.wordings
              .find(_.language == self.props.wrapped.language)
              .flatMap(_.learnMoreUrl)
              .map(_ + "#partenaires")
              .getOrElse("/#/404")

          def trackingPartners: () => Unit = { () =>
            TrackingService
              .track(
                eventName = "click-see-more-community",
                trackingContext = TrackingContext(
                  TrackingLocation.operationPage,
                  operationSlug = Some(self.props.wrapped.operation.slug)
                )
              )
          }

          def openRegisterAuthenticateModal() = () => {
            self.setState(_.copy(isAuthenticateModalOpened = true, loginOrRegisterView = "actions"))
          }

          def toggleAuthenticateModal() = () => {
            self.setState(_.copy(isAuthenticateModalOpened = !self.state.isAuthenticateModalOpened))
          }

          <.div(^.className := ActionsSectionStyles.mainWrapper)(
            <.article(^.className := ActionsSectionStyles.main)(
              <.div(^.className := js.Array(ActionsSectionStyles.wrapper, LayoutRulesStyles.centeredRow))(
                <.h3(^.className := ActionsSectionStyles.title)(
                  <.img(^.className := ActionsSectionStyles.logo, ^.src := logoMake.toString, ^.alt := "Make.org")()
                ),
                <.p(
                  ^.className := js
                    .Array(TextStyles.mediumText, TextStyles.boldText, ActionsSectionStyles.presentationTextAlt)
                )(
                  unescape(I18n.t("operation.actions.goals")),
                  unescape(".")
                  /* Todo Uncomment when Webflow page is ready
                unescape("&nbsp;("),
                <.a(
                  ^.href := "#",
                  ^.className := js.Array(
                    TextStyles.smallerText,
                    ActionsSectionStyles.redLink),
                  ^.target := "_blank"
                )(unescape(I18n.t("operation.actions.know-more"))),
                unescape(").")*/
                ),
                if (!self.props.wrapped.isConnected) {
                  <.p(^.className := js.Array(TextStyles.smallerText, ActionsSectionStyles.presentationBait))(
                    unescape(I18n.t("operation.actions.register-bait"))
                  )
                },
                if (!self.props.wrapped.isConnected) {
                  <.button(
                    ^.onClick := openRegisterAuthenticateModal,
                    ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnA, ActionsSectionStyles.cta)
                  )(unescape(I18n.t("operation.actions.register-button")))
                },
                if (!self.props.wrapped.isConnected) {
                  <.ModalComponent(
                    ^.wrapped := ModalProps(
                      isModalOpened = self.state.isAuthenticateModalOpened,
                      closeCallback = toggleAuthenticateModal()
                    )
                  )(
                    <.LoginOrRegisterComponent(
                      ^.wrapped := LoginOrRegisterProps(
                        registerView = self.state.loginOrRegisterView,
                        displayView = self.state.loginOrRegisterView,
                        trackingContext =
                          TrackingContext(TrackingLocation.actionsPlaceholder, Some(self.props.wrapped.operation.slug)),
                        trackingParameters = Map("signup-type" -> "light"),
                        trackingInternalOnlyParameters = Map.empty,
                        onSuccessfulLogin = () => {
                          self.setState(_.copy(isAuthenticateModalOpened = false))
                        }
                      )
                    )()
                  )
                }
              ),
              <.div(^.className := js.Array(ActionsSectionStyles.wrapperAlt, LayoutRulesStyles.centeredRow))(
                <.h3(^.className := ActionsSectionStyles.title)(
                  <.img(^.className := ActionsSectionStyles.icon, ^.src := clapping.toString, ^.alt := "Make.org")(),
                  unescape(I18n.t("operation.actions.support-title"))
                ),
                <.p(^.className := js.Array(TextStyles.smallerText, ActionsSectionStyles.presentationText))(
                  unescape(I18n.t("operation.actions.support-text"))
                ),
                <.img(
                  ^.src := actions.toString,
                  ^("srcset") := actions2x.toString + " 2x, " + actions3x.toString + " 3x",
                  ^.alt := I18n.t("operation.actions.support-title"),
                  ^("data-pin-no-hover") := "true"
                )()
              )
            ),
            <.aside(^.className := ActionsSectionStyles.sidebar)(
              <.div(^.className := js.Array(ActionsSectionStyles.wrapper, LayoutRulesStyles.centeredRow))(
                <.h3(^.className := ActionsSectionStyles.title)(
                  unescape(I18n.t("operation.actions.actionsplan-title"))
                ),
                <.p(^.className := js.Array(TextStyles.smallerText, ActionsSectionStyles.presentationText))(
                  unescape(I18n.t("operation.actions.actionsplan-text"))
                ),
                <.p(
                  ^.className := js.Array(
                    TextStyles.smallerText,
                    ActionsSectionStyles.presentationText,
                    ActionsSectionStyles.presentationTextAlt
                  )
                )(unescape(I18n.t("operation.actions.actionsplan-list-title"))),
                <.p(
                  ^.className := js.Array(
                    TextStyles.smallerText,
                    ActionsSectionStyles.presentationText,
                    ActionsSectionStyles.presentationTextTertiary
                  )
                )(
                  unescape(I18n.t("operation.actions.actionsplan-list-intro")),
                  <.ul(^.className := ActionsSectionStyles.presentationText)(
                    <.li(^.className := ActionsSectionStyles.presentationListItem)(
                      unescape(I18n.t("operation.actions.actions-list-item1"))
                    ),
                    <.li(^.className := ActionsSectionStyles.presentationListItem)(
                      unescape(I18n.t("operation.actions.actions-list-item2"))
                    ),
                    <.li(^.className := ActionsSectionStyles.presentationListItem)(
                      unescape(I18n.t("operation.actions.actions-list-item3"))
                    )
                  )
                )
              ),
              if (self.props.wrapped.operation.partners.nonEmpty) {
                <.div(
                  ^.className := js
                    .Array(
                      ActionsSectionStyles.wrapper,
                      ActionsSectionStyles.eraseMargin,
                      LayoutRulesStyles.centeredRow
                    )
                )(
                  <.h3(^.className := ActionsSectionStyles.title)(unescape(I18n.t("operation.actions.partners-title"))),
                  <.p(^.className := js.Array(TextStyles.smallerText, ActionsSectionStyles.presentationText))(
                    unescape(I18n.t("operation.actions.partners-text"))
                  ),
                  <.PartnerListComponent(^.wrapped := PartnerListProps(self.props.wrapped.operation.partners))(),
                  <.a(
                    ^.onClick := trackingPartners,
                    ^.href := linkPartner,
                    ^.className := js.Array(TextStyles.smallerText, TextStyles.boldText, ActionsSectionStyles.redLink),
                    ^.target := "_blank"
                  )(unescape(I18n.t("operation.community.partner.see-more")))
                )
              },
              <.div(^.className := RWDRulesLargeMediumStyles.showBlockBeyondLargeMedium)(<.AltFooterComponent()())
            ),
            <.style()(ActionsSectionStyles.render[String])
          )
      })
}

object ActionsSectionStyles extends StyleSheet.Inline {

  import dsl._

  val mainWrapper: StyleA =
    style(
      display.flex,
      flexFlow := s"column",
      maxWidth(ThemeStyles.containerMaxWidth),
      marginLeft(auto),
      marginRight(auto),
      paddingTop(20.pxToEm()),
      paddingBottom(20.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        flexFlow := s"row",
        paddingTop(40.pxToEm()),
        paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
        paddingBottom(40.pxToEm()),
        paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val main: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondLargeMedium(maxWidth(750.pxToPercent(1140)), marginRight(30.pxToPercent(1140)))
    )

  val sidebar: StyleA =
    style(ThemeStyles.MediaQueries.beyondLargeMedium(maxWidth(360.pxToPercent(1140))))

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)",
      padding(20.pxToEm()),
      marginBottom(20.pxToEm())
    )

  val wrapperAlt: StyleA =
    style(
      padding(20.pxToEm(), ThemeStyles.SpacingValue.small.pxToEm(), ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(paddingLeft(`0`), paddingRight(`0`))
    )

  val eraseMargin: StyleA =
    style(marginBottom(`0`))

  val logo: StyleA =
    style(width(40.pxToEm(15)), ThemeStyles.MediaQueries.beyondLargeMedium(width(40.pxToEm(20))))

  val icon: StyleA =
    style(
      width(20.pxToEm(15)),
      marginRight(5.pxToEm(15)),
      ThemeStyles.MediaQueries.beyondLargeMedium(width(20.pxToEm(20)), marginRight(5.pxToEm(20)))
    )

  val title: StyleA =
    style(
      TextStyles.title,
      fontSize(15.pxToEm()),
      lineHeight(1),
      paddingBottom(ThemeStyles.SpacingValue.smaller.pxToEm(15)),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm())),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        marginBottom(ThemeStyles.SpacingValue.small.pxToEm(18)),
        borderBottom(1.pxToEm(18), solid, ThemeStyles.BorderColor.veryLight)
      )
    )

  val presentationText: StyleA =
    style(color(ThemeStyles.TextColor.lighter))

  val presentationTextAlt: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val presentationBait: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val presentationTextTertiary: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val presentationListItem: StyleA =
    style(display.block, marginTop(5.pxToEm(13)), ThemeStyles.MediaQueries.beyondLargeMedium(marginTop(5.pxToEm(16))))

  val cta: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val redLink: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

}
