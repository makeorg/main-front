package org.make.front.components

import org.make.front.models._

import org.make.front.models.{Operation => OperationModel}
import org.make.front.models.{Sequence  => SequenceModel}

final case class AppState(configuration: Option[BusinessConfiguration],
                          politicalActions: Seq[PoliticalAction],
                          operations: Seq[OperationModel] = Seq(
                            OperationModel(
                              id = OperationId("1"),
                              slug = "vff",
                              title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
                              actionsCount = 0, // actions count
                              proposalsCount = 0, // proposal count
                              color = "#660779",
                              gradient = Some(GradientColor("#AB92CA", "#54325A")),
                              tags = Seq()
                            )
                          ),
                          sequences: Seq[SequenceModel] = Seq(
                            SequenceModel(
                              id = SequenceId("1"),
                              slug = "comment-lutter-contre-les-violences-faites-aux-femmes",
                              title = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?"
                            )
                          ),
                          bait: String = "Il faut",
                          connectedUser: Option[User],
                          notifications: Seq[Notification],
                          country: String = "FR",
                          language: String = "fr") {

  def themes: Seq[Theme] = configuration.map(_.themesForLocale(language)).getOrElse(Seq.empty)
  def findTheme(slug: String): Option[Theme] = themes.find(_.slug == slug)

  def findOperation(slug: String): Option[Operation] = operations.find(_.slug == slug)

}
