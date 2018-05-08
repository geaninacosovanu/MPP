package repository;

import model.Proba;
import org.springframework.stereotype.Component;
import validator.ValidationException;
import validator.Validator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
@Component
public class ProbaDBRepository implements IProbaRepository {
    private Validator<Proba> probaValidator;
    private Connection connection;

    public ProbaDBRepository(Validator<Proba> probaValidator, String propFile) {
        this.probaValidator = probaValidator;
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
            try (ResultSet resultSet = s.executeQuery("SELECT count(*) as Nr from main.Proba")) {
                size = resultSet.getInt("Nr");
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return size;
    }

    @Override
    public void save(Proba entity) throws ValidationException {
        probaValidator.validate(entity);
        try (PreparedStatement s = connection.prepareStatement("insert into Proba(nume,distanta) values (?,?)")) {
           // s.setInt(1,entity.getId());
            s.setString(1, entity.getNume());
            s.setFloat(2, entity.getDistanta());
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Proba exista deja!");
        }
    }

    @Override
    public void delete(Integer id) {
        try (PreparedStatement s = connection.prepareStatement("DELETE FROM Proba WHERE id=?")) {
            s.setInt(1, id);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public void update(Integer id, Proba entity) throws ValidationException {
        probaValidator.validate(entity);
        try (PreparedStatement s = connection.prepareStatement("UPDATE Proba SET nume=?, distanta=? WHERE id=?")) {
            s.setString(1, entity.getNume());
            s.setFloat(2, entity.getDistanta());
            s.setInt(3, id);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Proba findOne(Integer id) {
        Proba proba = null;
        try (PreparedStatement s = connection.prepareStatement("SELECT P.id,P.nume,P.distanta FROM main.Proba P WHERE P.id=?")) {
            s.setInt(1, id);
            ResultSet resultSet = s.executeQuery();
            proba = new Proba(resultSet.getInt("id"), resultSet.getString("nume"), resultSet.getFloat("distanta"));
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return proba;
    }

    @Override
    public Iterable<Proba> findAll() {
        List<Proba> all = new ArrayList<>();
        try (Statement s = connection.createStatement()) {
            try (ResultSet result = s.executeQuery("SELECT * FROM  main.Proba")) {
                while (result.next()) {
                    all.add(new Proba(result.getInt("id"), result.getString("nume"), result.getFloat("distanta")));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return all;
    }

}
