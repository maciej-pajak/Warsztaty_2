package programming_school;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        
        
        try ( Connection con = DriverManager.getConnection("jdbc:mysql://localhost/mydb?useSSL=false", "root", "coderslab") ) {
            
            for (User u : User.loadAll(con)) {
                System.out.println(u.toString());
            }
            Group g = Group.loadById(con, 1);
 
            System.out.println(g.toString());
            
            for (Group u : Group.loadAll(con)) {
                System.out.println(u.toString());
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
