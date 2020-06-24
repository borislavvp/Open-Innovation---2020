package com.kigalimedicine.openapi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class OpenApiGeneratorTest {
    @Test
    public void generateOpenApiSpecification() {
        Response response = given().get("/openapi")
                .then()
                .contentType("application/yaml;charset=UTF-8")
                .extract()
                .response();
        String yaml = response.body().prettyPrint();

        try {
            Path file = Paths.get("./target/openapi.yaml");
            Files.write(file, Collections.singleton(yaml), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
