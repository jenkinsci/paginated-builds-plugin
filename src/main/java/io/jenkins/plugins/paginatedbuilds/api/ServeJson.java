package io.jenkins.plugins.paginatedbuilds.api;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest2;
import org.kohsuke.stapler.StaplerResponse2;
import org.kohsuke.stapler.interceptor.Interceptor;
import org.kohsuke.stapler.interceptor.InterceptorAnnotation;

/**
 * Serves the return value of a web method as JSON, using Jackson with its default configuration.
 * <p>
 * Adapted from {@code ServeJson} and {@code JsonResponse} in the pipeline-rest-api module of the
 * <a href="https://github.com/jenkinsci/pipeline-stage-view-plugin">Pipeline Stage View plugin</a>
 * (MIT License, Copyright (c) 2013-2016, CloudBees, Inc.).
 * </p>
 */
@Restricted(NoExternalUse.class)
@Retention(RUNTIME)
@Target(METHOD)
@InterceptorAnnotation(ServeJson.Processor.class)
public @interface ServeJson {

    @Restricted(NoExternalUse.class)
    class Processor extends Interceptor {
        private static final ObjectMapper MAPPER = new ObjectMapper();

        @Override
        public Object invoke(StaplerRequest2 request, StaplerResponse2 response, Object instance, Object[] arguments)
                throws IllegalAccessException, InvocationTargetException {
            try {
                Object pojo = target.invoke(request, response, instance, arguments);
                return new HttpResponse() {
                    @Override
                    public void generateResponse(StaplerRequest2 req, StaplerResponse2 rsp, Object node)
                            throws IOException {
                        rsp.setContentType("application/json; charset=UTF-8");
                        MAPPER.writeValue(rsp.getOutputStream(), pojo);
                    }
                };
            } catch (Exception e) {
                throw new RuntimeException("Unexpected exception while serving JSON", e);
            }
        }
    }
}
