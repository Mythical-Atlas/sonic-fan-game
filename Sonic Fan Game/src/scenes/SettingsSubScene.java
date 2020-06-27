package scenes;

import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;

import javax.sound.sampled.Clip;

import org.joml.Vector2f;

import main.KeyListener;
import main.Loader;
import main.Window;
import rendering.Camera;
import rendering.Font;
import rendering.Image;
import rendering.Renderer;
import rendering.Shader;

public class SettingsSubScene extends Scene {
	private final int SETTINGS_FADE_IN		= 0;
	private final int SETTINGS				= 1;
	private final int SETTINGS_FADE_OUT		= 2;
	
	private int state;
	
	private Shader shader;
	private Renderer r;
	private Font f;
	
	private Image fade;
	
	private Clip forward;
	private Clip back;
	private Clip move;
	private Clip inaccessible;
	
	private int settingsSelection;
	
	private int fadeTimer;
	private int blinkTimer;
	private int menuSelection;
	private int oldMovement;
	private int newMovement;
	
	private boolean aKey;
	private boolean enterKey;
	private boolean backKey;
	private boolean upKey;
	private boolean downKey;
	private boolean leftKey;
	private boolean rightKey;
	
	private boolean aReady;
	private boolean enterReady;
	private boolean backReady;
	private boolean upReady;
	private boolean downReady;
	private boolean leftReady;
	private boolean rightReady;
	
	private boolean settingKey;
	private boolean keySetReady;
	
	public boolean exit;
	
	public SettingsSubScene() {
		shader = new Shader("/shaders/spriteBatch.glsl");
		shader.compile();
		
		camera = new Camera(new Vector2f());
		
		forward = Loader.forwardSound;
		back =	Loader.backSound;
		move = Loader.moveSound;
		inaccessible = Loader.inaccessibleSound;
		
		fade = new Image(Loader.fade);
		
		r = new Renderer();
		
		initFont();
	}
	
	public void init() {
		fadeTimer = 120;
		blinkTimer = 0;
		state = 0;
		exit = false;
		settingKey = false;
		keySetReady = false;
	}
	
	public void update(float dt) {
		checkKeys();
		
		camera.position = new Vector2f(0, -(Window.getInitHeight() - Window.getHeight()));
		
		if(state == SETTINGS_FADE_IN) {stateSettingsFadeIn(dt);}
		if(state == SETTINGS) {stateSettings(dt);}
		if(state == SETTINGS_FADE_OUT) {stateSettingsFadeOut(dt);}
		
		r.draw(shader, camera);
		r.reset();
	}
	
	private void stateSettingsFadeIn(float dt) {state = SETTINGS;}
	
