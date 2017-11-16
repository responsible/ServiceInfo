package controllers

import javax.inject.{Inject, Singleton}

import models.Domain
import play.api.libs.json._
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.DomainService
import utils.JsonReadWriter._
import utils.JsonResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class DomainController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val domainService = new DomainService

  def getDomainInfo(id: Int) = Action.async { implicit request =>
    domainService.getDomainById(id).map(domain => Ok(JsonResponse(data = Json.toJson(domain))))
  }

  def insertDomain = Action(parse.json).async { implicit request =>
    request.body.validate[Domain].fold(
      errors => Future(BadRequest(JsonResponse(code = 400, msg = "Invalid domain"))),
      domain => domainService.addDomain(domain).map(domain => Ok(JsonResponse(data = Json.toJson(domain))))
    )
  }

  def updateDomain(id: Int) = Action(parse.json).async { implicit request =>
    request.body.validate[Domain].fold(
      errors => Future(BadRequest(JsonResponse(code = 400, msg = "invalid domain"))),
      domain => domainService.updateDomain(domain.copy(id = Some(id))).map(
        rowUpdate => Ok(JsonResponse(data = Json.obj("count" -> rowUpdate))))
    )
  }

  def deleteDomain(id: Int) = Action.async { implicit request: Request[AnyContent] =>
    domainService.deleteDomain(id).map(rowUpdate => Ok(JsonResponse(data = Json.obj("count" -> rowUpdate))))
  }

  def getAllDomains = Action.async { implicit request: Request[AnyContent] =>
    domainService.getAllDomains.map { domain => Ok(JsonResponse(data = Json.toJson(domain))) }
  }
}
