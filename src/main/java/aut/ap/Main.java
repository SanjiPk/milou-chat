package aut.ap;

import aut.ap.UI.LoginForm;
import aut.ap.controller.EmailController;
import aut.ap.controller.UserController;
import aut.ap.entity.User;
import aut.ap.repository.EmailRepository;
import aut.ap.repository.UserRepository;
import aut.ap.service.EmailService;
import aut.ap.service.UserService;
import aut.ap.validation.EmailValidator;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);

        EmailRepository emailRepository = new EmailRepository();
        EmailService emailService = new EmailService(emailRepository);
        EmailValidator emailValidator = new EmailValidator();
        EmailController emailController = new EmailController(emailService, userController, emailValidator);

        LoginForm loginForm = new LoginForm(userController, emailController);
    }
}