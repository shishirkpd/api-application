package api.v1.applications.util

import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import api.v1.applications.TestDataHelper._

class JsonMapperUtilSpec extends PlaySpec with MockitoSugar {
  "createApplicationData" should {
    "create a ApplicationData for given ApplicationJSON" in {
      val res = JsonMapperUtil.createApplicationData(appJson)
      res.id mustBe -1
    }
  }

}
