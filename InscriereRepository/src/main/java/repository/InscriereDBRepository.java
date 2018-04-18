package repository;

import model.Inscriere;
import model.Participant;
import model.Proba;
import utils.Pair;
import validator.ValidationException;
import validator.Validator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class InscriereDBRepository implements IInscriereRepository {
    private Validator<Inscriere> inscriereValidator;
    private Connection connection;

    public InscriereDBRepository(Validator<Inscriere> inscriereValidator, String propFile) {
        this.inscriereValidator = inscriereValidator;
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
            try (ResultSet resultSet = s.executeQuery("SELECT count(*) as Nr from main.Inscriere")) {
                size = resultSet.getInt("Nr");
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return size;
    }

    @Override
    public void save(Inscriere entity) throws ValidationException {
        inscriereValidator.validate(entity);
        try (PreparedStatement s = connection.prepareStatement("insert into main.Inscriere(idParticipant, idProba) values (?,?)")) {
            s.setString(1, entity.getIdParticipant());
            s.setInt(2, entity.getIdProba());
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Inscrierea exista deja!");
        }
    }

    @Override
    public void delete(Pair<String,Integer> id) {
        try (PreparedStatement s = connection.prepareStatement("DELETE FROM main.Inscriere WHERE idParticipant=? AND idProba=?")) {
            s.setString(1, id.getFirst());
            s.setInt(2,id.getSecond());
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public void update(Pair<String,Integer> id, Inscriere entity) throws ValidationException {
        inscriereValidator.validate(entity);
        try (PreparedStatement s = connection.prepareStatement("UPDATE main.Inscriere SET idParticipant=?, idProba=? WHERE idParticipant=? AND idProba=?")) {
            s.setString(1, entity.getIdParticipant());
            s.setInt(2, entity.getIdProba());
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Inscriere findOne(Pair<String,Integer> id) {
        Inscriere inscriere = null;
        try (PreparedStatement s = connection.prepareStatement("SELECT * FROM Inscriere I WHERE I.idParticipant=? AND I.idProba=?")) {
            s.setString(1, id.getFirst());
            s.setInt(2,id.getSecond());
            ResultSet resultSet = s.executeQuery();
            inscriere = new Inscriere(resultSet.getString("idParticipant"),resultSet.getInt("idProba"));
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return inscriere;
    }

    @Override
    public Iterable<Inscriere> findAll() {
        List<Inscriere> all = new ArrayList<>();
        try (Statement s = connection.createStatement()) {
            try (ResultSet resultSet = s.executeQuery("SELECT * FROM  main.Inscriere")) {
                while (resultSet.next()) {
                    all.add(new Inscriere(resultSet.getString("idParticipant"),resultSet.getInt("idProba")));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return all;
    }

    @Override
    public Integer getNrParticipantiProba(Integer idProba) {
        Iterable<Participant> all = getParticipantiPtProba(idProba);
        Integer size=0;
        for(Participant p:all)
            size++;
        return size;
    }

    @Override
    public Iterable<Participant> getParticipantiPtProba(Integer idProba) {
        List<Participant> all = new ArrayList<>();
        try (PreparedStatement s = connection.prepareStatement("SELECT P2.id,P2.nume,P2.varsta " +
                "FROM  Inscriere I INNER JOIN Participant P2 ON I.idParticipant = P2.id WHERE I.idProba=?")) {
            s.setInt(1,idProba);
            ResultSet resultSet = s.executeQuery();
            while (resultSet.next()) {
                all.add(new Participant(resultSet.getString("id"),resultSet.getString("nume"),resultSet.getInt("varsta")));
            }
        }
        catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return all;

    }

    @Override
    public Iterable<Proba> getProbePtParticipant(String idParticipant) {
        List<Proba> all = new ArrayList<>();
        try (PreparedStatement s = connection.prepareStatement("SELECT P2.id,P2.nume,P2.distanta " +
                "FROM  Inscriere I INNER JOIN main.Proba P2 ON I.idProba = P2.id " +
                "WHERE I.idParticipant=?")) {
            s.setString(1,idParticipant);
            ResultSet resultSet = s.executeQuery();
            while (resultSet.next()) {
                all.add(new Proba(resultSet.getInt("id"),resultSet.getString("nume"),resultSet.getFloat("distanta")));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return all;
    }
}
