package objects;

import java.awt.Graphics2D;

import datatypes.Animation;
import datatypes.Vector;
import main.Loader;

public class Ring {
	public Vector pos;
	private Animation anim;
	public int destroy;
	
	public Ring(double x, double y) {
		pos = new Vector(x, y);
		anim = Loader.ringAnim;
		destroy = 0;
	}
	
	public void draw(Vector offset, int scaleX, int scaleY, Graphics2D graphics) {
		if(destroy == 0) {
			anim.draw(graphics, pos.x - offset.x, pos.y - offset.y, scaleX, scaleY);
			anim.update(1);
		}
		if(destroy == 1) {
			destroy = 2;
			anim = Loader.sparkleAnim;
		}
		
		anim.draw(graphics, pos.x - offset.x, pos.y - offset.y, scaleX, scaleY);
		anim.update(1);
		
		if(destroy == 2 && anim.finished) {destroy = 3;}
	}
}
