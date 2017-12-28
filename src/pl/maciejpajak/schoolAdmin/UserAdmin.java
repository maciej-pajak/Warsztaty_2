package pl.maciejpajak.schoolAdmin;

import java.io.Console;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import pl.maciejpajak.codingSchool.User;

public class UserAdmin {
    
    private static final String MENU_OPTIONS = 
            "\nenter one of the following commands:\n" +
            " add \t- to add new user\n" +
            " edit \t- to edit existing user\n" +
            " delete \t- to remove user\n" +
            " quit \t- to exit program\n>> ";
    
    private static User[] users;
    
    /**
     * Simple administration program, which allows to add, edit or delete groups from database.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("UserAdmin ver. 0.1");
        try ( Connection  con = DbUtils.getConnection() ) {
            
            Scanner scan = new Scanner(System.in);
            boolean loop = true;
            while (loop) {
                users = loadUsers(con);
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
            e.printStackTrace();
        }
        System.out.println("quitting...");
    }
    
    /**
     * Creates new user based on console input and adds it to database.
     * @param con
     * @param scan
     * @throws SQLException
     */
    private static void add(Connection con, Scanner scan) throws SQLException {
        System.out.println("\nenter user details:");
        System.out.print(" * username:\t");
        String username = scan.nextLine();
        System.out.print(" * email:\t");
        String email = scan.nextLine();
        System.out.print(" * password:\t");
        String password = getPasswordFromInput(scan);
        System.out.print(" * group id:\t");
        int gid;
        while ( !scan.hasNextInt() ) {
            System.out.println(scan.next() + " is not a number");
        }
        gid = scan.nextInt();
        scan.nextLine();
        User user = new User(username, email, password, gid);
        user.saveToDb(con);
    }
    
    /**
     * Edits user from database based on console input.
     * @param con
     * @param scan
     * @throws SQLException
     */
    private static void edit(Connection con, Scanner scan) throws SQLException {
        if ( users.length < 1 ) {
            System.out.println("there is no user to edit, add user first");
        } else {
            System.out.print("\nenter id of user you want to edit: ");
            User res = getById(con, scan);
            if ( res != null) {
                System.out.println("editing user with id = " + res.getId());
                System.out.print(" * enter new username: ");
                res.setUsername(scan.nextLine());
                System.out.print(" * enter new email: ");
                res.setEmail(scan.nextLine());
                System.out.print(" * enter new password: ");
                res.setPassword(getPasswordFromInput(scan));
                res.saveToDb(con);
            }
        }
    }
    
    /**
     * Removes user from database based on id from console input.
     * @param con
     * @param scan
     * @throws SQLException
     */
    private static void delete(Connection con, Scanner scan) {
        System.out.print("enter id of user you want to delete: ");
        try {
            getById(con, scan).delete(con);
        } catch (SQLException e) {
            System.out.print("Error:\t");
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Asks for id until user with that id is found in database.
     * @param con
     * @param scan
     * @return {@code User} with id read from console input
     * @throws SQLException
     */
    public static User getById(Connection con, Scanner scan) throws SQLException {
        User res = null;
        while (true) {
            while ( !scan.hasNextInt() ) {
                String tmp = scan.next();
                if (tmp.equals("x")) {
                    return null;
                }
                System.out.println( tmp + " is not a valid number, try again (or 'x' to exit)");
            }
            res = User.loadById( con, scan.nextInt() );
            scan.nextLine();
            if ( res == null ) {
                System.out.println("there is no user with that id");
            } else {
                break;
            }
        }
        return res;
    }
    
    private static String getPasswordFromInput(Scanner scan) {
        Console console = System.console();
        if (console != null) {
            return String.valueOf(console.readPassword());
        } else {
            System.out.println("could not initialize safe input, enter password in disclosed manner: ");
            return scan.nextLine();
        }
        
    }
    
    protected static User[] loadUsers(Connection con) throws SQLException {
        User[] users = User.loadAll(con);
        System.out.println("\nUsers in database:");
        System.out.printf("%-4s %-4s %-20s %s\n", "ID", "Group", "Username", "Email");
        for (User u : users) {
            System.out.println(u.toString());
        }
        return users;
    }
}
