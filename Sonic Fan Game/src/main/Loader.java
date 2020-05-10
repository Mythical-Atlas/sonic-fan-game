package main;

import static java.awt.event.KeyEvent.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;

import datatypes.Animation;
import datatypes.State;
import datatypes.TiledJSON;
import datatypes.Tilemap;
import datatypes.Tileset;
import states.MainState;
import states.MenuState;
import states.TestState;

import static functionholders.GraphicsFunctions.*;

@SuppressWarnings("serial")
public class Loader extends JPanel implements MouseListener, KeyListener, Runnable {
	private static final String TITLE = "Sonic Fan Game";
	private static final int TARGET_FPS = 60;
	private static final int SCALE = 1;
	private static final int DEFAULT_FRAME_WIDTH = 1280;
	private static final int DEFAULT_FRAME_HEIGHT = 720;
	private static boolean loadedAssets;
	
	private Thread thread;
	public static JFrame frame;
	private static State currentState;
	public static boolean fullscreen;
	private static int frameX;
	private static int frameY;
	private static int frameWidth;
	private static int frameHeight;
	
	public static int graphicsWidth;
	public static int graphicsHeight;
	
	public static int changeState;
	
	public static int[][] testMap1;
	public static int[][] testMap2;
	
	public static Tilemap leafForest1Map;
	
	public static Animation idleAnim;
	public static Animation runSlowestAnim;
	public static Animation runSlowAnim;
	public static Animation runNormalAnim;
	public static Animation runFastAnim;
	public static Animation runFastestAnim;
	public static Animation fallAnim;
	public static Animation jumpAnim;
	public static Animation skidAnim;
	public static Animation spinAnim;
	public static Animation crouchAnim0;
	public static Animation crouchAnim1;
	public static Animation spindashAnim;
	public static Animation spindashChargeAnim;
	public static Animation spindashDustAnim;
	public static Animation spindashChargeDustAnim;
	public static Animation skirtAnim;
	public static Animation turnAnim;
	
	public static Animation springAnim;
	public static Animation ringAnim;
	public static Animation sparkleAnim;
	
	public static Background leafBG;
	
	public static Animation hudRingAnim;
	public static BufferedImage hud;
	public static BufferedImage time;
	public static BufferedImage[] numbers;
	
	public static Clip jumpSound0;
	public static Clip jumpSound1;
	public static Clip landSound;
	public static Clip skidSound;
	public static Clip spinSound;
	public static Clip spindashChargeSound;
	public static Clip spindashReleaseSound;
	public static Clip stepSound0;
	public static Clip stepSound1;
	public static Clip stepSound2;
	public static Clip stepSound3;
	public static Clip stepSound4;
	
	public static Clip ringSound;
	
	public static void main(String[] args) {
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		
		frameWidth = DEFAULT_FRAME_WIDTH * SCALE;
		frameHeight = DEFAULT_FRAME_HEIGHT * SCALE;
		frameX = center.x - frameWidth / 2;
		frameY =  center.y - frameHeight / 2;
		graphicsWidth = DEFAULT_FRAME_WIDTH;
		graphicsHeight = DEFAULT_FRAME_HEIGHT;
		loadedAssets = false;
		
		resetFrame();
		frame.pack();
		frame.setVisible(true);
		
		changeState = -1;
	}
	
	public static void resetFrame() {
		if(frame != null) {frame.dispose();}
		frame = new JFrame(TITLE);
		
		frame.setBounds(frameX, frameY, frameWidth, frameHeight);
		frame.setContentPane(new Loader());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
	}

