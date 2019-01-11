package fr.laerce.cinema.web;

import fr.laerce.cinema.dao.FilmDao;
import fr.laerce.cinema.dao.PersonDao;
import fr.laerce.cinema.dao.RoleDao;
import fr.laerce.cinema.model.Person;
import fr.laerce.cinema.model.Play;
import fr.laerce.cinema.service.ImageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping(value = "/person")
public class PersonController {
    @Autowired
    PersonDao personneDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    FilmDao filmDao;

    @Autowired
    ImageManager imm;

    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("persons", personneDao.findAll());
        return "person/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") long id, Model model){
        model.addAttribute("person", personneDao.findById(id).get());
        return "person/detail";
    }

    @GetMapping("/mod/{id}")
    public String mod(@PathVariable("id")long id, Model model){
        model.addAttribute("person", personneDao.findById(id).get());
        model.addAttribute("newrole", new Play());
        return "person/form";
    }

    @GetMapping("/add")
    public String add(Model model){
        Person person = new Person();
        model.addAttribute("person", person);
        return "person/form";
    }

    @PostMapping("/add")
    public String submit(@RequestParam("photo") MultipartFile file, @ModelAttribute Person person){

        if(file.getContentType().equalsIgnoreCase("image/jpeg")){
            try {
                imm.savePhoto(person, file.getInputStream());
            } catch (IOException ioe){
                System.out.println("Erreur lecture : "+ioe.getMessage());
            }
        }
        personneDao.save(person);
        return "redirect:/person/list";
    }

}
