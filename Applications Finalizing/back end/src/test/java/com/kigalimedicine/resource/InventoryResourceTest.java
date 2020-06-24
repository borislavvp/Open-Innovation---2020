package com.kigalimedicine.resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import com.kigalimedicine.model.dto.InventoryItemDto;
import com.kigalimedicine.model.dto.ProductDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Scanner;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class InventoryResourceTest {

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
    public void getPharmacyInventory_noJwtToken_401Response() {
        Response response = given()
                .get("/inventory/1");

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    public void getPharmacyInventory_noPermission_401Response() {
        Response response = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .get("/inventory/1");

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    public void additem_noJwtToken_401Response() {
        Response response = given()
                .contentType(ContentType.JSON)
                .post("/inventory/1/1");

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    public void additem_noPermission_401Response() {
        Response response = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .get("/inventory/1");

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    public void updateItem_noJwtToken_401Response() {
        InventoryItemDto inventoryItemDto = new InventoryItemDto(null, null,null, 0, null, "EUR");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(inventoryItemDto)
                .put("/inventory/1");

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    public void updateItem_noPermission_401Response() {
        Response response = given()
                .header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .get("/inventory/1");

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    public void deleteItem_noJwtToken_401Response() {
        Response response = given()
                .delete("/inventory/1/1");

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    public void deleteItem_noPermission_401Response() {
        Response response = given().header("Authorization", "Bearer " + tokenHolder.getAccessToken())
                .get("/inventory/1");

        Assertions.assertEquals(401, response.statusCode());
    }
}
