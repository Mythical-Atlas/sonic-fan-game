package shapes;

import static functionholders.ListFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;

import datatypes.Shape;
import datatypes.Vector;

public class Circle extends Shape {
	public Circle() {}
	public Circle(Vector origin, double radius, Color color) {
		this.color = color;
		
		points = new Vector[]{origin, origin.add(radius, 0)};
	}
	public Circle(double radius, Color color) {
		this.color = color;
		
		Vector origin = new Vector();
		
		points = new Vector[]{origin, origin.add(radius, 0)};
	}
	public Circle(Vector origin, double radius) {
		color = Color.WHITE;
		
		points = new Vector[]{origin, origin.add(radius, 0)};
	}
	public Circle(double radius) {
		color = Color.WHITE;
		
		Vector origin = new Vector();
		
		points = new Vector[]{origin, origin.add(radius, 0)};
	}

	public Vector getCenter() {return(points[1]);}
	public double getRadius() {return(points[0].getDistance(points[1]));}
	
	public Vector[] getAxis(Shape b) {
		Vector[] axis = new Vector[b.points.length];
		
		for(int i = 0; i < axis.length; i++) {axis[i] = points[1].subtract(b.points[i]).normalize();}
		
		for(int i = 0; i < axis.length ; i++) {
			if(axis[i].y < 0) {axis[i] = axis[i].scale(-1);}
			if(axis[i].x < 0) {axis[i] = axis[i].scale(-1);}
		}
		
		return(removeDupes(axis));
	}
	
	public Vector[] getShadow(Vector axis) {return(new Vector[]{getCenter().subtract(axis.scale(getRadius())), getCenter().add(axis.scale(getRadius()))});}
	
	public void draw(Graphics2D graphics, Vector offset) {
		graphics.setColor(color);
		graphics.fillOval((int)(points[1].x - getRadius() - offset.x), (int)(points[1].y - getRadius() - offset.y), (int)getRadius() * 2, (int)getRadius() * 2);
	}
}
