package org.make.front.components.currentOperations

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades._
import org.make.front.models.{
  CountryConfiguration => CountryConfigurationModel,
  GradientColor        => GradientColorModel,
  OperationExpanded    => OperationExpandedModel,
  OperationWording     => OperationWordingModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._

object CurrentOperations {

  final case class CurrentOperationsProps(country: String,
                                          language: String,
                                          operations: Seq[OperationExpandedModel],
                                          supportedCountries: Seq[CountryConfigurationModel])

  final case class CurrentOperationsState()

  lazy val reactClass: ReactClass =
    React
      .createClass[CurrentOperationsProps, CurrentOperationsState](
        displayName = "CurrentOperations",
        render = (self) => {

          def operationTile(operation: OperationExpandedModel, isLongerBeyondMedium: Boolean = false): ReactElement = {
            val wording: OperationWordingModel =
              operation.getWordingByLanguageOrError(self.props.wrapped.language)

            val gradientValues: GradientColorModel =
              operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

            object DynamicOperationTileStyles extends StyleSheet.Inline {

              import dsl._

              val gradient: StyleA =
                style(background := s"linear-gradient(130deg, ${gradientValues.from}, ${gradientValues.to})")

              val ill: StyleA = style(maxWidth(operation.logoMaxWidth.getOrElse(470).pxToEm()))
            }

            <.article(^.className := OperationTileStyles.wrapper)(
              <.Link(^.to := s"/${self.props.wrapped.country}/consultation/${operation.slug}/selection")(
                <.div(
                  ^.className := Seq(
                    OperationTileStyles.illWrapper(isLongerBeyondMedium),
                    DynamicOperationTileStyles.gradient
                  )
                )(
                  <.img(
                    ^.className := Seq(OperationTileStyles.ill, DynamicOperationTileStyles.ill),
                    ^.src := operation.logoUrl.getOrElse(""),
                    ^.alt := unescape(wording.title)
                  )()
                )
              ),
              <.div(^.className := Seq(OperationTileStyles.contentWrapper, TableLayoutStyles.wrapper))(
                <.div(^.className := Seq(OperationTileStyles.titleWrapper, TableLayoutStyles.cellVerticalAlignMiddle))(
                  <.p(^.className := TextStyles.verySmallTitle)(unescape(wording.question))
                ),
                <.div(^.className := Seq(OperationTileStyles.CTAWrapper, TableLayoutStyles.cellVerticalAlignMiddle))(
                  <.Link(
                    ^.to := s"/${self.props.wrapped.country}/consultation/${operation.slug}/selection",
                    ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton)
                  )(unescape(I18n.t("current-operations.see-operation-cta")))
                )
              ),
              <.style()(DynamicOperationTileStyles.render[String])
            )
          }

          def countryTile(country: CountryConfigurationModel) =
            <.Link(^.to := s"/${country.countryCode}/")(
              <.span(^.className := Seq(CountryTileStyles.wrapper, TableLayoutStyles.wrapper))(
                <.span(^.className := Seq(CountryTileStyles.innerWrapper, TableLayoutStyles.cellVerticalAlignMiddle))(
                  <.img(
                    ^.className := CountryTileStyles.flag,
                    ^.src := country.flagUrl,
                    ^.alt := country.defaultLanguage
                  )(),
                  <.span(^.className := Seq(CountryTileStyles.label, TextStyles.mediumText))(
                    I18n.t(s"current-operations.${country.countryCode}")
                  )
                )
              )
            )

          <.div(^.className := Seq(CurrentOperationsStyles.wrapper, TableLayoutStyles.fullHeightWrapper))(
            <.div(^.className := TableLayoutStyles.row)(
              <.div(^.className := Seq(TableLayoutStyles.cell, CurrentOperationsStyles.mainHeaderWrapper))(
                <.MainHeaderContainer.empty
              )
            ),
            <.div(^.className := Seq(TableLayoutStyles.row, CurrentOperationsStyles.fullHeight))(
              <.div(^.className := TableLayoutStyles.cell)(
                <.section(^.className := CurrentOperationsStyles.operations)(
                  <.header(^.className := LayoutRulesStyles.centeredRow)(
                    <.h2(^.className := TextStyles.mediumTitle)(
                      unescape(
                        I18n.t(
                          "current-operations.operations-intro",
                          Replacements(("count", self.props.wrapped.operations.size.toString))
                        )
                      )
                    )
                  ),
                  if (self.props.wrapped.operations.lengthCompare(1) > 0) {
                    <.ul(
                      ^.className := Seq(CurrentOperationsStyles.operationsList, LayoutRulesStyles.centeredRowWithCols)
                    )(self.props.wrapped.operations.map { operation =>
                      <.li(
                        ^.className := Seq(
                          CurrentOperationsStyles.operationItem,
                          ColRulesStyles.col,
                          ColRulesStyles.colHalfBeyondMedium
                        )
                      )(operationTile(operation))
                    })
                  } else {
                    <.div(^.className := Seq(CurrentOperationsStyles.operationItem, LayoutRulesStyles.centeredRow))(
                      operationTile(self.props.wrapped.operations.head, isLongerBeyondMedium = true)
                    )
                  }
                ),
                <.section(^.className := CurrentOperationsStyles.countries)(
                  <.header(^.className := LayoutRulesStyles.centeredRow)(
                    <.h2(^.className := TextStyles.mediumTitle)(unescape(I18n.t("current-operations.countries-intro")))
                  ),
                  <.ul(
                    ^.className := Seq(CurrentOperationsStyles.countriesList, LayoutRulesStyles.centeredRowWithCols)
                  )(self.props.wrapped.supportedCountries.map { country =>
                    <.li(
                      ^.className := Seq(
                        CurrentOperationsStyles.countryItem,
                        ColRulesStyles.col,
                        ColRulesStyles.colHalfBeyondSmall,
                        ColRulesStyles.colThirdBeyondLarge
                      )
                    )(countryTile(country))
                  })
                )
              )
            ),
            <.style()(
              CurrentOperationsStyles.render[String],
              OperationTileStyles.render[String],
              CountryTileStyles.render[String]
            )
          )
        }
      )
}

object CurrentOperationsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      tableLayout.fixed,
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm())),
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent)
    )

  val fullHeight: StyleA =
    style(height(100.%%))

  val mainHeaderWrapper: StyleA =
    style(visibility.hidden)

  val operations: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(
        padding(
          ThemeStyles.SpacingValue.larger.pxToEm(),
          `0`,
          (ThemeStyles.SpacingValue.larger - ThemeStyles.SpacingValue.small).pxToEm()
        )
      ),
      textAlign.center
    )

  val operationsList: StyleA =
    style(textAlign.left)

  val operationItem: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val countries: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(
        padding(
          ThemeStyles.SpacingValue.larger.pxToEm(),
          `0`,
          (ThemeStyles.SpacingValue.larger - ThemeStyles.SpacingValue.small).pxToEm()
        )
      ),
      textAlign.center
    )

  val countriesList: StyleA =
    style()

  val countryItem: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))
    )
}

object CountryTileStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      height(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
      textAlign.left,
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val innerWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(
        paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
        paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val flag: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.middle,
      width(26.px),
      marginRight(15.px),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val label: StyleA =
    style(display.inlineBlock, verticalAlign.middle)
}

object OperationTileStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(width(100.%%), boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)")

  val illWrapper: (Boolean) => StyleA = styleF.bool(
    isLongerBeyondMedium =>
      if (isLongerBeyondMedium) {
        styleS(
          position.relative,
          padding(((50 * 100) / 180).%%, 50.%%),
          ThemeStyles.MediaQueries.beyondMedium(padding((((50 * 100) / 180) / 2).%%, 50.%%)),
          backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
          overflow.hidden
        )
      } else {
        styleS(
          position.relative,
          padding(((50 * 100) / 180).%%, 50.%%),
          backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
          overflow.hidden
        )
    }
  )

  val ill: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      maxHeight(70.%%),
      width(80.%%),
      transform := s"translate(-50%, -50%)"
    )

  val contentWrapper: StyleA =
    style(height(ThemeStyles.SpacingValue.evenLarger.pxToEm()), backgroundColor(ThemeStyles.BackgroundColor.white))

  val titleWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())),
      textAlign.left
    )

  val CTAWrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingRight(ThemeStyles.SpacingValue.medium.pxToEm())),
      textAlign.right
    )

}
