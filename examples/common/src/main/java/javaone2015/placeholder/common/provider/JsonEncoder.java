package javaone2015.placeholder.common.provider;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class JsonEncoder implements Encoder.Text<Object> {

    @Override
    public String encode(final Object entity) throws EncodeException {
        try {
            // TODO M: bind object mapper.
            return new JacksonContextResolver().getContext(Object.class).writeValueAsString(entity);
        } catch (final JsonProcessingException jpe) {
            throw new EncodeException(entity, jpe.getMessage(), jpe);
        }
    }

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}
