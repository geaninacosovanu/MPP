package validator;


import org.springframework.stereotype.Component;

public interface Validator<E> {
    void validate(E entity) throws ValidationException;
}
