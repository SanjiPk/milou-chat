package aut.ap.validation;

import aut.ap.entity.User;
import aut.ap.repository.UserRepository;

import java.util.Objects;

public class UserValidator implements Validator<User> {
    @Override
    public boolean validate(User user) {
        UserRepository userRepository = new UserRepository();
        if (user.getUserName().isBlank() || Objects.isNull(user.getUserName())) {
            return false;
        }
        if (user.getEmail().isBlank() || Objects.isNull(user.getEmail())) {
            return false;
        }

        try {
            if (!Objects.isNull(userRepository.findUser(user.getUserName()))) {
                return false;
            }

            return Objects.isNull(userRepository.findUser(user.getEmail()));
        } catch (Exception e) {
            return false;
        }
    }
}
