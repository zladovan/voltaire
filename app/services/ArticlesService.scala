package services

import models.Article
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.DB
import reactivemongo.api.commands.MultiBulkWriteResult

import scala.concurrent.Future

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 26.07.15
 * Time: 17:25
 */
trait ArticlesService {
  def create(article: Article): StoreResult[Article]
  def createBulk(articles: List[Article]): StoreResult[List[Article]]
  def find: Future[List[Article]]
  def findById(id: String): Future[Option[Article]]
}
class MongoArticlesService(db: DB) extends ArticlesService {

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  lazy val collection = db.collection[JSONCollection]("articles")

  override def create(article: Article): StoreResult[Article] = {
    collection.insert(article) map {
      case r if r.ok => Right(article)
      case r => Left(r.message)
    }
  }

  override def createBulk(articles: List[Article]): StoreResult[List[Article]] = {
    val articlesDocuments = articles map { a => Json.toJson(a).as[JsObject] }
    collection.bulkInsert(articlesDocuments.toStream, ordered = false) map {
      case r if r.ok => Right(filterAlreadyExisting(r, articles))
      case r => Left(r.errmsg.getOrElse("Unknown error"))
    }
  }

  override def find: Future[List[Article]] =
    collection.find(Json.obj()).cursor[Article]().collect[List]()


  // todo reuse common find part
  override def findById(id: String): Future[Option[Article]] =
    collection.find(Json.obj(("_id", id))).cursor[Article]().headOption


  private def filterAlreadyExisting(r: MultiBulkWriteResult, articles: List[Article]): List[Article] = {
    val existingIds = r.writeErrors.map(_.errmsg.replaceAll(".*dup key: \\{ : \"([^\"]+)\".*", "$1"))
    articles.filterNot(a => existingIds.contains(a._id))
  }

}
