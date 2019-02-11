package fr.laerce.cinema.service;

import fr.laerce.cinema.dao.PersonDao;
import fr.laerce.cinema.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class PersonManager {
    @Autowired
    PersonDao personDao;

    public List<Person> getAll(){
        return personDao.findAllByOrderByName();
    }
    public Person findById(long id){
        return personDao.findById(id).get();
    }
    public Person findByIdtmbd(BigInteger id){
        return personDao.findByIdtmbd(id);
    }
    public Person save(Person person){
        return personDao.save(person);
    }
    public boolean existsByIdtmbd(BigInteger id){return personDao.existsByIdtmbd(id);}
}
