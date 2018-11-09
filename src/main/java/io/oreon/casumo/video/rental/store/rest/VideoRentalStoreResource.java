package io.oreon.casumo.video.rental.store.rest;


import io.oreon.casumo.video.rental.store.dao.FilmRepository;
import io.oreon.casumo.video.rental.store.model.Film;
import io.oreon.casumo.video.rental.store.rest.dto.RentFilmsResponse;
import io.oreon.casumo.video.rental.store.rest.dto.RentalDays;
import io.oreon.casumo.video.rental.store.rest.dto.ReturnalFilmResponse;
import io.oreon.casumo.video.rental.store.rest.validation.RequestValidator;
import io.oreon.casumo.video.rental.store.service.CustomerService;
import io.oreon.casumo.video.rental.store.service.FilmRentalPriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

import static io.oreon.casumo.video.rental.store.rest.dto.ReturnalFilmResponse.Builder.aReturnalFilmResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;


//Spent around 1 hour so far
@RestController
@RequestMapping("/films")
public class VideoRentalStoreResource {

    private FilmRepository filmRepository;

    private RequestValidator requestValidator;

    private FilmRentalPriceService filmRentalPriceService;

    private CustomerService customerService;

    public VideoRentalStoreResource(FilmRepository filmRepository,
                                    RequestValidator requestValidator,
                                    FilmRentalPriceService filmRentalPriceService,
                                    CustomerService customerService) {
        this.filmRepository = filmRepository;
        this.requestValidator = requestValidator;
        this.filmRentalPriceService = filmRentalPriceService;
        this.customerService = customerService;
    }

    @GetMapping(value = "", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Film>> filmInventory() {
        return ok(this.filmRepository.findAll());
    }

    @PostMapping(value = "/rent", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RentFilmsResponse> rentFilms(@RequestBody Map<String, Integer> filmNameToDaysRented) {
        this.requestValidator.validateRentFilms(filmNameToDaysRented);
        String totalPrice = this.filmRentalPriceService.totalPriceForFilms(filmNameToDaysRented);
        return ok(new RentFilmsResponse(totalPrice));
    }

    @PostMapping(value = "/return", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReturnalFilmResponse> returnFilm(@RequestParam("customer") String customerName,
                                                            @RequestBody Map<String, RentalDays> filmNameToRentalDays) {
        this.requestValidator.validateReturnFilm(customerName, filmNameToRentalDays);
        Integer bonusPoints = this.customerService.awardBonusPointsToCustomer(customerName, filmNameToRentalDays.keySet());
        String surCharges = this.filmRentalPriceService.calculateSurCharges(filmNameToRentalDays);
        return ok(aReturnalFilmResponse()
                .withCustomerName(customerName)
                .withBonusPoints(bonusPoints)
                .withSurCharges(surCharges)
                .build());
    }
}
