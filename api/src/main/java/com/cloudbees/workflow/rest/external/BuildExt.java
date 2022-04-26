package com.cloudbees.workflow.rest.external;

import hudson.model.Result;
import hudson.model.Run;
import hudson.model.Build;

public class BuildExt {

    private String id;
    private long queueTimeMillis;
    private long startTimeMillis;
    private long duration;
    private String fullName;
    private String url;
    private String result;
    private String builtOn;

    public BuildExt(Run<?, ?> run) {
        this.id = run.getId();
        this.queueTimeMillis = run.getTimeInMillis();
        this.startTimeMillis = run.getStartTimeInMillis();
        this.duration = run.getDuration();
        this.fullName = run.getFullDisplayName();
        this.url = run.getUrl();
        this.builtOn = ((Build) run).getBuiltOnStr();

        Result result = run.getResult();
        this.result = result == null ? "null" : result.toString();
    }

    public String getId() {
        return id;
    }
    
    public long getQueueTimeMillis() {
        return queueTimeMillis;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public long getDuration() {
        return duration;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUrl() {
        return url;
    }

    public String getResult() {
        return result;
    }

    public String getBuiltOn() {
        return builtOn;
    }
}
