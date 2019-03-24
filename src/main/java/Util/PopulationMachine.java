package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/* Class used to populate the database
 *
 */
public class PopulationMachine {

    private int size;

    private static String USAGE = "java Util.PopulationMachine population_size";

    private static Connection conn;

    /**
     * Create a database connection with the given params
     * @param location: path of where to place the database
     * @param user: user name for the owner of the database
     * @param password: password of the database owner
     */
    public static void createConnection(String location,
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
    public static void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initialize(){
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
                "primary key (CID))";
        // The SQL query to create the vehicle table
        String createVehicle =
                "create table if not exists vehicle(" +
                "VIN varchar(17) not null," +
                "color varchar(20)," +
                "transmission varchar(20)," +
                "engine varchar(20)," +
                "primary key (VIN))";
        // The SQL query to create the dealer table
        String createDealer =
                "create table if not exists dealer(" +
                "DID numeric(5) not null," +
                "name varchar(20) not null," +
                "primary key (DID))";
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
                "primary key (VIN))";
        // The SQL query to create the customerPhoneNumbers table
        String createCustomerPhoneNumbers =
                "create table if not exists customerPhoneNumbers(" +
                "CID numeric(5) not null," +
                "phoneNumber numeric(10)," +
                "primary key (CID))";
        String[] createCommandList = {createCustomer, createVehicle, createDealer, createSale, createCustomerPhoneNumbers};
        sendCommands(createCommandList);
    }

    private static void populate(String table, String csv) {

        try {   //Get Metadata for column types
            ResultSetMetaData metaData = sendQuery("SELECT * FROM " + table + " FETCH first row only;").getMetaData();

            BufferedReader br = new BufferedReader(new FileReader( "./docs/CSV/" + csv));
            br.readLine(); // Skips column headers

            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",");
                populateLine(table, metaData, split);
            }

            br.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void populateLine(String table, ResultSetMetaData meta, String[] parts) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " + table + " VALUES (");

        //Determines Type and appends part
        for(int i  = 0; i < parts.length; i++) {
            switch (meta.getColumnType(i+1)) {
                case (Types.VARCHAR):
                case (Types.CHAR):
                    sb.append("'" + parts[i] + "'");
                    break;
                case (Types.NUMERIC):
                case (Types.OTHER):
                    sb.append(parts[i]);
            }
            if(i< parts.length-1)
                sb.append(',');
        }

        sb.append(");");
        sendCommand(sb.toString());
    }

    private static ResultSet sendQuery(String query) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void sendCommand(String command){
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void sendCommands(String[] queries){
        for (String query : queries){
            sendCommand(query);
        }
    }

    public static void main(String args[]){
        System.out.println(System.getProperty("user.dir"));
        createConnection("./Database/AutomobileDB", "user", "pass");
        initialize();
        populate("customer", "Customer.csv");
    }
}
