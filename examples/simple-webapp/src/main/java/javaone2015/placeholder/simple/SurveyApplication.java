package javaone2015.placeholder.simple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.mvc.mustache.MustacheMvcFeature;

import javaone2015.placeholder.common.provider.JacksonContextResolver;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
@ApplicationPath("/")
public class SurveyApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>() {{
            // Resources.
            add(DefaultResource.class);
            add(SurveyResource.class);
            add(SurveyMixed.class);

            // Providers.
            add(MustacheMvcFeature.class);
            add(JacksonFeature.class);
            add(JacksonContextResolver.class);
        }};
    }

    @Override
    public Map<String, Object> getProperties() {
        return new HashMap<String, Object>() {{
            put(MustacheMvcFeature.TEMPLATE_BASE_PATH, "/mustache");
        }};
    }

    @Path("/")
    public static class DefaultResource {

        @GET
        public Response get(@Context final UriInfo uriInfo) {
            return Response
                    .seeOther(uriInfo.getRequestUriBuilder().path(SurveyResource.class).resolveTemplate("surveyResultId", "default").build())
                    .build();
        }
    }
}
