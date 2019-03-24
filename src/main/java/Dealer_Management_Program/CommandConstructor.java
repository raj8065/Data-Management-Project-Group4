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

        String commandType = SQLCommand.substring(0, SQLCommand.indexOf(' '));
        ResultSet result = null;
        switch (commandType.toLowerCase().trim()){
            case("delete"):
                break;
            case("insert"):
                if(qb.executeCommand(SQLCommand)){
                    System.out.println("Insertion successful.");
                }
                else {
                    System.out.println("Insertion failed.");
                }
                break;
            case("select"):
                result = qb.sendQueryFullCommand(SQLCommand);
                break;
        }

        return result;
    }
}
