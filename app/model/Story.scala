package model

import java.time.LocalDateTime
import play.api.libs.json._

object Story {
  implicit val storyReads = Json.reads[Story]
  implicit val storyWrites = Json.writes[Story]
}

case class Story(id: Option[Int], content: Option[String], author: String, date: LocalDateTime)

