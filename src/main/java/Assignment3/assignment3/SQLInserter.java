package Assignment3.assignment3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class SQLInserter {
	private SQLConnector connector;
	
	public SQLInserter(SQLConnector con) {
		connector = con;
	}
	
	public int insertAddress(String address, String link) {
		int ret = -1;
		try{
			Connection c = connector.getConnection();
		
			Statement sqlStatement = c.createStatement();
			String sql = "INSERT INTO Addresses (Address, Link)\n" + 
					"                        VALUES (\"" + address + "\",\"" + link + "\");";
			sqlStatement.executeUpdate(sql);
			
			sql = "SELECT MAX(ID) AS ID FROM Addresses;";
			ResultSet rs = sqlStatement.executeQuery(sql);
			
			if (rs.next()) {
				ret = rs.getInt("ID");
			}
			
			sqlStatement.close();
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	    }
		return ret;
	}
	
	public void insertAttribute(int addressID, String attribute, String value) {
		try{
			Connection c = connector.getConnection();
		
			Statement sqlStatement = c.createStatement();
			String sql = "INSERT INTO Attributes (AddressID, Attribute, Value)\n" + 
					"                        VALUES (\"" + addressID + "\",\"" + attribute + "\",\"" + value + "\" );";
			sqlStatement.executeUpdate(sql);
			
			sqlStatement.close();
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	    }
	}
	
	
	
}
