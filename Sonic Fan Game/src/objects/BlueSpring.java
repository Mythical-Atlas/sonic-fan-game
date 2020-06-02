package objects;

import java.awt.Graphics2D;

import datatypes.Animation;
import datatypes.Vector;
import main.Loader;
import rendering.Camera;
import rendering.Renderer;
import rendering.Shader;

import static java.lang.Math.*;

public class BlueSpring {
	public Vector pos;
	
	public int bouncing;
	
	private Animation smallAnim;
	private Animation largeAnim;
	
	public BlueSpring(Vector pos) {this(pos.x, pos.y);}
	public BlueSpring(double x, double y) {
		pos = new Vector(x, y);
		bouncing = 0;
		
		smallAnim = new Animation(Loader.blueSpring0Anim, new int[]{2, 2, 1, 5, 3}, 0);
		largeAnim = new Animation(Loader.blueSpring1Anim, new int[]{2, 2, 1, 5, 3}, 0);
	}
	
	public void manageAnimation(float dt) {
		if(bouncing == 1) {
			for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
				smallAnim.update(1);
				if(smallAnim.finished) {
					smallAnim.reset();
					bouncing = 0;
				}
			}
		}
		else if(bouncing == 2) {
			for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
				largeAnim.update(1);
				if(largeAnim.finished) {
					largeAnim.reset();
					bouncing = 0;
				}
			}
		}
	}
	
	public void draw(Renderer r) {
		if(bouncing == 0 || bouncing == 1) {smallAnim.draw(pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, Loader.scale, Loader.scale, r);}
		else {largeAnim.draw(pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, Loader.scale, Loader.scale, r);}
	}
	
	public void slowBounce() {if(bouncing == 0) {bouncing = 1;}}
	public void fastBounce() {if(bouncing == 0) {bouncing = 2;}}
}
