package io.jenkins.plugins.paginatedbuilds.rest.external;

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

    public BuildExt() {}

    public BuildExt(Run<?, ?> run) {
        this.id = run.getId();
        this.queueTimeMillis = run.getTimeInMillis();
        this.startTimeMillis = run.getStartTimeInMillis();
        this.duration = run.getDuration();
        this.fullName = run.getFullDisplayName();
        this.url = run.getUrl();
        if (run instanceof Build) {
            this.builtOn = ((Build) run).getBuiltOnStr();
        } else {
            this.builtOn = "";
        }

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

    public void setId(String id) {
        this.id = id;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setQueueTimeMillis(long queueTimeMillis) {
        this.queueTimeMillis = queueTimeMillis;
    }

    public void setBuiltOn(String builtOn) {
        this.builtOn = builtOn;
    }
}
