import actors.ActorsModule
import akka.actor.Scheduler
import controllers.Assets
import play.api.libs.ws.WSClient
import play.api.libs.ws.ning.NingWSClient
import play.api.routing.Router
import play.api._
import play.api.ApplicationLoader.Context
import router.Routes

import scala.concurrent.ExecutionContext

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 26.07.15
 * Time: 12:44
 */
class VoltaireApplicationLoader extends ApplicationLoader {
  override def load(context: Context): Application = {
    Logger.configure(context.environment)
    (new BuiltInComponentsFromContext(context) with Components).application
  }
}

trait Components extends BuiltInComponents with ApplicationComponents with ActorsModule {
  import com.softwaremill.macwire.MacwireMacros._
  import ExecutionContext.Implicits.global

  lazy val assets: Assets = wire[Assets]
  lazy val router: Router = wire[Routes] withPrefix "/"
  lazy val ws: WSClient = NingWSClient()
  lazy val scheduler = actorSystem.scheduler

  val schedule = wire[Schedule]
}