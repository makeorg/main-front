/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.sequence.contents

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationIntroPartner, OperationIntroWording}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object IntroductionOfTheSequence {

  final case class IntroductionOfTheSequenceState(title: String,
                                                  explanation1: Option[String],
                                                  explanation2: Option[String],
                                                  partnershipText: Option[String],
                                                  cta: String,
                                                  duration: Option[String],
                                                  partners: js.Array[OperationIntroPartner])
  final case class IntroductionOfTheSequenceProps(clickOnButtonHandler: () => Unit, introWording: OperationIntroWording)

  lazy val reactClass: ReactClass =
    React
      .createClass[IntroductionOfTheSequenceProps, IntroductionOfTheSequenceState](
        displayName = "IntroductionOfTheSequence",
        getInitialState = { self =>
          IntroductionOfTheSequenceState(
            title = self.props.wrapped.introWording.title.getOrElse(unescape(I18n.t("sequence.introduction.title"))),
            explanation1 = self.props.wrapped.introWording.explanation1,
            explanation2 = self.props.wrapped.introWording.explanation2,
            partnershipText = self.props.wrapped.introWording.partnershipText,
            cta =
              self.props.wrapped.introWording.cta.getOrElse(unescape("&nbsp;" + I18n.t("sequence.introduction.cta"))),
            duration = self.props.wrapped.introWording.duration,
            partners = self.props.wrapped.introWording.partners
          )
        },
        render = { self =>
          <.div(^.className := js.Array(IntroductionOfTheSequenceStyles.introCardWrapper))(
            <.div(^.className := IntroductionOfTheSequenceStyles.contentWrapper)(
              <.div(^.className := IntroductionOfTheSequenceStyles.titleWrapper)(
                <.p(^.className := js.Array(TextStyles.bigIntro, IntroductionOfTheSequenceStyles.title))(
                  self.state.title
                )
              ),
              self.state.explanation1.map { content =>
                <.div(^.className := IntroductionOfTheSequenceStyles.explanationWrapper)(
                  <.p(
                    ^.className := js.Array(TextStyles.biggerMediumText, IntroductionOfTheSequenceStyles.explanation)
                  )(content)
                )
              },
              self.state.explanation2.map { content =>
                <.div(^.className := IntroductionOfTheSequenceStyles.explanationWrapper)(
                  <.p(
                    ^.className := js.Array(TextStyles.biggerMediumText, IntroductionOfTheSequenceStyles.explanation)
                  )(content)
                )
              },
              self.state.duration.map { duration =>
                <.div(^.className := IntroductionOfTheSequenceStyles.explanationWrapper)(
                  <.p(
                    ^.className := js.Array(
                      TextStyles.biggerMediumText,
                      TextStyles.boldText,
                      IntroductionOfTheSequenceStyles.explanation
                    )
                  )(duration)
                )
              },
              <.div(^.className := IntroductionOfTheSequenceStyles.ctaWrapper)(
                <.button(
                  ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton),
                  ^.onClick := self.props.wrapped.clickOnButtonHandler
                )(<.i(^.className := js.Array(FontAwesomeStyles.play))(), self.state.cta)
              )
            ),
            if (self.state.partners.nonEmpty) {
              <.p(^.className := IntroductionOfTheSequenceStyles.extraPartners)(
                self.state.partnershipText,
                <.ul()(self.state.partners.map { partner =>
                  <.li(^.className := IntroductionOfTheSequenceStyles.partnerItem)(
                    <.img(^.src := partner.imageUrl, ^.alt := partner.name, ^.key := partner.name)()
                  )
                }.toSeq)
              )
            },
            <.style()(IntroductionOfTheSequenceStyles.render[String])
          )
        }
      )

}

object IntroductionOfTheSequenceStyles extends StyleSheet.Inline {

  import dsl._

  val introCardWrapper: StyleA =
    style(
      display.flex,
      width(100.%%),
      height(100.%%),
      flexFlow := "column",
      alignItems.center,
      justifyContent.spaceBetween
    )

  val contentWrapper: StyleA =
    style(
      display.flex,
      maxWidth(100.%%),
      height(100.%%),
      flexFlow := "column",
      alignItems.center,
      justifyContent.center
    )

  val titleWrapper: StyleA =
    style(maxWidth(100.%%), marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val title: StyleA =
    style(textAlign.center, color(ThemeStyles.TextColor.lighter))

  val explanationWrapper: StyleA =
    style(
      maxWidth(100.%%),
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm())
    )

  val explanation: StyleA =
    style(textAlign.center)

  val ctaWrapper: StyleA =
    style(textAlign.center, marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val extraPartners: StyleA =
    style(
      TextStyles.smallerText,
      color(ThemeStyles.TextColor.lighter),
      display.flex,
      flexFlow := "column",
      justifyContent.center,
      alignItems.center,
      ThemeStyles.MediaQueries.beyondMedium(flexFlow := "row")
    )

  val partnerItem: StyleA =
    style(display.inlineBlock, marginLeft(ThemeStyles.SpacingValue.small.pxToEm()))
}
