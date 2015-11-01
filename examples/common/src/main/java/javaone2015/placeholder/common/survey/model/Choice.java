package javaone2015.placeholder.common.survey.model;

import java.util.Objects;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class Choice {

    private String id;
    private String text;

    public String choiceId() {
        return id;
    }

    public Choice choiceId(final String id) {
        this.id = id;
        return this;
    }

    public String text() {
        return text;
    }

    public Choice text(final String text) {
        this.text = text;
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
        final Choice choice = (Choice) o;
        return Objects.equals(id, choice.id)
                && Objects.equals(text, choice.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Choice{");
        sb.append("id='").append(id).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
