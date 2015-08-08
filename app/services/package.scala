import models.Article

import scala.concurrent.Future

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 08.08.15
 * Time: 21:16
 */
package object services {
  type ArticlesReader = String => List[Article]
  type StoreResult[A] = Future[Either[String, A]]
}
