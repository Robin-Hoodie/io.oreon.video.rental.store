package io.oreon.casumo.video.rental.store.dao;

import io.oreon.casumo.video.rental.store.model.Film;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FilmRepository extends CrudRepository<Film, Long> {

    Optional<Film> findByName(String name);

    Boolean existsByName(String name);
}
