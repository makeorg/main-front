package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.consultation.ConsultationCommunity.ConsultationCommunityProps
import org.make.front.models.{OperationExpanded => OperationModel}
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{RWDHideRulesStyles, RWDRulesLargeMediumStyles, TextStyles}
import org.make.front.styles.ui.AccordionStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object ConsultationPresentation {

  case class ConsultationPresentationState(isCollapsed: Boolean)
  case class ConsultationPresentationProps(operation: OperationModel,
                                           language: String,
                                           content: String,
                                           learnMoreUrl: Option[String] = None,
                                           isCollapsed: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationPresentationProps, ConsultationPresentationState](
        displayName = "ConsultationPresentation",
        getInitialState = { self =>
          ConsultationPresentationState(self.props.wrapped.isCollapsed)
        },
        render = { self =>
          val consultation: OperationModel = self.props.wrapped.operation

          def changeCollapse(): () => Unit = { () =>
            self.setState(_.copy(isCollapsed = !self.state.isCollapsed))
          }

          def onclick(url: String): () => Unit = { () =>
            scalajs.js.Dynamic.global.window.open(url, "_blank")
          }

          <.article(^.className := ConsultationPresentationStyles.wrapper)(
            <.header(^.className := ConsultationPresentationStyles.collapseWrapper)(
              <.button(
                ^.className := js.Array(
                  AccordionStyles.collapseTrigger,
                  ConsultationPresentationStyles.triggerWrapper,
                  ConsultationPresentationStyles.collapseTrigger(self.state.isCollapsed)
                ),
                ^.onClick := changeCollapse()
              )(
                <.h3(^.className := TextStyles.smallerTitle)(
                  unescape(I18n.t("operation.presentation.title")),
                  <.i(
                    ^.className := js.Array(
                      ConsultationPresentationStyles.triggerIcon,
                      FontAwesomeStyles.angleRight,
                      AccordionStyles.collapseIcon,
                      AccordionStyles.collapseIconToggle(self.state.isCollapsed)
                    )
                  )()
                )
              )
            ),
            <.div(
              ^.className := js
                .Array(AccordionStyles.collapseWrapper, AccordionStyles.collapseWrapperToggle(self.state.isCollapsed))
            )(
              <.p(^.className := js.Array(TextStyles.smallerText, ConsultationPresentationStyles.presentationText))(
                unescape(self.props.wrapped.content),
                unescape("&nbsp"),
                self.props.wrapped.learnMoreUrl.map { url =>
                  <.a(
                    ^.onClick := onclick(url),
                    ^.className := js.Array(TextStyles.boldText, ConsultationPresentationStyles.presentationlink)
                  )(unescape(I18n.t("operation.presentation.seeMore")))
                },
                <.hr(
                  ^.className := js
                    .Array(ConsultationPresentationStyles.sep, RWDRulesLargeMediumStyles.hideBeyondLargeMedium)
                )()
              ),
              <.div(^.className := RWDRulesLargeMediumStyles.hideBeyondLargeMedium)(
                <.ConsultationCommunityComponent(
                  ^.wrapped := ConsultationCommunityProps(consultation, self.props.wrapped.language)
                )()
              )
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
      maxWidth(ThemeStyles.containerMaxWidth),
      marginLeft(auto),
      marginRight(auto),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val collapseWrapper: StyleA =
    style(padding(`0`, ThemeStyles.SpacingValue.small.pxToEm()))

  val triggerWrapper: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(paddingTop(25.pxToEm()), borderBottom(1.pxToEm(), solid, ThemeStyles.BorderColor.veryLight))
    )

  val collapseTrigger: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(ThemeStyles.MediaQueries.beyondLargeMedium(borderBottom(none)))
      } else
        styleS(
          ThemeStyles.MediaQueries.beyondLargeMedium(borderBottom(1.pxToEm(), solid, ThemeStyles.BorderColor.veryLight))
      )
  )

  val triggerIcon: StyleA =
    style(position.relative, top(-4.pxToEm()))

  val presentationText: StyleA =
    style(
      color(ThemeStyles.TextColor.lighter),
      paddingRight(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingLeft(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), paddingBottom(20.pxToEm()))
    )

  val presentationlink: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val sep: StyleA =
    style(
      width(100.%%),
      height(2.pxToEm(13)),
      backgroundColor(ThemeStyles.BorderColor.veryLight),
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm(13))
    )

}