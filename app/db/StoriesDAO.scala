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
  def content = column[Option[String]]("content")
  def author = column[String]("author")
  def date = column[Timestamp]("date")
  def * = (id.?, content, author, date) <> (convModel, convStore)
  
  def convStore(s: Story): Option[(Option[Int], Option[String], String, Timestamp)] = {
    return Some((s.id, s.content, s.author, Timestamp.valueOf(s.date)))
  }
  
  def convModel(t: (Option[Int], Option[String], String, Timestamp)): Story = t match {
    case (id: Option[Int], content: Option[String], author: String, date: Timestamp) => Story(id, content, author, date.toLocalDateTime()) 
  }
}

class StoriesDAO {
  val db = Database.forConfig("postgres")
  val stories = TableQuery[Stories]

  def insert(s: Story): Future[Int] = db.run(stories += s)

  def getAllStories: Future[Seq[Story]] = db.run(stories.result)
  
  def getLatestStory: Future[Option[Story]] = db.run(stories.sortBy(_.date.desc).result.headOption)
}