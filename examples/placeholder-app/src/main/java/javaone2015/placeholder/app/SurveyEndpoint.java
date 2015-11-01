package javaone2015.placeholder.app;

import java.io.IOException;

import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.glassfish.tyrus.core.TyrusSession;

import javaone2015.placeholder.app.service.SurveyService;
import javaone2015.placeholder.common.provider.JsonEncoder;
import javaone2015.placeholder.common.survey.AbstractSurveyService;
import javaone2015.placeholder.common.survey.result.SurveyResults;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
@ServerEndpoint(
        value = "/survey/{id}/results",
        encoders = {JsonEncoder.class})
public class SurveyEndpoint {

    @Inject
    private SurveyService surveyService;

    @OnOpen
    public void connect(@PathParam("id") final String survey,
                        final TyrusSession session) throws IOException, EncodeException {
        session.setMaxIdleTimeout(0);
        surveyService.register(survey, session);

        session.getBasicRemote().sendObject(surveyService.surveyResults(survey));

        session.processMessages(String.class, stringStream -> stringStream.forEach(message -> {
            SurveyResults surveyResults;
            if (message != null && !message.isEmpty()) {
                session.getUserProperties().put(AbstractSurveyService.SELECTED_CHOICE, message);
                surveyResults = surveyService.choiceResults(message);
            } else {
                session.getUserProperties().put(AbstractSurveyService.SELECTED_CHOICE, null);
                surveyResults = surveyService.surveyResults(survey);
            }

            try {
                session.getBasicRemote().sendObject(surveyResults);
            } catch (IOException | EncodeException e) {
                // handle exception.
            }
        }));
    }

    @OnClose
    public void disconnect(@PathParam("id") final String survey,
                           final Session session) throws IOException {
        surveyService.unregister(survey, session);
    }
}
