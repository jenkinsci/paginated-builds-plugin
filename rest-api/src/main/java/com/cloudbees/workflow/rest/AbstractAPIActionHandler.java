/*
 * The MIT License
 *
 * Copyright (c) 2013-2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cloudbees.workflow.rest;

import hudson.model.Action;
import hudson.model.Job;
import jenkins.model.TransientActionFactory;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Collection;
import java.util.Collections;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public abstract class AbstractAPIActionHandler extends TransientActionFactory<Job> implements Action {

    public static final String URL_BASE = "valet";

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
            throw new IllegalStateException(String.format("Workflow API Action class '%s' does not implement a public default constructor.", getClass().getName()));
        }
    }
}
