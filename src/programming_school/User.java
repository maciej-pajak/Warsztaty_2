package programming_school;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class User {
    
    private int id = 0;
    private String username;
    private String email;
    private String password;
    
    // create new
    public User(String username, String email, String password) {
        this(0, username, email, password);
    }
    
    
    private User(int id, String username, String email, String password) {
        setUsername(username);
        setEmail(email);
        this.id = id;
        if ( id == 0 ) {
            setPassword(password);
        } else {
            this.password = password;   // TODO check
        }
    }
    
    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    private String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        return this;
    }
    
    public void saveToDb(Connection con) throws SQLException {
        if ( this.id == 0 ) {   // create new
            saveNewToDb(con);
        } else {                // update existing
            updateExistingInDb(con);
        }
    }
    
    private void saveNewToDb(Connection con) throws SQLException {
        String sql = "INSERT INTO user VALUES(null, ?, ?, ?);";
        String[] genereatedColumns = { "id" }; // TODO rethink
        PreparedStatement ps = con.prepareStatement(sql, genereatedColumns);
        ps.setString(1, this.getUsername());
        ps.setString(2, this.getEmail());
        ps.setString(3, this.getPassword());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if ( rs.next() ) {
            this.id = rs.getInt(1);
        }
    }
    
    private void updateExistingInDb(Connection con) throws SQLException {
       String sql = "UPDATE user SET username=?, email=?, password=? WHERE id=?;";
       PreparedStatement ps = con.prepareStatement(sql);
       ps.setString(1, this.getUsername());
       ps.setString(2, this.getEmail());
       ps.setString(3, this.getPassword());
       ps.setInt(4, this.getId());
       ps.executeUpdate();
    }
    
    public void delete(Connection con) throws SQLException {
        if ( this.id != 0 ) {
            String sql = "DELETE FROM user WHERE id=?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, this.id);
            ps.executeUpdate();
            this.id = 0;
        }
    }
    
    public static User[] loadAll(Connection con) throws SQLException {
        List<User> usersList = new ArrayList<User>();
        String sql = "SELECT * FROM user;";
        ResultSet rs = con.prepareStatement(sql).executeQuery();
        
        while ( rs.next() ) {
            usersList.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password")));
        }
       
        return usersList.toArray(new User[usersList.size()]);
    }
    
    public static User loadById(Connection con, int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id=?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        
        if ( rs.next() ) {
            return new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"));
        }
        return null;
    }
    
    @Override
    public String toString() {      // TODO improve?
        StringBuilder sb = new StringBuilder();
        sb.append(this.getUsername()).append('\t').append(this.getEmail());
        return sb.toString();
    }

}

