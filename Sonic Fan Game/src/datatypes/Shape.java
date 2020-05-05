package datatypes;

import static functionholders.CollisionFunctions.*;
import static functionholders.ListFunctions.*;
import static functionholders.MathFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;

public class Shape {
	public Vector[] points; // clockwise rotation
	public Color color;
	
	public Shape() {}
	public Shape(Vector[] points, Color color) {
		this.points = points;
		this.color = color;
	}
	public Shape(Vector[] points) {
		this.points = points;
		color = Color.WHITE;
	}
	
	public void translate(Vector b) {translate(b.x, b.y);}
	public void translate(double dx, double dy) {for(int i = 0; i < points.length; i++) {points[i].translate(dx, dy);}}
	
	public void relocate(Vector b) {relocate(b.x, b.y);}
	public void relocate(double dx, double dy) {
		Vector delta = new Vector(dx - getCenter().x, dy - getCenter().y);
		
		for(int i = 0; i < points.length; i++) {
			points[i].translate(delta);
		}
	}
	
	public void rotateAroundPoint(Vector origin, Vector angle) {for(int i = 0; i < points.length; i++) {points[i].rotateAroundPoint(origin, angle);}}
	
	public Vector getCenter() {
		double x = 0;
		double y = 0;
		
		for(int i = 0; i < points.length; i++) {
			x += points[i].x;
			y += points[i].y;
		}
		
		return(new Vector(x / points.length, y / points.length));
	}
	
	public Vector[] getAxis(Shape b) {
		Vector[] axis = new Vector[points.length];
		
		for(int i = 0; i < points.length - 1; i++) {axis[i] = points[i + 1].subtract(points[i]).normalize();}
		axis[points.length - 1] = points[points.length - 1].subtract(points[0]).normalize();
		
		for(int i = 0; i < axis.length ; i++) {
			double tempX = axis[i].x;
			double tempY = axis[i].y;
			
			axis[i].x = tempY;
			axis[i].y = -tempX;
			
			axis[i] = fixAxis(axis[i]);
		}
		
		return(removeDupes(axis));
	}
	
	public Vector[] getShadow(Vector axis) {
		int smallestIndex = -1;
		int largestIndex = -1;
		double smallest = 0;
		double largest = 0;
		for(int i = 0; i < points.length; i++) {
			double temp = getProjectedValue(points[i], axis);
			
			if(smallestIndex == -1 || temp < smallest) {
				smallest = temp;
				smallestIndex = i;
			}
			if(largestIndex == -1 || temp > largest) {
				largest = temp;
				largestIndex = i;
			}
		}
		
		return(new Vector[]{points[smallestIndex], points[largestIndex]});
	}
	
	public void draw(Graphics2D graphics, Vector offset) {
		int[] xPoints = null;
		int[] yPoints = null;
		
		for(int i = 0; i < points.length; i++) {
			xPoints = append(xPoints, (int)(points[i].x - offset.x));
			yPoints = append(yPoints, (int)(points[i].y - offset.y));
		}
		
		graphics.setColor(color);
		graphics.fillPolygon(xPoints, yPoints, points.length);
	}
	
	public Vector[] getInverseShadow(Vector vector) {return(null);}
}
