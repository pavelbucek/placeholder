package javaone2015.placeholder.simple.service;

import javaone2015.placeholder.common.survey.AbstractSurveyService;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class SurveyService extends AbstractSurveyService {

    private static final SurveyService INSTANCE = new SurveyService();

    public static SurveyService getInstance() {
        return INSTANCE;
    }

    private SurveyService() {
    }

    @Override
    protected SurveyResultStorage getStorage() {
        return SurveyResultStorage.getInstance();
    }
}
