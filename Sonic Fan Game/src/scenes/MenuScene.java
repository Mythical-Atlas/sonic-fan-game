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

public class MenuScene extends Scene {
	private final int FADING_OUT 			= -1;
	private final int FADING_IN 			=  0;
	private final int FADING_TITLE 			=  1;
	private final int PRESS_START 			=  2;
	private final int START_BLINK 			=  3;
	private final int START_FADE_FORWARDS 	=  4;
	private final int MENU_1 				=  5;
	private final int MENU_1_BLINK 			=  6;
	private final int MENU_1_FADE_FORWARDS	=  7;
	private final int MENU_1_FADE_BACKWARDS	=  8;
	private final int MENU_2 				=  9;
	private final int MENU_2_BLINK 			= 10;
	private final int MENU_2_FADE_FORWARDS	= 11;
	private final int MENU_2_FADE_BACKWARDS	= 12;
	private final int SETTINGS_FADE_IN		= 13;
	private final int SETTINGS				= 14;
	private final int SETTINGS_FADE_OUT		= 15;
	
	private int state;
	
	private Clip titleMusic;
	private Clip sonicAdvance2;
	private Clip forward;
	private Clip back;
	private Clip move;
	private Clip inaccessible;
	
	private Shader shader;
	
	private Image fade;
	private Image leftCloud;
	private Image rightCloud;
	private Image title;
	private Image start;
	
	private Image singleplayerWhiteSprite;
	private Image singleplayerYellowSprite;
	private Image multiplayerWhiteSprite;
	private Image multiplayerYellowSprite;
	private Image gameStartWhiteSprite;
	private Image gameStartYellowSprite;
	private Image timeAttackWhiteSprite;
	private Image timeAttackYellowSprite;
	private Image optionsWhiteSprite;
	private Image optionsYellowSprite;
	
	private int settingsSelection;
	private int fadeTimer;
	private int blinkTimer;
	private int menuSelection;
	private int oldMovement;
	private int newMovement;
	
	private boolean enterKey;
	private boolean backKey;
	private boolean upKey;
	private boolean downKey;
	private boolean leftKey;
	private boolean rightKey;
	
	private boolean enterReady;
	private boolean backReady;
	private boolean upReady;
	private boolean downReady;
	private boolean leftReady;
	private boolean rightReady;
	
	private Renderer r;
	
	private Font f;
	
	public void init() {
		shader = new Shader("/shaders/spriteBatch.glsl");
		shader.compile();
		
		camera = new Camera(new Vector2f());
		
		titleMusic = Loader.titleScreenMusic;
		
		sonicAdvance2 = Loader.titleSound;
		forward = Loader.forwardSound;
		back =	Loader.backSound;
		move = Loader.moveSound;
		inaccessible = Loader.inaccessibleSound;
		
		singleplayerWhiteSprite  = new Image(Loader.singleplayerWhiteSprite);
		singleplayerYellowSprite = new Image(Loader.singleplayerYellowSprite);
		multiplayerWhiteSprite   = new Image(Loader.multiplayerWhiteSprite);
		multiplayerYellowSprite  = new Image(Loader.multiplayerYellowSprite);
		gameStartWhiteSprite     = new Image(Loader.gameStartWhiteSprite);
		gameStartYellowSprite    = new Image(Loader.gameStartYellowSprite);
		timeAttackWhiteSprite    = new Image(Loader.timeAttackWhiteSprite);
		timeAttackYellowSprite   = new Image(Loader.timeAttackYellowSprite);
		optionsWhiteSprite       = new Image(Loader.optionsWhiteSprite);
		optionsYellowSprite      = new Image(Loader.optionsYellowSprite);
		
		fade = new Image(Loader.fade);
		title = new Image(Loader.title);
		leftCloud = new Image(Loader.leftCloud);
		rightCloud = new Image(Loader.rightCloud);
		start = new Image(Loader.pressStart);
		
		fadeTimer = 120;
		blinkTimer = 0;
		state = 0;
		
		r = new Renderer();
		
		initFont();
	}
		
