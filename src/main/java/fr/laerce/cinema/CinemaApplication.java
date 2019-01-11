package fr.laerce.cinema;

import fr.laerce.cinema.dao.FilmDao;
import fr.laerce.cinema.model.Film;
import fr.laerce.cinema.model.Play;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.List;

@SpringBootApplication
public class CinemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

  /* @Bean

    public CommandLineRunner runner(FilmDao dao){
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                List<Film> films = dao.getAll();
                for (Film film: films
                     ) {
                    System.out.println("Titre : "+film.getTitle()+" Réalisateur "+film.getDirector().getSurname());
                    List<Play> roles = film.getRoles();
                    for (Play role:  roles
                         ) {
                        System.out.println(role.getKey().getPerson().getSurname()+" joue le rôle de "+role.getName());
                    }
                }

            }
        };
    }*/

}

