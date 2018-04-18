package repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Properties properties;
    private Connection instance=null;
    public JdbcUtils(Properties properties) {
        this.properties = properties;
    }

    private Connection getNewConnection(){
        String driver=properties.getProperty("jdbc.driver");
        String url=properties.getProperty("jdbc.url");
        Connection connection = null;
        try {
            Class.forName(driver);
            connection= DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return connection;
    }


    public Connection getConnection() {
        try {
            if(instance==null||instance.isClosed())
                instance=getNewConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return instance;
    }
}
