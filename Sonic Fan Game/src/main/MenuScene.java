package main;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.joml.Vector2f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import rendering.Shader;
import rendering.Texture;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class MenuScene extends Scene {
	private Shader defaultShader;
	
	private Image sprite0;
	private Image sprite1;
	
	public MenuScene() {}
	
	public void init() {
		camera = new Camera(new Vector2f());
		
		defaultShader = new Shader("/shaders/default.glsl");
		defaultShader.compile();
		
		sprite0 = new Image(Loader.idleAnim[0]);
		sprite1 = new Image(Loader.runFastestAnim[0]);
	}
	
	public void update(float dt) {
		//System.out.println(camera.position.x + ", " + camera.position.y);
		
		 if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
	            camera.position.x += 100f * dt;
	        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
	            camera.position.x -= 100f * dt;
	        }
	        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
	            camera.position.y -= 100f * dt;
	        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
	            camera.position.y += 100f * dt;
	        }
		
		sprite0.draw(0, 0, 1, 1, defaultShader, camera);
		sprite1.draw(600, 300, -1, 1, defaultShader, camera);
	}
}