### Panaseer Engineering: Scala Take Home Coding Exercise 🖥️

👋 Welcome to our Scala take-home coding project! This is a chance for you to show us how you think through solutions, approach technical designs and adapt to an existing codebase. Remember you really **shouldn't spend more than 4 hours** on this exercise, otherwise you risk over-cooking it.

⏰ If you run out of time to complete all the tasks, don't worry! Rather than coding until the last minute, set yourself an end time and use 30 minutes at the end to reflect how you can tell us about your experience and how you approached the work. If you didn't get through all the scenarios perhaps think about how you would've approached the next one and tell us about it. If you did get through all of them then tell us what you would do next with more time.

🚀 With that out of the way, let's get started...

---

### The Dataset
The dataset for this assessment is a set of building planning application records (i.e. applications submitted to request approval to construct / modify building structures). This data has been selected based on its small size in order to take the emphasis of the challenge away from data manipulation with big data. You should be able to analyse this dataset locally on a single machine without the need for a distributed computing cluster.
An overview of the dataset can be found [here](http://data.gov.uk/dataset/planning-applications-northumberland).

The actual datasets required to answer the questions can be found in the [data directory](./data).

Whilst this is a small dataset please keep in mind how you could approach the tasks with a big data mindset. When we discuss the outcome of your exercise we will ask some follow-up questions how the computations could work in a cluster for example, so it may help you to think about this whilst you work on the exercise.

### Our User
Our main user is the Planning Application Manager for Northumberland. One of their responsibilities is monitoring the planning application review process and to identify future opportunities of optimising this process. To support our user in that task, we've started creating the backend for a small Web application that will give the user information about the applications registered in the system as well as valuable insight into their overall performance and workload.

---
## The Application

For the coding exercise we are building a REST API around the planning applications dataset. Your tasks will focus on completing some of the API capabilities and well as implementing a new set of analytic endpoints to help to satisfy the user's needs.
The application is based on sbt which is the standard Scala build tool, similar to npm or gradle for Javascript or Java based projects. The application includes a sbt wrapper in the root directory, so you don't need to install it in order to work on the coding challenge. If you are unfamiliar with sbt you can find information [here](https://www.scala-sbt.org/1.x/docs/Getting-Started.html)

The application uses Play Framework, Scala (2.13), Slick and H2 in-memory database and running on Java 1.8 

### How to run

This assumes you are using the wrapper included in the root of project but the sbt shell would also do.

From the root of the project run the following command to compile it:
```
./sbt compile
```

After compiling, you can start the application by running: 
```
./sbt run
```

After a few seconds, you will see the following line indicating the application is ready to receive http requests.

```
--- (Running the application, auto-reloading is enabled) ---
```

Application will be listening on port 9000, you can test it is working by opening the following URL in your browser:

```
http://localhost:9000/api/v1/application
```

The application uses database [evolutions](https://www.playframework.com/documentation/2.8.x/Evolutions) to initialise the `applications` table in the database and there is a module to automatically load the data into the in memory database. The first time you call the endpoint above will trigger the data load and will be available in subsequent calls.

Familiarise yourself with the structure of the project and use this as a basis to add further code for the purpose of completing the tasks below.
Feel free to restructure, improve or add additional files or dependencies wherever you see fit.
---

## Your Tasks

### Extending existing functionality

#### 💡 Task 1 - Extend the `application` endpoint to support inserting and updating application in the system
Current endpoint only supports retrieving the first 50 application in the system,
```
http://localhost:9000/api/v1/application
```
and retrieving a specific application by its ID:
```
http://localhost:9000/api/v1/application/{id}
```
Please extend its functionality to support insert and updates of applications so the Application Manager can add or modify relevant information in the system.

### Analytics use cases
The Planning Application Manager is monitoring the planning application review process and looking to identify future opportunities of optimising this process. In order to satisfy their needs, the product team has decided to create a new set of analytical endpoints to provide the Application Manager with extra insight.

Feel free the design the new endpoints as you see best fit considering how other system will integrate with them. 

#### 💡 Task 2 - User Story
> **As a** _Planning Application Manager_ **I need** _to know which case officers are assigned the highest and lowest number of applications_ **so that** _I can ensure workload is evenly distributed_.

What are the top N case officers with the highest and lowest number of applications per ward? Use a parameter for N. Return the information via API endpoint in JSON format 

#### 💡 Task 3 - User Story
> **As a** _Planning Application Manager_ **I need details on the content of the case_ **so that** _I can track certain key themes over time_.

Count the occurrence of each word within the case text (CASETEXT field) across all planning application records. Return the information via API endpoint in JSON format

---
### Considerations 
*The application is likely to grow beyond these user stories in the future. As you approach technical changes for the tasks please consider maintainability, scalability and reusability of the code.*

### Testing
*Testing is very important for any production-ready application and although 4 hours are not enough to add full coverage to the exercise, we do expect to see some good examples on how you would approach different types of testing for this application in the code. 
