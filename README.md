# Valet Jenkins Plugin

The `valet-jenkins-plugin` exposes a new REST API endpoint to access paginated build data for a Jenkins instance. This plugin is built for and used by the [Valet](http://github.com/github/valet) `forecast` command line tool. Out of the box Jenkins does not provide a REST API endpoint for accessing build data in pages, so this plugin fills that gap, by exposing the following new endpoint

## Paginated Builds Endpoint

### GET `<JENKINS_URL>`/job/`<job_name>`/valet/builds?start=`<BUILD_ID>`&size=`<PAGE_SIZE>`

- `start`: The build ID to start from.
- `size`: The number of builds to return.
- `job_name`: The name of the Jenkins job to get builds for.
- `JENKINS_URL`: The URL of the Jenkins instance to get builds for.

```json
// jenkins/job/test/valet/builds?start=3&size=2

[
  {
    "id": "3",
    "startTimeMillis": 1650842700846
  },
  {
    "id": "4",
    "startTimeMillis": 1650842760960
  }
]
```
