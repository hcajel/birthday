package com.cajel.challenge.birthday;

import com.cajel.challenge.birthday.entities.Guest;
import com.cajel.challenge.birthday.entities.Guest.Status;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.cajel.challenge.birthday.entities.Guest.Status.CONFIRMED;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GuestControllerRestAssuredTest {

    private String URLAPI = "/api/guests";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void testGuests() {

        Guest guest1 = new Guest(null, "Bob", "Doe", Guest.Status.POSSIBLE);
        Guest guest2 = new Guest(null, "Simon", "Doe", Guest.Status.POSSIBLE);

        Integer guestId1 = given().contentType(ContentType.JSON).body(guest1)
                        .when().post(URLAPI)
                        .then().statusCode(200).extract().path("id");

        Integer guestId2 = given().contentType(ContentType.JSON).body(guest2)
                .when().post(URLAPI)
                .then().statusCode(200).extract().path("id");

        given().contentType(ContentType.JSON)
                .when().get(URLAPI)
                .then().statusCode(200)
                .body("size()", equalTo(2),
                        "name", hasItems("Bob", "Simon"));


        Status newStatus = CONFIRMED;
        given().contentType(ContentType.JSON)
                .pathParam("id", guestId1)
                .queryParam("status", newStatus)
                .when().put(URLAPI + "/{id}/status")
                .then().statusCode(200)
                .body("status", equalTo(newStatus.toString()));

        given().contentType(ContentType.JSON)
                .pathParam("id", guestId2)
                .queryParam("status", newStatus)
                .when().put(URLAPI + "/{id}/status")
                .then().statusCode(200)
                .body("status", equalTo(newStatus.toString()));

        given().contentType(ContentType.JSON)
                .when().get(URLAPI)
                .then().statusCode(200)
                .body("size()", equalTo(2),
                        "status", hasItems(newStatus.toString(), newStatus.toString()));

        given().when().delete(URLAPI + "/{id}", guestId1)
                .then().statusCode(200);

        given().when().delete(URLAPI + "/{id}", guestId2)
                .then().statusCode(200);

        given().contentType(ContentType.JSON)
                .when().get(URLAPI)
                .then().statusCode(200)
                .body("size()", equalTo(0));
    }


}
