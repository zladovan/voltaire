package models

import java.nio.charset.Charset
import java.text.Normalizer

import com.google.common.hash.Hashing
import com.google.common.io.BaseEncoding
import play.api.libs.json.Json

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 26.07.15
 * Time: 17:35
 */
case class Article(
    _id: String,
    author: String,
    date: String,
    title: String,
    text: String)

object Article {
  implicit val reader = Json.reads[Article]
  implicit val writer = Json.writes[Article]

  def withNewId(author: String, date: String, title: String, text: String): Article =
    Article(hashId(title + "-" + date), author, date, title, text)

  private def hashId(id: String): String =
    BaseEncoding.base64().omitPadding().encode(Hashing.crc32c.hashString(id, Charset.forName("utf-8")).asBytes())
}