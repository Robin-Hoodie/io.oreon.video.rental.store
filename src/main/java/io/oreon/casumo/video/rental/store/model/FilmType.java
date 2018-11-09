package io.oreon.casumo.video.rental.store.model;

public enum FilmType {
    PREMIUM(2), REGULAR(1), OLD(1);

    private int bonusPoints;

    FilmType(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }
}
