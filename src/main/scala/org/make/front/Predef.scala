package org.make.front

import io.github.shogowada.statictags.{Attribute, SpaceSeparatedStringAttributeSpec}
import scalacss.internal.StyleA

object Predef {
  implicit class RichSpaceSeparatedStringAttributeSpec(val spec: SpaceSeparatedStringAttributeSpec) extends AnyVal {
    def := (style: StyleA): Attribute[Iterable[String]] = spec := style.htmlClass
    def := (styleSeq: Seq[StyleA]): Attribute[Iterable[String]] = spec := styleSeq.map(_.htmlClass)
  }
}