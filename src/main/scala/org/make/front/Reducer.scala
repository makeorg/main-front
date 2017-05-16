package org.make.front

import org.make.front.FrontPage.{FrontPageDebateProps, Image}
import org.make.front.Main.AppState

object Reducer {

  def reduce(maybeState: Option[AppState], action: Any): AppState = {

    val state = maybeState.getOrElse(defaultState())

    action match {
      case _ => state
    }

  }

  private def defaultState(): AppState =
    AppState(
      debates = Seq(
        FrontPageDebateProps(
          firstImage = Image(
            srcUrl = "https://cdn.make.org/upload/5878d3f591edd.jpg",
            base64Image = "R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==",
            imageAlt = "Education et formation professionnelle"
          ),
          secondImage = Image(
            srcUrl = "https://cdn.make.org/upload/5878d3f5be9a5.jpg",
            base64Image = "R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==",
            imageAlt = "Education et formation professionnelle"
          ),
          id = "49",
          title = "Education et formation professionnelle",
          description = "Comment réformer l&#039;école ?"
        )
      )
    )

}
