package com.api.booking.controller;

import com.api.booking.config.BaseIT;
import com.api.booking.repository.entity.Booking;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingResourceTest extends BaseIT {

    @Test
    @Sql("/data/propertyData.sql")
    @Sql("/data/bookingData.sql")
    void getBooking_success() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, guestJwtToken())
                .accept(ContentType.JSON)
                .when()
                .get("/api/bookings/1300")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("isCanceled", Matchers.equalTo(false));
    }

    @Test
    void getNonExistingBooking_notAuthorized() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, guestJwtToken())
                .accept(ContentType.JSON)
                .when()
                .get("/api/bookings/1966")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body("code", Matchers.equalTo("AUTHORIZATION_DENIED"));
    }

    @Test
    @Sql("/data/propertyData.sql")
    void createBooking_success() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, guestJwtToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(readResource("/requests/bookingDTORequest.json"))
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, bookingRepository.count());
    }

    @Test
    @Sql("/data/propertyData.sql")
    @Sql("/data/bookingData.sql")
    void createBooking_failsWithOverlappingBooking() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, guestJwtToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(readResource("/requests/overlappingBookingRequest.json"))
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body("code", Matchers.equalTo("ILLEGAL_STATE"));

        assertEquals(5, bookingRepository.count());
    }


    @Test
    void createBooking_missingField() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, guestJwtToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(readResource("/requests/bookingDTORequest_missingField.json"))
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                .body("fieldErrors.get(0).property", Matchers.equalTo("startDate"))
                .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql("/data/propertyData.sql")
    @Sql("/data/bookingData.sql")
    void updateBooking_success() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, guestJwtToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(readResource("/requests/bookingDTORequest.json"))
                .when()
                .put("/api/bookings/1300")
                .then()
                .statusCode(HttpStatus.OK.value());
        assertEquals(5, bookingRepository.count());
    }

    @Test
    @Sql("/data/propertyData.sql")
    @Sql("/data/bookingData.sql")
    void cancelBooking_success() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, guestJwtToken())
                .accept(ContentType.JSON)
                .when()
                .post("/api/bookings/1301/cancel")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertTrue(bookingRepository.findById(1301L).get().isCanceled());
    }

    @Test
    @Sql("/data/propertyData.sql")
    @Sql("/data/bookingData.sql")
    void rebookCanceledBooking_success() {
        String rebookRequestJson = "{ \"startDate\": \"2024-08-10\", \"endDate\": \"2024-08-12\" }";

        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, guestJwtToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(rebookRequestJson)
                .when()
                .post("/api/bookings/1304/rebook")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        Booking rebooked = bookingRepository.findById(1304L).orElseThrow();
        assertEquals(false, rebooked.isCanceled());
    }

    @Test
    @Sql("/data/propertyData.sql")
    @Sql("/data/bookingData.sql")
    void rebookNonCanceledBooking_failure() {
        String rebookRequestJson = "{ \"startDate\": \"2024-08-10\", \"endDate\": \"2024-08-12\" }";

        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, guestJwtToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(rebookRequestJson)
                .when()
                .post("/api/bookings/1301/rebook")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body("code", Matchers.equalTo("ILLEGAL_STATE"))
                .body("message", Matchers.containsString("Booking is not canceled and cannot be rebooked."));
    }

    @Test
    @Sql("/data/propertyData.sql")
    @Sql("/data/bookingData.sql")
    void deleteBooking_success() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, guestJwtToken())
                .accept(ContentType.JSON)
                .when()
                .delete("/api/bookings/1300")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(4, bookingRepository.count());
    }

}
