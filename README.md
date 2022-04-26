# Paginated Builds Plugin

The `paginated-builds` plugin exposes a new REST API endpoint to access paginated build data for a Jenkins instance. Out of the box Jenkins does not provide a REST API endpoint for accessing build data in pages, so this plugin fills that gap, by exposing the following new endpoint

## Paginated Builds Endpoint

### GET `<jenkins_url>`/job/`<job_name>`/builds?start=`<build_id>`&size=`<page_size>`

- `start`: The build ID to start from. (1 if not specified)
- `size`: The number of builds to return. (100 if not specified)
- `job_name`: The name of the Jenkins job to get builds for.
- `jenkins_url`: The URL of the Jenkins instance to get builds for.

```json
// jenkins/job/test/builds?start=3&size=2

{
  "count": 99,
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
    },
    ...
  ]
}
```
