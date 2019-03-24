package Dealer_Management_Program;

import java.util.Scanner;

public class User_Interface {

    public static void main(String[] args) {
        displayStartupMessage();
        Scanner scanner = new Scanner(System.in);


        String username = scanner.next();

        while(true) {
            processInput(scanner.nextLine());
        }
    }

    public static void displayStartupMessage() {
        System.out.println("Welcome to the WIP Dealership information management system.");
        System.out.println("+----------------------------------------------------------+");
        System.out.println("| To get help or information type -h                       |");
        System.out.println("+----------------------------------------------------------+");
    }

    public static void displayHelp() {
        System.out.println("Commands");
        System.out.println("------------------------------------------------------------");
        System.out.println("-h                                #Displays the help message");
        System.out.println("------------------------------------------------------------");
    }

    public static void processInput(String in) {
        switch(in.trim()) {
            case("-h"):
                displayHelp();
                break;
            default:
                System.out.println("Unknown input, use -h for help and information.");
        }
    }

}
