package com.bookstore.api.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    // No need to override getClasses() since ResourceConfig.packages() handles scanning
}