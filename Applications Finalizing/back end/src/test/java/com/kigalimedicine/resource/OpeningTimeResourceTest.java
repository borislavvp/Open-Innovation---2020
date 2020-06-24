package com.kigalimedicine.resource;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import com.kigalimedicine.model.dto.OpeningTimeDto;
import com.kigalimedicine.model.dto.PharmacyDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javax.inject.Inject;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class OpeningTimeResourceTest {

    private static TokenHolder tokenHolder;

    @ConfigProperty(name = "auth0.clientId")
    String auth0ClientId;

    @ConfigProperty(name = "auth0.clientSecret")
    String auth0ClientSecret;

    @ConfigProperty(name = "auth0.apiUrl")
    String auth0ApiUrl;

    @BeforeEach
    private void setup() {
        if (tokenHolder != null) {
            return;
        }
        AuthAPI authAPI = new AuthAPI(auth0ApiUrl, auth0ClientId, auth0ClientSecret);
        AuthRequest authRequest = authAPI.requestToken("https://" + auth0ApiUrl + "/api/v2/");
        try {
            tokenHolder = authRequest.execute();
        } catch (Auth0Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addOpeningTime_validOpeningTime_openingTimeDto() {
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response createPharmacy = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(pharmacyDto)
                .contentType(ContentType.JSON)
                .post("/pharmacy");

        PharmacyDto newPharmacy = createPharmacy.as(PharmacyDto.class);

        String openingTime = "{\"dayOfWeek\": \"MONDAY\", \"startTime\": \"09:00:00\", \"stopTime\": \"12:30:00\"}";

        Response addOpeningTimeResponse = given()
                .contentType(ContentType.JSON)
                .body(openingTime)
                .post("/pharmacy/" + newPharmacy.getId()  + "/opening-times");

        Assertions.assertEquals(200, addOpeningTimeResponse.statusCode());

        OpeningTimeDto responseDto = addOpeningTimeResponse.as(OpeningTimeDto.class);

        Assertions.assertEquals(DayOfWeek.MONDAY, responseDto.getDayOfWeek());
        Assertions.assertEquals("12:30:00", responseDto.getStopTime());
        Assertions.assertEquals("09:00:00", responseDto.getStartTime());

        Response get = given()
                .get("/pharmacy/" + createPharmacy.as(PharmacyDto.class).getId());

        PharmacyDto result = get.as(PharmacyDto.class);
        Assertions.assertEquals(1, result.getOpeningTimes().size());
    }

    @Test
    public void addOpeningTime_nonExistingPharmacy_404response() {
        String openingTime = "{\"dayOfWeek\": \"MONDAY\", \"startTime\": \"09:00:00\", \"stopTime\": \"12:30:00\"}";

        Response addOpeningTimeResponse = given()
                .contentType(ContentType.JSON)
                .body(openingTime)
                .post("/pharmacy/asdf/opening-times");

        Assertions.assertEquals(404, addOpeningTimeResponse.statusCode());
    }

    @Test
    public void deleteOpeningTime_validOpeningTime_200response() {
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response createPharmacy = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(pharmacyDto)
                .contentType(ContentType.JSON)
                .post("/pharmacy");

        PharmacyDto newPharmacy = createPharmacy.as(PharmacyDto.class);

        String openingTime = "{\"dayOfWeek\": \"MONDAY\", \"startTime\": \"09:00:00\", \"stopTime\": \"12:30:00\"}";

        Response addOpeningTimeResponse = given()
                .contentType(ContentType.JSON)
                .body(openingTime)
                .post("/pharmacy/" + newPharmacy.getId()  + "/opening-times");

        Assertions.assertEquals(200, addOpeningTimeResponse.statusCode());

        OpeningTimeDto responseDto = addOpeningTimeResponse.as(OpeningTimeDto.class);

        Response deleteOpeningTime = given()
                .contentType(ContentType.JSON)
                .body(openingTime)
                .delete("/pharmacy/" + newPharmacy.getId()  + "/opening-times/" + responseDto.getId());

        Assertions.assertEquals(204, deleteOpeningTime.statusCode());
    }

    @Test
    void getOpeningTimes_noOpeningTimes_emptyArray() {
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response createPharmacy = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(pharmacyDto)
                .contentType(ContentType.JSON)
                .post("/pharmacy");

        PharmacyDto newPharmacy = createPharmacy.as(PharmacyDto.class);

        Response get = given()
                .get("/pharmacy/" + newPharmacy.getId() + "/opening-times");

        get.peek();

        Assertions.assertEquals(200, get.statusCode());

        List<PharmacyDto> result = Arrays.asList(get.as(PharmacyDto[].class));
        Assertions.assertEquals(0, result.size());
    }

}
