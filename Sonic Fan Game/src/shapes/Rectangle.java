package shapes;

import java.awt.Color;
import java.awt.Graphics2D;

import datatypes.Shape;
import datatypes.Vector;

public class Rectangle extends Shape {
	public Rectangle(double x, double y, double w, double h, Color color) {this(new Vector(x, y), new Vector(w, h), color);}
	public Rectangle(Vector pos, Vector size, Color color) {
		this.color = color;
		
		points = new Vector[]{pos, pos.add(size.x, 0), pos.add(size.x, size.y), pos.add(0, size.y)};
	}
	public Rectangle(double w, double h) {this(new Vector(0, 0), new Vector(w, h), Color.WHITE);}
}
