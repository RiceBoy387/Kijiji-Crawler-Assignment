package Assignment3.assignment3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnector {
	private static SQLConnector connector = null;
	private final static String url = "jdbc:sqlite:Assignment3.db";
	
	private Connection conn = null;
	
	private SQLConnector() {
		conn = SQLConnector.establishConnection();
		
	}
	
	private static Connection establishConnection() {
		Connection newConn = null;
		try {
            // create a connection to the database
            newConn = DriverManager.getConnection(url);
            
            System.out.println("Connected to SQLite3 Assignment3 db.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		return newConn;
	}
	
	public static SQLConnector connect() {
		if (connector == null) {
			connector = new SQLConnector();
			return connector;
		} else {
			return connector;
		}
	}
	
	public Connection getConnection() {
		try {
			do {
				if(conn.isClosed()) {
					conn = SQLConnector.establishConnection();
				}
			} while (conn == null);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	public void close() {
		try {	
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
