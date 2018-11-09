package io.oreon.casumo.video.rental.store.helper;

import io.oreon.casumo.video.rental.store.dao.CustomerRepository;
import io.oreon.casumo.video.rental.store.dao.FilmRepository;

import javax.inject.Inject;
import javax.inject.Named;

import static io.oreon.casumo.video.rental.store.constants.TestConstants.*;

@Named
public class CustomerHelper {
    @Inject
    private CustomerRepository customerRepository;

    public void storeCustomers() {
        this.customerRepository.save(CUSTOMER_ONE);
    }
}
