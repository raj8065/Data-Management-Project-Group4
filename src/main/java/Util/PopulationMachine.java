package Util;

import org.h2.command.ddl.CreateRole;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/*
 * Class used to populate the database
 */
public class PopulationMachine {

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
     * Closes the connection to the database
     */
    public static void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the database tables for the Car Dealership application using SQL commands
     */
    private static void initialize(){
        // The SQL query to create the customer table
        String createCustomer =
            "create table if not exists customer(" +
            "CID numeric(5) not null," +
            "name varchar(20) not null," +
            "streetNum varchar(5)," +
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
            "year varchar(4)," +
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
            "CID numeric(5) not null," +
            "DID numeric(5) not null," +
            "cost numeric(8)," +
            "Day numeric(2) not null," +
            "Month numeric(2) not null," +
            "Year numeric(4) not null," +
            "primary key(VIN)," +
            "foreign key (VIN) references vehicle(VIN)," +
            "foreign key (CID) references customer(CID)," +
            "foreign key (DID) references dealer(DID))";
        // The SQL query to create the customerPhoneNumbers table
        String createCustomerPhoneNumbers =
            "create table if not exists customerPhoneNumbers(" +
            "CID numeric(5) not null," +
            "phoneNumber numeric(10)," +
            "primary key (CID,phoneNumber)," +
            "foreign key (CID) references Customer(CID))";
        // The SQL query to create the brandModels table
        String createBrandModels =
            "create table if not exists brandModels(" +
            "BrandName varchar(20) not null," +
            "ModelName varchar(20) not null," +
            "primary key (BrandName, ModelName))";
        // The SQL query to create the customerOwns table
        String createCustomerOwns =
            "create table if not exists customerOwns(" +
            "CID numeric(5) not null," +
            "VIN varchar(17) not null," +
            "primary key (CID,VIN)," +
            "foreign key (CID) references customer(CID)," +
            "foreign key (VIN) references vehicle(VIN))";
        // The SQL query to create the dealerCanSell table
        String createDealerCanSell =
            "create table if not exists dealerCanSell(" +
            "DID numeric(5) not null," +
            "BrandName varchar(20) not null," +
            "primary key (DID,BrandName)," +
            "foreign key (DID) references dealer(DID)," +
            "foreign key (BrandName) references brandModels(BrandName))";
        // The SQL query to create the dealerOwns table
        String createDealerOwns =
            "create table if not exists dealerOwns(" +
            "DID numeric(5) not null," +
            "VIN varchar(17) not null," +
            "primary key (DID,VIN)," +
            "foreign key (DID) references dealer(DID)," +
            "foreign key (VIN) references vehicle(VIN))";
        // The SQL query to create the modelBodyStyle table
        String createModelBodyStyle =
            "create table if not exists modelBodyStyle(" +
            "ModelName varchar(20) not null," +
            "BodyStyle varchar(20) not null," +
            "primary key (ModelName, BodyStyle)," +
            "foreign key (ModelName) references brandModels(ModelName))";
        // The SQL query to create the vehicleBodyStyle table
        String createVehicleBodyStyle =
            "create table if not exists vehicleBodyStyle(" +
            "VIN varchar(17) not null," +
            "BodyStyle varchar(20) not null," +
            "primary key (VIN)," +
            "foreign key (VIN) references vehicle(VIN)," +
            "foreign key (BodyStyle) references modelBodyStyle(BodyStyle))";
        // The SQL query to create the vehicleModel table
        String createVehicleModel =
            "create table if not exists vehicleModel(" +
            "VIN varchar(17) not null," +
            "ModelName varchar(20) not null," +
            "primary key (VIN)," +
            "foreign key (ModelName) references brandModels(ModelName))";

        // The SQL query to create the Vehicle View
        String createFullVehicle =
                "create or replace view fullVehicle as " +
                "SELECT a.VIN, c.ModelName, b.BodyStyle, a.color, a.transmission, a.engine " +
                "FROM vehicle a INNER JOIN vehicleBodyStyle b on a.VIN=b.VIN " +
                "INNER JOIN vehicleModel c on a.VIN=c.VIN";

        String[] createCommandList = {createCustomer, createVehicle, createDealer, createSale, createCustomerPhoneNumbers,
            createBrandModels, createCustomerOwns, createDealerCanSell, createDealerOwns, createModelBodyStyle,
            createVehicleBodyStyle, createVehicleModel, createFullVehicle};
        sendCommands(createCommandList);
    }

