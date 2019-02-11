package fr.laerce.cinema.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="film_tmdb")
public class FilmTmbd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "title", nullable = true, length = 50)
    private String title;
    @Basic
    @Column(name = "idtmbd", nullable = true, precision = 1)
    private int idtmbd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIdtmbd() {
        return idtmbd;
    }

    public void setIdtmbd(int idtmbd) {
        this.idtmbd = idtmbd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilmTmbd)) return false;
        FilmTmbd film_tmbd = (FilmTmbd) o;
        return getId() == film_tmbd.getId() &&
                getIdtmbd() == film_tmbd.getIdtmbd() &&
                Objects.equals(getTitle(), film_tmbd.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getIdtmbd());
    }
}
