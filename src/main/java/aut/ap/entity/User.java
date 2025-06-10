package aut.ap.entity;

import aut.ap.security.PasswordSecurity;
import jakarta.persistence.*;
@Entity
@Table(name = "users")
public class User implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String userName;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
    private String passwordHash;

    public User() {}

    public User(String userName, String email, String plainPassword) {
        setUserName(userName);
        setEmail(email);
        setPasswordHash(plainPassword);
    }

    public Integer getId() {
        return id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String plainPassword) {
        this.passwordHash = PasswordSecurity.hashPassword(plainPassword);
    }

    @Override
    public User clone() {
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
