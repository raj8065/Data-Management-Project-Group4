package Util;

import java.sql.*;

/* Class used to populate the database
 *
 */
public class PopulationMachine {

    private int size;

    private static String USAGE = "java Util.PopulationMachine population_size";

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
        String createCustomer =
                "create table if not exists customer(" +
                "CID numeric(5) not null," +
                "name varchar(20) not null," +
                "streetNum numeric(5)," +
                "streetName varchar(20)," +
                "city varchar(20)," +
                "state varchar(20)," +
                "zip numeric(5)," +
                "gender char(1)," +
                "annualIncome numeric(10,2)," +
                "primary key (CID)";
        // The SQL query to create the vehicle table
        String createVehicle =
                "create table if not exists vehicle(" +
                "VIN varchar(17) not null," +
                "brand varchar(20)," +
                "color varchar(20)," +
                "transmission varchar(20)," +
                "engine varchar(20)," +
                "model varchar(20)," +
                "bodyStyle varchar(20)," +
                "primary key (VIN)";
        // The SQL query to create the dealer table
        String createDealer =
                "create table if not exists dealer(" +
                "DID numeric(5) not null," +
                "name varchar(20) not null," +
                "primary key (DID)";
        // The SQL query to create the sale table
        String createSale =
                "create table if not exists sale(" +
                "VIN varchar(17) not null," +
                "DID numeric(5)," +
                "CID numeric(5)," +
                "transmission varchar(20)," +
                "engine varchar(20)," +
                "model varchar(20)," +
                "bodyStyle varchar(20)," +
                "primary key (VIN)";
        // The SQL query to create the customerPhoneNumbers table
        String createCustomerPhoneNumbers =
                "create table if not exists customerPhoneNumbers(" +
                "CID numeric(5) not null," +
                "phoneNumber numeric(10)," +
                "primary key (CID)";
        String[] createQueryList = {createCustomer, createVehicle, createDealer, createSale, createCustomerPhoneNumbers};
        sendQueries(createQueryList);
    }

    private void sendQuery(String query){
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendQueries(String[] queries){
        for (String query : queries){
            sendQuery(query);
        }
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
