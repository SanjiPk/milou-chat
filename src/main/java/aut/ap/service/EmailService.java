package aut.ap.service;

import aut.ap.entity.Email;
import aut.ap.entity.User;
import aut.ap.repository.IEmail;
import aut.ap.repository.EmailRepository;

import java.util.List;

public class EmailService implements IEmail {
    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }


    @Override
    public void sendEmail(Email email, User receiver) throws Exception {
        emailRepository.sendEmail(email, receiver);
    }

    @Override
    public void forwardEmail(Email email, User receiver) throws Exception {
        emailRepository.forwardEmail(email, receiver);
    }

    @Override
    public void replyEmail(Email replyEmail, Email email) throws Exception {
        emailRepository.replyEmail(replyEmail, email);
    }

    @Override
    public List<Email> getAllEmail(User emailOwner) throws Exception {
        return emailRepository.getAllEmail(emailOwner);
    }

    @Override
    public List<Email> getContactEmails(User emailOwner, User contact) throws Exception {
        return emailRepository.getContactEmails(emailOwner, contact);
    }

    @Override
    public List<Email> getUnreadEmail(User emailOwner) throws Exception {
        return emailRepository.getUnreadEmail(emailOwner);
    }

    @Override
    public Email getEmailByCode(User emailOwner, int emailId) throws Exception {
        return emailRepository.getEmailByCode(emailOwner, emailId);
    }

    @Override
    public void readEmail(User emailOwner, Email email) throws Exception {
        emailRepository.readEmail(emailOwner, email);
    }

    @Override
    public boolean isRead(Email email) {
        boolean read = false;
        try {
            read = emailRepository.isRead(email);
        } catch (Exception e) {
            return false;
        }
        return read;
    }
}
