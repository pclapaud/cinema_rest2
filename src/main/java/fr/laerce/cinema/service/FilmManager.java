package fr.laerce.cinema.service;

import fr.laerce.cinema.dao.FilmDao;
import fr.laerce.cinema.dao.GenreDao;
import fr.laerce.cinema.dao.RoleDao;
import fr.laerce.cinema.model.Film;
import fr.laerce.cinema.model.Play;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilmManager {


    private FilmDao filmDao;
    private RoleDao roleDao;

    /**
     * Constructeur utilisé par Spring pour la construction du bean
     * @ param  genre Dao le DAO qui gère le genre dans le système de persistance, ne peut être null
     */
    public FilmManager(FilmDao filmDao,RoleDao roleDao){
        this.filmDao = filmDao;
        this.roleDao = roleDao;

        assert(filmDao != null);
        assert(roleDao != null);
    }



    public Film getById(long id){
        return filmDao.findById(id).get();
    }

    public List<Film> getAll(){
        return filmDao.findAllByOrderByTitle();
    }

    /**
     * Sauvegarder le film
     * @param film le film à créer ou modifier
     * @return l'id du film créé ou modifié
     */
    public Long save(Film film){
        filmDao.save(film);
        return film.getId();
    }
    public Film save2(Film film){
        filmDao.save(film);
        return film;
    }

    /**
     * Supprime un rôle dans un film
     * @param roleId l'identifiant du rôle à supprimer
     * @return l'id du film auquel appartenait le rôle
     */

    public long removeRole(long roleId){
        Play role = roleDao.findById(roleId).get();
        long filmId = role.getFilm().getId();
        Film film = filmDao.findById(filmId).get();
        film.getRoles().remove(role);
        filmDao.save(film);
        roleDao.delete(role);
        return filmId;
    }


    /**
     * Crée un role associé à un film
     * @param filmId l'identifiant du film
     * @param play le role à créer
     * @return l'id du film associé au rôle
     */
    public long addRole(long filmId, Play play){
        Film film = filmDao.findById(filmId).get();
        play.setFilm(film);
        roleDao.save(play);
        return play.getFilm().getId();
    }

    public long saveRole(Play play){
        roleDao.save(play);
        return play.getId();
    }
    public long deleteFilm(Film film){
        filmDao.deleteById(film.getId());
        return film.getId();
    }
}
