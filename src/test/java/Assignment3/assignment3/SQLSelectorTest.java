package Assignment3.assignment3;


import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLSelectorTest {
	SQLSelector sel;
	SQLConnector conn;
	SQLReset resetter;
	
	@Before
	public void init() {
		conn = SQLConnector.connect();
		resetter = new SQLReset(conn);
		resetter.reset();
		sel = new SQLSelector(conn);
	}
	
	@Test
	public void testSelectAddressesSelectsAllEntries() {
		int loops =  4;
		try {
			Connection c = conn.getConnection();
			Statement sqlStatement = c.createStatement();
			for(int i = 0; i < loops; i++) {
				String sql = "INSERT INTO Addresses (Address, Link)\n" + 
						"                        VALUES (\"5151 Test Dr\",\"test.ca\");";
				sqlStatement.executeUpdate(sql);
			}
			
			ResultSet rs = sel.selectAddress();
			int count = 0;
			while (rs.next()) {
				count ++;
			}
			
			assertEquals(loops, count);
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        assertTrue(false);
	    }
	}
	
	@Test
	public void testSelectAttributeOnlySelectsRequestedAddressID() {
		int loops =  4;
		try {
			Connection c = conn.getConnection();
			Statement sqlStatement = c.createStatement();
			String sql;
			for(int i = 0; i < loops; i++) {
				sql = "INSERT INTO Addresses (Address, Link)\n" + 
						"                        VALUES (\"5151 Test Dr\",\"test.ca\");";
				sqlStatement.executeUpdate(sql);
				for (int j = 0; j < loops; j++) {
					sql = "INSERT INTO Attributes (AddressID, Attribute, Value)\n" + 
							"                        VALUES (\"" + Integer.toString(i+1)+ "\","
									+ "\"test\",\"" + Integer.toString(i) + "\" );";
					sqlStatement.executeUpdate(sql);
				}
			}
			
			ResultSet rs = sel.selectAttribute(3);
			 
			int count = 0;
			while (rs.next()) {
				assertEquals(3, rs.getInt("AddressID"));
				assertEquals("2", rs.getString("Value"));
				count ++;
			}
			assertEquals(loops, count);
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        assertTrue(false);
	    }
	}
	
	
	
}
