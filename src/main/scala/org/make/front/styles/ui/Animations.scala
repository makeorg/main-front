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

package org.make.front.styles.ui

import org.make.front.Main.CssSettings._

object AnimationsStyles extends StyleSheet.Inline {

  import dsl._

  // Fading Animations
  val fadeEnterAnimation =
    keyframes(0.%% -> keyframe(opacity(0)), 100.%% -> keyframe(opacity(1)))

  val fadeExitAnimation =
    keyframes(0.%% -> keyframe(opacity(1)), 100.%% -> keyframe(opacity(0)))

  val fadeOn250: StyleA =
    style(animationName(fadeEnterAnimation), animationTimingFunction.easeIn, animationDuration :=! s"0.25s")

  val fadeOff250: StyleA =
    style(animationName(fadeExitAnimation), animationTimingFunction.easeIn, animationDuration :=! s"0.25s")

  val fadeOff250Delayed500: StyleA = style(
    animationName(fadeExitAnimation),
    animationTimingFunction.easeIn,
    animationDuration :=! s"0.25s",
    animationDelay :=! s"0.5s"
  )

  val fadeOn500: StyleA =
    style(animationName(fadeEnterAnimation), animationTimingFunction.easeIn, animationDuration :=! s"0.5s")

  val fadeOff500: StyleA =
    style(animationName(fadeExitAnimation), animationTimingFunction.easeIn, animationDuration :=! s"0.5s")

  val hideChildren: StyleA = style(opacity(0), display.none)

  val showChildren: StyleA = style(opacity(1))

  // Collapse Animations
  val collapseEnterAnimation =
    keyframes(0.%% -> keyframe(transform := s"scaleY(1)"), 100.%% -> keyframe(transform := s"scaleY(0)"))

  val collapseExitAnimation =
    keyframes(0.%% -> keyframe(transform := s"scaleY(0)"), 100.%% -> keyframe(transform := s"scaleY(1)"))

  val collapseOn250: StyleA = style(
    animationName(collapseEnterAnimation),
    animationTimingFunction.easeIn,
    animationDuration :=! s"0.25s",
    transformOrigin := s"top"
  )

  val collapseOff250: StyleA = style(
    animationName(collapseExitAnimation),
    animationTimingFunction.easeIn,
    animationDuration :=! s"0.25s",
    transformOrigin := s"top"
  )

  val enableCollapse: StyleA = style(height(`0`), overflow.hidden)

  val disableCollapse: StyleA = style(height.auto)
}
