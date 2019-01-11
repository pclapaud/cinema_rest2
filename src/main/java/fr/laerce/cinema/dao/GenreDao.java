package fr.laerce.cinema.dao;

import fr.laerce.cinema.model.Genre;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository pour la gestion des genres des films
 */
public interface GenreDao extends CrudRepository<Genre, Long> {
    /**
     * La liste des genres par ordre alphabétique croissant
     * @return la liste de genres
     */
    public List<Genre> findAllByOrderByNameAsc();
    public Genre findByName(String name);
}
