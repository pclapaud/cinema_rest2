package fr.laerce.cinema.web;

import fr.laerce.cinema.dao.*;
import fr.laerce.cinema.model.*;
import fr.laerce.cinema.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigInteger;

@Controller
@RequestMapping("/film")
public class FilmController {

    @Value("${key}")
    private String apiKey;

    FilmManager filmManager;
    PersonManager personManager;
    ImageManager imm;
    GenreManager genreManager;
    FilmTmbdDao film_tmbdDao;
    RoleDao roledao;
    TmdbManager tmbdManager;

    public FilmController(TmdbManager tmbdManager,RoleDao roledao,FilmTmbdDao film_tmbdDao,GenreManager genreManager,ImageManager imm,PersonManager personManager,FilmManager filmManager) {
        this.tmbdManager = tmbdManager;
        assert(tmbdManager != null);
        this.roledao = roledao;
        assert(roledao != null);
        this.film_tmbdDao = film_tmbdDao;
        assert(film_tmbdDao != null);
        this.genreManager = genreManager;
        assert(genreManager != null);
        this.imm = imm;
        assert(imm != null);
        this.personManager = personManager;
        assert(personManager != null);
        this.filmManager = filmManager;
        assert(filmManager != null);

    }
    @GetMapping("/")
    public String index() {
        return "redirect:list";
    }
    @GetMapping("/rechercheImportTmdb")
    public String rechercheImportTmdb() {
        return "film/search";
    }
    @GetMapping("/rechercheBDD")
    public String rechercheBDD() {
        return "film/searchBDD";
    }

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
        model.addAttribute("persons", personManager.getAll());
        model.addAttribute("genresFilm", genreManager.getAll());
        model.addAttribute("film", filmManager.save2(new Film()));
        model.addAttribute("newrole", new Play());
        model.addAttribute("test", 0);
        return "film/detailsFilm";
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
    public String modRole(@ModelAttribute Play role) {
        filmManager.saveRole(role);
        return "redirect:/film/mod/" + role.getFilm().getId();
    }
    @GetMapping("/peuplerFilm")
    public String modRole( Model model) {
        tmbdManager.peuplerLesFilms();
        return "redirect:film/list";
    }
    @GetMapping("/peuplerGenre")
    public String modgenre( Model model) {

        tmbdManager.peuplerLesGenres();
        return "redirect:film/list";
    }


    @GetMapping("/importerFilmTmdb/")
    public String modfilm2(RedirectAttributes redi, @RequestParam("id") BigInteger tmbdId) throws InterruptedException {

        long id = tmbdManager.peuplerFilm(tmbdId).getId();
        redi.addFlashAttribute("flashmessage","import: ok");
        return "redirect:/film/details/"+id;
    }
}


