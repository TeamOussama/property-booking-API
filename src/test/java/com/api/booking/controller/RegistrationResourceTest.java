package com.api.booking.controller;

import com.api.booking.config.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RegistrationResourceTest extends BaseIT {

    @Test
    void register_success() {
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(readResource("/requests/registrationRequest.json"))
                .when()
                .post("/register")
                .then()
                .statusCode(HttpStatus.OK.value());
        assertEquals(4, userRepository.count());
    }

}
