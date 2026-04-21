package dev.vetyutnev.eventmanagerplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class EventManagerPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventManagerPlatformApplication.class, args);
    }

}
