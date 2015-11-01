package javaone2015.placeholder.app;

import javax.inject.Singleton;

import org.glassfish.jersey.server.ResourceConfig;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javaone2015.placeholder.app.service.SurveyResultStorage;
import javaone2015.placeholder.app.service.SurveyService;
import javaone2015.runtime.Placeholder;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class Main {

    public static void main(String[] args) {
        Placeholder.builder()
                .jaxRsApplication(
                        ResourceConfig.forApplicationClass(
                                SurveyApplication.class))
                .wsEndpoint(SurveyEndpoint.class, SurveyMixed.class)
                .binder(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bindAsContract(SurveyService.class)
                                .in(Singleton.class);
                        bindAsContract(SurveyResultStorage.class)
                                .in(Singleton.class);
                    }
                })
                .build(true);
    }
}
