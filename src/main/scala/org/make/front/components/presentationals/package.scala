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
    def UserNavComponent: ReactClassElementSpec = self(presentationals.UserNavComponent.reactClass)
    def SearchInputComponent: ReactClassElementSpec = self(presentationals.SearchInputComponent.reactClass)
    def HomeComponent: ReactClassElementSpec = self(presentationals.HomeComponent.reactClass)
    def ContainerComponent: ReactClassElementSpec = self(presentationals.ContainerComponent())
    def MainIntroComponent: ReactClassElementSpec = self(presentationals.MainIntroComponent.reactClass)
    def FooterComponent: ReactClassElementSpec = self(presentationals.FooterComponent.reactClass)
    def TagListComponent: ReactClassElementSpec = self(presentationals.TagListComponent.reactClass)
    def TagComponent: ReactClassElementSpec = self(presentationals.TagComponent.reactClass)
    def ThemeHeaderComponent: ReactClassElementSpec = self(presentationals.ThemeHeaderComponent.reactClass)
    def ConnectUserComponent: ReactClassElementSpec = self(presentationals.ConnectUserComponent.reactClass)
    def ProposalMatrixComponent: ReactClassElementSpec = self(presentationals.ProposalMatrixComponent.reactClass)
    def TagFilterComponent: ReactClassElementSpec = self(presentationals.TagFilterComponent.reactClass)
    def ProposalTileComponent: ReactClassElementSpec = self(presentationals.ProposalTileComponent.reactClass)
    def VoteComponent: ReactClassElementSpec = self(presentationals.VoteComponent.reactClass)
    def VoteButtonComponent: ReactClassElementSpec = self(presentationals.VoteButtonComponent.reactClass)
    def QualificationButtonComponent: ReactClassElementSpec =
      self(presentationals.QualificationButtonComponent.reactClass)
    def ProposalSubmitComponent: ReactClassElementSpec = self(presentationals.ProposalSubmitComponent.reactClass)

    // Containers
    def NotificationContainerComponent: ReactClassElementSpec =
      self(containers.NotificationContainerComponent.reactClass)
    def UserNavContainerComponent: ReactClassElementSpec =
      self(containers.UserNavContainerComponent.reactClass)
    def ConnectUserContainerComponent: ReactClassElementSpec = self(containers.ConnectUserContainerComponent.reactClass)
    def ProposalMatrixContainerComponent: ReactClassElementSpec =
      self(containers.ProposalMatrixContainerComponent.reactClass)
    def NavigationInThemesContainerComponent: ReactClassElementSpec =
      self(containers.NavigationInThemesContainerComponent.reactClass)
    def PoliticalActionsComponent: ReactClassElementSpec =
      self(containers.PoliticalActionContainerComponent.reactClass)
    def ProposalSubmitContainerComponent: ReactClassElementSpec =
      self(containers.ProposalSubmitContainerComponent.reactClass)
    def PasswordRecoveryContainerComponent: ReactClassElementSpec =
      self(containers.PasswordRecoveryContainerComponent.reactClass)
    def PasswordResetContainerComponent: ReactClassElementSpec =
      self(containers.PasswordResetContainerComponent.reactClass)
    def ShowcaseProposalContainerComponent: ReactClassElementSpec =
      self(containers.ShowcaseProposalContainerComponent.reactClass)
    def ActivateAccountContainerComponent: ReactClassElementSpec =
      self(containers.ActivateAccountContainerComponent.reactClass)
  }
}
