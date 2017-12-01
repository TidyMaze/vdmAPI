package db

import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import java.sql.Date
import scala.util.Success
import scala.util.Failure

class Stories(tag: Tag) extends Table[(Int, String, String, Date)](tag, "stories") {
  def id = column[Int]("id", O.PrimaryKey)
  def content = column[String]("content")
  def author = column[String]("author")
  def date = column[Date]("date")
  def * = (id, content, author, date)
}

class StoriesDAO {
  val db = Database.forConfig("postgres")
  val stories = TableQuery[Stories]

  def addStory = {
    try {
      val actions = DBIO.seq(
        stories.schema.create,
        stories += (42, "Bla", "Auth", new java.sql.Date(new java.util.Date().getTime))
      )

      db.run(actions).onComplete {
        case Success(r) => println("Query OK")
        case Failure(t) => println("failure in db query " + t.getMessage)
      }
    } finally db.close
  }

  def getAllStories = {
    try {
      println("Stories:")
      db.run(stories.result).map(_.foreach {
        case (id, content, author, date) =>
          println("  " + id + "\t" + content + "\t" + author + "\t" + date)
      }).onComplete {
        case Success(r) => println("Query OK")
        case Failure(t) => println("failure in db query " + t.getMessage)
      }
    } finally db.close
  }

}