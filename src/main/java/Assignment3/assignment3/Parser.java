package Assignment3.assignment3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
	
		// Variables to use
		private ArrayList <String> temp = new ArrayList<String>();
		private int counter;
		
		// Method for getting list of places
		public ArrayList<String> getLinks(Document htmlPage) {
			// Clear the temp arrayList and reset counter 
			temp.clear();
			counter =0;
			// Get all the listings section of the page
			Elements links = htmlPage.select("div.title");
			// Run a loop to get all the links on the page
			for (Element place : links) {
				if (counter < 24) {
					String address = (place.select("a[href]")).toString();
		        	int end = address.indexOf("class");
		        	String link = "https://www.kijiji.ca" + address.substring(9, end-2);
		        	// Add the URL to the list
		        	temp.add(link);
		        	counter ++;
				}
			}
			
			return temp;
		}
		
		// Method for returning the next page
		public String getMoreLinks(Document htmlPage) {

			// move to the next page
	        Elements pages = htmlPage.select("div.pagination");
	        String nextPage = (pages.select("a:contains(Next)")).toString();
	        String next = "https://www.kijiji.ca" + nextPage.substring(22, nextPage.indexOf(">")-1);
	        System.out.println("\nNext Page is: " + next + "\n");
			return next;
			
		}
		
		// Parse the given page for the content we need 
		public void parseLink(String link, String htmlPage) {
			
			SQLConnector connector = SQLConnector.connect();
			SQLInserter sql = new SQLInserter(connector);
			
			String address;
			
			
			// Hash map to hold all the values 
			Map<String, String> attributes = new HashMap<String, String>();
			
			System.out.println(link + "\n");
			
	    	// House or Appartment
	    	if (htmlPage.indexOf("House") != -1) {
	    		attributes.put("Type","House");
	    	}
	    	else {
	    		attributes.put("Type","Apartment/Condo");
	    	}
	    	
	    	// Bedroom and Bathroom
	    	int bed = htmlPage.indexOf("numberbedrooms_s");
	    	int bath = htmlPage.indexOf("numberbathrooms_s");
	    	if (htmlPage.substring(bed+19, bed+20).equals("") || htmlPage.substring(bed+19, bed+20).equals("0") || htmlPage.substring(bed+19, bed+20).equals("-")) {
	    		attributes.put("Bedrooms", "1");
	    		attributes.put("Bathrooms", htmlPage.substring(bath+20, bath+21));
	    	}
	    	else {
	    		attributes.put("Bedrooms", htmlPage.substring(bed+19, bed+20));
	    		attributes.put("Bathrooms", htmlPage.substring(bath+20, bath+21));
	    	}
	    	
	    	// Pets Allowed
	    	int pet = htmlPage.indexOf("petsallowed_s");
	    	String petVal = htmlPage.substring(pet+16, pet+17);
	    	if (petVal.contentEquals("0")) {
	    		attributes.put("Pets Allowed","NO!");
	    	}
	    	else {
	    		attributes.put("Pets Allowed","Yes!");
	    	}
	    	
	    	// Furnished 
	    	int furnish = htmlPage.indexOf("furnished_s");
	    	String furnVal = htmlPage.substring(furnish+14, furnish+15);
	    	if (furnVal.contentEquals("0")) {
	    		attributes.put("Furnished","No");
	    	} else {
	    		attributes.put("Furnished","Yes!");
	    	}
	    	
	    	// Parking Availability
	    	int park = htmlPage.indexOf("numberparkingspots_s");

	    	if (park != -1) {
	    		attributes.put("Parking Spots Available", htmlPage.substring(park+23, park+24));
	    	}
	    	else {
	    		attributes.put("Parking Spots Available", "N/A");
	    	}

	    	
	    	// Square Footage
	    	try {
	    		htmlPage = htmlPage.substring(htmlPage.indexOf("areainfeet_i")+15);
	    		String sizeVal = htmlPage.substring(0, htmlPage.indexOf(",")-1).replace("\"", "");
	    		if (sizeVal.length() < 50) {
	    			attributes.put("Size (Sqft)", sizeVal);
	    		}
	    		else {
	    			attributes.put("Size (Sqft)",  "N/A");
	    		}
	    	}catch(Exception e) {
	    		attributes.put("Size (Sqft)",  "N/A");
	    	}
	    	
	    	// Price
	    	try {
	    		htmlPage = htmlPage.substring(htmlPage.indexOf("<span content=")+1); 	
	        	String priceVal =(htmlPage.substring(htmlPage.indexOf("=")+2, htmlPage.indexOf(">")-1));
	        	if (priceVal.length() < 20) {
	        		attributes.put("Price","$"+priceVal);
	        	}
	        	else {
	        		attributes.put("Price", "Contact Seller");
	        	}
	    	}catch (Exception e) {
	    		attributes.put("Price", "Contact Seller");
	    	}
	    	// Address
	    	try {
	    		htmlPage = htmlPage.substring(htmlPage.indexOf("PostalAddress"));
	    		address = htmlPage.substring(htmlPage.indexOf(">")+1, htmlPage.indexOf("<")).replaceFirst(",","");
	        	
	    	}catch(Exception e) {
	    		address = "N/A";
	    	}
	    	
	    	// Insert the information into the databse 
	    	int id = sql.insertAddress(address, link);
	    	
	    	// Loop through the hash map and store the values in the database 
	    	for (Map.Entry<String, String> entry : attributes.entrySet()) {
			    sql.insertAttribute(id, entry.getKey(),entry.getValue());
			}
	    	
			
		}

}
