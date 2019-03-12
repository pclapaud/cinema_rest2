package fr.laerce.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
public class Review {

    private static final Map<Integer, List<Integer>> allowed;
    private static final Map<Integer, List<Integer>> allowedto;
    static {
        allowed = new HashMap<>();
        allowed.put(Review.REJECTED,Arrays.asList(new Integer[]{Review.WAINTING_MODERATION}));
        allowed.put(Review.DELETED,Arrays.asList(new Integer[]{Review.PUBLISHED}));
        allowed.put(Review.WAINTING_MODERATION,Arrays.asList(new Integer[]{Review.PUBLISHED,Review.WAINTING_MODERATION,Review.MUST_BE_MODIFIED}));
        allowed.put(Review.PUBLISHED,Arrays.asList(new Integer[]{Review.WAINTING_MODERATION}));
        allowed.put(Review.ABONDONED,Arrays.asList(new Integer[]{Review.MUST_BE_MODIFIED}));
        allowed.put(Review.MUST_BE_MODIFIED,Arrays.asList(new Integer[]{Review.WAINTING_MODERATION}));
    }
    static {
        allowedto = new HashMap<>();
        allowedto.put(Review.WAINTING_MODERATION,Arrays.asList(new Integer[]{Review.PUBLISHED,Review.REJECTED,Review.MUST_BE_MODIFIED,Review.WAINTING_MODERATION,Review.REJECTED}));
        allowedto.put(Review.PUBLISHED,Arrays.asList(new Integer[]{Review.WAINTING_MODERATION,Review.DELETED}));
        allowedto.put(Review.MUST_BE_MODIFIED,Arrays.asList(new Integer[]{Review.ABONDONED,Review.WAINTING_MODERATION}));
        allowedto.put(Review.ABONDONED,new ArrayList<>());
        allowedto.put(Review.REJECTED,new ArrayList<>());
        allowedto.put(Review.DELETED,new ArrayList<>());
    }
    public static final int WAINTING_MODERATION = 1;
    public static final int PUBLISHED = 2;
    public static final int MUST_BE_MODIFIED = 3;
    public static final int ABONDONED = 4;
    public static final int DELETED = 5;
    public static final int REJECTED = 6;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "article", nullable = true, length = -1)
    private String article;
    @Basic
    @Column(name = "datte", nullable = false)
    private Timestamp date;
    @ManyToOne
    @JoinColumn(name="film_id")
    @JsonIgnore
    private Film film;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    private int state = WAINTING_MODERATION;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }


    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp datte) {
        this.date = datte;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (id != review.id) return false;
        if (article != null ? !article.equals(review.article) : review.article != null) return false;
        if (date != null ? !date.equals(review.date) : review.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (article != null ? article.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    public int getState() {
        return this.state;
    }

    public void TransitTo(int target) throws IllegalTransitionStateException {
        if (canTransitTo(target)){
            this.state = target;
        }
        else {
            throw new IllegalTransitionStateException("Transition non autorisé");
        }

    }

    public void validByModerator() throws IllegalTransitionStateException {
        TransitTo(Review.PUBLISHED);

    }


    public void HoldForEditByModerator() throws IllegalTransitionStateException {
        TransitTo(Review.MUST_BE_MODIFIED);
    }

    public void cancelByUser() throws IllegalTransitionStateException {
        TransitTo(Review.ABONDONED);
    }

    public void deleteByUser() throws IllegalTransitionStateException {
        TransitTo(Review.DELETED);
    }

    public void editByUser() throws IllegalTransitionStateException {
        TransitTo(Review.WAINTING_MODERATION);
    }

    public void rejectByModerator() throws IllegalTransitionStateException {
        TransitTo(Review.REJECTED);
    }
    public boolean canTransitTo(int targetState){
//       return allowed.get(targetState).contains(this.state);

            return Review.allowedto.get(this.getState()).contains(targetState);



    }


}

