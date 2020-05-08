package main;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

public class MouseListener {
	private static MouseListener instance;
	
	private double scrollX;
	private double scrollY;
	private double xPos;
	private double yPos;
	private double lastX;
	private double lastY;
	
	private boolean[] mouseButtonPressed = new boolean[3];
	
	private boolean isDragging;
	
	private MouseListener() {
		scrollX = 0;
		scrollY = 0;
		xPos = 0;
		yPos = 0;
		lastX = 0;
		lastY = 0;
	}
	
	public static MouseListener get() {
		if(instance == null) {instance = new MouseListener();}
		
		return(instance);
	}
	
	public static void mousePosCallback(long window, double xpos, double ypos) {
		get().lastX = get().xPos;
		get().lastY = get().yPos;
		get().xPos = xpos;
		get().yPos = ypos;
		get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
	}
	public static void mouseButtonCallback(long window, int button, int action, int mods) {
		if(action == GLFW_PRESS) {if(button < get().mouseButtonPressed.length) {get().mouseButtonPressed[button] = true;}}
		if(action == GLFW_RELEASE) {
			if(button < get().mouseButtonPressed.length) {
				get().mouseButtonPressed[button] = false;
				get().isDragging = false;
			}
		}
	}
	public static void mouseScrollCallback(long window, double xoffset, double yoffset) {
		get().scrollX = xoffset;
		get().scrollY = yoffset;
	}
	
	public static void endFrame() {
		get().scrollX = 0;
		get().scrollY = 0;
		get().lastX = get().xPos;
		get().lastY = get().yPos;
	}
	
	public static float getX() {return((float)get().xPos);}
	public static float getY() {return((float)get().yPos);}
	public static float getDx() {return((float)(get().lastX - get().xPos));}
	public static float getDy() {return((float)(get().lastY - get().yPos));}
	public static float getScrollX() {return((float)get().scrollX);}
	public static float getScrollY() {return((float)get().scrollY);}
	public static boolean isDragging() {return(get().isDragging);}
	public static boolean mouseButtonDown(int button) {
		if(button < get().mouseButtonPressed.length) {return(get().mouseButtonPressed[button]);}
		return(false);
	}
}
