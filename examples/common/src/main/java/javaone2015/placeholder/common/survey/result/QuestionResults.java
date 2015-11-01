package javaone2015.placeholder.common.survey.result;

import java.util.List;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class QuestionResults {

    private String id;
    private String text;

    private List<ChoiceResults> choices;

    /**
     * JSON deserialization.
     */
    public QuestionResults() {
    }

    public QuestionResults(final String id, final String text, final List<ChoiceResults> choices) {
        this.id = id;
        this.text = text;
        this.choices = choices;
    }

    public String questionResultId() {
        return id;
    }

    public String text() {
        return text;
    }

    public List<ChoiceResults> choices() {
        return choices;
    }
}
