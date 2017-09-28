package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.{FontAwesomeStyles, TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.{Attr, Length}
import scalacss.internal.mutable.StyleSheet

object ProposalInfos {

  final case class ProposalInfosProps(proposal: ProposalModel)

  val reactClass: ReactClass =
    React.createClass[ProposalInfosProps, Unit](render = (self) => {

      def infos(proposal: ProposalModel): String = {

        (proposal.author.age, proposal.author.postalCode) match {

          case (Some(age), Some(postalCode)) =>
            unescape(
              I18n.t(
                "content.proposal.fullHeader",
                Replacements(
                  ("firstName", proposal.author.firstname.getOrElse(I18n.t("anonymous"))),
                  ("age", s"${age.toString}"),
                  ("postalCode", s"$postalCode")
                )
              )
            )

          case (Some(age), None) =>
            unescape(
              I18n.t(
                "content.proposal.ageHeader",
                Replacements(
                  ("firstName", proposal.author.firstname.getOrElse(I18n.t("anonymous"))),
                  ("age", s"${age.toString}")
                )
              )
            )

          case (None, Some(postalCode)) =>
            unescape(
              I18n.t(
                "content.proposal.postalCodeHeader",
                Replacements(
                  ("firstName", proposal.author.firstname.getOrElse(I18n.t("anonymous"))),
                  ("postalCode", s"$postalCode")
                )
              )
            )

          case (None, None) =>
            unescape(
              I18n.t(
                "content.proposal.postalCodeHeader",
                Replacements(("firstName", proposal.author.firstname.getOrElse(I18n.t("anonymous"))))
              )
            )
        }
      }

      def label(trending: String): Seq[ReactElement] = {
        trending match {
          case "hot" =>
            Seq(
              <.div(^.className := ProposalInfosStyles.labelWrapper)(
                <.p(^.className := ProposalInfosStyles.label)(
                  <("svg")(
                    ^("xmlns") := "http://www.w3.org/2000/svg",
                    ^("x") := "0px",
                    ^("y") := "0px",
                    ^("width") := "24",
                    ^("height") := "24",
                    ^("viewBox") := "0 0 24 24"
                  )(
                    <("path")(
                      ^("d") := "M9.2,19.5c-1-2.1-0.5-3.3,0.3-4.4c0.8-1.2,1.1-2.4,1.1-2.4s0.7,0.9,0.4,2.2 c1.2-1.3,1.4-3.4,1.2-4.2c2.6,1.8,3.8,5.8,2.2,8.8c8.1-4.6,2-11.4,1-12.2c0.4,0.8,0.4,2.1-0.3,2.7c-1.2-4.6-4.2-5.5-4.2-5.5 c0.4,2.4-1.3,4.9-2.8,6.9c-0.1-0.9-0.1-1.6-0.6-2.5C7.3,10.6,6,12,5.6,13.7C5.1,16,6,17.7,9.2,19.5z"
                    )()
                  )
                )
              )
            )
          case "trending" =>
            Seq(
              <.div(^.className := ProposalInfosStyles.labelWrapper)(
                <.p(^.className := ProposalInfosStyles.label)(
                  <.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.lineChart))()
                )
              )
            )
          case "new" =>
            Seq(
              <.div(^.className := ProposalInfosStyles.labelWrapper)(
                <.p(^.className := ProposalInfosStyles.label)("new")
              )
            )
          case _ => Seq()
        }
      }

      <.div(^.className := ProposalInfosStyles.wrapper)(
        <.div(^.className := ProposalInfosStyles.infosWrapper)(
          <.h4(^.className := Seq(TextStyles.smallText, ProposalInfosStyles.infos))(infos(self.props.wrapped.proposal))
        ),
        self.props.wrapped.proposal.trending.map(label).getOrElse(Seq.empty),
        <.style()(ProposalInfosStyles.render[String])
      )

    })
}

object ProposalInfosStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA = style(display.table, width(100.%%))

  val infosWrapper: StyleA = style(display.tableCell, verticalAlign.middle, width(100.%%))

  val infos: StyleA =
    style(color(ThemeStyles.TextColor.light))

  val labelWrapper: StyleA = style(display.tableCell, verticalAlign.middle)

  val label: StyleA = style(
    ThemeStyles.Font.circularStdBold,
    fontSize(11.pxToEm()),
    display.inlineBlock,
    verticalAlign.middle,
    width(24.pxToEm(11)),
    height(24.pxToEm(11)),
    lineHeight(24.pxToEm(11)),
    borderRadius(50.%%),
    backgroundColor(ThemeStyles.ThemeColor.prominent),
    color(ThemeStyles.TextColor.white),
    overflow.hidden,
    textAlign.center,
    unsafeChild("path")(Attr.real("fill") := ThemeStyles.TextColor.white)
  )
}