	public void update(float dt) {
		checkKeys();
		
		camera.position = new Vector2f(0, -(Window.getInitHeight() - Window.getHeight()));
		
		if(state == FADING_IN) {stateFadingIn(dt);}
		if(state == FADING_TITLE) {stateFadingTitle(dt);}
		if(state == PRESS_START) {statePressStart(dt);}
		if(state == START_BLINK) {stateStartBlink(dt);}
		if(state == START_FADE_FORWARDS) {stateStartFadeForwards(dt);}
		if(state == MENU_1) {stateMenu1(dt);}
		if(state == MENU_1_BLINK) {stateMenu1Blink(dt);}
		if(state == MENU_1_FADE_FORWARDS) {stateMenu1FadeForwards(dt);}
		if(state == MENU_1_FADE_BACKWARDS) {stateMenu1FadeBackwards(dt);}
		if(state == MENU_2) {stateMenu2(dt);}
		if(state == MENU_2_BLINK) {stateMenu2Blink(dt);}
		if(state == MENU_2_FADE_FORWARDS) {stateMenu2FadeForwards(dt);}
		if(state == MENU_2_FADE_BACKWARDS) {stateMenu2FadeBackwards(dt);}
		if(state == SETTINGS_FADE_IN) {stateSettingsFadeIn(dt);}
		if(state == SETTINGS) {stateSettings(dt);}
		if(state == SETTINGS_FADE_OUT) {stateSettingsFadeOut(dt);}
		if(state == FADING_OUT) {stateFadingOut(dt);}
		
		r.draw(shader, camera);
		r.reset();
	}
	
	private void stateFadingOut(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		glfwSetWindowShouldClose(Window.getWindow(), true);
	}
	
	private void stateFadingIn(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		fade.setColor(1, 1, 1, 1.0f - getLinearFade(fadeTimer / 60.0f));
		fade.draw(0, 0, screenWidth, screenHeight, r);
		
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {fadeTimer--;}
		if(fadeTimer <= 0) {
			fadeTimer = 120;
			
			titleMusic.stop();
			titleMusic.flush();
			titleMusic.setFramePosition(0);
			titleMusic.start();
			
			state = FADING_TITLE;
		}
	}
	
