package Dealer_Management_Program;

import Util.PersonTable;

import java.sql.*;
import java.util.ArrayList;

/**
 * This is a sample main program. 
 * You will create something similar
 * to run your database.
 * 
 * @author scj
 *
 */
public class QueryBuilder {

	//The connection to the database
	private Connection conn;

	public QueryBuilder(String location, String user, String password) {
		createConnection(location, user, password);
	}

	public ResultSet sendQuery(SQLCommand command) {
		return sendQueryFullCommand(command.toString());
	}

	public ResultSet sendQueryFullCommand(String command) {

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			return stmt.executeQuery(command);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
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
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
