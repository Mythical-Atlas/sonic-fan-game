package misc;

import static java.lang.Math.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.joml.Vector2f;

import datatypes.Animation;
import main.Loader;
import main.Window;
import objects.Player;
import rendering.Camera;
import rendering.Image;
import rendering.Shader;
import rendering.SpriteRenderer;

public class HUD {
	public static final int FPS_SAMPLE_SIZE = 10;
	public static final int SCALE = 2;
	
	private Image hud;
	private Image time;
	private Image[] numbers;
	
	private Animation ring;
	
	public int rings;
	
	private long start;
	
	//private Camera camera;
	
	private float[] frames;
	private int numFrames;
	
	private int fps;
	
	public HUD() {
		//camera = new Camera(new Vector2f());
		
		hud = new Image(Loader.hud);
		time = new Image(Loader.time);
		
		numbers = new Image[Loader.numbers.length];
		for(int i = 0; i < numbers.length; i++) {numbers[i] = new Image(Loader.numbers[i]);}
		
		ring = new Animation(Loader.hudRingAnim, new int[]{4, 4, 4, 4, 4, 4, 4, 4}, 0);
		
		start = System.nanoTime();
		
		hud.setPositions(1 * SCALE, 3 * SCALE, SCALE, SCALE);
		
		frames = new float[10];
		numFrames = 0;
		
		fps = 0;
	}
	
	public void draw(float dt, Player p, Shader shader, Camera camera) {
		frames[numFrames] = dt;
		numFrames++;
		if(numFrames == FPS_SAMPLE_SIZE) {
			float temp = 0;
			for(int i = 0; i < numFrames; i++) {temp += frames[i];}
			temp /= (numFrames * 1.0f);
			fps = (int)(1.0f / temp);
			numFrames = 0;
		}
		
		float xOffset = camera.position.x;
		float yOffset = camera.position.y + (Window.getInitHeight() - Window.getHeight());
		
		hud.setPositions(xOffset + 1 * SCALE, yOffset + 3 * SCALE, SCALE, SCALE);
		SpriteRenderer.add(hud);
		
		ring.draw(xOffset + 7 * SCALE, yOffset + 8 * SCALE, SCALE, SCALE, shader, camera);
//		ring.update((p.vel.getLength() / 10 + 1) * 1/*(dt / (1.0f / 60.0f))*/);
//		ring.update((p.vel.getLength() / 10 + 1) * 1/*(dt / (1.0f / 60.0f))*/); // 30fps only
		
		for(int f = 1; f < 60.0f / (1.0f / dt); f++) {ring.update((p.vel.getLength() / 10 + 1));}
		
		drawNumber(28 * SCALE, 3 * SCALE, p.rings, 3, shader, camera);

		long change = System.nanoTime() - start;
		
		int ms = (int)(change / 10000000);
		int s = (int)(ms / 100);
		int m = (int)(s / 60);
		
		time.setPositions(xOffset + Window.getWidth() / 2 - time.getWidth() * SCALE / 2, yOffset + 3 * SCALE, SCALE, SCALE);
		SpriteRenderer.add(time);
		
		drawNumber(Window.getWidth() / 2 - time.getWidth() * SCALE / 2 +  0 * SCALE, 3 * SCALE, m % 10, 1, shader, camera);
		drawNumber(Window.getWidth() / 2 - time.getWidth() * SCALE / 2 + 16 * SCALE, 3 * SCALE, s % 60, 2, shader, camera);
		drawNumber(Window.getWidth() / 2 - time.getWidth() * SCALE / 2 + 40 * SCALE, 3 * SCALE, ms % 100, 2, shader, camera);
		
		drawNumber(Window.getWidth() - numbers[0].getWidth() * SCALE * 2 - 1 * SCALE, 3 * SCALE, fps, 2, shader, camera);
	}
	
	private void drawNumber(int x, int y, int num, int places, Shader shader, Camera camera) {
		float xOffset = camera.position.x;
		float yOffset = camera.position.y + (Window.getInitHeight() - Window.getHeight());
		
		for(int i = 0; i < places; i++) {
			int n = (int)floor(num / pow(10, places - i - 1)) % 10;
			
			numbers[n].setPositions(xOffset + x + i * 8 * SCALE, yOffset + y, SCALE, SCALE);
			SpriteRenderer.add(numbers[n]);
		}
	}
}
