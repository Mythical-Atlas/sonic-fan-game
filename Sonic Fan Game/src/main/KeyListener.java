package main;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

public class KeyListener {
	private static KeyListener instance;
	
	private boolean keyPressed[] = new boolean[350];
	private ByteBuffer joystickButtonPressed;
	
	private KeyListener() {}
	
	public static KeyListener get() {
		if(instance == null) {instance = new KeyListener();}
		
		return(instance);
	}
	
	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if(action == GLFW_PRESS) {get().keyPressed[key] = true;}
		if(action == GLFW_RELEASE) {get().keyPressed[key] = false;}
		
		/*backKey = KeyListener.isKeyPressed(GLFW_KEY_ESCAPE) || KeyListener.isKeyPressed(GLFW_KEY_BACKSPACE) || KeyListener.isKeyPressed(GLFW_KEY_Z);
		enterKey = KeyListener.isKeyPressed(GLFW_KEY_ENTER) || KeyListener.isKeyPressed(GLFW_KEY_C) ||  KeyListener.isKeyPressed(GLFW_KEY_SPACE);
		upKey = KeyListener.isKeyPressed(GLFW_KEY_UP);
		downKey = KeyListener.isKeyPressed(GLFW_KEY_DOWN);
		
		if(key == GLFW_KEY_ESCAPE*/
	}
	
	public static boolean isKeyPressed(int key) {
		if(glfwGetJoystickName(GLFW_JOYSTICK_1) != null) {
			if(glfwGetJoystickName(GLFW_JOYSTICK_1).equals("Pro Controller")) {
				get().joystickButtonPressed = glfwGetJoystickButtons(GLFW_JOYSTICK_1);
				
				if(key == GLFW_KEY_C && get().joystickButtonPressed.get(1) == 1) {return(true);}
				if(key == GLFW_KEY_X && get().joystickButtonPressed.get(3) == 1) {return(true);}
				if(key == GLFW_KEY_Z && get().joystickButtonPressed.get(0) == 1) {return(true);}
				
				if(key == GLFW_KEY_ESCAPE && get().joystickButtonPressed.get(8) == 1) {return(true);}
				if(key == GLFW_KEY_ENTER && get().joystickButtonPressed.get(9) == 1) {return(true);}
				if(key == GLFW_KEY_BACKSPACE && get().joystickButtonPressed.get(13) == 1) {return(true);}
				
				if(key == GLFW_KEY_UP && get().joystickButtonPressed.get(16) == 1) {return(true);}
				if(key == GLFW_KEY_DOWN && get().joystickButtonPressed.get(18) == 1) {return(true);}
				if(key == GLFW_KEY_LEFT && get().joystickButtonPressed.get(19) == 1) {return(true);}
				if(key == GLFW_KEY_RIGHT && get().joystickButtonPressed.get(17) == 1) {return(true);}
				
				if(key < get().keyPressed.length) {return(get().keyPressed[key]);}
				return(false);
			}
			else {
				if(key < get().keyPressed.length) {return(get().keyPressed[key]);}
				return(false);
			}
		}
		else {
			if(key < get().keyPressed.length) {return(get().keyPressed[key]);}
			return(false);
		}
	}
}
