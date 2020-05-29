package objects;

import java.awt.Graphics2D;

import datatypes.Animation;
import datatypes.Vector;
import main.Loader;
import rendering.Camera;
import rendering.Renderer;
import rendering.Shader;

public class Rotor {
	public int facing;
	public Vector pos;
	public Animation anim;
	
	public Rotor(double x, double y) {
		pos = new Vector(x, y);
		anim = new Animation(Loader.rotorAnim, new int[]{4, 4, 4, 4, 4, 4}, 0);
		facing = 1;
	}
	
	public void draw(int scaleX, int scaleY, float dt, Renderer r) {anim.draw(pos.x - anim.getCurrentWidth() / 2 * scaleX / 2 * Loader.scale, pos.y - anim.getCurrentHeight() / 2 * scaleY / 2 * Loader.scale, scaleX / 2 * Loader.scale * facing, scaleY / 2 * Loader.scale, r);}
}
