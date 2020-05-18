package objects;

import static functionholders.ListFunctions.append;
import static java.lang.Math.PI;

import java.awt.Color;
import java.awt.Graphics2D;

import datatypes.Animation;
import datatypes.Vector;
import datatypes.Shape;
import main.Loader;
import rendering.Camera;
import rendering.Image;
import rendering.Shader;
import shapes.InverseArc;

public class Ramp {
	public int direction;
	public Vector pos;
	public double angle;
	public double strength;
	
	private Image image;
	
	public Ramp(double x, double y, double angle, double strength) {
		this.angle = angle;
		this.strength = strength;
		
		direction = 1;
		pos = new Vector(x, y);
		image = new Image(Loader.ramp);
	}
	
	public Shape[] getShapes(int tileWidth, int tileHeight, int scale) {
		int w = tileWidth * scale;
		int h = tileHeight * scale;
		int w2 = w / 2;
		int h2 = h / 2;
		int w3 = w / 3;
		int h3 = h / 3;
		int w6 = w / 6;
		int h6 = h / 6;
		int w12 = w / 12;
		int h12 = h / 12;
		int w24 = w / 24;
		int h24 = h / 24;
		int s0 = 0;
		int s1 = w / 12;
		int s2 = s1 * 2;
		int s3 = s1 * 3;
		int s4 = s1 * 4;
		int s5 = s1 * 5;
		int s6 = s1 * 6;
		int s7 = s1 * 7;
		int s8 = s1 * 8;
		int s9 = s1 * 9;
		int s00 = 0;
		int s01 = w / 12;
		int s02 = s1 * 2;
		int s03 = s1 * 3;
		int s04 = s1 * 4;
		int s05 = s1 * 5;
		
		int x = (int)pos.x;
		int y = (int)pos.y;
		
		Shape[] shapes = null;
		
		InverseArc a = new InverseArc(
			new Vector(x + s01 + s01 / 2, y + s03 + s01 / 2),
			PI + PI / 2,
			PI + PI / 2 + PI / 4,
			s03,
		Color.WHITE);
		
		shapes = append(shapes, new Shape(new Vector[]{
			a.points[2],
			new Vector(x + s05, y + s00),
			new Vector(x + s05, y + s03 + s01 / 2),
			new Vector(a.points[2].x, y + s03 + s01 / 2)
		}, Color.WHITE));
		shapes = append(shapes, a);
		
		return(shapes);
	}
	
	public void draw(int scaleX, int scaleY, float dt, Shader shader, Camera camera) {image.draw(pos.x, pos.y, scaleX, scaleY, shader, camera);}
}
