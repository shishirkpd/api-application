-- Applications schema and initial data

-- !Ups

create table APPLICATIONS (
  ID int auto_increment not null,
   CASETEXT varchar,
   CASEOFFICER varchar,
   CASEREFERENCE varchar,
   AGENTADDRESS varchar,
   APPLICANTADDRESS varchar,
   AGENT varchar,
   CASEDATE varchar,
   DECISIONTYPE varchar,
   PUBLICCONSULTATIONSTARTDATE varchar,
   WARD varchar,
   PUBLICCONSULTATIONENDDATE varchar,
   APPLICANTNAME varchar,
   COMMITTEEAREA varchar,
   DECISION varchar,
    primary key (ID)
);

-- !Downs

drop table APPLICATIONS;