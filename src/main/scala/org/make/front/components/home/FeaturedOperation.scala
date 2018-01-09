package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades._
import org.make.front.styles._
import org.make.front.styles.base.{LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.models.{Operation => OperationModel}

object FeaturedOperation {

  final case class FeaturedOperationProps(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[FeaturedOperationProps, Unit](
        displayName = "FeaturedOperation",
        render = (self) => {

          val operation: OperationModel =
            self.props.wrapped.operation

          def onclick: () => Unit = { () =>
            FacebookPixel.fbq("trackCustom", "click-homepage-header")
            scalajs.js.Dynamic.global.window.open(operation.wording.learnMoreUrl.get, "_blank")
          }
          <.section(^.className := Seq(TableLayoutStyles.wrapper, FeaturedOperationStyles.wrapper))(
            <.div(^.className := Seq(TableLayoutStyles.cellVerticalAlignMiddle, FeaturedOperationStyles.innerWrapper))(
              if (operation.wording.featuredIll.isDefined) {
                <.img(
                  ^.className := FeaturedOperationStyles.illustration,
                  ^.src := operation.wording.featuredIll.map(_.illUrl).getOrElse(""),
                  ^("srcset") := operation.wording.featuredIll
                    .map(_.smallIllUrl)
                    .getOrElse("") + " 400w, " + operation.wording.featuredIll
                    .map(_.smallIll2xUrl)
                    .getOrElse("") + " 800w, " + operation.wording.featuredIll
                    .map(_.mediumIllUrl)
                    .getOrElse("") + " 840w, " + operation.wording.featuredIll
                    .map(_.mediumIll2xUrl)
                    .getOrElse("") + " 1680w, " + operation.wording.featuredIll
                    .map(_.illUrl)
                    .getOrElse("") + " 1350w, " + operation.wording.featuredIll
                    .map(_.ill2xUrl)
                    .getOrElse("") + " 2700w",
                  ^.alt := operation.wording.title,
                  ^("data-pin-no-hover") := "true"
                )()
              },
              <.div(^.className := Seq(FeaturedOperationStyles.innerSubWrapper, LayoutRulesStyles.centeredRow))(
                if (operation.wording.greatCauseLabel.isDefined) {
                  <.div(^.className := FeaturedOperationStyles.labelWrapper)(
                    <.p(^.className := TextStyles.label)(unescape(operation.wording.greatCauseLabel.getOrElse("")))
                  )
                },
                <.h2(^.className := Seq(FeaturedOperationStyles.title, TextStyles.veryBigText, TextStyles.boldText))(
                  unescape(operation.wording.title)
                ),
                if (operation.wording.purpose.isDefined) {
                  <.h3(^.className := Seq(TextStyles.mediumText, FeaturedOperationStyles.subTitle))(
                    unescape(operation.wording.purpose.getOrElse(""))
                  )
                },
                if (operation.wording.learnMoreUrl.isDefined) {
                  <.p(^.className := FeaturedOperationStyles.ctaWrapper)(
                    <.button(^.onClick := onclick, ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton))(
                      unescape(I18n.t("home.featured-operation.learn-more"))
                    )
                  )
                },
                <.style()(FeaturedOperationStyles.render[String])
              )
            )
          )
        }
      )
}

object FeaturedOperationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative, height(440.pxToEm()), backgroundColor(ThemeStyles.BackgroundColor.black))

  val innerWrapper: StyleA =
    style(position.relative, padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`), textAlign.center, overflow.hidden)

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
      opacity(0.7)
    )

  val innerSubWrapper: StyleA =
    style(position.relative, zIndex(1))

  val labelWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val title: StyleA =
    style(color(ThemeStyles.TextColor.white), textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)")

  val subTitle: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      color(ThemeStyles.TextColor.white),
      textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)"
    )

  val ctaWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

}