	private void stateSettings(float dt) {
		setSelectionColor(0);
		f.draw(6 * Loader.scale, 0 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "fps " + Loader.fps, r);
		
		setSelectionColor(1);
		f.draw(6 * Loader.scale, 1 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "scale " + Loader.scale, r);
		
		setSelectionColor(2);
		f.draw(6 * Loader.scale, 2 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "size " + Loader.width + " x " + Loader.height, r);
		
		int xBase = Window.getWidth() - f.getStringWidth(Loader.scale, "joystick buttons") - 6 * Loader.scale;
		
		f.setColor(255, 255, 255, 255);
		f.draw(xBase, 0 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "joystick buttons", r);
		
		setSelectionColor(3);
		if(Loader.joyUpAxis ==  0) {f.draw(xBase, 1 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "up " + Loader.joyUp, r);}
		if(Loader.joyUpAxis == -1) {f.draw(xBase, 1 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "up " + Loader.joyUp + " neg", r);}
		if(Loader.joyUpAxis ==  1) {f.draw(xBase, 1 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "up " + Loader.joyUp + " pos", r);}
		
		setSelectionColor(4);
		if(Loader.joyDownAxis ==  0) {f.draw(xBase, 2 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "down " + Loader.joyDown, r);}
		if(Loader.joyDownAxis == -1) {f.draw(xBase, 2 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "down " + Loader.joyDown + " neg", r);}
		if(Loader.joyDownAxis ==  1) {f.draw(xBase, 2 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "down " + Loader.joyDown + " pos", r);}
		
		setSelectionColor(5);
		if(Loader.joyLeftAxis ==  0) {f.draw(xBase, 3 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "left " + Loader.joyLeft, r);}
		if(Loader.joyLeftAxis == -1) {f.draw(xBase, 3 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "left " + Loader.joyLeft + " neg", r);}
		if(Loader.joyLeftAxis ==  1) {f.draw(xBase, 3 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "left " + Loader.joyLeft + " pos", r);}
		
		setSelectionColor(6);
		if(Loader.joyRightAxis ==  0) {f.draw(xBase, 4 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "right " + Loader.joyRight, r);}
		if(Loader.joyRightAxis == -1) {f.draw(xBase, 4 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "right " + Loader.joyRight + " neg", r);}
		if(Loader.joyRightAxis ==  1) {f.draw(xBase, 4 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "right " + Loader.joyRight + " pos", r);}
		
		setSelectionColor(7);
		f.draw(xBase, 5 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "a " + Loader.joyA, r);
		
		setSelectionColor(8);
		f.draw(xBase, 6 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "b " + Loader.joyB, r);
		
		setSelectionColor(9);
		f.draw(xBase, 7 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "x " + Loader.joyX, r);
		
		setSelectionColor(10);
		f.draw(xBase, 8 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "start " + Loader.joyStart, r);
		
		setSelectionColor(11);
		f.draw(xBase, 9 * 12 * Loader.scale + 6 * Loader.scale, Loader.scale, "back " + Loader.joyBack, r);
		
		if(!settingKey) {
			if(backKey && backReady) {
				state = SETTINGS_FADE_OUT;
				fadeTimer = 0;
					
				/*back.stop();
				back.flush();
				back.setFramePosition(0);
				back.start();*/
			}
			else if(leftKey && leftReady) {
				if(settingsSelection == 0) {
					if(Loader.fps == 60) {Loader.fps = 30;}
					else if(Loader.fps == 30) {Loader.fps = 15;}
					else if(Loader.fps == 15) {Loader.fps = 60;}
				}
				if(settingsSelection == 1) {
					if(Loader.scale == 4) {Loader.scale = 3;}
					else if(Loader.scale == 3) {Loader.scale = 2;}
					else if(Loader.scale == 2) {Loader.scale = 1;}
					else if(Loader.scale == 1) {Loader.scale = 4;}
				}
				if(settingsSelection == 2) {
				         if(Loader.width > 240 * 6) {Loader.width = 240 * 6;}
					else if(Loader.width > 240 * 5) {Loader.width = 240 * 5;}
					else if(Loader.width > 240 * 4) {Loader.width = 240 * 4;}
					else if(Loader.width > 240 * 3) {Loader.width = 240 * 3;}
					else if(Loader.width > 240 * 2) {Loader.width = 240 * 2;}
					else if(Loader.width > 240 * 1) {Loader.width = 240 * 1;}
					else                            {Loader.width = 240 * 6;}
					     
					     if(Loader.height > 160 * 6) {Loader.height = 160 * 6;}
					else if(Loader.height > 160 * 5) {Loader.height = 160 * 5;}
					else if(Loader.height > 160 * 4) {Loader.height = 160 * 4;}
					else if(Loader.height > 160 * 3) {Loader.height = 160 * 3;}
					else if(Loader.height > 160 * 2) {Loader.height = 160 * 2;}
					else if(Loader.height > 160 * 1) {Loader.height = 160 * 1;}
					else                             {Loader.height = 160 * 6;}
					
					Window.setWidth(Loader.width);
					Window.setHeight(Loader.height);
				}
			}
			else if(rightKey && rightReady) {
				if(settingsSelection == 0) {
					if(Loader.fps == 15) {Loader.fps = 30;}
					else if(Loader.fps == 30) {Loader.fps = 60;}
					else if(Loader.fps == 60) {Loader.fps = 15;}
				}
				if(settingsSelection == 1) {
					if(Loader.scale == 1) {Loader.scale = 2;}
					else if(Loader.scale == 2) {Loader.scale = 3;}
					else if(Loader.scale == 3) {Loader.scale = 4;}
					else if(Loader.scale == 4) {Loader.scale = 1;}
				}
				if(settingsSelection == 2) {
					     if(Loader.width < 240 * 1) {Loader.width = 240 * 1;}
					else if(Loader.width < 240 * 2) {Loader.width = 240 * 2;}
					else if(Loader.width < 240 * 3) {Loader.width = 240 * 3;}
					else if(Loader.width < 240 * 4) {Loader.width = 240 * 4;}
					else if(Loader.width < 240 * 5) {Loader.width = 240 * 5;}
					else if(Loader.width < 240 * 6) {Loader.width = 240 * 6;}
					else                            {Loader.width = 240 * 1;}
					     
					     if(Loader.height < 160 * 1) {Loader.height = 160 * 1;}
					else if(Loader.height < 160 * 2) {Loader.height = 160 * 2;}
					else if(Loader.height < 160 * 3) {Loader.height = 160 * 3;}
					else if(Loader.height < 160 * 4) {Loader.height = 160 * 4;}
					else if(Loader.height < 160 * 5) {Loader.height = 160 * 5;}
					else if(Loader.height < 160 * 6) {Loader.height = 160 * 6;}
					else                             {Loader.height = 160 * 1;}
					     
					Window.setWidth(Loader.width);
					Window.setHeight(Loader.height);
				}
			}
			else if(upKey && upReady) {
				settingsSelection--;
				if(settingsSelection < 0) {settingsSelection = 11;}
			}
			else if(downKey && downReady) {
				settingsSelection++;
				if(settingsSelection > 11) {settingsSelection = 0;}
			}
			else if(enterKey && enterReady || aKey && aReady) {
				if(settingsSelection >= 3 && settingsSelection <= 11) {
					settingKey = true;
					keySetReady = false;
				}
			}
		}
		else {
			if(!keySetReady) {keySetReady = !enterKey && !aKey;}
			else {
				if(settingsSelection ==  3) {
					if(KeyListener.getNextJoystickButton() != -1) {
						Loader.joyUp = KeyListener.getNextJoystickButton();
						Loader.joyUpAxis = 0;
						settingKey = false;
					}
					else {
						if(KeyListener.getNextJoystickAxis() != -1) {
							Loader.joyUp = KeyListener.getNextJoystickAxis();
							Loader.joyUpAxis = KeyListener.getJoystickAxisSign(Loader.joyUp);
							settingKey = false;
						}
					}
				}
				if(settingsSelection ==  4) {
					if(KeyListener.getNextJoystickButton() != -1) {
						Loader.joyDown = KeyListener.getNextJoystickButton();
						Loader.joyDownAxis = 0;
						settingKey = false;
					}
					else {
						if(KeyListener.getNextJoystickAxis() != -1) {
							Loader.joyDown = KeyListener.getNextJoystickAxis();
							Loader.joyDownAxis = KeyListener.getJoystickAxisSign(Loader.joyDown);
							settingKey = false;
						}
					}
				}
				if(settingsSelection ==  5) {
					if(KeyListener.getNextJoystickButton() != -1) {
						Loader.joyLeft = KeyListener.getNextJoystickButton();
						Loader.joyLeftAxis = 0;
						settingKey = false;
					}
					else {
						if(KeyListener.getNextJoystickAxis() != -1) {
							Loader.joyLeft = KeyListener.getNextJoystickAxis();
							Loader.joyLeftAxis = KeyListener.getJoystickAxisSign(Loader.joyLeft);
							settingKey = false;
						}
					}
				}
				if(settingsSelection ==  6) {
					if(KeyListener.getNextJoystickButton() != -1) {
						Loader.joyRight = KeyListener.getNextJoystickButton();
						Loader.joyRightAxis = 0;
						settingKey = false;
					}
					else {
						if(KeyListener.getNextJoystickAxis() != -1) {
							Loader.joyRight = KeyListener.getNextJoystickAxis();
							Loader.joyRightAxis = KeyListener.getJoystickAxisSign(Loader.joyRight);
							settingKey = false;
						}
					}
				}
				if(settingsSelection ==  7) {if(KeyListener.getNextJoystickButton() != -1) {Loader.joyA = KeyListener.getNextJoystickButton(); settingKey = false;}}
				if(settingsSelection ==  8) {if(KeyListener.getNextJoystickButton() != -1) {Loader.joyB = KeyListener.getNextJoystickButton(); settingKey = false;}}
				if(settingsSelection ==  9) {if(KeyListener.getNextJoystickButton() != -1) {Loader.joyX = KeyListener.getNextJoystickButton(); settingKey = false;}}
				if(settingsSelection == 10) {if(KeyListener.getNextJoystickButton() != -1) {Loader.joyStart = KeyListener.getNextJoystickButton(); settingKey = false;}}
				if(settingsSelection == 11) {if(KeyListener.getNextJoystickButton() != -1) {Loader.joyBack = KeyListener.getNextJoystickButton(); settingKey = false;}}
			}
			
			enterReady = false;
			backReady = false;
			upReady = false;
			downReady = false;
			leftReady = false;
			rightReady = false;
			aReady = false;
		}
		
		checkKeys();
		enterReady = !enterKey;
		backReady = !backKey;
		upReady = !upKey;
		downReady = !downKey;
		leftReady = !leftKey;
		rightReady = !rightKey;
		aReady = !aKey;
	}
	
