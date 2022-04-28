package io.jenkins.plugins.paginatedbuilds.api;

import java.util.ArrayList;
import java.util.List;

import com.cloudbees.workflow.util.ModelUtil;
import com.cloudbees.workflow.util.ServeJson;

import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.Fingerprint.RangeSet;
import io.jenkins.plugins.paginatedbuilds.model.BuildExt;
import io.jenkins.plugins.paginatedbuilds.model.BuildResponse;

/**
 * API Action handler to return Job info.
 * <p>
 * Bound to {@code ${{rootURL}/job/<jobname>/builds/*}}
 * </p>
 *
 */
@Extension
public class BuildAPI extends AbstractAPIActionHandler {
    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final int DEFAULT_START = 1;

    public static String getUrl(Job job) {
        return ModelUtil.getFullItemUrl(job.getUrl()) + URL_BASE + "/";
    }

    @ServeJson
    public BuildResponse doIndex(@QueryParameter int start, @QueryParameter int size) {
        return doBuilds(start, size);
    }

    @ServeJson
    public BuildResponse doBuilds(@QueryParameter int start, @QueryParameter int size) {
        size = size == 0 ? DEFAULT_PAGE_SIZE : size;
        start = start == 0 ? DEFAULT_START : start;

        Job job = getJob();
        RangeSet range = RangeSet.fromString(start + "-" + (size + start - 1), false);
        List<Run> rawBuilds = job.getBuilds(range);

        // In case there were some missing builds in the range set, fill out the rest
        rawBuilds = getAdditionalBuilds(job, rawBuilds, size);

        ArrayList<BuildExt> builds = rawBuilds.stream()
                .map(b -> new BuildExt(b))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        return new BuildResponse(builds.size(), builds);
    }

    public List<Run> getAdditionalBuilds(Job job, List<Run> builds, int size) {
        while (builds.size() < size) {
            int missingBuilds = size - builds.size();
            Run nextRun = builds.get(builds.size() - 1).getNextBuild();

            if (nextRun == null)
                break;

            RangeSet range = RangeSet
                    .fromString(nextRun.getId() + "-" + (Integer.parseInt(nextRun.getId()) + missingBuilds - 1), false);
            builds.addAll(job.getBuilds(range));
        }
        return builds;
    }
}
