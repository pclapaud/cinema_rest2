package fr.laerce.cinema.api;

import fr.laerce.cinema.dao.RoleDao;
import fr.laerce.cinema.model.Film;
import fr.laerce.cinema.model.Person;
import fr.laerce.cinema.model.Play;
import fr.laerce.cinema.model.TestModel;
import fr.laerce.cinema.service.FilmManager;
import fr.laerce.cinema.service.PersonManager;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/film")
public class FilmRestController {
    private FilmManager filmManager;
    private PersonManager personManager;
    private RoleDao roleDao;

    public FilmRestController(FilmManager filmManager,PersonManager personManager,RoleDao roleDao){
        assert(filmManager != null);
        this.filmManager = filmManager;
        this.personManager = personManager;
        this.roleDao = roleDao;
    }


    @GetMapping("")
    public List<Film> getAll(){
        return filmManager.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable("id")long id){
        return filmManager.getById(id);
    }

    @PostMapping("")
    public Film add(@RequestBody TestModel test){
        Film film = test.film;
        Person director = personManager.findById(test.director);
        film.setDirector(director);
//        Set<Play> lesRoles = null;
//        for (long role_id:roles_id
//             ) {
//            lesRoles.add(roleDao.findById(role_id).get());
//        }
//        film.setRoles(lesRoles);
        film = filmManager.save2(film);
        return film;
    }
}
