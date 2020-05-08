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

public class KeyListener {
	private static KeyListener instance;
	
	public boolean keyPressed[] = new boolean[350];
	
	private KeyListener() {}
	
	public static KeyListener get() {
		if(instance == null) {instance = new KeyListener();}
		
		return(instance);
	}
	
	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if(action == GLFW_PRESS) {get().keyPressed[key] = true;}
		if(action == GLFW_RELEASE) {get().keyPressed[key] = false;}
	}
	
	public static boolean isKeyPressed(int key) {
		if(key < get().keyPressed.length) {return(get().keyPressed[key]);}
		return(false);
	}
}
