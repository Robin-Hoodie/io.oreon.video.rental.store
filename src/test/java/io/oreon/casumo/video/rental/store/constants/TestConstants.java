package io.oreon.casumo.video.rental.store.constants;

import io.oreon.casumo.video.rental.store.model.Customer;
import io.oreon.casumo.video.rental.store.model.Film;

import static io.oreon.casumo.video.rental.store.model.Customer.Builder.aCustomer;
import static io.oreon.casumo.video.rental.store.model.Film.Builder.aFilm;
import static io.oreon.casumo.video.rental.store.model.FilmType.OLD;
import static io.oreon.casumo.video.rental.store.model.FilmType.PREMIUM;
import static io.oreon.casumo.video.rental.store.model.FilmType.REGULAR;

public class TestConstants {

    public static final String PREMIUM_FILM_NAME = "premiumFilm";
    public static final String REGULAR_FILM_NAME = "regularFilm";
    public static final String OLD_FILM_NAME = "oldFilm";

    public static final String NON_EXISTING_FILM_NAME = "nonexisting";
    public static final String NON_EXISTING_CUSTOMER_NAME = "nonexisting";

    public static final String CUSTOMER_ONE_NAME = "customerOne";

    public static final Film PREMIUM_FILM = aFilm().withName(PREMIUM_FILM_NAME).withType(PREMIUM).build();
    public static final Film REGULAR_FILM = aFilm().withName(REGULAR_FILM_NAME).withType(REGULAR).build();
    public static final Film OLD_FILM = aFilm().withName(OLD_FILM_NAME).withType(OLD).build();

    public static final Customer CUSTOMER_ONE = aCustomer().withName(CUSTOMER_ONE_NAME).build();

}
