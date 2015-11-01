# Placeholder

**This "framework" is just a PoC**

Slides: [how-to-thrive-on-restwebsocketbased-microservices](http://www.slideshare.net/pavelbucek/how-to-thrive-on-restwebsocketbased-microservices)

One of the goal was to better integrate Jersey (JAX-RS RI) and Tyrus (WebSocket API for Java RI)
into one small (minimal) and fast framework, which provides the right (again, minimal) amount
of functionality.

# How to build

You'll need **Maven 3.3.3+** and **JDK 1.8+**.

We made some modifications in Jersey and Tyrus, so you'll need to check them out and build them:

```
git clone git@github.com:pavelbucek/jersey-j1.git
cd jersey-j1
mvn clean install -Dmaven.test.skip
cd ..
```

```
git clone git clone git@github.com:pavelbucek/tyrus-j1.git
cd tyrus-j1
mvn clean install -Dmaven.test.skip
cd ..
```

Checkout Placeholder + build:

`git clone git clone git@github.com:pavelbucek/placeholder.git`
`cd placeholder`
`mvn clean install -Dmaven.test.skip`

# Demos

Assumption here is that you have already built the project.

## simple-webapp

- simple functionality, just to show trivial solution (Java SE, no injection)

```
cd placeholder/examples/simple-webapp/
mvn jetty:run
```

- app runs on: `http://localhost:8080/survey/javaee/`

## cdi-webapp

- same app, same functionality

File `placeholder/examples/cdi-webapp/target/cdi-webapp.war` can be deployed on any Java EE 7 compatible
appserver; usually available on something like: `http://localhost:8080/cdi-webapp/survey/javaee/`

- jetty version:

```
cd placeholder/examples/cdi-webapp/
mvn jetty:run -Pstandalone
```

- app url: `http://localhost:8080/survey/javaee/`

## placeholder

- same functionality as previous, minimal runtime, significantly faster startup, smaller footprint.

`java -jar placeholder/examples/placeholder-app/target/placeholder-app-0.1.0-SNAPSHOT.jar`

- app url: `http://localhost:8080/survey/javaee/`
