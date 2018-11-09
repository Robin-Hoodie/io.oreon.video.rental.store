package io.oreon.casumo.video.rental.store.service;

import javax.inject.Named;
import java.math.BigDecimal;

@Named
public class CurrencyService {

    public String formatInSEK(BigDecimal price) {
        return formatInCurrency(price, Currency.SEK);
    }

    private String formatInCurrency(BigDecimal price, Currency currency) {
        return price.toString() + " " + currency;
    }

    private enum Currency {
        SEK
    }
}
