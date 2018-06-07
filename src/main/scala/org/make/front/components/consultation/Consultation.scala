package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.consultation.ConsultationHeader.ConsultationHeaderProps
import org.make.front.components.consultation.ConsultationSection.ConsultationSectionProps
import org.make.front.models.{OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.RWDHideRulesStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object Consultation {

  final case class ConsultationProps(operation: OperationModel,
                                     countryCode: String,
                                     language: String,
                                     onWillMount: () => Unit,
                                     activeTab: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationProps, Unit](
        displayName = "Consultation",
        componentWillMount = { self =>
          self.props.wrapped.onWillMount()
        },
        componentDidMount = { self =>
          TrackingService
            .track(
              "display-page-operation",
              TrackingContext(TrackingLocation.operationPage, operationSlug = Some(self.props.wrapped.operation.slug)),
              Map("id" -> self.props.wrapped.operation.operationId.value)
            )
        },
        render = self => {
          val consultation: OperationModel = self.props.wrapped.operation

          if (consultation.isActive) {
            <("consultation")()(
              <.div(^.className := ConsultationStyles.mainHeaderWrapper)(
                <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty),
                <.div(^.className := ConsultationStyles.fixedMainHeaderWrapper)(
                  <.CookieAlertContainerComponent.empty,
                  <.MainHeaderContainer.empty
                )
              ),
              <.ConsultationHeaderComponent(
                ^.wrapped := ConsultationHeaderProps(
                  operation = consultation,
                  language = self.props.wrapped.language,
                  activeTab = self.props.wrapped.activeTab,
                  countryCode = self.props.wrapped.countryCode
                )
              )(),
              <.section(^.className := ConsultationStyles.mainContentWrapper)(
                if (self.props.wrapped.activeTab == "consultation") {
                  <.ConsultationSection(
                    ^.wrapped := ConsultationSectionProps(
                      operation = consultation,
                      language = self.props.wrapped.language,
                      countryCode = self.props.wrapped.countryCode
                    )
                  )()
                } else {
                  <.ActionsSection()()
                },
                <.style()(ConsultationStyles.render[String])
              )
            )
          } else {
            <.div.empty
          }
        }
      )
}

object ConsultationStyles extends StyleSheet.Inline {

  import dsl._

  val mainHeaderWrapper: StyleA = style(
    paddingBottom(50.pxToEm()),
    ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
  )

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

  val contentWrapper: StyleA =
    style(display.block, backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

  val mainContentWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.grey), paddingTop(20.pxToEm()), paddingBottom(20.pxToEm()))
}
