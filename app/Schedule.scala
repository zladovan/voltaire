import actors.CheckChanges
import akka.actor.{ActorRef, Scheduler}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 30.07.15
 * Time: 22:28
 */
class Schedule(scheduler: Scheduler, voltaireActor: ActorRef)(implicit ec: ExecutionContext) {
  private val voltaireActorSchedule = scheduler.schedule(1.minute, 30.minutes, voltaireActor, CheckChanges)
}
