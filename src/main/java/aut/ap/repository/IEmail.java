package aut.ap.repository;

import aut.ap.entity.Email;
import aut.ap.entity.User;

import java.util.List;

public interface IEmail {
    void sendEmail(Email email, User receiver);

    void forwardEmail(Email email, User receiver);

    void replyEmail(Email replyEmail, Email email);

    List<Email> getAllEmail(User emailOwner);

    List<Email> getContactEmails(User emailOwner, User contact);

    List<Email> getUnreadEmail(User emailOwner);

    Email getEmailByCode(User emailOwner, int emailId);

    void readEmail(User emailOwner, Email email);
}
