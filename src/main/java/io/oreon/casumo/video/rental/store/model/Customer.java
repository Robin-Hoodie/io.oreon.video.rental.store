package io.oreon.casumo.video.rental.store.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String name;

    private int bonusPoints;

    //Private empty constructor for Hibernate
    private Customer() {}

    private Customer(String name, int bonusPoints) {
        this.name = name;
        this.bonusPoints = bonusPoints;
    }

    public String getName() {
        return name;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bonusPoints=" + bonusPoints +
                '}';
    }

    public static class Builder {
        private String name = "CustomerName";
        private int bonusPoints;

        private Builder() {}

        public static Builder aCustomer() {
            return new Builder();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withBonusPoints(int bonusPoints) {
            this.bonusPoints = bonusPoints;
            return this;
        }

        public Customer build() {
            return new Customer(this.name, this.bonusPoints);
        }
    }
}
