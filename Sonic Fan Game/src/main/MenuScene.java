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
	
	private TestSprite sprite0;
	private TestSprite sprite1;
	
	public MenuScene() {}
	
	public void init() {
		camera = new Camera(new Vector2f());
		
		defaultShader = new Shader("/shaders/default.glsl");
		defaultShader.compile();
		
		sprite0 = new TestSprite("/sonicsprites/idle0.png", 0, 0);
		sprite1 = new TestSprite("/sonicsprites/fastest0.png", 0, 300);
	}
	
	public void update(float dt) {
		 if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
	            camera.position.x += 100f * dt;
	        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
	            camera.position.x -= 100f * dt;
	        }
	        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
	            camera.position.y += 100f * dt;
	        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
	            camera.position.y -= 100f * dt;
	        }
		
		sprite0.draw(defaultShader, camera);
		sprite1.draw(defaultShader, camera);
	}
}