package io.jenkins.plugins.paginatedbuilds.api;

import hudson.model.Action;
import hudson.model.Job;
import jenkins.model.TransientActionFactory;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Collection;
import java.util.Collections;


public abstract class AbstractAPIActionHandler extends TransientActionFactory<Job> implements Action {

    public static final String URL_BASE = "builds";

    public Job target;

    @Override
    public String getUrlName() {
        return URL_BASE;
    }

    @Override
    public String getIconFileName() {
        // No display
        return null;
    }

    @Override
    public String getDisplayName() {
        // No display
        return null;
    }

    @Override
    public Class<Job> type() {
        return Job.class;
    }

    protected Job getJob() {
        return target;
    }

    @NonNull
    @Override
    public Collection<? extends Action> createFor(@NonNull Job target) {
        try {
            AbstractAPIActionHandler instance = getClass().newInstance();
            instance.target = target;
            return Collections.singleton(instance);
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Paginated Builds API Action class '%s' does not implement a public default constructor.", getClass().getName()));
        }
    }
}
