package fr.laerce.cinema.service;



import fr.laerce.cinema.dao.FilmTmbdDao;
import fr.laerce.cinema.model.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

@Component
public class TmdbManager {
    @Autowired
    FilmTmbdDao film_tmbdDao;


    @Autowired
    GenreManager genreManager;

    @Autowired
    RoleManager roleManager;

    @Autowired
    FilmManager filmManager;

    @Value("${key}")
    private String apiKey;

    @Autowired
    PersonManager personManager;

    @Autowired
    ImageManager imm;
    public Page<FilmTmbd> findAllByTitle(String title, Integer page){
        Pageable pageable = PageRequest.of(page , 20);
        Page<FilmTmbd> articlePage = film_tmbdDao.findAllByTitleContainingIgnoreCase(title,pageable);
        return articlePage;
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
    public void stopSyteme(String nbrString,long tempRest) throws InterruptedException {
        int nbr = Integer.parseInt(nbrString);
        if (nbr<=1){
            Thread.sleep(tempRest);
            System.out.println("pause de :"+tempRest*1000);
        }
    }
    public Person importPersonne(JSONObject role,Film filma){

        BigInteger BigIntegerId = role.getBigInteger("id");
        RestTemplate template = new RestTemplate();
        Person personne = null;
        if (!personManager.existsByIdtmbd(BigIntegerId)){

            String resourceCreditcast = "https://api.themoviedb.org/3/person/"+BigIntegerId+"?api_key="+apiKey+"&language=fr-FR";
            ResponseEntity<String> responsecast = template.getForEntity(resourceCreditcast, String.class);
            long reset = secondsBeforeReset(responsecast.getHeaders().get("x-ratelimit-reset").toString());
            try {
                stopSyteme(stripBraces(responsecast.getHeaders().get("x-ratelimit-remaining").toString()),reset);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JSONObject person = new JSONObject(responsecast.getBody());
            personne = new Person();
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
            return personManager.save(personne);
        }
        else{
            return personManager.findByIdtmbd(BigIntegerId);
        }

    }

    public Film peuplerFilm(BigInteger id) throws InterruptedException {
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response;
        long reset;

        String resourceUrl = "https://api.themoviedb.org/3/movie/"+id+"?api_key="+apiKey+"&language=fr-FR";
        response = template.getForEntity(resourceUrl, String.class);
        reset = secondsBeforeReset(response.getHeaders().get("x-ratelimit-reset").toString());
        stopSyteme(stripBraces(response.getHeaders().get("x-ratelimit-remaining").toString()),reset);
        Film filma = new Film();
        JSONObject film = new JSONObject(response.getBody());
        if (!filmManager.existsByIdtmbd(film.getBigInteger("id"))){
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
                Genre gen = genreManager.getById(genre.getLong("id"));
                Set<Genre> lesgenres = filma.getGenres();
                if (lesgenres!=null){
                    lesgenres.add(gen);
                    filma.setGenres(lesgenres);
                }
                else {
                    lesgenres = new HashSet<>();
                    lesgenres.add(gen);
                    filma.setGenres(lesgenres);
                }

            }
            filma = filmManager.save2(filma);
            System.out.println("--------\nRequetes restantes : "+stripBraces(response.getHeaders().get("x-ratelimit-remaining").toString()));
            reset = secondsBeforeReset(response.getHeaders().get("x-ratelimit-reset").toString());
            System.out.println("Temps restant avant reset : "+reset+"\n\n");
        }
        else {
            filma = filmManager.getByIdtmbd(id);
        }



        String resourceCredit = "https://api.themoviedb.org/3/movie/"+id+"/credits?api_key="+apiKey+"&language=fr-FR";
        response = template.getForEntity(resourceCredit, String.class);
        reset = secondsBeforeReset(response.getHeaders().get("x-ratelimit-reset").toString());
        stopSyteme(stripBraces(response.getHeaders().get("x-ratelimit-remaining").toString()),reset);
        JSONObject credit = new JSONObject(response.getBody());
        JSONArray cast = (JSONArray) credit.get("cast");
        for (int i = 0; i < cast.length(); i++ ) {
            JSONObject role = (JSONObject) cast.get(i);
            Person personne = importPersonne(role,filma);
            if (!roleManager.existsByNameAndActorAndFilm(role.optString("character"),personne,filma)){
                Play play = new Play();
                play.setName(role.optString("character"));
                play.setRank(role.optInt("order"));
                play.setActor(personne);
                play.setFilm(filma);
                play = roleManager.save(play);
            }

        }
        JSONArray crew = (JSONArray) credit.get("crew");
        for (int i = 0; i < crew.length(); i++ ) {
            JSONObject role = (JSONObject) crew.get(i);
            if (role.getString("job").equals("Director")) {
                Person personne = importPersonne(role,filma);
                filma.setDirector(personne);
                filmManager.save2(filma);

            }
        }
        System.out.println("--------\nRequetes restantes : "+stripBraces(response.getHeaders().get("x-ratelimit-remaining").toString()));
        reset = reset = secondsBeforeReset(response.getHeaders().get("x-ratelimit-reset").toString());
        System.out.println("Temps restant avant reset : "+reset+"\n\n");

        return filma;
    }

    public void peuplerLesGenres(){
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response;


        String filename = "https://api.themoviedb.org/3/genre/movie/list?api_key=e7e21157a30acfe8e589e73e95b74d44&language=fr-FR";
        response = template.getForEntity(filename, String.class);
        JSONObject credit = new JSONObject(response.getBody());
        JSONArray genres = (JSONArray) credit.get("genres");
        for (int i = 0; i < genres.length(); i++ ) {
            JSONObject genre = (JSONObject) genres.get(i);
            Long longId = genre.getLong("id");
            String name = genre.getString("name");
            Genre genree =genreManager.getById(longId);
            System.out.println(genre);

            if (genree == null) {
                genree = new Genre();
                genree.setId(longId);
                genree.setName(name);
                genreManager.save(genree);
            }

        }
    }

    public void peuplerLesFilms() {
        try {
            LocalDate date = LocalDate.now().minusDays(1);
            String day = String.format("%02d", date.getDayOfMonth());
            String month = String.format("%02d", date.getMonthValue());
            String year = String.valueOf(date.getYear());
            String filename = "http://files.tmdb.org/p/exports/movie_ids_" + month + "_" + day + "_" + year + ".json.gz";
            File file = new File("tampom");
            FileUtils.copyURLToFile(new URL(filename), file);
            InputStream is = new FileInputStream(file);
            InputStream gz = new GZIPInputStream(is);
            InputStream bufferedIS = new BufferedInputStream(gz);
            BufferedReader rd = new BufferedReader(new InputStreamReader(bufferedIS, StandardCharsets.UTF_8));
            String line;
            while ((line = rd.readLine()) != null) {
                JSONObject test = new JSONObject(line);
                int id = Integer.parseInt(test.get("id").toString());
                if (!test.getBoolean("adult") && film_tmbdDao.existsByIdtmbd(id)) {
                    String title = test.get("original_title").toString();
                    FilmTmbd film = new FilmTmbd();
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
    }
}
