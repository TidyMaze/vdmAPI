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
import java.time.ZonedDateTime
import scala.util.Try
import scala.util.Success
import java.time.format.DateTimeParseException
import scala.util.Failure
import java.security.InvalidParameterException
import scala.concurrent.Future

class StoriesController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  val storiesDao = new StoriesDAO

  def index(from: Option[String], to: Option[String], author: Option[String]) = Action.async {
    val fromParsed = from.map(d => Try(Instant.parse(d)))
    val toParsed = to.map(d => Try(Instant.parse(d)))

    (fromParsed, toParsed) match {
      case (Some(Failure(e)), _) => Future.successful(BadRequest(s"Invalid parameter from : ${from.get} : ${e.getMessage}"))
      case (_, Some(Failure(e))) => Future.successful(BadRequest(s"Invalid parameter to : ${to.get} : ${e.getMessage}"))
      case (paramFrom, paramTo) => storiesDao.getStoriesFiltered(paramFrom.map(_.get), paramTo.map(_.get), author).map(stories =>
        Ok(Json.obj(
          "posts" -> stories,
          "count" -> stories.length)))
    }

  }

  def show(id: Int) = Action.async {
    storiesDao.getStoryById(id).map {
      case Some(s) => Ok(Json.obj("post" -> s))
      case None    => NotFound(s"404 : Post with id $id doesn't exist :(")
    }
  }

}