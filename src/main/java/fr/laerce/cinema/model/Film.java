package fr.laerce.cinema.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Film")
@Table(name="films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "title", nullable = true, length = 300)
    private String title;
    @Basic
    @Column(name = "idtmbd")
    private BigInteger idtmbd;
    @Basic
    @Column(name = "rating", nullable = true, precision = 1)
    private double rating;
    @Basic
    @Column(name = "image_path", nullable = true, length = 120)
    private String imagePath;
    @Basic
    @Column(name = "summary", nullable = true, length = -1)
    private String summary;
    @Basic
    @Column(name="release_date", nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @ManyToOne
    @JoinColumn(name ="film_director")
    @JsonIgnore
    private Person director;


    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private Set<Play> roles;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="film_genre", joinColumns = @JoinColumn(name="film_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @JsonIgnore
    private Set<Genre> genres;


    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private Set<Review> reviews;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Person getDirector() {
        return director;
    }

    public void setDirector(Person person) {
        this.director = person;
    }

    public Set<Play> getRoles() {
        return roles;
    }

    public void setRoles(Set<Play> roles) {
        this.roles = roles;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }


    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public BigInteger getIdtmbd() {
        return idtmbd;
    }

    public void setIdtmbd(BigInteger idtmbd) {
        this.idtmbd = idtmbd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return getId() == film.getId() &&
                Double.compare(film.getRating(), getRating()) == 0 &&
                Objects.equals(getTitle(), film.getTitle()) &&
                Objects.equals(getIdtmbd(), film.getIdtmbd()) &&
                Objects.equals(getImagePath(), film.getImagePath()) &&
                Objects.equals(getSummary(), film.getSummary()) &&
                Objects.equals(getReleaseDate(), film.getReleaseDate()) &&
                Objects.equals(getDirector(), film.getDirector()) &&
                Objects.equals(getRoles(), film.getRoles()) &&
                Objects.equals(getGenres(), film.getGenres()) &&
                Objects.equals(getReviews(), film.getReviews());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getIdtmbd(), getRating(), getImagePath(), getSummary(), getReleaseDate());
    }
}
