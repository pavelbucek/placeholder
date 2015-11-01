package javaone2015.placeholder.app;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import javax.inject.Inject;

import org.glassfish.jersey.server.mvc.Template;

import javaone2015.placeholder.app.service.SurveyService;
import javaone2015.placeholder.common.survey.model.Survey;
import javaone2015.placeholder.common.survey.result.SurveyResults;

import static java.lang.String.format;
import static javaone2015.placeholder.common.survey.SurveyHelper.transform;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
@Path("/survey/{id}")
@Produces("text/html")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class SurveyResource implements Resource {

    @PathParam("id")
    private String id;

    @Context
    private UriInfo uriInfo;

    @Inject
    private SurveyService surveyService;

    @GET
    @Template(name = "/survey-form")
    public Survey questionnaire() {
        return surveyService.survey(id);
    }

    @GET
    @Path("results")
    @Template(name = "/survey-results")
    public SurveyResults results(@QueryParam("name") final Optional<String> name,
                                 @QueryParam("thank") final Optional<String> thank) {
        final String message = name.map(str -> format("Thank you, %s, for participating in this survey.", str))
                .map(Optional::ofNullable)
                .orElseGet(() -> thank)
                .orElse("Thank you!");

        return surveyService.surveyResults(id)
                .message(message);
    }

    @POST
    public Response submit(@FormParam("name") final Optional<String> name,
                           final Form results) {
        surveyService.submit(transform(surveyService.survey(id), results));

        final UriBuilder redirect = uriInfo.getRequestUriBuilder().path("results");

        return Response
                .seeOther(name.map(str -> redirect.queryParam("name", str).build()).orElse(redirect.build()))
                .build();
    }
}
