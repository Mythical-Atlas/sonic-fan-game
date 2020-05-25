package scenes;

import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL33.*;
import static java.awt.event.KeyEvent.*;
import static functionholders.CollisionFunctions.*;
import static functionholders.DebugFunctions.*;
import static functionholders.ListFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.nio.ByteBuffer;

import javax.sound.sampled.Clip;

import org.joml.Vector2f;

import badniks.Badnik;
import badniks.Spinner;
import datatypes.Shape;
import datatypes.State;
import datatypes.TiledJSON;
import datatypes.Tilemap;
import datatypes.Vector;
import main.KeyListener;
import main.Loader;
import main.Window;
import misc.Background;
import misc.HUD;
import objects.Item;
import objects.Player;
import objects.Ramp;
import objects.Ring;
import objects.Rotor;
import objects.Spring;
import objects.SpringPole;
import rendering.Camera;
import rendering.Image;
import rendering.Shader;
import rendering.SpriteRenderer;
import shapes.Arc;
import shapes.Circle;
import shapes.InverseArc;
import shapes.Rectangle;
import shapes.Triangle;

public class MenuScene extends Scene {
	private final int SCALE = 2;
	
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
	
	private int fadeTimer;
	private int blinkTimer;
	private int menuSelection;
	private int oldMovement;
	private int newMovement;
	
	private boolean enterKey;
	private boolean backKey;
	private boolean upKey;
	private boolean downKey;
	
	private boolean enterReady;
	private boolean backReady;
	private boolean upReady;
	private boolean downReady;
	
	public void init() {
		SpriteRenderer.reset();
		
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
	}
		
