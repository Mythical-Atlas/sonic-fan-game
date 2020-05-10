package main;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

public class Window {
	private long glfwWindow;
	
	private static Window window = null;
	
	private static Scene currentScene = null;
	
	private Window() {}
	
	public static Window get() {
		if(window == null) {window = new Window();}
		
		return(window);
	}
	
	public static void changeScene(int newScene) {
		switch(newScene) {
			case(0):
				currentScene = new MenuScene();
				currentScene.init();
				break;
			case(1):
				currentScene = new MainScene();
				currentScene.init();
				break;
			default:
				assert(false): "Unknown scene " + newScene + ".";
		}
	}
	
	public void run() {
		init();
		loop();
		
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!glfwInit()) {throw new IllegalStateException("Unable to initialize GLFW.");}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		
		glfwWindow = glfwCreateWindow(Loader.DEFAULT_FRAME_WIDTH, Loader.DEFAULT_FRAME_HEIGHT, Loader.TITLE, NULL, NULL);
		if(glfwWindow == NULL) {throw new IllegalStateException("Failed to create the GLFW window.");}
		
		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		
		glfwMakeContextCurrent(glfwWindow);
		glfwSwapInterval(1);
		
		glfwShowWindow(glfwWindow);
		
		GL.createCapabilities();
		
		changeScene(0);
	}
	
	public void loop() {
		float beginTime = Time.getTime();
		float endTime = Time.getTime();
		float dt = -1.0f;
		
		while(!glfwWindowShouldClose(glfwWindow)) {
			glfwPollEvents();
			
			glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);
			
			if(dt >= 0) {currentScene.update(dt);}
			
			glfwSwapBuffers(glfwWindow);
			
			endTime = Time.getTime();
			dt = endTime - beginTime;
			beginTime = endTime;
			
			System.out.println("" + (1.0f / dt) + " FPS");
		}
	}
}
