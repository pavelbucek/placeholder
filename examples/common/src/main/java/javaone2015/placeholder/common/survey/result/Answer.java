package javaone2015.placeholder.common.survey.result;

import java.util.Objects;

import javaone2015.placeholder.common.survey.model.Choice;
import javaone2015.placeholder.common.survey.model.Question;
import javaone2015.placeholder.common.survey.model.Survey;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class Answer {

    private Survey survey;
    private Question question;
    private Choice choice;

    public Survey survey() {
        return survey;
    }

    public Answer survey(final Survey survey) {
        this.survey = survey;
        return this;
    }

    public Question question() {
        return question;
    }

    public Answer question(final Question question) {
        this.question = question;
        return this;
    }

    public Choice choice() {
        return choice;
    }

    public Answer choice(final Choice choice) {
        this.choice = choice;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Answer answer = (Answer) o;
        return Objects.equals(question, answer.question) &&
                Objects.equals(choice, answer.choice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, choice);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Answer{");
        sb.append("survey=").append(survey);
        sb.append(", question=").append(question);
        sb.append(", choice=").append(choice);
        sb.append('}');
        return sb.toString();
    }
}
