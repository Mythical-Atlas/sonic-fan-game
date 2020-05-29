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
import rendering.Renderer;
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
		int s1 = w / 12;
		int s00 = 0;
		int s01 = w / 12;
		int s03 = s1 * 3;
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
	
	public void draw(int scaleX, int scaleY, float dt, Renderer r) {image.draw(pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, scaleX / 2 * Loader.scale, scaleY / 2 * Loader.scale, r);}
}
