package services

import java.sql.Timestamp
import java.util.Calendar

import models.{Service, Services}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ServiceService {
  def addService(service: Service): Future[Service] = {
    Services.insert(service.copy(createTime = Some(new Timestamp(Calendar.getInstance().getTime.getTime))))
  }

  def updateService(service: Service): Future[Int] = {
    getServiceById(service.id.get).filter(_.isDefined).flatMap { serviceFromDB =>
      Services.update(serviceFromDB.get.copy(
        name = service.name,
        updateTime = Some(new Timestamp(Calendar.getInstance().getTime.getTime))))
    }
  }

  def deleteService(id: Int): Future[Int] = {
    Services.delete(id)
  }

  def getServiceById(id: Int): Future[Option[Service]] = {
    Services.getById(id)
  }

  def getAllServices: Future[Seq[Service]] = {
    Services.getAll
  }
}
