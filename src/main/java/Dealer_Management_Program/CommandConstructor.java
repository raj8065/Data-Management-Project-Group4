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

    public ResultSet getDB(String userInput){
        return null;
    }

    public void useCommand(String SQLCommand){
        qb.executeCommand(SQLCommand);
    }

    public ResultSet useQuery(String SQLCommand){

        ResultSet result = null;
        result = qb.sendQueryFullCommand(SQLCommand);

        return result;
    }
}
