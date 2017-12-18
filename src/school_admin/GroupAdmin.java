package school_admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import programming_school.Group;

public class GroupAdmin {
    
    private static String MENU_OPTIONS = 
            "\nenter one of the following commands:\n" +
            " add \t- to add group user\n" +
            " edit \t- to edit existing group\n" +
            " delete \t- to remove group\n" +
            " quit \t- to exit program\n>> ";
    
    private static Group[] groups;
    
    /**
     * Simple administration program, which allows to add, edit or delete users from database.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("GroupAdmin ver. 0.1");
        try ( Connection  con = DriverManager.getConnection("jdbc:mysql://localhost/mydb?useSSL=false", "root", "coderslab") ) {        // TODO edit database name
            
            Scanner scan = new Scanner(System.in);
            boolean loop = true;
            while (loop) {
                groups = Group.loadAll(con);
                System.out.println("\nGroups in database:");
                System.out.printf("%-4s %s\n", "ID", "Name");
                for (Group g : groups) {
                    System.out.println(g.toString());
                }
                System.out.print(MENU_OPTIONS);
                switch ( scan.nextLine() ) {
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
            }
            
        } catch (SQLException e) {
            System.out.print("Error:\t");
            System.out.println(e.getMessage());
        }
        System.out.println("quitting...");
    }
    
    /**
     * Creates new group based on console input and adds it to database.
     * @param con
     * @param scan
     * @throws SQLException
     */
    private static void add(Connection con, Scanner scan) throws SQLException {
        System.out.println("\nenter group details:");
        System.out.print(" * name:\t");
        String name = scan.nextLine();     
        Group user = new Group(name);
        user.saveToDb(con);
    }
    
    /**
     * Edits group from database based on console input.
     * @param con
     * @param scan
     * @throws SQLException
     */
    private static void edit(Connection con, Scanner scan) throws SQLException {
        if ( groups.length < 1 ) {
            System.out.println("there is no group to edit, add group first");
        } else {
            System.out.print("\nenter id of group you want to edit: ");
            Group res = getById(con, scan);
            System.out.println("editing group with id = " + res.getId());
            System.out.print(" * enter group name: ");
            res.setName(scan.nextLine());
            res.saveToDb(con);
        }
    }
    
    /**
     * Removes group from database based on id from console input.
     * @param con
     * @param scan
     * @throws SQLException
     */
    private static void delete(Connection con, Scanner scan) {
        System.out.print("enter id of group you want to delete: ");
        try {
            getById(con, scan).delete(con);
        } catch (SQLException e) {
            System.out.print("Error:\t");
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Asks for id until group with that id is found in database.
     * @param con
     * @param scan
     * @return {@code Group} with id read from console input
     * @throws SQLException
     */
    private static Group getById(Connection con, Scanner scan) throws SQLException {
        Group res = null;
        while (true) {
            while ( !scan.hasNextInt() ) {
                System.out.println( scan.next() + " is not a valid number");
            }
            res = Group.loadById( con, scan.nextInt() );
            scan.nextLine();
            if ( res == null ) {
                System.out.println("there is no group with that id");
            } else {
                break;
            }
        }
        return res;
    }
}
