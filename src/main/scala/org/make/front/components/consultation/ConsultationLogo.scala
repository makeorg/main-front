package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.models.{OperationExpanded => OperationModel, OperationWording => OperationWordingModel}

import scala.scalajs.js

object ConsultationLogo {

  case class ConsultationLogoProps(operation: OperationModel, language: String)
  case class ConsultationWordingProps(operation: OperationWordingModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[ConsultationLogoProps, Unit](displayName = "ConsultationLogo", render = (self) => {
        val consultation: OperationModel = self.props.wrapped.operation
        val wording: OperationWordingModel =
          self.props.wrapped.operation.getWordingByLanguageOrError(self.props.wrapped.language)
        <.h1()(<.img(^.src := consultation.whiteLogoUrl, ^.alt := wording.title)())
      })
}
