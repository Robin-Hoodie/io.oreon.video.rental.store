package io.oreon.casumo.video.rental.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static java.util.Arrays.stream;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = CasumoVideoRentalStoreApplication.class, webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public abstract class SpringIntegrationTest {
    @Inject
    protected ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @Inject
    private CrudRepository<?, ?>[] crudRepositories;

    @Before
    public void setupRestAssured() {
        RestAssured.port = port;
    }

    @After
    public void cleanCrudRepositories() {
        stream(crudRepositories).forEach(CrudRepository::deleteAll);
    }
}
