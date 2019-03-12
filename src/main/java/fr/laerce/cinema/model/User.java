package fr.laerce.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * Projet thyme-security
 * Pour LAERCE SAS
 * <p>
 * Créé le  21/03/2017.
 *
 * @author fred
 */
@Entity(name = "User")
@Table(name="user", schema = "public")
public class User {
    private long id;
    private String name;
    private String password;
    private boolean active;
    private String mail;
    private Set<Groups> groups;
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NAME", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "PASSWORD", nullable = false, length = 128)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "ACTIVE", nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Basic
    @Column(name = "MAIL", nullable = false, length = 100)
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name="USER_GROUP",
            joinColumns =@JoinColumn(name = "ID_USER", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ID_GROUP", referencedColumnName = "ID"))
    public Set<Groups> getGroups() {
        return groups;
    }

    public void setGroups(Set<Groups> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                isActive() == user.isActive() &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getMail(), user.getMail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPassword(), isActive(), getMail());
    }
}
