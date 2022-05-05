package io.jenkins.plugins;

import hudson.model.*;
import io.jenkins.plugins.paginatedbuilds.api.*;
import io.jenkins.plugins.paginatedbuilds.model.*;
import com.gargoylesoftware.htmlunit.Page;
import hudson.model.queue.QueueTaskFuture;
import hudson.model.Fingerprint.RangeSet;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import com.cloudbees.workflow.util.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.List;

/**
 * Test the raw build APIs
 */
public class BuildAPITest {
  private static long startTime;

  @Rule
  public JenkinsRule jenkinsRule = new JenkinsRule();

  @After
  public void after() {
  }

  @BeforeClass
  public static void setup() {
    startTime = System.currentTimeMillis();
  }

  @Test
  public void testFreestyleOneBuild() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");

    QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
    jenkinsRule.assertBuildStatusSuccess(build);

    BuildResponse builds = getBuilds(job, "builds/?orderBy=asc");

    Assert.assertEquals(1, builds.getCount());
    BuildExt buildExt = builds.getBuilds().get(0);
    assertBuildInfoOkay(job, buildExt, "1");
  }

  @Test
  public void testFreestyleTwoBuilds() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");

    QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
    jenkinsRule.assertBuildStatusSuccess(build);
    QueueTaskFuture<FreeStyleBuild> build2 = job.scheduleBuild2(0);
    jenkinsRule.assertBuildStatusSuccess(build2);

    BuildResponse builds = getBuilds(job, "builds/?orderBy=asc");

    Assert.assertEquals(2, builds.getCount());
    BuildExt buildExt = builds.getBuilds().get(1);
    assertBuildInfoOkay(job, buildExt, "2");
  }

  @Test
  public void testFreestyleTenBuildsSizeFive() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");

    for (int i = 0; i < 10; i++) {
      QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
      jenkinsRule.assertBuildStatusSuccess(build);
    }

    BuildResponse builds = getBuilds(job, "builds/?size=5&orderBy=asc");

    Assert.assertEquals(5, builds.getCount());
    BuildExt buildExt = builds.getBuilds().get(4);
    assertBuildInfoOkay(job, buildExt, "5");
  }

  @Test
  public void testFreestyleTenBuildsStartSix() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");

    for (int i = 0; i < 10; i++) {
      QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
      jenkinsRule.assertBuildStatusSuccess(build);
    }

    BuildResponse builds = getBuilds(job, "builds/?start=6&orderBy=asc");

    Assert.assertEquals(5, builds.getCount());
    BuildExt buildExt = builds.getBuilds().get(4);
    assertBuildInfoOkay(job, buildExt, "10");
  }

  @Test
  public void testFreestyleTenBuildsStartSixSizeTwo() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");

    for (int i = 0; i < 10; i++) {
      QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
      jenkinsRule.assertBuildStatusSuccess(build);
    }

    BuildResponse builds = getBuilds(job, "builds/?start=6&size=2&orderBy=asc");

    Assert.assertEquals(2, builds.getCount());
    BuildExt buildExt = builds.getBuilds().get(1);
    assertBuildInfoOkay(job, buildExt, "7");
  }

  @Test
  public void testPipelineBuild() throws Exception {
    WorkflowJob job = jenkinsRule.jenkins.createProject(WorkflowJob.class, "TestJob");

    job.setDefinition(new CpsFlowDefinition("" +
        "node {" +
        "   stage ('Build'); " +
        "   echo ('Building'); " +
        "   stage ('Test'); " +
        "   echo ('Testing'); " +
        "   stage ('Deploy'); " +
        "     writeFile file: 'file.txt', text:'content'; " +
        "     archive(includes: 'file.txt'); " +
        "   echo ('Deploying'); " +
        "}", true));

    QueueTaskFuture<WorkflowRun> build = job.scheduleBuild2(0);
    jenkinsRule.assertBuildStatusSuccess(build);

    BuildResponse builds = getBuilds(job, "builds");

    Assert.assertEquals(1, builds.getCount());
    BuildExt buildExt = builds.getBuilds().get(0);
    assertBuildInfoOkay(job, buildExt, "1");
  }

  @Test
  public void testNoBuilds() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");
    BuildAPI buildAPI = new BuildAPI();
    BuildResponse builds = buildAPI.doBuilds(0, 10, "desc");

    Assert.assertEquals(0, builds.getCount());
  }

  @Test
  public void testAdditionalBuilds() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");
    BuildAPI buildAPI = new BuildAPI();
    for (int i = 0; i < 10; i++) {
      QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
      jenkinsRule.assertBuildStatusSuccess(build);
    }

    RangeSet range = BuildAPI.createRangeSet(job, 1, 8, false);
    List<Run> builds = ((Job) job).getBuilds(range);
    buildAPI.addAdditionalBuilds((Job) job, builds, 10, false);

    Assert.assertEquals(10, builds.size());
  }

  @Test
  public void testAdditionalBuildsDesc() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");
    BuildAPI buildAPI = new BuildAPI();
    for (int i = 0; i < 10; i++) {
      QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
      jenkinsRule.assertBuildStatusSuccess(build);
    }

    RangeSet range = BuildAPI.createRangeSet(job, 0, 8, true);
    List<Run> builds = ((Job) job).getBuilds(range);
    buildAPI.addAdditionalBuilds((Job) job, builds, 10, true);

    Assert.assertEquals(10, builds.size());
  }

   @Test
   public void testFetchesBuildsInAscendingOrder() throws Exception {
     FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");
     for (int i = 0; i < 10; i++) {
       QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
       jenkinsRule.assertBuildStatusSuccess(build);
     }

     BuildResponse builds = getBuilds(job, "builds/?orderBy=asc");
     Assert.assertEquals(builds.getBuilds().get(0).getId(), "1");
     Assert.assertEquals(builds.getBuilds().get(9).getId(), "10");
   }

  @Test
  public void testFetchesBuildsInDescendingOrderNoOrderBy() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");
    for (int i = 0; i < 10; i++) {
      QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
      jenkinsRule.assertBuildStatusSuccess(build);
    }

    BuildResponse builds = getBuilds(job, "builds/");
    Assert.assertEquals(builds.getBuilds().get(0).getId(), "10");
    Assert.assertEquals(builds.getBuilds().get(9).getId(), "1");
  }

   @Test
  public void testFetchesBuildsInDescendingOrder() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");
    for (int i = 0; i < 10; i++) {
      QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
      jenkinsRule.assertBuildStatusSuccess(build);
    }

    BuildResponse builds = getBuilds(job, "builds/?orderBy=desc");
    Assert.assertEquals(builds.getBuilds().get(0).getId(), "10");
     Assert.assertEquals(builds.getBuilds().get(9).getId(), "1");
   }

  @Test
  public void testFetchesBuildsInDescendingOrderByDefault() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");
    for (int i = 0; i < 10; i++) {
      QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
      jenkinsRule.assertBuildStatusSuccess(build);
    }

    BuildResponse builds = getBuilds(job, "builds/?orderBy=NotanOrderBy");
    Assert.assertEquals(builds.getBuilds().get(0).getId(), "10");
    Assert.assertEquals(builds.getBuilds().get(9).getId(), "1");
  }

  @Test
  public void testCreateRangeSet() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");
    for (int i = 0; i < 10; i++) {
      QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
      jenkinsRule.assertBuildStatusSuccess(build);
    }

    RangeSet range = BuildAPI.createRangeSet(job, 1, 5, true);
    Assert.assertEquals(range.min(), 1);
    Assert.assertEquals(range.max(), 6);
  }

  @Test
  public void testCreateRangeSet2() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");
    for (int i = 0; i < 10; i++) {
      QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
      jenkinsRule.assertBuildStatusSuccess(build);
    }

    RangeSet range = BuildAPI.createRangeSet(job, 0, 5, true);
    Assert.assertEquals(range.min(), 6);
    Assert.assertEquals(range.max(), 11);
  }

  @Test
  public void testCreateRangeSet3() throws Exception {
    FreeStyleProject job = jenkinsRule.jenkins.createProject(FreeStyleProject.class, "TestJob");
    for (int i = 0; i < 10; i++) {
      QueueTaskFuture<FreeStyleBuild> build = job.scheduleBuild2(0);
      jenkinsRule.assertBuildStatusSuccess(build);
    }

    RangeSet range = BuildAPI.createRangeSet(job, 0, 5, false);
    Assert.assertEquals(range.min(), 1);
    Assert.assertEquals(range.max(), 6);
  }

  private void assertBuildInfoOkay(Job job, BuildExt buildExt, String jobNumber) {
    Assert.assertEquals("TestJob #" + jobNumber, buildExt.getFullName());
    Assert.assertEquals("SUCCESS", buildExt.getResult());
    Assert.assertTrue(buildExt.getDuration() > 0);
    Assert.assertTrue(buildExt.getStartTimeMillis() > startTime);
    Assert.assertEquals(jobNumber, buildExt.getId());
    Assert.assertTrue(buildExt.getStartTimeMillis() > buildExt.getQueueTimeMillis());
    Assert.assertEquals("", buildExt.getBuiltOn());
  }

  private BuildResponse getBuilds(Job job, String endpoint) throws IOException, SAXException {
    JenkinsRule.WebClient webClient = jenkinsRule.createWebClient();

    String buildsUrl = job.getUrl() + endpoint;
    Page buildsPage = webClient.goTo(buildsUrl, "application/json");
    String jsonResponse = buildsPage.getWebResponse().getContentAsString();

    JSONReadWrite jsonReadWrite = new JSONReadWrite();

    return jsonReadWrite.fromString(jsonResponse, BuildResponse.class);
  }
}
