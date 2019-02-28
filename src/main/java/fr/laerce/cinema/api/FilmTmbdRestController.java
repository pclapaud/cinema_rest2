package fr.laerce.cinema.api;

import fr.laerce.cinema.dao.FilmDao;
import fr.laerce.cinema.dao.FilmTmbdDao;
import fr.laerce.cinema.dao.GenreDao;
import fr.laerce.cinema.dao.RoleDao;
import fr.laerce.cinema.model.*;
import fr.laerce.cinema.service.FilmManager;
import fr.laerce.cinema.service.ImageManager;
import fr.laerce.cinema.service.PersonManager;
import fr.laerce.cinema.service.TmdbManager;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/film")
public class FilmTmbdRestController {
    private FilmManager filmManager;
    private PersonManager personManager;
    private GenreDao genredao;
    private RoleDao roledao;
    private FilmDao filmDao;
    private TmdbManager tmdbManager;
    ImageManager imm;

    public FilmTmbdRestController(TmdbManager tmdbManager,FilmManager filmManager, PersonManager personManager, GenreDao genredao, RoleDao roledao, FilmDao filmDao, ImageManager imm){
        assert(filmManager != null);
        this.filmManager = filmManager;
        this.personManager = personManager;
        this.genredao = genredao;
        this.roledao = roledao;
        this.filmDao = filmDao;
        this.tmdbManager = tmdbManager;
        this.imm = imm;
    }
    @PostMapping("/{title}/{page}")
    public Page<FilmTmbd> remove(@PathVariable("title") String title,@PathVariable("page") String page){
        Page<FilmTmbd> resultat = tmdbManager.findAllByTitle(title,Integer.parseInt(page)-1);
        return resultat;
    }
}
