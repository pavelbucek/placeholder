package javaone2015.placeholder.app.service;

import javax.inject.Inject;

import javaone2015.placeholder.common.survey.AbstractSurveyResultStorage;
import javaone2015.placeholder.common.survey.AbstractSurveyService;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class SurveyService extends AbstractSurveyService {

    @Inject
    private SurveyResultStorage storage;

    @Override
    protected AbstractSurveyResultStorage getStorage() {
        return storage;
    }
}
