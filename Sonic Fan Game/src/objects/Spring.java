package objects;

import java.awt.Graphics2D;

import datatypes.Animation;
import datatypes.Vector;
import main.Loader;
import rendering.Camera;
import rendering.Shader;

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
		anim = new Animation(Loader.springAnim, new int[]{2, 2, 1, 5, 3}, 0);
		bouncing = false;
	}
	
	public void draw(int scaleX, int scaleY, float dt, Shader shader, Camera camera) {
		if(!bouncing) {anim.draw(pos.x, pos.y, scaleX, scaleY, shader, camera);}
		else {
			anim.draw(pos.x, pos.y, scaleX, scaleY, shader, camera);
			anim.update(dt / (1.0f / 60.0f));
			if(anim.finished) {
				anim.reset();
				bouncing = false;
			}
		}
	}
}
