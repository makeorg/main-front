package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.models.{OperationExpanded => OperationModel}
import org.make.front.components.share.ShareProposal.ShareProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.utils._

import scala.scalajs.js

object ConsultationShare {

  case class ConsultationShareProps(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationShareProps, Unit](
      displayName = "ConsultationShare",
      render = { self =>
        <.article(^.className := js.Array(
          ConsultationShareStyles.wrapper,
          LayoutRulesStyles.centeredRow)
        )(
          <.h3(^.className := js.Array(
            TextStyles.smallerTitle,
            ConsultationShareStyles.title)
          )(
            unescape(I18n.t("operation.share.title"))
          ),
          <.ShareComponent(^.wrapped := ShareProps(operation = self.props.wrapped.operation))(),
          <.style()(ConsultationShareStyles.render[String])
        )
      }
    )

}

object ConsultationShareStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)",
      padding(
        20.pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm()
      ),
      marginTop(ThemeStyles.SpacingValue.small.pxToEm())
    )

  val title: StyleA =
    style(
      paddingBottom(ThemeStyles.SpacingValue.smaller.pxToEm())
    )
}