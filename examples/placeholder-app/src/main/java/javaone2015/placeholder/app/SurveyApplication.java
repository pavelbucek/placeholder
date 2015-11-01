package javaone2015.placeholder.app;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.mustache.MustacheMvcFeature;

import javaone2015.placeholder.common.provider.JacksonContextResolver;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
@ApplicationPath("/")
public class SurveyApplication extends ResourceConfig {

    public SurveyApplication() {
        // Resources.
        register(DefaultResource.class);
        register(SurveyResource.class);
        register(SurveyMixed.class);

        // Providers.
        register(MustacheMvcFeature.class);
        register(JacksonFeature.class);
        register(JacksonContextResolver.class);

        property(MustacheMvcFeature.TEMPLATE_BASE_PATH, "/mustache");
    }

    @Path("/")
    public static class DefaultResource {

        @GET
        public Response get(@Context final UriInfo uriInfo) {
            return Response
                    .seeOther(uriInfo.getRequestUriBuilder()
                            .path(SurveyResource.class)
                            .resolveTemplate("surveyResultId", "default")
                            .build())
                    .build();
        }
    }
}
