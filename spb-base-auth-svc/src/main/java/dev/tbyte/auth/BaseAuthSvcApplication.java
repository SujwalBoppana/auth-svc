package dev.tbyte.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Authentication and Access Control Service.
 */
@SpringBootApplication
public class BaseAuthSvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseAuthSvcApplication.class, args);
    }

}