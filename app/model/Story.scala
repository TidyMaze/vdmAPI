package model

import java.time.LocalDateTime
import play.api.libs.json._
import java.time.ZonedDateTime
import java.time.Instant

object Story {
  implicit val storyReads = Json.reads[Story]
  implicit val storyWrites = Json.writes[Story]
}

case class Story(id: Option[Int], content: Option[String], author: String, date: Instant)