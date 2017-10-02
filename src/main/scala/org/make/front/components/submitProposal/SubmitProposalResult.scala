package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.front.components.Components._
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.models.{Theme => ThemeModel}
import org.make.front.styles.ui.CTAStyles

object SubmitProposalResult {

  case class SubmitProposalResultProps(maybeTheme: Option[ThemeModel],
                                       onBack: ()                  => _,
                                       onSubmitAnotherProposal: () => _)

  val reactClass: ReactClass =
    React
      .createClass[SubmitProposalResultProps, Unit](
        displayName = "SubmitProposalResult",
        render = { self =>
          <.div()(
            <.h2()(
              <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.handPeaceO))(),
              <.Translate(^.value := "content.proposal.confirmationThanks", ^.dangerousHtml := true)()
            ),
            <.p()(<.Translate(^.value := "content.proposal.confirmationContent", ^.dangerousHtml := true)()),
            <.button(^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton), ^.onClick := (() => {
              self.props.wrapped.onBack()
            }))(
              <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.handOLeft))(),
              self.props.wrapped.maybeTheme.map { theme =>
                <.Translate(
                  ^.value := "content.proposal.confirmationButtonBackTheme",
                  ^("theme") := theme.title,
                  ^.dangerousHtml := true
                )()
              }.getOrElse {
                <.Translate(^.value := "content.proposal.confirmationButtonBack", ^.dangerousHtml := true)()
              }
            ),
            <.button(
              ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton),
              ^.onClick := self.props.wrapped.onSubmitAnotherProposal
            )(
              <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.lightbulbTransparent))(),
              <.Translate(^.value := "content.proposal.confirmationButtonAnotherProposal", ^.dangerousHtml := true)()
            )
          )

        }
      )

}
