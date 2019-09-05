package Assignment3.assignment3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class SQLInserterTest {
	SQLInserter ins;
	SQLConnector conn;
	SQLReset resetter;
	
	@Before
	public void init() {
		conn = SQLConnector.connect();
		resetter = new SQLReset(conn);
		resetter.reset();
		ins = new SQLInserter(conn);
	}
	
	@Test
	public void testInsertAddressReturnsID() {
		int test = ins.insertAddress("5050 Test Dr", "test.ca");
		assertEquals(1, test);
		test = ins.insertAddress("5151 Test Dr", "test2.ca");
		assertEquals(2, test);
	}
	
	@Test
	public void testInsertAddressCorrectlyInserts() {
		int test = ins.insertAddress("5050 Test Dr", "test.ca");
		test = ins.insertAddress("5151 Test Dr", "test2.ca");
		try{
			Connection c = conn.getConnection();
			Statement sqlStatement = c.createStatement();
			
			String sql = "SELECT ID, Address, Link FROM Addresses ORDER BY ID DESC LIMIT 1;";
			ResultSet rs = sqlStatement.executeQuery(sql);
			
			while(rs.next()) {
				assertEquals(2, rs.getInt("ID"));
				assertEquals("5151 Test Dr", rs.getString("Address"));
				assertEquals("test2.ca", rs.getString("Link"));
			}
			
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        assertTrue(false);
	    }
	}
	
	@Test
	public void testInsertAttributeCorrectlyInserts() {
		ins.insertAttribute(1, "Bedrooms", "2");
		try{
			Connection c = conn.getConnection();
			Statement sqlStatement = c.createStatement();
			
			String sql = "SELECT AddressID, Attribute, Value FROM Attributes ORDER BY ID DESC LIMIT 1;";
			ResultSet rs = sqlStatement.executeQuery(sql);
			
			while(rs.next()) {
				assertEquals(1, rs.getInt("AddressID"));
				assertEquals("Bedrooms", rs.getString("Attribute"));
				assertEquals("2", rs.getString("Value"));
			}
			
		} catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        assertTrue(false);
	    }
	}
	
}
