package programming_school;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Group {
    
    private int id;
    private String name;
    
    
    public Group(String name) {
        this(0, name);
    }
    
    private Group(int id, String name) {
        this.setName(name);
        this.id = id;
    }
    
    public static Group[] loadAll(Connection con) throws SQLException {
        List<Group> groupList = new ArrayList<Group>();
        String sql = "SELECT * FROM user_group;";
        ResultSet rs = con.prepareStatement(sql).executeQuery();
        
        while ( rs.next() ) {
            groupList.add( new Group(rs.getInt("id"), rs.getString("name")) );
        }
       
        return groupList.toArray(new Group[groupList.size()]);
    }
    
    public static Group loadById(Connection con, int id) throws SQLException {
        String sql = "SELECT * FROM user_group WHERE id=?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        
        if ( rs.next() ) {
            Group gr = new Group(rs.getString("name"));
            gr.id = rs.getInt("id");
            return gr;
        }
        return null;
    }
    
    public void saveToDb(Connection con) throws SQLException {
        if ( this.id == 0 ) {   // create new
            saveNewToDb(con);
        } else {                // update existing
            updateExistingInDb(con);
        }
    }
    
    private void saveNewToDb(Connection con) throws SQLException {
        String sql = "INSERT INTO user_group VALUES(null, ?);";
        String[] genereatedColumns = { "id" }; // TODO rethink
        PreparedStatement ps = con.prepareStatement(sql, genereatedColumns);
        ps.setString(1, this.getName());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if ( rs.next() ) {
            this.id = rs.getInt(1);
        }    
    }
    
    private void updateExistingInDb(Connection con) throws SQLException {
        String sql = "UPDATE user_group SET name=? WHERE id=?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, this.getName());
        ps.setInt(2, this.getId());
        ps.executeUpdate();
    }
    
    public void delete(Connection con) throws SQLException {
        if ( this.id != 0 ) {
            String sql = "DELETE FROM group_user WHERE id=?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, this.id);
            ps.executeUpdate();
            this.id = 0;
        }
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    @Override
    public String toString() { // TODO improve ?
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append('\t').append(getId());
        return sb.toString();
    }
    
}
