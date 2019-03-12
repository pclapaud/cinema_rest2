package fr.laerce.cinema.dao;

import fr.laerce.cinema.model.Film;
import fr.laerce.cinema.model.Review;
import fr.laerce.cinema.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ReviewDao extends CrudRepository<Review, Long> {

    Review findAllByUser(User user);
    Review findAllByUserAndFilm(User user, Film film);
    Page<Review> findAllByFilm(Film film, Pageable pageable);
    List<Review> findAllByFilm(Film film);
}
