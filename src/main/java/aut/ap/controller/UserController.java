package aut.ap.controller;

import aut.ap.entity.User;
import aut.ap.exceptions.UserNotValidException;
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

    public void register(String userName, String email, String password) throws Exception {
        User usr = new User(userName, email, password);
        userService.register(usr);
    }

    public boolean login(String email, String password) {
        try {
            currentUser = userService.login(email, password);
        } catch (Exception e) {
            return false;
        }
        return !Objects.isNull(currentUser);
    }

    public List<User> showContact() throws Exception{
        return userService.showContact(currentUser);
    }

    public void changePassword(String oldPassword, String newPassword) throws Exception{
        try {
            userService.changePassword(currentUser, oldPassword, newPassword);
        } catch (Exception e) {
            return;
        }
    }

    public User findUser(String str) throws Exception {
        return userService.findUser(str);
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
