package io.oreon.casumo.video.rental.store.service;

import io.oreon.casumo.video.rental.store.SpringIntegrationTest;
import io.oreon.casumo.video.rental.store.dao.CustomerRepository;
import io.oreon.casumo.video.rental.store.helper.CustomerHelper;
import io.oreon.casumo.video.rental.store.helper.FilmHelper;
import io.oreon.casumo.video.rental.store.model.Customer;
import org.junit.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.HashSet;

import static io.oreon.casumo.video.rental.store.constants.TestConstants.*;
import static io.oreon.casumo.video.rental.store.model.Customer.Builder.aCustomer;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;


public class CustomerServiceTest extends SpringIntegrationTest {

    @Inject
    private CustomerService customerService;

    @Inject
    private CustomerHelper customerHelper;

    @Inject
    private FilmHelper filmHelper;

    @Inject
    private CustomerRepository customerRepository;

    @Test //2 for premium film, 1 for regular film and 1 for old film (2 + 1 + 1)
    public void customerShouldBeAwarded4BonusPoints() {
        this.customerHelper.storeCustomers();
        this.filmHelper.storeFilms();
        this.customerService.awardBonusPointsToCustomer(CUSTOMER_ONE_NAME, new HashSet<>(asList(PREMIUM_FILM_NAME, REGULAR_FILM_NAME, OLD_FILM_NAME)));
        assertThat(this.customerRepository.findByName(CUSTOMER_ONE_NAME))
                .hasValueSatisfying(customer -> assertThat(customer.getBonusPoints()).isEqualTo(4));
    }

    @Test //Starting with 6, with extra 2 for premium film, 1 for regular film and 1 for old film (6 + 2 + 1 + 1)
    public void customerShouldBeAwarded4BonusPointsForATotalOf10() {
        String customerName = "customerWith6BonusPoints";;
        this.customerRepository.save(aCustomer().withName(customerName).withBonusPoints(6).build());
        this.filmHelper.storeFilms();
        this.customerService.awardBonusPointsToCustomer(customerName, new HashSet<>(asList(PREMIUM_FILM_NAME, REGULAR_FILM_NAME, OLD_FILM_NAME)));
        assertThat(this.customerRepository.findByName(customerName))
                .hasValueSatisfying(customer -> assertThat(customer.getBonusPoints()).isEqualTo(10));
    }
}
