package api.v1.applications

object TestDataHelper {

   val appData = ApplicationData(id = 1L,
    casetext = "Erection of a 90.7m single wind turbine and truck.",
    caseofficer= "Mrs Sarah Seabury",
    casereference= "12/03483/RENE",
    agentaddress= "163 West George Street, Glasgow, G2 2JJ",
    applicantaddress= "C/o Agent",
    agent= "Mr Patrick Dunne",
    casedate= "08/01/2013",
    decisiontype= "",
    publicconsultationstartdate= "08/01/2013",
    ward= "Bellingham",
    publicconsultationenddate= "04/03/2013",
    applicantname= "Empirica Investments Ltd",
    committeearea= "West",
    decision= "Application Withdrawn")

 val appResource = ApplicationResource(id = "1",
    link = "/api/v1/application/1",
    casetext = "Erection of a 90.7m single wind turbine and truck.",
    caseofficer= "Mrs Sarah Seabury",
    casereference= "12/03483/RENE",
    agentaddress= "163 West George Street, Glasgow, G2 2JJ",
    applicantaddress= "C/o Agent",
    agent= "Mr Patrick Dunne",
    casedate= "08/01/2013",
    decisiontype= "",
    publicconsultationstartdate= "08/01/2013",
    ward= "Bellingham",
    publicconsultationenddate= "04/03/2013",
    applicantname= "Empirica Investments Ltd",
    committeearea= "West",
    decision= "Application Withdrawn")

   val appJson = ApplicationJSON(
    casetext = "Erection of a 90.7m single wind turbine and truck.",
    caseofficer= "Mrs Sarah Seabury",
    casereference= "12/03483/RENE",
    agentaddress= "163 West George Street, Glasgow, G2 2JJ",
    applicantaddress= "C/o Agent",
    agent= "Mr Patrick Dunne",
    casedate= "08/01/2013",
    decisiontype= "",
    publicconsultationstartdate= "08/01/2013",
    ward= "Bellingham",
    publicconsultationenddate= "04/03/2013",
    applicantname= "Empirica Investments Ltd",
    committeearea= "West",
    decision= "Application Withdrawn")


   val appData1 = appData
   val appData2 = appData.copy(id = 2L, ward = "Bellingham west", caseofficer = "John", casetext = "Erection of a 90.7m single wind turbine and truck.")
   val appData3 = appData.copy(id = 3L, ward = "Bellingham west")
}
