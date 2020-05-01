package shapes;

import java.awt.Color;
import java.awt.Graphics2D;

import datatypes.Shape;
import datatypes.Vector;

public class Triangle extends Shape {
	public Triangle(double ax, double ay, double bx, double by, double cx, double cy, Color color) {this(new Vector(ax, ay), new Vector(bx, by), new Vector(cx, cy), color);}
	public Triangle(Vector a, Vector b, Vector c, Color color) {
		this.color = color;
		
		points = new Vector[]{a, b, c};
	}
}
