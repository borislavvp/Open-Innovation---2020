package com.kigalimedicine.resource;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import com.kigalimedicine.model.dto.PharmacyDto;
import com.kigalimedicine.model.dto.PharmacyMemberDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PharmacyResourceTest {

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
    public void createPharmacy_validJsonBody_pharmacyDto() {
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response initialState = given()
                .get("/pharmacy");

        int amountOfInitialPharmacies = initialState.as(List.class).size();

        Response response = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(pharmacyDto)
                .contentType(ContentType.JSON)
                .post("/pharmacy");

        response.peek();

        PharmacyDto result = response.as(PharmacyDto.class);

        Assertions.assertEquals("Stefs pharmacy", result.getName());
        // DISABLED FOR USERTESTING, CHANGE BACK FOR PRODUCTION
//        Assertions.assertFalse(result.getApproved());
        Assertions.assertEquals(51.3174046, result.getLatitude());
        Assertions.assertEquals(5.3951756, result.getLongitude());

        Response pharmacyGetResponse = given()
                .queryParam("showNotApproved", true)
                .get("/pharmacy");

        Assertions.assertEquals(amountOfInitialPharmacies + 1, pharmacyGetResponse.as(List.class).size());
    }

    @Test
    public void createPharmacy_noJwtToken_401Response() {
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response response = given()
                .body(pharmacyDto)
                .contentType(ContentType.JSON)
                .post("/pharmacy");


        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    public void createPharmacy_invalidJwtToken_401Response() {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE2MTYyMzkwMjJ9.GOKZqDEYCIuuuWAgOXLiSE9FZafJ0vV9SY9DaWTAb3g";
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response response = given()
                .body(pharmacyDto)
                .header("Authorization", "Bearer " + jwt)
                .contentType(ContentType.JSON)
                .post("/pharmacy");

        Assertions.assertEquals(403, response.statusCode());
    }

    @Test
    public void getPharmacy_existingPharmacy_jsonResponse() {
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response create = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(pharmacyDto)
                .contentType(ContentType.JSON)
                .post("/pharmacy");

        PharmacyDto createResult = create.as(PharmacyDto.class);

        Response get = given()
                .get("/pharmacy/" + createResult.getId());

        Assertions.assertEquals(200, get.statusCode());

        PharmacyDto result = get.as(PharmacyDto.class);

        Assertions.assertEquals(createResult.getId(), result.getId());
        Assertions.assertEquals("Stefs pharmacy", result.getName());
        // DISABLED FOR USERTESTING, CHANGE BACK FOR PRODUCTION
//        Assertions.assertFalse(result.getApproved());
        Assertions.assertEquals(51.3174046, result.getLatitude());
        Assertions.assertEquals(5.3951756, result.getLongitude());
    }

    @Test
    public void getPharmacy_nonExistingPharmacy_404response() {
        Response get = given()
                .get("/pharmacy/1234567");

        Assertions.assertEquals(404, get.statusCode());
    }

    @Test
    public void editPharmacy_existingPharmacy_200response() {
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response create = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(pharmacyDto)
                .contentType(ContentType.JSON)
                .post("/pharmacy");

        // DISABLED FOR USERTESTING, CHANGE BACK FOR PRODUCTION
//        Assertions.assertFalse(create.as(PharmacyDto.class).getApproved());

        Long pharmacyId = create.as(PharmacyDto.class).getId();

        PharmacyDto editDto = new PharmacyDto(null, "Stefpharmacy", true, 51.3174047, 5.3951758, "Kigali", new ArrayList<>(), new ArrayList<>());

        Response edit = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(editDto)
                .contentType(ContentType.JSON)
                .patch("/pharmacy/" + pharmacyId);

        Assertions.assertEquals(pharmacyId, edit.as(PharmacyDto.class).getId());
        Assertions.assertTrue(edit.as(PharmacyDto.class).getApproved());
        Assertions.assertEquals("Stefpharmacy", edit.as(PharmacyDto.class).getName());
        Assertions.assertEquals(51.3174047, edit.as(PharmacyDto.class).getLatitude());
        Assertions.assertEquals(5.3951758, edit.as(PharmacyDto.class).getLongitude());
        Assertions.assertEquals("Kigali", edit.as(PharmacyDto.class).getAddress());
    }

    @Test
    public void editPharmacy_onlyName_200response() {
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response create = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(pharmacyDto)
                .contentType(ContentType.JSON)
                .post("/pharmacy");
        // DISABLED FOR USERTESTING, CHANGE BACK FOR PRODUCTION
//        Assertions.assertFalse(create.as(PharmacyDto.class).getApproved());

        Long pharmacyId = create.as(PharmacyDto.class).getId();

        String payload = "{\n" +
                "  \"name\": \"only name changes\"\n" +
                "}";
        Response edit = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(payload)
                .contentType(ContentType.JSON)
                .patch("/pharmacy/" + pharmacyId);

        Assertions.assertEquals(pharmacyId, edit.as(PharmacyDto.class).getId());
        // DISABLED FOR USERTESTING, CHANGE BACK FOR PRODUCTION
//        Assertions.assertFalse(edit.as(PharmacyDto.class).getApproved());
        Assertions.assertEquals("only name changes", edit.as(PharmacyDto.class).getName());
        Assertions.assertEquals(51.3174046, edit.as(PharmacyDto.class).getLatitude());
        Assertions.assertEquals(5.3951756, edit.as(PharmacyDto.class).getLongitude());
        Assertions.assertEquals("Hoefijzer 21", edit.as(PharmacyDto.class).getAddress());
    }

    @Test
    public void editPharmacy_nonExistingPharmacy_404response() {
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response edit = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(pharmacyDto)
                .contentType(ContentType.JSON)
                .patch("/pharmacy/23412");

        Assertions.assertEquals(404, edit.statusCode());
    }

    @Test
    public void deletePharmacy_existingPharmacy_noContentResponse() {
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response create = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(pharmacyDto)
                .contentType(ContentType.JSON)
                .post("/pharmacy");

        Long pharmacyId = create.as(PharmacyDto.class).getId();

        Response delete = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .delete("/pharmacy/" + pharmacyId);

        Assertions.assertEquals(204, delete.statusCode());

        Response get = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .get("/pharmacy/" + pharmacyId);

        Assertions.assertEquals(404, get.statusCode());
    }

    @Test
    public void deletePharmacy_nonExistingPharmacy_notFoundResponse() {
        Response delete = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .delete("/pharmacy/1234534");

        Assertions.assertEquals(404, delete.statusCode());
    }

    @Test
    public void getMembers_oneMember_arraySizeOne() {
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response create = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(pharmacyDto)
                .contentType(ContentType.JSON)
                .post("/pharmacy");

        Long pharmacyId = create.as(PharmacyDto.class).getId();

        Response get = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .get("/pharmacy/" + pharmacyId + "/member");

        Assertions.assertEquals(1, get.as(List.class).size());
    }

    @Test
    public void addMember_validMemberJson_arraySizeTwo() {
        PharmacyDto pharmacyDto = new PharmacyDto(null, "Stefs pharmacy", true, 51.3174046, 5.3951756, "Hoefijzer 21", new ArrayList<>(), new ArrayList<>());

        Response create = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(pharmacyDto)
                .contentType(ContentType.JSON)
                .post("/pharmacy");

        Long pharmacyId = create.as(PharmacyDto.class).getId();

        PharmacyMemberDto pharmacyMemberDto = new PharmacyMemberDto("123", "123");

        given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .body(pharmacyMemberDto)
                .contentType(ContentType.JSON)
                .post("/pharmacy/" + pharmacyId + "/member");

        Response get = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .get("/pharmacy/" + pharmacyId + "/member");

        Assertions.assertEquals(2, get.as(List.class).size());

    }

}