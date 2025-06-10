package aut.ap.repository;

import aut.ap.entity.User;
import org.hibernate.Session;
import java.util.List;
import java.util.function.Consumer;

public class UserRepository implements IUser{
    @Override
    public void addUser(User user) {
        DbUtil.runInTransaction((Consumer<Session>) session -> session.persist(user));
    }

    @Override
    public void updateUser(User user) {
        String sqlCommand = "update users set username = :username," +
                " email = :email," +
                " password_hash = :password " +
                "where id = :id";

        DbUtil.runInTransaction(session -> {
            session.createNativeMutationQuery(sqlCommand)
                    .setParameter("username", user.getUserName())
                    .setParameter("email", user.getEmail())
                    .setParameter("password", user.getPasswordHash())
                    .setParameter("id", user.getId())
                    .executeUpdate();
        });
    }

    @Override
    public void deleteUser(User user) {
        DbUtil.runInTransaction((Consumer<Session>)  session -> session.remove(user));
    }

    @Override
    public User findUser(String str) {
        String sqlCommand = "select * from users where email = :str OR username = :str";

        return DbUtil.runInTransaction(session -> {
            return session.createNativeQuery(sqlCommand, User.class)
                    .setParameter("str", str)
                    .getSingleResultOrNull();
        });
    }

    @Override
    public List<User> findAllContact(User user) {
        String sqlCommand = "SELECT DISTINCT u.* " +
                "FROM users AS u " +
                "         JOIN email_recipients er ON u.id = er.receiver_id " +
                "         JOIN emails e ON e.id = er.email_id " +
                "WHERE e.sender_id = (SELECT id FROM users WHERE id = :user) " +
                "UNION " +
                "SELECT DISTINCT u.* " +
                "FROM users AS u " +
                "         JOIN emails e ON u.id = e.sender_id " +
                "         JOIN email_recipients er ON er.email_id = e.id " +
                "WHERE er.receiver_id = (SELECT id FROM users WHERE id = :user)";

        return DbUtil.runInTransaction(session -> {
            return session.createNativeQuery(sqlCommand, User.class)
                    .setParameter("user", user.getId())
                    .getResultList();
        });
    }
}
