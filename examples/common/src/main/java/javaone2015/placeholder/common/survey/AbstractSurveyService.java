package javaone2015.placeholder.common.survey;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import javaone2015.placeholder.common.survey.model.Survey;
import javaone2015.placeholder.common.survey.result.Submission;
import javaone2015.placeholder.common.survey.result.SurveyResults;

import static java.util.function.Function.identity;
import static javaone2015.placeholder.common.survey.SurveyExamples.surveys;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public abstract class AbstractSurveyService {

    public static final String SELECTED_CHOICE = "selected.choice";

    private final Map<String, Survey> surveys;
    private final ConcurrentMap<String, Set<Session>> sessions;

    public AbstractSurveyService() {
        this.sessions = new ConcurrentHashMap<>();
        this.surveys = surveys().stream().collect(Collectors.toMap(Survey::surveyId, identity()));
    }

    public void register(final String surveyId, final Session session) {
        sessions.computeIfAbsent(surveyId, key -> Collections.newSetFromMap(new ConcurrentHashMap<>()))
                .add(session);
    }

    public void unregister(final String surveyId, final Session session) {
        sessions.computeIfPresent(surveyId, (key, value) -> {
            value.remove(session);
            return value;
        });
    }

    public Survey survey(final String surveyId) {
        return surveys.get(surveyId);
    }

    public SurveyResults surveyResults(final String surveyId) {
        return getStorage().surveyResults(survey(surveyId));
    }

    public SurveyResults choiceResults(final String choiceId) {
        return getStorage().choiceResults(choiceId);
    }

    public void submit(final Submission submission) {
        final Survey survey = submission.survey();
        final String surveyId = survey.surveyId();

        // Process results - aggregate.
        getStorage().apply(submission);

        sessions.getOrDefault(surveyId, Collections.<Session>emptySet())
                .stream()
                .forEach(session -> {
                    try {
                        final Object o = session.getUserProperties().get(SELECTED_CHOICE);
                        if (o != null && !(o.toString()).isEmpty()) {
                            session.getBasicRemote().sendObject(getStorage().choiceResults(o.toString()));
                        } else {
                            session.getBasicRemote().sendObject(getStorage().surveyResults(survey));
                        }
                    } catch (final IOException | EncodeException e) {
                        e.printStackTrace();
                    }
                });
    }

    protected abstract AbstractSurveyResultStorage getStorage();

}
