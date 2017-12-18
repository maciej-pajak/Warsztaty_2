package programming_school;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Exercise {
    
    private int id;
    private String title;
    private String description;
    private int user_id;
    private int solution_id;
    
    public Exercise(String title, String description, int user_id, int solution_id) {
        this(0, title, description, user_id, solution_id);
    }
    
    private Exercise(int id, String title, String description, int user_id, int solution_id) {  
        setTitle(title);
        setUserId(user_id);
        setDescription(description);
        setSolutionId(solution_id);
        this.id = id;
    }

    public static Exercise loadById(Connection con, int id) throws SQLException {
        final String sql = "SELECT * FROM exercise WHERE id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if ( rs.next() ) {
            return getFromResultSet(rs);
        }
        return null;
    }
    
    public static Exercise[] loadAll(Connection con) throws SQLException {
        List<Exercise> exerciseList = new ArrayList<Exercise>();
        final String sql = "SELECT * FROM exercise";
        ResultSet rs = con.prepareStatement(sql).executeQuery();
        while ( rs.next() ) {
            exerciseList.add(getFromResultSet(rs));
        }
        return exerciseList.toArray(new Exercise[exerciseList.size()]);
    }
    
    public static Exercise[] loadAllByUserId(Connection con, int id) throws SQLException {
        List<Exercise> exerciseList = new ArrayList<Exercise>();
        final String sql = "SELECT * FROM exercise WHERE user_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while ( rs.next() ) {
            exerciseList.add(getFromResultSet(rs));
        }
        return exerciseList.toArray(new Exercise[exerciseList.size()]);
    }
    
    private static Exercise getFromResultSet(ResultSet rs) throws SQLException {
        return new Exercise(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getInt("user_id"), rs.getInt("solution_id"));
    }
    
    public void saveToDb(Connection con) throws SQLException {
        if ( this.id == 0 ) {   // create new
            saveNewToDb(con);
        } else {                // update existing
            updateExistingInDb(con);
        }
    }
    
    private void saveNewToDb(Connection con) throws SQLException {
        String sql = "INSERT INTO exercise VALUES(null, ?, ?, ?, ?);";
        String[] genereatedColumns = { "id" };
        PreparedStatement ps = con.prepareStatement(sql, genereatedColumns);
        ps.setString(1, this.getTitle());
        ps.setString(2, this.getDescription());
        ps.setInt(3, this.getUserId());
        ps.setInt(4, this.getSolutionId());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if ( rs.next() ) {
            this.id = rs.getInt(1);
        }    
    }
    
    private void updateExistingInDb(Connection con) throws SQLException {
        String sql = "UPDATE exercise SET title=?, description=?, user_id=?, solution_id=? WHERE id=?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, this.getTitle());
        ps.setString(2, this.getDescription());
        ps.setInt(3, this.getUserId());
        ps.setInt(4, this.getSolutionId());
        ps.setInt(5, this.getId());
        ps.executeUpdate();
    }
    
    public void delete(Connection con) throws SQLException {
        if ( this.id != 0 ) {
            String sql = "DELETE FROM exercise WHERE id=?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, this.id);
            ps.executeUpdate();
            this.id = 0;
        }
    }
    
    @Override
    public String toString() {
        return String.format("%-4s %-4s %-8s %-20s %s", getId(), getUserId(), getSolutionId(), getTitle(), getDescription());
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public int getSolutionId() {
        return solution_id;
    }

    public void setSolutionId(int solution_id) {
        this.solution_id = solution_id;
    }

    public int getId() {
        return id;
    }
    
    

}
