package javaone2015.placeholder.app.client;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

import javaone2015.placeholder.common.provider.JacksonContextResolver;
import javaone2015.placeholder.common.survey.result.SurveyResults;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class SurveyResultDecoder implements Decoder.Text<SurveyResults> {
    private final ObjectMapper context = new JacksonContextResolver().getContext(SurveyResults.class);

    @Override
    public SurveyResults decode(String s) throws DecodeException {

        try {
            return context.readValue(s, SurveyResults.class);
        } catch (IOException e) {
            throw new DecodeException(s, e.getMessage(), e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
