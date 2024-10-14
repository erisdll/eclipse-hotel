package com.erika.eclipse_hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EclipseHotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(EclipseHotelApplication.class, args);
    }

}
