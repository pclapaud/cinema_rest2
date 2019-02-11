package fr.laerce.cinema.dao;

import fr.laerce.cinema.model.FilmTmbd;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface FilmTmbdDao extends CrudRepository<FilmTmbd, Integer> {

    public FilmTmbd findByIdtmbd(Integer id);
    public boolean existsByIdtmbd(int id);

}
