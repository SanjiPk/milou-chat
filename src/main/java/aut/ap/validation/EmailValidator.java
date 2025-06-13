package aut.ap.validation;

import aut.ap.entity.Email;
import java.util.Objects;

public class EmailValidator implements Validator<Email> {

    @Override
    public boolean validate(Email email) {
        if (email.getSubject().isBlank() || Objects.isNull(email.getSubject())) {
            return false;
        }
        return !email.getBody().isBlank() && !Objects.isNull(email.getBody());
    }
}
