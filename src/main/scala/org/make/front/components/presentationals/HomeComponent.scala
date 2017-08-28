package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.containers.ShowcaseProposalContainerComponent.ShowcaseProposalContainerProps

object HomeComponent {
  lazy val reactClass: ReactClass =
    React.createClass[Unit, Unit](
      render = (_) =>
        <.div()(
          <.HomeHeaderComponent.empty,
          <.ShowcaseProposalContainerComponent(
            ^.wrapped := ShowcaseProposalContainerProps(translationKey = "content.homepage.expressYourself")
          )()
      )
    )
}
