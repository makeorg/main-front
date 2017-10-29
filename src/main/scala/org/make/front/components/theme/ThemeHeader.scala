package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.theme.SubmitProposalInRelationToTheme.SubmitProposalInRelationToThemeProps
import org.make.front.facades._
import org.make.front.models.{GradientColor => GradientColorModel, Theme => ThemeModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.InputStyles
import org.make.front.styles.utils._
import org.scalajs.dom.raw.HTMLElement

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.mutable.StyleSheet.Inline

object ThemeHeader {

  case class ThemeHeaderProps(theme: ThemeModel)

  case class ThemeHeaderState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[ThemeHeaderProps, ThemeHeaderState](displayName = "ThemeHeader", getInitialState = { _ =>
      ThemeHeaderState(isProposalModalOpened = false)
    }, render = {
      self =>
        var proposalInput: Option[HTMLElement] = None

        def toggleProposalModal() = () => {
          self.setState(state => state.copy(isProposalModalOpened = !self.state.isProposalModalOpened))
        }

        def openProposalModalFromInput() = () => {
          self.setState(state => state.copy(isProposalModalOpened = true))
          proposalInput.foreach(_.blur())
        }

        val gradientValues: GradientColorModel =
          self.props.wrapped.theme.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

        object DynamicThemeHeaderStyles extends Inline {
          import dsl._

          val gradient = style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
        }

        var imageSrc: String = ""
        var imageSrcset: String = ""

        val SmallIllUrl: String = self.props.wrapped.theme.slug match {
          case "developpement-durable-energie" => themeAgricultureRuraliteSmall.toString
          case "agriculture-ruralite"          => themeAgricultureRuraliteSmall.toString
          case "democratie-vie-politique"      => themeDemocratieViePolitiqueSmall.toString
          case "economie-emploi-travail"       => themeEconomieEmploiTravailSmall.toString
          case "education"                     => themeEducationSmall.toString
          case "europe-monde"                  => themeEuropeMondeSmall.toString
          case "logement"                      => themeLogementSmall.toString
          case "numerique-culture"             => themeNumeriqueCultureSmall.toString
          case "sante-alimentation"            => themeSanteAlimentationSmall.toString
          case "securite-justice"              => themeSecuriteJusticeSmall.toString
          case "transports-deplacement"        => themeTransportsDeplacementSmall.toString
          case "vivre-ensemble-solidarites"    => themeVivreEnsembleSolidariteSmall.toString
          case _                               => homeSmall.toString
        }

        val SmallIllUrl2x: String = self.props.wrapped.theme.slug match {
          case "developpement-durable-energie" => themeAgricultureRuraliteSmall2x.toString
          case "agriculture-ruralite"          => themeAgricultureRuraliteSmall2x.toString
          case "democratie-vie-politique"      => themeDemocratieViePolitiqueSmall2x.toString
          case "economie-emploi-travail"       => themeEconomieEmploiTravailSmall2x.toString
          case "education"                     => themeEducationSmall2x.toString
          case "europe-monde"                  => themeEuropeMondeSmall2x.toString
          case "logement"                      => themeLogementSmall2x.toString
          case "numerique-culture"             => themeNumeriqueCultureSmall2x.toString
          case "sante-alimentation"            => themeSanteAlimentationSmall2x.toString
          case "securite-justice"              => themeSecuriteJusticeSmall2x.toString
          case "transports-deplacement"        => themeTransportsDeplacementSmall2x.toString
          case "vivre-ensemble-solidarites"    => themeVivreEnsembleSolidariteSmall2x.toString
          case _                               => homeSmall2x.toString
        }

        val MediumIllUrl: String = self.props.wrapped.theme.slug match {
          case "developpement-durable-energie" => themeAgricultureRuraliteMedium.toString
          case "agriculture-ruralite"          => themeAgricultureRuraliteMedium.toString
          case "democratie-vie-politique"      => themeDemocratieViePolitiqueMedium.toString
          case "economie-emploi-travail"       => themeEconomieEmploiTravailMedium.toString
          case "education"                     => themeEducationMedium.toString
          case "europe-monde"                  => themeEuropeMondeMedium.toString
          case "logement"                      => themeLogementMedium.toString
          case "numerique-culture"             => themeNumeriqueCultureMedium.toString
          case "sante-alimentation"            => themeSanteAlimentationMedium.toString
          case "securite-justice"              => themeSecuriteJusticeMedium.toString
          case "transports-deplacement"        => themeTransportsDeplacementMedium.toString
          case "vivre-ensemble-solidarites"    => themeVivreEnsembleSolidariteMedium.toString
          case _                               => homeMedium.toString
        }

        val MediumIllUrl2x: String = self.props.wrapped.theme.slug match {
          case "developpement-durable-energie" => themeAgricultureRuraliteMedium2x.toString
          case "agriculture-ruralite"          => themeAgricultureRuraliteMedium2x.toString
          case "democratie-vie-politique"      => themeDemocratieViePolitiqueMedium2x.toString
          case "economie-emploi-travail"       => themeEconomieEmploiTravailMedium2x.toString
          case "education"                     => themeEducationMedium2x.toString
          case "europe-monde"                  => themeEuropeMondeMedium2x.toString
          case "logement"                      => themeLogementMedium2x.toString
          case "numerique-culture"             => themeNumeriqueCultureMedium2x.toString
          case "sante-alimentation"            => themeSanteAlimentationMedium2x.toString
          case "securite-justice"              => themeSecuriteJusticeMedium2x.toString
          case "transports-deplacement"        => themeTransportsDeplacementMedium2x.toString
          case "vivre-ensemble-solidarites"    => themeVivreEnsembleSolidariteMedium2x.toString
          case _                               => homeMedium2x.toString
        }

        val IllUrl: String = self.props.wrapped.theme.slug match {
          case "developpement-durable-energie" => themeAgricultureRuralite.toString
          case "agriculture-ruralite"          => themeAgricultureRuralite.toString
          case "democratie-vie-politique"      => themeDemocratieViePolitique.toString
          case "economie-emploi-travail"       => themeEconomieEmploiTravail.toString
          case "education"                     => themeEducation.toString
          case "europe-monde"                  => themeEuropeMonde.toString
          case "logement"                      => themeLogement.toString
          case "numerique-culture"             => themeNumeriqueCulture.toString
          case "sante-alimentation"            => themeSanteAlimentation.toString
          case "securite-justice"              => themeSecuriteJustice.toString
          case "transports-deplacement"        => themeTransportsDeplacement.toString
          case "vivre-ensemble-solidarites"    => themeVivreEnsembleSolidarite.toString
          case _                               => home.toString
        }

        val IllUrl2x: String = self.props.wrapped.theme.slug match {
          case "developpement-durable-energie" => themeAgricultureRuralite2x.toString
          case "agriculture-ruralite"          => themeAgricultureRuralite2x.toString
          case "democratie-vie-politique"      => themeDemocratieViePolitique2x.toString
          case "economie-emploi-travail"       => themeEconomieEmploiTravail2x.toString
          case "education"                     => themeEducation2x.toString
          case "europe-monde"                  => themeEuropeMonde2x.toString
          case "logement"                      => themeLogement2x.toString
          case "numerique-culture"             => themeNumeriqueCulture2x.toString
          case "sante-alimentation"            => themeSanteAlimentation2x.toString
          case "securite-justice"              => themeSecuriteJustice2x.toString
          case "transports-deplacement"        => themeTransportsDeplacement2x.toString
          case "vivre-ensemble-solidarites"    => themeVivreEnsembleSolidarite2x.toString
          case _                               => home2x.toString
        }

        imageSrc = IllUrl
        imageSrcset = SmallIllUrl + " 400w, " + SmallIllUrl2x + " 800w, " + MediumIllUrl + " 840w, " + MediumIllUrl2x + " 1680w, " + IllUrl + " 1350w, " + IllUrl2x + " 2700w"

        <.header(^.className := Seq(ThemeHeaderStyles.wrapper, DynamicThemeHeaderStyles.gradient))(
          <.div(^.className := ThemeHeaderStyles.innerWrapper)(
            <.img(
              ^.className := ThemeHeaderStyles.illustration,
              ^.src := imageSrc,
              ^("srcset") := imageSrcset,
              ^.alt := self.props.wrapped.theme.title,
              ^("data-pin-no-hover") := "true"
            )(),
            <.div(^.className := RowRulesStyles.centeredRow)(
              <.div(^.className := ColRulesStyles.col)(
                <.h1(^.className := Seq(TextStyles.veryBigTitle, ThemeHeaderStyles.title))(
                  self.props.wrapped.theme.title
                ),
                <.p(
                  ^.className := Seq(
                    InputStyles.wrapper,
                    InputStyles.withIcon,
                    InputStyles.biggerWithIcon,
                    ThemeHeaderStyles.proposalInputWithIconWrapper
                  )
                )(
                  <.span(^.className := ThemeHeaderStyles.inputInnerWrapper)(
                    <.span(^.className := ThemeHeaderStyles.inputSubInnerWrapper)(
                      <.input(
                        ^.`type`.text,
                        ^.value := I18n.t("theme.proposal-form-in-header.bait"),
                        ^.readOnly := true,
                        ^.ref := ((input: HTMLElement) => proposalInput = Some(input)),
                        ^.onFocus := openProposalModalFromInput
                      )()
                    ),
                    <.span(^.className := ThemeHeaderStyles.textLimitInfoWapper)(
                      <.span(^.className := Seq(TextStyles.smallText, ThemeHeaderStyles.textLimitInfo))(
                        I18n.t("theme.proposal-form-in-header.limit-of-chars-info")
                      )
                    )
                  )
                ),
                <.FullscreenModalComponent(
                  ^.wrapped := FullscreenModalProps(
                    isModalOpened = self.state.isProposalModalOpened,
                    closeCallback = toggleProposalModal()
                  )
                )(
                  <.SubmitProposalInRelationToThemeComponent(
                    ^.wrapped := SubmitProposalInRelationToThemeProps(
                      theme = self.props.wrapped.theme,
                      onProposalProposed = () => {
                        self.setState(_.copy(isProposalModalOpened = false))
                      }
                    )
                  )()
                )
              )
            )
          ),
          <.style()(ThemeHeaderStyles.render[String], DynamicThemeHeaderStyles.render[String])
        )
    })

}

object ThemeHeaderStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      position.relative,
      display.table,
      width(100.%%),
      height(300.pxToEm()),
      /*height :=! s"calc(100% - ${200.pxToEm().value})",*/
      backgroundColor(ThemeStyles.BackgroundColor.black), //TODO:gradient
      overflow.hidden
    )

  val innerWrapper: StyleA =
    style(
      position.relative,
      display.tableCell,
      verticalAlign.middle,
      paddingTop((ThemeStyles.SpacingValue.larger + 50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries.beyondSmall(paddingTop((ThemeStyles.SpacingValue.larger + 80).pxToEm())),
      paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()),
      textAlign.center
    )

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
      (&.before)(content := "'\\F0EB'"),
      unsafeChild("input")(ThemeStyles.Font.circularStdBold, cursor.text)
    )

  val inputInnerWrapper: StyleA = style(display.table, width(100.%%))

  val inputSubInnerWrapper: StyleA =
    style(display.tableCell, width(100.%%))

  val textLimitInfoWapper: StyleA = style(display.tableCell, verticalAlign.middle)

  val textLimitInfo: StyleA =
    style(
      display.inlineBlock,
      lineHeight :=! s"${28.pxToEm(13).value}",
      paddingLeft(ThemeStyles.SpacingValue.small.pxToEm(13)),
      ThemeStyles.MediaQueries
        .beyondSmall(lineHeight :=! s"${38.pxToEm(16).value}", paddingLeft(ThemeStyles.SpacingValue.small.pxToEm(16))),
      ThemeStyles.MediaQueries.beyondMedium(lineHeight :=! s"${48.pxToEm(16).value}"),
      color(ThemeStyles.TextColor.lighter),
      whiteSpace.nowrap
    )

}
