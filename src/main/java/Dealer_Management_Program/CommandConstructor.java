package Dealer_Management_Program;

import java.sql.ResultSet;

public class CommandConstructor {

    private QueryBuilder qb;

    public CommandConstructor(String loc, String username, String password){
        qb = new QueryBuilder(loc, username, password);
    }

    public ResultSet getDB(String userInput){
        return null;
    }

    public ResultSet getDBFullCommand(String SQLCommand){
        ResultSet result = qb.sendQueryFullCommand(SQLCommand);
        if(result != null) {
            return result;
        }
        else{
            System.out.println("There was an error.");
            return null;
        }
    }
}
