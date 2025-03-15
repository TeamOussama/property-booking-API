package com.api.booking.controller;

import com.api.booking.config.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BlockResourceTest extends BaseIT {


    @Test
    @Sql("/data/propertyData.sql")
    @Sql("/data/blockData.sql")
    void getBlock_success() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, managerJwtToken())
                .accept(ContentType.JSON)
                .when()
                .get("/api/blocks/1200")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("reason", Matchers.equalTo("At vero eos."));
    }

    @Test
    @Sql("/data/propertyData.sql")
    void createBlock_success() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, managerJwtToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(readResource("/requests/blockDTORequest.json"))
                .when()
                .post("/api/blocks/property/1100")
                .then()
                .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, blockRepository.count());
    }

    @Test
    @Sql("/data/propertyData.sql")
    void createBlock_missingField() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, managerJwtToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(readResource("/requests/blockDTORequest_missingField.json"))
                .when()
                .post("/api/blocks/property/1100")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                .body("fieldErrors.get(0).property", Matchers.equalTo("startDate"))
                .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql("/data/propertyData.sql")
    @Sql("/data/blockData.sql")
    void updateBlock_success() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, managerJwtToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(readResource("/requests/blockDTORequest.json"))
                .when()
                .put("/api/blocks/1200")
                .then()
                .statusCode(HttpStatus.OK.value());
        assertEquals("block reason", blockRepository.findById(((long) 1200)).orElseThrow().getReason());
        assertEquals(2, blockRepository.count());
    }

    @Test
    @Sql("/data/propertyData.sql")
    @Sql("/data/blockData.sql")
    void deleteBlock_success() {
        RestAssured
                .given()
                .header(HttpHeaders.AUTHORIZATION, managerJwtToken())
                .accept(ContentType.JSON)
                .when()
                .delete("/api/blocks/1200")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, blockRepository.count());
    }

}
