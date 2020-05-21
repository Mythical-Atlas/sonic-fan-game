package objects;

import java.awt.Graphics2D;

import datatypes.Animation;
import datatypes.Vector;
import main.Loader;
import rendering.Camera;
import rendering.Shader;

import static java.lang.Math.*;

public class SpringPole {
	public Vector pos;
	public int direction;
	public boolean bouncing;
	
	private boolean slowBounce;
	private boolean fastBounce;
	private Animation slowAnim;
	private Animation fastAnim;
	
	public SpringPole(Vector pos, int direction) {this(pos.x, pos.y, direction);}
	public SpringPole(double x, double y, int direction) {
		this.direction = direction;
		
		pos = new Vector(x, y);
		fastAnim = new Animation(Loader.springPoleFastAnim, new int[]{1, 5, 5, 5, 5, 5, 5, 5, 6}, 0);
		slowAnim = new Animation(Loader.springPoleSlowAnim, new int[]{1, 5, 5, 5, 5, 6}, 0);
		slowBounce = false;
		fastBounce = false;
		bouncing = false;
	}
	
	public void draw(int scaleX, int scaleY, float dt, Shader shader, Camera camera) {
		if(!slowBounce && !fastBounce) {
			fastAnim.reset();
			slowAnim.reset();
			fastAnim.draw(pos.x, pos.y, scaleX * direction, scaleY, shader, camera);
		}
		else if(fastBounce) {
			fastAnim.draw(pos.x, pos.y, scaleX * direction, scaleY, shader, camera);
			
			for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
				fastAnim.update(1);
			
				if(fastAnim.frame == 4) {bouncing = false;}
				if(fastAnim.finished) {
					fastAnim.reset();
					slowAnim.reset();
					fastBounce = false;
					slowBounce = false;
				}
			}
		}
		else if(slowBounce) {
			slowAnim.draw(pos.x, pos.y, scaleX * direction, scaleY, shader, camera);
			
			for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
				slowAnim.update(1);
			
				if(slowAnim.frame == 3) {bouncing = false;}
				if(slowAnim.finished) {
					fastAnim.reset();
					slowAnim.reset();
					fastBounce = false;
					slowBounce = false;
				}
			}
		}
	}
	
	public void fastBounce() {
		slowBounce = false;
		fastBounce = true;
		bouncing = true;
	}
	public void slowBounce() {
		slowBounce = true;
		fastBounce = false;
		bouncing = true;
	}
}
