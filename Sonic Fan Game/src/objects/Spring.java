package objects;

import java.awt.Graphics2D;

import datatypes.Animation;
import datatypes.Vector;
import main.Loader;
import rendering.Camera;
import rendering.Renderer;
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
		bouncing = false;
		
		switch(type) {
			case(0): anim = new Animation(Loader.spring0Anim, new int[]{2, 2, 1, 5, 3}, 0); break;
			case(1): anim = new Animation(Loader.spring1Anim, new int[]{2, 2, 1, 5, 3}, 0); break;
			case(2): anim = new Animation(Loader.spring2Anim, new int[]{2, 2, 1, 5, 3}, 0); break;
			case(3): anim = new Animation(Loader.spring3Anim, new int[]{2, 2, 1, 5, 3}, 0); break;
			case(4): anim = new Animation(Loader.spring4Anim, new int[]{2, 2, 1, 5, 3}, 0); break;
			case(5): anim = new Animation(Loader.spring5Anim, new int[]{2, 2, 1, 5, 3}, 0); break;
			case(6): anim = new Animation(Loader.spring6Anim, new int[]{2, 2, 1, 5, 3}, 0); break;
			case(7): anim = new Animation(Loader.spring7Anim, new int[]{2, 2, 1, 5, 3}, 0); break;
			case(8): anim = new Animation(Loader.spring8Anim, new int[]{2, 2, 1, 5, 3}, 0); break;
			case(9): anim = new Animation(Loader.spring9Anim, new int[]{2, 2, 1, 5, 3}, 0); break;
			case(10): anim = new Animation(Loader.spring10Anim, new int[]{2, 2, 1, 5, 3}, 0); break;
			case(11): anim = new Animation(Loader.spring11Anim, new int[]{2, 2, 1, 5, 3}, 0); break;
		}
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
	
	public void draw(int scaleX, int scaleY, Renderer r) {anim.draw(pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, scaleX / 2 * Loader.scale, scaleY / 2 * Loader.scale, r);}
}
