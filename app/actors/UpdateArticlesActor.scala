package actors

import akka.actor.Actor

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 04.08.15
 * Time: 22:10
 */
class UpdateArticlesActor(update: => Unit) extends Actor {

  override def receive: Receive = {
    case CheckChanges => update
  }

}
