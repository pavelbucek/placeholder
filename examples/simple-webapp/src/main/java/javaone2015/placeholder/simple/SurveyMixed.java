package javaone2015.placeholder.simple;

import java.io.IOException;

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

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.glassfish.jersey.server.mvc.Template;

import javaone2015.placeholder.common.provider.JsonEncoder;
import javaone2015.placeholder.common.survey.AbstractSurveyService;
import javaone2015.placeholder.common.survey.model.Survey;
import javaone2015.placeholder.common.survey.result.SurveyResults;
import javaone2015.placeholder.simple.service.SurveyService;

import static java.lang.String.format;
import static javaone2015.placeholder.common.survey.SurveyHelper.transform;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
@Path("/survey-mixed/{id}")
@Produces("text/html")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@ServerEndpoint(
        value = "/survey-mixed/{id}/results",
        encoders = {JsonEncoder.class})
public class SurveyMixed {

    private SurveyService surveyService = SurveyService.getInstance();

    @GET
    @Template(name = "/survey-form")
    public Survey questionnaire(@PathParam("id") final String id) {
        return surveyService.survey(id);
    }

    @GET
    @Path("results")
    @Template(name = "/survey-results")
    public SurveyResults results(@PathParam("id") final String id,
                                 @QueryParam("name") final String name,
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
    public Response submit(@PathParam("id") final String id,
                           @FormParam("name") final String name,
                           final Form results,
                           @Context final UriInfo uriInfo) {
        surveyService.submit(transform(surveyService.survey(id), results));

        final UriBuilder redirect = uriInfo.getRequestUriBuilder().path("results");
        return Response
                .seeOther(name != null && !"".equals(name) ? redirect.queryParam("name", name).build() : redirect.build())
                .build();
    }

    @OnOpen
    public void connect(@javax.websocket.server.PathParam("id") final String survey,
                        final Session session) throws IOException, EncodeException {
        session.setMaxIdleTimeout(0);
        surveyService.register(survey, session);

        session.getBasicRemote().sendObject(surveyService.surveyResults(survey));
    }

    @OnClose
    public void disconnect(@javax.websocket.server.PathParam("id") final String survey,
                           final Session session) throws IOException {
        surveyService.unregister(survey, session);
    }

    @OnMessage
    public void action(@javax.websocket.server.PathParam("id") final String survey,
                       final Session session,
                       final String message) throws IOException, EncodeException {

        SurveyResults surveyResults;
        if (message != null && !message.isEmpty()) {
            session.getUserProperties().put(AbstractSurveyService.SELECTED_CHOICE, message);
            surveyResults = surveyService.choiceResults(message);
        } else {
            session.getUserProperties().put(AbstractSurveyService.SELECTED_CHOICE, null);
            surveyResults = surveyService.surveyResults(survey);
        }

        session.getBasicRemote().sendObject(surveyResults);
    }
}
