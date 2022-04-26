package io.jenkins.plugins.paginatedbuilds.rest.external;

import java.util.List;

public class BuildResponse {
  private int count;
  private List<BuildExt> builds;

  public BuildResponse(){}

  public BuildResponse(int count, List<BuildExt> builds) {
    this.count = count;
    this.builds = builds;
  }

  public int getCount() {
    return count;
  }

  public List<BuildExt> getBuilds() {
    return builds;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public void setBuilds(List<BuildExt> builds) {
    this.builds = builds;
  }
}