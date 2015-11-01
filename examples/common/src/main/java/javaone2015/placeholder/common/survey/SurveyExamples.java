package javaone2015.placeholder.common.survey;

import java.util.Collections;
import java.util.List;

import javaone2015.placeholder.common.survey.model.Choice;
import javaone2015.placeholder.common.survey.model.Question;
import javaone2015.placeholder.common.survey.model.Survey;

import static java.util.UUID.randomUUID;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public final class SurveyExamples {

    private static final List<Survey> SURVEYS = Collections.singletonList(javaEeSurvey());

    private static Survey javaEeSurvey() {
        return new Survey().surveyId("javaee").name("Few Questions about Java EE")
                .question(new Question().questionId("like").text("Do you like Java EE?")
                        .choice(new Choice().choiceId(randomUUID().toString()).text("Yes, mainly JAX-RS"))
                        .choice(new Choice().choiceId(randomUUID().toString()).text("Yes, mainly WebSocket"))
                        .choice(new Choice().choiceId(randomUUID().toString()).text("Yes"))
                        .choice(new Choice().choiceId(randomUUID().toString()).text("No"))
                        .choice(new Choice().choiceId(randomUUID().toString()).text("No, I prefer Spring"))
                        .choice(new Choice().choiceId(randomUUID().toString()).text("What's Java EE?"))
                        .choice(new Choice().choiceId(randomUUID().toString()).text("What's Java?")))
                .question(new Question().questionId("beer").text("Enough beer?")
                        .choice(new Choice().choiceId(randomUUID().toString()).text("No"))
                        .choice(new Choice().choiceId(randomUUID().toString()).text("No!"))
                        .choice(new Choice().choiceId(randomUUID().toString()).text("Definitely Not!")))
                .question(new Question().questionId("yes").text("Yes?")
                        .choice(new Choice().choiceId(randomUUID().toString()).text("What?"))
                        .choice(new Choice().choiceId(randomUUID().toString()).text("No")));
    }

    public static List<Survey> surveys() {
        return SURVEYS;
    }

    /**
     * Prevents instantiation.
     */
    private SurveyExamples() {
        throw new AssertionError("No instances allowed.");
    }
}
