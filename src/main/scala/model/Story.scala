package model

import java.time.LocalDateTime


case class Story(id: Option[Int], content: Option[String], author: String, date: LocalDateTime)