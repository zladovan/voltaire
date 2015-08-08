import com.typesafe.config.{Config, ConfigFactory}
import controllers.Application
import play.api.libs.ws.WSClient
import play.api.libs.ws.ning.NingWSClient
import reactivemongo.api.{DB, MongoConnectionOptions, MongoDriver}
import services.MongoArticlesService

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 26.07.15
 * Time: 13:22
 */
trait ApplicationComponents {
  import com.softwaremill.macwire.MacwireMacros._

  lazy val config: Config = ConfigFactory.load

  lazy val db: DB = {
    import reactivemongo.core.nodeset.Authenticate

    import scala.collection.JavaConversions._
    import scala.concurrent.ExecutionContext.Implicits.global

    val driver = new MongoDriver
    val dbName = config.getString("mongodb.db")
    val connection = driver.connection(
      config.getStringList("mongodb.servers"),
      MongoConnectionOptions(),
      Seq(Authenticate(
        dbName,
        config.getString("mongodb.credentials.username"),
        config.getString("mongodb.credentials.password")))
    )
    connection.db(dbName)
  }

  lazy val ws: WSClient = NingWSClient()
  lazy val applicationController = wire[Application]
  lazy val articlesService = wire[MongoArticlesService]

}