	public Loader() {
		setPreferredSize(new Dimension(frameWidth, frameHeight));
		setFocusable(true);
		requestFocus();
		
		if(!loadedAssets) {
			loadedAssets = true;
			
			testMap1 = new TiledJSON("/maps/testMap1.json").map[0];
			testMap2 = new TiledJSON("/maps/testMap2.json").map[0];
			
			leafForest1Map = new Tilemap("/maps/leaf.json", "/maps/");
			
			idleAnim = new Animation("sonicsprites", "idle", new int[]{6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 12, 6, 6, 6, 12, 8}, 0);
			runSlowestAnim = new Animation("sonicsprites", "slowest", new int[]{6, 6, 6, 6, 6, 6, 6, 6}, 0);
			runSlowAnim    = new Animation("sonicsprites", "slow", new int[]{6, 6, 6, 6, 6, 6, 6, 6}, 0);
			runNormalAnim  = new Animation("sonicsprites", "run", new int[]{6, 6, 6, 6, 6, 6, 6, 6}, 0);
			runFastAnim    = new Animation("sonicsprites", "fast", new int[]{6, 6, 6, 6, 6, 6, 6, 6}, 0);
			runFastestAnim = new Animation("sonicsprites", "fastest", new int[]{6, 6, 6, 6, 6, 6, 6, 6}, 0);
			fallAnim = new Animation("sonicsprites", "fall", new int[]{3, 3, 3}, 0);
			jumpAnim = new Animation("sonicsprites", "jump", new int[]{3, 3, 2, 2, 2, 2, 2, 2, 2, 2}, 2);
			skidAnim = new Animation("sonicsprites", "skid", new int[]{2, 4, 4}, 1);
			spinAnim = new Animation("sonicsprites", "spin", new int[]{2, 2, 2, 2}, 0);
			crouchAnim0 = new Animation("sonicsprites", "crouchDown", new int[]{1, 1, 1, 1}, 3);
			crouchAnim1 = new Animation("sonicsprites", "crouchUp", new int[]{1, 3}, 2);
			spindashAnim = new Animation("sonicsprites", "spindash", new int[]{2, 2, 2, 2}, 0);
			spindashChargeAnim = new Animation("sonicsprites", "charge", new int[]{2, 2, 2, 3}, 0);
			spindashDustAnim = new Animation("sonicsprites", "dust", new int[]{2, 2, 2, 2, 2, 2, 2, 2}, 0);
			spindashChargeDustAnim = new Animation("sonicsprites", "chargeDust", new int[]{2, 2, 2, 2, 2, 2, 2, 2}, 0);
			skirtAnim = new Animation("sonicsprites", "skirt", new int[]{2, 2, 2, 4}, 0);
			turnAnim = new Animation("sonicsprites", "turn", new int[]{1, 3}, 0);
			
			springAnim = new Animation("objectsprites", "spring", new int[]{2, 2, 1, 5, 3}, 0, 2);
			ringAnim = new Animation("hudsprites", "ring", new int[]{4, 4, 4, 4, 4, 4, 4, 4}, 0, 2);
			sparkleAnim = new Animation("objectsprites", "effect", new int[]{4, 4, 4, 5}, 0);
			
			jumpSound0 = loadSound("/sonicsounds/jump0.wav", -10.0f);
			jumpSound1 = loadSound("/sonicsounds/jump1.wav", -10.0f);
			landSound = loadSound("/sonicsounds/land.wav", -10.0f);
			skidSound = loadSound("/sonicsounds/skid.wav", -10.0f);
			spinSound = loadSound("/sonicsounds/spin.wav", -10.0f);
			spindashChargeSound = loadSound("/sonicsounds/spindashCharge.wav", -10.0f);
			spindashReleaseSound = loadSound("/sonicsounds/spindashRelease.wav", -10.0f);
			stepSound0 = loadSound("/sonicsounds/step0.wav", -10.0f);
			stepSound1 = loadSound("/sonicsounds/step1.wav", -10.0f);
			stepSound2 = loadSound("/sonicsounds/step2.wav", -10.0f);
			stepSound3 = loadSound("/sonicsounds/step3.wav", -10.0f);
			stepSound4 = loadSound("/sonicsounds/step4.wav", -10.0f);
			
			ringSound = loadSound("/objectSounds/ring.wav", -20.0f);
			
			hudRingAnim = new Animation("hudsprites", "ring", new int[]{4, 4, 4, 4, 4, 4, 4, 4}, 0, HUD.SCALE);
			
			leafBG = new Background(new String[]{"/maps/bg0.png", "/maps/bg1.png", "/maps/bg2.png"}, new int[]{0, 5, 2}, new int[]{5, 10, 13}, 2, 16);
			leafBG.setTween(0, 0, new Color(120, 136, 248));
			leafBG.setTween(0, 1, new Color(128, 160, 248));
			leafBG.setTween(1, 1, 14, 14);
			leafBG.setTween(2, 1, 14, 14);
			
			try {
				hud = scaleImage(ImageIO.read(getClass().getResourceAsStream("/hudsprites/rings.png")), HUD.SCALE);
				time = scaleImage(ImageIO.read(getClass().getResourceAsStream("/hudsprites/time.png")), HUD.SCALE);
				
				numbers = new BufferedImage[10];
				for(int i = 0; i < 10; i++) {numbers[i] = scaleImage(ImageIO.read(getClass().getResourceAsStream("/hudsprites/" + i + ".png")), HUD.SCALE);}
			}
			catch(Exception e) {e.printStackTrace();}
		}
	}

