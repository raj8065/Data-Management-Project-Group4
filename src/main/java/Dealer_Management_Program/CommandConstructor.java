package Dealer_Management_Program;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CommandConstructor {

    private QueryBuilder qb;

    public CommandConstructor(String loc, String username, String password) throws SQLException {
        qb = new QueryBuilder(loc, username, password);
    }

    public void closeConnection() {
        qb.closeConnection();
    }

    public boolean useCommand(String SQLCommand){
        try {
            return qb.executeCommand(SQLCommand);
        } catch (SQLException e) {
            System.out.println("You do not have permissions to activate this command.");
            System.err.println(e);
        }
        return false;
    }

    public ResultSet useQuery(String SQLCommand){

        ResultSet result = null;
        try {
            result = qb.executeQuery(SQLCommand);
        } catch (SQLException e) {
            System.out.println("You do not have permissions to send this query");

        }

        return result;
    }

    private ArrayList<String> parseResultSetWithString(ResultSet rs, String columnName) throws SQLException {

        ArrayList<String> parsed = new ArrayList<>();

        if (rs != null) {
            parsed.add(rs.getString(columnName));

            while (rs.next())
                parsed.add(rs.getString(columnName));
        }

        return parsed;
    }

    private ArrayList<String> getCustomerID(String name) {
        try {

            ResultSet customers = useQuery("SELECT CID FROM CUSTOMER WHERE name='" + name + "'");

            return parseResultSetWithString(customers, "CID");

        } catch (SQLException e) {
            System.out.println("Could not find customer(s) of name '" + name + "'.");
            return null;
        }
    }

    private ArrayList<String> getDealerID(String name) {
        try {

            ResultSet dealers = useQuery("SELECT DID FROM DEALER WHERE name='" + name + "'");

            return parseResultSetWithString(dealers, "DID");

        } catch (SQLException e) {
            System.out.println("Could not find dealer(s) of name '" + name + "'.");
            return null;
        }
    }

    public void makeSale(String VIN, String Dealer, String Customer) {
        useCommand("PREPARE COMMIT CAR_SALE");

        ArrayList<String> customer = getCustomerID(Customer);
        if(customer != null) {

            ArrayList<String> dealer = getCustomerID(Dealer);
            if(dealer != null) {
                //Gives car to customer
                useCommand("INSERT INTO CUSTOMEROWNS VALES (" + customer.get(0) + ", " + VIN + ")");


                //Takes away from dealer
                useCommand("DELETE FROM DEALEROWNS WHERE VIN=" + VIN + " AND DID=" + dealer.get(0));
            } else {
                System.out.println("Unable to make sale, due to dealer '" + Dealer + "' being unable to be found.");

            }
        } else {
            System.out.println("Unable to make sale, due to customer '" + Customer + "' being unable to be found.");
        }

        useCommand("COMMIT");
    }
}
