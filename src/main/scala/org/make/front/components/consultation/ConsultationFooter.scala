package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.base.TableLayoutStyles

object ConsultationFooter {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "ConsultationFooter",
        render = _ => {
          <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
            <.span()(
              <.a(^.href := I18n.t(s"main-footer.menu.item-2.link"), ^.target := "_blank")(
                unescape(I18n.t(s"main-footer.menu.item-2.label"))
              ),
              " · ",
              <.a(^.href := I18n.t(s"main-footer.menu.item-4.link"), ^.target := "_blank")(
                unescape(I18n.t(s"main-footer.menu.item-4.label"))
              ),
              " · ",
              <.a(^.href := I18n.t(s"main-footer.menu.item-5.link"), ^.target := "_blank")(
                unescape(I18n.t(s"main-footer.menu.item-5.label"))
              ),
              " · ",
              <.a(^.href := I18n.t(s"main-footer.menu.item-6.link"), ^.target := "_blank")(
                unescape(I18n.t(s"main-footer.menu.item-6.label"))
              ),
              " · ",
              <.a(^.href := I18n.t(s"main-footer.menu.item-7.link"), ^.target := "_blank")(
                unescape(I18n.t(s"main-footer.menu.item-7.label"))
              )
            )
          )
        }
      )
}
