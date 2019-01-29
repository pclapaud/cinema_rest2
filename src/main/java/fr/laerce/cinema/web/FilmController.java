package fr.laerce.cinema.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.laerce.cinema.dao.*;
import fr.laerce.cinema.model.Film;
import fr.laerce.cinema.model.Film_Tmbd;
import fr.laerce.cinema.model.Person;
import fr.laerce.cinema.model.Play;
import fr.laerce.cinema.service.FilmManager;
import fr.laerce.cinema.service.GenreManager;
import fr.laerce.cinema.service.ImageManager;
import fr.laerce.cinema.service.PersonManager;
import org.apache.commons.io.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

@Controller
@RequestMapping("/film")
public class FilmController {

    @Value("${key}")
    private String apiKey;

    @Autowired
    FilmManager filmManager;

    @Autowired
    PersonManager personManager;

    @Autowired
    GenreManager genreManager;

    @Autowired
    Film_TmbdDao film_tmbdDao;

    @Autowired
    RoleDao roledao;

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
    private long secondsBeforeReset(String value){
        long timestamp = Long.valueOf(stripBraces(value));
        LocalDateTime resetTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();
        return now.until( resetTime, ChronoUnit.SECONDS);
    }

    private String stripBraces(String value){
        return value.substring(0, value.length()-1).substring(1);
    }
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
    @GetMapping("/A/")
    public String modfilm2(@RequestParam("id") long id, Model model) {

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response;
        long reset;

        String resourceUrl = "https://api.themoviedb.org/3/movie/"+id+"?api_key="+apiKey+"&language=fr-FR";
        response = template.getForEntity(resourceUrl, String.class);
        Film filma = new Film();
        JSONObject film = new JSONObject(response.getBody());
        if (filmManager.getByIdtmbd(film.getBigInteger("id"))==null){
            JSONArray genres = (JSONArray) film.get("genres");
            filma.setTitle(film.getString("title"));
            filma.setIdtmbd(film.getBigInteger("id"));
            filma.setSummary(film.getString("overview"));
            String ann = film.optString("release_date");
            if (ann.length()!=0){
                filma.setReleaseDate(LocalDate.parse(ann));
            }
            String filename = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/"+film.optString("poster_path");
            try {
                InputStream is = new URL(filename).openStream();
                imm.savePoster(filma,is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            filma.setRating(film.getDouble("popularity"));
            filma = filmManager.save2(filma);
            for(int i = 0; i < genres.length(); i++){
                JSONObject genre = (JSONObject) genres.get(i);
                System.out.println("- Genre : "+genre.getString("name"));
            }
            System.out.println("--------\nRequetes restantes : "+stripBraces(response.getHeaders().get("x-ratelimit-remaining").toString()));
            reset = secondsBeforeReset(response.getHeaders().get("x-ratelimit-reset").toString());
            System.out.println("Temps restant avant reset : "+reset+"\n\n");
        }



        String resourceCredit = "https://api.themoviedb.org/3/movie/"+id+"/credits?api_key="+apiKey+"&language=fr-FR";
        response = template.getForEntity(resourceCredit, String.class);
        JSONObject credit = new JSONObject(response.getBody());
        JSONArray cast = (JSONArray) credit.get("cast");
        for (int i = 0; i < cast.length(); i++ ) {
            JSONObject role = (JSONObject) cast.get(i);
            int stringId = role.getInt("id");
            String resourceCreditcast = "https://api.themoviedb.org/3/person/"+stringId+"?api_key="+apiKey+"&language=fr-FR";
            ResponseEntity<String> responsecast = template.getForEntity(resourceCreditcast, String.class);
            JSONObject person = new JSONObject(responsecast.getBody());
            Person personne = new Person();
            String ann2 = person.optString("birthday");
            if (ann2.length()!=0){
                personne.setBirthday(LocalDate.parse(ann2));
            }
            String filename2 = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/"+person.optString("profile_path");
            try {
                InputStream is2 = new URL(filename2).openStream();
                imm.savePhoto(personne,is2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            personne.setName(person.optString("name"));
            personne.setIdtmbd(BigInteger.valueOf(role.optInt("id")));
            personne = personManager.save(personne);
            Play play = new Play();
            play.setName(role.optString("character"));
            play.setRank(role.optInt("order"));
            play.setActor(personne);
            play.setFilm(filma);
            play = roledao.save(play);
        }
        JSONArray crew = (JSONArray) credit.get("crew");
        for (int i = 0; i < crew.length(); i++ ) {
            JSONObject role = (JSONObject) crew.get(i);
            if (role.getString("job").equals("Director")) {
                int stringId = role.getInt("id");
                String resourceCreditcast = "https://api.themoviedb.org/3/person/" + stringId + "?api_key=" + apiKey + "&language=fr-FR";
                ResponseEntity<String> responsecast = template.getForEntity(resourceCreditcast, String.class);
                JSONObject person = new JSONObject(responsecast.getBody());
                Person personne = new Person();
                String ann3 = person.optString("birthday");
                if (ann3.length() != 0) {
                    personne.setBirthday(LocalDate.parse(ann3));
                }
                String filename3 = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + person.optString("profile_path");
                try {
                    InputStream is = new URL(filename3).openStream();
                    imm.savePhoto(personne, is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                personne.setName(person.optString("name"));
                personne.setIdtmbd(BigInteger.valueOf(role.optInt("id")));
                personne = personManager.save(personne);
                filma.setDirector(personne);
            }
        }
        System.out.println("--------\nRequetes restantes : "+stripBraces(response.getHeaders().get("x-ratelimit-remaining").toString()));
        reset = reset = secondsBeforeReset(response.getHeaders().get("x-ratelimit-reset").toString());
        System.out.println("Temps restant avant reset : "+reset+"\n\n");


        Iterable<Film> films = filmManager.getAll();
        Iterable<Film_Tmbd> filmsTmbd = film_tmbdDao.findAll();
        model.addAttribute("films", films);
        model.addAttribute("filmstmbd", filmsTmbd);
        return "film/list";
    }
}


