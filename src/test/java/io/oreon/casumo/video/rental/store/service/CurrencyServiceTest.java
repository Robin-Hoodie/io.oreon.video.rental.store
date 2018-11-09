package io.oreon.casumo.video.rental.store.service;

import io.oreon.casumo.video.rental.store.SpringIntegrationTest;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyServiceTest extends SpringIntegrationTest {

    @Inject
    private CurrencyService currencyService;

    @Test
    public void shouldReturn40SEK() {
        assertThat(this.currencyService.formatInSEK(new BigDecimal(40))).isEqualTo("40 SEK");
    }
}
