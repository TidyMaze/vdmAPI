package api

import play.api.mvc.Action
import play.api.mvc.Result
import play.api.mvc.BaseController
import play.api.mvc.ControllerComponents
import javax.inject.Inject
import play.api.mvc.AbstractController
import model.Story
import java.time.LocalDateTime
import play.api.libs.json.Json
import model.Story._
import play.api.libs.json.JsObject

class StoriesController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {

    val obj = Seq(
      Story(Some(1), Some("Content1"), "author1", LocalDateTime.now()),
      Story(Some(2), Some("Content2"), "author2", LocalDateTime.now())
    )

    Ok(Json.obj(
      "posts" -> obj,
      "count" -> obj.length))
  }

  def show(id: Int) = Action {
    
    val obj = Story(Some(42), Some("Content"), "author", LocalDateTime.now())
    
    Ok(Json.obj("post" -> obj))
  }

}