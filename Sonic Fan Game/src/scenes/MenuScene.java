package scenes;

import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL33.*;
import static java.awt.event.KeyEvent.*;
import static functionholders.CollisionFunctions.*;
import static functionholders.DebugFunctions.*;
import static functionholders.ListFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.nio.ByteBuffer;

import javax.sound.sampled.Clip;

import org.joml.Vector2f;

import badniks.Badnik;
import badniks.Spinner;
import datatypes.Shape;
import datatypes.State;
import datatypes.TiledJSON;
import datatypes.Tilemap;
import datatypes.Vector;
import main.KeyListener;
import main.Loader;
import main.Window;
import misc.Background;
import misc.HUD;
import objects.Item;
import objects.Player;
import objects.Ramp;
import objects.Ring;
import objects.Rotor;
import objects.Spring;
import objects.SpringPole;
import rendering.Camera;
import rendering.Image;
import rendering.Shader;
import rendering.SpriteRenderer;
import shapes.Arc;
import shapes.Circle;
import shapes.InverseArc;
import shapes.Rectangle;
import shapes.Triangle;

public class MenuScene extends Scene {
	private final int SCALE = 2;
	
	private Clip titleMusic;
	private Clip sonicAdvance2;
	private Clip forward;
	private Clip back;
	private Clip move;
	
	private Shader shader;
	
	private Image fade;
	private Image leftCloud;
	private Image rightCloud;
	private Image title;
	private Image start;
	
	private int fadeTimer;
	private int startTimer;
	
	private boolean starting;
	
	public void init() {
		SpriteRenderer.reset();
		
		shader = new Shader("/shaders/spriteBatch.glsl");
		shader.compile();
		
		camera = new Camera(new Vector2f());
		
		titleMusic = Loader.titleScreenMusic;
		
		sonicAdvance2 = Loader.titleSound;
		forward = Loader.forwardSound;
		back =	Loader.backSound;
		move = Loader.moveSound;
		
		fade = new Image(Loader.fade);
		title = new Image(Loader.title);
		leftCloud = new Image(Loader.leftCloud);
		rightCloud = new Image(Loader.rightCloud);
		start = new Image(Loader.pressStart);
		
		fadeTimer = 60;
		startTimer = 0;
	}
		
	public void update(float dt) {
		checkKeysPressed();
		checkKeysReleased();
		
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		camera.position = new Vector2f(0, -(Window.getInitHeight() - Window.getHeight()));
		
		glClearColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
		
		SpriteRenderer.reset();
		
		 leftCloud.draw(0,                                       screenHeight -  leftCloud.getHeight() * 2, 2, 2, shader, camera);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * 2, screenHeight - rightCloud.getHeight() * 2, 2, 2, shader, camera);
		
		title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
		
		if(!starting && fadeTimer == 0) {
			if(startTimer < 30) {start.draw(screenWidth / 2 - start.getWidth(), screenHeight / 3 * 2 - start.getHeight(), 2, 2, shader, camera);}
			for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
				startTimer++;
				if(startTimer == 60) {startTimer = 0;}
			}
		}
		if(!starting && fadeTimer > 0) {
			float fadeNum = fadeTimer / 60.0f;
			double alpha = -pow(2, fadeNum) + 2;
			fade.setColor(1, 1, 1, (float)(1.0f - alpha));
			fade.draw(0, 0, screenWidth, screenHeight, shader, camera);
			
			for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
				fadeTimer--;
				if(fadeTimer <= 0) {
					fadeTimer = 0;
					
					titleMusic.stop();
					titleMusic.flush();
					titleMusic.setFramePosition(0);
					titleMusic.start();
					
					sonicAdvance2.stop();
					sonicAdvance2.flush();
					sonicAdvance2.setFramePosition(0);
					sonicAdvance2.start();
				}
			}
		}
		if(starting && fadeTimer >= 0) {
			if(startTimer >= 60) {
				float fadeNum = fadeTimer / 60.0f;
				double alpha = -pow(2, fadeNum) + 2;
				fade.setColor(1, 1, 1, (float)(1.0f - alpha));
				fade.draw(0, 0, screenWidth, screenHeight, shader, camera);
				
				for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
					fadeTimer++;
					if(fadeTimer >= 60) {
						titleMusic.stop();
						sonicAdvance2.stop();
						forward.stop();
						back.stop();
						move.stop();
						
						Window.changeScene(1);
					}
				}
			}
			else {
				if(startTimer % 20 < 10) {start.draw(screenWidth / 2 - start.getWidth(), screenHeight / 3 * 2 - start.getHeight(), 2, 2, shader, camera);}
				for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {startTimer++;}
			}
		}
		
		SpriteRenderer.draw(shader, camera);
	}
	
	public void checkKeysPressed() {
		if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {if(fadeTimer == 0 && !starting) {glfwSetWindowShouldClose(Window.getWindow(), true);}}
		if(KeyListener.isKeyPressed(GLFW_KEY_ENTER)) {
			if(fadeTimer == 0 && !starting) {
				starting = true;
				startTimer = 0;
				forward.stop();
				forward.flush();
				forward.setFramePosition(0);
				forward.start();
			}
		}
	}
	public void checkKeysReleased() {
		//if(KeyListener.isKeyPressed(GLFW_KEY_F1)) {toggle0 = true;}
		//if(KeyListener.isKeyPressed(GLFW_KEY_F2)) {toggle1 = true;}
	}
}