package api.v1.applications.util

import api.v1.applications.{ApplicationData, ApplicationJSON}

object JsonMapperUtil {

  def createApplicationData(a: ApplicationJSON): ApplicationData = {
    ApplicationData(-1L, a.casetext, a.caseofficer, a.casereference, a
      .agentaddress, a.applicantaddress, a.agent, a.casedate, a.decisiontype, a.publicconsultationstartdate, a.ward,
      a.publicconsultationenddate, a.applicantname, a.committeearea, a.decision)
  }
}
