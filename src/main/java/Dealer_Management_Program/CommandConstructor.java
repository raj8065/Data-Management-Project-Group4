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
        switch (commandType.toLowerCase()){
            case("delete"):
                break;
            case("insert"):
                break;
            case("select"):
                result = qb.sendQueryFullCommand(SQLCommand);
                break;
        }

        if(result != null) {
            return result;
        }
        else{
            System.out.println("There was an error.");
            return null;
        }
    }
}
