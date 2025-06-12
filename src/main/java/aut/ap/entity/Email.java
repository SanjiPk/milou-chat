package aut.ap.entity;

import aut.ap.UI.HomePage;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
public class Email implements Cloneable, Comparable<Email> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "sender_id", foreignKey = @ForeignKey(name = "fk_email_sender"))
    private User sender;

    @ManyToOne
    @JoinColumn(name = "reply_id", foreignKey = @ForeignKey(name = "fk_email_reply"))
    private Email reply;

    @ManyToOne
    @JoinColumn(name = "forward_id", foreignKey = @ForeignKey(name = "fk_email_forward"))
    private Email forward;

    @Column(name = "send_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime sendTime;

    public Email() {
    }

    public Email(String subject, String body, User sender, Email reply, Email forward) {
        setSubject(subject);
        setBody(body);
        setSender(sender);
        setReply(reply);
        setForward(forward);
        setSendTime(LocalDateTime.now());
    }

    public Integer getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Email getReply() {
        return reply;
    }

    public void setReply(Email reply) {
        this.reply = reply;
    }

    public Email getForward() {
        return forward;
    }

    public void setForward(Email forward) {
        this.forward = forward;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public Email clone() {
        try {
            Email clone = (Email) super.clone();
            if (reply != null) {
                clone.reply = this.reply.clone();
            }
            if (forward != null) {
                clone.forward = this.forward.clone();
            }
            if (sender != null) {
                clone.sender = this.sender.clone();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return String.format("Sender username: %s\n" +
                        "Sender email: %s\n" +
                        "send at: %s\n" +
                        "---> email subject: %s\n" +
                        "---> email body: %s"
                , sender.getUserName(), sender.getEmail(), sendTime.toString(), subject, body);
    }

    @Override
    public int compareTo(Email o) {
        if (this.getSendTime().isBefore(o.getSendTime())) {
            return 1;
        }
        else {
            return -1;
        }
    }
}
