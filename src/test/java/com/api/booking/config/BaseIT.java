package com.api.booking.config;

import com.api.booking.BookingApplication;
import com.api.booking.repository.BlockRepository;
import com.api.booking.repository.BookingRepository;
import com.api.booking.repository.PropertyRepository;
import com.api.booking.repository.UserRepository;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context, with all data
 * wiped out before each test.
 */
@SpringBootTest(
        classes = BookingApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("it")
@Sql({"/data/clearAll.sql", "/data/userData.sql"})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class BaseIT {

    @LocalServerPort
    public int serverPort;

    @Autowired
    public PropertyRepository propertyRepository;

    @Autowired
    public BlockRepository blockRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public BookingRepository bookingRepository;

    @PostConstruct
    public void initRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.urlEncodingEnabled = false;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @SneakyThrows
    public String readResource(final String resourceName) {
        return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), StandardCharsets.UTF_8);
    }

    public String guestJwtToken() {
        // user guest, expires 2040-01-01
        return "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJndWVzdCIsInJvbGVzIjpbIkdVRVNUIl0sImlzcyI6ImJvb3RpZnkiLCJpYXQiOjE3NDE2MDUwMzUsImV4cCI6MjIwODk4ODgwMH0." +
                "by_98X59mCyYPpGvHAl1UDbD7b7zTe08qeutBTeEtx793PmpxAlLg4qWyuiaqvZalOtB8WX_NdzgCrPxEFaPPg";
    }

    public String managerJwtToken() {
        // user manager, expires 2040-01-01
        return "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJtYW5hZ2VyIiwicm9sZXMiOlsiTUFOQUdFUiJdLCJpc3MiOiJib290aWZ5IiwiaWF0IjoxNzQxNjA1MDM1LCJleHAiOjIyMDg5ODg4MDB9." +
                "9iG_CRw67Ji3EkJq04i_uNTHZR7FIB_xX0ZOK27jESoiscWio43YQ0g2ohWPGhCj2c06gpQ6g95ZOR48w6N6ww";
    }

    public String ownerJwtToken() {
        // user owner, expires 2040-01-01
        return "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJvd25lciIsInJvbGVzIjpbIk9XTkVSIl0sImlzcyI6ImJvb3RpZnkiLCJpYXQiOjE3NDE2MDUwMzUsImV4cCI6MjIwODk4ODgwMH0." +
                "9Wx0N2y0XTMHSIhFXsyhOchg3OUn-l1X_d-dpQaXNjUSd4dwJK2zyDF4i9c7R8O82vU3BzvzQcjSvjFBMLagJg";
    }

}
