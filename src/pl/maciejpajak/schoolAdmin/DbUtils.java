package pl.maciejpajak.schoolAdmin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {
    
    private static final String DB_URL = "jdbc:mysql://localhost/coding_school?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "coderslab";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

}
