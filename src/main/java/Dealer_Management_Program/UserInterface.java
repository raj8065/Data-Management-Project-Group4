package Dealer_Management_Program;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface {

    private static boolean inUse = false;

    private static CommandConstructor cc;

    public static void main(String[] args) {
        displayStartupMessage();
        Scanner scanner = new Scanner(System.in);


        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        try {
            cc = new CommandConstructor("./Database/AutomobileDB", username, password);

            System.out.println("------------------------------------------------------------");
            System.out.println("Logged In");
            System.out.println("------------------------------------------------------------");

            inUse = true;
        } catch (SQLException e) {
            System.out.println("------------------------------------------------------------");
            System.out.println("Username or Password incorrect, exiting program.");
        }



        while(inUse)
            processInput(scanner.nextLine());

        System.out.println("------------------------------------------------------------");
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
        System.out.println("-e                                #Exits the program");
        System.out.println("-c                                #Allows direct SQL Command");
        System.out.println("-q                                #Allows direct SQL Query");
        System.out.println("------------------------------------------------------------");
    }

    private static void processInput(String in) {
        in = in.trim();
        String flag = in.substring(0, 2);

        switch(flag) {
            case("-h"):
                displayHelp();
                break;

            case("-e"):
                inUse = false;
                cc.closeConnection();
                break;

            case("-c"):
                cc.useCommand(in.substring(2).trim());
                break;

            case("-q"):
                displayResult(cc.useQuery(in.substring(2).trim()));
                break;

            case("-s"):
                cc.makeSale("","","");
                break;

            default:
                System.out.println("Unknown input, use -h for help and information.");
        }
    }

    private static void displayResult(ResultSet result){

        if(result == null){
            return;
        }

        int amnt = 0;
        try {
            amnt = result.getMetaData().getColumnCount();


            for (int i = 0; i < amnt; i++)
                System.out.print(result.getMetaData().getColumnName(i + 1) + " ");
            System.out.println();
            System.out.println("-----------------------------------------------------");

            while (result.next()) {
                for (int i = 0; i < amnt; i++)
                    System.out.print(result.getObject(i + 1).toString() + " ");

                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println();
            System.out.println("-- ERROR METADATA NOT FOUND --");
        }

    }

}
