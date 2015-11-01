package javaone2015.placeholder.common.survey;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javaone2015.placeholder.common.survey.model.Choice;
import javaone2015.placeholder.common.survey.model.Question;
import javaone2015.placeholder.common.survey.model.Survey;
import javaone2015.placeholder.common.survey.result.ChoiceResults;
import javaone2015.placeholder.common.survey.result.QuestionResults;
import javaone2015.placeholder.common.survey.result.Submission;
import javaone2015.placeholder.common.survey.result.SurveyResults;

import static javaone2015.placeholder.common.survey.SurveyExamples.surveys;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public abstract class AbstractSurveyResultStorage {

    private final Map<Survey, Map<Question, Map<Choice, AtomicInteger>>> surveyResults;

    private final Map<String, Survey> choiceToSurvey;
    private final Map<String, Map<Question, Map<Choice, AtomicInteger>>> choiceResults;

    public AbstractSurveyResultStorage() {
        surveyResults = surveys().stream()
                .collect(LinkedHashMap::new,
                        (surveys, survey) -> surveys.put(survey, survey.questions().stream()
                                .collect(LinkedHashMap::new,
                                        (questions, question) -> questions.put(question, question.choices().stream()
                                                .collect(LinkedHashMap::new,
                                                        (choices, choice) -> choices.put(choice, new AtomicInteger(0)),
                                                        Map::putAll)),
                                        Map::putAll)),
                        Map::putAll);

        choiceToSurvey = surveys().stream()
                .collect(HashMap::new,
                        (map, survey) -> survey.questions().stream()
                                .flatMap(question -> question.choices().stream())
                                .forEach(choice -> map.put(choice.choiceId(), survey)),
                        Map::putAll);

        choiceResults = surveys().stream()
                .collect(HashMap::new,
                        (map, survey) -> survey.questions().stream()
                                .flatMap(question -> question.choices().stream())
                                .forEach(choice -> map.put(choice.choiceId(), survey.questions().stream()
                                        .collect(LinkedHashMap::new,
                                                (questions, question) -> questions.put(question, question.choices().stream()
                                                        .collect(LinkedHashMap::new,
                                                                (choices, current) -> choices.put(current, new AtomicInteger(0)),
                                                                Map::putAll)),
                                                Map::putAll))),
                        Map::putAll);
    }

    public void apply(final Submission submission) {
        final Map<Question, Map<Choice, AtomicInteger>> results = surveyResults.get(submission.survey());

        submission.answers().forEach(answer -> results
                .computeIfAbsent(answer.question(), key -> new LinkedHashMap<>())
                .computeIfAbsent(answer.choice(), key -> new AtomicInteger(0))
                .incrementAndGet());

        submission.answers()
                .forEach(answer -> {
                    final Choice choice = answer.choice();
                    if (choice.choiceId() == null) {
                        choice.choiceId(choice.text());
                    }

                    choiceToSurvey.putIfAbsent(choice.choiceId(), answer.survey());

                    final Map<Question, Map<Choice, AtomicInteger>> choiceResult = choiceResults
                            .computeIfAbsent(choice.choiceId(), key -> new LinkedHashMap<>());

                    submission.answers().forEach(answer1 -> choiceResult
                            .computeIfAbsent(answer1.question(), key -> new LinkedHashMap<>())
                            .computeIfAbsent(answer1.choice(), key -> new AtomicInteger(0))
                            .incrementAndGet());
                });
    }

    public SurveyResults surveyResults(final Survey survey) {
        return new SurveyResults(survey.surveyId(), survey.name(), questions(surveyResults.get(survey)));
    }

    private List<QuestionResults> questions(final Map<Question, Map<Choice, AtomicInteger>> results) {
        return results.entrySet().stream()
                .map(questionEntry -> {
                    final Question question = questionEntry.getKey();

                    final int total = questionEntry.getValue().values().stream()
                            .map(AtomicInteger::get)
                            .reduce((left, right) -> left + right)
                            .orElse(1);

                    final List<ChoiceResults> choices = questionEntry.getValue().entrySet().stream()
                            .map(choiceEntry -> new ChoiceResults(choiceEntry.getKey().choiceId(), choiceEntry.getKey().text(),
                                    (100 * choiceEntry.getValue().get() / (double) total)))
                            .collect(Collectors.toList());

                    return new QuestionResults(question.questionId(), question.text(), choices);
                })
                .collect(Collectors.toList());
    }

    public SurveyResults choiceResults(final String choiceId) {
        final Survey survey = choiceToSurvey.get(choiceId);
        return new SurveyResults(survey.surveyId(), survey.name(), questions(choiceResults.get(choiceId)));
    }
}
