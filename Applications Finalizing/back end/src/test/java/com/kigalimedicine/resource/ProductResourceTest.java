package com.kigalimedicine.resource;
import com.kigalimedicine.model.dto.ProductDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ProductResourceTest {
    

    @Test
    public void createProduct_Not_Approved_User() {
        ProductDto productDto = new ProductDto(null,"Paracetamol","123456789","pcs",5);
        
        Response response = given()
                .body(productDto)
                .contentType(ContentType.JSON)
                .post("/product");

        Assertions.assertEquals(401, response.statusCode());
    }
    
    @Test
    public void updateProduct_Invalid_ProductId() {
        ProductDto productDto = new ProductDto(null, "Paracetamol", "123456789", "pcs", 5);

        Response response = given().pathParam("productId", 123).contentType(ContentType.JSON).body(productDto)
                .put("/product/{productId}");

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    public void deleteProduct_Invalid_ProductId() {
        Response response = given().pathParam("productId", 123).contentType(ContentType.JSON)
                .delete("/product/{productId}");

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    public void getProductPharmacies_Invalid_ProductId() {
        Response response = given().pathParam("productId", 123).contentType(ContentType.JSON)
                .get("/product/{productId}/pharmacies");

        Assertions.assertEquals(401, response.statusCode());
    }

     @Test
    public void getProductInventoryItems_Invalid_ProductId() {
        Response response = given().pathParam("productId", 123).contentType(ContentType.JSON)
                .get("/product/{productId}/inventory-items");

        Assertions.assertEquals(401, response.statusCode());
    }

}