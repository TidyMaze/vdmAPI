package db

import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import java.sql.Date
import scala.util.Success
import scala.util.Failure
import model.Story
import java.sql.Timestamp
import java.time.LocalDateTime
import scala.concurrent.Future
import slick.dbio.Effect.Write

class Stories(tag: Tag) extends Table[Story](tag, "stories") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def content = column[String]("content")
  def author = column[String]("author")
  def date = column[Timestamp]("date")
  def * = (id.?, content, author, date) <> (convModel, convStore)
  
  def convStore(s: Story): Option[(Option[Int], String, String, Timestamp)] = {
    return Some((s.id, s.content.getOrElse(null), s.author, Timestamp.valueOf(s.date)))
  }
  
  def convModel(t: (Option[Int], String, String, Timestamp)): Story = t match {
    case (id: Option[Int], content: String, author: String, date: Timestamp) => Story(id, Option(content), author, date.toLocalDateTime()) 
  }
}

class StoriesDAO {
  val db = Database.forConfig("postgres")
  val stories = TableQuery[Stories]

  def insert(s: Story): Future[Int] = {
    try {
      return db.run(stories += s)
    } finally db.close
  }

  def getAllStories: Future[Seq[Story]] = {
    try {
      return db.run(stories.result)
    } finally db.close
  }

}