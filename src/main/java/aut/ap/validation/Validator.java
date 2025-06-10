package aut.ap.validation;

public interface Validator<T> {
    boolean validate(T t);
}
