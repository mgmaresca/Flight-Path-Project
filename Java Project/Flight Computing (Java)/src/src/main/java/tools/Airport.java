package src.main.java.tools;

public class Airport {

		private String abbr;
		private Point coordinates;
		
		public Airport(String abbr, Point coordinates){
			this.abbr = abbr;
			this.coordinates = coordinates;
		}
		
		public String getAbbr(){
			return this.abbr;
		}
		
		public Point getCoordinates(){
			return this.coordinates;
		}
}
