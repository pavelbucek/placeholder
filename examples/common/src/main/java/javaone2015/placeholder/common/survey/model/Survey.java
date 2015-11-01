package javaone2015.placeholder.common.survey.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class Survey {

    private String id;
    private String name;

    private List<Question> questions;

    public String surveyId() {
        return id;
    }

    public Survey surveyId(final String id) {
        this.id = id;
        return this;
    }

    public String name() {
        return name;
    }

    public Survey name(final String name) {
        this.name = name;
        return this;
    }

    public List<Question> questions() {
        return questions;
    }

    public Survey questions(final List<Question> questions) {
        this.questions = questions;
        return this;
    }

    public Survey question(final Question question) {
        if (this.questions == null) {
            this.questions = new ArrayList<>();
        }
        this.questions.add(question);
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
        final Survey survey = (Survey) o;
        return Objects.equals(id, survey.id) &&
                Objects.equals(name, survey.name) &&
                Objects.equals(questions, survey.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, questions);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Survey{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", questions=").append(questions);
        sb.append('}');
        return sb.toString();
    }
}
