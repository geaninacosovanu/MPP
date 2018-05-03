package repository;

import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtils;
import validator.ValidationException;
import validator.Validator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class UserDBRepository implements IUserRepository {
    String confFile;
    private Validator<User> validator;

    public UserDBRepository(Validator<User> validator, String conf) {
        this.validator = validator;
        this.confFile = conf;
    }

    @Override
    public Integer size() {
        Integer size = null;
        try (Session session = HibernateUtils.getSessionFactory(confFile).openSession()) {
            Transaction t = null;
            try {
                t = session.beginTransaction();
                size = ((Long) session.createQuery("select count(*) from User").uniqueResult()).intValue();
                t.commit();
            } catch (RuntimeException e) {
                if (t != null)
                    t.rollback();
            }
        }
        return size;
    }


    @Override
    public void save(User entity) throws ValidationException {
        validator.validate(entity);
        String generatedPassword;
        generatedPassword = getEncryptedPassword(entity.getParola());
        try (Session session = HibernateUtils.getSessionFactory(confFile).openSession()) {
            Transaction t = null;
            try {
                t = session.beginTransaction();
                User user = new User(entity.getId(), generatedPassword);
                session.save(user);
                t.commit();
            } catch (RuntimeException ex) {
                if (t != null)
                    t.rollback();

            }

        }
    }

    @Override
    public void delete(String userId) {
        try (Session session = HibernateUtils.getSessionFactory(confFile).openSession()) {
            Transaction t = null;
            try {
                t = session.beginTransaction();
                User u = session.createQuery("from User user where user.id=:id", User.class).setString("id", userId).uniqueResult();
                session.delete(u);
                t.commit();
            } catch (RuntimeException ex) {
                if (t != null)
                    t.rollback();

            }
        }
    }


    @Override
    public void update(String userId, User entity) throws ValidationException {
        validator.validate(entity);
        try(Session s = HibernateUtils.getSessionFactory(confFile).openSession()){
            Transaction t =null;
            try{
                t=s.beginTransaction();
                User u= (User) s.load(User.class,new String(userId));
                u.setParola(getEncryptedPassword(entity.getParola()));
                t.commit();
            }catch (RuntimeException ex) {
                if (t != null)
                    t.rollback();

            }
        }


    }

    private String getEncryptedPassword(String parola) {
        String generatedPassword =null ;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(parola.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    @Override
    public User findOne(String userId) {
        User user = null;
        try (Session s =HibernateUtils.getSessionFactory(confFile).openSession()){
            Transaction t =null;
            try {
                t =s.beginTransaction();
                user = s.createQuery("from User user where user.id=:id",User.class).setString("id",userId).uniqueResult();
                t.commit();
            }catch (RuntimeException ex) {
                if (t != null)
                    t.rollback();

            }
        }
        return user;
    }

    @Override
    public Iterable<User> findAll() {
        List<User> all = new ArrayList<>();
        try (Session s = HibernateUtils.getSessionFactory(confFile).openSession()) {
            Transaction t = null;
            try {
                t = s.beginTransaction();
                all = s.createQuery("from User").list();
                t.commit();
            } catch (RuntimeException ex) {
                if (t != null)
                    t.rollback();

            }

        }


        return all;
    }

    @Override
    public boolean exists(User u) {
        String generatedPassword = null;
        generatedPassword = getEncryptedPassword(u.getParola());
        try (Session session = HibernateUtils.getSessionFactory(confFile).openSession()) {
            User u1 = (User) session.createQuery("from User user where user.id=:id and user.parola=:par").setString("id", u.getId()).setString("par", generatedPassword).setMaxResults(1).uniqueResult();
            if (u1 != null)
                return true;
            return false;
        }


    }
}
