 var planeSVG = "m2,106h28l24,30h72l-44,-133h35l80,132h98c21,0 21,34 0,34l-98,0 -80,134h-35l43,-133h-71l-24,30h-28l15,-47";

 var targetSVG = "M9,0C4.029,0,0,4.029,0,9s4.029,9,9,9s9-4.029,9-9S13.971,0,9,0z M9,15.93 c-3.83,0-6.93-3.1-6.93-6.93S5.17,2.07,9,2.07s6.93,3.1,6.93,6.93S12.83,15.93,9,15.93 M12.5,9c0,1.933-1.567,3.5-3.5,3.5S5.5,10.933,5.5,9S7.067,5.5,9,5.5 S12.5,7.067,12.5,9z";

 var planes = [];
 var lines = [];

 var departures = [];

 function getDestinationCoordinates(destination){

  var coordinates = [];

  for (var i in airports){

    if(destination == airports[i].abbr){

      coordinates[0] = airports[i].latitude;
      coordinates[1] = airports[i].longitude;

      return coordinates;
    }
  }
}

function getDepartures(origin){

  for(var i in flights){

    var flight = flights[i];

    if(origin.abbr == flight.origin){

      var departure = {};

      departure.origin = [origin.latitude, origin.longitude];
      departure.destination = getDestinationCoordinates(flight.destination);

      console.log("Length of path: " + flight.path.length + " Path:" + JSON.stringify(flight.path));

      if(flight.path.length > 0){

        var path = flight.path;
        var middlePoints = [];

        for(var p in path){

          var point = [path[p].latitude, path[p].longitude];
          console.log("Adding new extra point: " + point);
          middlePoints.push(point);
        }

        departure.points =  middlePoints;

      }else{

        departure.points = [];

      }

      departures.push(departure);

    }
  }

  console.log("Departures from that origin: " + JSON.stringify(departures));

}

var map = AmCharts.makeChart("mapdiv", {
  "type": "map",
  "mouseWheelZoomEnabled": false,

  "dataProvider": {
    "map": "worldLow",
    "backgroundZoomsToTop": true,
    "autoResize": true,
    "images": [],
    "lines": []
  },

  "areasSettings": {
   "unlistedAreasColor": "#CED8F6",
   "selectable": false
 },

 "imagesSettings": {
   "color": "#585869",
   "rollOverColor": "#585869",
   "selectedColor": "#585869",
   "pauseDuration": 0.2,
   "animationDuration": 2.5,
   "adjustAnimationSpeed": true
 },

 "linesSettings": {
  "color": "#585869",
  "alpha": 0.4
},

"listeners": [ {
        "event": "clickMapObject",
        "method": function( e ) {

        //We remove all the previous lines and planes
        lines = [];
        planes = [];
        departures = [];
        map.dataProvider.lines = [];
        map.dataProvider.images = [];

        for ( var x in airports ) {
          var airport = airports[ x ];
          //console.log(airport);
          airport.svgPath = targetSVG;
          airport.zoomLevel = 10;
          airport.scale = 0.5;
          airport.selectable = true;
          map.dataProvider.images.push( airport );
        }

        //We extract clicked airport
        var origin = e.mapObject;
        console.log("Origin: " + origin.title);

        getDepartures(origin);

        //Counter for the loop
        var i = 0;

        for(var d in departures){

          var f = departures[d];

          //We create the path for each flight
          switch(f.points.length){

            case 0:

            var line = {};
            line.id = "line" + i.toString();
            line.alpha= 0.3;
            line.latitudes= [f.origin[0], f.destination[0]];
            line.longitudes= [f.origin[1], f.destination[1]];
              //line.planeId = plane.id;
              map.dataProvider.lines.push(line);
              lines.push(line);

              //We add the plane for each path
              var plane = {};
              
              plane.id = "plane" + i.toString();
              plane.svgPath = planeSVG;
              plane.positionOnLine = 0;
              plane.animateAlongLine = true;
              plane.lineId = line.id;
              plane.loop = true;
              plane.scale = 0.01;
              plane.positionScale = 1.8;
              map.dataProvider.images.push(plane);
              planes.push(plane);
              

              break;

              case 1:

              var line = {};
              line.id = "line" + i.toString();
              line.alpha= 0.3;
              line.latitudes= [f.origin[0], f.points[0][0], f.destination[0]];
              line.longitudes= [f.origin[1], f.points[0][1], f.destination[1]];
              //line.planeId = plane.id;
              map.dataProvider.lines.push(line);
              lines.push(line);

              var plane = {};
              
              plane.id = "plane" + i.toString();
              plane.svgPath = planeSVG;
              plane.positionOnLine = 0;
              plane.animateAlongLine = true;
              plane.lineId = line.id;
              plane.loop = true;
              plane.scale = 0.01;
              plane.positionScale = 1.8;
              map.dataProvider.images.push(plane);
              planes.push(plane);

              break;

              /*case 2:

              var line = {};
              line.id = "line" + i.toString();
              line.alpha= 0.3;
              line.latitudes= [f.origin[0], f.points[0][0], f.points[1][0], f.destination[0]];
              line.longitudes= [f.origin[1], f.points[0][1], f.points[1][1], f.destination[1]];
              //line.planeId = plane.id;
              map.dataProvider.lines.push(line);
              lines.push(line);

              var plane = {};
              
              plane.id = "plane" + i.toString();
              plane.svgPath = planeSVG;
              plane.positionOnLine = 0;
              plane.animateAlongLine = true;
              plane.lineId = line.id;
              plane.loop = true;
              plane.scale = 0.01;
              plane.positionScale = 1.8;
              map.dataProvider.images.push(plane);
              planes.push(plane);

              break;*/

              default:

              var line = {};
              var latitudes = [];
              var longitudes = [];

              latitudes.push(f.origin[0]);
              longitudes.push(f.origin[1]);
              for(var q = 0; q < f.points.length; q++){
                latitudes.push(f.points[q][0]);
                longitudes.push(f.points[q][1]);
              }
              latitudes.push(f.destination[0]);
              longitudes.push(f.destination[1]);

              line.id = "line" + i.toString();
              line.alpha= 0.3;
              line.latitudes = latitudes;
              line.longitudes = longitudes;
              
              
              //line.planeId = plane.id;
              map.dataProvider.lines.push(line);
              lines.push(line);

              var plane = {};
              
              plane.id = "plane" + i.toString();
              plane.svgPath = planeSVG;
              plane.positionOnLine = 0;
              plane.animateAlongLine = true;
              plane.lineId = line.id;
              plane.loop = true;
              plane.scale = 0.01;
              plane.positionScale = 1.8;
              map.dataProvider.images.push(plane);
              planes.push(plane);

            }

            i++;

          }

          map.dataProvider.zoomLevel = map.zoomLevel();
          map.dataProvider.zoomLatitude = map.dataProvider.zoomLatitude = map.zoomLatitude();
          map.dataProvider.zoomLongitude = map.dataProvider.zoomLongitude = map.zoomLongitude();



          map.validateData();

      }

    }]


  });


// Populate the map when the page loads
AmCharts.ready( function() {

  for ( var x in airports ) {
    var airport = airports[ x ];
    //console.log(airport);
    airport.svgPath = targetSVG;
    airport.zoomLevel = 5;
    airport.scale = 0.5;
    airport.selectable = true;
    map.dataProvider.images.push( airport );
  }

  map.validateData();
} );

