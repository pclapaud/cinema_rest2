package fr.laerce.cinema.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public class ContainerJsonForFilm {
    public Long director;
    public Film film;
    public List<Long> genres;
    public MultipartFile file;
}
