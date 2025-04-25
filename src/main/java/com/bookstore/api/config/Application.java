package com.bookstore.api.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;


@ApplicationPath("/api")
public class Application extends ResourceConfig {
    public Application(){
        // Registering JAX-RS resources files and exception mappers in the project
        packages("com.bookstore.api.resource", "com.bookstore.api.exception.mapper");
        //Enable Jackson from JSON
        register(JacksonFeature.class);
    }

}