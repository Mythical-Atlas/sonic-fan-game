package objects;

import java.awt.Graphics2D;

import datatypes.Animation;
import datatypes.Vector;
import main.Loader;
import rendering.Camera;
import rendering.Shader;

import static java.lang.Math.*;

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
	
	public void manageAnimation(float dt) {
		if(bouncing) {
			for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {anim.update(1);}
			
			if(anim.finished) {
				anim.reset();
				bouncing = false;
			}
		}
	}
	
	public void draw(int scaleX, int scaleY, Shader shader, Camera camera) {
		if(!bouncing) {anim.draw(pos.x, pos.y, scaleX, scaleY, shader, camera);}
		else {anim.draw(pos.x, pos.y, scaleX, scaleY, shader, camera);}
	}
}
