package api

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.mvc.Result
import play.api.mvc.BaseController
import play.api.mvc.ControllerComponents
import javax.inject.Inject
import play.api.mvc.AbstractController

class StoriesController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok("It works!")
  }

}