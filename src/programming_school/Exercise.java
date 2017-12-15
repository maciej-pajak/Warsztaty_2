package programming_school;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Exercise {
    
    private int id;
    private String title;
    private int user_id;
    private int solution_id;
    
    public Exercise(String title, int user_id, int solution_id) {
        setTitle(title);
        setUser_id(user_id);
        setSolution_id(solution_id);
        this.id = 0;
    }
    
    private Exercise(int id, String title, int user_id, int solution_id) {  
        setTitle(title);            // TODO use public Exercise(..)
        setUser_id(user_id);
        setSolution_id(solution_id);
        this.id = id;
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
