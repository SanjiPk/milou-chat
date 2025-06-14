package aut.ap.repository;

import aut.ap.entity.User;
import java.util.List;

public interface IUser {
    void addUser(User user) throws Exception;

    void updateUser(User user) throws Exception;

    void deleteUser(User user) throws Exception;

    User findUser(String email) throws Exception;

    List<User> findAllContact(User user) throws Exception;
}
