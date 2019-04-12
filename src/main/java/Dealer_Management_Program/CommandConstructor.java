package Dealer_Management_Program;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void makeSale(String VIN, String Dealer, String Customer) {
        useCommand("PREPARE COMMIT CAR_SALE");


        try {
        ResultSet customer = useQuery("SELECT CID FROM CUSTOMERS WHERE CustomerName='" + Customer + "'");
        String CID = "";
        CID = customer.getString("CID");
        useCommand("INSERT INTO CUSTOMEROWNS VALES (" + CID + ", " + VIN + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        useCommand("DELETE FROM DEALEROWNS WHERE VIN=" + VIN);

        useCommand("COMMIT");
    }
}
