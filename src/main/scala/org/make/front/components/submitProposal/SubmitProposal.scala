package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.submitProposal.SubmitProposalAndAuthenticateContainer.SubmitProposalAndAuthenticateContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.Location
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingLocation
import org.make.services.tracking.TrackingService.TrackingContext

object SubmitProposal {

  case class SubmitProposalProps(onProposalProposed: () => Unit, maybeLocation: Option[Location])

  case class SubmitProposalState()

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[SubmitProposalProps, SubmitProposalState](
        displayName = "SubmitProposal",
        getInitialState = { _ =>
          SubmitProposalState()
        },
        render = { self =>
          val intro: (ReactElement) => ReactElement = { element =>
            <.div()(
              <.p(^.className := Seq(TextStyles.mediumText, TextStyles.intro, SubmitProposalStyles.intro))(
                unescape(I18n.t("submit-proposal.intro"))
              ),
              element,
              <.style()(SubmitProposalStyles.render[String])
            )
          }

          <.SubmitProposalAndAuthenticateContainerComponent(
            ^.wrapped :=
              SubmitProposalAndAuthenticateContainerProps(
                trackingContext = TrackingContext(TrackingLocation.submitProposalPage),
                intro = intro,
                onProposalProposed = self.props.wrapped.onProposalProposed,
                maybeTheme = None,
                maybeOperation = None,
                maybeSequence = None,
                maybeLocation = self.props.wrapped.maybeLocation
              )
          )()
        }
      )
    )
}

object SubmitProposalStyles extends StyleSheet.Inline {

  import dsl._

  val intro: StyleA =
    style(
      textAlign.center,
      marginBottom(ThemeStyles.SpacingValue.medium.pxToEm(15)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm(18)))
    )
}
