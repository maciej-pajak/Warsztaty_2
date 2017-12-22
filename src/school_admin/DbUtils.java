package school_admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {
    
    private static String DB_URL = "jdbc:mysql://localhost/coding_school?useSSL=false";
    private static String DB_USER = "root";
    private static String DB_PASS = "coderslab";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();   // FIXME
        } catch (Exception e) {
            
        }
        
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

}
