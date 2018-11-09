package io.oreon.casumo.video.rental.store.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;

public class RentalDays {

    private Integer originalDaysRented;
    private Integer realDaysRented;

    @JsonCreator
    private RentalDays(@JsonProperty("originalDaysRented") Integer originalDaysRented, @JsonProperty("realDaysRented") Integer realDaysRented) {
        this.originalDaysRented = originalDaysRented;
        this.realDaysRented = realDaysRented;
    }

    public Integer getOriginalDaysRented() {
        return originalDaysRented;
    }

    public Integer getRealDaysRented() {
        return realDaysRented;
    }

    public static class Builder {
        private Integer originalDaysRented = 1;
        private Integer realDaysRented = 1;

        public static Builder aRentalDays() {
            return new Builder();
        }

        public Builder withOriginalDaysRented(Integer originalDaysRented) {
            this.originalDaysRented = originalDaysRented;
            return this;
        }

        public Builder withRealDaysRented(Integer realDaysRented) {
            this.realDaysRented = realDaysRented;
            return this;
        }

        public RentalDays build() {
            return new RentalDays(this.originalDaysRented, this.realDaysRented);
        }
    }
}
