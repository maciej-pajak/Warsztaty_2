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
    private int group_id;
    
    public User(String username, String email, String password, int group_id) {
        this(0, username, email, password, group_id);
    }
    
    private User(int id, String username, String email, String password, int group_id) {
        setUsername(username);
        setEmail(email);
        this.id = id;
        setGroupId(group_id);
        if ( id == 0 ) {
            setPassword(password);
        } else {
            this.password = password;
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
    
    public int getGroupId() {
        return group_id;
    }
    
    public User setGroupId(int group_id) {
        this.group_id = group_id;
        return this;
    }
    
    public void saveToDb(Connection con) throws SQLException {
        if ( this.id == 0 ) {   // create new
            saveNewToDb(con);
        } else {                // update existing
            updateExistingInDb(con);
        }
    }
    
    private static final String CREATE_USER_QUERY = "INSERT INTO user VALUES(null, ?, ?, ?, ?);";
    private static final String UPDATE_USER_QUERY = "UPDATE user SET username=?, email=?, password=? user_group_id=? WHERE id=?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM user WHERE id=?;";
    // TODO finish queries
    private void saveNewToDb(Connection con) throws SQLException {
        String[] genereatedColumns = { "id" };
        PreparedStatement ps = con.prepareStatement(CREATE_USER_QUERY, genereatedColumns);
        ps.setString(1, this.getUsername());
        ps.setString(2, this.getEmail());
        ps.setString(3, this.getPassword());
        ps.setInt(4, this.getGroupId());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if ( rs.next() ) {
            this.id = rs.getInt(1);
        }
    }
    
    private void updateExistingInDb(Connection con) throws SQLException {
       PreparedStatement ps = con.prepareStatement(UPDATE_USER_QUERY);
       ps.setString(1, this.getUsername());
       ps.setString(2, this.getEmail());
       ps.setString(3, this.getPassword());
       ps.setInt(4, this.getGroupId());
       ps.setInt(5, this.getId());
       ps.executeUpdate();
    }  
    
    public void delete(Connection con) throws SQLException {
        if ( this.id != 0 ) {
            PreparedStatement ps = con.prepareStatement(DELETE_USER_QUERY);
            ps.setInt(1, this.id);
            ps.executeUpdate();
            this.id = 0;
        }
    }
    // TODO add constructor form ResultSet
    public static User[] loadAll(Connection con) throws SQLException {
        List<User> usersList = new ArrayList<User>();
        String sql = "SELECT * FROM user;";
        ResultSet rs = con.prepareStatement(sql).executeQuery();
        
        while ( rs.next() ) {
            usersList.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getInt("user_group_id")));
        }
       
        return usersList.toArray(new User[usersList.size()]);
    }
    
    public static User loadById(Connection con, int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id=?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        
        if ( rs.next() ) {
            return new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getInt("user_group_id"));
        }
        return null;
    }
    
    public static User[] loadAllByGroupId(Connection con, int id) throws SQLException {
        List<User> usersList = new ArrayList<User>();
        String sql = "SELECT * FROM user WHERE user_group_id=?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        
        while ( rs.next() ) {
            usersList.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getInt("user_group_id")));
        }
       
        return usersList.toArray(new User[usersList.size()]);
    }
    
    @Override
    public String toString() { 
        return String.format( "%-4s %-4s %-20s %s", getId(), getGroupId(), getUsername(), getEmail() );
    }

}

