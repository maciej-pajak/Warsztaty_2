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
    private int user_id;
    private int solution_id;
    
    // constructors
    public Exercise(String title, int user_id, int solution_id) {
        this(0, title, user_id, solution_id);
    }
    
    private Exercise(int id, String title, int user_id, int solution_id) {  
        setTitle(title);            // TODO use public Exercise(..)
        setUser_id(user_id);
        setSolution_id(solution_id);
        this.id = id;
    }

    public static Exercise loadById(Connection con, int id) throws SQLException {
        final String sql = "SELECT * FROM exercise WHERE id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if ( rs.next() ) {
            return new Exercise(rs.getInt("id"), rs.getString("title"), rs.getInt("user_id"), rs.getInt("solution_id"));
        }
        return null;
    }
    
    public static Exercise[] loadAll(Connection con) throws SQLException {
        List<Exercise> exerciseList = new ArrayList<Exercise>();
        final String sql = "SELECT * FROM exercise";
        ResultSet rs = con.prepareStatement(sql).executeQuery();
        while ( rs.next() ) {
            exerciseList.add(new Exercise(rs.getInt("id"), rs.getString("title"), rs.getInt("user_id"), rs.getInt("solution_id")) );
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
            exerciseList.add(new Exercise(rs.getInt("id"), rs.getString("title"), rs.getInt("user_id"), rs.getInt("solution_id")) );
        }
        return exerciseList.toArray(new Exercise[exerciseList.size()]);
    }
    
    public void saveToDb(Connection con) throws SQLException {
        if ( this.id == 0 ) {   // create new
            saveNewToDb(con);
        } else {                // update existing
            updateExistingInDb(con);
        }
    }
    
    private void saveNewToDb(Connection con) throws SQLException {
        String sql = "INSERT INTO exercise VALUES(null, ?, ?, ?);";
        String[] genereatedColumns = { "id" }; // TODO rethink
        PreparedStatement ps = con.prepareStatement(sql, genereatedColumns);
        ps.setString(1, this.getTitle());
        ps.setInt(2, this.getUser_id());
        ps.setInt(3, this.getSolution_id());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if ( rs.next() ) {
            this.id = rs.getInt(1);
        }    
    }
    
    private void updateExistingInDb(Connection con) throws SQLException {
        String sql = "UPDATE exercise SET title=?, user_id=?, solution_id=? WHERE id=?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, this.getTitle());
        ps.setInt(2, this.getUser_id());
        ps.setInt(3, this.getSolution_id());
        ps.setInt(4, this.getId());
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
        StringBuilder sb = new StringBuilder();
        sb.append(title).append('\t').append(user_id).append('\t').append(solution_id);
        return sb.toString();
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSolution_id() {
        return solution_id;
    }

    public void setSolution_id(int solution_id) {
        this.solution_id = solution_id;
    }

    public int getId() {
        return id;
    }
    
    

}
