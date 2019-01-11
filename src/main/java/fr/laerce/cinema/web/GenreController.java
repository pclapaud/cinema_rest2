package fr.laerce.cinema.web;

import fr.laerce.cinema.model.Genre;
import fr.laerce.cinema.service.GenreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@Controller
@RequestMapping("/genre")
public class GenreController {

    /**
     *
     */
    GenreManager genreManager;

    /**
     *
     * @param genreManager
     */
    public GenreController(GenreManager genreManager){
        this.genreManager = genreManager;
        assert(genreManager != null);
    }

    /**
     *
     * @param model
     * @return
     */
    @GetMapping("")
    public String main(Model model){
        model.addAttribute("genres", genreManager.getAll());
        model.addAttribute("newgenre", new Genre());
        return "genre/form";
    }

}
