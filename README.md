# Paginated Builds Plugin

The `paginated-builds` plugin exposes a REST API endpoint to access paginated build data for a Jenkins job. Out of the box Jenkins does not provide a method to access build data in pages - you can either get all the builds or the most recent 100 builds. This plugin assists in fetching historical build data for jobs with a large number of builds by being able to request specific pages of historical data.

## Paginated Builds Endpoint

GET `<jenkins_url>/job/<job_name>/builds?start=<build_id>&size=<page_size>&orderBy=<order_by>`

- `start`: The build ID to start from. (1 if not specified)
- `size`: The number of builds to return. (100 if not specified)
- `job_name`: The name of the Jenkins job to get builds for.
- `order_by`: The order to return the builds in (`asc` or `desc`). (`desc` if not specified)
- `jenkins_url`: The URL of the Jenkins instance.

```json
// localhost:8080/jenkins/job/integration-tests/job/designer/job/freestyle-elephant/builds?start=1&size=2&orderBy=asc

{
  "count": 2,
  "builds": [
    {
      "id": "1",
      "startTimeMillis": 1614174840363,
      "duration": 592,
      "fullName": "integration-tests » designer » freestyle-elephant #1",
      "url": "job/integration-tests/job/designer/job/freestyle-elephant/1/",
      "result": "FAILURE",
      "queueId": 8198,
      "queueTimeMillis": 1614174840346,
      "builtOn": "linux2"
    },
    {
      "id": "2",
      "startTimeMillis": 1614261240361,
      "duration": 168,
      "fullName": "integration-tests » designer » freestyle-elephant #2",
      "url": "job/integration-tests/job/designer/job/freestyle-elephant/2/",
      "result": "FAILURE",
      "queueId": 9486,
      "queueTimeMillis": 1614261240346,
      "builtOn": "__"
    }
  ]
}
```
