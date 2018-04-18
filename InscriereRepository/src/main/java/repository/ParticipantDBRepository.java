package repository;

import model.Participant;
import validator.ValidationException;
import validator.Validator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantDBRepository implements IParticipantRepository {
    private Validator<Participant> participantValidator;
    private Connection connection;

    public ParticipantDBRepository(Validator<Participant> participantValidator,String propFile) {
        this.participantValidator = participantValidator;
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(new File(propFile).getAbsolutePath()));
            JdbcUtils jdbcUtils = new JdbcUtils(prop);
            connection = jdbcUtils.getConnection();
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Integer size() {
        Integer size = null;
        try (Statement s = connection.createStatement()) {
            try (ResultSet resultSet = s.executeQuery("SELECT count(*) as Nr from main.Participant")) {
                size = resultSet.getInt("Nr");
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return size;
    }

    @Override
    public void save(Participant entity) throws ValidationException {
        participantValidator.validate(entity);
        try (PreparedStatement s = connection.prepareStatement("insert into Participant(id,nume,varsta) values (?,?,?)")) {
            s.setString(1, entity.getId());
            s.setString(2, entity.getNume());
            s.setInt(3, entity.getVarsta());
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Participantul exista deja!");
        }
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement s = connection.prepareStatement("DELETE FROM Participant WHERE id=?")) {
            s.setString(1, id);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public void update(String id, Participant entity) throws ValidationException {
        participantValidator.validate(entity);
        try (PreparedStatement s = connection.prepareStatement("UPDATE main.Participant SET nume=?, varsta=? WHERE id=?")) {
            s.setString(1, entity.getNume());
            s.setInt(2, entity.getVarsta());
            s.setString(3, id);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Participant findOne(String id) {
        Participant participant = null;
        try (PreparedStatement s = connection.prepareStatement("SELECT P.id,P.nume,P.varsta FROM main.Participant P WHERE P.id=?")) {
            s.setString(1, id);
            ResultSet resultSet = s.executeQuery();
            if(resultSet.next()!=false)
                participant = new Participant(resultSet.getString("id"), resultSet.getString("nume"), resultSet.getInt("varsta"));
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return participant;
    }

    @Override
    public Iterable<Participant> findAll() {
        List<Participant> all = new ArrayList<>();
        try (Statement s = connection.createStatement()) {
            try (ResultSet resultSet = s.executeQuery("SELECT * FROM  main.Participant")) {
                while (resultSet.next()) {
                    all.add(new Participant(resultSet.getString("id"), resultSet.getString("nume"), resultSet.getInt("varsta")));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return all;
    }

    @Override
    public Participant getParticipant(String nume, Integer varsta) {
        Participant participant = null;
        try (PreparedStatement s = connection.prepareStatement("SELECT P.id,P.nume,P.varsta FROM main.Participant P WHERE P.nume=? AND P.varsta=?")) {
            s.setString(1, nume);
            s.setInt(2, varsta);
            ResultSet resultSet = s.executeQuery();
            if(resultSet.next()!=false)
                participant = new Participant(resultSet.getString("id"), resultSet.getString("nume"), resultSet.getInt("varsta"));
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return participant;
    }
}
