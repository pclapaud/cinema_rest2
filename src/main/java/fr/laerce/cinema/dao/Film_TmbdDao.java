package fr.laerce.cinema.dao;

import fr.laerce.cinema.model.Film_Tmbd;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Film_TmbdDao extends CrudRepository<Film_Tmbd, Integer> {

    public Film_Tmbd findByIdtmbd(Integer id);

}
