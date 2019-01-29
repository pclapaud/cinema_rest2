package fr.laerce.cinema.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.laerce.cinema.dao.*;
import fr.laerce.cinema.model.Film;
import fr.laerce.cinema.model.Film_Tmbd;
import fr.laerce.cinema.model.Play;
import fr.laerce.cinema.service.FilmManager;
import fr.laerce.cinema.service.GenreManager;
import fr.laerce.cinema.service.ImageManager;
import fr.laerce.cinema.service.PersonManager;
import org.apache.commons.io.FileUtils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

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
    Film_TmbdDao film_tmbdDao;


    @Autowired
    ImageManager imm;

    @GetMapping("/list")
    public String list(Model model) {
        Iterable<Film> films = filmManager.getAll();
        Iterable<Film_Tmbd> filmsTmbd = film_tmbdDao.findAll();
        model.addAttribute("films", films);
        model.addAttribute("filmstmbd", filmsTmbd);
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
    public String modRole(@PathVariable("id") long roleId, @ModelAttribute Play role) {
        filmManager.saveRole(role);
        return "redirect:/film/mod/" + role.getFilm().getId();
    }
    @GetMapping("/peupler")
    public String modRole( Model model) {
        try {
            LocalDate date = LocalDate.now().minusDays(1);
            String day = String.format("%02d",date.getDayOfMonth());
            String month = String.format("%02d",date.getMonthValue());
            String year = String.valueOf(date.getYear());
            String filename = "http://files.tmdb.org/p/exports/movie_ids_"+month+"_"+day+"_"+year+".json.gz";
            File file = new File("tampom");
            FileUtils.copyURLToFile(new URL(filename), file);
            InputStream is = new FileInputStream(file);
            InputStream gz = new GZIPInputStream(is);
            InputStream bufferedIS = new BufferedInputStream(gz);
            BufferedReader rd = new BufferedReader(new InputStreamReader(bufferedIS, StandardCharsets.UTF_8));
            String line;
            while((line = rd.readLine()) !=null){
                JSONObject test = new JSONObject(line);
                int id = Integer.parseInt(test.get("id").toString());
                if (!test.getBoolean("adult") && film_tmbdDao.findByIdtmbd(id)==null){
                    String title = test.get("original_title").toString();
                    Film_Tmbd film = new Film_Tmbd();
                    film.setIdtmbd(id);
                    film.setTitle(title);
                    film_tmbdDao.save(film);
                }
            }




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Iterable<Film> films = filmManager.getAll();
        model.addAttribute("films", films);
        return "film/list";
    }
    @GetMapping("/peupler/{id}")
    public String modRole(@PathVariable("id") long id, Model model) {
        try {
                System.out.println(id);
                String filenameMovie = "https://api.themoviedb.org/3/movie/"+id+"?api_key=e7e21157a30acfe8e589e73e95b74d44&language=fr-FR";
                InputStream isMovie = new URL(filenameMovie).openStream();
                Map<String,List<String>> headers =  new URL(filenameMovie).openConnection().getHeaderFields();
                InputStream bufferedISMovie = new BufferedInputStream(isMovie);
                BufferedReader rdMovie = new BufferedReader(new InputStreamReader(bufferedISMovie, StandardCharsets.UTF_8));

                System.out.println(rdMovie.readLine());
                Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
                for (Map.Entry<String, List<String>> entry : entrySet) {
                    String headerName = entry.getKey();

                    if (headerName.equals("X-RateLimit-Remaining")){
                        System.out.println("Header Name:" + headerName);
                        List<String> headerValues = entry.getValue();
                        for (String value : headerValues) {
                            System.out.print("Header value:" + value);
                        }
                        System.out.println();
                    }
                }
                System.out.println("ok");



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterable<Film> films = filmManager.getAll();
        model.addAttribute("films", films);
        return "film/list";
    }

}
