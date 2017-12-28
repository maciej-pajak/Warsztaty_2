package pl.maciejpajak.schoolAdmin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import pl.maciejpajak.codingSchool.Exercise;
import pl.maciejpajak.codingSchool.Solution;
import pl.maciejpajak.codingSchool.User;

public class SolutionAdmin {
    
    private static final String MENU_OPTIONS = 
            "\nenter one of the following commands:\n" +
            " add \t- to bind users with soultions\n" +
            " view \t- to view user's solution\n" +
            " quit \t- to exit program\n>> ";
    
    /**
     * Simple administration program, which allows to add, edit or delete exercises from database.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("SolutionAdmin ver. 0.1");
        try ( Connection  con = DbUtils.getConnection() ) {
            
            Scanner scan = new Scanner(System.in);
            boolean loop = true;
            while (loop) {
                System.out.print(MENU_OPTIONS);
                try {
                    switch (scan.nextLine()) {
                    case "add":
                        add(con, scan);
                        break;
                    case "view":
                        view(con, scan);
                        break;
                    case "quit":
                        loop = false;
                        break;
                    default:
                        System.out.println("invalid command, try again\n");
                    }
                } catch (SQLException e) {
                    System.out.print("Error:\t");
                    System.out.println(e.getMessage());
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("quitting...");
    }
    
    /**
     * Creates new solution based on console input and adds it to database.
     * @param con
     * @param scan
     * @throws SQLException
     */
    private static void add(Connection con, Scanner scan) throws SQLException {
        UserAdmin.loadUsers(con);
        System.out.println("enter user id: ");
        User user = UserAdmin.getById(con, scan);
        
        ExerciseAdmin.loadExercises(con);
        System.out.println("enter exercise id: ");
        Exercise exercise = ExerciseAdmin.getById(con, scan);
        Solution sol = new Solution(user.getId(), exercise.getId());
        System.out.println("\nSavig new solution for exercise " + exercise.getId() + " from user " + user.getId());
        sol.saveToDb(con);
    }
    
    /**
     * shows every solution from specific user
     * @param con
     * @param scan
     * @throws SQLException
     */
    private static void view(Connection con, Scanner scan) throws SQLException {
        UserAdmin.loadUsers(con);
        System.out.println("enter user id: ");
        User user = UserAdmin.getById(con, scan);
        if ( user != null) {
            Solution[] solutions = Solution.loadAllByUserId(con, user.getId());
            System.out.println("user's solutions: ");
            for (Solution s : solutions) {
                System.out.println(s.toString());
            }
        } else {
            System.out.println("no user selected");
        }
    }
    
}
