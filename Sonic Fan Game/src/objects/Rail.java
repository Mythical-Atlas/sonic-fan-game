package objects;

import static functionholders.ListFunctions.*;
import static functionholders.MathFunctions.*;
import static java.lang.Math.*;

import java.awt.Color;

import datatypes.Shape;
import datatypes.Vector;
import shapes.InverseArc;

public class Rail {
	public Vector[] corners;
	
	public Rail() {corners = null;}
	public Rail(Vector[] corners) {
		this.corners = new Vector[corners.length];
		for(int i = 0; i < corners.length; i++) {this.corners[i] = new Vector(corners[i].x, corners[i].y);}
	}
	
	public void appendCorner(Vector corner) {corners = append(corners, new Vector(corner.x, corner.y));}
	
	public Shape[] getShapes(int tileWidth, int tileHeight, int scale) {
		if(corners == null) {return(null);}
		
		Shape[] shapes = null;
		int w = tileWidth * scale;
		int s00 = 0;
		int s01 = w / 12;
		
		for(int i = 0; i < corners.length - 1; i++) {
			int x1 = (int)corners[i].x;
			int y1 = (int)corners[i].y;
			int x2 = (int)corners[i + 1].x;
			int y2 = (int)corners[i + 1].y;
			double angle = getAngleOfVector(new Vector(x2 - x1, y2 - y1)) + PI / 2;
			
			shapes = append(shapes, new Shape(new Vector[]{
				new Vector(x1 + s00, y1 + s00),
				new Vector(x2 + s00, y2 + s00),
				new Vector(x2 + cos(angle) * s01, y2 + sin(angle) * s01),
				new Vector(x1 + cos(angle) * s01, y1 + sin(angle) * s01),
			}, Color.WHITE));
		}
		
		return(shapes);
	}
}
