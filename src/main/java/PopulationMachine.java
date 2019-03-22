import java.sql.*;
import java.util.ArrayList;

/* Class used to populate the database
 *
 */
public class PopulationMachine {

    private int size;

    private static String USAGE = "java PopulationMachine population_size";

    private Connection conn;

    /**
     * Create a database connection with the given params
     * @param location: path of where to place the database
     * @param user: user name for the owner of the database
     * @param password: password of the database owner
     */
    public void createConnection(String location,
                                 String user,
                                 String password){
        try {

            //This needs to be on the front of your location
            String url = "jdbc:h2:" + location;

            //This tells it to use the h2 driver
            Class.forName("org.h2.Driver");

            //creates the connection
            conn = DriverManager.getConnection(url,
                    user,
                    password);
        } catch (SQLException | ClassNotFoundException e) {
            //You should handle this better
            e.printStackTrace();
        }
    }

    /**
     * When your database program exits
     * you should close the connection
     */
    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PopulationMachine(int populationSize){
        size = populationSize;
    }

    private void populateCustomers(){

    }

    private void populate(){

    }

    private void initialize(){
        // The SQL query to create the customer table
        String createCustomer = "create table student(\n" +
                "CID numeric(5) not null,\n" +
                "name varchar(20) not null,\n" +
                "streetNum numeric(5),\n" +
                "streetName varchar(20),\n" +
                "city varchar(20),\n" +
                "state varchar(20),\n" +
                "zip numeric(5),\n" +
                "gender char(1),\n" +
                "annualIncome numeric(10,2),\n" +
                "primary key (ID),\n";
        // The SQL query to create the vehicle table
        String createVehicle = "create table student(\n" +
                "CID numeric(5) not null,\n" +
                "name varchar(20) not null,\n" +
                "streetNum numeric(5),\n" +
                "streetName varchar(20),\n" +
                "city varchar(20),\n" +
                "state varchar(20),\n" +
                "zip numeric(5),\n" +
                "gender char(1),\n" +
                "annualIncome numeric(10,2),\n" +
                "primary key (ID),\n";
    }

    private void sendQuery(){

    }

    public static void main(String args[]){
        PopulationMachine pm = new PopulationMachine(0);
        String size = args[0];
        if (size.matches("\\d+")) {
            int num = Integer.parseInt(size);
            if (num < 0)
                num *= -1;
            pm = new PopulationMachine(num);
        }
        else {
            System.err.println(USAGE);
            System.err.println("population_size must be an integer");
            System.exit(1);
        }
        pm.initialize();
        pm.populate();
    }
}
