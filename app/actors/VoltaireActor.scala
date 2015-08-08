package actors

import akka.actor.Actor

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 14.07.15
 * Time: 21:18
 */
class VoltaireActor(updateFunctions: List[ExecutionContext => Unit]) extends Actor {

  import context.dispatcher

  override def receive: Receive = {
    case CheckChanges => updateFunctions foreach (u => Future(u(implicitly[ExecutionContext])))
  }

}

case object CheckChanges
