package programming_school;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Exercise {
    
    private static final String LOAD_BY_ID_QUERY = "SELECT * FROM exercise WHERE id=?;";
    private static final String LOAD_ALL_QUERY = "SELECT * FROM exercise;";
    private static final String LOAD_BY_USER_ID_QUERY = "SELECT exercise.id, title, exercise.description FROM exercise JOIN solution ON exercise.id=solution.exercise_id JOIN user ON solution.user_id=user.id WHERE user.id=?;";
    private static final String DELETE_QUERY = "DELETE FROM exercise WHERE id=?;";
    private static final String CREATE_QUERY = "INSERT INTO exercise (title, description) VALUES(?, ?);";
    private static final String UPDATE_QUERY = "UPDATE exercise SET title=?, description=? WHERE id=?;";
    
    private int id;
    private String title;
    private String description;
    
    public Exercise(String title, String description) {
        this(0, title, description);
    }
    
    private Exercise(int id, String title, String description) {
        setId(id);
        setTitle(title);
        setDescription(description);
    }
    
    private Exercise(ResultSet rs) throws SQLException {
        this( rs.getInt("id"), rs.getString("title"), rs.getString("description") );
    }

    public static Exercise loadById(Connection con, int id) throws SQLException {
        PreparedStatement ps = con.prepareStatement(LOAD_BY_ID_QUERY);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        
        if ( rs.next() ) {
            return new Exercise(rs);
        }
        return null;
    }
    
    public static Exercise[] loadAll(Connection con) throws SQLException {
        List<Exercise> solutionList = new ArrayList<Exercise>();
        ResultSet rs = con.prepareStatement(LOAD_ALL_QUERY).executeQuery();
        
        while ( rs.next() ) {
            solutionList.add( new Exercise(rs) );
        }
       
        return solutionList.toArray(new Exercise[solutionList.size()]);
    }

    public static Exercise[] loadAllByUserId(Connection con, int id) throws SQLException {
        List<Exercise> solutionList = new ArrayList<Exercise>();
        PreparedStatement ps = con.prepareStatement(LOAD_BY_USER_ID_QUERY);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while ( rs.next() ) {
            solutionList.add( new Exercise(rs) );
        }
        
        return solutionList.toArray(new Exercise[solutionList.size()]);
    }

    public void delete(Connection con) throws SQLException {
        if ( this.id != 0 ) {
            PreparedStatement ps = con.prepareStatement(DELETE_QUERY);
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
        String[] genereatedColumns = { "id" };
        PreparedStatement ps = con.prepareStatement(CREATE_QUERY, genereatedColumns);
        ps.setString(1, this.getTitle() );
        ps.setString(2, this.getDescription());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if ( rs.next() ) {
            this.id = rs.getInt(1);
        }    
    }

    private void updateExistingInDb(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, this.getTitle() );
        ps.setString(2, this.getDescription());
        ps.setInt(3, this.getId());
        ps.executeUpdate();
    }
    
    public int getId() {
        return id;
    }
    
    private void setId(int id) {
        this.id = id;
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
    
    @Override
    public String toString() {
        return this.id + " " + this.title + " " + this.description;  // TODO improve
    }
    

}
