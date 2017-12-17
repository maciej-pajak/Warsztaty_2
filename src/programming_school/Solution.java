package programming_school;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    
    private int id;
    private String created; // TODO type? Date of String?? if Date update constructor
    private String updated; // TODO
    private String description;
    
    public Solution(String created, String updated, String description) {
        this(0, created, updated, description);
    }
    
    private Solution(int id, String created, String updated, String description) {
        this.id = id;
        setCreated(created);
        setUpdated(updated);
        setDescription(description);
    }
    
    public static Solution loadById(Connection con, int id) throws SQLException {
        String sql = "SELECT * FROM solution WHERE id=?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        
        if ( rs.next() ) {
            Solution sol = new Solution(rs.getInt("id"), rs.getString("created"), rs.getString("updated"), rs.getString("description"));
            return sol;
        }
        return null;
    }
    
    public static Solution[] loadAll(Connection con) throws SQLException {
        List<Solution> solutionList = new ArrayList<Solution>();
        String sql = "SELECT * FROM solution;";
        ResultSet rs = con.prepareStatement(sql).executeQuery();
        
        while ( rs.next() ) {
            solutionList.add( new Solution(rs.getInt("id"), rs.getString("created"), rs.getString("updated"), rs.getString("description")) );
        }
       
        return solutionList.toArray(new Solution[solutionList.size()]);
    }
    
    public void delete(Connection con) throws SQLException {
        if ( this.id != 0 ) {
            String sql = "DELETE FROM solution WHERE id=?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, this.id);
            ps.executeUpdate();
            this.id = 0;
        }
    }
    
    public void saveToDb(Connection con) throws SQLException {
        if ( this.id == 0 ) {   // create new
            saveNewToDb(con);
        } else {                // update existing
            updateExistingInDb(con);
        }
    }
    
    private void saveNewToDb(Connection con) throws SQLException {
        String sql = "INSERT INTO solution VALUES(null, ?, ?, ?);";
        String[] genereatedColumns = { "id" }; // TODO rethink
        PreparedStatement ps = con.prepareStatement(sql, genereatedColumns);
        ps.setString(1, this.getCreated());
        ps.setString(2, this.getUpdated());
        ps.setString(3, this.getDescription());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if ( rs.next() ) {
            this.id = rs.getInt(1);
        }    
    }
    
    private void updateExistingInDb(Connection con) throws SQLException {
        String sql = "UPDATE solution SET created=?, updated=?, description=? WHERE id=?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, this.getCreated());
        ps.setString(2, this.getUpdated());
        ps.setString(3, this.getDescription());
        ps.setInt(4, this.getId());
        ps.executeUpdate();
    }
    
    public int getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    

}
