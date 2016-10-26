// Simple script to filter and order airport database from OpenFlights
// We only need the most important airport around the world

var airports = [];

function filterAirports(){

    var total = 0;

    for(i in airports_raw.features){

        var p = airports_raw.features[i].properties;
        var c = airports_raw.features[i].geometry.coordinates;

        if (p.type == "major"){

            var a = {};
            a.title = p.name;
            a.abbr = p.abbrev;
            a.latitude = c[1];
            a.longitude = c[0];

            airports.push(a);
            total++;

        }
    }

   console.log( JSON.stringify(airports));

    console.log(" Total number of airports: " + total);
}
