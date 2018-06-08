package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import scala.scalajs.js

object ConsultationPresentation {

  case class ConsultationPresentationProps(content: String, learnMoreUrl: Option[String] = None)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationPresentationProps, Unit](
        displayName = "ConsultationPresentation",
        render = { self =>
          def onclick(url: String): () => Unit = { () =>
            scalajs.js.Dynamic.global.window.open(url, "_blank")
          }
          <.article(^.className := ConsultationPresentationStyles.wrapper)(
            <.div(^.className := ConsultationPresentationStyles.contentWrapper)(
              <.h3(^.className := js.Array(TextStyles.mediumText, TextStyles.boldText))(
                unescape(I18n.t("operation.presentation.title"))
              ),
              <.p()(self.props.wrapped.content),
              self.props.wrapped.learnMoreUrl.map { url =>
                <.a(^.onClick := onclick(url), ^.className := TextStyles.boldText)(
                  unescape(I18n.t("operation.presentation.seeMore"))
                )
              }
            ),
            <.style()(ConsultationPresentationStyles.render[String])
          )
        }
      )

}

object ConsultationPresentationStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      position.relative,
      height(100.%%),
      minHeight(360.pxToEm()),
      ThemeStyles.MediaQueries.belowMedium(minHeight.inherit),
      minWidth(240.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val contentWrapper: StyleA =
    style(
      padding(
        ThemeStyles.SpacingValue.small.pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm(),
        ThemeStyles.SpacingValue.medium.pxToEm()
      ),
      overflow.hidden
    )

}
