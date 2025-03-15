package com.api.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = "com.api.booking.repository.entity")
public class BookingApplication {

    public static void main(final String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

}
