package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions.{LoadPoliticalAction, SetPoliticalAction}
import org.make.front.components.AppState
import org.make.front.facades._
import org.make.front.models.PoliticalAction

object PoliticalActionMiddleware {

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (appStore: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case LoadPoliticalAction =>
        if (appStore.getState.politicalActions.isEmpty) {
          dispatch(SetPoliticalAction(retrievePoliticalAction()))
        }
      case action => dispatch(action)
  }

  // toDo: replace by an api call
  private def retrievePoliticalAction(): Seq[PoliticalAction] =
    Seq(
      PoliticalAction(
        themeSlug = Some("agriculture-ruralite"),
        imageUrl = alimenterre.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.festival-alimenterre.org/page/comment-puis-organiser-projection-12" target="_blank">J'organise une projection</a> <span style="color: rgba(0, 0, 0, 0.5)">|</span> <a href="http://www.festival-alimenterre.org/agenda" target="_blank">Je trouve une séance près de chez moi !</a>"""
        ),
        introduction = Some(
          "Il faut mettre en oeuvre une transition alimentaire, promouvoir une agriculture durable - Samuel, 24 ans - 43 votes favorables"
        ),
        text =
          "Le festival cinématographique Alimenterre : une occasion unique de sensibiliser votre entourage à la préservation de l'environnement"
      ),
      /*
      toDo: waiting validation
      PoliticalAction(
        themeSlug = Some("agriculture-ruralite"),
        imageUrl = andes.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="https://twitter.com/AssoANDES" target="_blank">Suis-nous sur Twitter @AssoANDES et retweete nos actus !</a> <span style="color: rgba(0, 0, 0, 0.5)">|</span> <a href="mailto:communication@epiceries-solidaires.org" target="_blank" target="_blank">Tu as des compétences de juriste ou d’informaticien ? Rejoins notre équipe de bénévole !</a>"""
        ),
        introduction =
          Some("Il faut informer et favoriser une alimentation saine, équilibrée et bio pour tous. - 20 votes"),
        text = "Avec ANDES - Quand faire ses courses ne ravit pas que ton cabas !"
      ),
       */
      PoliticalAction(
        themeSlug = Some("agriculture-ruralite"),
        imageUrl = famillesRurales.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="https://www.famillesrurales.org/agir/sengager-avec-familles-rurales" target="_blank">Je deviens bénévole</a>"""
        ),
        introduction = Some(
          "Il faut prendre en compte les problèmes de ruralité devenus insupportable au quotidien ! - 33 votes favorables"
        ),
        text = "Allez, viens ! On est bien... à la campagne et avec nos Familles rurales !"
      ),
      PoliticalAction(
        themeSlug = Some("developpement-durable-energie"),
        imageUrl = goodplanet.toString,
        imageTitle = Some("Fondation Goodplanet"),
        date = None,
        location = None,
        links = Some(
          """<a href="https://www.goodplanet.org/fr/agir-a-nos-cotes/devenir-benevole/" target="_blank">Je deviens bénévole</a> <span style="color: rgba(0, 0, 0, 0.5)">|</span> <a href="https://www.goodplanet.org/fr/agir-a-nos-cotes/ecogestes/" target="_blank">J'éco-agis !</a>"""
        ),
        introduction = Some(
          "Il faut mettre l'écologie et le développement durable au coeur de l'éducation - Isabelle, 53 ans - 86 votes favorables"
        ),
        text =
          "Il suffira d'un geste, un matin - mais aussi quand tu veux ! Notre environnement et la Fondation GoodPlanet te remercieront&nbsp;! "
      ),
      PoliticalAction(
        themeSlug = Some("developpement-durable-energie"),
        imageUrl = anper.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.peche-et-riviere.org/signaler-une-pollution.htm" target="_blank">Je signale une pollution aquatique</a>"""
        ),
        introduction = Some(
          "Il faut œuvrer à la sauvegarde de nos fleuves, rivières, lacs dans un état de plus en plus alarmant ! - Michel, 62 ans, Malbuisson - 9 votes favorables"
        ),
        text = "Pour que petit ruisseau devienne grand océan - Préservons nos cours d'eau !"
      ),
      PoliticalAction(
        themeSlug = Some("developpement-durable-energie"),
        imageUrl = amisDeLaTerre.toString,
        imageTitle = Some("Amis de la terre"),
        date = None,
        location = None,
        links = Some("""<a href="http://www.amisdelaterre.org/-groupes-.html" target="_blank">J'agis !</a>"""),
        introduction = Some(
          "Il faut partager de façon égale les ressources de la terre qui produissent assez de nourriture pour tout le monde de façon saine - Laetitia, 34 ans - 19 votes favorables"
        ),
        text = "Agis avec les Amis de la Terre pour une transition vers des sociétés soutenables "
      ),
      PoliticalAction(
        themeSlug = Some("developpement-durable-energie"),
        imageUrl = leChainonManquant.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://lechainon-manquant.fr/devenirbenevole" target="_blank">J'assure une tournée du chainon !</a>"""
        ),
        introduction = Some(
          "Il faut proposer un plan de réduction du gaspillage alimentaire par une collecte journalière, en parallèle des éboueurs, pour redistribuer !"
        ),
        text = "Le gaspillage alimentaire, ça te vénère ? La solution est par ici !"
      ),
      PoliticalAction(
        themeSlug = Some("developpement-durable-energie"),
        imageUrl = fondationPourLaNatureEtLHomme.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.fondation-nature-homme.org/action/peluchodon-donnez-vos-peluches-pour-sauver-les-grands-singes" target="_blank">Je donne</a>"""
        ),
        introduction = Some(
          "Il faut s'engager pour la protection animale et environnementale sous toutes ses formes - Betty Pelissou, 39 ans - Eaubonne"
        ),
        text = "Donne tes vieilles peluches pour protéger les grands singes avec la FNH"
      ),
      PoliticalAction(
        themeSlug = Some("developpement-durable-energie"),
        imageUrl = fondationPourLaNatureEtLHomme.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some("""<a href="http://www.adianteapps.com/elrduq" target="_blank">Je télécharge l'appli</a>"""),
        introduction =
          Some("Il faut sensibiliser les jeunes générations à la protection de l'environnement - Lucie, 18 ans"),
        text = "Télécharge Pour Ma Planète, l'appli et agis en faveur de l'environnement aux côtés de la FNH "
      ),
      PoliticalAction(
        themeSlug = Some("developpement-durable-energie"),
        imageUrl = fondationPourLaNatureEtLHomme.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.fondation-nature-homme.org/action/5-gestes-pour-devenir-eco-acteur/?page=0" target="_blank">J'éco-agis !</a>"""
        ),
        introduction = Some(
          "Il faut éduquer les consommateurs afin d'adopter des comportements et des achats éco-responsables - Thomas, 27 ans"
        ),
        text = "Grâce à la FNH, découvre 5 gestes du quotidien qui change la donne - même à ton échelle !"
      ),
      PoliticalAction(
        themeSlug = Some("economie-emploi-travail"),
        imageUrl = solidaritesNouvellesPourLeLogement.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="https://snc.asso.fr/benevole/engagement" target="_blank">J'accompagne un chercheur d'emploi</a>"""
        ),
        introduction = Some(
          "Il faut des programmes d'accompagnements personnalisés en fonction des aptitudes de chacun pour les orienter au mieux - Samuel, 24 ans - 42 votes favorables"
        ),
        text =
          "Tu es ce que l'on surnomme un Self Made Man ? Les success stories, ça te connait ? Et en plus t'as du temps à donner ? Viens en faire profiter les autres, radin !"
      ),
      PoliticalAction(
        themeSlug = Some("economie-emploi-travail"),
        imageUrl = associationFrancaiseDesFemmesIngenieurs.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.femmes-ingenieurs.org/82_p_43876/adhesion.html" target="_blank">Je suis femme ingénieure, je rejoins la communauté ! </a>"""
        ),
        introduction = Some(
          "Il faut mette en place plus de sensibilisation sur l'égalité homme/femme et les stéréotypes de genre - Audrey, 25 ans, Aspremont - 51 votes favorables"
        ),
        text =
          "L'expression \"les filles ne sont pas bonnes en maths\" te donne des boutons ? Tu es un bel exemple du contraire ? Viens par ici !"
      ),
      PoliticalAction(
        themeSlug = Some("education"),
        imageUrl = concoursAfficheArcadeTaninges.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.patrimoine-taninges.fr/Download/Inscription-Concours-Contes-2017.pdf" target="_blank">Je participe au concours</a>"""
        ),
        introduction = Some(
          "Il faut accorder plus de place à la culture, à la créativité et au vivre ensemble - Anaïs, 31 ans - 51 votes favorables"
        ),
        text = "Viens épater la galerie de tes talents de grand dramaturge au concours organisé par A.R.C.A.D.E"
      ),
      PoliticalAction(
        themeSlug = Some("education"),
        imageUrl = jetDEncre.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.jetsdencre.asso.fr/le-kit-creer-son-journal/" target="_blank">Je crée mon journal !</a>"""
        ),
        introduction = Some(
          "Il faut donner de la voix, de la vie et des moyens aux quartiers - Jérémy, 24 ans - 49 votes favorables"
        ),
        text =
          "Dans ton quartier, ta ville, ton bahut, prends la parole avant qu'on te la donne : fais ton journal avec les conseils de Jets d'Encre !"
      ),
      PoliticalAction(
        themeSlug = Some("education"),
        imageUrl = sesame.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.sesame-educ.org/index.php?page=rejoignez-nous" target="_blank">J'interviens dans les écoles !</a>"""
        ),
        introduction = Some(
          "Il faut établir un programme d'éducation sexuelle régulier à partir de la 4ème pour éduquer nos jeunes - Camille, 29 ans - 2 votes favorables"
        ),
        text = "Parce que la pornographie ne reflète pas la réalité, éduquons nos jeunes à la sexualité avec Sésame"
      ),
      PoliticalAction(
        themeSlug = Some("logement"),
        imageUrl = solidaritesNouvellesPourLeLogement.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="https://www.snl-union.org/agir-avec-snl/faire-un-don/" target="_blank">Je donne</a> <span style="color: rgba(0, 0, 0, 0.5)">|</span>  <a href="https://www.snl-union.org/agir-avec-snl/nous-confier-un-logement/" target="_blank">Je confie une logement</a> <span style="color: rgba(0, 0, 0, 0.5)">|</span>  <a href="https://www.snl-union.org/agir-avec-snl/devenir-benevole/" target="_blank">Je deviens bénévole</a>"""
        ),
        introduction = Some("Il faut permettre l'accès au logement à tous les citoyens - Delphine - 9 votes favorables"),
        text =
          "Quel plaisir de rentrer chez soi au chaud après une longue journée d'hiver ! Faisons en sorte que chacun puisse en faire de même !"
      ),
      PoliticalAction(
        themeSlug = Some("numerique-culture"),
        imageUrl = rempart.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.rempart.com/n/tous-nos-chantiers-benevoles-stages-et-services-civiques/n:120322" target="_blank">Je restaure un monument près de chez moi</a>"""
        ),
        introduction = Some(
          "Il faut proposer des mesures de valorisation de la nature, de la culture et du patrimoine local - Mickaël, 35 ans, La Calmette - 26 votes favorables"
        ),
        text = "Avec Rempart, fais de la restauration de notre patrimoine l'occasion d'une éducation citoyenne"
      ),
      /*
      toDo: waiting validation
      PoliticalAction(
        themeSlug = Some("sante-alimentation"),
        imageUrl = "",
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://blog.lesgueulescassees.org/linitiative/" target="_blank">Vous êtes producteurs, artisans, fabricants : rejoignez la démarche en signalant vos produits pour les proposer à la vente.</a> <span style="color: rgba(0, 0, 0, 0.5)">|</span>  <a href="mailto:contact@lesgueulescassees.org" target="_blank">Vous êtes consommateurs ? : signalez-nous vos coordonnées pour suivre l’initiative et son développement dans votre région – contact@lesgueulescassees.org</a>"""
        ),
        introduction = Some(
          "Il faut favoriser la vente des fruits et légumes moches en les rendant plus attractifs et en sensibilisant les gens à leur utilisation"
        ),
        text = "Qui a dit que moche n'était pas bon ? Stoppons le gachis !"
      ),
       */
      PoliticalAction(
        themeSlug = Some("sante-alimentation"),
        imageUrl = solidariteSida.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.benebox.org/594_p_47053/devenir-benevole.html" target="_blank">Je deviens bénévole</a>"""
        ),
        introduction = Some(
          "Il faut s'engager sur le sujet de la contribution française à la lutte mondiale contre le sida et les grandes pandémies "
        ),
        text = "Ensemble, faisons reculer le Sida avec Solidarité Sida"
      ),
      PoliticalAction(
        themeSlug = Some("sante-alimentation"),
        imageUrl = handicapInternational.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.handicap-international.fr/je-deviens-mob-acteur" target="_blank">Je deviens Mob'acteur</a>"""
        ),
        introduction = Some(
          "Il faut faire attention à tous ceux qui sont démunis et vulnérables : les enfants, les personnes handicapées physiques - Agnès, 59 ans, Grenade - 62 votes favorables"
        ),
        text = "Donnons un coup de main de temps à autres à Handicap International"
      ),
      PoliticalAction(
        themeSlug = Some("securite-justice"),
        imageUrl = genepi.toString,
        imageTitle = None,
        date = None,
        location = None,
        links =
          Some("""<a href="http://www.genepi.fr/p-61-devenir-membre.php" target="_blank">Je deviens bénévole</a>"""),
        introduction = Some(
          "Il faut agir sur la prison devenu une \"prépa\" à la criminalité. La prison doit devenir un lieu de formation à la vie - 133 votes favorables"
        ),
        text = "Faisons en sorte que la connaissance ne s'arrête pas aux barreaux des prisons"
      ),
      PoliticalAction(
        themeSlug = Some("securite-justice"),
        imageUrl = sansA.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://sans-a.fr/project/la-vie-apres-le-placard-saison-2/" target="_blank">Je découvre la BD</a>"""
        ),
        introduction = Some(
          "Il faut créer des centres de réinsertion par le travail ou l'associatif - Christophe, 35 ans - 69 votes favorables"
        ),
        text =
          "La vie après le placard, c'est quoi ? Au delà des a priori, des chiffres ? Viens vite le découvrir en BD !"
      ),
      /*
      toDo: waiting for links validation
      PoliticalAction(
        themeSlug = Some("democratie-vie-politique"),
        imageUrl = "",
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="https://transparency-france.org/adhesion/" target="_blank">J'adhère</a> <span style="color: rgba(0, 0, 0, 0.5)">|</span>  <a href="https://transparency-france.org/vous-etes-un-citoyen/" target="_blank">J'agis !</a>"""
        ),
        introduction = Some(
          "Il faut intensifier la lutte contre la fraude fiscale, l'économie mafieuse et la corruption à tous les niveaux - Corinne, 49 ans - 61 votes favorables"
        ),
        text = "Luttons contre la corruption sous toutes ses formes avec Transparency Internationale"
      ),
       */
      PoliticalAction(
        themeSlug = Some("democratie-vie-politique"),
        imageUrl = oxfamFrance.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some("""<a href="https://www.oxfamfrance.org/agir/benevole" target="_blank">Je deviens bénévole</a>"""),
        introduction = Some(
          "Il faut créer des lieux de mobilisation citoyenne sur les enjeux de développement durable-réfléchir ensemble avec bienveillance - Geneviève, 45 ans - 12 votes favorables"
        ),
        text = "Rejoins les rangs d'Oxfam pour mettre un terme à la pauvreté"
      ),
      PoliticalAction(
        themeSlug = Some("democratie-vie-politique"),
        imageUrl = associationPourUneDemocratieDirecte.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.pour-une-democratie-directe.fr/nous-contacter/" target="_blank">Je propose des suggestions de loi !</a> <span style="color: rgba(0, 0, 0, 0.5)">|</span> <a href="https://www.facebook.com/Association-pour-une-D%C3%A9mocratie-directe-312843953250/" target="_blank">Je suis et partage l'info sur facebook</a>"""
        ),
        introduction = Some("Il faut éduquer les citoyens à la démocratie - Pierre, 29 ans - 31 votes favorables"),
        text = "Défends les droits des citoyens de s'informer, de s'exprimer et d'agir avec Démocratie Directe !"
      ),
      PoliticalAction(
        themeSlug = Some("democratie-vie-politique"),
        imageUrl = anticor.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some("""<a href="https://anticor.espace-adherent.org/" target="_blank">J'adhère !</a>"""),
        introduction = Some(
          "Il faut instaurer une confiance des citoyens vis-à-vis de la politique - Marc-Henri, 22 ans, Le Tampon - 87 votes favorables"
        ),
        text =
          "Ethique et politique ne sont pas forcément antinomiques ! Réhabilite le rapport de confiance avec Anticor"
      ),
      PoliticalAction(
        themeSlug = Some("vivre-ensemble-solidarites"),
        imageUrl = leRefuge.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="https://www.le-refuge.org/nous-soutenir/devenir-benevole.html" target="_blank">Je témoigne / Je deviens bénévole </a>"""
        ),
        introduction = Some(
          "Il faut mettre en place plus de sensibilisation sur l'homophobie à l'école - Audrey, 25 ans, Aspremont - 45 votes favorables"
        ),
        text =
          "Le rejet appelle à la détresse, Le Refuge à la tendresse : épaule un jeune homosexuel et aide le à construire sa vie"
      ),
      PoliticalAction(
        themeSlug = Some("vivre-ensemble-solidarites"),
        imageUrl = maisonDesPotes.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.maisondespotes.fr/mettre-en-place-une-permanence-d-aide-juridictionnelle" target="_blank">Je mets en place une aide juridictionnelle</a> <span style="color: rgba(0, 0, 0, 0.5)">|</span>  <a href="http://www.maisondespotes.fr/actions-culturelles" target="_blank">Je participe à la rédaction du Pote à Pote</a> """
        ),
        introduction = Some(
          "Il faut proposer un lieu de culture et d'échange dans les quartiers sensibles privilégiant les ateliers socioculturels - Sabrina, 28 ans - 138 votes favorables"
        ),
        text = "Viens contribuer au vivre ensemble de ton quartier avec la Maison des Potes"
      ),
      PoliticalAction(
        themeSlug = Some("vivre-ensemble-solidarites"),
        imageUrl = emmaus.toString,
        imageTitle = None,
        date = None,
        location = None,
        links =
          Some("""<a href="http://emmaus-france.org/ou-donner-ou-acheter/" target="_blank">Je donne un objet</a> """),
        introduction = Some(
          "Il faut créer plus de chantiers d'insertion pour que plus de personnes se valorisent et se forment dans les secteurs associatifs et citoyens - Patrice, 45 ans - 64 votes favorables"
        ),
        text = "Quoi de mieux que d'offrir une seconde vie à ton mobilier avec Emmaüs ?"
      ),
      PoliticalAction(
        themeSlug = Some("vivre-ensemble-solidarites"),
        imageUrl = cartooningForPeace.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some("""<a href="http://www.cartooningforpeace.org/theme-2017/" target="_blank">Je participe</a> """),
        introduction = Some(
          "Il faut accepter les différences et instaurer un vrai dialogue d'acceptation de celles-ci avec un respect partagé - Daniel, 60 ans - 7 votes"
        ),
        text =
          "Tu aimes dessiner ? Cela tombe bien ! Cartooning for Peace organise un grand concours \"Vivre ensemble, c'est tout un art\""
      ),
      /*
      toDo: waiting validation
      PoliticalAction(
        themeSlug = Some("vivre-ensemble-solidarites"),
        imageUrl = "",
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://dd64.blogs.apf.asso.fr/archive/2017/08/21/l-apf-recupere-vos-livres-102755.html" target="_blank">Je donne un livre</a> """
        ),
        introduction = Some("Il faut mette des boites à livres partout - Emilie, 47 ans, Le Mans - 34 votes favorables"),
        text = "Plutôt que de les laisser prendre la poussière sur tes étagères, viens donner tes vieux livres à l'APF"
      ),
       */
      PoliticalAction(
        themeSlug = Some("vivre-ensemble-solidarites"),
        imageUrl = niPutesNiSoumises.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.npns.fr/noussoutenir/rejoignez-nous/" target="_blank">Je deviens bénévole</a> """
        ),
        introduction = Some(
          "Il faut défendre l'égalité femmes-hommes et la lutte contre toutes discriminations - Saint-Quentin - 152 votes favorables"
        ),
        text = "Accompagne Ni Putes Ni Soumises pour que laïcité, mixité et égalité deviennent bien plus que des mots "
      ),
      PoliticalAction(
        themeSlug = Some("vivre-ensemble-solidarites"),
        imageUrl = sakado.toString,
        imageTitle = None,
        date = None,
        location = None,
        links =
          Some("""<a href="http://sakado.org/les-points-de-depot-sakado" target="_blank">J'offre un Sakado</a> """),
        introduction = Some(
          "Il faut donner plus envie aux plus riches d’aider les plus pauvres avec leur argent - 48 votes favorables"
        ),
        text =
          "Un sac Sakado, ce n'est ni un toit, ni un emploi, mais ton beau geste de solidarité pour les sans-abris "
      ),
      PoliticalAction(
        themeSlug = Some("vivre-ensemble-solidarites"),
        imageUrl = sosRacisme.toString,
        imageTitle = None,
        date = None,
        location = None,
        links = Some(
          """<a href="http://sos-racisme.org/pouvons-nous-compter-sur-vous/" target="_blank">Je deviens bénévole</a> """
        ),
        introduction = Some(
          "Il faut lutter contre le racisme et toutes autres formes de discriminations envers autrui - 23 votes favorables"
        ),
        text = "Luttons contre le racisme"
      )
    )

}
