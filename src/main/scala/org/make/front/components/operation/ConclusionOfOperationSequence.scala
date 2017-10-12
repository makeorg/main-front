package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.subscribeToNewsletter.SubscribeToNewsletterFormContainer.SubscribeToNewsletterFormContainerProps
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}

import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet

object ConclusionOfOperationSequence {

  final case class ConclusionOfOperationSequenceProps(isConnected: Boolean)

  final case class ConclusionOfOperationSequenceState()

  lazy val reactClass: ReactClass =
    React.createClass[ConclusionOfOperationSequenceProps, ConclusionOfOperationSequenceState](
      displayName = "IntroOfOperationSequence",
      getInitialState = { self =>
        ConclusionOfOperationSequenceState()
      },
      render = { self =>
        def onSubscribeToNewsletterSuccess(): Unit = {
          scala.scalajs.js.Dynamic.global.console.log("yo")
        }

        val subscribeToNewsletterFormIntro: String =
          if (self.props.wrapped.isConnected) {
            unescape("Nous vous tiendrons informé.e de l’avancée et des résultats de la consultation par&nbsp;mail.")
          } else {
            unescape(
              "Nous vous invitons à saisir votre adresse e-mail pour être informé.e de l’avancée et des résultats de la&nbsp;consultation."
            )
          }

        <.div(^.className := Seq(ConclusionOfOperationSequenceStyles.wrapper))(
          <.div(^.className := ConclusionOfOperationSequenceStyles.innerWrapper)(
            <.div(^.className := Seq(RowRulesStyles.row))(
              <.div(^.className := ColRulesStyles.col)(
                <.p(
                  ^.className := Seq(ConclusionOfOperationSequenceStyles.intro, TextStyles.bigText, TextStyles.boldText)
                )(unescape("Merci pour votre contribution&nbsp;!"))
              )
            ),
            <.SubscribeToNewsletterFormContainerComponent(
              ^.wrapped := SubscribeToNewsletterFormContainerProps(
                intro = Some(subscribeToNewsletterFormIntro),
                onSubscribeToNewsletterSuccess = onSubscribeToNewsletterSuccess
              )
            )()
          ),
          <.style()(ConclusionOfOperationSequenceStyles.render[String])
        )
      }
    )
}

object ConclusionOfOperationSequenceStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(display.table, width(100.%%), height(100.%%))

  val innerWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val intro: StyleA =
    style(textAlign.center)

}
