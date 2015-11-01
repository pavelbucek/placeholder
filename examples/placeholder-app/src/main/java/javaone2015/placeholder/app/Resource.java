package javaone2015.placeholder.app;

import java.time.LocalTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public interface Resource {

    @GET
    @Path("ping")
    @Produces("text/plain")
    default String ping() {
        return "Pong at " + LocalTime.now();
    }
}
