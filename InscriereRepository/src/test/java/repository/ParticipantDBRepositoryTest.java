package repository;

import static org.junit.Assert.*;
import model.Inscriere;
import model.Participant;
import model.Proba;
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

public class ParticipantDBRepositoryTest {
    Validator<Participant> participantValidator =new ParticipantValidator();
    String propFile = "src/test/resources/bdTest.properties";
    IParticipantRepository participantRepository = new ParticipantDBRepository(participantValidator,propFile);

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
                s.executeUpdate("DELETE FROM main.Participant");
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
        assertEquals(java.util.Optional.of(0).get(), participantRepository.size());
        try {
            participantRepository.save(new Participant("1","p1", 25));
            assertEquals(java.util.Optional.of(1).get(), participantRepository.size());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void save() {
        try {
            participantRepository.save(new Participant("1","p1", 25));
            participantRepository.save(new Participant("2","p2", 10));
            assertEquals(java.util.Optional.of(2).get(), participantRepository.size());
            participantRepository.save(new Participant("1","p4",20));
        } catch (ValidationException e) {
        }
        catch (RepositoryException e){
            assertEquals(java.util.Optional.of(2).get(), participantRepository.size());

        }
    }

    @Test
    public void delete() {
        try {
            participantRepository.save(new Participant("1","p1", 25));
            participantRepository.save(new Participant("2","p2", 10));
            assertEquals(java.util.Optional.of(2).get(), participantRepository.size());
            participantRepository.delete("1");
            assertEquals(java.util.Optional.of(1).get(), participantRepository.size());
            participantRepository.delete("5");
            assertEquals(java.util.Optional.of(1).get(), participantRepository.size());
        } catch (ValidationException e) {
        }catch (RepositoryException e){
        }
    }

    @Test
    public void update() {
        try {
            participantRepository.save(new Participant("1","p1", 25));
            participantRepository.save(new Participant("2","p2", 10));
            assertEquals(java.util.Optional.of(2).get(), participantRepository.size());
            participantRepository.update("1",new Participant("1","p1",24));
            assertEquals(java.util.Optional.of(24).get(), participantRepository.findOne("1").getVarsta());
            participantRepository.update("7",new Participant("7","p7",25));
        } catch (ValidationException e) {

        }catch (RepositoryException e){
        }
    }

    @Test
    public void findOne() {
        try {
            participantRepository.save(new Participant("1","p1", 25));
            participantRepository.save(new Participant("2","p2", 10));
            assertEquals(java.util.Optional.of(2).get(), participantRepository.size());
            Participant p = participantRepository.findOne("1");
            assertEquals(java.util.Optional.of("1").get(),p.getId());
            p= participantRepository.findOne("7");
            assertEquals(null,p);
        } catch (ValidationException e) {

        }catch (RepositoryException e){
        }
    }

    @Test
    public void findAll() {
        try {
            participantRepository.save(new Participant("1","p1", 25));
            participantRepository.save(new Participant("2","p2", 10));
        } catch (ValidationException e) {
        }
        Iterable<Participant> all= participantRepository.findAll();
        int size=0;
        for(Participant p:all)
            size++;
        assertEquals(2,size);
    }

}