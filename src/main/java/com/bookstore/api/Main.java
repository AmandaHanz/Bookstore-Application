package com.bookstore.api;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static final String BASE_URI = "http://localhost:8080/api/";

    public static HttpServer startServer() {
        // Configure JAX-RS application with package scanning and Jackson
        final ResourceConfig config = new ResourceConfig()
                .packages("com.bookstore.api.resource", "com.bookstore.api.exception.mapper")
                .register(JacksonFeature.class); // Enable JSON serialization
        LOGGER.debug("ResourceConfig initialized with packages: com.bookstore.api.resource, com.bookstore.api.exception.mapper");
        // Create and start Grizzly server
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
        LOGGER.debug("Grizzly HTTP server created for URI: {}", BASE_URI);
        return server;
    }

    public static void main(String[] args) throws IOException {
        LOGGER.info("Starting Jersey application...");
        final HttpServer server = startServer();
        LOGGER.info("Jersey app started with endpoints available at {}", BASE_URI);
        System.out.println("Hit Enter to stop the server...");
        System.in.read();
        server.shutdownNow();
        LOGGER.info("Server stopped.");
    }
}