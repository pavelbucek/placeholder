package javaone2015.placeholder.app.client;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Form;

import org.glassfish.tyrus.ext.client.java8.SessionBuilder;

import javaone2015.placeholder.common.survey.result.ChoiceResults;
import javaone2015.placeholder.common.survey.result.QuestionResults;
import javaone2015.placeholder.common.survey.result.SurveyResults;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class RandomClientJava8 {

    /**
     * Well.. this class should be renamed ;-)
     */
    private static final Random RANDOM = new Random();

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE =
            Executors.newSingleThreadScheduledExecutor();

    private static final Invocation.Builder REQUEST =
            ClientBuilder.newClient()
                         .target("http://localhost:8080/survey-mixed/javaee")
                         .request();


    /**
     * Main!
     */
    public static void main(String[] args) throws IOException, DeploymentException, InterruptedException {

        ClientEndpointConfig cec = ClientEndpointConfig.Builder
                .create()
                .decoders(Collections.singletonList(SurveyResultDecoder.class))
                .build();

        new SessionBuilder()
                .clientEndpointConfig(cec)
                .uri(URI.create("ws://localhost:8080/survey-mixed/javaee/results"))
                .messageHandler(SurveyResults.class, results -> SCHEDULED_EXECUTOR_SERVICE
                        .schedule(() -> sendRandomData(results), 3, TimeUnit.SECONDS))
                .connect();

        new CountDownLatch(1).await();
    }

    /**
     * @param results just to get the structure.
     */
    static void sendRandomData(SurveyResults results) {

        final Form form = new Form();
        final Form cheat = new Form();

        for (QuestionResults questionResults : results.questions()) {
            final List<ChoiceResults> choices = questionResults.choices();
            final int size = choices.size();

            final int i = RANDOM.nextInt(size);
            form.param(questionResults.questionResultId(), choices.get(i).choiceResultId());

            for (ChoiceResults choice : choices) {
                if (choice.text().contains("WebSocket") ||
                        (questionResults.text().contains("beer") && choice.text().contains("Not!"))) {
                    cheat.param(questionResults.questionResultId(), choice.choiceResultId());
                }
            }
        }

        if (RANDOM.nextInt() % 3 == 0) {
            REQUEST.post(Entity.form(cheat));
        } else {
            REQUEST.post(Entity.form(form));
        }

        System.out.println(":: sent: " + formToString(form));
    }


    static String formToString(Form form) {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (Map.Entry<String, List<String>> e : form.asMap().entrySet()) {
            sb.append(" KEY: ").append(e.getKey()).append(" VALUES:");
            for (String val : e.getValue()) {
                sb.append(" ").append(val);
            }
        }
        sb.append('}');

        return sb.toString();
    }
}
