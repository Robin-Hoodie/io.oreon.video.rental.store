package io.oreon.casumo.video.rental.store.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class ReturnalFilmResponse {
    private String customerName;
    private Integer bonusPoints;
    private String surCharges;

    @JsonCreator
    private ReturnalFilmResponse(@JsonProperty("customerName") String customerName, @JsonProperty("bonusPoints") Integer bonusPoints, @JsonProperty("surCharges") String surCharges) {
        this.customerName = customerName;
        this.bonusPoints = bonusPoints;
        this.surCharges = surCharges;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Integer getBonusPoints() {
        return bonusPoints;
    }

    public String getSurCharges() {
        return surCharges;
    }

    public static class Builder {
        private String customerName = "customerName";
        private Integer bonusPoints;
        private String surCharges = "0 SEK";

        public static Builder aReturnalFilmResponse() {
            return new Builder();
        }

        public Builder withCustomerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder withBonusPoints(Integer bonusPoints) {
            this.bonusPoints = bonusPoints;
            return this;
        }

        public Builder withSurCharges(String surCharges) {
            this.surCharges = surCharges;
            return this;
        }

        public ReturnalFilmResponse build() {
            return new ReturnalFilmResponse(this.customerName, this.bonusPoints, this.surCharges);
        }
    }
}
