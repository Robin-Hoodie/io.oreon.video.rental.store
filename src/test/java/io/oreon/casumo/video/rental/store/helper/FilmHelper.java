package io.oreon.casumo.video.rental.store.helper;

import io.oreon.casumo.video.rental.store.dao.FilmRepository;
import io.oreon.casumo.video.rental.store.model.Film;

import javax.inject.Inject;
import javax.inject.Named;

import static io.oreon.casumo.video.rental.store.constants.TestConstants.OLD_FILM;
import static io.oreon.casumo.video.rental.store.constants.TestConstants.PREMIUM_FILM;
import static io.oreon.casumo.video.rental.store.constants.TestConstants.REGULAR_FILM;
import static java.util.Arrays.asList;

@Named
public class FilmHelper {
    @Inject
    private FilmRepository filmRepository;

    private static final String BODY_START =  "[";
    private static final String BODY_END =  "]";
    private static final String QUOTE = "\"";

    public void storeFilms() {
        this.filmRepository.save(PREMIUM_FILM);
        this.filmRepository.save(REGULAR_FILM);
        this.filmRepository.save(OLD_FILM);
    }
}
