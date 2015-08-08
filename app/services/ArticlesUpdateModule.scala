package services

import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 08.08.15
 * Time: 23:38
 */
trait ArticlesUpdateModule {
  import com.softwaremill.macwire.MacwireMacros._

  lazy val articlesUpdateFactory: ArticlesUpdateFactory = wire[ArticlesUpdateFactory]
  lazy val articlesUpdateFunctions = List[ExecutionContext => Unit](
    articlesUpdateFactory.update("http://voltaire.netkosice.sk/ini.js", ArticlesFromJsReader)
  )

  def ws: WSClient
  def articlesService: ArticlesService
}
