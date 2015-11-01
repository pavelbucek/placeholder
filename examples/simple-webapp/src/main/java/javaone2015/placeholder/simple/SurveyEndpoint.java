package javaone2015.placeholder.simple;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import javaone2015.placeholder.common.provider.JsonEncoder;
import javaone2015.placeholder.common.survey.AbstractSurveyService;
import javaone2015.placeholder.common.survey.result.SurveyResults;
import javaone2015.placeholder.simple.service.SurveyService;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
@ServerEndpoint(
        value = "/survey/{id}/results",
        encoders = {JsonEncoder.class})
public class SurveyEndpoint {

    private SurveyService surveyService = SurveyService.getInstance();

    @OnOpen
    public void connect(@PathParam("id") final String survey,
                        final Session session) throws IOException, EncodeException {
        session.setMaxIdleTimeout(0);
        surveyService.register(survey, session);

        session.getBasicRemote().sendObject(surveyService.surveyResults(survey));
    }

    @OnClose
    public void disconnect(@PathParam("id") final String survey,
                           final Session session) throws IOException {
        surveyService.unregister(survey, session);
    }

    @OnMessage
    public void action(@PathParam("id") final String survey,
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
