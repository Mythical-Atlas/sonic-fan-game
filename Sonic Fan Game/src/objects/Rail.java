package objects;

import static functionholders.ListFunctions.*;
import static functionholders.MathFunctions.*;
import static java.lang.Math.*;

import java.awt.Color;

import datatypes.Shape;
import datatypes.Vector;
import shapes.Arc;
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
		int s02 = s01 * 2;
		int s03 = s01 * 3;
		int s04 = s01 * 4;
		int s05 = s01 * 5;
		
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
		
		for(int i = 1; i < corners.length - 1; i++) {
			int x0 = (int)corners[i - 1].x;
			int y0 = (int)corners[i - 1].y;
			int x1 = (int)corners[i].x;
			int y1 = (int)corners[i].y;
			int x2 = (int)corners[i + 1].x;
			int y2 = (int)corners[i + 1].y;
			
			double angle = getAngleOfVector(new Vector(x2 - x1, y2 - y1)) + PI / 2;
			double angle0 = getAngleOfVector(new Vector(x2 - x1, y2 - y1));
			double angle1 = getAngleOfVector(new Vector(x0 - x1, y0 - y1));
			double difAngle = angle1 - angle0;
			
			while(difAngle > PI * 2) {difAngle -= PI * 2;}
			while(difAngle < 0)      {difAngle += PI * 2;}
			
			if(difAngle < PI && angle1 == PI) {
				/*System.out.println(angle0);
				System.out.println(angle1);
				System.out.println(difAngle);
				System.out.println();*/
				
				shapes = append(shapes, new InverseArc( // WHY DOESN'T THIS WORK?!?!?!
					new Vector(x1, y1),
					angle0,
					angle1,
					s03,
				Color.WHITE));
			}
			/*else if(difAngle > PI) {
				shapes = append(shapes, new Arc(
					new Vector(x1, y1),
					angle0,
					angle1,
					s01,
				Color.WHITE));
			}*/
		}
		
		return(shapes);
	}
}
