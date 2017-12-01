package model

import java.time.LocalDateTime


case class Story(content: Option[String], date: LocalDateTime, author: String)