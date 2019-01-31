package fr.laerce.cinema.service;

import fr.laerce.cinema.dao.GenreDao;
import fr.laerce.cinema.model.Genre;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Service pour la gestion du Genre des films
 */
@Component
public class GenreManager {

    /**
     * Le DAO qui gère le genre dans le système de persistance
     */
    private GenreDao genreDao;


    /**
     * Constructeur utilisé par Spring pour la construction du bean
     * @param genreDao le DAO qui gère le genre dans le système de persistance, ne peut être null
     */
    public GenreManager(GenreDao genreDao){
        this.genreDao = genreDao;
        assert(genreDao != null);
    }

    /**
     * Obtnir la liste de tous les genres dans la base
     * @return la liste des genres
     */
    public List<Genre> getAll(){
        return genreDao.findAllByOrderByNameAsc();
    }

    /**
     * Récupère un genre par son identifiant
     * @param id identifiant du genre
     * @return le genre associé à l'id stocké dans la base
     * @throws IllegalArgumentException si id n'est pas dans la base
     */
//    public Genre getById(long id){
//        return genreDao.findById(id).orElseThrow(()->new IllegalArgumentException("Genre.id inexistant: "+id));
//    }
    public Genre getById(long id){
        return genreDao.findById(id).orElse(null);
    }


    /**
     * Sauvegarde ou crée (id=0) un genre
     * @param genre le genre à créer ou sauvegarder
     * @return le genre sauvegardé ou créé
     */
    public Genre save(Genre genre){
        if(genreDao.findByName(genre.getName()) !=null){
            throw new IllegalArgumentException("le genre"+genre.getName()+" existe déjà");
        }
        else{
            genre = genreDao.save(genre);
        }
        return genre;
    }

    /**
     * Supprime un genre de la base si et seulement si il existe et n'est lié à aucun film
     * @param id identitiant du genre à supprimer
     * @return le genre détaché du système de persistance
     * @throws IllegalStateException si le genre est encore lié à au moins un film
     */
    public Genre delete(long id){
        Genre inbase=getById(id);
        if(inbase.getFilms().size() > 0){
            throw new IllegalStateException("Le genre '"+inbase.getName()+"' est encore associé à des films");
        }
        genreDao.deleteById(inbase.getId());
        return inbase;
    }

}