    /**
     * Takes in the name of a table within the database and the name of a csv file and populates the table with the
     * information from the csv
     * @param table The name of the table to populate
     * @param csv   The name of the csv file to get the data from
     */
    private static void populate(String table, String csv) {

        try {
            // Get Metadata for column types
            ResultSetMetaData metaData = sendQuery("SELECT * FROM " + table + " FETCH first row only;").getMetaData();

            // Set up the reader
            BufferedReader br = new BufferedReader(new FileReader( "./docs/CSV/" + csv));
            br.readLine(); // Skips column headers

            // Read CSV line by line and add it to the table
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

    /**
     * Processes a line from a CSV file and puts it into the indicated table
     * @param table The name of the table to insert the information into
     * @param meta  Metadata of the table, used for the column types
     * @param parts The CSV line split at the commas
     * @throws SQLException
     */
    private static void populateLine(String table, ResultSetMetaData meta, String[] parts) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " + table + " VALUES (");

        //Determines type of the column and formats the CSV part the appends it to the SQL string
        for(int i  = 0; i < parts.length; i++) {
            switch (meta.getColumnType(i+1)) {
                case (Types.VARCHAR):
                case (Types.CHAR):
                    sb.append("'");
                    sb.append(parts[i].replaceAll("'","''")); // Casting in case there is an apostrophe
                    sb.append("'");
                    break;
                default:
                    sb.append(parts[i]);
            }

            // splits the value arguments
            if(i< parts.length-1)
                sb.append(',');
        }

        sb.append(");");
        sendCommand(sb.toString());
    }

    private static void createRole(String roleName) {
        sendCommand("CREATE ROLE IF NOT EXISTS " + roleName);
    }

    private static void grantPermissions(String name, String table, String permission) {
        if(permission.toUpperCase().equals("ALL") && table == null)
            sendCommand("GRANT " + permission + " TO " + name);
        else
            sendCommand("GRANT " + permission + " ON " + table + " TO " + name);
    }

    private static void grantRole(String user, String role) {
        sendCommand("Grant " + role + " TO " + user);
    }

    private static void createUser(String username, String password) {
        sendCommand("CREATE USER IF NOT EXISTS " + username + " PASSWORD '" + password + "'");
    }

    /**
     * Sends an SQL query to the database
     * @param query The SQL code to send to the database
     * @return      The output of the query, may be null
     */
    private static ResultSet sendQuery(String query) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends an SQL command to the database
     * @param command   The SQL command to send
     */
    private static void sendCommand(String command){
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(command);
        } catch (JdbcSQLIntegrityConstraintViolationException e2) {

        } catch (SQLException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Sends multiple SQL commands to the database
     * @param queries   An array of SQL commands to send
     */
    private static void sendCommands(String[] queries){
        for (String query : queries){
            sendCommand(query);
        }
    }

    public static void main(String args[]) {
        createConnection("./Database/AutomobileDB", "user", "pass");

        initialize();

        populate("brandModels", "BrandModels.csv");
        populate("customer", "Customer.csv");
        populate("customerOwns", "CustomerOwns.csv");
        populate("customerPhoneNumbers", "CustomerPhoneNumbers.csv");
        populate("dealer", "Dealer.csv");
        populate("dealerCanSell", "DealerCanSell.csv");
        populate("dealerOwns", "DealerOwns.csv");
        populate("modelBodyStyle", "ModelBodyStyles.csv");
        populate("sale", "Sale.csv");
        populate("vehicle", "Vehicle.csv");
        populate("vehicleBodyStyle", "VehicleBodyStyle.csv");
        populate("vehicleModel", "VehicleModel.csv");

        createRole("Customer");
        grantPermissions("Customer","Customer", "SELECT");
        grantPermissions("Customer","CustomerPhoneNumbers", "SELECT, UPDATE");
        grantPermissions("Customer","Sale", "SELECT");
        grantPermissions("Customer","Vehicle", "SELECT");
        grantPermissions("Customer","VehicleBodyStyle", "SELECT");
        grantPermissions("Customer","VehicleModel", "SELECT");
        grantPermissions("Customer","customerOwns", "SELECT");
        grantPermissions("Customer","brandModels", "SELECT");
        grantPermissions("Customer", "fullVehicle", "SELECT");

        createRole("Dealer");
        grantPermissions("Dealer","Customer", "ALL");
        grantPermissions("Dealer","CustomerOwns", "ALL");
        grantPermissions("Dealer","CustomerPhoneNumbers", "ALL");
        grantPermissions("Dealer","Sale", "ALL");
        grantPermissions("Dealer","Vehicle", "ALL");
        grantPermissions("Dealer","VehicleBodyStyle", "ALL");
        grantPermissions("Dealer","VehicleModel", "ALL");
        grantPermissions("Dealer","brandModels", "ALL");
        grantPermissions("Dealer", "Dealer", "SELECT");
        grantPermissions("Dealer", "DealerCanSell", "SELECT");
        grantPermissions("Dealer", "DealerOwns", "ALL");


        createRole("Salesman");
        grantPermissions("Salesman","Customer", "SELECT");
        grantPermissions("Salesman","CustomerPhoneNumbers", "SELECT");
        grantPermissions("Salesman","Sale", "SELECT, INSERT");
        grantPermissions("Salesman","Vehicle", "SELECT");
        grantPermissions("Salesman","VehicleBodyStyle", "SELECT");
        grantPermissions("Salesman","VehicleModel", "SELECT");
        grantPermissions("Salesman","brandModels", "SELECT");
        grantPermissions("Salesman", "Dealer", "SELECT");
        grantPermissions("Salesman", "DealerCanSell", "SELECT");
        grantPermissions("Salesman", "DealerOwns", "SELECT");

        createRole("SystemAdmin");
        grantPermissions("SystemAdmin", null, "ALL");

        createUser("SalesUser", "SalesPass");
        createUser("SysAdUser", "SysAdPass");
        createUser("CustomerUser", "CustomerPass");
        createUser("DlrUser", "DlrPass");

        grantRole("SalesUser", "Salesman");
        grantRole("SysAdUser", "SystemAdmin");
        grantRole("CustomerUser", "Customer");
        grantRole("DlrUser", "Dealer");

        closeConnection();
    }
}
