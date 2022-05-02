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

    public static String getUrl(Job job) {
        return ModelUtil.getFullItemUrl(job.getUrl()) + URL_BASE + "/";
    }

    @ServeJson
    public BuildResponse doIndex(@QueryParameter int start, @QueryParameter int size, @QueryParameter String orderBy) {
        return doBuilds(start, size, orderBy);
    }

    @ServeJson
    public BuildResponse doBuilds(@QueryParameter int start, @QueryParameter int size, @QueryParameter String orderBy) {

        boolean shouldReverse = orderBy == null || orderBy.compareToIgnoreCase("asc") != 0;
        size = size == 0 ? DEFAULT_PAGE_SIZE : size;

        try {
            Job job = getJob();
            RangeSet range = createRangeSet(job, start, size, shouldReverse);
            List<Run> rawBuilds = job.getBuilds(range);

            // In case there were some missing builds in the range set, fill out the rest
            addAdditionalBuilds(job, rawBuilds, size);

            ArrayList<BuildExt> builds = rawBuilds.stream()
                    .map(b -> new BuildExt(b))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            if (shouldReverse) {
                builds.sort((b1, b2) -> Integer.parseInt(b2.getId()) - Integer.parseInt(b1.getId()));
            }
            return new BuildResponse(builds.size(), builds);
        } catch (Exception e) {
            return new BuildResponse(0, new ArrayList<BuildExt>());
        }
    }

    public void addAdditionalBuilds(Job job, List<Run> builds, int size) {
        while (builds.size() < size) {
            int missingBuilds = size - builds.size();
            Run nextRun = builds.get(builds.size() - 1).getNextBuild();

            if (nextRun == null)
                break;

            RangeSet range = RangeSet
                    .fromString(nextRun.getId() + "-" + (Integer.parseInt(nextRun.getId()) + missingBuilds - 1), false);

            builds.addAll(job.getBuilds(range));
        }
    }

    public static RangeSet createRangeSet(Job job, int start, int size, boolean shouldReverse) {
        if (shouldReverse) {
            int lastBuild = Integer.parseInt(job.getLastBuild().getId());
            int defaultStart = Math.max(lastBuild - size + 1, 1);
            start = start == 0 ? defaultStart : Math.max(start - size + 1, 1);
        } else {
            start = start == 0 ? 1 : start;
        }

        int end = start + size - 1;
        return RangeSet.fromString(start + "-" + end, false);
    }
}
