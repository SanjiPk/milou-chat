package aut.ap.controller;

import aut.ap.entity.Email;
import aut.ap.entity.User;
import aut.ap.service.EmailService;
import aut.ap.validation.EmailValidator;

import java.util.List;

public class EmailController {
    private final EmailService emailService;
    private final UserController userController;
    private final EmailValidator emailValidator;

    public EmailController(EmailService emailService, UserController userController, EmailValidator emailValidator) {
        this.emailService = emailService;
        this.userController = userController;
        this.emailValidator = emailValidator;
    }

    public void sendEmail(String subject, String body, User sender, Email reply, Email forward, String receiver) {
        Email email = new Email(subject, body, sender, reply, forward);

        if (emailValidator.validate(email)) {
            try {
                emailService.sendEmail(email, userController.findUser(receiver));
            } catch (Exception e) {
                return;
            }
        }
    }

    public List<Email> getAllEmail() {
        try {
            return emailService.getAllEmail(UserController.getCurrentUser());
        } catch (Exception e) {
            return null;
        }
    }

    public List<Email> getUnreadEmail() {
        try {
            return emailService.getUnreadEmail(UserController.getCurrentUser());
        } catch (Exception e) {
            return null;
        }
    }

    public Email findEmailByCode(int id) {
        try {
            return emailService.getEmailByCode(UserController.getCurrentUser(), id);
        } catch (Exception e) {
            return null;
        }
    }

    public void readEmail(Email email) {
        try {
            emailService.readEmail(UserController.getCurrentUser(), email);
        } catch (Exception e) {
            return;
        }
    }

    public List<Email> showContactEmail(User contact) {
        return emailService.getContactEmails(UserController.getCurrentUser(), contact);
    }
}
