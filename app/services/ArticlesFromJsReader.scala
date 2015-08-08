package services

import models.Article

/**
 * Created with IntelliJ IDEA.
 * User: zladovan
 * Date: 04.08.15
 * Time: 22:16
 */
protected object ArticlesFromJsReader extends ArticlesReader {

  override def apply(jsFileContent: String): List[Article] =
    (contentToLinesLists andThen linesListsToArticles)(jsFileContent)

  private def contentToLinesLists: (String) => List[List[String]] = {
    _.lines.foldLeft[List[List[String]]](Nil) {
      case (data, row) if startRows contains row.trim => Nil :: data
      case (active :: data, row) => (extract(row) :: active) :: data
    }
  }

  private def linesListsToArticles: (List[List[String]]) => List[Article] =
    _.transpose map (_.reverse) collect { case List(a, d, ti, tx) => Article.withNewId(a, d, ti, tx) }

  private val startRows = List("Authors", "Dates", "Titles", "Texts") map { r => s"a$r = new Array(" }

  private def extract(row: String): String =
    row.trim.stripPrefix("'").stripSuffix(");").stripSuffix(",").stripSuffix("'")
}
