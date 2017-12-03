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
import java.time.Instant
import java.time.ZonedDateTime
import java.time.ZoneOffset

class Stories(tag: Tag) extends Table[Story](tag, "stories") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def content = column[Option[String]]("content")
  def author = column[String]("author")
  def date = column[Timestamp]("date")
  def * = (id.?, content, author, date) <> (convModel, convStore)

  def convStore(s: Story): Option[(Option[Int], Option[String], String, Timestamp)] = {
    return Some((s.id, s.content, s.author, Timestamp.from(s.date)))
  }

  def convModel(t: (Option[Int], Option[String], String, Timestamp)): Story = t match {
    case (id: Option[Int], content: Option[String], author: String, date: Timestamp) => Story(id, content, author, date.toInstant())
  }
}

class StoriesDAO {
  val db = Database.forConfig("postgres")
  val stories = TableQuery[Stories]

  def insert(s: Story): Future[Int] = db.run(stories += s)

  def getAllStories: Future[Seq[Story]] = db.run(stories.sortBy(_.date.desc).result)

  def getStoriesFiltered(from: Option[Instant], to: Option[Instant], author: Option[String]): Future[Seq[Story]] = {
    val q = stories
      .filter(s => if (author.isDefined) s.author === author.get else LiteralColumn(true))
      .filter(s => if (from.isDefined) s.date >= Timestamp.from(from.get) else LiteralColumn(true))
      .filter(s => if (to.isDefined) s.date <= Timestamp.from(to.get) else LiteralColumn(true))
      .sortBy(_.date.desc)
    db.run(q.result)
  }

  def getStoryById(id: Int): Future[Option[Story]] = db.run(stories.filter(_.id === id).result.headOption)
  
  def getLatestStory: Future[Option[Story]] = db.run(stories.sortBy(_.date.desc).result.headOption)
}