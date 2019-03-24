package Dealer_Management_Program;

import java.util.Scanner;

public class User_Interface {

    enum UserType {
        UNKNOWN,
        CUSTOMER,
        DEALERSHIP_MANAGER,
        SYSTEM_ADMIN
    }

    static Enum userType = UserType.UNKNOWN;

    private static boolean inUse;

    public static void main(String[] args) {
        displayStartupMessage();
        Scanner scanner = new Scanner(System.in);


        String username = scanner.nextLine();

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
            default:
                userType = UserType.UNKNOWN;
                break;
        }

        inUse = true;
        while(inUse) {
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
                    System.out.println(in.substring(2).trim());
                    break;
                }
            default:
                System.out.println("Unknown input, use -h for help and information.");
        }
    }

}
