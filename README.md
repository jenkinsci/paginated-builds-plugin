# Valet Jenkins Plugin

The `valet-jenkins-plugin` exposes a new REST API endpoint to access paginated build data for a Jenkins instance. This plugin is built for and used by the [Valet](http://github.com/github/valet) `forecast` command line tool. Out of the box Jenkins does not provide a REST API endpoint for accessing build data in pages, so this plugin fills that gap, by exposing the following new endpoint

```http
GET <YOUR_JENKINS_URL>/job/<job_name>/valet/builds?start=<BUILD_ID>&size=<PAGE_SIZE>
```
