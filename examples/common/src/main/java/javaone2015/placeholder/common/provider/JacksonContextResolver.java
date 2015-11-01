package javaone2015.placeholder.common.provider;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
@Provider
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public JacksonContextResolver() {
        mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        return mapper;
    }
}
