package fr.laerce.cinema.dao;

import fr.laerce.cinema.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public interface PersonDao extends CrudRepository<Person, Long> {
    public List<Person> findAllByOrderBySurname();
}
