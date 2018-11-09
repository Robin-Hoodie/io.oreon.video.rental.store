package io.oreon.casumo.video.rental.store.service;

import io.oreon.casumo.video.rental.store.dao.FilmRepository;
import io.oreon.casumo.video.rental.store.model.Film;
import io.oreon.casumo.video.rental.store.rest.dto.RentalDays;

import javax.inject.Named;
import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

import static java.math.BigDecimal.ZERO;

@Named
public class FilmRentalPriceService {

    private static final int OLD_PRICE = 30;
    private static final int REGULAR_PRICE = 30;
    private static final int PREMIUM_PRICE = 40;
    
    private FilmRepository filmRepository;
    private CurrencyService currencyService;

    private Function<String, IllegalStateException> filmNotFoundException = (filmName) -> new IllegalStateException("Film with name " + filmName + " was not found! This should have been validated already");

    public FilmRentalPriceService(FilmRepository filmRepository, CurrencyService currencyService) {
        this.filmRepository = filmRepository;
        this.currencyService = currencyService;
    }

    public String totalPriceForFilms(Map<String, Integer> filmNameToDaysToRent) {
        BigDecimal totalPrice = filmNameToDaysToRent
                .entrySet()
                .stream()
                .map(entry -> priceForFilm(entry.getKey(), entry.getValue()))
                .reduce(ZERO, BigDecimal::add);
        return this.currencyService.formatInSEK(totalPrice);
    }

    public String calculateSurCharges(Map<String, RentalDays> filmNameToRentalDays) {
        BigDecimal surCharges = filmNameToRentalDays
                .entrySet()
                .stream()
                .map(entry -> surChargeForFilm(entry.getKey(), entry.getValue()))
                .reduce(ZERO, BigDecimal::add);
        return this.currencyService.formatInSEK(surCharges);
    }

    protected BigDecimal surChargeForFilm(String filmName, RentalDays rentalDays) {
        if (rentalDays.getRealDaysRented() <= rentalDays.getOriginalDaysRented()) {
            return ZERO;
        }
        return this.filmRepository
                .findByName(filmName)
                .map(film -> surChargeForFilm(film, rentalDays.getRealDaysRented() - rentalDays.getOriginalDaysRented()))
                .orElseThrow(() -> filmNotFoundException.apply(filmName));
    }

    protected BigDecimal priceForFilm(String filmName, int daysRented) {
        return this.filmRepository
                .findByName(filmName)
                .map(film -> priceForFilm(film, daysRented))
                .orElseThrow(() -> filmNotFoundException.apply(filmName));
    }

    protected BigDecimal surChargeForFilm(Film film, int extraDays) {
        switch (film.getType()) {
            case PREMIUM:
                return new BigDecimal(PREMIUM_PRICE * extraDays);
            case REGULAR:
                return new BigDecimal(REGULAR_PRICE * extraDays);
            case OLD:
                return new BigDecimal(OLD_PRICE * extraDays);
            default:
                throw new IllegalArgumentException("Could not calculate price for filmType: " + film.getType());
        }
    }

    protected BigDecimal priceForFilm(Film film, int daysRented) {
        switch (film.getType()) {
            case PREMIUM:
                return calculatePriceForPremium(daysRented);
            case REGULAR:
                return calculatePriceForRegular(daysRented);
            case OLD:
                return calculatePriceForOld(daysRented);
            default:
                throw new IllegalArgumentException("Could not calculate price for filmType: " + film.getType());
        }
    }

    private BigDecimal calculatePriceForPremium(int daysRented) {
        return new BigDecimal(daysRented * PREMIUM_PRICE);
    }

    private BigDecimal calculatePriceForRegular(int daysRented) {
        BigDecimal basicPrice = new BigDecimal(REGULAR_PRICE);
        if (daysRented > 3) {
            BigDecimal extraPrice = new BigDecimal(REGULAR_PRICE * (daysRented - 3));
            return basicPrice.add(extraPrice);
        }
        return new BigDecimal(REGULAR_PRICE);
    }

    private BigDecimal calculatePriceForOld(int daysRented) {
        BigDecimal basicPrice = new BigDecimal(OLD_PRICE);
        if (daysRented > 5) {
            BigDecimal extraPrice = new BigDecimal(REGULAR_PRICE * (daysRented - 5));
            return basicPrice.add(extraPrice);
        }
        return new BigDecimal(REGULAR_PRICE);
    }
}
