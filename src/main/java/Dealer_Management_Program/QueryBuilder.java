package Dealer_Management_Program;

import java.sql.*;

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

	public QueryBuilder(String location, String user, String password) throws SQLException {
		createConnection(location, user, password);
	}

	public boolean executeCommand(String command) throws SQLException {
        Statement stmt = null;
		stmt = conn.createStatement();
		return stmt.execute(command);
    }

    public boolean executeCommand(SQLCommand command) throws SQLException {
        return executeCommand(command.toString());
    }

	public ResultSet executeQuery(SQLCommand command) throws SQLException {
		return executeQuery(command.toString());
	}

	public ResultSet executeQuery(String command) throws SQLException {

		Statement stmt = null;

		stmt = conn.createStatement();
		return stmt.executeQuery(command);
	}
	/**
	 * Create a database connection with the given params
	 * @param location: path of where to place the database
	 * @param user: user name for the owner of the database
	 * @param password: password of the database owner
	 */
	public boolean createConnection(String location,
			                     String user,
			                     String password) throws SQLException {
		try {

			//This needs to be on the front of your location
			String url = "jdbc:h2:" + location;

			//This tells it to use the h2 driver
			Class.forName("org.h2.Driver");

			//creates the connection
			conn = DriverManager.getConnection(url,
					user,
					password);
			return true;
		} catch (ClassNotFoundException e) {
			//You should handle this better
			e.printStackTrace();
			return false;
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
