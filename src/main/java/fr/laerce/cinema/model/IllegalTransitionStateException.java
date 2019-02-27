package fr.laerce.cinema.model;

public class IllegalTransitionStateException extends Exception {
    public IllegalTransitionStateException(String error_transition) {
        super(error_transition);
    }
}
