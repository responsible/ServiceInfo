package controllers

import javax.inject.{Inject, Singleton}

import models.Service
import models.Services.{serviceReads, serviceWrites}
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import services.ServiceService
import utils.JsonResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ServiceController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val serviceService = new ServiceService

  def getServiceInfo(id: Int) = Action.async { implicit request =>
    serviceService.getServiceById(id).map(service => Ok(JsonResponse(data = Json.toJson(service))))
  }

  def insertService = Action(parse.json).async { implicit request =>
    request.body.validate[Service].fold(
      errors => Future(BadRequest(JsonResponse(code = 400, msg = "Invalid service"))),
      service => serviceService.addService(service).map(service => Ok(JsonResponse(data = Json.toJson(service))))
    )
  }

  def updateService(id: Int) = Action(parse.json).async { implicit request =>
    request.body.validate[Service].fold(
      errors => Future(BadRequest(JsonResponse(code = 400, msg = "invalid service"))),
      service => serviceService.updateService(service.copy(id = Some(id))).map(
        rowUpdate => Ok(JsonResponse(data = Json.obj("count" -> rowUpdate))))
    )
  }

  def deleteService(id: Int) = Action.async { implicit request =>
    serviceService.deleteService(id).map(rowUpdate => Ok(JsonResponse(data = Json.obj("count" -> rowUpdate))))
  }

  def getAllServices = Action.async { implicit request =>
    serviceService.getAllServices.map { service => Ok(JsonResponse(data = Json.toJson(service))) }
  }
}
