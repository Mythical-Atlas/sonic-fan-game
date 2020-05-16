package objects;

import java.nio.ByteBuffer;

import datatypes.Animation;
import datatypes.Vector;
import main.Loader;
import rendering.Camera;
import rendering.Shader;

public class Item {
	public int type;
	public int destroy;
	public Vector pos;
	
	private Animation anim;
	
	public Item(double x, double y) {
		pos = new Vector(x, y);
		anim = new Animation(new ByteBuffer[]{Loader.itemBox}, new int[]{10}, 0);
		destroy = 0;
	}

	public void update(float dt) {}
	public void draw(int scaleX, int scaleY, float dt, Shader shader, Camera camera) {
		anim.draw(pos.x, pos.y, scaleX, scaleY, shader, camera);
		for(int f = 1; f < 60.0f / (1.0f / dt); f++) {anim.update(1);}
		if(destroy == 1 && anim.finished) {destroy = 2;}
	}
	public void destroy() {
		int initWidth = anim.getCurrentSize()[0];
		int initHeight = anim.getCurrentSize()[1];
		
		destroy = 1;
		anim = new Animation(Loader.explosionAnim, new int[]{6, 6, 6, 7}, 0);
		
		int newWidth = anim.getCurrentSize()[0];
		int newHeight = anim.getCurrentSize()[1];
		
		pos.x -= (newWidth - initWidth) / 2;
		pos.y -= (newHeight - initHeight) / 2;
	}
}
