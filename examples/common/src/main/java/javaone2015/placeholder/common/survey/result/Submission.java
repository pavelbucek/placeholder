package javaone2015.placeholder.common.survey.result;

import java.util.List;
import java.util.Objects;

import javaone2015.placeholder.common.survey.model.Survey;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class Submission {

    private Survey survey;
    private List<Answer> answers;

    public Survey survey() {
        return survey;
    }

    public Submission survey(final Survey survey) {
        this.survey = survey;
        return this;
    }

    public List<Answer> answers() {
        return answers;
    }

    public Submission answers(final List<Answer> answers) {
        this.answers = answers;
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
        final Submission that = (Submission) o;
        return Objects.equals(survey, that.survey) &&
                Objects.equals(answers, that.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(survey, answers);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Submission{");
        sb.append("survey=").append(survey);
        sb.append(", answers=").append(answers);
        sb.append('}');
        return sb.toString();
    }
}
