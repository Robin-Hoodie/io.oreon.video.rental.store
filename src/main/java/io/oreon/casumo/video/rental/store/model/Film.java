package io.oreon.casumo.video.rental.store.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static io.oreon.casumo.video.rental.store.model.FilmType.REGULAR;

@Entity
public class Film {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String name;

    private FilmType type;

    //Private empty constructor for Hibernate
    private Film() {}

    private Film(String name, FilmType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public FilmType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    public static class Builder {
        private String name = "FilmName";
        private FilmType type= REGULAR;

        private Builder() {}

        public static Builder aFilm() {
            return new Builder();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withType(FilmType type) {
            this.type = type;
            return this;
        }

        public Film build() {
            return new Film(this.name, this.type);
        }
    }
}
