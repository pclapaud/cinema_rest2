package fr.laerce.cinema.dao;

import fr.laerce.cinema.model.Film;
import fr.laerce.cinema.model.Person;
import fr.laerce.cinema.model.Play;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface RoleDao extends CrudRepository<Play, Long> {
    public List<Person> findAllByOrderByName();
    public Play findByNameAndActorAndFilm(String name, Person actor, Film film);
    public boolean existsByNameAndActorAndFilm(String name, Person actor, Film film);

}
