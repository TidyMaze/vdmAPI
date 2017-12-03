package api

import java.time.LocalDateTime

import scala.concurrent.ExecutionContext.Implicits.global

import db.StoriesDAO
import javax.inject.Inject
import model.Story
import model.Story.storyWrites
import play.api.libs.json.Json
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import java.time.Instant
import java.time.ZoneOffset

class StoriesController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  val storiesDao = new StoriesDAO

  def index(from: Option[String], to: Option[String], author: Option[String]) = Action.async {
    val fromParsed = from.map(d => LocalDateTime.ofInstant(Instant.parse(d), ZoneOffset.UTC))
    val toParsed = to.map(d => LocalDateTime.ofInstant(Instant.parse(d), ZoneOffset.UTC))
    storiesDao.getStoriesFiltered(fromParsed, toParsed, author).map(stories =>
      Ok(Json.obj(
        "posts" -> stories,
        "count" -> stories.length
      ))
    )
  }

  def show(id: Int) = Action {

    val obj = Story(Some(42), Some("Content"), "author", LocalDateTime.now())

    Ok(Json.obj("post" -> obj))
  }

}