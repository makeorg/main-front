package org.make.front


import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.Main.AppState

object FrontPage {

  case class Image(srcUrl: String,
                   base64Image: String,
                   imageAlt: String
                  )

  case class FrontPageDebateProps(
                                   firstImage: Image,
                                   secondImage: Image,
                                   id: String,
                                   title: String,
                                   description: String
                                 )

  case class WrappedProps(debates: Seq[FrontPageDebateProps])


  case class ViewDebate(id: String)

  def debateList(props: Props[WrappedProps]): ReactElement = {
    <.div(^.className := "homelist_bloc")(
      props.wrapped.debates.map { prop =>
        debateItem(prop)
      }
    )
  }


  def debateItem(props: FrontPageDebateProps): ReactElement = {
    <.div(^.className := "homelist_item")(
      <.div(^.className := "homelist_item_bkg")(
        <.img(
          ^.src := props.firstImage.srcUrl,
          ^.className := "img-responsive bkg",
          ^.alt := props.firstImage.imageAlt
        )(),
        <.noscript()(
          <.img(
            ^.src := props.firstImage.srcUrl,
            ^.alt := props.firstImage.imageAlt,
            ^.className := "img-responsive bkg"
          )()
        ),
        <.img(
          ^.src := props.secondImage.srcUrl,
          ^.className := "img-responsive overimg bkg",
          ^.alt := props.secondImage.imageAlt
        )(),
        <.noscript()(
          <.img(
            ^.src := props.secondImage.srcUrl,
            ^.alt := props.secondImage.imageAlt,
            ^.className := "img-responsive overimg"
          )()
        )
      ),
      <.div(^.className := "nsg_bloc")(
        <.div(
          ^.className := "homelist_item_title",
          ^.data := props.id
        )(
          <.h4()(
            <.span()(props.title)
          ),
          <.a(
            ^.href := "#",
            ^("data-trackevent") := "hp_selection_clic_debate",
            ^("data-tracklabel") := "theme_id",
            ^("data-trackvalue") := props.id,
            ^.className := "todebate nsg_track"
          )(props.description),
          <.a(
            ^.className := "todebatelist make_btn nsg_track",
            ^("data-trackevent") := "hp_selection_clic_moredebate",
            ^("data-tracklabel") := "theme_id",
            ^("data-trackvalue") := props.id,
            ^.href := "#"
          )("Plus de débats")
        )
      )
    )
  }

  def debateContainer: ReactClass = ReactRedux.connectAdvanced { _: Dispatch =>
    (state: AppState, _: Unit) => {
      WrappedProps(
        debates = state.debates
      )
    }
  }(FrontPage.debateList(_))

}
