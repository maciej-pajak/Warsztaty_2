package pl.maciejpajak.codingSchool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Active record class for group
 * 
 * @author maciej-pajak
 *
 */
public class Group {
    
    private static final String LOAD_ALL_QUERY = "SELECT * FROM user_group;";
    private static final String LOAD_BY_ID_QUERY = "SELECT * FROM user_group WHERE id=?;";
    private static final String CREATE_QUERY = "INSERT INTO user_group VALUES(null, ?);";
    private static final String UPDATE_QUERY = "UPDATE user_group SET name=? WHERE id=?;";
    private static final String DELETE_QUERY = "DELETE FROM user_group WHERE id=?;";
    
    private int id;
    private String name;
    
    
    public Group(String name) {
        this(0, name);
    }
    
    private Group(int id, String name) {
        this.setName(name);
        this.id = id;
    }
    
    private Group(ResultSet rs) throws SQLException {
        this(rs.getInt("id"), rs.getString("name"));
    }
    
    public static Group[] loadAll(Connection con) throws SQLException {
        List<Group> groupList = new ArrayList<Group>();
        
        try ( ResultSet rs = con.prepareStatement(LOAD_ALL_QUERY).executeQuery() ) {
            while ( rs.next() ) {
                groupList.add( new Group(rs) );
            }
        }
        
        return groupList.toArray(new Group[groupList.size()]);
    }
    
    public static Group loadById(Connection con, int id) throws SQLException {
        try ( PreparedStatement ps = con.prepareStatement(LOAD_BY_ID_QUERY) ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if ( rs.next() ) {
                return new Group(rs);
            }
            rs.close();
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
        String[] genereatedColumns = { "id" };
        try ( PreparedStatement ps = con.prepareStatement(CREATE_QUERY, genereatedColumns) ) {
            ps.setString(1, this.getName());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if ( rs.next() ) {
                this.id = rs.getInt(1);
            }
            rs.close();
        }
    }
    
    private void updateExistingInDb(Connection con) throws SQLException {
        try ( PreparedStatement ps = con.prepareStatement(UPDATE_QUERY) ) {
            ps.setString(1, this.getName());
            ps.setInt(2, this.getId());
            ps.executeUpdate();
        }
    }
    
    public void delete(Connection con) throws SQLException {
        if ( this.id != 0 ) {
            PreparedStatement ps = con.prepareStatement(DELETE_QUERY);
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
    public String toString() {
        return String.format("[%s, %s]", getId(), getName());
    }
    
}
