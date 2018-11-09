package io.oreon.casumo.video.rental.store.rest;

import io.oreon.casumo.video.rental.store.dao.CustomerRepository;
import io.oreon.casumo.video.rental.store.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/customers")
public class CustomerResource {

    private CustomerRepository customerRepository;

    public CustomerResource(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping(value = "", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Customer>> customers() {
        return ok(this.customerRepository.findAll());
    }
}
