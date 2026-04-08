package org.example.ideavoltex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Entry point for the Idea Voltex Secure Transaction System.
 * * This class initializes the Spring Boot application context,
 * starting the embedded web server and auto-configuring the
 * underlying cryptographic and database services.
 */
@SpringBootApplication
@EnableMongoRepositories
public class Main {

    /**
     * Bootstraps the application.
     * Starts the dependency injection container and scans for components,
     * including Controllers, Services, and Cryptographic Utilities.
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}