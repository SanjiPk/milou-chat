package aut.ap.controller;

import aut.ap.entity.Email;
import aut.ap.entity.User;
import aut.ap.exceptions.EmailNotValidException;
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

    public void sendEmail(String subject, String body, User sender, Email reply, Email forward, String receiver) throws Exception {
        Email email = new Email(subject, body, sender, reply, forward);

        if (emailValidator.validate(email)) {
            emailService.sendEmail(email, userController.findUser(receiver));
        } else {
            throw new EmailNotValidException("Email is not valid.");
        }
    }

    public void replyEmail(Email replyEmail, Email email) throws Exception {
        emailService.replyEmail(replyEmail, email);
    }

    public void forwardEmail(Email email, User receiver) throws Exception {
        emailService.forwardEmail(email, receiver);
    }

    public List<Email> getAllEmail() throws Exception {
        return emailService.getAllEmail(UserController.getCurrentUser());
    }

    public List<Email> getUnreadEmail() throws Exception {
        return emailService.getUnreadEmail(UserController.getCurrentUser());
    }

    public Email findEmailByCode(int id) throws Exception {
        return emailService.getEmailByCode(UserController.getCurrentUser(), id);
    }

    public void readEmail(Email email) throws Exception {
        emailService.readEmail(UserController.getCurrentUser(), email);
    }

    public List<Email> showContactEmail(User contact) throws Exception {
        return emailService.getContactEmails(UserController.getCurrentUser(), contact);
    }

    public boolean isRead(Email email) {
        return emailService.isRead(email);
    }
}
