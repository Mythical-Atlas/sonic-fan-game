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
			//if(glfwGetJoystickName(GLFW_JOYSTICK_1).equals("Pro Controller")) {
				get().joystickButtonPressed = glfwGetJoystickButtons(GLFW_JOYSTICK_1);
				
				if(get().keyPressed[key] || key == GLFW_KEY_C      && get().joystickButtonPressed.get(Loader.joyA) == 1) {return(true);}
				if(get().keyPressed[key] || key == GLFW_KEY_X      && get().joystickButtonPressed.get(Loader.joyX) == 1) {return(true);}
				if(get().keyPressed[key] || key == GLFW_KEY_Z      && get().joystickButtonPressed.get(Loader.joyB) == 1) {return(true);}
				
				if(get().keyPressed[key] || key == GLFW_KEY_ESCAPE && get().joystickButtonPressed.get(Loader.joyStart) == 1) {return(true);}
				if(get().keyPressed[key] || key == GLFW_KEY_ENTER  && get().joystickButtonPressed.get(Loader.joyBack) == 1) {return(true);}
				
				if(get().keyPressed[key] || key == GLFW_KEY_UP     && get().joystickButtonPressed.get(Loader.joyUp) == 1) {return(true);}
				if(get().keyPressed[key] || key == GLFW_KEY_DOWN   && get().joystickButtonPressed.get(Loader.joyDown) == 1) {return(true);}
				if(get().keyPressed[key] || key == GLFW_KEY_LEFT   && get().joystickButtonPressed.get(Loader.joyLeft) == 1) {return(true);}
				if(get().keyPressed[key] || key == GLFW_KEY_RIGHT  && get().joystickButtonPressed.get(Loader.joyRight) == 1) {return(true);}
				
				if(key < get().keyPressed.length) {return(get().keyPressed[key]);}
				return(false);
			/*}
			else {
				if(key < get().keyPressed.length) {return(get().keyPressed[key]);}
				return(false);
			}*/
		}
		else {
			if(key < get().keyPressed.length) {return(get().keyPressed[key]);}
			return(false);
		}
	}
	
	public static int getNumJoystickKeysPressed() {
		if(glfwGetJoystickName(GLFW_JOYSTICK_1) != null) {
			get().joystickButtonPressed = glfwGetJoystickButtons(GLFW_JOYSTICK_1);
			
			int out = 0;
			for(int i = 0; i < get().joystickButtonPressed.capacity(); i++) {if(get().joystickButtonPressed.get(i) == 1) {out++;}}
			
			return(out);
		}
		else {return(0);}
	}
	
	public static int getNextJoystickButton() {
		for(int i = 0; i < get().joystickButtonPressed.capacity(); i++) {if(get().joystickButtonPressed.get(i) == 1) {return(i);}}
		return(-1);
	}
}
