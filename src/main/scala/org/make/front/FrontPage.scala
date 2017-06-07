package org.make.front


import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.Main.AppState

object FrontPage {


  val themes: Seq[ThemeProps] = Seq(
    ThemeProps(
      id = "vie-politique",
      name = "Vie politique",
      image = "theme01ViePolitique.jpg",
      actions = 1,
      propositions = "8.1K"
    ),
    ThemeProps(
      id = "ecologie",
      name = "Écologie",
      image = "theme02Ecologie.jpg",
      actions = 3,
      propositions = "5.6K"
    ),
    ThemeProps(
      id = "sante",
      name = "Santé",
      image = "theme03Sante.jpg",
      actions = 2,
      propositions = "3.2K"
    ),
    ThemeProps(
      id = "education-formation",
      name = "Éducation &amp; formation",
      image = "theme04Education.jpg",
      actions = 5,
      propositions = "12.6K"
    ),
    ThemeProps(
      id = "economie-industrie",
      name = "Économie &amp; industrie",
      image = "theme05EcoInd.jpg",
      actions = 3,
      propositions = "1.7K"
    ),
    ThemeProps(
      id = "emploi",
      name = "Emploi",
      image = "theme06Emploi.jpg",
      actions = 12,
      propositions = "9.9K"
    ),
    ThemeProps(
      id = "securite-justice",
      name = "Sécurité &amp; justice",
      image = "theme07Securite.jpg",
      actions = 1,
      propositions = "2.5K"
    ),
    ThemeProps(
      id = "logement",
      name = "Logement",
      image = "theme08Logement.jpg",
      actions = 2,
      propositions = "4.6K"
    ),
    ThemeProps(
      id = "discrimination",
      name = "Discrimination &amp; exclusion",
      image = "theme09Discrimination.jpg",
      actions = 2,
      propositions = "5.6K"
    ),
    ThemeProps(
      id = "agriculture",
      name = "Agriculture &amp; alimentation",
      image = "theme10Agriculture.jpg",
      actions = 7,
      propositions = "1.3K"
    ),
    ThemeProps(
      id = "impots",
      name = "Impôts &amp; budget",
      image = "theme11ImpotsBudget.jpg",
      actions = 3,
      propositions = "5.6K"
    ),
    ThemeProps(
      id = "retraites",
      name = "Retraites",
      image = "theme12Retraites.jpg",
      actions = 3,
      propositions = "5.6K"
    ),
    ThemeProps(
      id = "europe-etranger",
      name = "Europe &amp; affaires étrangères",
      image = "theme13EuropeAffEtr.jpg",
      actions = 3,
      propositions = "5.6K"
    ),
    ThemeProps(
      id = "immigration",
      name = "Immigration",
      image = "theme14Immigration.jpg",
      actions = 3,
      propositions = "5.6K"
    ),
    ThemeProps(
      id = "tourisme",
      name = "Tourisme",
      image = "theme15Tourisme.jpg",
      actions = 3,
      propositions = "5.6K"
    ),
    ThemeProps(
      id = "famille",
      name = "Famille &amp; enfance",
      image = "theme16FamilleEnfance.jpg",
      actions = 3,
      propositions = "5.6K"
    ),
    ThemeProps(
      id = "sciences-recherche",
      name = "Sciences &amp; recherche",
      image = "theme17SciencesRech.jpg",
      actions = 3,
      propositions = "5.6K"
    ),
    ThemeProps(
      id = "transports",
      name = "Transports",
      image = "theme18Transports.jpg",
      actions = 3,
      propositions = "5.6K"
    ),
    ThemeProps(
      id = "religion",
      name = "Religion",
      image = "theme19Religion.jpg",
      actions = 3,
      propositions = "5.6K"
    ),
    ThemeProps(
      id = "numerique",
      name = "Numérique",
      image = "theme20Numerique.jpg",
      actions = 3,
      propositions = "5.6K"
    ),
    ThemeProps(
      id = "sports-associations",
      name = "Vie associative &amp; sports",
      image = "theme21VieAssociative.jpg",
      actions = 3,
      propositions = "5.6K"
    )
  )


  case class ThemeListProps(propositions: Seq[ThemeProps])
  case class ThemeProps(id: String, name: String, image: String, actions: Int, propositions: String)

