package datatypes;

import static java.lang.Math.*;

import static functionholders.MathFunctions.*;

public class Vector {
	public double x;
	public double y;
	
	public Vector() {this(0, 0);}
	public Vector(double[] pos) {this(pos[0], pos[1]);}
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector add(double x, double y) {return(add(new Vector(x, y)));}
	public Vector add(Vector b) {return(new Vector(x + b.x, y + b.y));}
	
	public Vector subtract(double x, double y) {return(subtract(new Vector(x, y)));}
	public Vector subtract(Vector b) {return(new Vector(x - b.x, y - b.y));}
	
	public Vector multiply(double x, double y) {return(multiply(new Vector(x, y)));}
	public Vector multiply(Vector b) {return(new Vector(x * b.x, y * b.y));}
	
	public Vector divide(double x, double y) {return(divide(new Vector(x, y)));}
	public Vector divide(Vector b) {return(new Vector(x / b.x, y / b.y));}
	
	public Vector square() {return(new Vector(x * x, y * y));}
	
	public Vector normalize() {return(new Vector(x / getLength(), y / getLength()));}
	
	public Vector project(double x, double y) {return(project(new Vector(x, y)));}
	public Vector project(Vector b) {return(new Vector((getDotProduct(b) / (b.x * b.x + b.y * b.y)) * b.x,(getDotProduct(b) / (b.x * b.x + b.y * b.y)) * b.y));}
	
	public Vector getNew() {return(new Vector(x, y));}
	
	public Vector getPerpendicular() {return(new Vector(-y, x));}
	
	public double getDistance(double x, double y) {return(getDistance(new Vector(x, y)));}
	public double getDistance(Vector b) {return(subtract(b).getLength());}
	
	public double getLength() {return(Math.sqrt(x * x + y * y));}
	
	public double getDotProduct(double x, double y) {return(getDotProduct(new Vector(x, y)));}
	public double getDotProduct(Vector b) {return(x * b.x + y * b.y);}
	
	public boolean checkEqual(double bx, double by) {return(x == bx && y == by);}
	public boolean checkEqual(Vector b) {return(x == b.x && y == b.y);}
	
	public void translate(double x, double y) {translate(new Vector(x, y));}
	public void translate(Vector b) {
		Vector temp = add(b);
		x = temp.x;
		y = temp.y;
	}
	
	public void rotate(double x, double y) {rotate(new Vector(x, y));}
	public void rotate(Vector angles) {
		double length = getLength();
		double xAngle = acos(x / length) + angles.x;
		double yAngle = asin(y / length) + angles.y;
		
		x = cos(xAngle) * length;
		y = sin(yAngle) * length;
	}
	
	public void rotateAroundPoint(double originX, double originY, double anglesX, double anglesY) {rotateAroundPoint(new Vector(originX, originY), new Vector(anglesX, anglesY));}
	public void rotateAroundPoint(Vector origin, Vector angles) {
		x -= origin.x;
		y -= origin.y;
		
		double length = getLength();
		double xAngle = acos((x) / length) + angles.x;
		double yAngle = asin((y) / length) + angles.y;
		
		x = cos(xAngle) * length + origin.x;
		y = sin(yAngle) * length + origin.y;
	}
	
	public void rotateAroundPointTo(double originX, double originY, double anglesX, double anglesY) {rotateAroundPointTo(new Vector(originX, originY), new Vector(anglesX, anglesY));}
	public void rotateAroundPointTo(Vector origin, Vector angles) {
		x -= origin.x;
		y -= origin.y;
		
		if(angles.getLength() != 0) {
			double length = getLength();
			double currentAngle = getAngleOfVector(new Vector(x, y));
			double deltaAngle = getAngleOfVector(angles);
			double angle = currentAngle + deltaAngle;
			
			x = cos(angle) * length;
			y = sin(angle) * length;
		}
		
		x += origin.x;
		y += origin.y;
	}
	
	public void rotateAroundPointTo(double originX, double originY, double angles) {rotateAroundPointTo(new Vector(originX, originY), angles);}
	public void rotateAroundPointTo(Vector origin, double angles) {
		x -= origin.x;
		y -= origin.y;
		
		double length = getLength();
		double currentAngle = getAngleOfVector(new Vector(x, y));
		double angle = currentAngle + angles;
		
		x = cos(angle) * length;
		y = sin(angle) * length;
		
		x += origin.x;
		y += origin.y;
	}
	
	public Vector scale(double amount) {return(new Vector(x * amount, y * amount));}
}
