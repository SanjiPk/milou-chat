package aut.ap.repository;

import aut.ap.entity.Email;
import aut.ap.entity.User;

import java.util.List;

public interface IEmail {
    void sendEmail(Email email, User receiver) throws Exception;

    void forwardEmail(Email email, User receiver) throws Exception;

    void replyEmail(Email replyEmail, Email email) throws Exception;

    List<Email> getAllEmail(User emailOwner) throws Exception;

    List<Email> getContactEmails(User emailOwner, User contact) throws Exception;

    List<Email> getUnreadEmail(User emailOwner) throws Exception;

    Email getEmailByCode(User emailOwner, int emailId) throws Exception;

    void readEmail(User emailOwner, Email email) throws Exception;

    boolean isRead(Email email) throws Exception;
}
