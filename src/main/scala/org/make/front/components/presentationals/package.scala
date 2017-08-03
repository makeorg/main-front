package org.make.front.components

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.statictags.{Attribute, SpaceSeparatedStringAttributeSpec}

import scalacss.StyleA

package object presentationals {
  implicit class RichSpaceSeparatedStringAttributeSpec(val spec: SpaceSeparatedStringAttributeSpec) extends AnyVal {
    def :=(style: StyleA): Attribute[Iterable[String]] = spec := style.htmlClass
    def :=(styleSeq: Seq[StyleA]): Attribute[Iterable[String]] = spec := styleSeq.map(_.htmlClass)
  }

  implicit class RichVirtualDOMElements(val self: VirtualDOMElements) extends AnyVal {
    // Components
    def HeaderComponent: ReactClassElementSpec = self(presentationals.HeaderComponent.reactClass)
    def UserHeaderComponent: ReactClassElementSpec = self(presentationals.UserHeaderComponent.reactClass)
    def SearchInputComponent: ReactClassElementSpec = self(presentationals.SearchInputComponent.reactClass)
    def HomeComponent: ReactClassElementSpec = self(presentationals.HomeComponent.reactClass)
    def ContainerComponent: ReactClassElementSpec = self(presentationals.ContainerComponent())
    def HomeHeaderComponent: ReactClassElementSpec = self(presentationals.HomeHeaderComponent.reactClass)
    def FooterComponent: ReactClassElementSpec = self(presentationals.FooterComponent.reactClass)
    def TagListComponent: ReactClassElementSpec = self(presentationals.TagListComponent.reactClass)
    def TagComponent: ReactClassElementSpec = self(presentationals.TagComponent.reactClass)

    // Containers
    def NotificationContainerComponent: ReactClassElementSpec =
      self(containers.NotificationContainerComponent.reactClass)
    def UserHeaderContainerComponent: ReactClassElementSpec =
      self(containers.UserHeaderContainerComponent.reactClass)
    def FooterContainerComponent: ReactClassElementSpec =
      self(containers.FooterContainerComponent.reactClass)
  }

}
