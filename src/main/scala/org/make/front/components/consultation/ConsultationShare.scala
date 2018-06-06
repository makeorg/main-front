package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.models.{OperationExpanded => OperationModel}
import org.make.front.components.share.ShareProposal.ShareProps
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._

object ConsultationShare {

  case class ConsultationShareProps(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationShareProps, Unit](
      displayName = "ConsultationShare",
      render = { self =>
        <.article(^.className := ConsultationShareStyles.wrapper)(
          <.div(^.className := ConsultationShareStyles.contentWrapper)(
            <.ShareComponent(^.wrapped := ShareProps(operation = self.props.wrapped.operation))()
          ),
          <.style()(ConsultationShareStyles.render[String])
        )
      }
    )

}

object ConsultationShareStyles extends StyleSheet.Inline {
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