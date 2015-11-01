package javaone2015.runtime;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Application;

import javax.websocket.DeploymentException;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainerProvider;
import org.glassfish.jersey.java8.Java8TypesFeature;
import org.glassfish.jersey.server.ResourceConfig;

import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.Populator;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandlerRegistration;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.tyrus.container.grizzly.server.GrizzlyServerContainer;
import org.glassfish.tyrus.core.TyrusWebSocketEngine;
import org.glassfish.tyrus.spi.ServerContainer;
import org.glassfish.tyrus.spi.WebSocketEngine;

import static java.lang.String.format;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class Placeholder {

    private final HttpServer httpServer;

    private Placeholder(final HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    public Placeholder start() {
        if (httpServer.isStarted()) {
            throw new UnsupportedOperationException("Server already started.");
        }

        try {
            httpServer.start();
        } catch (final IOException ioe) {
            throw new PlaceholderException("Cannot start Ohpas server.", ioe);
        }

        try {
            Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
            Thread.currentThread().join();
        } catch (final InterruptedException ie) {
            throw new PlaceholderException(ie);
        }

        return this;
    }

    public Placeholder stop() {
        httpServer.shutdownNow();
        return this;
    }

    public static Builder builder() {
        return new Builder("http://localhost:8080");
    }

    public static Builder builder(final String uri) {
        return new Builder(uri);
    }

    public static Builder builder(final URI uri) {
        return new Builder(uri);
    }

    public static final class Builder {

        private static final Logger LOGGER = Logger.getLogger(Builder.class.getName());

        private final URI uri;

        private final List<Binder> binders = new ArrayList<>();

        private final Map<Class<?>, String> endpoints = new HashMap<>();

        private String jerseyContext;
        private ResourceConfig resourceConfig;

        public Builder(final URI uri) {
            this.uri = uri;
        }

        public Builder(final String uri) {
            this(URI.create(uri));
        }

        public Builder jaxRsApplication(final String context, final Class<? extends Application> application) {
            this.jerseyContext = context;
            this.resourceConfig = ResourceConfig.forApplicationClass(application);
            return this;
        }

        public Builder jaxRsApplication(final String context, final Application application) {
            this.jerseyContext = context;
            this.resourceConfig = ResourceConfig.forApplication(application);
            return this;
        }

        public Builder jaxRsApplication(final Class<? extends Application> application) {
            return jaxRsApplication("", application);
        }

        public Builder jaxRsApplication(final Application application) {
            return jaxRsApplication("", application);
        }

        public Builder wsEndpoint(final Class<?> endpoint) {
            return wsEndpoint("", endpoint);
        }

        public Builder wsEndpoint(final String context, final Class<?> endpoint) {
            this.endpoints.put(endpoint, context);
            return this;
        }

        public Builder wsEndpoint(final Class<?>... endpoints) {
            return wsEndpoint("", endpoints);
        }

        public Builder wsEndpoint(final String context, final Class<?>... endpoints) {
            Arrays.asList(endpoints).stream()
                    .forEach(endpoint -> wsEndpoint(context, endpoint));
            return this;
        }

        public Builder binder(final AbstractBinder binder) {
            binders.add(binder);
            return this;
        }

        public Placeholder build(final boolean start) {
            final HttpServer httpServer = new HttpServer();

            // Listener.
            httpServer.addListener(new NetworkListener("grizzly",
                    uri.getHost(),
                    uri.getPort()));

            final ServerConfiguration serverConfig = httpServer.getServerConfiguration();

            // Static content.
            final CLStaticHttpHandler handler = new CLStaticHttpHandler(getClass().getClassLoader(), "/META-INF/web/");
            handler.setFileCacheEnabled(true);
            serverConfig.addHttpHandler(handler, HttpHandlerRegistration.builder()
                    .contextPath("/static")
                    .urlPattern("/*")
                    .build());

            // Binders.
            binders.add(0, new GrizzlyHttpContainer.GrizzlyBinder());

            // Custom providers.
            resourceConfig.register(Java8TypesFeature.class);

            // Jersey.
            final GrizzlyHttpContainer jerseyContainer = new GrizzlyHttpContainerProvider().createContainer(
                    GrizzlyHttpContainer.class,
                    resourceConfig,
                    binders.toArray(new Binder[binders.size()]));

            serverConfig.addHttpHandler(jerseyContainer, HttpHandlerRegistration.builder()
                    .contextPath(slash(jerseyContext))
                    .build());

            // Automatic bindings.
            // TODO: We need to pass our locator to Jersey/Tyrus.
            final ServiceLocator locator = jerseyContainer.getApplicationHandler().getServiceLocator();
            populateLocator(locator);

            // Tyrus.
            final Map<String, Object> tyrusProperties = new HashMap<>(1);
            tyrusProperties.put(TyrusWebSocketEngine.SERVICE_LOCATOR, locator);

            final ServerContainer tyrusContainer = new GrizzlyServerContainer().createContainer(httpServer, tyrusProperties);

            try {
                // TODO: ???
                tyrusContainer.start("", uri.getPort());

                final WebSocketEngine wsEngine = tyrusContainer.getWebSocketEngine();

                endpoints.entrySet().stream()
                        .forEach(entry -> registerEndpoint(wsEngine, entry.getKey(), entry.getValue()));

            } catch (IOException | DeploymentException e) {
                LOGGER.log(Level.SEVERE, "Cannot start WebSocket.", e);
            }

            // Create.
            final Placeholder PH = new Placeholder(httpServer);
            return start ? PH.start() : PH;
        }

        private void populateLocator(final ServiceLocator locator) {
            try {
                final DynamicConfigurationService dcs = locator.getService(DynamicConfigurationService.class);
                final Populator populator = dcs.getPopulator();

                populator.populate(new ClasspathDescriptorFileFinder());
            } catch (final IOException e) {
                throw new PlaceholderException(e);
            }
        }

        private void registerEndpoint(final WebSocketEngine engine, final Class<?> endpoint, final String context) {
            try {
                engine.register(endpoint, slash(context));
            } catch (final DeploymentException e) {
                throw new PlaceholderException(format("Cannot register endpoint '%s' (context='%s').", endpoint, context), e);
            }
        }

        private String slash(final String path) {
            return "".equals(path) || path.startsWith("/") ? path : "/" + path;
        }
    }
}
