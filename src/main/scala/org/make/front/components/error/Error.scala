package org.make.front.components.error

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, RWDHideRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

object Error {

  case class ErrorProps(redirectToRandomTheme: () => Unit)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[ErrorProps, Unit](
        displayName = "Error",
        render = { self =>
          <("error")()(
            <.div(^.className := Seq(TableLayoutStyles.fullHeightWrapper))(
              <.div(^.className := TableLayoutStyles.row)(
                <.div(^.className := Seq(TableLayoutStyles.cell, ErrorStyles.mainHeaderWrapper))(
                  <.MainHeaderContainer.empty
                )
              ),
              <.div(^.className := Seq(TableLayoutStyles.row, ErrorStyles.fullHeight))(
                <.div(^.className := Seq(TableLayoutStyles.cell, ErrorStyles.articleCell))(
                  <.div(^.className := Seq(LayoutRulesStyles.centeredRow, ErrorStyles.fullHeight))(
                    <.article(^.className := ErrorStyles.article)(
                      <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
                        <.div(
                          ^.className := Seq(TableLayoutStyles.cellVerticalAlignMiddle, ErrorStyles.articleInnerWrapper)
                        )(
                          <.div(^.className := LayoutRulesStyles.row)(
                            <.p(^.className := Seq(ErrorStyles.intro, TextStyles.mediumIntro))(
                              unescape(I18n.t("error.intro"))
                            ),
                            <.div(^.className := ErrorStyles.titleWrapper)(
                              <.h1(^.className := TextStyles.veryBigTitle)(unescape(I18n.t("error.title")))
                            ),
                            <.div(^.className := ErrorStyles.textWrapper)(
                              <.p(^.className := Seq(ErrorStyles.text, TextStyles.mediumText))(
                                unescape(I18n.t("error.recherche-intro"))
                              )
                            ),
                            <.div(^.className := ErrorStyles.searchFormWrapper)(<.SearchFormContainer.empty),
                            <.div(
                              ^.className := Seq(
                                RWDHideRulesStyles.showBlockBeyondMedium,
                                LayoutRulesStyles.evenNarrowerCenteredRow
                              )
                            )(<.hr(^.className := ErrorStyles.separatorLine)()),
                            <.div(^.className := ErrorStyles.textWrapper)(
                              <.p(^.className := Seq(ErrorStyles.text, TextStyles.mediumText))(
                                unescape(I18n.t("error.redirection-intro"))
                              )
                            ),
                            <.ul(^.className := ErrorStyles.ctasWrapper)(
                              <.li(^.className := ErrorStyles.ctaWrapper)(
                                <.Link(^.className := Seq(CTAStyles.basic, CTAStyles.basicOnA), ^.to := s"/")(
                                  <.i(^.className := FontAwesomeStyles.home)(),
                                  unescape("&nbsp;" + I18n.t("error.redirect-to-home"))
                                )
                              ),
                              <.li(^.className := ErrorStyles.ctaWrapper)(
                                <.button(
                                  ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton),
                                  ^.onClick := self.props.wrapped.redirectToRandomTheme
                                )(
                                  <.i(^.className := FontAwesomeStyles.random)(),
                                  unescape("&nbsp;" + I18n.t("error.redirect-to-random-theme"))
                                )
                              )
                            )
                          )
                        )
                      )
                    )
                  )
                )
              )
            ),
            <.NavInThemesContainerComponent.empty,
            <.style()(ErrorStyles.render[String])
          )
        }
      )
    )
}

object ErrorStyles extends StyleSheet.Inline {

  import dsl._

  val fullHeight: StyleA =
    style(height(100.%%))

  val mainHeaderWrapper: StyleA =
    style(visibility.hidden)

  val articleCell: StyleA =
    style(verticalAlign.middle, padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`))

  val article: StyleA =
    style(height(100.%%), backgroundColor(ThemeStyles.BackgroundColor.lightBlueGrey), textAlign.center)

  val articleInnerWrapper: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(
        paddingTop(ThemeStyles.SpacingValue.large.pxToEm()),
        paddingBottom(ThemeStyles.SpacingValue.large.pxToEm())
      )
    )

  val intro: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val titleWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(ThemeStyles.SpacingValue.large.pxToEm()))
    )

  val textWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(
        marginTop(ThemeStyles.SpacingValue.medium.pxToEm()),
        marginBottom(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val text: StyleA =
    style(color(ThemeStyles.TextColor.lighter))

  val searchFormWrapper: StyleA =
    style(maxWidth(940.pxToEm()), margin(ThemeStyles.SpacingValue.medium.pxToEm(), auto))

  val ctasWrapper: StyleA = style(margin((-1 * ThemeStyles.SpacingValue.smaller).pxToEm()))

  val ctaWrapper: StyleA =
    style(display.inlineBlock, verticalAlign.middle, padding(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val separatorLine: StyleA =
    style(
      height(1.px),
      width(100.%%),
      margin(ThemeStyles.SpacingValue.largerMedium.pxToEm(), `0`),
      border.none,
      backgroundColor(ThemeStyles.BorderColor.veryLight)
    )
}
