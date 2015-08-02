package actors

import akka.actor.{Props, ActorSystem}
import com.softwaremill.macwire._
import play.api.libs.ws.WSClient
import services.ArticlesService

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 30.07.15
 * Time: 21:33
 */
trait ActorsModule {
  def createVoltaireActor = actorSystem.actorOf(Props(wire[VoltaireActor]))

  def actorSystem: ActorSystem

  def articlesService: ArticlesService
  def ws: WSClient
}
