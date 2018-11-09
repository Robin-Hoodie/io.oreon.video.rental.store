package io.oreon.casumo.video.rental.store.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.oreon.casumo.video.rental.store.SpringIntegrationTest;
import io.oreon.casumo.video.rental.store.helper.CustomerHelper;
import io.oreon.casumo.video.rental.store.helper.FilmHelper;
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

public class VideoRentalStoreResourceIntegrationTest extends SpringIntegrationTest {

    @Inject
    private FilmHelper filmHelper;

    @Inject
    private CustomerHelper customerHelper;

    @BeforeClass
    public static void setupBasePath() {
        RestAssured.basePath = "/films";
    }


    @Test
    public void filmInventoryShouldReturnAllFilms() {
        this.filmHelper.storeFilms();
        Film[] films = given()
                .when()
                .contentType(JSON)
                .get("/")
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .as(Film[].class);

        assertThat(films).hasSize(3).usingElementComparatorIgnoringFields("id").containsExactly(PREMIUM_FILM, REGULAR_FILM, OLD_FILM);
    }

    @Test
    public void filmInventoryShouldReturnAnEmptyArray() {
        Film[] films = given()
                .when()
                .contentType(JSON)
                .get("/")
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .as(Film[].class);

        assertThat(films).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void rentFilmsShouldReturnCorrectPrices() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        RentFilmsResponse rentFilmsResponse = given()
                .when()
                .body(objectMapper.writeValueAsString(of(PREMIUM_FILM_NAME, 5, REGULAR_FILM_NAME, 7, OLD_FILM_NAME, 7)))
                .contentType(JSON)
                .post("/rent")
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .as(RentFilmsResponse.class);

        assertThat(rentFilmsResponse).isEqualToComparingFieldByField(new RentFilmsResponse("440 SEK"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void rentFilmsShouldContainErrorsForFilm() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        Map<String, List<String>> errors = given()
                .when()
                .body(objectMapper.writeValueAsString(of(NON_EXISTING_FILM_NAME, 5)))
                .contentType(JSON)
                .post("/rent")
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .as(Map.class);

        assertThat(errors).containsOnlyKeys(FILM_ERROR_KEY);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void rentFilmsShouldContainErrorsForDaysRented() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        Map<String, List<String>> errors = given()
                .when()
                .body(objectMapper.writeValueAsString(of(PREMIUM_FILM_NAME, -1)))
                .contentType(JSON)
                .post("/rent")
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .as(Map.class);

        assertThat(errors).containsOnlyKeys(DAYS_RENTED_ERROR_KEY);
    }

    @Test
    public void rentFilmsShouldReturnTotalPriceOf440SEK() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        RentFilmsResponse totalPrice = given()
                .when()
                .body(objectMapper.writeValueAsString(of(PREMIUM_FILM_NAME, 5, REGULAR_FILM_NAME, 7, OLD_FILM_NAME, 7)))
                .contentType(JSON)
                .post("/rent")
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .as(RentFilmsResponse.class);

        assertThat(totalPrice).isEqualToComparingFieldByField(new RentFilmsResponse("440 SEK"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void returnFilmsShouldContainErrorForCustomer() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        this.customerHelper.storeCustomers();
        Map<String, List<String>> errors = given()
                .when()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(of(PREMIUM_FILM_NAME, aRentalDays().build())))
                .post("/return?customer={name}", NON_EXISTING_CUSTOMER_NAME)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .as(Map.class);

        assertThat(errors).containsOnlyKeys(CUSTOMER_ERORR_KEY);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void returnFilmsShouldContainErrorForFilms() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        this.customerHelper.storeCustomers();
        Map<String, List<String>> errors = given()
                .when()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(of(NON_EXISTING_FILM_NAME, aRentalDays().build())))
                .post("/return?customer={name}", CUSTOMER_ONE_NAME)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .as(Map.class);

        assertThat(errors).containsOnlyKeys(FILM_ERROR_KEY);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void returnFilmsShouldContainErrorForRentalDays() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        this.customerHelper.storeCustomers();
        Map<String, List<String>> errors = given()
                .when()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(of(PREMIUM_FILM_NAME, aRentalDays().withRealDaysRented(-1).build())))
                .post("/return?customer={name}", CUSTOMER_ONE_NAME)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .as(Map.class);

        assertThat(errors).containsOnlyKeys(RENTAL_DAYS_ERROR_KEY);
    }

    @Test
    public void returnFilmsShouldReturnCorrectResponse() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        this.customerHelper.storeCustomers();
        ReturnalFilmResponse returnalFilmResponse = given()
                .when()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(of(PREMIUM_FILM_NAME, aRentalDays().withRealDaysRented(2).withOriginalDaysRented(1).build())))
                .post("/return?customer={name}", CUSTOMER_ONE_NAME)
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .as(ReturnalFilmResponse.class);

        assertThat(returnalFilmResponse).isEqualToComparingFieldByField(
                aReturnalFilmResponse()
                        .withCustomerName(CUSTOMER_ONE_NAME)
                        .withBonusPoints(2)
                        .withSurCharges("40 SEK"));
    }
}
