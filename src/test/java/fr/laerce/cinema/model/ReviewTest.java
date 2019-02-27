package fr.laerce.cinema.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReviewTest {

    @Test
    public void setArticle() {
        Review review = new Review();
        review.setArticle("test commentaire");
        assertEquals("test commentaire",review.getArticle());
    }
    @Test
    public void TransitionInitial() {
        Review review = new Review();
        assertEquals(review.WAINTING_MODERATION,review.getState());
    }
    @Test
    public void goodTransitionPublie() {
        Review review = new Review();
        try {
            review.validByModerator();
            assertEquals(review.PUBLISHED,review.getState());
        } catch (IllegalTransitionStateException e) {
            fail("transition attendue");
        }
    }

    @Test
    public void BadTransitionToPublie() {
        Review review = new Review();
        try {
            review.HoldForEditByModerator();
            review.validByModerator();
            fail("transition vers publie non autorisée");
        } catch (IllegalTransitionStateException e) {
            assertEquals(review.MUST_BE_MODIFIED,review.getState());
        }
    }
    @Test
    public void goodTransitionAbadonne() {
        Review review = new Review();
        try {
            review.HoldForEditByModerator();
            review.cancelByUser();
            assertEquals(review.ABONDONED,review.getState());
        } catch (IllegalTransitionStateException e) {
            fail("transition attendue");
        }
    }
    @Test
    public void badTransitionAbandonne() {
        Review review = new Review();
        try {
            review.cancelByUser();
            fail("transition vers ABONDONED non autorisée");
        } catch (IllegalTransitionStateException e) {
            assertEquals(review.WAINTING_MODERATION,review.getState());
        }
    }
    @Test
    public void goodTransitionSupprimer() {
        Review review = new Review();
        try {
            review.validByModerator();
            review.deleteByUser();
            assertEquals(review.DELETED,review.getState());
        } catch (IllegalTransitionStateException e) {
            fail("transition attendue");
        }
    }
    @Test
    public void badTransitionSupprimer() {
        Review review = new Review();
        try {
            review.cancelByUser();
            fail("transition vers ABONDONED non autorisée");
        } catch (IllegalTransitionStateException e) {
            assertEquals(review.WAINTING_MODERATION,review.getState());
        }
    }
    @Test
    public void goodTransitionModerationViaEditer() {
        Review review = new Review();
        try {
            review.editByUser();
            assertEquals(review.WAINTING_MODERATION,review.getState());
        } catch (IllegalTransitionStateException e) {
            fail("transition attendue");
        }
    }
    @Test
    public void badTransitionModerationViaEditer() {
        Review review = new Review();
        try {
            review.HoldForEditByModerator();
            review.cancelByUser();
            review.editByUser();
            fail("transition vers ABONDONED non autorisée");
        } catch (IllegalTransitionStateException e) {
            assertEquals(review.ABONDONED,review.getState());
        }
    }
    @Test
    public void goodTransitionModerationViaEditer2() {
        Review review = new Review();
        try {
            review.HoldForEditByModerator();
            review.editByUser();
            assertEquals(review.WAINTING_MODERATION,review.getState());
        } catch (IllegalTransitionStateException e) {
            fail("transition attendue");
        }
    }

    @Test
    public void goodTransitionModerationViaEditer3() {
        Review review = new Review();
        try {
            review.HoldForEditByModerator();
            review.editByUser();
            assertEquals(review.WAINTING_MODERATION,review.getState());
        } catch (IllegalTransitionStateException e) {
            fail("transition attendue");
        }
    }

    @Test
    public void goodTransitionModerationViaRejete() {
        Review review = new Review();
        try {
            review.rejectByModerator();

            assertEquals(review.REJECTED,review.getState());
        } catch (IllegalTransitionStateException e) {
            fail("transition attendue");
        }
    }
    @Test
    public void badTransitionModerationViaRejete() {
        Review review = new Review();
        try {
            review.HoldForEditByModerator();
            review.rejectByModerator();
            fail("transition vers ABONDONED non autorisée");
        } catch (IllegalTransitionStateException e) {
            assertEquals(review.MUST_BE_MODIFIED,review.getState());
        }
    }

}