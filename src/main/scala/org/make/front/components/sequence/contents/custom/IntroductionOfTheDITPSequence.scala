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

package org.make.front.components.sequence.contents.custom

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{sequenceIntroIcon, I18n}
import org.make.front.models.{OperationIntroPartner, OperationIntroWording}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object IntroductionOfTheDITPSequence {

  final case class IntroductionOfTheDITPSequenceState(title: String,
                                                      explanation1: Option[String],
                                                      explanation2: Option[String],
                                                      cta: String,
                                                      duration: Option[String],
                                                      partners: js.Array[OperationIntroPartner])
  final case class IntroductionOfTheDITPSequenceProps(clickOnButtonHandler: () => Unit,
                                                      introWording: OperationIntroWording)

  lazy val reactClass: ReactClass =
    React
      .createClass[IntroductionOfTheDITPSequenceProps, IntroductionOfTheDITPSequenceState](
        displayName = "IntroductionOfTheDITPSequence",
        getInitialState = { self =>
          IntroductionOfTheDITPSequenceState(
            title = self.props.wrapped.introWording.title.getOrElse(unescape(I18n.t("sequence.introduction.title"))),
            explanation1 = self.props.wrapped.introWording.explanation1,
            explanation2 = self.props.wrapped.introWording.explanation2,
            cta =
              self.props.wrapped.introWording.cta.getOrElse(unescape("&nbsp;" + I18n.t("sequence.introduction.cta"))),
            duration = self.props.wrapped.introWording.duration,
            partners = self.props.wrapped.introWording.partners
          )
        },
        render = { self =>
          <.div(^.className := js.Array(IntroductionOfTheDITPSequenceStyles.introCardWrapper))(
            <.p(^.className := IntroductionOfTheDITPSequenceStyles.extraIcon)(
              <.img(
                ^.src := sequenceIntroIcon.toString,
                ^.alt := "",
                ^.className := IntroductionOfTheDITPSequenceStyles.sequenceIcon
              )()
            ),
            <.div(^.className := IntroductionOfTheDITPSequenceStyles.contentWrapper)(
              <.div(^.className := IntroductionOfTheDITPSequenceStyles.titleWrapper)(
                <.p(^.className := js.Array(TextStyles.bigIntro, IntroductionOfTheDITPSequenceStyles.title))(
                  self.state.title
                )
              ),
              self.state.explanation1.map { content =>
                <.p(^.className := IntroductionOfTheDITPSequenceStyles.explanation)(content)

              },
              self.state.explanation2.map { content =>
                <.p(^.className := IntroductionOfTheDITPSequenceStyles.explanation)(content)
              },
              self.state.duration.map { duration =>
                <.p(^.className := IntroductionOfTheDITPSequenceStyles.explanation)(duration)
              },
              <.div(^.className := IntroductionOfTheDITPSequenceStyles.ctaWrapper)(
                <.button(
                  ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton),
                  ^.onClick := self.props.wrapped.clickOnButtonHandler
                )(<.i(^.className := js.Array(FontAwesomeStyles.play))(), self.state.cta)
              )
            ),
            if (self.state.partners.nonEmpty) {
              <.p(^.className := IntroductionOfTheDITPSequenceStyles.extraPartners)(self.state.partners.map { partner =>
                <.img(^.src := partner.imageUrl, ^.alt := partner.name, ^.key := partner.name)()
              }.toSeq)

            },
            <.style()(IntroductionOfTheDITPSequenceStyles.render[String])
          )
        }
      )

}

object IntroductionOfTheDITPSequenceStyles extends StyleSheet.Inline {

  import dsl._

  val introCardWrapper: StyleA =
    style(
      display.flex,
      width(100.%%),
      height(100.%%),
      flexFlow := "column",
      padding(`0`, ThemeStyles.SpacingValue.small.pxToEm()),
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
    style(maxWidth(100.%%), marginBottom(25.pxToEm()))

  val title: StyleA =
    style(textAlign.center, color(ThemeStyles.TextColor.lighter))

  val explanationWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()),
      maxWidth(100.%%)
    )

  val explanation: StyleA =
    style(
      textAlign.center,
      ThemeStyles.Font.circularStdBook,
      fontSize(15.pxToEm()),
      lineHeight(20.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(20.pxToEm()), lineHeight(30.pxToEm()))
    )

  val ctaWrapper: StyleA =
    style(textAlign.center, marginTop(20.pxToEm()))

  val extraPartners: StyleA =
    style(
      TextStyles.smallerText,
      color(ThemeStyles.TextColor.lighter),
      display.flex,
      flexFlow := "column",
      justifyContent.center,
      alignItems.center,
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(flexFlow := "row")
    )

  val extraIcon: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val sequenceIcon: StyleA =
    style(maxWidth(75.pxToEm()), ThemeStyles.MediaQueries.beyondSmall(maxWidth(100.%%)))

  val partnerItem: StyleA =
    style(display.inlineBlock, marginLeft(ThemeStyles.SpacingValue.small.pxToEm()))
}
