package javaone2015.placeholder.common.survey.result;

import java.util.List;
import java.util.Objects;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class SurveyResults {

    private String id;
    private String name;

    private List<QuestionResults> questions;

    private String message;

    /**
     * JSON deserialization.
     */
    public SurveyResults() {
    }

    public SurveyResults(final String id, final String name, final List<QuestionResults> questions) {
        this.id = id;
        this.name = name;
        this.questions = questions;
    }

    public String surveyResultId() {
        return id;
    }

    public String name() {
        return name;
    }

    public List<QuestionResults> questions() {
        return questions;
    }

    public String message() {
        return message;
    }

    public SurveyResults message(final String message) {
        this.message = message;
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
        final SurveyResults that = (SurveyResults) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(questions, that.questions) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, questions, message);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SurveyResults{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", questions=").append(questions);
        sb.append(", message=").append(message);
        sb.append('}');
        return sb.toString();
    }
}
