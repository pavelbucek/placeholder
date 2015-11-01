package javaone2015.placeholder.common.survey;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;

import javaone2015.placeholder.common.survey.model.Choice;
import javaone2015.placeholder.common.survey.model.Survey;
import javaone2015.placeholder.common.survey.result.Answer;
import javaone2015.placeholder.common.survey.result.Submission;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public final class SurveyHelper {

    public static Submission transform(final Survey survey, final Form results) {
        final MultivaluedMap<String, String> map = results.asMap();

        final List<Answer> answers = survey.questions().stream()
                .filter(question -> map.containsKey(question.questionId()))
                .map(question -> {
                    final String actual = map.get(question.questionId()).get(0);
                    final Optional<Choice> found = question.choices().stream()
                            .filter(choice -> choice.choiceId().equals(actual))
                            .findFirst();

                    return new Answer().survey(survey).question(question).choice(found.orElse(new Choice().text(actual)));
                })
                .collect(Collectors.toList());

        return new Submission().survey(survey).answers(answers);
    }

    /**
     * Prevents instantiation.
     */
    private SurveyHelper() {
        throw new AssertionError("No instances allowed.");
    }
}
