package main;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

public class KeyListener {
	private static KeyListener instance;
	
	private boolean keyPressed[] = new boolean[350];
	private ByteBuffer joystickButtonsPressed;
	private FloatBuffer joystickAxes;
	
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
				get().joystickButtonsPressed = glfwGetJoystickButtons(GLFW_JOYSTICK_1);
				get().joystickAxes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
				
				try {if(key == GLFW_KEY_C      && get().joystickButtonsPressed.get(Loader.joyA)     == 1) {return(true);}} catch(Exception e) {return(get().keyPressed[key]);}
				try {if(key == GLFW_KEY_X      && get().joystickButtonsPressed.get(Loader.joyX)     == 1) {return(true);}} catch(Exception e) {return(get().keyPressed[key]);}
				try {if(key == GLFW_KEY_Z      && get().joystickButtonsPressed.get(Loader.joyB)     == 1) {return(true);}} catch(Exception e) {return(get().keyPressed[key]);}
					
				try {if(key == GLFW_KEY_ESCAPE && get().joystickButtonsPressed.get(Loader.joyStart) == 1) {return(true);}} catch(Exception e) {return(get().keyPressed[key]);}
				try {if(key == GLFW_KEY_ENTER  && get().joystickButtonsPressed.get(Loader.joyBack)  == 1) {return(true);}} catch(Exception e) {return(get().keyPressed[key]);}
				
				if(key == GLFW_KEY_UP) {
					try {
						if(Loader.joyUpAxis == 0) {if(get().joystickButtonsPressed.get(Loader.joyUp) == 1) {return(true);}}
						if(Loader.joyUpAxis == 1) {if(get().joystickAxes.get(Loader.joyUp) > 0.1) {return(true);}}
						if(Loader.joyUpAxis == -1) {if(get().joystickAxes.get(Loader.joyUp) < -0.1) {return(true);}}
					}
					catch(Exception e) {return(get().keyPressed[key]);}
				}
				if(key == GLFW_KEY_DOWN) {
					try {
						if(Loader.joyDownAxis == 0) {if(get().joystickButtonsPressed.get(Loader.joyDown) == 1) {return(true);}}
						if(Loader.joyDownAxis == 1) {if(get().joystickAxes.get(Loader.joyDown) > 0.1) {return(true);}}
						if(Loader.joyDownAxis == -1) {if(get().joystickAxes.get(Loader.joyDown) < -0.1) {return(true);}}
					}
					catch(Exception e) {return(get().keyPressed[key]);}
				}
				if(key == GLFW_KEY_LEFT) {
					try {
						if(Loader.joyLeftAxis == 0) {if(get().joystickButtonsPressed.get(Loader.joyLeft) == 1) {return(true);}}
						if(Loader.joyLeftAxis == 1) {if(get().joystickAxes.get(Loader.joyLeft) > 0.1) {return(true);}}
						if(Loader.joyLeftAxis == -1) {if(get().joystickAxes.get(Loader.joyLeft) < -0.1) {return(true);}}
					}
					catch(Exception e) {return(get().keyPressed[key]);}
				}
				if(key == GLFW_KEY_RIGHT) {
					try {
						if(Loader.joyRightAxis == 0) {if(get().joystickButtonsPressed.get(Loader.joyRight) == 1) {return(true);}}
						if(Loader.joyRightAxis == 1) {if(get().joystickAxes.get(Loader.joyRight) > 0.1) {return(true);}}
						if(Loader.joyRightAxis == -1) {if(get().joystickAxes.get(Loader.joyRight) < -0.1) {return(true);}}
					}
					catch(Exception e) {return(get().keyPressed[key]);}
				}
				
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
			get().joystickButtonsPressed = glfwGetJoystickButtons(GLFW_JOYSTICK_1);
			
			int out = 0;
			for(int i = 0; i < get().joystickButtonsPressed.capacity(); i++) {if(get().joystickButtonsPressed.get(i) == 1) {out++;}}
			
			return(out);
		}
		else {return(0);}
	}
	
	public static int getNextJoystickButton() {
		for(int i = 0; i < get().joystickButtonsPressed.capacity(); i++) {if(get().joystickButtonsPressed.get(i) == 1) {return(i);}}
		return(-1);
	}
	
	public static int getNextJoystickAxis() {
		for(int i = 0; i < get().joystickAxes.capacity(); i++) {if(get().joystickAxes.get(i) > 0.1 || get().joystickAxes.get(i) < -0.1) {return(i);}}
		return(-1);
	}
	
	public static int getJoystickAxisSign(int axis) {
		if(get().joystickAxes.get(axis) > 0.1) {return(1);}
		if(get().joystickAxes.get(axis) < -0.1) {return(-1);}
		return(0);
	}
}
