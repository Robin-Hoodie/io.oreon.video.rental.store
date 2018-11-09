package io.oreon.casumo.video.rental.store.rest.validation;

import io.oreon.casumo.video.rental.store.dao.CustomerRepository;
import io.oreon.casumo.video.rental.store.dao.FilmRepository;
import io.oreon.casumo.video.rental.store.rest.dto.RentalDays;
import io.oreon.casumo.video.rental.store.rest.exception.APIException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Named
public class RequestValidator {
    public static final String CUSTOMER_ERORR_KEY = "customer";
    public static final String DAYS_RENTED_ERROR_KEY = "daysRented";
    public static final String RENTAL_DAYS_ERROR_KEY = "rentalDays";
    public static final String FILM_ERROR_KEY = "film";

    private FilmRepository filmRepository;

    private CustomerRepository customerRepository;

    @Inject
    public RequestValidator(FilmRepository filmRepository, CustomerRepository customerRepository) {
        this.filmRepository = filmRepository;
        this.customerRepository = customerRepository;
    }

    public void validateRentFilms(Map<String, Integer> filmNameToDaysToRent) {
        Map<String, List<String>> rentFilmsErrors = new HashMap<>();
        errorsForProperty(filmNameToDaysToRent.keySet(), errorForFilmName()).ifPresent(errors -> rentFilmsErrors.put(FILM_ERROR_KEY, errors));
        errorsForProperty(filmNameToDaysToRent.values(), errorForDaysRented()).ifPresent(errors -> rentFilmsErrors.put(DAYS_RENTED_ERROR_KEY, errors));
        if (!rentFilmsErrors.isEmpty()) {
            throw new APIException(rentFilmsErrors);
        }
    }

    public void validateReturnFilm(String customerName, Map<String, RentalDays> filmNameToRentalDays) {
        Map<String, List<String>> returnFilmsErrors = new HashMap<>();
        errorForCustomerName(customerName)
                .ifPresent(error -> returnFilmsErrors.put(CUSTOMER_ERORR_KEY, singletonList(error)));
        errorsForProperty(filmNameToRentalDays.keySet(), errorForFilmName())
                .ifPresent(errors -> returnFilmsErrors.put(FILM_ERROR_KEY, errors));
        errorsForProperty(filmNameToRentalDays.values(), errorForRentalDays())
                .ifPresent(errors -> returnFilmsErrors.put(RENTAL_DAYS_ERROR_KEY, errors));
        if (!returnFilmsErrors.isEmpty()) {
            throw new APIException(returnFilmsErrors);
        }
    }

    private <T> Optional<List<String>> errorsForProperty(Collection<T> collection, Function<T, Optional<String>> errorFunction) {
        List<String> errors = new ArrayList<>();
        collection.forEach(element -> errorFunction.apply(element).ifPresent(errors::add));
        return errors.isEmpty() ? empty() : of(errors);
    }

    private Optional<String> errorForCustomerName(String name) {
        return this.customerRepository.existsByName(name) ? empty() : of("Customer with name " + name + " doesn't exist!");
    }

    private Function<Integer, Optional<String>> errorForDaysRented() {
        return daysRented -> daysRented >= 0 ? empty() : of("Days rented can't be negative! Days rented was " + daysRented);
    }

    private Function<String, Optional<String>> errorForFilmName() {
        return name -> this.filmRepository.existsByName(name) ? empty() : of("Film with name " + name + " doesn't exist!");
    }

    private Function<RentalDays, Optional<String>> errorForRentalDays() {
        return rentalDays -> rentalDays.getOriginalDaysRented() > 0 && rentalDays.getRealDaysRented() > 0 ?
                empty() : of("originalDaysRented and realDaysRented should be positive!");

    }
}
