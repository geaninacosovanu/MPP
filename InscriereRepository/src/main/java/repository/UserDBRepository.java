package repository;

import model.User;
import validator.ValidationException;
import validator.Validator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDBRepository implements IUserRepository {
    private Validator<User> validator;
    private Connection connection;

    public UserDBRepository(Validator<User> validator,String propFile) {
        this.validator=validator;
        Properties prop= new Properties();
        try {
            prop.load(new FileReader(new File(propFile).getAbsolutePath()));
            JdbcUtils jdbc=new JdbcUtils(prop);
            connection=jdbc.getConnection();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Integer size() {
        Integer size=null;
        try(Statement s = connection.createStatement()) {
            try(ResultSet result=s.executeQuery("SELECT count(*) as nr FROM  User")){
                size=result.getInt("nr");
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return size;
    }

    @Override
    public void save(User entity) throws ValidationException {
        validator.validate(entity);
        String generatedPassword=null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(entity.getParola().getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try (PreparedStatement s = connection.prepareStatement("insert into user(userId, parola) values (?,?)")){
            s.setString(1,entity.getId());
            s.setString(2,generatedPassword);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

    }

    @Override
    public void delete(String userId) {
        try (PreparedStatement s = connection.prepareStatement("DELETE FROM User WHERE userId=?")){
            s.setString(1,userId);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

    }

    @Override
    public void update(String userId, User entity) throws ValidationException {
        validator.validate(entity);
        String generatedPassword=null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(entity.getParola().getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try ( PreparedStatement s = connection.prepareStatement("UPDATE User SET parola=? WHERE userId=?")) {
            s.setString(1,generatedPassword);
            s.setString(2,userId);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public User findOne(String userId) {
        User user=null;
        try (PreparedStatement s = connection.prepareStatement("SELECT U.userId,U.parola FROM User U WHERE U.userId=?")){
            s.setString(1,userId);
            ResultSet resultSet =s.executeQuery();
            user = new User(resultSet.getString("userId"),resultSet.getString("parola"));
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return user;
    }

    @Override
    public Iterable<User> findAll() {
        List<User> all=new ArrayList<>();
        try(Statement s = connection.createStatement()) {
            try(ResultSet result=s.executeQuery("SELECT * FROM  User")){
                while(result.next()){
                    all.add(new User(result.getString("userId"),result.getString("parola")));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return all;
    }

    @Override
    public boolean exists(User u) {
        String generatedPassword=null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(u.getParola().getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try (PreparedStatement s = connection.prepareStatement("SELECT COUNT (*) AS Nr FROM User U WHERE U.userId=? AND U.parola=?")){
            s.setString(1,u.getId());
            s.setString(2,generatedPassword);
            ResultSet resultSet =s.executeQuery();
            resultSet.next();
            if(resultSet.getInt("Nr") == 0)
                return false;
            return true;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

    }
}