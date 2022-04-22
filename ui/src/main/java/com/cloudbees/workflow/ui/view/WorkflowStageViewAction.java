/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */
package com.cloudbees.workflow.ui.view;

import hudson.Extension;
import hudson.model.Action;
import jenkins.model.TransientActionFactory;
import hudson.model.Job;

import java.util.Collection;
import java.util.Collections;

/**
 * {@link Job} ui extension point {@link Action}.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class WorkflowStageViewAction implements Action {

    public final Job target;

    private WorkflowStageViewAction(Job job) {
        this.target = job;
    }

    @Override
    public String getDisplayName() {
        return Messages.full_stage_view();
    }

    @Override
    public String getUrlName() {
        return "workflow-stage";
    }

    @Override
    public String getIconFileName() {
        return "package.png";
    }

    @Extension
    public static class Factory extends TransientActionFactory<Job> {

        @Override
        public Class<Job> type() {
            return Job.class;
        }

        @Override
        public Collection<? extends Action> createFor(Job target) {
            return Collections.singleton(new WorkflowStageViewAction(target));
        }
    }
}
