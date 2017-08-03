package org.make.front.styles

import org.make.front.Main.CssSettings._

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
    val isHoverable: StyleA = style(addClassName("is-hoverable"))
    val isActive: StyleA = style(addClassName("is-active"))
    val hasDropdown: StyleA = style(addClassName("has-dropdown"))
    val isSmall: StyleA = style(addClassName("is-small"))
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
    val isLeft: StyleA = style(addClassNames("is-left"))
    val isRight: StyleA = style(addClassNames("is-right"))
    val hasIconsLeft: StyleA = style(addClassNames("has-icons-left"))
    val hasIconsRight: StyleA = style(addClassNames("has-icons-right"))
    val control: StyleA = style(addClassNames("control"))
    val button: StyleA = style(addClassNames("button"))
    val notification: StyleA = style(addClassName("notification"))
    val notificationDelete: StyleA = style(addClassName("delete"))
    val tag: StyleA = style(addClassNames("tag"))
  }

  object Syntax {
    val isPrimary: StyleA = style(addClassNames("is-primary"))
    val isInfo: StyleA = style(addClassNames("is-info"))
    val isSuccess: StyleA = style(addClassNames("is-success"))
    val isWarning: StyleA = style(addClassNames("is-warning"))
    val isDanger: StyleA = style(addClassNames("is-danger"))
    val isDark: StyleA = style(addClassNames("is-dark"))
    val isLight: StyleA = style(addClassNames("is-light"))
  }

  object Components {
    object Navbar {
      val navbar: StyleA = style(addClassNames("navbar"))
      val navbarBrand: StyleA = style(addClassNames("navbar-brand"))
      val navbarBurger: StyleA = style(addClassNames("navbar-burger"))
      val navbarMenu: StyleA = style(addClassNames("navbar-menu"))
      val navbarStart: StyleA = style(addClassNames("navbar-start"))
      val navbarEnd: StyleA = style(addClassNames("navbar-end"))
      val navbarItem: StyleA = style(addClassNames("navbar-item"))
      val navbarLink: StyleA = style(addClassNames("navbar-link"))
      val navbarDropdown: StyleA = style(addClassNames("navbar-dropdown"))
      val navbarDivider: StyleA = style(addClassNames("navbar-divider"))
    }
  }
}
