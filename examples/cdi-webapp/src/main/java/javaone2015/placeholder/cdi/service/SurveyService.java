package javaone2015.placeholder.cdi.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import javaone2015.placeholder.common.survey.AbstractSurveyService;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
@Singleton
public class SurveyService extends AbstractSurveyService {

    @Inject
    private SurveyResultStorage storage;

    @Override
    protected SurveyResultStorage getStorage() {
        return storage;
    }
}
