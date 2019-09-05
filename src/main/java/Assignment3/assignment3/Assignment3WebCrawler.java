package Assignment3.assignment3;

import java.util.ArrayList;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Assignment3.assignment3.Parser;

/**
 * Hello world!
 *
 */
public class Assignment3WebCrawler 
{
    public static void main( String[] args ) throws IOException
    {
    	
		// Variables to use
		String html = args[0];
		int listings = Integer.valueOf(args[1]);
		boolean proceed = false, notPurged = true;
		
		// Option for purging since doesn't purge on invalid URL
		if (html.equals("0") && listings == 0) {
			// Purge the database
			SQLConnector connector = SQLConnector.connect();
	    	SQLReset db = new SQLReset(connector);
	    	db.reset();
	    	notPurged = false;
	    	System.out.println("### Database has been purged ###\n");
		}else {
			// Make sure the URL is valid 
			try {
				Document doc = Jsoup.connect(html).get();
			}catch(Exception e) {
				System.out.println("ERROR: INVALID URL !!\n### Database has NOT been purged ###");
			}
			if (html.length() > 42 && html.indexOf("https://www.kijiji.ca/b-apartments-condos/")!=-1) {
				proceed = true;
			}
		}

		// Only start if the URL is valid
		if (proceed) {
			// Get the SQL Connection
			SQLConnector connector = SQLConnector.connect();
	    	SQLReset db = new SQLReset(connector);
	    	db.reset();
	    	System.out.println("### Database has been purged ###\n");

			// Create the parser object 
			Parser parse = new Parser();
			// Array list to store the listings 
			ArrayList <String> places = new ArrayList<String>();
			
			// Loop until we have finished parsing the req listings 
			while (listings > 0) {
				
				places.clear();
				// Get the document 
				Document doc = Jsoup.connect(html).get();
			
				// Get the links for the places on the page 
				places = parse.getLinks(doc);
				
				// Loop to run through all the links/places 
				int counter=23;
				while (listings > 0 && counter >= 0) {
					
					try {
						String listingLink = places.get(counter);
						// Connect to the page and get the HTML
		            	Document listing = Jsoup.connect(listingLink).get();
		            	// Get the string of the HTML page
		            	String listingString = listing.toString();
		            	System.out.println("REMAINING PAGES: " + listings);
						// Call parse Links to get the information for the listing
						parse.parseLink(listingLink, listingString);
					} catch (Exception e) {
						System.out.println("ERROR: Invalid Page!");
						counter = -1;
						listings = -1;
						break;
					}
					// Modiffy counters 
					counter --;
					listings --;
				}
				
				// Check if we finished all the listings 
				if (listings > 0) {
					// Call to get the next page 
					html = parse.getMoreLinks(doc);
				}
				
			}
		}else if (notPurged){
			System.out.println("ERROR: INVALID URL !!\n### Database has NOT been purged ###");
		}
		
    }
}
