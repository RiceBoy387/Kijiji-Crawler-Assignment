package Assignment3.assignment3;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLSelector {
	private SQLConnector connector;
	
	public SQLSelector(SQLConnector con) {
		connector = con;
	}
	
	public ResultSet selectAddress() {
		ResultSet rs = null;
		try{
			Connection c = connector.getConnection();
			Statement sqlStatement = c.createStatement();
			
			String sql = "SELECT ID, Address, Link FROM Addresses;";
			rs = sqlStatement.executeQuery(sql);
			
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	    }
		return rs;
	}
	
	public ResultSet selectAttribute(int addressID) {
		ResultSet rs = null;
		try{
			Connection c = connector.getConnection();
			Statement sqlStatement = c.createStatement();
			
			String sql = "SELECT ID, AddressID, Attribute, Value"
					+ " FROM Attributes WHERE AddressID=" + addressID +";";
			rs = sqlStatement.executeQuery(sql);
			
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	    }
		return rs;
	}
}
