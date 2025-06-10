package aut.ap.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Consumer;
import java.util.function.Function;

public class DbUtil {

    public static void runInTransaction(Consumer<Session> consumer) {
        Transaction tx = null;
        try (Session session = SingletonSessionFactory.get().openSession()) {
            tx = session.beginTransaction();
            consumer.accept(session);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public static <T> T runInTransaction(Function<Session, T> function) {
        Transaction tx = null;
        T result = null;
        try (Session session = SingletonSessionFactory.get().openSession()) {
            tx = session.beginTransaction();
            result = function.apply(session);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        }
        return result;
    }
}
