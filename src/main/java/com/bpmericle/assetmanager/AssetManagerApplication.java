package com.bpmericle.assetmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class that starts the asset manager Spring Boot application.
 *
 * @author Brian Mericle
 */
@SpringBootApplication
public class AssetManagerApplication {

    /**
     * Starts the asset manager Spring Boot application.
     *
     * @param args the arguments passed into the Spring Boot application.
     */
    public static void main(String[] args) {
        SpringApplication.run(AssetManagerApplication.class, args);
    }
}
