package repository;

import static org.junit.Assert.*;
import model.Inscriere;
import model.Participant;
import model.Proba;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Pair;
import validator.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.Assert.*;

public class UserDBRepositoryTest {
    Validator<User> userValidator = new UserValidator();
    String propFile = "src/test/resources/bdTest.properties";
    IUserRepository userRepository = new UserDBRepository(userValidator, propFile);

    @After
    public void tearDown() throws Exception {
        setUp();
    }

    @Before
    public void setUp() throws Exception {
        Connection connection = null;
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(new File(propFile).getAbsolutePath()));
            JdbcUtils jdbc = new JdbcUtils(prop);
            connection = jdbc.getConnection();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try (Statement s = connection.createStatement()) {
            try {
                s.executeUpdate("DELETE FROM main.User");
            } catch (SQLException e) {
                throw new RepositoryException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        connection.close();
    }

    @Test
    public void size() {
        assertEquals(java.util.Optional.of(0).get(), userRepository.size());
        try {
            userRepository.save(new User("u1", "p1"));
            assertEquals(java.util.Optional.of(1).get(), userRepository.size());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void save() {
        try {
            userRepository.save(new User("u2", "p2"));
            userRepository.save(new User("u3", "p3"));
            assertEquals(java.util.Optional.of(2).get(), userRepository.size());
            userRepository.save(new User("u2", "p4"));
        } catch (ValidationException e) {
        } catch (RepositoryException e) {
            assertEquals(java.util.Optional.of(2).get(), userRepository.size());

        }
    }

    @Test
    public void delete() {
        try {
            userRepository.save(new User("u2", "p2"));
            userRepository.save(new User("u3", "p3"));
            assertEquals(java.util.Optional.of(2).get(), userRepository.size());
            userRepository.delete("u2");
            assertEquals(java.util.Optional.of(1).get(), userRepository.size());
            userRepository.delete("u5");
            assertEquals(java.util.Optional.of(1).get(), userRepository.size());
        } catch (ValidationException e) {
        } catch (RepositoryException e) {
        }
    }

    @Test
    public void update() {
        try {
            userRepository.save(new User("u2", "p2"));
            userRepository.save(new User("u3", "p3"));
            assertEquals(java.util.Optional.of(2).get(), userRepository.size());
            userRepository.update("u2", new User("u2", "parola"));
            assertEquals(userRepository.findOne("u2").getParola(), "8287458823facb8ff918dbfabcd22ccb");
            userRepository.update("u5", new User("u5", "parola"));
        } catch (ValidationException e) {

        } catch (RepositoryException e) {
        }
    }

    @Test
    public void findOne() {
        try {
            userRepository.save(new User("u2", "p2"));
            userRepository.save(new User("u3", "p3"));
            assertEquals(java.util.Optional.of(2).get(), userRepository.size());
            User u = userRepository.findOne("u2");
            assertEquals("u2", u.getId());
            u = userRepository.findOne("ds");
            assertEquals(null, u);
        } catch (ValidationException e) {

        } catch (RepositoryException e) {
        }
    }

    @Test
    public void findAll() {
        try {
            userRepository.save(new User("u2", "p2"));
            userRepository.save(new User("u3", "p3"));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Iterable<User> all = userRepository.findAll();
        int size = 0;
        for (User u : all)
            size++;
        assertEquals(2, size);
    }


}