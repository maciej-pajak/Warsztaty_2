package programming_school;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    
    private static final String LOAD_BY_ID_QUERY = "SELECT * FROM solution WHERE id=?;";
    private static final String LOAD_ALL_QUERY = "SELECT * FROM solution;";
    private static final String LOAD_BY_USER_ID_QUERY = "SELECT * FROM solution WHERE user_id=?;";
    private static final String LOAD_UNSOLVED_BY_USER_ID_QUERY = "SELECT * FROM solution WHERE user_id=? AND updated IS NULL;";
    private static final String DELETE_QUERY = "DELETE FROM solution WHERE id=?;";
    private static final String CREATE_QUERY = "INSERT INTO solution (created, user_id, exercise_id) VALUES(?, ?, ?);";
    private static final String UPDATE_QUERY = "UPDATE solution SET updated=?, description=? WHERE id=?;";
    private static final String LOAD_BY_EXERCISE_ID_QUERY = "SELECT * FROM solution JOIN exercise ON solution.id=exercise.soultion_id WHERE exercise.id=?;";
    
    private int id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String description;
    private int user_id;
    private int exercise_id;
    
    public Solution(int user_id, int exercise_id) {
        this.id = 0;
        setUserId(user_id);
        setExerciseId(exercise_id);
    }
    
    public Solution(String created, String updated, String description, int user_id, int exercise_id) {
        this(0, created, updated, description, user_id, exercise_id);
    }
    
    private Solution(int id, String created, String updated, String description, int user_id, int exercise_id) {
        this.id = id;
        setCreated(created);
        setUpdated(updated);
        setDescription(description);
        setUserId(user_id);
        setExerciseId(exercise_id);
    }

    public static Solution loadById(Connection con, int id) throws SQLException {
        PreparedStatement ps = con.prepareStatement(LOAD_BY_ID_QUERY);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if ( rs.next() ) {
            return getFromResultSet(rs);
        }
        return null;
    }
    
    public static Solution[] loadAll(Connection con) throws SQLException {
        List<Solution> exerciseList = new ArrayList<Solution>();
        ResultSet rs = con.prepareStatement(LOAD_ALL_QUERY).executeQuery();
        while ( rs.next() ) {
            exerciseList.add(getFromResultSet(rs));
        }
        return exerciseList.toArray(new Solution[exerciseList.size()]);
    }
    
    public static Solution[] loadAllByUserId(Connection con, int id) throws SQLException {
        List<Solution> exerciseList = new ArrayList<Solution>();
        PreparedStatement ps = con.prepareStatement(LOAD_BY_USER_ID_QUERY);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while ( rs.next() ) {
            exerciseList.add(getFromResultSet(rs));
        }
        return exerciseList.toArray(new Solution[exerciseList.size()]);
    }
    
    public static Solution[] loadUnsolvedByUserId(Connection con, int id) throws SQLException {
        List<Solution> exerciseList = new ArrayList<Solution>();
        PreparedStatement ps = con.prepareStatement(LOAD_UNSOLVED_BY_USER_ID_QUERY);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while ( rs.next() ) {
            exerciseList.add(getFromResultSet(rs));
        }
        return exerciseList.toArray(new Solution[exerciseList.size()]);
    }

    public static Solution[] loadAllByExerciseId(Connection con, int id) throws SQLException {
        List<Solution> exerciseList = new ArrayList<Solution>();
        PreparedStatement ps = con.prepareStatement(LOAD_BY_EXERCISE_ID_QUERY);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while ( rs.next() ) {
            exerciseList.add(getFromResultSet(rs));
        }
        return exerciseList.toArray(new Solution[exerciseList.size()]);
    }
    
    private static Solution getFromResultSet(ResultSet rs) throws SQLException {
        return new Solution( rs.getInt("id"), rs.getString("created"), rs.getString("updated"), rs.getString("description"), rs.getInt("user_id"), rs.getInt("exercise_id") );
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
        this.created = LocalDateTime.now();
        ps.setString(1, this.getCreated());
        ps.setInt(2, this.getUserId());
        ps.setInt(3, this.getExerciseId());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if ( rs.next() ) {
            this.id = rs.getInt(1);
        }    
    }
    
    private void updateExistingInDb(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(UPDATE_QUERY);
        this.updated = LocalDateTime.now();
        ps.setString(1, this.getUpdated());
        ps.setString(2, this.getDescription());
        ps.setInt(3, this.getId());
        ps.executeUpdate();
    }
    
    public void delete(Connection con) throws SQLException {
        if ( this.id != 0 ) {
            PreparedStatement ps = con.prepareStatement(DELETE_QUERY);
            ps.setInt(1, this.id);
            ps.executeUpdate();
            this.id = 0;
        }
    }
    
    private static String LocalDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return dateTime.format(formatter);
        }
    }
    
    private static LocalDateTime StringToLocalDateTime(String dateTimeStr) {
        if (dateTimeStr == null) {
            return null;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.S]");
            return LocalDateTime.parse(dateTimeStr, formatter);
        }
    }
    
    @Override
    public String toString() {
        return String.format("%-2s %-2s %-2s %-20s %-20s %s", getId(), getUserId(), getExerciseId(), getCreated(), getUpdated(), getDescription()); // FIXME
    }
    
    public String getCreated() {
        return LocalDateTimeToString(created);
    }

    private Solution setCreated(String created) {
        this.created = StringToLocalDateTime(created);
        return this;
    }

    public String getUpdated() {
        return LocalDateTimeToString(updated);
    }

    private Solution setUpdated(String updated) {
        this.updated = StringToLocalDateTime(updated);
        return this;
    }
       
    public String getDescription() {
        return description;
    }

    public Solution setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getUserId() {
        return user_id;
    }

    public Solution setUserId(int user_id) {
        this.user_id = user_id;
        return this;
    }

    public int getExerciseId() {
        return exercise_id;
    }

    public Solution setExerciseId(int exercise_id) {
        this.exercise_id = exercise_id;
        return this;
    }

    public int getId() {
        return id;
    }

}
