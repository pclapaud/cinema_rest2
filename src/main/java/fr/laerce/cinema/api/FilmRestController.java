package fr.laerce.cinema.api;

import fr.laerce.cinema.dao.GenreDao;
import fr.laerce.cinema.dao.RoleDao;
import fr.laerce.cinema.model.*;
import fr.laerce.cinema.service.FilmManager;
import fr.laerce.cinema.service.PersonManager;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/film")
public class FilmRestController {
    private FilmManager filmManager;
    private PersonManager personManager;
    private GenreDao genredao;

    public FilmRestController(FilmManager filmManager,PersonManager personManager,GenreDao genredao){
        assert(filmManager != null);
        this.filmManager = filmManager;
        this.personManager = personManager;
        this.genredao = genredao;
    }


    @GetMapping("")
    public List<Film> getAll() {
        return filmManager.getAll();
    }
    @GetMapping("/{id}")
    public Film getById(@PathVariable("id")long id){
        return filmManager.getById(id);
    }

    @PostMapping("")
    public Film add(@RequestBody Container container){
        Film film = container.film;
        Person director = personManager.findById(container.director);
        film.setDirector(director);
        Set<Genre> lesgenres = new HashSet<>();
        for (long genre_id:container.genres
             ) {
            Genre genre = genredao.findById(genre_id).get();
          lesgenres.add(genre);
        }
        film.setGenres(lesgenres);
        film = filmManager.save2(film);
        return film;
    }
}
