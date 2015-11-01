package javaone2015.placeholder.common.survey.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TODO M: Type?
 *
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class Question {

    private String id;
    private String text;

    private List<Choice> choices;

    public String questionId() {
        return id;
    }

    public Question questionId(final String id) {
        this.id = id;
        return this;
    }

    public String text() {
        return text;
    }

    public Question text(final String text) {
        this.text = text;
        return this;
    }

    public List<Choice> choices() {
        return choices;
    }

    public Question choices(final List<Choice> choices) {
        this.choices = choices;
        return this;
    }

    public Question choice(final Choice choice) {
        if (this.choices == null) {
            this.choices = new ArrayList<>();
        }
        this.choices.add(choice);
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
        final Question question = (Question) o;
        return Objects.equals(id, question.id) &&
                Objects.equals(text, question.text) &&
                Objects.equals(choices, question.choices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, choices);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Question{");
        sb.append("id='").append(id).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", choices=").append(choices);
        sb.append('}');
        return sb.toString();
    }
}
