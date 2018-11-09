package io.oreon.casumo.video.rental.store.service;

import io.oreon.casumo.video.rental.store.dao.CustomerRepository;
import io.oreon.casumo.video.rental.store.dao.FilmRepository;
import io.oreon.casumo.video.rental.store.model.Customer;
import io.oreon.casumo.video.rental.store.model.Film;
import io.oreon.casumo.video.rental.store.model.FilmType;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Named
public class CustomerService {

    private CustomerRepository customerRepository;

    private FilmRepository filmRepository;

    public CustomerService(CustomerRepository customerRepository, FilmRepository filmRepository) {
        this.customerRepository = customerRepository;
        this.filmRepository = filmRepository;
    }

    @Transactional
    public int awardBonusPointsToCustomer(String customerName, Set<String> filmNames) {
        Customer customer = this.customerRepository.findByName(customerName)
                .orElseThrow(() -> new IllegalStateException("Customer with name " + customerName + " was not found! This should have been validated already"));
        int newBonusPoints = filmNames.stream()
                .map(filmName -> this.filmRepository.findByName(filmName)
                        .orElseThrow(() -> new IllegalStateException("Film with name " + filmName + " was not found! This should have been validated already")))
                .map(Film::getType)
                .map(FilmType::getBonusPoints)
                .reduce(customer.getBonusPoints(), (currentPoints, extraPoints) -> currentPoints + extraPoints);
        customer.setBonusPoints(newBonusPoints);
        return newBonusPoints;
    }
}
