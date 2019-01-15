package fr.laerce.cinema.web;

import fr.laerce.cinema.dao.FilmDao;
import fr.laerce.cinema.dao.GenreDao;
import fr.laerce.cinema.dao.PersonDao;
import fr.laerce.cinema.dao.RoleDao;
import fr.laerce.cinema.model.Film;
import fr.laerce.cinema.model.Play;
import fr.laerce.cinema.service.FilmManager;
import fr.laerce.cinema.service.GenreManager;
import fr.laerce.cinema.service.ImageManager;
import fr.laerce.cinema.service.PersonManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/film")
public class FilmController {


    @Autowired
    FilmManager filmManager;

    @Autowired
    PersonManager personManager;

    @Autowired
    GenreManager genreManager;



    @Autowired
    ImageManager imm;

    @GetMapping("/list")
    public String list(Model model) {
        Iterable<Film> films = filmManager.getAll();
        model.addAttribute("films", films);
        return "film/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") long id, Model model) {
        model.addAttribute("film", filmManager.getById(id));
        return "film/detail";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("title", "Ajout d'un film");
        model.addAttribute("film", new Film());


        return "film/form";
    }

    @GetMapping("/mod/{id}")
    public String mod(@PathVariable("id") long id, Model model) {
        Film film = filmManager.getById(id);
        model.addAttribute("title", film.getTitle() + " : modification");
        model.addAttribute("persons", personManager.getAll());
        model.addAttribute("genresFilm", genreManager.getAll());
        model.addAttribute("film", film);
        model.addAttribute("plays", film.getRoles());
        model.addAttribute("newrole", new Play());
        model.addAttribute("test", 1);
        return "film/form";
    }
    @GetMapping("/details/{id}")
    public String detailFilm(Model model, @PathVariable("id")long id){
        model.addAttribute("film", filmManager.getById(id));
        model.addAttribute("persons", personManager.getAll());
        model.addAttribute("roles", filmManager.getById(id).getRoles());
        model.addAttribute("genres",genreManager.getAll());
        model.addAttribute("newrole",new Play());
        return "Film/detailsFilm";
    }


    @PostMapping("/add")
    public String submit(@ModelAttribute Film film) {
        filmManager.save(film);
        return "redirect:list";
    }

    @GetMapping("/rmrole/{role_id}")
    public String rmRole(@PathVariable("role_id") Long roleId) {
        long filmId = filmManager.removeRole(roleId);

        return "redirect:/film/mod/" + filmId;
    }

    @PostMapping("/addrole")
    public String addRole(@ModelAttribute Play role) {
        long filmId = role.getFilm().getId();
        filmManager.addRole(filmId, role);
        return "redirect:/film/mod/" + filmId;
    }

    @PostMapping("/modrole/{id}")
    public String modRole(@PathVariable("id") long roleId, @ModelAttribute Play role) {
        filmManager.saveRole(role);
        return "redirect:/film/mod/" + role.getFilm().getId();
    }


}