  def apply(): ReactClass = ReactRedux.connectAdvanced { dispatch =>
    (state: AppState, props: Props[Unit]) => {
      ThemeListProps(state.themes)
    }
  }(frontPage)

  def frontPage: ReactClass = React.createClass[ThemeListProps, Unit] { self =>
    <.div()(
      header(),
      themes(self.props.wrapped),
      footer()
    )
  }

  def themeItem(props: ThemeProps): ReactElement = {
    <.div(^.className := "theme")(
      <.a(^.className := "themeimgcontainer w-inline-block", ^.href := "#")(
        <.img(^.className := "themeimg", ^("data-ix") := "ombrombre", ^.src := s"assets/images/${props.image}")(),
        <.div(^.className := "themecontainer2lignes")(
          <.div(^.className := "themecontainer2lignesl1")(
            <.h2(^.className := "themeheading", ^("data -ix") := "ombre")(props.name)
          ),
          <.div(^.className := "themecontainer2lignesl2")(
            <.div(^.className := "themenbactionspropositionstxt")(
              s"${props.actions} ACTIONS EN COURS &nbsp; &nbsp;${props.propositions} PROPOSITIONS"
            )
          )
        )
      )
    )
  }

  def themes(props: ThemeListProps): ReactElement = {
    <.div(^.className := "margesbody")(
      <.div(^.className := "container1140 containermain")(
        <.h1(^.className := "pagethemestitle")("SÉLECTIONNEZ UN THÈME"),
        <.div(^.className := "themeflexwrap")(
          props.propositions.map { p =>
            themeItem(p)
          }
        )
      )
    )
  }

  def header(): ReactElement = {
    <.div(^.className := "header", ^.id := "top")(
      <.div(^.className := "container1140 w-clearfix")(
        <.a(^.className := "headerlogocontainer w-inline-block", ^.href := "index.html")(
          <.img(^.className := "headerlogo", ^.src := "assets/images/logoMake.svg")()
        ),
        <.div(^.className := "headerformwrapper w-clearfix w-form")(
          <.form(^.className := "headerform", ^("data-name") := "Email Form", ^.id := "email-form", ^.name := "email-form")(
            <.input(^.className := "headerinput w-input",
              ^("data-name") := "Name",
              ^.id := "name",
              ^.maxlength := 256,
              ^.name := "name",
              ^.placeholder := "Recherchez une idée, un thème, un tag",
              ^.`type` := "text")(),
            <.input(^.className := "headerinput headerinputmobile w-input",
              ^("data-name") := "Name 2",
              ^.id := "name-2", ^.maxlength := 256, ^.name := "name-2", ^.placeholder := "Recherchez...", ^.`type` := "text")()
          ),
          <.div(^.className := "w-form-done")(
            <.div()("Thank you! Your submission has been received!")
          ),
          <.div(^.className := "w-form-fail")(
            <.div()("Oops! Something went wrong while submitting the form")
          ),
          <.div(^.className := "headerlogin w-clearfix")(
            <.img(^.className := "headerloginavatar", ^.src := "assets/images/headerAvatar.png")(),
            <.div(^.className := "headerloginprenomarrow")(
              <.div(^.className := "headerloginprenom")("Marc"),
              <.div(^.className := "headerloginarrow")(
                <.span(^.className := "fontawesome pink")()
              )
            )
          )
        )
      )
    )
  }

  def footer(): ReactElement = {
    <.div(^.className := "footer")(
      <.div(^.className := "container1140footer")(
        <.div(^.className := "footerlogocontainer")(
          <.img(^.className := "footerlogo", ^.src := "assets/images/logoMake.svg")()
        ),
        <.ul(^.className := "footerlist w-clearfix")(
          <.li(^.className := "list-item")(
            <.a(^.className := "footerdevenezmaker footerlink pink", ^.href := "#")(
              <.span(^.className := "fontawesome")(""),
              "&nbsp;Devenez Maker !"
            )
          ),
          <.li()(
            <.a(^.className := "footerlink", ^.href := "#")("Conditions d’utilisation")
          ),
          <.li()(
            <.a(^.className := "footerlink", ^.href := "#")("Jobs")
          ),
          <.li()(
            <.a(^.className := "footerlink", ^.href := "#")("Espace presse")
          )
        )
      )
    )
  }


}
