package services

import models.Article
import play.api.Logger
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 08.08.15
 * Time: 20:32
 */
class ArticlesUpdateFactory(ws: WSClient, articles: ArticlesService) {

  def update(url: String, articlesReader: ArticlesReader): (ExecutionContext => Unit) = implicit ec =>
    ws.url(url)
      .get()
      .map(responseToString andThen articlesReader andThen articles.createBulk)
      .foreach(logResult)

  def responseToString: (WSResponse => String) =
    response => new Predef.String(response.bodyAsBytes, "windows-1250")

  def logResult(result: StoreResult[List[Article]])(implicit ex: ExecutionContext) =
    result.onComplete({
      case Failure(e) => Logger.error("Error during update", e)
      case Success(Left(e)) => Logger.error("Error during update: " + e)
      case Success(Right(Nil)) => Logger.debug("No new articles found during update.")
      case Success(Right(as)) => Logger.debug(as.foldLeft(s"New articles found (${as.size}):")(_ + "\n\t" + _))
    })

}
