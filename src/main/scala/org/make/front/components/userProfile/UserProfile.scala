package org.make.front.components.userProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{User => UserModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.{CTAStyles, InputStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scalacss.internal.{Attr, StyleA}
import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

object UserProfile {

  final case class UserProfileProps(user: Option[UserModel], logout: () => Unit)

  val reactClass: ReactClass =
    React
      .createClass[UserProfileProps, Unit](
        displayName = "UserProfile",
        render = (self) => {

          <.div(^.className := UserProfileStyles.wrapper)(
            <.MainHeaderComponent.empty,
            <.div(^.className := Seq(RowRulesStyles.centeredRow))(
              <.div(^.className := ColRulesStyles.col)(
                <.div(^.className := UserProfileStyles.pageWrapper)(
                  <.div(
                    ^.className := Seq(
                      UserProfileStyles.avatarAndNavWrapper,
                      ColRulesStyles.col,
                      ColRulesStyles.colQuarterBeyondMedium
                    )
                  )(
                    <.span(^.className := UserProfileStyles.avatarWrapper)(
                      if (self.props.wrapped.user.flatMap(_.profile).flatMap(_.avatarUrl).nonEmpty) {
                        <.img(
                          ^.src := self.props.wrapped.user.flatMap(_.profile).flatMap(_.avatarUrl).getOrElse(""),
                          ^.className := UserProfileStyles.avatar,
                          ^.alt := self.props.wrapped.user.flatMap(_.firstName).getOrElse(""),
                          ^("data-pin-no-hover") := "true"
                        )()
                      } else {
                        <.i(^.className := Seq(UserProfileStyles.avatarPlaceholder, FontAwesomeStyles.user))()
                      }
                    ),
                    <.div(^.className := UserProfileStyles.disconnectButtonWrapper)(
                      <.button(
                        ^.className := Seq(CTAStyles.basicOnButton, CTAStyles.basic, CTAStyles.moreDiscreet),
                        ^.onClick := self.props.wrapped.logout
                      )(
                        <.i(^.className := Seq(FontAwesomeStyles.signOut))(),
                        unescape("&nbsp;" + I18n.t("user-profile.disconnect-cta"))
                      )
                    )
                  ),
                  <.div(
                    ^.className := Seq(
                      UserProfileStyles.contentWrapper,
                      ColRulesStyles.col,
                      ColRulesStyles.colHalfBeyondMedium
                    )
                  )(
                    <.header(^.className := UserProfileStyles.titleWrapper)(
                      <.h1(^.className := Seq(UserProfileStyles.title, TextStyles.mediumTitle))(
                        I18n.t("user-profile.title")
                      )
                    ),
                    <.form(^.novalidate := true)(
                      <.div(^.className := UserProfileStyles.infosWrapper)(
                        <.div(^.className := UserProfileStyles.infoFieldWrapper)(
                          <.div(^.className := UserProfileStyles.infoFieldLabelWrapper)(
                            <.p(^.className := TextStyles.smallText)(I18n.t("authenticate.inputs.email.placeholder"))
                          ),
                          <.div(^.className := UserProfileStyles.infoFieldInputWrapper)(
                            <.div(
                              ^.className := Seq(
                                InputStyles.wrapper,
                                InputStyles.withIcon,
                                UserProfileStyles.emailInputWithIconWrapper
                              )
                            )(
                              <.input(
                                ^.`type`.email,
                                ^.required := true,
                                ^.placeholder := s"${I18n.t("authenticate.inputs.email.placeholder")} ${I18n.t("authenticate.inputs.required")}",
                                ^.value := self.props.wrapped.user.map(_.email).getOrElse(""),
                                ^.readOnly := true
                              )()
                            )
                          )
                        ),
                        <.div(^.className := UserProfileStyles.infoFieldWrapper)(
                          <.div(^.className := UserProfileStyles.infoFieldLabelWrapper)(
                            <.p(^.className := TextStyles.smallText)(
                              I18n.t("authenticate.inputs.first-name.placeholder")
                            )
                          ),
                          <.div(^.className := UserProfileStyles.infoFieldInputWrapper)(
                            <.div(
                              ^.className := Seq(
                                InputStyles.wrapper,
                                InputStyles.withIcon,
                                UserProfileStyles.firstNameInputWithIconWrapper
                              )
                            )(
                              <.input(
                                ^.`type`.text,
                                ^.required := true,
                                ^.placeholder := s"${I18n.t("authenticate.inputs.first-name.placeholder")} ${I18n.t("authenticate.inputs.required")}",
                                ^.value := self.props.wrapped.user.flatMap(_.firstName).getOrElse(""),
                                ^.readOnly := true
                              )()
                            )
                          )
                        ),
                        if (self.props.wrapped.user
                              .flatMap(_.profile)
                              .flatMap(_.departmentNumber)
                              .isDefined) {
                          <.div(^.className := UserProfileStyles.infoFieldWrapper)(
                            <.div(^.className := UserProfileStyles.infoFieldLabelWrapper)(
                              <.p(^.className := TextStyles.smallText)(
                                I18n.t("authenticate.inputs.postal-code.placeholder")
                              )
                            ),
                            <.div(^.className := UserProfileStyles.infoFieldInputWrapper)(
                              <.div(
                                ^.className := Seq(
                                  InputStyles.wrapper,
                                  InputStyles.withIcon,
                                  UserProfileStyles.postalCodeInputWithIconWrapper
                                )
                              )(
                                <.input(
                                  ^.`type`.text,
                                  ^.required := false,
                                  ^.placeholder := s"${I18n.t("authenticate.inputs.postal-code.placeholder")}",
                                  ^.value := self.props.wrapped.user
                                    .flatMap(_.profile)
                                    .flatMap(_.departmentNumber)
                                    .getOrElse(""),
                                  ^.readOnly := true
                                )()
                              )
                            )
                          )
                        },
                        if (self.props.wrapped.user
                              .flatMap(_.profile)
                              .flatMap(_.profession)
                              .isDefined) {
                          <.div(^.className := UserProfileStyles.infoFieldWrapper)(
                            <.div(^.className := UserProfileStyles.infoFieldLabelWrapper)(
                              <.p(^.className := TextStyles.smallText)(I18n.t("authenticate.inputs.job.placeholder"))
                            ),
                            <.div(^.className := UserProfileStyles.infoFieldInputWrapper)(
                              <.div(
                                ^.className := Seq(
                                  InputStyles.wrapper,
                                  InputStyles.withIcon,
                                  UserProfileStyles.professionInputWithIconWrapper
                                )
                              )(
                                <.input(
                                  ^.`type`.text,
                                  ^.required := false,
                                  ^.placeholder := s"${I18n.t("authenticate.inputs.job.placeholder")}",
                                  ^.value := self.props.wrapped.user
                                    .flatMap(_.profile)
                                    .flatMap(_.profession)
                                    .getOrElse(""),
                                  ^.readOnly := true
                                )()
                              )
                            )
                          )
                        }
                      )
                    )
                  )
                )
              )
            ),
            <.style()(UserProfileStyles.render[String])
          )
        }
      )
}

object UserProfileStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(minHeight(100.%%), backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

  val pageWrapper: StyleA =
    style(
      minHeight(300.pxToEm()),
      margin :=! s"${ThemeStyles.SpacingValue.larger.pxToEm().value} 0",
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)",
      ThemeStyles.MediaQueries.beyondSmall(paddingLeft(ColRulesStyles.gutter), paddingRight(ColRulesStyles.gutter)),
      ThemeStyles.MediaQueries.beyondMedium(padding(`0`))
    )

  val content: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val avatarAndNavWrapper: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondMedium(paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm())),
      textAlign.center
    )

  val contentWrapper: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingTop(ThemeStyles.SpacingValue.larger.pxToEm())),
      paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm())
    )

  val titleWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()))

  val title: StyleA =
    style()

  val disconnectButtonWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.medium.pxToEm()))

  val avatarWrapper: StyleA =
    style(
      position.relative,
      display.inlineBlock,
      verticalAlign.middle,
      width(80.pxToEm()),
      height(80.pxToEm()),
      marginTop(-10.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(marginTop(-20.pxToEm())),
      overflow.hidden,
      backgroundColor(ThemeStyles.BackgroundColor.white),
      borderRadius(50.%%),
      border :=! s"2px solid ${ThemeStyles.BorderColor.base.value}",
      ThemeStyles.MediaQueries.beyondSmall(width(160.pxToEm()), height(160.pxToEm()), borderWidth(5.px)),
      textAlign.center
    )

  val avatarPlaceholder: StyleA =
    style(
      width(100.%%),
      lineHeight(76.pxToEm(32)),
      ThemeStyles.MediaQueries.beyondSmall(lineHeight(150.pxToEm(64)), fontSize(64.pxToEm())),
      fontSize(32.pxToEm()),
      color(ThemeStyles.TextColor.lighter)
    )

  val avatar: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      transform := s"translate(-50%, -50%)",
      width(80.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(width(160.pxToEm())),
      minWidth(100.%%),
      minHeight(100.%%),
      maxWidth.none,
      maxHeight.none
    )

  val infosWrapper: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(display.table, width(100.%%)))

  val infoFieldWrapper: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(display.tableRow))

  val infoFieldLabelWrapper: StyleA =
    style(
      display.none,
      ThemeStyles.MediaQueries.beyondSmall(
        display.tableCell,
        color(ThemeStyles.TextColor.lighter),
        textAlign.right,
        paddingRight(ThemeStyles.SpacingValue.small.pxToEm())
      )
    )

  val infoFieldInputWrapper: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(display.tableCell))

  val emailInputWithIconWrapper: StyleA =
    style(borderColor.transparent, (&.before)(Attr.real("content") := "'\\f003'"))

  val firstNameInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      borderColor.transparent,
      (&.before)(Attr.real("content") := "'\\f007'")
    )

  val ageInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      borderColor.transparent,
      (&.before)(Attr.real("content") := "'\\f1ae'")
    )

  val postalCodeInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      borderColor.transparent,
      (&.before)(Attr.real("content") := "'\\f041'")
    )

  val professionInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      borderColor.transparent,
      (&.before)(Attr.real("content") := "'\\f0f2'")
    )
}