	private void stateFadingTitle(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.setColor(1, 1, 1, getLinearFade(fadeTimer / 60.0f));
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
			fadeTimer--;
			if(fadeTimer <= 0) {
				fadeTimer = 0;
				
				sonicAdvance2.stop();
				sonicAdvance2.flush();
				sonicAdvance2.setFramePosition(0);
				sonicAdvance2.start();
				
				state = PRESS_START;
				title.setColor(1, 1, 1, 1);
			}
		}
	}
	
	private void statePressStart(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		if(blinkTimer < 30) {start.draw(screenWidth / 2 - start.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - start.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);}
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
			blinkTimer++;
			if(blinkTimer == 60) {blinkTimer = 0;}
		}
		
		if(enterKey && enterReady) {
			state = START_BLINK;
			blinkTimer = 0;
			
			forward.stop();
			forward.flush();
			forward.setFramePosition(0);
			forward.start();
		}
		if(backKey && backReady) {state = FADING_OUT;}
	
		enterReady = !enterKey;
		backReady = !backKey;
	}
	
	private void stateStartBlink(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		if(blinkTimer % 10 < 5) {start.draw(screenWidth / 2 - start.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - start.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);}
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
			blinkTimer++;
			if(blinkTimer == 30) {
				state = START_FADE_FORWARDS;
				oldMovement = 0;
				newMovement = start.getWidth();
				fadeTimer = 0;
			}
		}
	}
	
	private void stateStartFadeForwards(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		start.                   setColor(1, 1, 1, 1.0f - getLinearFade(( newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		singleplayerYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
		multiplayerWhiteSprite.  setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
		
		start.draw(screenWidth / 2 - start.getWidth() / 2 * Loader.scale + oldMovement * 2, screenHeight / 3 * 2 - start.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		singleplayerYellowSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth() / 2 * Loader.scale + newMovement * Loader.scale, screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		multiplayerWhiteSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth() / 2 * Loader.scale + newMovement * Loader.scale, screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
			oldMovement -= 10;
			newMovement -= 10;
		}
		
		if(newMovement <= 0) {
			start.setColor(1, 1, 1, 1);
			singleplayerYellowSprite.setColor(1, 1, 1, 1);
			multiplayerWhiteSprite.setColor(1, 1, 1, 1);
			state = MENU_1;
			menuSelection = 0;
		}
	}
	
	private void stateMenu1(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		if(menuSelection == 0) {
			singleplayerYellowSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			multiplayerWhiteSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		if(menuSelection == 1) {
			singleplayerWhiteSprite.draw(screenWidth / 2 - singleplayerWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - singleplayerWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			multiplayerYellowSprite.draw(screenWidth / 2 - multiplayerYellowSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - multiplayerYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		
		if(enterKey && enterReady) {
			if(menuSelection == 0) {
				state = MENU_1_BLINK;
				blinkTimer = 0;
				
				forward.stop();
				forward.flush();
				forward.setFramePosition(0);
				forward.start();
			}
			else {
				inaccessible.stop();
				inaccessible.flush();
				inaccessible.setFramePosition(0);
				inaccessible.start();
			}
		}
		else if(backKey && backReady) {
			state = MENU_1_FADE_BACKWARDS;
			oldMovement = 0;
			newMovement = -start.getWidth();
			fadeTimer = 0;
				
			back.stop();
			back.flush();
			back.setFramePosition(0);
			back.start();
		}
		else {
			if(upKey && upReady) {
				menuSelection--;
				if(menuSelection < 0) {menuSelection = 1;}
				
				move.stop();
				move.flush();
				move.setFramePosition(0);
				move.start();
			}
			else if(downKey && downReady) {
				menuSelection++;
				if(menuSelection > 1) {menuSelection = 0;}
				
				move.stop();
				move.flush();
				move.setFramePosition(0);
				move.start();
			}
		}
		
		enterReady = !enterKey;
		backReady = !backKey;
		upReady = !upKey;
		downReady = !downKey;
	}
	
	private void stateMenu1Blink(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		if(menuSelection == 0) {multiplayerWhiteSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);}
		if(menuSelection == 1) {singleplayerWhiteSprite.draw(screenWidth / 2 - singleplayerWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - singleplayerWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);}
		
		if(blinkTimer % 10 < 5) {
			if(menuSelection == 0) {singleplayerYellowSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);}
			if(menuSelection == 1) {multiplayerYellowSprite.draw(screenWidth / 2 - multiplayerYellowSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - multiplayerYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);}
		}
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
			blinkTimer++;
			if(blinkTimer == 30) {
				state = MENU_1_FADE_FORWARDS;
				oldMovement = 0;
				newMovement = singleplayerWhiteSprite.getWidth();
				fadeTimer = 0;
			}
		}
	}
	
	private void stateMenu1FadeForwards(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		singleplayerYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		multiplayerYellowSprite. setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		singleplayerWhiteSprite. setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		multiplayerWhiteSprite.  setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		
		gameStartYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
		timeAttackWhiteSprite.setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
		optionsWhiteSprite.   setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
		
		if(menuSelection == 0) {
			singleplayerYellowSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			multiplayerWhiteSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		if(menuSelection == 1) {
			singleplayerWhiteSprite.draw(screenWidth / 2 - singleplayerWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - singleplayerWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			multiplayerYellowSprite.draw(screenWidth / 2 - multiplayerYellowSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - multiplayerYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		
		gameStartYellowSprite.draw(screenWidth / 2 - gameStartYellowSprite.getWidth() / 2 * Loader.scale + newMovement * Loader.scale, screenHeight / 3 * 2 - gameStartYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() / 2 * Loader.scale + newMovement * Loader.scale, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() / 2 * Loader.scale + newMovement * Loader.scale, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
			oldMovement -= 10;
			newMovement -= 10;
		}
		
		if(newMovement <= 0) {
			singleplayerYellowSprite.setColor(1, 1, 1, 1);
			multiplayerYellowSprite.setColor(1, 1, 1, 1);
			singleplayerWhiteSprite.setColor(1, 1, 1, 1);
			multiplayerWhiteSprite.setColor(1, 1, 1, 1);
			
			gameStartYellowSprite.setColor(1, 1, 1, 1);
			timeAttackWhiteSprite.setColor(1, 1, 1, 1);
			optionsWhiteSprite.   setColor(1, 1, 1, 1);
			
			state = MENU_2;
			menuSelection = 0;
		}
	}
	
	private void stateMenu1FadeBackwards(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		start.                   setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
		singleplayerYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		multiplayerYellowSprite. setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		singleplayerWhiteSprite. setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		multiplayerWhiteSprite.  setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		
		start.draw(screenWidth / 2 - start.getWidth() / 2 * Loader.scale + newMovement * Loader.scale, screenHeight / 3 * 2 - start.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		if(menuSelection == 0) {
			singleplayerYellowSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			multiplayerWhiteSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		if(menuSelection == 1) {
			singleplayerWhiteSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			multiplayerYellowSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
			oldMovement += 10;
			newMovement += 10;
		}
		
		if(newMovement >= 0) {
			start.setColor(1, 1, 1, 1);
			singleplayerYellowSprite.setColor(1, 1, 1, 1);
			multiplayerYellowSprite. setColor(1, 1, 1, 1);
			singleplayerWhiteSprite. setColor(1, 1, 1, 1);
			multiplayerWhiteSprite.  setColor(1, 1, 1, 1);
			
			state = PRESS_START;
			blinkTimer = 0;
		}
	}
	
	private void stateMenu2(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		gameStartYellowSprite. setColor(1, 1, 1, 1);
		timeAttackYellowSprite.setColor(1, 1, 1, 1);
		optionsYellowSprite.   setColor(1, 1, 1, 1);
		gameStartWhiteSprite.  setColor(1, 1, 1, 1);
		timeAttackWhiteSprite. setColor(1, 1, 1, 1);
		optionsWhiteSprite.    setColor(1, 1, 1, 1);
		
		if(menuSelection == 0) {
			gameStartYellowSprite.draw(screenWidth / 2 - gameStartYellowSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - gameStartYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		if(menuSelection == 1) {
			gameStartWhiteSprite.draw(screenWidth / 2 - gameStartWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - gameStartWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			timeAttackYellowSprite.draw(screenWidth / 2 - timeAttackYellowSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - timeAttackYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		if(menuSelection == 2) {
			gameStartWhiteSprite.draw(screenWidth / 2 - gameStartWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - gameStartWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			optionsYellowSprite.draw(screenWidth / 2 - optionsYellowSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - optionsYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		
		if(enterKey && enterReady) {
			if(menuSelection == 0) {
				state = MENU_2_BLINK;
				blinkTimer = 0;
				
				forward.stop();
				forward.flush();
				forward.setFramePosition(0);
				forward.start();
			}
			else if(menuSelection == 2) {
				state = MENU_2_BLINK;
				blinkTimer = 0;
				
				forward.stop();
				forward.flush();
				forward.setFramePosition(0);
				forward.start();
			}
			else {
				inaccessible.stop();
				inaccessible.flush();
				inaccessible.setFramePosition(0);
				inaccessible.start();
			}
		}
		else if(backKey && backReady) {
			state = MENU_2_FADE_BACKWARDS;
			oldMovement = 0;
			newMovement = -singleplayerWhiteSprite.getWidth();
			fadeTimer = 0;
				
			back.stop();
			back.flush();
			back.setFramePosition(0);
			back.start();
		}
		else {
			if(upKey && upReady) {
				menuSelection--;
				if(menuSelection < 0) {menuSelection = 2;}
				
				move.stop();
				move.flush();
				move.setFramePosition(0);
				move.start();
			}
			else if(downKey && downReady) {
				menuSelection++;
				if(menuSelection > 2) {menuSelection = 0;}
				
				move.stop();
				move.flush();
				move.setFramePosition(0);
				move.start();
			}
		}
		
		enterReady = !enterKey;
		backReady = !backKey;
		upReady = !upKey;
		downReady = !downKey;
	}
	
	private void stateMenu2Blink(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		if(menuSelection == 0) {
			timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		if(menuSelection == 1) {
			timeAttackYellowSprite.draw(screenWidth / 2 - timeAttackYellowSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - timeAttackYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		if(menuSelection == 2) {
			gameStartWhiteSprite.draw(screenWidth / 2 - gameStartWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - gameStartWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		
		if(blinkTimer % 10 < 5) {
			if(menuSelection == 0) {gameStartYellowSprite.draw(screenWidth / 2 - gameStartYellowSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - gameStartYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);}
			if(menuSelection == 1) {gameStartWhiteSprite.draw(screenWidth / 2 - gameStartWhiteSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - gameStartWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);}
			if(menuSelection == 2) {optionsYellowSprite.draw(screenWidth / 2 - optionsYellowSprite.getWidth() / 2 * Loader.scale, screenHeight / 3 * 2 - optionsYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);}
		}
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {blinkTimer++;}
		if(blinkTimer >= 30) {
			oldMovement = 0;
			newMovement = singleplayerWhiteSprite.getWidth();
			fadeTimer = 0;
			state = MENU_2_FADE_FORWARDS;
		}
	}
	
	private void stateMenu2FadeForwards(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		gameStartWhiteSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		timeAttackWhiteSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		optionsWhiteSprite.   setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		gameStartYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		timeAttackYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		optionsYellowSprite.   setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		
		if(menuSelection == 0) {
			gameStartYellowSprite.draw(screenWidth / 2 - gameStartYellowSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - gameStartYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		if(menuSelection == 1) {
			gameStartWhiteSprite.draw(screenWidth / 2 - gameStartWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - gameStartWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			timeAttackYellowSprite.draw(screenWidth / 2 - timeAttackYellowSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - timeAttackYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		if(menuSelection == 2) {
			gameStartWhiteSprite.draw(screenWidth / 2 - gameStartWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - gameStartWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			optionsYellowSprite.draw(screenWidth / 2 - optionsYellowSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - optionsYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		
		fade.setColor(0, 0, 0, 1.0f - getLinearFade(fadeTimer / 60.0f));
		fade.draw(0, 0, screenWidth, screenHeight, r);
		
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
			oldMovement -= 10;
			newMovement -= 10;
		}
		
		if(newMovement <= 0) {
			oldMovement = -singleplayerWhiteSprite.getWidth();
			newMovement = 0;
		}
		
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
			fadeTimer++;
			if(fadeTimer >= 60) {
				titleMusic.stop();
				sonicAdvance2.stop();
				
				if(menuSelection == 0) {Window.changeScene(1);}
				if(menuSelection == 2) {
					fadeTimer = 120;
					settingsSelection = 0;
					state = SETTINGS_FADE_IN;
				}
			}
		}
	}
	
	private void stateMenu2FadeBackwards(float dt) {
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, r);
		leftCloud.draw(0, screenHeight -  leftCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * Loader.scale, screenHeight - rightCloud.getHeight() * Loader.scale, Loader.scale, Loader.scale, r);
		
		title.draw(screenWidth / 2 - title.getWidth() / 2 * Loader.scale, screenHeight / 3 - title.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		singleplayerYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
		multiplayerWhiteSprite.  setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
		
		gameStartYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		timeAttackYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		optionsYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		gameStartWhiteSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		timeAttackWhiteSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		optionsWhiteSprite.   setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
		
		singleplayerYellowSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth() / 2 * Loader.scale + newMovement * Loader.scale, screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		multiplayerWhiteSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth() / 2 * Loader.scale + newMovement * Loader.scale, screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		
		if(menuSelection == 0) {
			gameStartYellowSprite.draw(screenWidth / 2 - gameStartYellowSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - gameStartYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		if(menuSelection == 1) {
			gameStartWhiteSprite.draw(screenWidth / 2 - gameStartWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - gameStartWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			timeAttackYellowSprite.draw(screenWidth / 2 - timeAttackYellowSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - timeAttackYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		if(menuSelection == 2) {
			gameStartWhiteSprite.draw(screenWidth / 2 - gameStartWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - gameStartWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
			optionsYellowSprite.draw(screenWidth / 2 - optionsYellowSprite.getWidth() / 2 * Loader.scale + oldMovement * Loader.scale, screenHeight / 3 * 2 - optionsYellowSprite.getHeight() / 2 * Loader.scale, Loader.scale, Loader.scale, r);
		}
		
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {
			oldMovement += 10;
			newMovement += 10;
		}
		
		if(newMovement >= 0) {
			singleplayerYellowSprite.setColor(1, 1, 1, 1);
			multiplayerWhiteSprite.  setColor(1, 1, 1, 1);
			
			gameStartYellowSprite. setColor(1, 1, 1, 1);
			timeAttackYellowSprite.setColor(1, 1, 1, 1);
			optionsYellowSprite.   setColor(1, 1, 1, 1);
			gameStartWhiteSprite.  setColor(1, 1, 1, 1);
			timeAttackWhiteSprite. setColor(1, 1, 1, 1);
			optionsWhiteSprite.    setColor(1, 1, 1, 1);
			
			state = MENU_1;
			menuSelection = 0;
		}
	}
	
	private void stateSettingsFadeIn(float dt) {state = SETTINGS;}
	
	private void stateSettings(float dt) {
		f.setColor(255, 255, 255, 255);
		if(settingsSelection == 0) {f.setColor(255, 255, 0, 255);}
		f.draw(50 * Loader.scale, 50 * Loader.scale, Loader.scale, "fps " + Loader.fps, r);
		
		f.setColor(255, 255, 255, 255);
		if(settingsSelection == 1) {f.setColor(255, 255, 0, 255);}
		f.draw(50 * Loader.scale, 62 * Loader.scale, Loader.scale, "scale " + Loader.scale, r);
		
		f.setColor(255, 255, 255, 255);
		if(settingsSelection == 2) {f.setColor(255, 255, 0, 255);}
		f.draw(50 * Loader.scale, 74 * Loader.scale, Loader.scale, "size " + Loader.width + " x " + Loader.height, r);
		
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
			if(settingsSelection < 0) {settingsSelection = 2;}
		}
		else if(downKey && downReady) {
			settingsSelection++;
			if(settingsSelection > 2) {settingsSelection = 0;}
		}
		
		enterReady = !enterKey;
		backReady = !backKey;
		upReady = !upKey;
		downReady = !downKey;
		leftReady = !leftKey;
		rightReady = !rightKey;
	}
	
	private void stateSettingsFadeOut(float dt) {state = FADING_IN;}
	
	private void checkKeys() {
		backKey = KeyListener.isKeyPressed(GLFW_KEY_ESCAPE) || KeyListener.isKeyPressed(GLFW_KEY_BACKSPACE) || KeyListener.isKeyPressed(GLFW_KEY_Z);
		enterKey = KeyListener.isKeyPressed(GLFW_KEY_ENTER) || KeyListener.isKeyPressed(GLFW_KEY_C) ||  KeyListener.isKeyPressed(GLFW_KEY_SPACE);
		upKey = KeyListener.isKeyPressed(GLFW_KEY_UP);
		downKey = KeyListener.isKeyPressed(GLFW_KEY_DOWN);
		leftKey = KeyListener.isKeyPressed(GLFW_KEY_LEFT);
		rightKey = KeyListener.isKeyPressed(GLFW_KEY_RIGHT);
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
}