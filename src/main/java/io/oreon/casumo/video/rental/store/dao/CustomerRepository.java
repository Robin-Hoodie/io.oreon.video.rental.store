package io.oreon.casumo.video.rental.store.dao;

import io.oreon.casumo.video.rental.store.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByName(String name);

    Boolean existsByName(String name);
}
