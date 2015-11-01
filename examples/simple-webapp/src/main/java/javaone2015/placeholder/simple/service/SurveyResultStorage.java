package javaone2015.placeholder.simple.service;

import javaone2015.placeholder.common.survey.AbstractSurveyResultStorage;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class SurveyResultStorage extends AbstractSurveyResultStorage {

    private static final SurveyResultStorage INSTANCE = new SurveyResultStorage();

    public static SurveyResultStorage getInstance() {
        return INSTANCE;
    }

    private SurveyResultStorage() {
    }
}
