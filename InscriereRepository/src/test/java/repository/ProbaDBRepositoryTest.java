package repository;
import model.Inscriere;
import model.Participant;
import model.Proba;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Pair;
import validator.*;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.Assert.*;

public class ProbaDBRepositoryTest {
    Validator<Proba> probaValidator =new ProbaValidator();
    String propFile = "src/test/resources/bdTest.properties";
    IProbaRepository probaDBRepository = new ProbaDBRepository(probaValidator,propFile);

    @After
    public void tearDown() throws Exception {
        setUp();
    }
    @Before
    public void setUp() throws Exception {
        Connection connection =null;
        Properties prop= new Properties();
        try {
            prop.load(new FileReader(new File(propFile).getAbsolutePath()));
            JdbcUtils jdbc=new JdbcUtils(prop);
            connection=jdbc.getConnection();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try(Statement s = connection.createStatement()) {
            try{
                s.executeUpdate("DELETE FROM main.Proba");
            }catch (SQLException e) {
                throw new RepositoryException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        connection.close();
    }

    @Test
    public void size() {
        assertEquals(java.util.Optional.of(0).get(), probaDBRepository.size());
        try {
            probaDBRepository.save(new Proba(1,"p1", 250f));
            assertEquals(java.util.Optional.of(1).get(), probaDBRepository.size());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void save() {
        try {
            probaDBRepository.save(new Proba(1,"p2",500f));
            probaDBRepository.save(new Proba(3,"p3",100f));
            assertEquals(java.util.Optional.of(2).get(), probaDBRepository.size());
            probaDBRepository.save(new Proba(1,"p4",20f));
        } catch (ValidationException e) {
        }
        catch (RepositoryException e){
            assertEquals(java.util.Optional.of(2).get(), probaDBRepository.size());

        }
    }

    @Test
    public void delete() {
        try {
            probaDBRepository.save(new Proba(1,"p2",400f));
            probaDBRepository.save(new Proba(3,"p3",200f));
            assertEquals(java.util.Optional.of(2).get(), probaDBRepository.size());
            probaDBRepository.delete(1);
            assertEquals(java.util.Optional.of(1).get(), probaDBRepository.size());
            probaDBRepository.delete(5);
            assertEquals(java.util.Optional.of(1).get(), probaDBRepository.size());
        } catch (ValidationException e) {
        }catch (RepositoryException e){
        }
    }

    @Test
    public void update() {
        try {
            probaDBRepository.save(new Proba(1,"p2",400f));
            probaDBRepository.save(new Proba(3,"p3",200f));
            assertEquals(java.util.Optional.of(2).get(), probaDBRepository.size());
            probaDBRepository.update(1,new Proba(1,"p2",250f));
            assertEquals(java.util.Optional.of(250f),probaDBRepository.findOne(1).getDistanta());
            probaDBRepository.update(7,new Proba(7,"p7",250f));
        } catch (ValidationException e) {

        }catch (RepositoryException e){
        }
    }

    @Test
    public void findOne() {
        try {
            probaDBRepository.save(new Proba(1,"p2",400f));
            probaDBRepository.save(new Proba(3,"p3",200f));
            assertEquals(java.util.Optional.of(2).get(), probaDBRepository.size());
            Proba p = probaDBRepository.findOne(1);
            assertEquals(java.util.Optional.of(1).get(),p.getId());
            p= probaDBRepository.findOne(7);
            assertEquals(null,p);
        } catch (ValidationException e) {

        }catch (RepositoryException e){
        }
    }

    @Test
    public void findAll() {
        try {
            probaDBRepository.save(new Proba(1,"p2",400f));
            probaDBRepository.save(new Proba(3,"p3",200f));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Iterable<Proba> all= probaDBRepository.findAll();
        int size=0;
        for(Proba p:all)
            size++;
        assertEquals(2,size);
    }

}