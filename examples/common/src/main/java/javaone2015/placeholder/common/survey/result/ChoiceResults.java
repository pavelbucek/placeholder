package javaone2015.placeholder.common.survey.result;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class ChoiceResults {

    private String id;
    private String text;

    private double percentage;

    /**
     * JSON deserialization.
     */
    public ChoiceResults() {
    }

    public ChoiceResults(final String id, final String text, final double percentage) {
        this.id = id;
        this.text = text;
        this.percentage = percentage;
    }

    public String choiceResultId() {
        return id;
    }

    public String text() {
        return text;
    }

    public double percentage() {
        return percentage;
    }
}
