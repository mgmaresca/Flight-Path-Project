// Definition of JSON with all the flight paths
//
//
// var flights = [{
// 	"timestamp":0.0,		-- Timestamp of the beginning of the flight
// 	"origin": "",			-- Abbrev of the airport (This info is compare with the airport JSON in order to obtain lat and lon)
// 	"destination": "",		-- Abbrev of the airport (This info is compare with the airport JSON in order to obtain lat and lon)
// 	"path": [{				-- If more than two points, here are the rest of middle points of the path
// 			"latitude": 0.0,
// 			"longitude": 0.0
// 		},{
// 			"latitude": 0.0,
// 			"longitude": 0.0
// 		}
// 	]
// }];