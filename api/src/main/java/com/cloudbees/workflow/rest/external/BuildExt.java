package com.cloudbees.workflow.rest.external;

import hudson.model.Result;
import hudson.model.Run;

public class BuildExt {

    private String id;
    private long startTimeMillis;
    private long duration;
    private String fullName;
    private String url;
    private String result;
    private long queueId;
    private long queueTimeMillis;

    public BuildExt(Run<?, ?> run) {
        this.id = run.getId();
        this.startTimeMillis = run.getStartTimeInMillis();
        this.duration = run.getDuration();
        this.fullName = run.getFullDisplayName();
        this.url = run.getUrl();
        this.queueId = run.getQueueId();
        this.queueTimeMillis = run.getTimeInMillis();
        
        Result result = run.getResult();
        this.result = result == null ? "null" : result.toString();
    }

    public String getId() {
        return id;
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

    public long getQueueId() {
        return queueId;
    }

    public long getQueueTimeMillis() {
        return queueTimeMillis;
    }
}
