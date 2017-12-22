package school_admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import programming_school.Solution;
import programming_school.User;

public class UserApplication {

    private static final String MENU_OPTIONS = 
            "\nenter one of the following commands:\n" +
            " add \t- to add solution to one of your tasks\n" +
            " view \t- to view your solutions\n" +
            " quit \t- to exit program\n>> ";
    
//    private static Connection con;
    private static int userId;
    
    public static void main(String[] args) {
        
        if (testArg(args)) {
            try ( Connection con = DbUtils.getConnection()) {
                User user = User.loadById(con, userId);
                if ( user != null ) {
                    mainLoop(con);
                }
            } catch (SQLException e) {
                System.out.println("Connection problem: " + e.getMessage());
            }
        }
 
    }
    
    private static void mainLoop(Connection con) {
        try (Scanner scan = new Scanner(System.in)) {
            boolean loop = true;
            while (loop) {
                System.out.print(MENU_OPTIONS);
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
            }
        }
    }

    private static void view(Connection con, Scanner scan) {
        System.out.println("Your solutions:");
        try {
            Solution[] solutions = Solution.loadAllByUserId(con, userId);
            if ( solutions != null) {
                for (Solution s : solutions) {
                    System.out.println(s.toString());
                }
            } else {
                System.out.println("You have no solutions.");
            }
        } catch (SQLException e) {
            System.out.println("There was a problem while loading your solutions: " + e.getMessage());
        }
    }
    
    private static void add(Connection con, Scanner scan) {
        
        try {
            Solution[] unsolved = Solution.loadUnsolvedByUserId(con, userId);
            if (unsolved != null) {
                System.out.println("Unsolved exercises:");
                int[] unsolvedIndexes = new int[unsolved.length];
                for (int i = 0 ; i < unsolved.length ; i++) {
                    System.out.println(unsolved[i].toString() + " -->" + unsolved[i].getId());
                    unsolvedIndexes[i] = unsolved[i].getId();
                }
                System.out.println("Choose which one you would like to edit: ");
                int id = getIdFromArray(scan, unsolvedIndexes);
                System.out.println("Enter your solution, type 'quit' to exit");
                Solution.loadById(con, id).setDescription(createDescription(scan)).saveToDb(con);
            } else {
                System.out.println("You have no unsolved exercises :)");
            }

        } catch (SQLException e) {
            System.out.println("There was a problem while loading your solutions: " + e.getMessage());
        }
    }
    
    public static int getIdFromArray(Scanner scan, int[] array) {
        while (true) {
            while ( !scan.hasNextInt() ) {
                System.out.println( scan.next() + " is not a valid number");
            }
            int id = scan.nextInt();
            scan.nextLine();
            boolean exists = false;
            for (int i = 0 ; i < array.length ; i++) {
                System.out.println("id=" + id + " array: " + array[i]);
                if ( array[i] == id ) {
                    exists = true;
                    break;
                }
            }
            if ( exists ) {
                return id;
            } else {
                System.out.println("there is no unsolved exercise with that id");
            }
        }
    }
    
    private static boolean testArg(String[] args) {
        boolean res = false;
        if ( args.length < 1 ) {
            System.out.println("No arguments. Quiting application...");
        } else {
            try {
                userId = Integer.parseInt(args[0]);
                res = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid argument.");
            }
        }
        return res;
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

}
