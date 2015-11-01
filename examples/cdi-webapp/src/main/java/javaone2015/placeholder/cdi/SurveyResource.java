package javaone2015.placeholder.cdi;

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

import javaone2015.placeholder.cdi.service.SurveyService;
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
public class SurveyResource {

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
    public SurveyResults results(@QueryParam("name") final String name,
                                 @QueryParam("thank") final String thank) {
        String message = "Thank you!";
        if (name != null) {
            message = format("Thank you, %s, for participating in this survey.", name);
        } else if (thank != null) {
            message = thank;
        }

        return surveyService.surveyResults(id).message(message);
    }

    @POST
    public Response submit(@FormParam("name") final String name,
                           final Form results) {
        surveyService.submit(transform(surveyService.survey(id), results));

        final UriBuilder redirect = uriInfo.getRequestUriBuilder().path("results");
        return Response
                .seeOther(name != null && !"".equals(name) ? redirect.queryParam("name", name).build() : redirect.build())
                .build();
    }
}
