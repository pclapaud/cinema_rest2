package fr.laerce.cinema.dao;

import fr.laerce.cinema.model.Play;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends CrudRepository<Play, Long> {
}
