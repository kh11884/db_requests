package DBTools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    public Connection getDBConnecion() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/test_base";
        String login = "postgres";
        String password = "postgres";
        return DriverManager.getConnection(url, login, password);
    }
}
