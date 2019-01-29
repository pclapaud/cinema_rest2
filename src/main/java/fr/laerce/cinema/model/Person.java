package fr.laerce.cinema.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "name", nullable = false, length = 60)
    private String name;
    @Basic
    @Column(name = "idtmbd", nullable = false, length = 60)
    private BigInteger idtmbd;

    @Basic
    @Column(name = "birthday", nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Basic
    @Column(name = "image_path", nullable = true, length = 80)
    private String imagePath;

    @OneToMany(mappedBy = "director")
    @JsonIgnore
    private Set<Film> directedFilms;

    @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Play> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String surname) {
        this.name = surname;
    }

   public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthYear) {
        this.birthday = birthYear;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Set<Film> getDirectedFilms() {
        return directedFilms;
    }

    public void setDirectedFilms(Set<Film> films) {
        this.directedFilms = films;
    }

    public BigInteger getIdtmbd() {
        return idtmbd;
    }

    public void setIdtmbd(BigInteger idtmbd) {
        this.idtmbd = idtmbd;
    }

    public Set<Play> getRoles() {
        return roles;
    }

    public void setRoles(Set<Play> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return getId() == person.getId() &&
                Objects.equals(getName(), person.getName()) &&
                Objects.equals(getIdtmbd(), person.getIdtmbd()) &&
                Objects.equals(getBirthday(), person.getBirthday()) &&
                Objects.equals(getImagePath(), person.getImagePath()) &&
                Objects.equals(getDirectedFilms(), person.getDirectedFilms()) &&
                Objects.equals(getRoles(), person.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getIdtmbd(), getBirthday(), getImagePath(), getDirectedFilms(), getRoles());
    }
}
