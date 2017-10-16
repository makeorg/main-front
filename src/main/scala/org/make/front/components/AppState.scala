package org.make.front.components

import org.make.front.models.{
  SequenceId            => SequenceIdModel,
  Operation             => OperationModel,
  Sequence              => SequenceModel,
  BusinessConfiguration => BusinessConfigurationModel,
  PoliticalAction       => PoliticalActionModel,
  OperationId           => OperationIdModel,
  GradientColor         => GradientColorModel,
  ProposalId            => ProposalIdModel,
  User                  => UserModel,
  Theme                 => ThemeModel
}

final case class AppState(configuration: Option[BusinessConfigurationModel],
                          politicalActions: Seq[PoliticalActionModel],
                          operations: Seq[OperationModel] = Seq(
                            OperationModel(
                              operationId = OperationIdModel("1"),
                              slug = "stop-aux-violences-faites-aux-femmes",
                              title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
                              actionsCount = 0,
                              proposalsCount = 0,
                              color = "#660779",
                              gradient = Some(GradientColorModel("#AB92CA", "#54325A")),
                              tags = Seq()
                            )
                          ),
                          sequences: Seq[SequenceModel] = Seq(
                            SequenceModel(
                              sequenceId = SequenceIdModel("1"),
                              slug = "comment-lutter-contre-les-violences-faites-aux-femmes",
                              title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
                              proposalsSlugs = Seq(
                                "il-faut-inventer-un-code-pour-signaler-facilement-que-lon-est-harcelee-exemple-commander-un-cocktail-angel-dans-un-bar",
                                "il-faut-un-organisme-public-pour-pouvoir-signaler-recenser-et-publier-les-cas-de-harcelement-au-travail-dans-les-entreprises",
                                "il-faut-que-les-reseaux-sociaux-creent-des-outils-integres-ex-chatbots-pour-venir-en-aide-aux-ados-victimes-de-cyber-harcelement",
                                "il-faut-une-charte-ethique-pour-toutes-les-publicites-stop-a-lutilisation-de-limage-des-femmes-comme-objet-sexuel",
                                "il-faut-des-formations-specifiques-pour-les-gendarmes-et-les-policiers-sur-la-prise-en-charge-des-femmes-victimes",
                                "il-faut-creer-plus-de-lieux-ou-les-femmes-puissent-se-refugier-a-toute-heure-en-cas-durgence",
                                "il-faut-ameliorer-le-signalement-avec-les-technologies-telephones-equipes-dun-bouton-durgence-appli-pour-signaler-le-harcelement-de-rue",
                                "il-faut-des-procedures-facilitees-de-depot-de-plaintes-sans-que-les-policiers-dissuadent-ou-minimisent-les-faits",
                                "il-faut-que-les-bus-deposent-les-femmes-entre-les-arrets-la-nuit-quand-elles-le-demandent",
                                "il-faut-former-les-jeunes-femmes-aux-techniques-de-self-defense-pour-mieux-se-proteger-et-se-defendre"
                              )
                            )
                          ),
                          bait: String = "Il faut ",
                          connectedUser: Option[UserModel],
                          country: String = "FR",
                          language: String = "fr") {

  def themes: Seq[ThemeModel] = configuration.map(_.themesForLocale(country, language)).getOrElse(Seq.empty)
  def findTheme(slug: String): Option[ThemeModel] = themes.find(_.slug == slug)

  def findOperation(slug: String): Option[OperationModel] = operations.find(_.slug == slug)

}
