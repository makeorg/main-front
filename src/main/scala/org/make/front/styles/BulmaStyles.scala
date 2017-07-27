package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.ValueT
import scalacss.internal.mutable.Register

object BulmaStyles extends StyleSheet.Inline {

  import dsl._

  object Layout {
    val container: StyleA = style(addClassNames("container"))
    val section: StyleA = style(addClassName("section"))
    val hero: StyleA = style(addClassName("hero"))
  }

  // see http://bulma.io/documentation/modifiers/helpers/
  object Helpers {
    val isClearfix: StyleA = style(addClassName("is-clearfix"))
    val isPulledLeft: StyleA = style(addClassName("is-pulled-left"))
    val isPulledRight: StyleA = style(addClassName("is-pulled-right"))
    val isOverlay: StyleA = style(addClassName("is-overlay"))
    val isFullWidth: StyleA = style(addClassName("is-fullwidth"))
    val hasTextCentered: StyleA = style(addClassName("has-text-centered"))
    val hasTextLeft: StyleA = style(addClassName("has-text-left"))
    val hasTextRight: StyleA = style(addClassName("has-text-right"))
    val isMarginless: StyleA = style(addClassName("is-marginless"))
    val isPaddingless: StyleA = style(addClassName("is-paddingless"))
    val isUnselectable: StyleA = style(addClassName("is-unselectable"))
    val isHidden: StyleA = style(addClassName("is-hidden"))
  }

  // see http://bulma.io/documentation/modifiers/responsive-helpers/
  object ResponsiveHelpers {
    val block: StyleA = style(addClassNames("block"))
    val flex: StyleA = style(addClassNames("flex"))
    val inline: StyleA = style(addClassNames("inline"))
    val inlineBlock: StyleA = style(addClassNames("inline-block"))
    val inlineFlex: StyleA = style(addClassNames("inline-flex"))
    val isFlexMobile: StyleA = style(addClassNames("is-flex-mobile"))
    val isFlexTabletOnly: StyleA = style(addClassNames("is-flex-tablet-only"))
    val isFlexDesktopOnly: StyleA = style(addClassNames("is-flex-desktop-only"))
    val isFlexWidescreen: StyleA = style(addClassNames("is-flex-widescreen"))
    val isFlexTouch: StyleA = style(addClassNames("is-flex-touch"))
    val isFlexTablet: StyleA = style(addClassNames("is-flex-tablet"))
    val isFlexDesktop: StyleA = style(addClassNames("is-flex-desktop"))
    val isHiddenMobile: StyleA = style(addClassNames("is-hidden-mobile"))
    val isHiddenTabletOnly: StyleA = style(addClassNames("is-hidden-tablet-only"))
    val isHiddenDesktopOnly: StyleA = style(addClassNames("is-hidden-desktop-only"))
    val isHiddenWidescreen: StyleA = style(addClassNames("is-hidden-widescreen"))
    val isHiddenTouch: StyleA = style(addClassNames("is-hidden-touch"))
    val isHiddenTablet: StyleA = style(addClassNames("is-hidden-tablet"))
    val isHiddenDesktop: StyleA = style(addClassNames("is-hidden-desktop"))
  }

  object Grid {

    // see http://bulma.io/documentation/grid/columns/
    object Columns {
      val columns: StyleA = style(addClassName("columns"))
      val columnsMultiLines: StyleA = style(addClassName("columns"), addClassName("is-multiline"))
      val column: StyleA = style(addClassName("column"))
      val isThreeQuarters: StyleA = style(addClassNames("is-three-quarters"))
      val isTwoThirds: StyleA = style(addClassNames("is-two-thirds"))
      val isHalf: StyleA = style(addClassNames("is-half"))
      val isOneThird: StyleA = style(addClassNames("is-one-third"))
      val isOneQuarter: StyleA = style(addClassNames("is-one-quarter"))
      val is2: StyleA = style(addClassNames("is-2"))
      val is3: StyleA = style(addClassNames("is-3"))
      val is4: StyleA = style(addClassNames("is-4"))
      val is5: StyleA = style(addClassNames("is-5"))
      val is6: StyleA = style(addClassNames("is-6"))
      val is7: StyleA = style(addClassNames("is-7"))
      val is8: StyleA = style(addClassNames("is-8"))
      val is9: StyleA = style(addClassNames("is-9"))
      val is10: StyleA = style(addClassNames("is-10"))
      val is11: StyleA = style(addClassNames("is-11"))
    }

    // see http://bulma.io/documentation/grid/tiles/
    object Tiles {
      val tile: StyleA = style(addClassNames("tile"))
      val isAncestor: StyleA = style(addClassNames("is-ancestor"))
      val isParent: StyleA = style(addClassNames("is-parent"))
      val isChild: StyleA = style(addClassNames("is-child"))
      val isVertical: StyleA = style(addClassNames("is-vertical"))
      val is1: StyleA = style(addClassNames("is-1"))
      val is2: StyleA = style(addClassNames("is-2"))
      val is3: StyleA = style(addClassNames("is-3"))
      val is4: StyleA = style(addClassNames("is-4"))
      val is5: StyleA = style(addClassNames("is-5"))
      val is6: StyleA = style(addClassNames("is-6"))
      val is7: StyleA = style(addClassNames("is-7"))
      val is8: StyleA = style(addClassNames("is-8"))
      val is9: StyleA = style(addClassNames("is-9"))
      val is10: StyleA = style(addClassNames("is-10"))
      val is11: StyleA = style(addClassNames("is-11"))
      val is12: StyleA = style(addClassNames("is-12"))
    }
  }

  object Element {
    val icon: StyleA = style(addClassNames("icon"))
  }
}


