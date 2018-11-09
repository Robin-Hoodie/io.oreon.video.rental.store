package io.oreon.casumo.video.rental.store.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.oreon.casumo.video.rental.store.RestDocTest;
import io.oreon.casumo.video.rental.store.helper.CustomerHelper;
import io.oreon.casumo.video.rental.store.helper.FilmHelper;
import io.oreon.casumo.video.rental.store.model.Film;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.inject.Inject;

import static com.google.common.collect.ImmutableMap.of;
import static io.oreon.casumo.video.rental.store.constants.TestConstants.*;
import static io.oreon.casumo.video.rental.store.rest.dto.RentalDays.Builder.aRentalDays;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;


public class VideoRentalStoreResourceRestDocTest extends RestDocTest {

    private static final String CUSTOMER_NAME_PARAM_KEY = "customer";
    private static final String CUSTOMER_NAME_PARAM_DESCRIPTION = "The name of the customer";

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
        given(super.spec)
                .filter(
                        document("films/list/ok",
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
    public void filmInventoryShouldReturnAnEmptyArray() {
        Film[] films = given(super.spec)
                .filter(
                        document("films/list/empty",
                                preprocessRequest(setPort(), prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
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
    public void rentFilmsShouldContainErrorsForFilm() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        given(super.spec)
                .filter(
                        document("films/rent/error-filmnames",
                                preprocessRequest(setPort(), prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .when()
                .body(objectMapper.writeValueAsString(of(NON_EXISTING_FILM_NAME, 5)))
                .contentType(JSON)
                .post("/rent")
                .then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void rentFilmsShouldContainErrorsForDaysRented() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        given(super.spec)
                .filter(
                        document("films/rent/error-daysrented",
                                preprocessRequest(setPort(), prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .when()
                .body(objectMapper.writeValueAsString(of(PREMIUM_FILM_NAME, -1)))
                .contentType(JSON)
                .post("/rent")
                .then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void rentFilmsShouldReturnCorrectPrices() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        given(super.spec)
                .filter(
                        document("films/rent/ok",
                                preprocessRequest(setPort(), prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .when()
                .body(objectMapper.writeValueAsString(of(PREMIUM_FILM_NAME, 5, REGULAR_FILM_NAME, 7, OLD_FILM_NAME, 7)))
                .contentType(JSON)
                .post("/rent")
                .then()
                .statusCode(HTTP_OK);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void returnFilmsShouldContainErrorForCustomer() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        this.customerHelper.storeCustomers();
        given(super.spec)
                .filter(
                        document("films/return/error-customer",
                                preprocessRequest(setPort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName(CUSTOMER_NAME_PARAM_KEY).description(CUSTOMER_NAME_PARAM_DESCRIPTION)
                                )
                        )
                )
                .when()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(of(PREMIUM_FILM_NAME, aRentalDays().build())))
                .post("/return?customer={name}", NON_EXISTING_CUSTOMER_NAME)
                .then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void returnFilmsShouldContainErrorForFilms() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        this.customerHelper.storeCustomers();
        given(super.spec)
                .filter(
                        document("films/return/error-films",
                                preprocessRequest(setPort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName(CUSTOMER_NAME_PARAM_KEY).description(CUSTOMER_NAME_PARAM_DESCRIPTION)
                                )
                        )
                )
                .when()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(of(NON_EXISTING_FILM_NAME, aRentalDays().build())))
                .post("/return?customer={name}", CUSTOMER_ONE_NAME)
                .then()
                .statusCode(HTTP_BAD_REQUEST);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void returnFilmsShouldContainErrorForRentalDays() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        this.customerHelper.storeCustomers();
        given(super.spec)
                .filter(
                        document("films/return/error-rentaldays",
                                preprocessRequest(setPort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName(CUSTOMER_NAME_PARAM_KEY).description(CUSTOMER_NAME_PARAM_DESCRIPTION)
                                )
                        )
                )
                .when()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(of(PREMIUM_FILM_NAME, aRentalDays().withRealDaysRented(-1).build())))
                .post("/return?customer={name}", CUSTOMER_ONE_NAME)
                .then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    public void returnFilmsShouldReturnCorrectResponse() throws JsonProcessingException {
        this.filmHelper.storeFilms();
        this.customerHelper.storeCustomers();
        given(super.spec)
                .filter(
                        document("films/return/ok",
                                preprocessRequest(setPort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName(CUSTOMER_NAME_PARAM_KEY).description(CUSTOMER_NAME_PARAM_DESCRIPTION)
                                )
                        )
                )
                .when()
                .contentType(JSON)
                .body(objectMapper.writeValueAsString(of(PREMIUM_FILM_NAME, aRentalDays().withRealDaysRented(2).withOriginalDaysRented(1).build())))
                .post("/return?customer={name}", CUSTOMER_ONE_NAME)
                .then()
                .statusCode(HTTP_OK);
    }
}
