package aut.ap.service;

import aut.ap.entity.User;
import aut.ap.exceptions.EntityNotFoundException;
import aut.ap.exceptions.IncorrectPasswordException;
import aut.ap.exceptions.UserNotValidException;
import aut.ap.repository.UserRepository;
import aut.ap.security.PasswordSecurity;
import aut.ap.validation.UserValidator;
import aut.ap.validation.Validator;

import java.util.List;
import java.util.Objects;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) {
        Validator<User> userValidator = new UserValidator();
        if (userValidator.validate(user)) {
            userRepository.addUser(user);
        }
        else {
            throw new UserNotValidException("User is not validate.");
        }
    }

    public User login(String email, String password) {
        User usr = userRepository.findUser(email);

        if (email.isBlank()) {
            throw new IllegalArgumentException("Email is empty.");
        }

        if (Objects.isNull(usr)) {
            throw new EntityNotFoundException("User not found.");
        }

        if (PasswordSecurity.checkPassword(password, usr.getPasswordHash())) {
            return usr;
        }
        return null;
    }

    public void changePassword(User current, String oldPass, String newPass) throws Exception{
        User usr = userRepository.findUser(current.getEmail());
        if (Objects.isNull(usr)) {
            throw new EntityNotFoundException("User not found!!!");
        }

        if (PasswordSecurity.checkPassword(oldPass, usr.getPasswordHash())) {
            throw  new IncorrectPasswordException("Old password is incorrect.");
        }

        usr.setPasswordHash(newPass);
        userRepository.updateUser(usr);
    }

    public List<User> showContact(User usr) {
        return userRepository.findAllContact(usr);
    }

    public User findUser(String str) {
        return userRepository.findUser(str);
    }
}
