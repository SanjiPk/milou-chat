package aut.ap.repository;

import aut.ap.entity.User;
import java.util.List;

public interface IUser {
    void addUser(User user);

    void updateUser(User user);

    void deleteUser(User user);

    User findUser(String email);

    List<User> findAllContact(User user);
}
