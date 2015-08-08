package actors

import akka.actor.{ActorSystem, Props}
import com.softwaremill.macwire._

import scala.concurrent.ExecutionContext

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 30.07.15
 * Time: 21:33
 */
trait ActorsModule {

  def createVoltaireActor = actorSystem.actorOf(Props(wire[VoltaireActor]))

  def actorSystem: ActorSystem
  def articlesUpdateFunctions: List[ExecutionContext => Unit]
}
