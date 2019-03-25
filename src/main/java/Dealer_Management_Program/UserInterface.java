package Dealer_Management_Program;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface {

    enum UserType {
        UNKNOWN,
        CUSTOMER,
        DEALERSHIP_MANAGER,
        SYSTEM_ADMIN
    }

    static Enum userType = UserType.UNKNOWN;

    private static boolean inUse;

    private static CommandConstructor cc;

    public static void main(String[] args) {
        displayStartupMessage();
        Scanner scanner = new Scanner(System.in);


        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        switch(username.toLowerCase()){
            case "customer":
                userType = UserType.CUSTOMER;
                break;
            case "dealership_manager":
                userType = UserType.DEALERSHIP_MANAGER;
                break;
            case "system_admin":
                userType = UserType.SYSTEM_ADMIN;
                break;
            case "user":
                userType = UserType.SYSTEM_ADMIN;
                break;
            default:
                userType = UserType.UNKNOWN;
                break;
        }
        System.out.println(userType);
        cc = new CommandConstructor("./Database/AutomobileDB", username, password);

        inUse = true;        while(inUse) {
            processInput(scanner.nextLine());
        }
        System.out.println("Thank you for using the Dealership information system!");
    }

    private static void displayStartupMessage() {
        System.out.println("Welcome to the WIP Dealership information management system.");
        System.out.println("+----------------------------------------------------------+");
        System.out.println("| To get help or information type -h                       |");
        System.out.println("+----------------------------------------------------------+");
        System.out.println("Please log in with your username: ");
    }

    private static void displayHelp() {
        System.out.println("Commands");
        System.out.println("------------------------------------------------------------");
        System.out.println("-h                                #Displays the help message");
        System.out.println("-q                                #Quits the program");
        if(userType == UserType.SYSTEM_ADMIN){
            System.out.println("-c                                #Allows direct SQL Query");
        }
        System.out.println("------------------------------------------------------------");
    }

    private static void processInput(String in) {
        in = in.trim();
        String flag = in.substring(0, 2);

        switch(flag) {
            case("-h"):
                displayHelp();
                break;
            case("-q"):
                inUse = false;
                break;
            case("-c"):
                if(userType == UserType.SYSTEM_ADMIN) {
                    try {
                        displayResult(cc.getDBFullCommand(in.substring(2).trim()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            default:
                System.out.println("Unknown input, use -h for help and information.");
        }
    }

    private static void displayResult(ResultSet result) throws SQLException {

        if(result == null){
            return;
        }

        int amnt = result.getMetaData().getColumnCount();

        for (int i = 0; i < amnt; i++)
            System.out.print(result.getMetaData().getColumnName(i + 1) + " ");
        System.out.println();
        System.out.println("-----------------------------------------------------");

        while (result.next()) {
            for (int i = 0; i < amnt; i++)
                System.out.print(result.getObject(i + 1).toString() + " ");

            System.out.println();
        }


    }

}
