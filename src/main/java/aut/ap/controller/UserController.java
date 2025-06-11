package aut.ap.controller;

import aut.ap.entity.User;
import aut.ap.service.UserService;
import aut.ap.validation.UserValidator;
import aut.ap.validation.Validator;

import java.util.List;
import java.util.Objects;

public class UserController {
    private final UserService userService;
    private static User currentUser;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void register(String userName, String email, String password) {
        User usr = new User(userName, email, password);
        Validator<User> userValidator = new UserValidator();
        if (userValidator.validate(usr)) {
            userService.register(usr);
        }
    }

    public boolean login(String email, String password) {
        try {
            currentUser = userService.login(email, password);
        } catch (Exception e) {
            return false;
        }
        return !Objects.isNull(currentUser);
    }

    public List<User> showContact() {
        return userService.showContact(currentUser);
    }

    public void changePassword(String oldPassword, String newPassword) {
        try {
            userService.changePassword(currentUser, oldPassword, newPassword);
        } catch (Exception e) {
            return;
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
