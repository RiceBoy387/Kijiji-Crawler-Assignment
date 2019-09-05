package Assignment3.assignment3;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLConnectorTest {
	
	SQLConnector expected;
	SQLConnector testing;
	
	@Before
	public void initialize() {
		expected = SQLConnector.connect();
		testing = SQLConnector.connect();
	}
	
	@Test
	public void testSingletonOfSQLConnector() {
		assertEquals(expected, testing);
	}
	
	@Test
	public void testConnectionRemainsTheSame() {
		Connection expect = expected.getConnection();
		Connection test = testing.getConnection();
		assertEquals(expect, test);
	}
	
	@Test
	public void testCloseConnectorClosestheConnection() {
		Connection test = expected.getConnection();
		expected.close();
		try {
			assertTrue(test.isClosed());
		} catch (SQLException e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testGetConnectionAfterClosingReturnsNewConn() {
		Connection test = expected.getConnection();
		expected.close();
		Connection test2 = expected.getConnection();
		assertNotSame(test, test2);
	}
	
	@Test
	public void testGetConnectionAfterClosingMaintainsSingleton() {
		Connection firstConn = expected.getConnection();
		expected.close();
		Connection expect = expected.getConnection();
		Connection test = testing.getConnection();
		assertEquals(expect, test);
	}
	
}
