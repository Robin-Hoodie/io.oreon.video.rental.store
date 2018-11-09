package io.oreon.casumo.video.rental.store.service;

import io.oreon.casumo.video.rental.store.SpringIntegrationTest;
import org.junit.Test;

import javax.inject.Inject;
import java.math.BigDecimal;

import static io.oreon.casumo.video.rental.store.constants.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;


public class FilmRentalPriceServiceTest extends SpringIntegrationTest {

    @Inject
    private FilmRentalPriceService filmRentalPriceService;

    @Test
    public void priceForPremiumRentedFor5DaysShouldBe200() {
        assertThat(filmRentalPriceService.priceForFilm(PREMIUM_FILM, 5)).isEqualTo(new BigDecimal(200));
    }

    @Test
    public void priceForRegularRentedFor3DaysShouldBe30() {
        assertThat(filmRentalPriceService.priceForFilm(REGULAR_FILM, 3)).isEqualTo(new BigDecimal(30));
    }

    @Test
    public void priceForRegularRentedFor7DaysShouldBe150() {
        assertThat(filmRentalPriceService.priceForFilm(REGULAR_FILM, 7)).isEqualTo(new BigDecimal(150));
    }

    @Test
    public void priceForOldRentedFor5DaysShouldBe30() {
        assertThat(filmRentalPriceService.priceForFilm(OLD_FILM, 3)).isEqualTo(new BigDecimal(30));
    }

    @Test
    public void priceForOldRentedFor7DaysShouldBe90() {
        assertThat(filmRentalPriceService.priceForFilm(OLD_FILM, 7)).isEqualTo(new BigDecimal(90));
    }
}
