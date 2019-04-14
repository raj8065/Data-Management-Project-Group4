package Dealer_Management_Program;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface {

    private static boolean inUse = false;

    private static CommandConstructor cc;

    private static String user = "";

    private static Scanner scanner;

    public static void main(String[] args) {
        displayStartupMessage();
        scanner = new Scanner(System.in);


        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        try {
            cc = new CommandConstructor("./Database/AutomobileDB", username, password);

            System.out.println("------------------------------------------------------------");
            System.out.println("Logged In");
            System.out.println("------------------------------------------------------------");

            user = getUserName(cc.useQuery("SELECT CURRENT_USER()"));

            inUse = true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("------------------------------------------------------------");
            System.out.println("Username or Password incorrect, exiting program.");
        }

        if(user.equals("CUSTOMERUSER")){
            searchVehicles();
        } else {
            while (inUse)
                processInput(scanner.nextLine());
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("Thank you for using the Dealership information system!");
    }

    private static void displayStartupMessage() {
        System.out.println("Welcome to the Dealership information management system.");
        System.out.println("+----------------------------------------------------------+");
        System.out.println("| To get help or information type -h                       |");
        System.out.println("+----------------------------------------------------------+");
        System.out.println("Please log in with your username: ");
    }

    private static void displayHelp() {
        if(user.equals("CUSTOMERUSER")){
            System.out.println("Commands");
            System.out.println("------------------------------------------------------------");
            System.out.println("-f                                #Enter Search Mode");
            System.out.println("-e                                #Exits the program");
            System.out.println("-h                                #Displays the help message");
            System.out.println("------------------------------------------------------------");

        } else {
            System.out.println("Commands");
            System.out.println("------------------------------------------------------------------");
            System.out.println("-h                                                      #Displays the help message");
            System.out.println("-e                                                      #Exits the program");
            System.out.println("-c [SQL Command]                                        #Allows direct SQL Command");
            System.out.println("-q [SQL Query]                                          #Allows direct SQL Query");
            System.out.println("-s '[VIN]' '[Dealer Name]' '[Customer Name]' '[Cost]'   #Allows the sale of vehicles");
            System.out.println("------------------------------------------------------------------");
        }
    }

    private static void processInput(String in) {
        in = in.trim();
        if(in.length() < 2) {
            System.out.println("Unknown input, use -h for help and information.");
            return;
        }
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
                String[] temp = in.split("(' '*)|('* ')|('$)");
                cc.makeSale(temp[1],temp[2].replaceAll("'","''"),temp[3],temp[4]);
                break;

            case("-f"):
                searchVehicles();
                break;

            default:
                System.out.println("Unknown input, use -h for help and information.");
        }
    }

    public static void displayResult(ResultSet result){

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

    private static void searchVehicles(){
        searchVehiclesHelp();
        boolean findMode = true;
        while(findMode){
            String in = scanner.nextLine();
            try{
                if(in.substring(0,2).equals("-a")){ // display attribute information
                    // for now just list all
                    ResultSet result = cc.useQuery("SELECT * FROM fullVehicle");
                    ResultSetMetaData meta = result.getMetaData();
                    int col = meta.getColumnCount();
                    System.out.println("-----------------------------------------");
                    System.out.println("Attribute-Name\tType");
                    for(int i = 1; i <= col; i++){
                        System.out.printf("%-15s %s\n", meta.getColumnName(i), meta.getColumnTypeName(i));
                    }
                    System.out.println("-----------------------------------------");
                } else if (in.substring(0,2).equals("-v")) {
                    displayResult(cc.useQuery("SELECT * FROM fullvehicle"));
                } else if (in.substring(0,2).equals("-h")) {
                    searchVehiclesHelp();
                } else if(in.substring(0,2).equals("-e")) {
                    findMode = false;
                } else if(in.substring(0,4).equals("less")) {
                    String[] args = in.split(" ");
                    String q = "SELECT * FROM fullvehicle WHERE upper(" + args[1] + ")<upper('" + args[2] + "')";
                    displayResult(cc.useQuery(q));
                } else if(in.substring(0,5).equals("equal")) {
                    String[] args = in.split(" ");
                    String q = "SELECT * FROM fullvehicle WHERE upper(" + args[1] + ")=upper('" + args[2] + "')";
                    displayResult(cc.useQuery(q));
                } else if(in.substring(0,7).equals("greater")) {
                    String[] args = in.split(" ");
                    String q = "SELECT * FROM fullvehicle WHERE upper(" + args[1] + ")>upper('" + args[2] + "')";
                    displayResult(cc.useQuery(q));
                } else {
                    System.out.println("Incorrect Input, Try Again. -a to list attribute info");
                }
            } catch (Exception e){
                System.out.println("Incorrect Input, Try Again. -a to list attribute info");
            }
        }
        System.out.println("Returning To Main Menu");
        //displayHelp();
    }

    public static void searchVehiclesHelp(){
        System.out.println("---------------------You are now in Vehicle Search Mode--------------------");
        System.out.println("Available Commands:");
        System.out.println("equal 'attribute' 'value'           # attribute must equal value");
        System.out.println("less 'attribute' 'value'            # attribute must be less than value");
        System.out.println("greater 'attribute' 'value'         # attribute must be greater than value");
        System.out.println("-a                                  # display attribute information");
        System.out.println("-e                                  # Exit to main menu");
        System.out.println("-v                                  # display all vehicles");
        System.out.println("-h                                  # display this help message");
        System.out.println("---------------------------------------------------------------------------");
    }



    private static String getUserName(ResultSet result) {
        if(result == null){
            return "";
        }

        int amnt = 0;
        try {
            amnt = result.getMetaData().getColumnCount();


            while (result.next()) {
                for (int i = 0; i < amnt; i++)
                    return result.getObject(i + 1).toString();

            }
        } catch (SQLException e) {
            return "";
        }
        return "";
    }

}
