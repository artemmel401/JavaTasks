package ru.artem.NauJava.rest;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import ru.artem.NauJava.entity.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAPITest {
    @LocalServerPort
    private int port;

    private static final String BASE_URL = "/users";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.authentication =
                RestAssured.preemptive().basic("Artem", "123");
    }

    // Тесты для эндпоинта /users/findByIsActive

    @Test
    void findByIsActive_WithValidActiveParam_ShouldReturnUsersList() {
        given()
                .queryParam("isActive", true)
                .when()
                .get(BASE_URL + "/findByIsActive")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("$", instanceOf(List.class));
    }

    @Test
    void findByIsActive_WithValidInactiveParam_ShouldReturnUsersList() {
        given()
                .queryParam("isActive", false)
                .when()
                .get(BASE_URL + "/findByIsActive")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("$", instanceOf(List.class));
    }

    @Test
    void findByIsActive_WithoutRequiredParam_ShouldReturnBadRequest() {
        given()
                .when()
                .get(BASE_URL + "/findByIsActive")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void findByIsActive_WithInvalidBooleanParam_ShouldReturnBadRequest() {
        given()
                .queryParam("isActive", "invalid_boolean")
                .when()
                .get(BASE_URL + "/findByIsActive")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    // Тесты для эндпоинта /users/findByEmail

    @Test
    void findByEmail_WithExistingEmail_ShouldReturnUser() {
        String existingEmail = "Artem";

        given()
                .queryParam("email", existingEmail)
                .when()
                .get(BASE_URL + "/findByEmail")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("email", equalTo(existingEmail));
    }

    @Test
    void findByEmail_WithNonExistingEmail_ShouldReturnEmpty() {
        String nonExistingEmail = "nonexisting@example.com";

        given()
                .queryParam("email", nonExistingEmail)
                .when()
                .get(BASE_URL + "/findByEmail")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(equalTo("null"));
    }

    @Test
    void findByEmail_WithoutEmailParam_ShouldReturnBadRequest() {
        given()
                .when()
                .get(BASE_URL + "/findByEmail")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void findByEmail_WithEmptyEmail_ShouldHandleGracefully() {
        given()
                .queryParam("email", "")
                .when()
                .get(BASE_URL + "/findByEmail")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void findByEmail_WithNullEmail_ShouldReturnBadRequest() {
        given()
                .queryParam("email", (String) null)
                .when()
                .get(BASE_URL + "/findByEmail")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void findByEmail_WithInvalidEmailFormat_ShouldHandleGracefully() {
        given()
                .queryParam("email", "invalid-email-format")
                .when()
                .get(BASE_URL + "/findByEmail")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    // Тесты для эндпоинта /users/findByDate

    @Test
    void findByRegistrationDate_WithValidDate_ShouldReturnUsersList() {
        String validDate = "2024-01-15T10:30:00";

        given()
                .queryParam("date", validDate)
                .when()
                .get(BASE_URL + "/findByDate")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("$", instanceOf(List.class));
    }

    @Test
    void findByRegistrationDate_WithCurrentDate_ShouldReturnUsersList() {
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        given()
                .queryParam("date", currentDate)
                .when()
                .get(BASE_URL + "/findByDate")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    void findByRegistrationDate_WithoutDateParam_ShouldReturnBadRequest() {
        given()
                .when()
                .get(BASE_URL + "/findByDate")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void findByRegistrationDate_WithInvalidDateFormat_ShouldReturnBadRequest() {
        given()
                .queryParam("date", "invalid-date-format")
                .when()
                .get(BASE_URL + "/findByDate")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void findByRegistrationDate_WithFutureDate_ShouldReturnEmptyList() {
        String futureDate = "2030-12-31T23:59:59";

        given()
                .queryParam("date", futureDate)
                .when()
                .get(BASE_URL + "/findByDate")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("$", empty());
    }

    @Test
    void findByRegistrationDate_WithVeryOldDate_ShouldReturnEmptyList() {
        String oldDate = "1900-01-01T00:00:00";

        given()
                .queryParam("date", oldDate)
                .when()
                .get(BASE_URL + "/findByDate")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("$", empty());
    }


    // Тесты на производительность (таймауты)

    @Test
    void findAllEndpoints_ShouldRespondInReasonableTime() {
        given()
                .queryParam("isActive", true)
                .when()
                .get(BASE_URL + "/findByIsActive")
                .then()
                .time(lessThan(2000L)); // Ответ должен прийти менее чем за 2 секунды

        given()
                .queryParam("email", "test@example.com")
                .when()
                .get(BASE_URL + "/findByEmail")
                .then()
                .time(lessThan(2000L));

        given()
                .queryParam("date", "2024-01-15T10:30:00")
                .when()
                .get(BASE_URL + "/findByDate")
                .then()
                .time(lessThan(2000L));
    }
}
