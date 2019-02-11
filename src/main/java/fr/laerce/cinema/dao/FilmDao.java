package fr.laerce.cinema.dao;

import fr.laerce.cinema.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Repository
public interface FilmDao extends CrudRepository<Film, Long> {
    public List<Film> findAllByOrderByTitle();
    public Film findByIdtmbd(BigInteger id);
    public boolean existsByIdtmbd(BigInteger id);

}
