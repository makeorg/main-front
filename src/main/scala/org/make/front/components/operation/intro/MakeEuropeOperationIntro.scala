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

import scala.scalajs.js

object MakeEuropeOperationIntro {

  case class MakeEuropeOperationIntroProps(operation: OperationModel)

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

          val countries = js.Array(
            CountryModel(flagUrl = atFlag.toString, code = "Austria"),
            CountryModel(flagUrl = beFlag.toString, code = "Belgium"),
            CountryModel(flagUrl = bgFlag.toString, code = "Bulgaria"),
            CountryModel(flagUrl = cyFlag.toString, code = "Cyprus"),
            CountryModel(flagUrl = hrFlag.toString, code = "Croatia"),
            CountryModel(flagUrl = czFlag.toString, code = "Czech Republic"),
            CountryModel(flagUrl = dkFlag.toString, code = "Denmark"),
            CountryModel(flagUrl = eeFlag.toString, code = "Estonia"),
            CountryModel(flagUrl = fiFlag.toString, code = "Finland"),
            CountryModel(flagUrl = frFlag.toString, code = "France"),
            CountryModel(flagUrl = deFlag.toString, code = "Germany"),
            CountryModel(flagUrl = grFlag.toString, code = "Greece"),
            CountryModel(flagUrl = ieFlag.toString, code = "Ireland"),
            CountryModel(flagUrl = itFlag.toString, code = "Italy"),
            CountryModel(flagUrl = lvFlag.toString, code = "Latvia"),
            CountryModel(flagUrl = ltFlag.toString, code = "Lithuania"),
            CountryModel(flagUrl = luFlag.toString, code = "Luxembourg"),
            CountryModel(flagUrl = mtFlag.toString, code = "Malta"),
            CountryModel(flagUrl = nlFlag.toString, code = "Netherlands"),
            CountryModel(flagUrl = plFlag.toString, code = "Poland"),
            CountryModel(flagUrl = ptFlag.toString, code = "Portugal"),
            CountryModel(flagUrl = roFlag.toString, code = "Romania"),
            CountryModel(flagUrl = skFlag.toString, code = "Slovakia"),
            CountryModel(flagUrl = siFlag.toString, code = "Slovenia"),
            CountryModel(flagUrl = esFlag.toString, code = "Spain"),
            CountryModel(flagUrl = seFlag.toString, code = "Sweden")
          )

          object DynamicMakeEuropeOperationIntroStyles extends StyleSheet.Inline {
            import dsl._
            val gradient: StyleA =
              style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")
          }

          <.div(
            ^.className := js.Array(
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
            <.div(^.className := js.Array(OperationIntroStyles.headingWrapper, LayoutRulesStyles.centeredRow))(
              <.p(^.className := js.Array(MakeEuropeOperationIntroStyles.logoWrapper))(
                <.img(
                  ^.src := makeEuropeWhiteLogo.toString,
                  ^.alt := unescape(I18n.t("operation.make-europe.intro.title"))
                )()
              ),
              <.ul(^.className := MakeEuropeOperationIntroStyles.countriesList)(
                countries
                  .map(
                    country =>
                      <.li(^.className := MakeEuropeOperationIntroStyles.countryItem)(
                        <.img(
                          ^.src := country.flagUrl,
                          ^.alt := country.code,
                          ^.className := MakeEuropeOperationIntroStyles.countryLogo
                        )()
                    )
                  )
                  .toSeq
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
