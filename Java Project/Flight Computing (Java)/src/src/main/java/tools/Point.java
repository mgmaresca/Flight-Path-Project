package src.main.java.tools;

public class Point {

	private double x;
    
    private double y;
    
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
    
    public Double getDistance(Point p){
    	Double x1 = this.x;
    	Double y1 = this.y;
    	
    	Double x2 = p.getX();
    	Double y2 = p.getY();
    	
    	return Math.hypot(x2-x1, y2-y1);
    }
    
    public double distanceTo(Point p2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(this.x - p2.getX());
        Double lonDistance = Math.toRadians(this.y - p2.getY());
        
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.x)) * Math.cos(Math.toRadians(p2.getX()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        double distance = R * c ; // convert to meters

        return distance;
    }
}
