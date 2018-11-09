package io.oreon.casumo.video.rental.store.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.oreon.casumo.video.rental.store.SpringIntegrationTest;
import io.oreon.casumo.video.rental.store.helper.CustomerHelper;
import io.oreon.casumo.video.rental.store.helper.FilmHelper;
import io.oreon.casumo.video.rental.store.model.Customer;
import io.oreon.casumo.video.rental.store.model.Film;
import io.oreon.casumo.video.rental.store.rest.dto.RentFilmsResponse;
import io.oreon.casumo.video.rental.store.rest.dto.ReturnalFilmResponse;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static io.oreon.casumo.video.rental.store.constants.TestConstants.*;
import static io.oreon.casumo.video.rental.store.rest.dto.RentalDays.Builder.aRentalDays;
import static io.oreon.casumo.video.rental.store.rest.dto.ReturnalFilmResponse.Builder.aReturnalFilmResponse;
import static io.oreon.casumo.video.rental.store.rest.validation.RequestValidator.*;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerResourceIntegrationTest extends SpringIntegrationTest {

    @Inject
    private CustomerHelper customerHelper;

    @BeforeClass
    public static void setupBasePath() {
        RestAssured.basePath = "/customers";
    }

    @Test
    public void customerInventoryShouldReturnAllCustomers() {
        this.customerHelper.storeCustomers();
        Customer[] customers = given()
                .when()
                .contentType(JSON)
                .get("/")
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .as(Customer[].class);

        assertThat(customers).hasSize(1).usingElementComparatorIgnoringFields("id").containsExactly(CUSTOMER_ONE);
    }

    @Test
    public void customerInventoryShouldReturnAnEmptyArray() {
        Customer[] customers = given()
                .when()
                .contentType(JSON)
                .get("/")
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .as(Customer[].class);

        assertThat(customers).isEmpty();
    }
}
