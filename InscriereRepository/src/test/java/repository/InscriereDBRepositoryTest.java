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
import java.util.Optional;
import java.util.Properties;

import static org.junit.Assert.*;

public class InscriereDBRepositoryTest {
    Validator<Inscriere> inscriereValidator =new InscriereValidator();
    String propFile = "src/test/resources/bdTest.properties";
    IInscriereRepository inscriereRepository = new InscriereDBRepository(inscriereValidator,propFile);
    Validator<Participant> participantValidator =new ParticipantValidator();
    IParticipantRepository participantRepository = new ParticipantDBRepository(participantValidator,propFile);
    Validator<Proba> probaValidator =new ProbaValidator();
    IProbaRepository probaDBRepository = new ProbaDBRepository(probaValidator,propFile);

    @After
    public void tearDown() throws Exception {
        cleanTable();
        participantRepository.delete("1");
        participantRepository.delete("2");
        participantRepository.delete("9");
        probaDBRepository.delete(1);
        probaDBRepository.delete(3);
        probaDBRepository.delete(2);
    }

    @Before
    public void setUp() throws Exception {
        cleanTable();
        participantRepository.save(new Participant("1","p2", 10));
        participantRepository.save(new Participant("2","p2", 10));
        participantRepository.save(new Participant("9","p2", 10));
        probaDBRepository.save(new Proba(1,"a",12f));
        probaDBRepository.save(new Proba(2,"a",12f));
        probaDBRepository.save(new Proba(3,"a",12f));

    }

    private void cleanTable() throws SQLException {
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
                s.executeUpdate("DELETE FROM main.Inscriere");
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
        assertEquals(Optional.of(0).get(), inscriereRepository.size());
        try {
            inscriereRepository.save(new Inscriere("1",2));
            assertEquals(Optional.of(1).get(), inscriereRepository.size());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void save() {
        try {
            inscriereRepository.save(new Inscriere("1",2));
            inscriereRepository.save(new Inscriere("1",3));
            assertEquals(Optional.of(2).get(),inscriereRepository.size());
            inscriereRepository.save(new Inscriere("1",2));
        } catch (ValidationException e) {
        }
        catch (RepositoryException e){
            assertEquals(Optional.of(2).get(),inscriereRepository.size());
        }
    }

    @Test
    public void delete() {
        try {
            inscriereRepository.save(new Inscriere("1",2));
            inscriereRepository.save(new Inscriere("1",3));
            assertEquals(Optional.of(2).get(),inscriereRepository.size());
            inscriereRepository.delete(new Pair<>("1",2));
            assertEquals(Optional.of(1).get(),inscriereRepository.size());
            inscriereRepository.delete(new Pair<>("1",7));
            assertEquals(Optional.of(1).get(),inscriereRepository.size());
        } catch (ValidationException e) {
        }catch (RepositoryException e){
        }
    }

    @Test
    public void update() {
        try {
            inscriereRepository.save(new Inscriere("1",2));
            inscriereRepository.save(new Inscriere("1",3));
            assertEquals(Optional.of(2).get(),inscriereRepository.size());
            inscriereRepository.update(new Pair<>("1",2),new Inscriere("1",5));
            assertEquals(inscriereRepository.findOne(new Pair<>("1",2)).getIdProba(), Optional.of(2).get());
            inscriereRepository.update(new Pair<>("5",2),new Inscriere("5",3));
        } catch (ValidationException e) {

        }catch (RepositoryException e){
        }
    }

    @Test
    public void findOne() {
        try {
            inscriereRepository.save(new Inscriere("1",2));
            inscriereRepository.save(new Inscriere("1",3));
            assertEquals(Optional.of(2).get(),inscriereRepository.size());
            Inscriere i = inscriereRepository.findOne(new Pair<>("1",2));
            assertEquals(Optional.of(2).get(),i.getIdProba());
            i=inscriereRepository.findOne(new Pair<>("1",5));
            assertEquals(null,i);
        } catch (ValidationException e) {

        }catch (RepositoryException e){
        }
    }

    @Test
    public void findAll() {
        try {
            inscriereRepository.save(new Inscriere("1",2));
            inscriereRepository.save(new Inscriere("1",3));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Iterable<Inscriere> all=inscriereRepository.findAll();
        int size=0;
        for(Inscriere i:all)
            size++;
        assertEquals(2,size);
    }


    @Test
    public void getNrParticipantiProba() {
        try {

            inscriereRepository.save(new Inscriere("1",2));
            inscriereRepository.save(new Inscriere("1",3));
            inscriereRepository.save(new Inscriere("1",1));
            inscriereRepository.save(new Inscriere("2",3));
            inscriereRepository.save(new Inscriere("9",3));
            assertEquals( Optional.of(3).get(),inscriereRepository.getNrParticipantiProba(3));

        } catch (ValidationException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getParticipantiPtProba() {
        try {
            inscriereRepository.save(new Inscriere("1",2));
            inscriereRepository.save(new Inscriere("1",3));
            inscriereRepository.save(new Inscriere("1",1));
            inscriereRepository.save(new Inscriere("2",3));
            inscriereRepository.save(new Inscriere("9",3));
            Iterable<Participant> all=inscriereRepository.getParticipantiPtProba(3);
            int size=0;
            for(Participant p:all)
                size++;
            assertEquals(3,size);

        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getProbePtParticipant() {
        try {
            inscriereRepository.save(new Inscriere("1",2));
            inscriereRepository.save(new Inscriere("1",3));
            inscriereRepository.save(new Inscriere("1",1));
            inscriereRepository.save(new Inscriere("2",3));
            inscriereRepository.save(new Inscriere("9",3));
            Iterable<Proba> all=inscriereRepository.getProbePtParticipant("1");
            int size=0;
            for(Proba p:all)
                size++;
            assertEquals(3,size);


        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}