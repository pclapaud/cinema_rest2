package fr.laerce.cinema.service;

import fr.laerce.cinema.dao.FilmDao;
import fr.laerce.cinema.dao.PersonDao;
import fr.laerce.cinema.dao.RoleDao;
import fr.laerce.cinema.model.Film;
import fr.laerce.cinema.model.Person;
import fr.laerce.cinema.model.Play;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Component
public class RoleManager {
    @Autowired
    RoleDao roleDao;

//    public Film findById(long id){
//        return filmDao.findById(id).get();
//    }
//    public Film findByIdtmbd(BigInteger id){
//        return filmDao.findByIdtmbd(id);
//    }
    public Play save(Play play){
        return roleDao.save(play);
    }
    public boolean existsByNameAndActorAndFilm(String name, Person actor, Film film){return roleDao.existsByNameAndActorAndFilm(name,actor,film);}
}
