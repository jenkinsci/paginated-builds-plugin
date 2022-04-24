package com.cloudbees.workflow.rest.external;

import hudson.model.Result;
import hudson.model.Run;

public class BuildExt {

    private String id;
    private long startTimeMillis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public static BuildExt create(Run run) {

        final BuildExt buildExt = new BuildExt();
        buildExt.setId(run.getId());
        buildExt.setStartTimeMillis(run.getStartTimeInMillis());

        return buildExt;
    }
}
