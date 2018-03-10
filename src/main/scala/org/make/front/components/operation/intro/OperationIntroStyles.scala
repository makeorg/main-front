package org.make.front.components.operation.intro

import org.make.front.Main.CssSettings._
import org.make.front.styles._
import org.make.front.styles.utils._

object OperationIntroStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.black))

  val presentationInnerWrapper: StyleA =
    style(
      position.relative,
      zIndex(1),
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(
        padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`, ThemeStyles.SpacingValue.large.pxToEm())
      )
    )

  val titleWrapper: StyleA = style(maxWidth(470.pxToEm()), marginLeft.auto, marginRight.auto)

  val logoWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val infos: StyleA =
    style(width(100.%%), textAlign.center, backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent))

  val separator: StyleA =
    style(margin(ThemeStyles.SpacingValue.medium.pxToEm(), `0`, ThemeStyles.SpacingValue.small.pxToEm()))

  val separatorLineWrapper: StyleA =
    style(width(50.%%), paddingTop(2.pxToEm()))

  val separatorLine: StyleA =
    style(height(1.px), width(100.%%), backgroundColor(ThemeStyles.BorderColor.white), opacity(0.2))

  val separatorLineToTheLeft: StyleA = style(
    maxWidth(290.pxToEm()),
    marginLeft.auto,
    background := s"linear-gradient(to left, rgba(255,255,255,1) 0%, rgba(255,255,255,0) 100%)"
  )

  val separatorLineToTheRight: StyleA = style(
    maxWidth(290.pxToEm()),
    marginRight.auto,
    background := s"linear-gradient(to right, rgba(255,255,255,1) 0%, rgba(255,255,255,0) 100%)"
  )

  val separatorTextWrapper: StyleA = style(padding(`0`, 20.pxToEm()))

  val separatorText: StyleA = style(color(ThemeStyles.TextColor.white), opacity(0.5))

  val partnersList: StyleA = style(textAlign.center)

  val partnerItem: StyleA = style(
    display.inlineBlock,
    verticalAlign.middle,
    padding((ThemeStyles.SpacingValue.small / 2).pxToEm(), ThemeStyles.SpacingValue.small.pxToEm())
  )

  val partnerLogo: StyleA = style()

  val otherPartners: StyleA = style(textAlign.center, color(ThemeStyles.TextColor.white), opacity(0.5))

  val explanationWrapper: StyleA =
    style(
      position.relative,
      zIndex(1),
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent)
    )

  val explanationIll: StyleA =
    style(width(100.%%), ThemeStyles.MediaQueries.belowSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm())))

  val explanationTextWrapper: StyleA = style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val explanationText: StyleA = style(color(ThemeStyles.TextColor.white))

  val ctaWrapper: StyleA = style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))
}
