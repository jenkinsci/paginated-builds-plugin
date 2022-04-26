# Paginated Builds Plugin

The `paginated-builds` plugin exposes a new REST API endpoint to access paginated build data for a Jenkins instance. Out of the box Jenkins does not provide a method to access build data in pages - you can either get all the builds, or the most recent 100 builds. For jobs that have a very large number of builds, there is no way to get the data for all the builds from the Jenkins Rest API, since it can take too long (and time out) when attempting to retrieve all the builds at once. This plugin fills that gap by exposing the following new endpoint

## Paginated Builds Endpoint

### GET `<jenkins_url>`/job/`<job_name>`/builds?start=`<build_id>`&size=`<page_size>`

- `start`: The build ID to start from. (1 if not specified)
- `size`: The number of builds to return. (100 if not specified)
- `job_name`: The name of the Jenkins job to get builds for.
- `jenkins_url`: The URL of the Jenkins instance.

```json
// localhost:8080/jenkins/job/integration-tests/job/designer/job/freestyle-elephant/builds?start=1&size=2

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