	public void addNotify() {
		super.addNotify();
		
		addKeyListener(this);
		addMouseListener(this);
		
		thread = new Thread(this);
		thread.start();
	}

	@SuppressWarnings("static-access")
	public void run() {
		BufferedImage image = new BufferedImage(graphicsWidth, graphicsHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D)image.getGraphics();
		
		if(currentState == null) {currentState = new MenuState();}
		
		int currentFPS = 0;
		
		while(true) {
			long start = System.nanoTime();
					
			currentState.update();	
			currentState.draw(graphics);
			
			if(changeState == 0) {
				currentState = new MenuState();
				changeState = -1;
			}
			if(changeState == 1) {
				currentState = new TestState(testMap1);
				changeState = -1;
			}
			if(changeState == 2) {
				currentState = new TestState(testMap2);
				changeState = -1;
			}
			if(changeState == 3) {
				currentState = new MainState();
				changeState = -1;
			}
			
			graphics.setColor(Color.WHITE);
			graphics.drawString(currentFPS + " FPS", graphicsWidth - 40, graphicsHeight);
			
			Graphics graphicsTemp = getGraphics();
			
			graphicsTemp.drawImage(image, 0, 0, frameWidth, frameHeight, null);
			graphicsTemp.dispose();
			
			long wait = 1000 / TARGET_FPS - (System.nanoTime() - start) / 1000000;
			if(wait <= 0) {wait = 0;}
			
			try {thread.sleep(wait);}
			catch(Exception e) {e.printStackTrace();}
			
			currentFPS = (int)((1000 / TARGET_FPS) / ((System.nanoTime() - start) / 1000000.0) * TARGET_FPS);
		}
	}

	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		if(key.getKeyCode() == KeyEvent.VK_F12) {
			fullscreen = !fullscreen;
			
			if(fullscreen) {
				frameWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
				frameHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
				graphicsWidth = frameWidth;
				graphicsHeight = frameHeight;
				
				resetFrame();
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
				frame.setUndecorated(true);
				frame.pack();
				frame.setVisible(true);
			}
			else {
				//Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
				
				//frameX = center.x - frameWidth / 2;
				//frameY =  center.y - frameHeight / 2;
				frameWidth = DEFAULT_FRAME_WIDTH * SCALE;
				frameHeight = DEFAULT_FRAME_HEIGHT * SCALE;
				graphicsWidth = DEFAULT_FRAME_WIDTH;
				graphicsHeight = DEFAULT_FRAME_HEIGHT;
				
				resetFrame();
				frame.pack();
				frame.setVisible(true);
			}
		}
		currentState.keyPressed(key.getKeyCode());
	}
	public void keyReleased(KeyEvent key) {currentState.keyReleased(key.getKeyCode());}
	
	public void mouseClicked(MouseEvent mouse) {currentState.mouseClicked(mouse);}
	public void mouseEntered(MouseEvent mouse) {
		try {currentState.mouseEntered(mouse);}
		catch(Exception e) {}
	}
	public void mouseExited(MouseEvent mouse) {
		try {currentState.mouseExited(mouse);}
		catch(Exception e) {}
	}
	public void mousePressed(MouseEvent mouse) {currentState.mousePressed(mouse);}
	public void mouseReleased(MouseEvent mouse) {currentState.mouseReleased(mouse);}
	
	private Clip loadSound(String path, float amp) {
		Clip sound = null;
		try {
			sound = AudioSystem.getClip();
			sound.open(AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getResourceAsStream(path))));
		}
		catch (Exception e) {e.printStackTrace();}
		FloatControl gainControl = (FloatControl)sound.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(amp);
		
		return(sound);
	}
}