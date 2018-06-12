package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.OperationExpanded
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._

import scala.scalajs.js

object ConsultationCommunity {

  case class ConsultationCommunityProps(operation: OperationExpanded, language: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationCommunityProps, Unit](
        displayName = "ConsultationCommunity",
        render = self => {

          def onclick: () => Unit = { () =>
            scalajs.js.Dynamic.global.window
              .open(
                I18n.t(
                  "operation.community.learn-more.link",
                  Replacements(("operation-slug", self.props.wrapped.operation.slug))
                ),
                "_blank"
              )
          }

          def linkPartner =
            self.props.wrapped.operation.wordings
              .find(_.language == self.props.wrapped.language)
              .flatMap(_.learnMoreUrl)
              .map(_ + "#partenaires")
              .getOrElse("/#/404")

          <.article(^.className := ConsultationCommunityStyles.wrapper)(
            <.div(^.className := ConsultationCommunityStyles.contentWrapper)(
              <.h3(^.className := js.Array(TextStyles.mediumText, TextStyles.boldText))(
                unescape(I18n.t("operation.community.title"))
              ),
              <.p()(
//                todo count citizen who participate
//                unescape(I18n.t("operation.community.citizen-count", Replacements(("count", "52341")))),
//                unescape(I18n.t("operation.community.already-participate")),
                <.button(^.onClick := onclick, ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton))(
                  unescape(I18n.t("operation.community.learn-more.label"))
                )
              ),
              <.h3(^.className := js.Array(TextStyles.mediumText, TextStyles.boldText))(
                unescape(I18n.t("operation.community.partner.title"))
              ),
              <(self.props.wrapped.operation.partnersComponent).empty,
              <.a(^.href := linkPartner, ^.className := TextStyles.boldText, ^.target := "_blank")(
                unescape(I18n.t("operation.community.partner.see-more"))
              )
            ),
            <.style()(ConsultationCommunityStyles.render[String])
          )

        }
      )

}

object ConsultationCommunityStyles extends StyleSheet.Inline {

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
