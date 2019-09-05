// Load up the API document
document.addEventListener('DOMContentLoaded', function () {
    if (document.querySelectorAll('#map').length > 0)
    {
        var js_file = document.createElement('script');
        js_file.type = 'text/javascript';
        js_file.src = 'https://maps.googleapis.com/maps/api/js?v=3.37&key=#######################&callback=initMap';
        document.getElementsByTagName('head')[0].appendChild(js_file);
    }
});


var lastWindow=null;

function initMap() {

    // Create the map and center it on Toronto
    var options = {zoom: 10, center:{lat: 43.6532, lng: -79.3832}};
    var map = new google.maps.Map(document.getElementById('map'),options);
    // Create Geocoder
    var geocoder = new google.maps.Geocoder();

    // Fetch the data from the database
    var xmlhttp = new XMLHttpRequest();
    var url = "http://localhost:1021/getListings";
    console.log("got here");
    xmlhttp.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            var myArr = JSON.parse(this.responseText);
            console.log(myArr);
            attrToMarker(myArr);
        }
    };
    xmlhttp.open("GET", url, false);
    xmlhttp.send();

    function attrToMarker(myArr) {
        var coordinate = {};
        var i;
        var infoStr;
        // Loop through the array can call marker
        for(var key in myArr) {
        	infoStr = '';
        	if(myArr.hasOwnProperty(key)){
        		infoStr = '<h><b> Address: '+ key +'</b></h><ul>';
	        	for (var key2 in myArr[key]){
	        		if (key2 == "Link") {
	        			
	        			infoStr += '</u><b><a href="' + myArr[key][key2] +'">Check out the posting!</a></b>'
	        		} else {
	        			infoStr += '<li>' + key2 + ': ' + myArr[key][key2] +'</li>';
	        		}
	        	}
	            coordinate = {location: key, info: infoStr};

	            // Call the geocode function
	            geocodeAddress(geocoder, coordinate);
        	}

        }

    }


    // Function that will add in a marker
    function addMarker(coordinate){
        // Create the marker
        var marker = new google.maps.Marker({position: coordinate.coords, map: map, title: coordinate.info, clickable: true, animation: google.maps.Animation.DROP,});
        // Info window for the place
        var infoWindow = new google.maps.InfoWindow({content: coordinate.info});
        // Add in event listener to show popup when clicked
        marker.addListener('click', function(){
            if (lastWindow) lastWindow.close();
            infoWindow.open(map, marker);
            lastWindow = infoWindow;
        })
    }
    // Function for geocode to get the lat and lng
    function geocodeAddress(geocoder, address) {
        // Get the lat and lng for the address
        geocoder.geocode({'address': address.location}, function(results, status) {
            if (status === 'OK') {
                // Call the addMarker function
                addMarker({coords:results[0].geometry.location, info: address.info});
            } else if (status == google.maps.GeocoderStatus.OVER_QUERY_LIMIT) {
                setTimeout(function () {
                    //Recursively calling geocodeAddress with delay
                    geocodeAddress(geocoder, address);
                }, 100);
            }
        });
    }

}
