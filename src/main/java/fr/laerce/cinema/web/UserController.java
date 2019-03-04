package fr.laerce.cinema.web;



import fr.laerce.cinema.dao.GroupDao;
import fr.laerce.cinema.model.User;
import fr.laerce.cinema.service.JpaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("")
public class UserController {
    @Autowired
    private HttpSession httpSession;

    GroupDao groupDao;
    JpaUserService jpaUserService;

    public UserController(GroupDao groupDao, JpaUserService jpaUserService) {
        this.jpaUserService = jpaUserService;
        assert(jpaUserService != null);
        this.groupDao = groupDao;
        assert(groupDao != null);
    }
    @GetMapping("")
    public String home2(){

        return "redirect:/film/list";
    }
    @GetMapping("/profil/{name}")
    public String home2(Model model, @PathVariable("name") String name){
        model.addAttribute("userU",jpaUserService.findByUserName(name));
        model.addAttribute("users",jpaUserService.findAll());
        return "profil";
    }
    @GetMapping("/editpassword/{id}")
    public String modpasswordForm(Model model, @PathVariable("id")long id){
        model.addAttribute("user",jpaUserService.findByUserId(id));
        model.addAttribute("idUser",id);
        return "editpassword";
    }
    @GetMapping("/mod")
    public String modpassword(@RequestParam("password") String password, @RequestParam("password1") String password1, @RequestParam("password2") String password2){
        //on check que les 2 nouveau mdp sont les meme
        if (password1.equals(password2)){
        //on recup l'utilisateur dans la session
        User user = jpaUserService.findByUserName((String)httpSession.getAttribute("userName"));
            //on check que l'ancien mdp est le meme que celui de la bdd
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(password, user.getPassword())){
            //on change le mdp
            user.setPassword(password1);
            //on sauvegarde
            jpaUserService.save(user);
        }

    }
        return "redirect:/home";
    }
    @GetMapping("/mod2")
    public String modpasswordAdmin(@RequestParam("id") long id, @RequestParam("password1") String password1, @RequestParam("password2") String password2){
        if (password1.equals(password2)){
            User user = jpaUserService.findByUserId(id);
            user.setPassword(password1);
            jpaUserService.save(user);
        }
        return "redirect:/profil";
    }

}


