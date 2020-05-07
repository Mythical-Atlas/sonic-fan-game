package objects;

import java.awt.Graphics2D;

import datatypes.Animation;
import datatypes.Vector;
import main.Loader;

public class Spring {
	public Vector pos;
	public double angle;
	public double strength;
	public boolean bouncing;
	
	private Animation anim;
	private int type;
	
	public Spring(double x, double y, double angle, double strength, int type) {
		this.angle = angle;
		this.strength = strength;
		this.type = type;
		
		pos = new Vector(x, y);
		anim = new Animation(Loader.springAnim.frames, Loader.springAnim.durations, Loader.springAnim.repeatFrame, 1);
		bouncing = false;
	}
	
	public void draw(Vector offset, int scaleX, int scaleY, Graphics2D graphics) {
		if(!bouncing) {anim.draw(graphics, pos.x - offset.x, pos.y - offset.y, scaleX, scaleY);}
		else {
			anim.draw(graphics, pos.x - offset.x, pos.y - offset.y, scaleX, scaleY);
			anim.update(1);
			if(anim.finished) {
				anim.reset();
				bouncing = false;
			}
		}
	}
}
