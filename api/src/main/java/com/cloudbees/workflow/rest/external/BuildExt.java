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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getQueueId() {
        return queueId;
    }

    public void setQueueId(long queueId) {
        this.queueId = queueId;
    }

    public long getQueueTimeMillis() {
        return queueTimeMillis;
    }

    public void setQueueTime(long queueTime) {
        this.queueTimeMillis = queueTime;
    }

    public static BuildExt create(Run run) {

        final BuildExt buildExt = new BuildExt();
        buildExt.setId(run.getId());
        buildExt.setStartTimeMillis(run.getStartTimeInMillis());
        buildExt.setDuration(run.getDuration());
        buildExt.setFullName(run.getFullDisplayName());
        buildExt.setUrl(run.getAbsoluteUrl());
        buildExt.setQueueId(run.getQueueId());
        buildExt.setQueueTime(run.getTimeInMillis());

        Result result = run.getResult();
        buildExt.setResult(result == null ? "null" : result.toString());

        return buildExt;
    }
}