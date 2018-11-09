package io.oreon.casumo.video.rental.store.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RentFilmsResponse {
    private String totalPrice;

    public RentFilmsResponse(@JsonProperty("totalPrice") String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
}
