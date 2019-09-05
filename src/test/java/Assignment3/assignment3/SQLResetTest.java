package Assignment3.assignment3;


import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLResetTest {

	SQLInserter ins;
	SQLConnector conn;
	SQLReset resetter;
	
	@Before
	public void init() {
		conn = SQLConnector.connect();
		resetter = new SQLReset(conn);
		resetter.reset();
	}
	
	@Test
	public void testResetClearsTables() {
		int loops =  4;
		try {
			Connection c = conn.getConnection();
			Statement sqlStatement = c.createStatement();
			String sql;
			for(int i = 0; i < loops; i++) {
				sql = "INSERT INTO Addresses (Address, Link)\n" + 
						"                        VALUES (\"5151 Test Dr\",\"test.ca\");";
				sqlStatement.executeUpdate(sql);
				sql = "INSERT INTO Attributes (AddressID, Attribute, Value)\n" + 
						"                        VALUES (\"" + Integer.toString(i+1)+ "\","
								+ "\"test\",\"" + Integer.toString(i) + "\" );";
				sqlStatement.executeUpdate(sql);
			}
			
			resetter.reset();
			
			sql = "SELECT * FROM Addresses;";
			ResultSet rs = sqlStatement.executeQuery(sql);
			assertFalse(rs.next());
			
			sql = "SELECT * FROM Attributes";
			rs = sqlStatement.executeQuery(sql);
			assertFalse(rs.next());
			
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        assertTrue(false);
	    }
	}
	
}
