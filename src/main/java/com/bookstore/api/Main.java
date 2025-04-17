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


    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // Configure JAX-RS application with package scanning and Jackson
        final ResourceConfig config = new ResourceConfig()
                .packages("com.bookstore.api.resource", "com.bookstore.api.exception.mapper")
                .register(JacksonFeature.class); // Enable JSON serialization
        LOGGER.debug("ResourceConfig initialized with packages.");
        // Create and start Grizzly server
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
        LOGGER.debug("Grizzly HTTP server created for URI: {}", BASE_URI);
        return server;
    }

    /**
     * Main method to start the server.
     *
     * @param args command line arguments
     * @throws IOException if an I/O error occurs
     */

    public static void main(String[] args) throws IOException {
        LOGGER.info("Starting Jersey application...");
        final HttpServer server = startServer();
        LOGGER.info("Jersey application started with endpoints; available at {}", BASE_URI);
        System.out.println("Press Enter to stop the server...");
        System.in.read();
        server.shutdownNow();
        LOGGER.info("Server stopped.");
    }
}