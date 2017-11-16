package services

import java.sql.{Date, Timestamp}
import java.util.Calendar

import models.{Domain, Domains}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class DomainService {
  def addDomain(domain: Domain): Future[Domain] = {
    Domains.insert(domain.copy(createTime = Some(new Timestamp(Calendar.getInstance().getTime.getTime))))
  }

  def updateDomain(domain: Domain): Future[Int] = {
    getDomainById(domain.id.get).filter(_.isDefined).flatMap { domainFromDB =>
      Domains.update(domainFromDB.get.copy(
        name = domain.name,
        updateTime = Some(new Timestamp(Calendar.getInstance().getTime.getTime))))
    }
  }

  def deleteDomain(id: Int): Future[Int] = {
    Domains.delete(id)
  }

  def getDomainById(id: Int): Future[Option[Domain]] = {
    Domains.getById(id)
  }

  def getAllDomains: Future[Seq[Domain]] = {
    Domains.getAll
  }
}