	private void stateSettingsFadeOut(float dt) {exit = true;}
	
	private void checkKeys() {
		backKey = KeyListener.isKeyPressed(GLFW_KEY_ESCAPE) || KeyListener.isKeyPressed(GLFW_KEY_BACKSPACE) || KeyListener.isKeyPressed(GLFW_KEY_Z);
		enterKey = KeyListener.isKeyPressed(GLFW_KEY_ENTER) || KeyListener.isKeyPressed(GLFW_KEY_C) ||  KeyListener.isKeyPressed(GLFW_KEY_SPACE);
		upKey = KeyListener.isKeyPressed(GLFW_KEY_UP);
		downKey = KeyListener.isKeyPressed(GLFW_KEY_DOWN);
		leftKey = KeyListener.isKeyPressed(GLFW_KEY_LEFT);
		rightKey = KeyListener.isKeyPressed(GLFW_KEY_RIGHT);
		aKey = KeyListener.isKeyPressed(GLFW_KEY_C);
	}

	private float getLinearFade(float alpha) {
		//double fade = -pow(100, (alpha - 0.99f)) + 1;
		double fade = -pow(2, pow(alpha, 2)) + 2;
		return((float)fade);
	}

	private void initFont() {
		f = new Font(Loader.font);
		
		f.setSpaceWidth(6);
		
		f.addGlyph(  0, 0, 5, 11, '0');
		f.addGlyph(  6, 0, 3, 11, '1');
		f.addGlyph( 10, 0, 5, 11, '2');
		f.addGlyph( 16, 0, 5, 11, '3');
		f.addGlyph( 22, 0, 6, 11, '4');
		f.addGlyph( 29, 0, 5, 11, '5');
		f.addGlyph( 35, 0, 5, 11, '6');
		f.addGlyph( 41, 0, 5, 11, '7');
		f.addGlyph( 47, 0, 5, 11, '8');
		f.addGlyph( 53, 0, 5, 11, '9');
		
		f.addGlyph( 59, 0, 6, 11, 'a');
		f.addGlyph( 66, 0, 6, 11, 'b');
		f.addGlyph( 73, 0, 6, 11, 'c');
		f.addGlyph( 80, 0, 6, 11, 'd');
		f.addGlyph( 87, 0, 6, 11, 'e');
		f.addGlyph( 94, 0, 6, 11, 'f');
		f.addGlyph(101, 0, 6, 11, 'g');
		f.addGlyph(108, 0, 7, 11, 'h');
		f.addGlyph(116, 0, 2, 11, 'i');
		f.addGlyph(119, 0, 5, 11, 'j');
		f.addGlyph(125, 0, 6, 11, 'k');
		f.addGlyph(132, 0, 6, 11, 'l');
		f.addGlyph(139, 0, 6, 11, 'm');
		f.addGlyph(146, 0, 6, 11, 'n');
		f.addGlyph(153, 0, 6, 11, 'o');
		f.addGlyph(159, 0, 6, 11, 'p');
		f.addGlyph(167, 0, 7, 11, 'q');
		f.addGlyph(175, 0, 6, 11, 'r');
		f.addGlyph(182, 0, 6, 11, 's');
		f.addGlyph(189, 0, 6, 11, 't');
		f.addGlyph(196, 0, 6, 11, 'u');
		f.addGlyph(203, 0, 6, 11, 'v');
		f.addGlyph(210, 0, 6, 11, 'w');
		f.addGlyph(217, 0, 6, 11, 'x');
		f.addGlyph(224, 0, 6, 11, 'y');
		f.addGlyph(231, 0, 6, 11, 'z');
	}
	
	private void setSelectionColor(int selection) {
		if(settingsSelection == selection) {
			if(!settingKey) {f.setColor(255, 255, 0, 255);}
			else {f.setColor(0, 255, 255, 255);}
		}
		else {f.setColor(255, 255, 255, 255);}
	}
}
