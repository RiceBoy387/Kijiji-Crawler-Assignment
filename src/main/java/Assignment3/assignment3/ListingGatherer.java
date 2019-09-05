package Assignment3.assignment3;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getListings")
public class ListingGatherer extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
	         throws ServletException, IOException {
		String jsonRet = "";
		try {
			resp.setContentType("application/json");
			
			SQLConnector connector = SQLConnector.connect();
			
			SQLSelector s = new SQLSelector(connector);
	        ResultSet addresses = s.selectAddress();
	        ResultSet attributes = null;
	        
	        jsonRet = "{\n";
	        while(addresses.next()) {
	        	jsonRet = jsonRet +"\"" + addresses.getString("Address") +"\": {";
	        	int id = addresses.getInt("ID");
	        	attributes = s.selectAttribute(id);
	        	
	        	boolean firstAtt = true;
	        	while(attributes.next()) {
	        		if(!firstAtt) {
	        			jsonRet = jsonRet + ",";
	        		}
	        		firstAtt = false;
	        		jsonRet = jsonRet + "\n\t\"" + attributes.getString("Attribute") + "\": \""
	        				+ attributes.getString("Value") + "\"";
	        	}
	        	jsonRet = jsonRet + ",\n\t\"Link\": \"" + addresses.getString("Link") + "\"\n\t},\n";
	        	
	        	
	        	attributes.close();
	        }
	        
	        jsonRet = jsonRet.substring(0, jsonRet.length() - 2) + "\n}";
	        System.out.println(jsonRet);
	        
        } catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	    }
		resp.getWriter().write(jsonRet);
	}

}
