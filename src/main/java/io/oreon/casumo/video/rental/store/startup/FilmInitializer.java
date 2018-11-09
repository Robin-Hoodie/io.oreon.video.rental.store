package io.oreon.casumo.video.rental.store.startup;

import io.oreon.casumo.video.rental.store.constants.SpringProfiles;
import io.oreon.casumo.video.rental.store.dao.CustomerRepository;
import io.oreon.casumo.video.rental.store.dao.FilmRepository;
import io.oreon.casumo.video.rental.store.model.Customer;
import io.oreon.casumo.video.rental.store.model.Film;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;

import javax.inject.Inject;
import javax.inject.Named;

import static io.oreon.casumo.video.rental.store.constants.SpringProfiles.DEV;
import static io.oreon.casumo.video.rental.store.model.Customer.Builder.aCustomer;
import static io.oreon.casumo.video.rental.store.model.Film.Builder.aFilm;
import static io.oreon.casumo.video.rental.store.model.FilmType.OLD;
import static io.oreon.casumo.video.rental.store.model.FilmType.PREMIUM;
import static io.oreon.casumo.video.rental.store.model.FilmType.REGULAR;
import static java.util.Arrays.asList;

@Named
@Profile(DEV) //Only run this on DEV environment
public class FilmInitializer implements CommandLineRunner {

    private FilmRepository filmRepository;

    private CustomerRepository customerRepository;

    public FilmInitializer(FilmRepository filmRepository, CustomerRepository customerRepository) {
        this.filmRepository = filmRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        this.filmRepository.deleteAll(); //Make sure DB is in clean state
        Film lotr = aFilm().withName("The Lord of the Rings: The Return of the King").withType(PREMIUM).build();
        Film starWars = aFilm().withName("Star Wars Episode IV: A New Hope").withType(PREMIUM).build();
        Film fightClub = aFilm().withName("Fight Club").withType(PREMIUM).build();
        Film topGun = aFilm().withName("Top Gun").build();
        Film southPark = aFilm().withName("South Park: The Movie").build();
        Film forrestGump = aFilm().withName("Forrest Gump").build();
        Film exorcist = aFilm().withName("The Exorcist").withType(OLD).build();
        Film spaceOdyssey = aFilm().withName("2001: A Space Odyssey").withType(OLD).build();
        Film fullmetalJacket = aFilm().withName("Full Metal Jacket").withType(OLD).build();
        Film montyPython = aFilm().withName("Monty Python and the Holy Grail").withType(OLD).build();
        this.filmRepository.saveAll(asList(lotr, starWars, fightClub, topGun, southPark, forrestGump, exorcist, spaceOdyssey, fullmetalJacket, montyPython));
        Customer gandalf = aCustomer().withName("Gandalf Greybeard").build();
        Customer sam = aCustomer().withName("Samwise Gamgee").build();
        Customer bilbo = aCustomer().withName("Bilbo Baggins").build();
        this.customerRepository.saveAll(asList(gandalf, sam, bilbo));
    }
}
