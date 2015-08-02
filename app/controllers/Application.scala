package controllers

import models.Article
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import services.ArticlesService
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

class Application(articles: ArticlesService) extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready bitch."))
  }

  def getAll = Action.async {
    articles.find map (articles => Ok(Json.toJson(articles)))
  }

  def get(id: String) = Action.async {
    articles.findById(id) map {
      case Some(article) => Ok(Json.toJson(article))
      case None => NotFound
    }
  }

  /*def add = Action.async(parse.json) { request =>
    request.body.validate[Article].fold(
      errors => Future(BadRequest(errors.toString)),
      article => articles.create(article) map {
        case Right(a) => Created(Json.toJson(a))
        case Left(e) => BadRequest(e)
      })
  }*/

}
