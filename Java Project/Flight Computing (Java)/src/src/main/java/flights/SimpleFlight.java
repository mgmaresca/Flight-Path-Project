package src.main.java.flights;

import java.util.ArrayList;
import java.util.List;

import src.main.java.tools.Point;

public class SimpleFlight {
	
	private String origin;
	private String destination;
	//private Double timestamp;
	private List<Point> path = new ArrayList<Point>();
	
	public SimpleFlight(String origin, String destination, List<Point> path){
		if(origin == null){
			this.origin = "";
		}else{
			this.origin = origin;
		}
		
		if(origin == null){
			this.destination = "";
		}else{
			this.destination = destination;
		}
		
		/*
		if(timestamp == null){
			this.timestamp = 0.0;
		}else{
			this.timestamp = timestamp;
		}
		*/
		
		this.path = path;
	};
	
	public List<Point> getPath(){
		return this.path;
	};
	
	public void setPath(List<Point> newPath){
		this.path = newPath;
	};
	
	public String getOrigin(){
		return this.origin;
	};
	
	public void setOrigin(String origin){
		this.origin = origin;
	};
	
	public String getDestination(){
		return this.destination;
	};
	
	public void setDestination(String destination){
		this.destination = destination;
	};
	
	/*
	public Double getTime(){
		return this.timestamp;
	};
	
	public void setTime(Double time){
		this.timestamp = time;
	};
	*/
	
}
