package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationExpanded => OperationModel}
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._

object ConsultationLinkSequence {

  case class ConsultationLinkSequenceProps(operation: OperationModel, country: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationLinkSequenceProps, Unit](
      displayName = "ConsultationLinkSequence",
      render = (self) => {
        val consultation: OperationModel = self.props.wrapped.operation

        <.div()(
          unescape(I18n.t("operation.sequence.link.presentation")),

          <.Link(^.to := s"/${self.props.wrapped.country}/consultation/${consultation.slug}/selection")(
            unescape(I18n.t("operation.sequence.link.cta"))
          )
        )
      }
    )
}