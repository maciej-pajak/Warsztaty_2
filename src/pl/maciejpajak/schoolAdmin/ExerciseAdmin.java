package pl.maciejpajak.schoolAdmin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import pl.maciejpajak.codingSchool.Exercise;

public class ExerciseAdmin {
    
    private static final String MENU_OPTIONS = 
            "\nenter one of the following commands:\n" +
            " add \t- to add new exercise\n" +
            " edit \t- to edit existing exercise\n" +
            " delete \t- to remove exercise\n" +
            " quit \t- to exit program\n>> ";
    
    private static Exercise[] exercises;
    
    /**
     * Simple administration program, which allows to add, edit or delete exercises from database.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("ExerciseAdmin ver. 0.1");
        try ( Connection  con = DbUtils.getConnection() ) {
            
            Scanner scan = new Scanner(System.in);
            boolean loop = true;
            while (loop) {
                exercises = Exercise.loadAll(con);
                System.out.print(MENU_OPTIONS);
                try {
                    switch (scan.nextLine()) {
                    case "add":
                        add(con, scan);
                        break;
                    case "edit":
                        edit(con, scan);
                        break;
                    case "delete":
                        delete(con, scan);
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
                    e.printStackTrace();
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("quitting...");
    }
    
    /**
     * Creates new exercise based on console input and adds it to database.
     * @param con
     * @param scan
     * @throws SQLException
     */
    private static void add(Connection con, Scanner scan) throws SQLException { 
        System.out.print("\nenter title: ");
        String title = scan.nextLine();
        Exercise exerc = new Exercise(title, createDescription(scan));
        exerc.saveToDb(con);
    }
    
    /**
     * Edits exercise from database based on console input.
     * @param con
     * @param scan
     * @throws SQLException
     */
    private static void edit(Connection con, Scanner scan) throws SQLException {
        if ( exercises.length < 1 ) {
            System.out.println("there is no exercise to edit, add user first");
        } else {
            System.out.print("\nenter id of exercise you want to edit: ");
            Exercise res = getById(con, scan);
            
            System.out.println("editing exercise with id = " + res.getId());
            
            System.out.println("\nenter new title");
            String title = scan.nextLine();

            res.setDescription(createDescription(scan));
            res.setTitle(title);
            res.saveToDb(con);

        }
    }
    
    private static String createDescription(Scanner scan) {
        System.out.println("\nenter desciption (type 'quit' in new line to exit):");
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = scan.nextLine();
            if (line.equals("quit")) {
                break;
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Removes exercise from database based on id from console input.
     * @param con
     * @param scan
     * @throws SQLException
     */
    private static void delete(Connection con, Scanner scan) throws SQLException {
        System.out.print("enter id of exercise you want to delete: ");
        getById(con, scan).delete(con);
    }
    
    /**
     * Asks for id until exercise with that id is found in database.
     * @param con
     * @param scan
     * @return {@code Exercise} with id read from console input
     * @throws SQLException
     */
    public static Exercise getById(Connection con, Scanner scan) throws SQLException {
        Exercise res = null;
        while (true) {
            while ( !scan.hasNextInt() ) {
                System.out.println( scan.next() + " is not a valid number");
            }
            res = Exercise.loadById( con, scan.nextInt() );
            scan.nextLine();
            if ( res == null ) {
                System.out.println("there is no exercise with that id");
            } else {
                break;
            }
        }
        return res;
    }

    protected static Exercise[] loadExercises(Connection con) throws SQLException {
        Exercise[] exercises = Exercise.loadAll(con);
        System.out.println("\nExercises in database:");
        System.out.printf("%-4s %-10s %-10s %s\n", "ID", "Created", "Updated", "Description");  // TODO
        for (Exercise e : exercises) {
            System.out.println(e.toString());
        }
        return exercises;
    }
}
