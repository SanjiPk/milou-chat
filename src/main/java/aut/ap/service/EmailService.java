package aut.ap.service;

import aut.ap.entity.Email;
import aut.ap.entity.User;
import aut.ap.exceptions.EmailNotValidException;
import aut.ap.repository.IEmail;
import aut.ap.repository.EmailRepository;
import aut.ap.validation.EmailValidator;

import java.util.List;

public class EmailService implements IEmail {
    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }


    @Override
    public void sendEmail(Email email, User receiver) throws EmailNotValidException {
        emailRepository.sendEmail(email, receiver);
    }

    @Override
    public void forwardEmail(Email email, User receiver) {
        emailRepository.forwardEmail(email, receiver);
    }

    @Override
    public void replyEmail(Email replyEmail, Email email) {
        emailRepository.replyEmail(replyEmail, email);
    }

    @Override
    public List<Email> getAllEmail(User emailOwner) throws NullPointerException{
        return emailRepository.getAllEmail(emailOwner);
    }

    @Override
    public List<Email> getContactEmails(User emailOwner, User contact) {
        return emailRepository.getContactEmails(emailOwner, contact);
    }

    @Override
    public List<Email> getUnreadEmail(User emailOwner) throws NullPointerException{
        return emailRepository.getUnreadEmail(emailOwner);
    }

    @Override
    public Email getEmailByCode(User emailOwner, int emailId) throws NullPointerException{
        return emailRepository.getEmailByCode(emailOwner, emailId);
    }

    @Override
    public void readEmail(User emailOwner, Email email) {
        emailRepository.readEmail(emailOwner, email);
    }
}
