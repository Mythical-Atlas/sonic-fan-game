package objects;

import java.awt.Graphics2D;

import datatypes.Animation;
import datatypes.Vector;
import main.Loader;
import rendering.Camera;
import rendering.Renderer;
import rendering.Shader;

import static java.lang.Math.*;

public class DashPad {
	public Vector pos;
	public double angle;
	public int direction;
	
	private Animation anim;
	
	public DashPad(double x, double y, int direction, double angle) {
		this.direction = direction;
		this.angle = angle;
		
		pos = new Vector(x, y);
		
		anim = new Animation(Loader.dashPad, new int[]{2, 2, 1, 5, 3}, 0);
	}
	
	public void manageAnimation(float dt) {for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {anim.update(1);}}
	
	public void draw(Renderer r) {anim.draw(pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, Loader.scale * direction, Loader.scale, r);}
}
