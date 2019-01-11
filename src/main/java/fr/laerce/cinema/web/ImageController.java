package fr.laerce.cinema.web;

import fr.laerce.cinema.dao.FilmDao;
import fr.laerce.cinema.dao.PersonDao;
import fr.laerce.cinema.service.ImageManager;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

@Controller()
@RequestMapping(value = "/img")
public class ImageController {
    @Autowired
    FilmDao filmDao;
    @Autowired
    PersonDao personDao;
    @Autowired
    ImageManager imageManager;

    @GetMapping(value = "/poster/film/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[]
    films(@PathVariable("id") Long id) {
        String posterName = filmDao.findById(id).get().getImagePath();
        InputStream is = imageManager.retreivePoster(posterName);
        byte[] image = null;
        try {
            image = IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @GetMapping(value = "/photo/person/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[]
    photos(@PathVariable("id") Long id) {
        String photoName = personDao.findById(id).get().getImagePath();
        InputStream is = imageManager.retreivePhoto(photoName);
        byte[] image = null;
        try {
            image = IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;

    }
}
