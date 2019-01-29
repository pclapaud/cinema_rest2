package fr.laerce.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="film_tmdb")
public class Film_Tmbd {
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
        if (!(o instanceof Film_Tmbd)) return false;
        Film_Tmbd film_tmbd = (Film_Tmbd) o;
        return getId() == film_tmbd.getId() &&
                getIdtmbd() == film_tmbd.getIdtmbd() &&
                Objects.equals(getTitle(), film_tmbd.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getIdtmbd());
    }
}
