package Dealer_Management_Program;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
            System.err.println(e);
            System.out.println("You do not have permissions to activate this command.");
        }
        return false;
    }

    public ResultSet useQuery(String SQLCommand){

        ResultSet result = null;
        try {
            result = qb.executeQuery(SQLCommand);
        } catch (SQLException e) {
            System.err.println(e);
            System.out.println("You do not have permissions to send this query");

        }

        return result;
    }

    private ArrayList<Integer> parseResultSetWithInteger(ResultSet rs, String columnName) throws SQLException {

        ArrayList<Integer> parsed = new ArrayList<>();

        if (rs != null) {

            while (rs.next())
                parsed.add(rs.getInt(columnName));
        }

        return parsed;
    }

    private ArrayList<Integer> getCustomerID(String name) {
        try {

            ResultSet customers = useQuery("SELECT CID FROM CUSTOMER WHERE name='" + name + "'");

            return parseResultSetWithInteger(customers, "CID");

        } catch (SQLException e) {
            System.err.println(e);
            System.out.println("Could not find customer(s) of name '" + name + "'.");
            return null;
        }
    }

    private ArrayList<Integer> getDealerID(String name) {
        try {

            ResultSet dealers = useQuery("SELECT DID FROM DEALER WHERE name='" + name + "'");

            return parseResultSetWithInteger(dealers, "DID");

        } catch (SQLException e) {
            System.out.println("Could not find dealer(s) of name '" + name + "'.");
            return null;
        }
    }

    public void makeSale(String VIN, String Dealer, String Customer, String Cost) {
        useCommand("PREPARE COMMIT CAR_SALE");

        ArrayList<Integer> customer = getCustomerID(Customer);
        if(customer != null && customer.size() > 0) {

            ArrayList<Integer> dealer = getDealerID(Dealer);
            if(dealer != null && dealer.size() > 0) {

                //Checks DealerOwns
                ResultSet isEmpty = useQuery("SELECT * FROM DEALEROWNS WHERE VIN=" + VIN + " AND DID=" + dealer.get(0));
                try {
                    if(!isEmpty.next()) {
                        System.out.println("Dealer " + Dealer.replaceAll("''","'") + "does not own VIN = " + VIN );
                        return;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //Takes away from dealer
                useCommand("DELETE FROM DEALEROWNS WHERE VIN=" + VIN + " AND DID=" + dealer.get(0));

                //Gives car to customer
                useCommand("INSERT INTO CUSTOMEROWNS VALUES (" + customer.get(0) + ", " + VIN + ")");

                //Creates Sale
                useCommand("INSERT INTO SALE VALUES(" + VIN + ", " + customer.get(0) + ", " + dealer.get(0)
                        + ", " + Cost + ", DAY_OF_MONTH(CURRENT_DATE), MONTH(CURRENT_DATE), YEAR(CURRENT_DATE))");
            } else {
                System.out.println("Unable to make sale, due to dealer '" + Dealer + "' being unable to be found.");

            }
        } else {
            System.out.println("Unable to make sale, due to customer '" + Customer + "' being unable to be found.");
        }

        useCommand("COMMIT");
    }
}
