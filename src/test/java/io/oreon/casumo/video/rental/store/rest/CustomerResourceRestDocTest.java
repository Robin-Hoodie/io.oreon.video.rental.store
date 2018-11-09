package io.oreon.casumo.video.rental.store.rest;

import io.oreon.casumo.video.rental.store.RestDocTest;
import io.oreon.casumo.video.rental.store.helper.CustomerHelper;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;


public class CustomerResourceRestDocTest extends RestDocTest {

    @Inject
    private CustomerHelper customerHelper;

    @BeforeClass
    public static void setupBasePath() {
        RestAssured.basePath = "/customers";
    }

    @Test
    public void customerInventoryShouldReturnAllCustomers() {
        this.customerHelper.storeCustomers();
        given(super.spec)
                .filter(
                        document("customers/list/ok",
                                preprocessRequest(setPort(), prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .when()
                .contentType(JSON)
                .get("/")
                .then()
                .statusCode(HTTP_OK);
    }

    @Test
    public void customerInventoryShouldReturnAnEmptyArray() {
        given(super.spec)
                .filter(
                        document("customers/list/empty",
                                preprocessRequest(setPort(), prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .when()
                .contentType(JSON)
                .get("/")
                .then()
                .statusCode(HTTP_OK);
    }
}
