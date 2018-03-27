package org.make.front.components.operation.intro

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades._
import org.make.front.models.{GradientColor => GradientColorModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.LayoutRulesStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object MakeEuropeOperationIntro {

  case class MakeEuropeOperationIntroProps(operation: OperationModel, language: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[MakeEuropeOperationIntroProps, Unit](
        displayName = "VFFOperationIntro",
        render = (self) => {

          def onClick: () => Unit = { () =>
            TrackingService.track(
              "click-button-learn-more",
              TrackingContext(TrackingLocation.operationPage, Some(self.props.wrapped.operation.slug))
            )
          }

          val operation: OperationModel =
            self.props.wrapped.operation

          val gradientValues: GradientColorModel =
            operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

          final case class CountryModel(flagUrl: String, code: String)

          val countries = Seq(
            CountryModel(flagUrl = frFlag.toString, code = "France"),
            CountryModel(flagUrl = gbFlag.toString, code = "Great Britain"),
            CountryModel(flagUrl = itFlag.toString, code = "Italia")
          )

          object DynamicMakeEuropeOperationIntroStyles extends StyleSheet.Inline {
            import dsl._
            val gradient: StyleA =
              style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          <.div(
            ^.className := Seq(
              OperationIntroStyles.wrapper,
              MakeEuropeOperationIntroStyles.wrapper,
              DynamicMakeEuropeOperationIntroStyles.gradient
            )
          )(
            <.img(
              ^.className := MakeEuropeOperationIntroStyles.illustration,
              ^.src := makeEuropeIll.toString,
              ^("srcset") := makeEuropeIllSmall.toString + " 400w, " + makeEuropeIllSmall2x.toString + " 800w, " + makeEuropeIllMedium.toString + " 840w, " + makeEuropeIllMedium2x.toString + " 1680w, " + makeEuropeIll.toString + " 1350w, " + makeEuropeIll2x.toString + " 2700w",
              ^.alt := I18n.t("operation.make-europe.intro.title"),
              ^("data-pin-no-hover") := "true"
            )(),
            <.div(^.className := Seq(OperationIntroStyles.headingWrapper, LayoutRulesStyles.centeredRow))(
              <.p(^.className := Seq(MakeEuropeOperationIntroStyles.logoWrapper))(
                <.img(
                  ^.src := makeEuropeWhiteLogo.toString,
                  ^.alt := unescape(I18n.t("operation.make-europe.intro.title"))
                )()
              ),
              <.ul(^.className := MakeEuropeOperationIntroStyles.countriesList)(
                countries.map(
                  country =>
                    <.li(^.className := MakeEuropeOperationIntroStyles.countryItem)(
                      <.img(
                        ^.src := country.flagUrl,
                        ^.alt := country.code,
                        ^.className := MakeEuropeOperationIntroStyles.countryLogo
                      )()
                  )
                )
              )
            ),
            <.style()(
              OperationIntroStyles.render[String],
              MakeEuropeOperationIntroStyles.render[String],
              DynamicMakeEuropeOperationIntroStyles.render[String]
            )
          )
        }
      )
}

object MakeEuropeOperationIntroStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA = style(position.relative, overflow.hidden)

  val illustration: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      height.auto,
      maxHeight.none,
      minHeight(100.%%),
      width.auto,
      maxWidth.none,
      minWidth(100.%%),
      transform := s"translate(-50%, -50%)",
      opacity(0.7),
      mixBlendMode := "multiply"
    )

  val logoWrapper: StyleA = style(maxWidth(358.pxToEm()), marginLeft.auto, marginRight.auto)

  val countriesList: StyleA = style(
    textAlign.center,
    margin(
      (ThemeStyles.SpacingValue.medium - ThemeStyles.SpacingValue.smaller).pxToEm(),
      -(ThemeStyles.SpacingValue.smaller / 2).pxToEm(),
      `0`
    )
  )

  val countryItem: StyleA = style(
    display.inlineBlock,
    verticalAlign.middle,
    padding(ThemeStyles.SpacingValue.smaller.pxToEm(), (ThemeStyles.SpacingValue.smaller / 2).pxToEm(), `0`)
  )

  val countryLogo: StyleA = style(width(32.pxToEm()))

}
