package src.main.java.flights;

import java.io.BufferedReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import src.main.java.tools.Airport;
import src.main.java.tools.PathReducer;
import src.main.java.tools.Point;


public class Path {
	
	private List<Point> positions = new ArrayList<Point>();
	private List<Point> altitude = new ArrayList<Point>();
	
	private List<Integer> landings = new ArrayList<Integer>();
	private List<SimpleFlight> simpleFlights = new ArrayList<SimpleFlight>();
	private List<Airport> airports = new ArrayList<Airport>();
	
	
	// Constructor of the class
	// Takes a CSV in order to rescue the values of positions and altitude of the flight
	
	public Path(String input, String output) throws IOException{
		readCSV(input);
		System.out.println("Number of messages: " + positions.size());
		computePath(output);
	};
	
	// Get method that returns the positions
	public List<Point> getPositions(){
		return this.positions;
	};
	
	// Get method that returns the altitude
	public List<Point> getAltitude(){
		return this.altitude;
	};
	
	public List<Integer> getLandings(){
		return this.landings;
	}
	
	// Method to read the content of a CSV file
	private void readCSV(String filename){
			
		BufferedReader fileReader = null;
	    
	    //Delimiter used in CSV file
	    final String DELIMITER = ",";
	    try
	        {
	          String line = "";
	            //Create the file reader
	            fileReader = new BufferedReader(new FileReader(filename));
	            
	          
		            while ((line = fileReader.readLine()) != null)
	            {
	                //Get all tokens available in line
	                String[] tokens = line.split(DELIMITER);
	                
		                // Position stored as a Point(latitude, longitude)
	                this.positions.add(new Point(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])));
	                // Altitude stored as Point(timestamp, altitude)
	                this.altitude.add(new Point(Double.parseDouble(tokens[3]), Double.parseDouble(tokens[0])));
	            }
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        finally
	        {
	            try {
	                fileReader.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	};
	
	// Creates an ArrayList of the major airports in the world
	// IMPORTANT:	The CSV from where the method read the data in order to create the ArrayList is saved locally.
	//				Change the path if necessary
	private void createAirportList(){
		
		BufferedReader fileReader = null;
	    
	    //Delimiter used in CSV file
	    final String DELIMITER = ",";
	    try
	        {
	          String line = "";
	            //Create the file reader
	            fileReader = new BufferedReader(new FileReader("/home/maria/Escritorio/Final visualisation/javascripts/data/airports_def.csv"));
	            
	          
	            while ((line = fileReader.readLine()) != null)
	            {
	                //Get all tokens available in line
	                String[] tokens = line.split(DELIMITER);
	                
	                Airport a = new Airport(tokens[0], new Point(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])));
	                this.airports.add(a);
	            }
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        finally
	        {
	            try {
	                fileReader.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    
	    System.out.println("List of airports: " + this.airports);
		
	}
	
	// Given an array of altitudes, the method calculate the slope within the path
	private List<Double> calculateSlopes(){
		
		List<Double> slopes = new ArrayList<Double>();
		
		if(this.altitude.size() > 0){
			
			for(int i = 0; i < this.altitude.size(); i++){
				
				if(i == 0){
					slopes.add(0.0);
				}else{
					
					slopes.add(this.altitude.get(i).getY() - this.altitude.get(i-1).getY());
				}
			}	
		}
		
		
		return slopes;
	};
	
	// The method identifies the different landings taking on account the altitude of the
	// aircraft. We assume that if there is a descend of 1500 feet or more, the plane is going to land
	private void identifyLandings(){
		
		Boolean isLanding = false;
		
		Double slope = 0.0;
		Double descend = 0.0;
		int iterator = 0;
		List<Double> slopes = calculateSlopes();
		
	
		double ascend = 0.0;
		if(slopes.size() > 0){
			
			for(int j = 0; j < slopes.size(); j++){
				
				slope = slopes.get(j);
				
				if(!isLanding && slope < 0){
					
					descend = - slope;
					isLanding = true;
					
				}else if (isLanding && slope < 0.0 && descend < 1500){
					
					descend = descend - slope;
					
				}else if (isLanding && slope <= 0.0 && descend >= 1500){
					
					//hugeDesc = false;
					iterator = j;
					
					
				}else if(isLanding && slope > 0.0 && ascend < 800){
					
					//System.out.println("Slope is positive");
					ascend = ascend + slope;
					//System.out.println("Ascend: " + ascend);
					
				}else if (isLanding && slope > 0.0 && ascend >= 800 && iterator != 0){
					
					//System.out.println("We start looking for a new landing. Number iteration: " + j + " Slope: " + slope);
					landings.add(iterator);
					iterator = 0;
					descend = 0.0;
					isLanding = false;
					ascend = 0.0;
					
				}
			}
		}
		
		
		System.out.println("Landings: " + landings);
		
	};
	
	// Once the landings are identified we can split the list of coordinates into different flight paths
	private void splitFlights(){
		
		int iterator = 0;
		
		if(landings.size() > 0){
			
			for(int i = 0; i < landings.size(); i++){
				
				List<Point> path = new ArrayList<Point>();
					
					System.out.println("Iterator: " + iterator + " Last index: " + (landings.get(i)+1) );
					for(int j = iterator; j < (landings.get(i)+1); j ++){
						
						path.add(this.positions.get(j));
					}
					
					SimpleFlight flight = new SimpleFlight(null, null, path);
					
					
					
					iterator = landings.get(i);
					this.simpleFlights.add(flight);
				}
		}
		
		
		System.out.println("Number of flights created: " + this.simpleFlights.size());
		
			
	};
	
	// Private method used in findAirport() in order to find the minimum distance given a List of
	// distances to the different airports
	private int minimunDistance(List<Double> distances){
		
		Double distance = distances.get(0);
		int iterator = 0;
		for(int i = 1; i < distances.size(); i++){
			
			if(distances.get(i)<distance){
				distance = distances.get(i);
				iterator = i;
			}
		}
		
		if(distance <= 130){
			return iterator;
		}else{
			return -1;
		}
	}
	
	// Given a particular point returns the nearest airport to it (The maximum distance is 130km)
	// It creates an ArrayList with the distance of that point to every airport, and using the method
	// minimum distance identifies the nearest airport.
	private Airport findAirport(Point p){
		
		List<Double> distances = new ArrayList<Double>();
			
		for(int i= 0; i < this.airports.size(); i++){
			
			Point coordinates = this.airports.get(i).getCoordinates();
			distances.add(p.distanceTo(coordinates));	
			
		}
		
		if(minimunDistance(distances) >= 0 ){
			return this.airports.get(minimunDistance(distances));
		}else{
			return null;
		}
	}
	
	// Method to identify if there are important losses of messages 
	// NOTE: This method is never used in the main execution of the program
	public void identifyJumps(){
		
		Double lat = 0.0;
		Double lon = 0.0;
		
		Double prev_lat = 0.0;
		Double prev_lon = 0.0;
		
		Double lat_diff = 0.0;
		Double lon_diff = 0.0;
		
		for (int i = 1; i < positions.size(); i++){
			
			prev_lat = positions.get(i-1).getX();
			prev_lon = positions.get(i-1).getY();
			
			lat = positions.get(i).getX();
			lon = positions.get(i).getY();
			
			lat_diff = Math.abs(lat-prev_lat);
			
			lon_diff = Math.abs(lon-prev_lon);
			
			if(lat_diff > 1.5 || lon_diff > 1.5){
				
				landings.add(i);
				System.out.println("lat_diff " + i + " " + lat_diff);
				System.out.println("lon_diff " + i + " " + lon_diff);
				System.out.println("Point: " + i + " " + positions.get(i));
				System.out.println("Point: " + (i-1) + " " + positions.get(i-1));
				System.out.println("Altitude point: " + i + " " + altitude.get(i).getY());
				System.out.println("Altitude point: " + (i-1) + " " + altitude.get(i-1).getY());
				System.out.println(" ");
			}
		}
			
		
		System.out.println("Resulting number of landings: " + landings.size());
		System.out.println("Resulting landings array: " + this.landings);
	};

	// MAIN METHOD OF THE CLASS
	// Given a sorted list of position messages of an aircraft (Each message includes: altitude, latitude, longitude and timestamp)
	// Computes the different flights made by the plane
	private void computePath(String output) throws IOException{
		
		createAirportList();
		identifyLandings();
		splitFlights();
		
		List<SimpleFlight> flights = new ArrayList<SimpleFlight>();
		
		if(this.simpleFlights.size() > 0){
			for(int i = 0; i < this.simpleFlights.size(); i++){
				
				List<Point> path = this.simpleFlights.get(i).getPath();
				
				Airport origin = findAirport(path.get(0));
				Airport destination = findAirport(path.get(path.size()-1));
				
				if(origin != null && destination != null && !origin.equals(destination)){
					path.add(0,origin.getCoordinates());
					path.add(destination.getCoordinates());
					
					System.out.println("Path before reducing: " + path.size());
					path = PathReducer.reduce(path, 1.5);
					System.out.println("Path after reducing: " + path.size());
					
					flights.add(new SimpleFlight(origin.getAbbr(), destination.getAbbr(), path));
					
				}
				
			}
			
			this.simpleFlights.clear();
			this.simpleFlights = flights;
			
			System.out.println("Total number of flights with complete path: " + simpleFlights.size());
			for(int j= 0; j<simpleFlights.size(); j++){
				System.out.println("Flight path " + j + " " + " Origin: " + simpleFlights.get(j).getOrigin() + " Path: " + simpleFlights.get(j).getPath() );
				System.out.println("Destination " + simpleFlights.get(j).getDestination());
				System.out.println(" ");
			}
			
			if(simpleFlights.size() > 0){
				
				writeOutputFile(output);
				
			}else{
				
				System.out.println("After reducing paths and searching airports, there are no flights recorded");
			}
			
		}else{
			
			System.out.println("There are no flights recorded");
			
		}
		
	}
	
	private void writeOutputFile(String output) throws IOException{
		
		JSONArray json = new JSONArray();
		
		for(int i=0; i < simpleFlights.size(); i++){
			
			JSONObject obj = new JSONObject();
			SimpleFlight flight = simpleFlights.get(i);
			
			JSONArray path = new JSONArray();
			
			if(flight.getPath().size() > 2){
				
				System.out.println("SimpleFlight large: " + i + " Size of path: " + flight.getPath().size());
				
				List<Point> middlePoints = new ArrayList<Point>();
				middlePoints = flight.getPath().subList(1,flight.getPath().size()-1);
				
				System.out.println("Simplified path: " + middlePoints);
				
				for(int j=0; j < middlePoints.size(); j++){
					
					JSONObject coordinate = new JSONObject();
					
					coordinate.put("latitude", middlePoints.get(j).getX());
					coordinate.put("longitude", middlePoints.get(j).getY());

					System.out.println("Coordinate " + j + ": " + coordinate.toString());
					
					path.add(coordinate);
				}
				
				
				
			}else{
				
				System.out.println("SimpleFlight: " + i + " Size of path: " + flight.getPath().size());
				
			}
			
			System.out.println("Path to add to JSON " + path.toString());
			
			obj.put("path", path);
			obj.put("destination", flight.getDestination());
			obj.put("origin", flight.getOrigin());
			
			System.out.println("Final jSON object " + obj.toString());
			System.out.println(" ");
			json.add(obj);
			
			
		}
		
		System.out.println("\nJSON Object: " + json.toString());
		
		File f = new File(output);
		
		f.getParentFile().mkdirs(); 
		f.createNewFile();
		
		FileWriter file = new FileWriter(f);
		
		try {
			file.write("var flights = ");
			file.write(json.toString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("\nJSON Object: " + json.toString());
			
		}catch (Exception e){
			
			System.out.println("An error ocurred while writing the output file of computePath() method: " + e);
			
		}finally{
			
			file.flush();
			file.close();
		}
		
	}
	
	// HELPER FUNCTION
	// Converts UNIX timestamp to a readable date format
	public void unixtime2date(){
		
		for(int i = 0; i < this.altitude.size(); i++){
			
			long unixSeconds = (long) altitude.get(i).getX();
			Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
			sdf.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating (see comment at the bottom
			String formattedDate = sdf.format(date);
			System.out.println(formattedDate);
		}
		
	};
	
	public static void main(String[] args) throws IOException {
		
		if(args.length == 2){
			
			Path newPath = new Path(args[0], args[1]);
			
		}else{
			
			System.out.println("Two parameters are required to run this function:");
			System.out.println("	First parameter: path of the file from where to read position messages");
			System.out.println("	Second parameter: path of the file where to write the file with computed paths");
			System.out.println(" ");
			System.out.println("IMPORTANT: This function works ONLY with FILES not FOLDERS");
		}
		
		
		//Uncomment this line to tun test locally
		//Path mypath = new Path("/home/maria/Escritorio/Datatest/flight214.csv", "/home/maria/Escritorio/Final visualisation/javascripts/data/flight.js");
	
		
	};

}
