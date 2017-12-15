package programming_school;

public class Solution {
    
    private int id;
    private String created; //DAtetime
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
