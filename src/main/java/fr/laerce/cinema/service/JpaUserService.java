package fr.laerce.cinema.service;

import fr.laerce.cinema.dao.GroupDao;
import fr.laerce.cinema.dao.UserDao;
import fr.laerce.cinema.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * Projet thyme-security
 * Pour LAERCE SAS
 * <p>
 * Créé le  21/03/2017.
 *
 * @author fred
 */
@Service
public class JpaUserService {
    private UserDao userDao;
    private GroupDao groupDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    @Autowired
    public void setGroupDao(GroupDao groupDao){
        this.groupDao = groupDao;
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setGroups(new HashSet<>(groupDao.findAll()));
        userDao.save(user);
    }

    public User findByUserName(String userName){
        return userDao.findByName(userName);
    }
    public User findByUserId(long id){
        return userDao.findById(id);
    }
    public List<User> findAll(){
        return userDao.findAllByOrderById();
    }

}
