package Assignment3.assignment3;

import java.sql.Connection;
import java.sql.Statement;

public class SQLReset {
	private SQLConnector connector;
	
	public SQLReset(SQLConnector con) {
		connector = con;
	}
	
	public void reset() {
		try{
			Connection c = connector.getConnection();
		
			Statement sqlStatement = c.createStatement();
			String sql = "DELETE FROM Addresses;";
			sqlStatement.executeUpdate(sql);
			
			sql = "DELETE FROM sqlite_sequence WHERE name='Addresses';";
			sqlStatement.executeUpdate(sql);
			
			sql = "DELETE FROM Attributes;";
			sqlStatement.executeUpdate(sql);
			
			sql = "DELETE FROM sqlite_sequence WHERE name='Attributes';";
			sqlStatement.executeUpdate(sql);
			
		
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	    }
	}
	
}
