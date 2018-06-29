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

package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.theme.SubmitProposalInRelationToTheme.SubmitProposalInRelationToThemeProps
import org.make.front.facades._
import org.make.front.models.{GradientColor => GradientColorModel, TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.InputStyles
import org.make.front.styles.utils._
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

object ThemeHeader {

  case class ThemeIllustrationsModel(SmallIllUrl: String = "",
                                     SmallIllUrl2x: String = "",
                                     MediumIllUrl: String = "",
                                     MediumIllUrl2x: String = "",
                                     IllUrl: String = "",
                                     IllUrl2x: String = "")

  def themeIllustrations(theme: TranslatedThemeModel): ThemeIllustrationsModel = {
    theme.slug match {
      case "developpement-durable-energie" =>
        ThemeIllustrationsModel(
          SmallIllUrl = themeAgricultureRuraliteSmall.toString,
          SmallIllUrl2x = themeAgricultureRuraliteSmall2x.toString,
          MediumIllUrl = themeAgricultureRuraliteMedium.toString,
          MediumIllUrl2x = themeAgricultureRuraliteMedium2x.toString,
          IllUrl = themeAgricultureRuralite.toString,
          IllUrl2x = themeAgricultureRuralite2x.toString
        )
      case "agriculture-ruralite" =>
        ThemeIllustrationsModel(
          SmallIllUrl = themeAgricultureRuraliteSmall.toString,
          SmallIllUrl2x = themeAgricultureRuraliteSmall2x.toString,
          MediumIllUrl = themeAgricultureRuraliteMedium.toString,
          MediumIllUrl2x = themeAgricultureRuraliteMedium2x.toString,
          IllUrl = themeAgricultureRuralite.toString,
          IllUrl2x = themeAgricultureRuralite2x.toString
        )
      case "democratie-vie-politique" =>
        ThemeIllustrationsModel(
          SmallIllUrl = themeDemocratieViePolitiqueSmall.toString,
          SmallIllUrl2x = themeDemocratieViePolitiqueSmall2x.toString,
          MediumIllUrl = themeDemocratieViePolitiqueMedium.toString,
          MediumIllUrl2x = themeDemocratieViePolitiqueMedium2x.toString,
          IllUrl = themeDemocratieViePolitique.toString,
          IllUrl2x = themeDemocratieViePolitique2x.toString
        )
      case "economie-emploi-travail" =>
        ThemeIllustrationsModel(
          SmallIllUrl = themeEconomieEmploiTravailSmall.toString,
          SmallIllUrl2x = themeEconomieEmploiTravailSmall2x.toString,
          MediumIllUrl = themeEconomieEmploiTravailMedium.toString,
          MediumIllUrl2x = themeEconomieEmploiTravailMedium2x.toString,
          IllUrl = themeEconomieEmploiTravail.toString,
          IllUrl2x = themeEconomieEmploiTravail2x.toString
        )
      case "education" =>
        ThemeIllustrationsModel(
          SmallIllUrl = themeEducationSmall.toString,
          SmallIllUrl2x = themeEducationSmall2x.toString,
          MediumIllUrl = themeEducationMedium.toString,
          MediumIllUrl2x = themeEducationMedium2x.toString,
          IllUrl = themeEducation.toString,
          IllUrl2x = themeEducation2x.toString
        )
      case "europe-monde" =>
        ThemeIllustrationsModel(
          SmallIllUrl = themeEuropeMondeSmall.toString,
          SmallIllUrl2x = themeEuropeMondeSmall2x.toString,
          MediumIllUrl = themeEuropeMondeMedium.toString,
          MediumIllUrl2x = themeEuropeMondeMedium2x.toString,
          IllUrl = themeEuropeMonde.toString,
          IllUrl2x = themeEuropeMonde2x.toString
        )
      case "logement" =>
        ThemeIllustrationsModel(
          SmallIllUrl = themeLogementSmall.toString,
          SmallIllUrl2x = themeLogementSmall2x.toString,
          MediumIllUrl = themeLogementMedium.toString,
          MediumIllUrl2x = themeLogementMedium2x.toString,
          IllUrl = themeLogement.toString,
          IllUrl2x = themeLogement2x.toString
        )
      case "numerique-culture" =>
        ThemeIllustrationsModel(
          SmallIllUrl = themeNumeriqueCultureSmall.toString,
          SmallIllUrl2x = themeNumeriqueCultureSmall2x.toString,
          MediumIllUrl = themeNumeriqueCultureMedium.toString,
          MediumIllUrl2x = themeNumeriqueCultureMedium2x.toString,
          IllUrl = themeNumeriqueCulture.toString,
          IllUrl2x = themeNumeriqueCulture2x.toString
        )
      case "sante-alimentation" =>
        ThemeIllustrationsModel(
          SmallIllUrl = themeSanteAlimentationSmall.toString,
          SmallIllUrl2x = themeSanteAlimentationSmall2x.toString,
          MediumIllUrl = themeSanteAlimentationMedium.toString,
          MediumIllUrl2x = themeSanteAlimentationMedium2x.toString,
          IllUrl = themeSanteAlimentation.toString,
          IllUrl2x = themeSanteAlimentation2x.toString
        )
      case "securite-justice" =>
        ThemeIllustrationsModel(
          SmallIllUrl = themeSecuriteJusticeSmall.toString,
          SmallIllUrl2x = themeSecuriteJusticeSmall2x.toString,
          MediumIllUrl = themeSecuriteJusticeMedium.toString,
          MediumIllUrl2x = themeSecuriteJusticeMedium2x.toString,
          IllUrl = themeSecuriteJustice.toString,
          IllUrl2x = themeSecuriteJustice2x.toString
        )
      case "transports-deplacement" =>
        ThemeIllustrationsModel(
          SmallIllUrl = themeTransportsDeplacementSmall.toString,
          SmallIllUrl2x = themeTransportsDeplacementSmall2x.toString,
          MediumIllUrl = themeTransportsDeplacementMedium.toString,
          MediumIllUrl2x = themeTransportsDeplacementMedium2x.toString,
          IllUrl = themeTransportsDeplacement.toString,
          IllUrl2x = themeTransportsDeplacement2x.toString
        )
      case "vivre-ensemble-solidarites" =>
        ThemeIllustrationsModel(
          SmallIllUrl = themeVivreEnsembleSolidariteSmall.toString,
          SmallIllUrl2x = themeVivreEnsembleSolidariteSmall2x.toString,
          MediumIllUrl = themeVivreEnsembleSolidariteMedium.toString,
          MediumIllUrl2x = themeVivreEnsembleSolidariteMedium2x.toString,
          IllUrl = themeVivreEnsembleSolidarite.toString,
          IllUrl2x = themeVivreEnsembleSolidarite2x.toString
        )
      case _ =>
        ThemeIllustrationsModel(
          SmallIllUrl = welcomeSmall.toString,
          SmallIllUrl2x = welcomeSmall2x.toString,
          MediumIllUrl = welcomeMedium.toString,
          MediumIllUrl2x = welcomeMedium2x.toString,
          IllUrl = welcome.toString,
          IllUrl2x = welcome2x.toString
        )
    }
  }

  case class ThemeHeaderProps(theme: TranslatedThemeModel)

  case class ThemeHeaderState(isProposalModalOpened: Boolean, themeIllustrations: ThemeIllustrationsModel)

  lazy val reactClass: ReactClass =
    React.createClass[ThemeHeaderProps, ThemeHeaderState](
      displayName = "ThemeHeader",
      getInitialState = { _ =>
        ThemeHeaderState(isProposalModalOpened = false, themeIllustrations = ThemeIllustrationsModel())
      },
      componentDidMount = { (self) =>
        self.setState(_.copy(themeIllustrations = themeIllustrations(self.props.wrapped.theme)))
      },
      componentWillReceiveProps = { (self, nextProps) =>
        self.setState(_.copy(themeIllustrations = themeIllustrations(nextProps.wrapped.theme)))
      },
      render = { self =>
        var proposalInput: Option[HTMLElement] = None

        def closeProposalModal() = () => {
          self.setState(state => state.copy(isProposalModalOpened = false))
        }

        def openProposalModalFromInput() = () => {
          self.setState(state => state.copy(isProposalModalOpened = true))
          TrackingService.track(
            "click-proposal-submit-form-open",
            TrackingContext(TrackingLocation.themePage),
            Map("themeId" -> self.props.wrapped.theme.id.value)
          )
          proposalInput.foreach(_.blur())
        }

        val gradientValues: GradientColorModel =
          self.props.wrapped.theme.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

        object DynamicThemeHeaderStyles extends StyleSheet.Inline {
          import dsl._

          val gradient = style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
        }

        val imageSrc: String = self.state.themeIllustrations.IllUrl
        val imageSrcset
          : String = self.state.themeIllustrations.SmallIllUrl + " 400w, " + self.state.themeIllustrations.SmallIllUrl2x + " 800w, " + self.state.themeIllustrations.MediumIllUrl + " 840w, " + self.state.themeIllustrations.MediumIllUrl2x + " 1680w, " + self.state.themeIllustrations.IllUrl + " 1350w, " + self.state.themeIllustrations.IllUrl2x + " 2700w"

        <.header(
          ^.className := js
            .Array(TableLayoutStyles.wrapper, ThemeHeaderStyles.wrapper, DynamicThemeHeaderStyles.gradient)
        )(
          <.div(^.className := js.Array(TableLayoutStyles.cellVerticalAlignMiddle, ThemeHeaderStyles.innerWrapper))(
            <.img(
              ^.className := ThemeHeaderStyles.illustration,
              ^.src := imageSrc,
              ^("srcset") := imageSrcset,
              ^.alt := self.props.wrapped.theme.title,
              ^("data-pin-no-hover") := "true"
            )(),
            <.div(^.className := LayoutRulesStyles.centeredRow)(
              <.h1(^.className := js.Array(TextStyles.veryBigTitle, ThemeHeaderStyles.title))(
                self.props.wrapped.theme.title
              ),
              <.p(
                ^.className := js.Array(
                  InputStyles.wrapper,
                  InputStyles.withIcon,
                  InputStyles.biggerWithIcon,
                  ThemeHeaderStyles.proposalInputWithIconWrapper
                )
              )(
                <.span(^.className := TableLayoutStyles.wrapper)(
                  <.span(^.className := js.Array(TableLayoutStyles.cell, ThemeHeaderStyles.inputWrapper))(
                    <.input(
                      ^.`type`.text,
                      ^.value := I18n.t("common.bait"),
                      ^.readOnly := true,
                      ^.ref := ((input: HTMLElement) => proposalInput = Some(input)),
                      ^.onFocus := openProposalModalFromInput
                    )()
                  ),
                  <.span(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                    <.span(^.className := js.Array(TextStyles.smallText, ThemeHeaderStyles.textLimitInfo))(
                      I18n.t("theme.proposal-form-in-header.limit-of-chars-info")
                    )
                  )
                )
              ),
              <.FullscreenModalComponent(
                ^.wrapped := FullscreenModalProps(
                  isModalOpened = self.state.isProposalModalOpened,
                  closeCallback = closeProposalModal()
                )
              )(
                <.SubmitProposalInRelationToThemeComponent(
                  ^.wrapped := SubmitProposalInRelationToThemeProps(
                    theme = self.props.wrapped.theme,
                    onProposalProposed = () => {
                      self.setState(_.copy(isProposalModalOpened = false))
                    },
                    maybeLocation = None
                  )
                )()
              )
            )
          ),
          <.style()(ThemeHeaderStyles.render[String], DynamicThemeHeaderStyles.render[String])
        )
      }
    )
}

object ThemeHeaderStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative, height(300.pxToEm()), backgroundColor(ThemeStyles.BackgroundColor.black), overflow.hidden)

  val innerWrapper: StyleA =
    style(position.relative, padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`), textAlign.center)

  val illustration: StyleA =
    style(
      position.absolute,
      top(`0`),
      left(50.%%),
      height.auto,
      maxHeight.none,
      minHeight(100.%%),
      width.auto,
      maxWidth.none,
      minWidth(100.%%),
      transform := s"translate(-50%, 0%)",
      opacity(0.5),
      filter := s"grayscale(100%)",
      mixBlendMode := "multiply"
    )

  val title: StyleA =
    style(
      display.inlineBlock,
      marginBottom(15.pxToEm(30)),
      lineHeight(41.pxToEm(30)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(10.pxToEm(40)), lineHeight(56.pxToEm(40))),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(10.pxToEm(60)), lineHeight(83.pxToEm(60))),
      color(ThemeStyles.TextColor.white),
      textShadow := s"1px 1px 1px rgb(0, 0, 0)"
    )

  val proposalInputWithIconWrapper: StyleA =
    style(
      boxShadow := "0 2px 5px 0 rgba(0,0,0,0.50)",
      &.before(content := "'\\F0EB'"),
      unsafeChild("input")(ThemeStyles.Font.circularStdBold, cursor.text)
    )

  val inputWrapper: StyleA =
    style(width(100.%%))

  val textLimitInfo: StyleA =
    style(
      display.inlineBlock,
      lineHeight(28.pxToEm(13)),
      paddingLeft(ThemeStyles.SpacingValue.small.pxToEm(13)),
      ThemeStyles.MediaQueries
        .beyondSmall(lineHeight(38.pxToEm(16)), paddingLeft(ThemeStyles.SpacingValue.small.pxToEm(16))),
      ThemeStyles.MediaQueries.beyondMedium(lineHeight(48.pxToEm(16))),
      color(ThemeStyles.TextColor.lighter),
      whiteSpace.nowrap
    )
}
