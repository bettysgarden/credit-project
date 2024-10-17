package ru.test.conveyor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ConveyorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConveyorApplication.class, args);
    }

}
