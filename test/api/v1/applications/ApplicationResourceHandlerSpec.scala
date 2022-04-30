package api.v1.applications

import org.mockito.Matchers.{any, anyObject}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import play.api.MarkerContext

import javax.inject.Provider
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import api.v1.applications.TestDataHelper._

class ApplicationResourceHandlerSpec extends PlaySpec with MockitoSugar {

  private val applicationRouter = mock[Provider[ApplicationRouter]]

  private val applicationRepository = mock[ApplicationRepository]

  private val applicationResourceHandler = new ApplicationResourceHandler(applicationRouter, applicationRepository)

  implicit private val mc = mock[MarkerContext]



  "find" should {
    "return Nil" in {
      when(applicationRepository.list()).thenReturn(Future(List(appData)))
      val res = applicationResourceHandler.find(mc)

      res map { ap =>
        ap.size mustBe 1
        ap.toList.map(_.id) mustBe 1
      }
    }

    "return list of application" in {

      when(applicationRepository.list()).thenReturn(Future(List(appData)))
      val res = applicationResourceHandler.find(mc)

      res map { ap =>
        ap mustBe Nil
      }
    }
  }

  "lookup" should {
    "it should return None for the id 11L" in {
      when(applicationRepository.get(any[Long])(any())).thenReturn(Future(None))

      val res = applicationResourceHandler.lookup(11L)
      res map { ap =>
        ap mustBe None
      }
    }

    "return application for the id 1L" in {

      when(applicationRepository.get(any[Long])(any())).thenReturn(Future(Option(appData)))

      val res = applicationResourceHandler.lookup(1L)
      res map { ap =>
        ap.map(_.id) mustBe 1
      }
    }
  }

  "create" should {
    "it should successfully create the application in db" in {

      when(applicationRepository.create(anyObject())(any())).thenReturn(Future(12L))

      val res = applicationResourceHandler.create(appJson)

      res map { ap =>
        ap mustBe 12L
      }
    }

    "it should throw the exception when create fails" in {

      when(applicationRepository.create(anyObject())(any())).thenThrow(new RuntimeException("something went wrong"))

      val res = intercept[RuntimeException] {
        applicationResourceHandler.create(appJson)
      }
      assert(res.getMessage.equals(s"something went wrong"))
    }
  }

  "update" should {
    "it should not update the record if id not present" in {
      when(applicationRepository.update(anyObject())(any())).thenReturn(Future(0))
      val res = applicationResourceHandler.update(appJson, 13L)

      res map { a =>
        a mustBe 0
      }
    }

    "it should update the record for given id 11L" in {
      when(applicationRepository.update(anyObject())(any())).thenReturn(Future(1))
      val res = applicationResourceHandler.update(appJson, 11L)

      res map { a =>
        a mustBe 1
      }
    }
  }

  "getWorkLoadForOfficerWithWard" should {
    "return the Map of officer with cases per ward" in {

      when(applicationRepository.list()(any())).thenReturn(Future(List(appData1, appData2)))

      val res = applicationResourceHandler.getWorkLoadForOfficerWithWard

      res map { wlfow =>
        wlfow.get(appData1.caseofficer).size mustBe 2
      }
    }

    "return the Map of officer with cases per ward for different officer" in {

      when(applicationRepository.list()(any())).thenReturn(Future(List(appData1, appData2)))

      val res = applicationResourceHandler.getWorkLoadForOfficerWithWard

      res map { wlfow =>
        wlfow.size mustBe 2
        wlfow.get(appData1.caseofficer).size mustBe 1
        wlfow.get(appData2.caseofficer).size mustBe 1
      }
    }
  }

  "getWorkLoadForOfficer" should {
    "return the cases per officer" in {

      when(applicationRepository.list()(any())).thenReturn(Future(List(appData1, appData2, appData3)))

      val res = applicationResourceHandler.getWorkLoadForOfficer()

      res map { wlfow =>
        wlfow.size mustBe 2
        wlfow.get(appData1.caseofficer).size mustBe 2
        wlfow.get(appData2.caseofficer).size mustBe 1
      }
    }

    "return the cases per officer whose cases are 2" in {

      when(applicationRepository.list()(any())).thenReturn(Future(List(appData1, appData2, appData3)))

      val res = applicationResourceHandler.getWorkLoadForOfficer()

      res map { wlfow =>
        wlfow.size mustBe 1
        wlfow.get(appData1.caseofficer).size mustBe 2
      }
    }
  }

  "fetchCases" should {
    "return all the cases if no keyword present" in {
      when(applicationRepository.list()(any())).thenReturn(Future(List(appData1, appData2, appData3)))

      val res = applicationResourceHandler.fetchCases()
      res map { appCases =>
        appCases.size mustBe 3
      }
    }

    "return all the cases if no keyword present truck" in {
      when(applicationRepository.list()(any())).thenReturn(Future(List(appData1, appData2, appData3)))

      val keyWord = "truck"
      val res = applicationResourceHandler.fetchCases(Option(keyWord))
      res map { appCases =>
        appCases.size mustBe 1
        appCases.contains(keyWord) mustBe true
      }
    }

    "return all the cases if keyword present turbine" in {
      when(applicationRepository.list()(any())).thenReturn(Future(List(appData1, appData2, appData3)))
      val keyWord = "turbine"
      val res = applicationResourceHandler.fetchCases(Option(keyWord))
      res map { appCases =>
        appCases.size mustBe 2
        appCases.contains(keyWord) mustBe true
      }
    }
  }
}