	public void update(float dt) {
		checkKeys();
		
		int screenWidth = Window.getWidth();
		int screenHeight = Window.getHeight();
		camera.position = new Vector2f(0, -(Window.getInitHeight() - Window.getHeight()));
		
		SpriteRenderer.reset();
		
		/*fade.setColors(new float[]{
			              0,  80.0f / 255.0f, 224.0f / 255.0f, 1,
			100.0f / 255.0f, 160.0f / 255.0f, 248.0f / 255.0f, 1,
			100.0f / 255.0f, 208.0f / 255.0f, 248.0f / 255.0f, 1,
			              0,  80.0f / 255.0f, 224.0f / 255.0f, 1
		});
		fade.draw(0, 0, screenWidth, screenHeight, shader, camera);*/
		
		fade.setColor(0, 80.0f / 255.0f, 224.0f / 255.0f, 1);
		fade.draw(0, 0, screenWidth, screenHeight, shader, camera);
		
		 leftCloud.draw(0,                                       screenHeight -  leftCloud.getHeight() * 2, 2, 2, shader, camera);
		rightCloud.draw(screenWidth - rightCloud.getWidth() * 2, screenHeight - rightCloud.getHeight() * 2, 2, 2, shader, camera);
		
		if(state == FADING_IN) {
			fade.setColor(1, 1, 1, 1.0f - getLinearFade(fadeTimer / 60.0f));
			fade.draw(0, 0, screenWidth, screenHeight, shader, camera);
			
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
		else if(state == FADING_TITLE) {
			title.setColor(1, 1, 1, getLinearFade(fadeTimer / 60.0f));
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
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
		else if(state == PRESS_START) {
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
			if(blinkTimer < 30) {start.draw(screenWidth / 2 - start.getWidth(), screenHeight / 3 * 2 - start.getHeight(), 2, 2, shader, camera);}
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
		else if(state == START_BLINK) {
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
			if(blinkTimer % 10 < 5) {start.draw(screenWidth / 2 - start.getWidth(), screenHeight / 3 * 2 - start.getHeight(), 2, 2, shader, camera);}
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
		else if(state == START_FADE_FORWARDS) {
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
			start.                   setColor(1, 1, 1, 1.0f - getLinearFade(( newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			singleplayerYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
			multiplayerWhiteSprite.  setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
			
			start.draw(screenWidth / 2 - start.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - start.getHeight(), 2, 2, shader, camera);
			singleplayerYellowSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth() + newMovement * 2, screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight(), 2, 2, shader, camera);
			multiplayerWhiteSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth() + newMovement * 2, screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight(), 2, 2, shader, camera);
			
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
		else if(state == MENU_1) {
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
			if(menuSelection == 0) {
				singleplayerYellowSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth(), screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight(), 2, 2, shader, camera);
				multiplayerWhiteSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth(), screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight(), 2, 2, shader, camera);
			}
			if(menuSelection == 1) {
				singleplayerWhiteSprite.draw(screenWidth / 2 - singleplayerWhiteSprite.getWidth(), screenHeight / 3 * 2 - singleplayerWhiteSprite.getHeight(), 2, 2, shader, camera);
				multiplayerYellowSprite.draw(screenWidth / 2 - multiplayerYellowSprite.getWidth(), screenHeight / 3 * 2 - multiplayerYellowSprite.getHeight(), 2, 2, shader, camera);
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
		else if(state == MENU_1_BLINK) {
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
			if(menuSelection == 0) {multiplayerWhiteSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth(), screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight(), 2, 2, shader, camera);}
			if(menuSelection == 1) {singleplayerWhiteSprite.draw(screenWidth / 2 - singleplayerWhiteSprite.getWidth(), screenHeight / 3 * 2 - singleplayerWhiteSprite.getHeight(), 2, 2, shader, camera);}
			
			if(blinkTimer % 10 < 5) {
				if(menuSelection == 0) {singleplayerYellowSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth(), screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight(), 2, 2, shader, camera);}
				if(menuSelection == 1) {multiplayerYellowSprite.draw(screenWidth / 2 - multiplayerYellowSprite.getWidth(), screenHeight / 3 * 2 - multiplayerYellowSprite.getHeight(), 2, 2, shader, camera);}
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
		else if(state == MENU_1_FADE_FORWARDS) {
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
			singleplayerYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			multiplayerYellowSprite. setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			singleplayerWhiteSprite. setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			multiplayerWhiteSprite.  setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			
			gameStartYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
			timeAttackWhiteSprite.setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
			optionsWhiteSprite.   setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
			
			if(menuSelection == 0) {
				singleplayerYellowSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight(), 2, 2, shader, camera);
				multiplayerWhiteSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight(), 2, 2, shader, camera);
			}
			if(menuSelection == 1) {
				singleplayerWhiteSprite.draw(screenWidth / 2 - singleplayerWhiteSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - singleplayerWhiteSprite.getHeight(), 2, 2, shader, camera);
				multiplayerYellowSprite.draw(screenWidth / 2 - multiplayerYellowSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - multiplayerYellowSprite.getHeight(), 2, 2, shader, camera);
			}
			
			gameStartYellowSprite.draw(screenWidth / 2 - gameStartYellowSprite.getWidth() + newMovement * 2, screenHeight / 3 * 2 - gameStartYellowSprite.getHeight(), 2, 2, shader, camera);
			timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() + newMovement * 2, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight(), 2, 2, shader, camera);
			optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() + newMovement * 2, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight(), 2, 2, shader, camera);
			
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
		else if(state == MENU_1_FADE_BACKWARDS) {
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
			start.                   setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
			singleplayerYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			multiplayerYellowSprite. setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			singleplayerWhiteSprite. setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			multiplayerWhiteSprite.  setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			
			start.draw(screenWidth / 2 - start.getWidth() + newMovement * 2, screenHeight / 3 * 2 - start.getHeight(), 2, 2, shader, camera);
			
			if(menuSelection == 0) {
				singleplayerYellowSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight(), 2, 2, shader, camera);
				multiplayerWhiteSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight(), 2, 2, shader, camera);
			}
			if(menuSelection == 1) {
				singleplayerWhiteSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight(), 2, 2, shader, camera);
				multiplayerYellowSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight(), 2, 2, shader, camera);
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
		else if(state == MENU_2) {
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
			if(menuSelection == 0) {
				gameStartYellowSprite.draw(screenWidth / 2 - gameStartYellowSprite.getWidth(), screenHeight / 3 * 2 - gameStartYellowSprite.getHeight(), 2, 2, shader, camera);
				timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth(), screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight(), 2, 2, shader, camera);
				optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth(), screenHeight / 3 * 2 - optionsWhiteSprite.getHeight(), 2, 2, shader, camera);
			}
			if(menuSelection == 1) {
				gameStartWhiteSprite.draw(screenWidth / 2 - gameStartWhiteSprite.getWidth(), screenHeight / 3 * 2 - gameStartWhiteSprite.getHeight(), 2, 2, shader, camera);
				timeAttackYellowSprite.draw(screenWidth / 2 - timeAttackYellowSprite.getWidth(), screenHeight / 3 * 2 - timeAttackYellowSprite.getHeight(), 2, 2, shader, camera);
				optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth(), screenHeight / 3 * 2 - optionsWhiteSprite.getHeight(), 2, 2, shader, camera);
			}
			if(menuSelection == 2) {
				gameStartWhiteSprite.draw(screenWidth / 2 - gameStartWhiteSprite.getWidth(), screenHeight / 3 * 2 - gameStartWhiteSprite.getHeight(), 2, 2, shader, camera);
				timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth(), screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight(), 2, 2, shader, camera);
				optionsYellowSprite.draw(screenWidth / 2 - optionsYellowSprite.getWidth(), screenHeight / 3 * 2 - optionsYellowSprite.getHeight(), 2, 2, shader, camera);
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
		else if(state == MENU_2_BLINK) {
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
			if(menuSelection == 0) {
				timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth(), screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight(), 2, 2, shader, camera);
				optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth(), screenHeight / 3 * 2 - optionsWhiteSprite.getHeight(), 2, 2, shader, camera);
			}
			if(menuSelection == 1) {
				gameStartWhiteSprite.draw(screenWidth / 2 - gameStartWhiteSprite.getWidth(), screenHeight / 3 * 2 - gameStartWhiteSprite.getHeight(), 2, 2, shader, camera);
				optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth(), screenHeight / 3 * 2 - optionsWhiteSprite.getHeight(), 2, 2, shader, camera);
			}
			if(menuSelection == 2) {
				gameStartWhiteSprite.draw(screenWidth / 2 - gameStartWhiteSprite.getWidth(), screenHeight / 3 * 2 - gameStartWhiteSprite.getHeight(), 2, 2, shader, camera);
				timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth(), screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight(), 2, 2, shader, camera);
			}
			
			if(blinkTimer % 10 < 5) {
				if(menuSelection == 0) {gameStartYellowSprite.draw(screenWidth / 2 - gameStartYellowSprite.getWidth(), screenHeight / 3 * 2 - gameStartYellowSprite.getHeight(), 2, 2, shader, camera);}
				if(menuSelection == 1) {timeAttackYellowSprite.draw(screenWidth / 2 - timeAttackYellowSprite.getWidth(), screenHeight / 3 * 2 - timeAttackYellowSprite.getHeight(), 2, 2, shader, camera);}
				if(menuSelection == 2) {optionsYellowSprite.draw(screenWidth / 2 - optionsYellowSprite.getWidth(), screenHeight / 3 * 2 - optionsYellowSprite.getHeight(), 2, 2, shader, camera);}
			}
			for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {blinkTimer++;}
			if(blinkTimer >= 30) {
				oldMovement = 0;
				newMovement = singleplayerWhiteSprite.getWidth();
				fadeTimer = 0;
				state = MENU_2_FADE_FORWARDS;
			}
		}
		else if(state == MENU_2_FADE_FORWARDS) {
			gameStartYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			timeAttackWhiteSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			optionsWhiteSprite.   setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
			gameStartYellowSprite.draw(screenWidth / 2 - gameStartYellowSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - gameStartYellowSprite.getHeight(), 2, 2, shader, camera);
			timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight(), 2, 2, shader, camera);
			optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight(), 2, 2, shader, camera);
			
			fade.setColor(0, 0, 0, 1.0f - getLinearFade(fadeTimer / 60.0f));
			fade.draw(0, 0, screenWidth, screenHeight, shader, camera);
			
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
					forward.stop();
					back.stop();
					move.stop();
					
					Window.changeScene(1);
				}
			}
		}
		else if(state == MENU_2_FADE_BACKWARDS) {
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
			singleplayerYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
			multiplayerWhiteSprite.  setColor(1, 1, 1, 1.0f - getLinearFade((oldMovement * 1.0f) / (start.getWidth() * 1.0f)));
			
			gameStartYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			timeAttackYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			optionsYellowSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			gameStartWhiteSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			timeAttackWhiteSprite.setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			optionsWhiteSprite.   setColor(1, 1, 1, 1.0f - getLinearFade((newMovement * 1.0f) / (start.getWidth() * 1.0f)));
			
			singleplayerYellowSprite.draw(screenWidth / 2 - singleplayerYellowSprite.getWidth() + newMovement * 2, screenHeight / 3 * 2 - singleplayerYellowSprite.getHeight(), 2, 2, shader, camera);
			multiplayerWhiteSprite.draw(screenWidth / 2 - multiplayerWhiteSprite.getWidth() + newMovement * 2, screenHeight / 3 * 2 - multiplayerWhiteSprite.getHeight(), 2, 2, shader, camera);
			
			if(menuSelection == 0) {
				gameStartYellowSprite.draw(screenWidth / 2 - gameStartYellowSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - gameStartYellowSprite.getHeight(), 2, 2, shader, camera);
				timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight(), 2, 2, shader, camera);
				optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight(), 2, 2, shader, camera);
			}
			if(menuSelection == 1) {
				gameStartWhiteSprite.draw(screenWidth / 2 - gameStartYellowSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - gameStartYellowSprite.getHeight(), 2, 2, shader, camera);
				timeAttackYellowSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight(), 2, 2, shader, camera);
				optionsWhiteSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight(), 2, 2, shader, camera);
			}
			if(menuSelection == 2) {
				gameStartWhiteSprite.draw(screenWidth / 2 - gameStartYellowSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - gameStartYellowSprite.getHeight(), 2, 2, shader, camera);
				timeAttackWhiteSprite.draw(screenWidth / 2 - timeAttackWhiteSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - timeAttackWhiteSprite.getHeight(), 2, 2, shader, camera);
				optionsYellowSprite.draw(screenWidth / 2 - optionsWhiteSprite.getWidth() + oldMovement * 2, screenHeight / 3 * 2 - optionsWhiteSprite.getHeight(), 2, 2, shader, camera);
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
		else if(state == FADING_OUT) {
			title.draw(screenWidth / 2 - title.getWidth(), screenHeight / 3 - title.getHeight(), 2, 2, shader, camera);
			
			glfwSetWindowShouldClose(Window.getWindow(), true);
		}
		
		SpriteRenderer.draw(shader, camera);
	}
	
	private void checkKeys() {
		backKey = KeyListener.isKeyPressed(GLFW_KEY_ESCAPE) || KeyListener.isKeyPressed(GLFW_KEY_BACKSPACE) || KeyListener.isKeyPressed(GLFW_KEY_Z);
		enterKey = KeyListener.isKeyPressed(GLFW_KEY_ENTER) || KeyListener.isKeyPressed(GLFW_KEY_C) ||  KeyListener.isKeyPressed(GLFW_KEY_SPACE);
		upKey = KeyListener.isKeyPressed(GLFW_KEY_UP);
		downKey = KeyListener.isKeyPressed(GLFW_KEY_DOWN);
	}
	
	private float getLinearFade(float alpha) {
		//double fade = -pow(100, (alpha - 0.99f)) + 1;
		double fade = -pow(2, pow(alpha, 2)) + 2;
		return((float)fade);
	}
}