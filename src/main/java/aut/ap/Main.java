package aut.ap;

import aut.ap.UI.LoginForm;
import aut.ap.controller.UserController;
import aut.ap.repository.UserRepository;
import aut.ap.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);

        LoginForm loginForm = new LoginForm(userController);
    }
}