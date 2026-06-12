package dev.vetyutnev.notificator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableKafka
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class EventNotificatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventNotificatorApplication.class, args);
    }

}
