# CSCC01 Assignment3 - Kijiji to GoogleMaps

This project was created to crawl Kijiji for apartments and houses and then map the listings onto Google Maps. The crawler is designed to be run from the command line and the Google Map can be viewed in the browser. The crawler takes in a 'seed' and a 'limit' which represents the Kijiji URL to start crawling and number of listings to find. 

## Usage

You will need to navigate into the assignment3 folder which also contains the pom.xml file.
After navigating into the correct folder run the folowing commands:

To compile and ensure all required dependencies are available (You will only need to run this once at the beginning):
```bash
mvn compile
```

To start up the TomCat server:
```bash
mvn tomcat7:run
```

To crawl Kijiji, open a new Terminal and run:
```bash
mvn exec:java -Dexec.mainClass=Assignment3.assignment3.Assignment3WebCrawler -Dexec.args="URL LIMIT"
```

To manually purge the database:
```bash
mvn exec:java -Dexec.mainClass=Assignment3.assignment3.Assignment3WebCrawler -Dexec.args="0 0"
```

To view the Google Map open your browser and go to: **localhost:1021**

**Note:** Every time you initiate a successful crawl the database will get purged and to see the new listings on the map you will need to refresh the page.

## Creators 

* Justin Lyn - justin.lyn@mail.utoronto.ca

* Ryan de Sao Jose - ryan.desaojose@mail.utoronto.ca