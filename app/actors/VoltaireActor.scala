package actors

import models.Article
import play.api.Play.current
import play.api.Logger
import play.api.libs.ws.{WSClient, WSResponse, WS}
import akka.actor.Actor
import reactivemongo.core.errors.{DatabaseException, GenericDatabaseException}
import services.ArticlesService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 14.07.15
 * Time: 21:18
 */
class VoltaireActor(ws: WSClient, articles: ArticlesService) extends Actor {

  override def receive: Receive = {
    case CheckChanges => checkChanges()
  }

  private def checkChanges() =
    ws.url("http://voltaire.netkosice.sk/ini.js")
      .get()
      .map(responseToArticles)
      .map(articles.createBulk)
      .foreach(_.onComplete({
        case Failure(e) => Logger.error("Error during update", e)
        case Success(Left(e)) => Logger.error("Error during update: " + e)
        case Success(Right(Nil)) => Logger.debug("No new articles found during update.")
        case Success(Right(as)) => Logger.debug(as.foldLeft(s"New articles found (${as.size}):")(_ + "\n\t" + _))
    }))

  private def responseToArticles: (WSResponse) => List[Article] =
    responseToBody andThen bodyToLinesList andThen listsToArticles

  private def responseToBody: (WSResponse) => String =
    response => new Predef.String(response.bodyAsBytes, "windows-1250")

  private def bodyToLinesList: (String) => List[List[String]] = {
    _.lines.foldLeft[List[List[String]]](Nil) {
      case (data, row) if startRows contains row.trim => Nil :: data
      case (active :: data, row) => (extract(row) :: active) :: data
    }
  }

  private def listsToArticles: (List[List[String]]) => List[Article] =
    _.transpose map (_.reverse) collect { case List(a, d, ti, tx) => Article.withNewId(a, d, ti, tx) }

  private val startRows = List("Authors", "Dates", "Titles", "Texts") map { r => s"a$r = new Array(" }

  private def extract(row: String): String =
    row.trim.stripPrefix("'").stripSuffix(");").stripSuffix(",").stripSuffix("'")

}

case object CheckChanges
