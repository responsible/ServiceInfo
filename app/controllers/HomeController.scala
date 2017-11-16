package controllers

import javax.inject._

import play.api.mvc._
import utils.JsonResponse

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(JsonResponse(msg = "Welcome to Play"))
  }
}
