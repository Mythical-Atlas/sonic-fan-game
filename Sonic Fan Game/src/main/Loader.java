package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import org.lwjgl.BufferUtils;

import datatypes.Animation;
import datatypes.TiledJSON;
import datatypes.Tilemap;
import misc.Background;
import misc.HUD;
import rendering.Image;
import rendering.Shader;
import scenes.Scene;

import static functionholders.GraphicsFunctions.*;

public class Loader {
	private static Loader singleton = null;
	
	public static final String TITLE = "Sonic Advance 2 DX";
	public static final int TARGET_FPS = 60;
	public static final int DEFAULT_FRAME_WIDTH = 240 * 4;
	public static final int DEFAULT_FRAME_HEIGHT = 160 * 4;
	public static boolean loadedAssets;
	
	public static Scene currentScene;
	public static boolean fullscreen;
	
	public static int graphicsWidth;
	public static int graphicsHeight;
	
	public static int[][] testMap1;
	public static int[][] testMap2;
	
	public static ByteBuffer[] idleAnim;
	public static ByteBuffer[] runSlowestAnim;
	public static ByteBuffer[] runSlowAnim;
	public static ByteBuffer[] runNormalAnim;
	public static ByteBuffer[] runFastAnim;
	public static ByteBuffer[] runFastestAnim;
	public static ByteBuffer[] bounceUpAnim;
	public static ByteBuffer[] bounceDownAnim;
	public static ByteBuffer[] fallAnim;
	public static ByteBuffer[] jumpAnim;
	public static ByteBuffer[] skidAnim;
	public static ByteBuffer[] spinAnim;
	public static ByteBuffer[] crouchAnim0;
	public static ByteBuffer[] crouchAnim1;
	public static ByteBuffer[] spindashAnim;
	public static ByteBuffer[] spindashChargeAnim;
	public static ByteBuffer[] spindashDustAnim;
	public static ByteBuffer[] spindashChargeDustAnim;
	public static ByteBuffer[] skirtAnim;
	public static ByteBuffer[] turnAnim;
	public static ByteBuffer[] landAnim;
	public static ByteBuffer[] startAnim;
	public static ByteBuffer[] trickRightAnim;
	public static ByteBuffer[] trickUp0Anim;
	public static ByteBuffer[] trickUp1Anim;
	public static ByteBuffer[] rampAnim;
	public static ByteBuffer[] sonicRotorAnim;
	public static ByteBuffer[] dashAnim;
	
	public static ByteBuffer[] springAnim;
	public static ByteBuffer[] ringAnim;
	public static ByteBuffer[] sparkleAnim;
	public static ByteBuffer[] rotorAnim;
	public static ByteBuffer[] springPoleFastAnim;
	public static ByteBuffer[] springPoleSlowAnim;
	
	public static ByteBuffer leafBG0;
	public static ByteBuffer leafBG1;
	public static ByteBuffer leafBG2;
	
	public static ByteBuffer windowIcon2;
	public static ByteBuffer windowIcon3;
	
	public static ByteBuffer start0;
	public static ByteBuffer start1;
	public static ByteBuffer start2;
	public static ByteBuffer start3;
	
	public static ByteBuffer pause1;
	public static ByteBuffer pause2;
	public static ByteBuffer pause3;
	
	public static ByteBuffer singleplayerWhiteSprite;
	public static ByteBuffer singleplayerYellowSprite;
	public static ByteBuffer multiplayerWhiteSprite;
	public static ByteBuffer multiplayerYellowSprite;
	public static ByteBuffer gameStartWhiteSprite;
	public static ByteBuffer gameStartYellowSprite;
	public static ByteBuffer timeAttackWhiteSprite;
	public static ByteBuffer timeAttackYellowSprite;
	public static ByteBuffer optionsWhiteSprite;
	public static ByteBuffer optionsYellowSprite;
	
	public static ByteBuffer leafLayer1;
	public static ByteBuffer leafLayer2;
	
	public static ByteBuffer[] hudRingAnim;
	public static ByteBuffer hud;
	public static ByteBuffer time;
	public static ByteBuffer[] numbers;
	public static ByteBuffer[] redNumbers;
	
	public static ByteBuffer itemBox;
	public static ByteBuffer ramp;
	
	public static ByteBuffer fade;
	public static ByteBuffer title;
	public static ByteBuffer leftCloud;
	public static ByteBuffer rightCloud;
	public static ByteBuffer pressStart;
	
	public static ByteBuffer[] spinnerAnim;
	public static ByteBuffer[] explosionAnim;
	
	public static Image testBuffer;
	
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
	public static Clip trickSound;
	public static Clip boostSound;
	public static Clip dashSound;
	
	public static Clip voice3;
	public static Clip voice2;
	public static Clip voice1;
	public static Clip voiceGo;
	
	public static Clip ringSound;
	public static Clip springSound;
	public static Clip popSound;
	public static Clip springPoleSound;
	
	public static Clip leaf1Music;
	public static Clip titleScreenMusic;
	
	public static Clip titleSound;
	public static Clip forwardSound;
	public static Clip backSound;
	public static Clip moveSound;
	public static Clip inaccessibleSound;
	
	public static int debugMode;
	
	public static void main(String[] args) {
		if(args.length > 0) {
			if(args[0].equals("debug")) {
				if(args[1].equals("leaf1")) {debugMode = 1;}
			}
		}
		
		get().init();
		Window.get().run();
	}

	public static Loader get() {
		if(singleton == null) {singleton = new Loader();}
		return(singleton);
	}
	
	private Loader() {}
	
	private void init() {
		if(!loadedAssets) {
			loadedAssets = true;
			
			fade = loadImage("/objectsprites/fade.png");
			title = loadImage("/objectsprites/title.png");
			leftCloud = loadImage("/objectsprites/cloudLeft.png");
			rightCloud = loadImage("/objectsprites/cloudRight.png");
			pressStart = loadImage("/objectsprites/start.png");
			
			testMap1 = new TiledJSON("/maps/testMap1.json").map[0];
			testMap2 = new TiledJSON("/maps/testMap2.json").map[0];
			
			idleAnim = loadImages("/sonicsprites", "idle");
			runSlowestAnim = loadImages("/sonicsprites", "slowest");
			runSlowAnim    = loadImages("/sonicsprites", "slow");
			runNormalAnim  = loadImages("/sonicsprites", "run");
			runFastAnim    = loadImages("/sonicsprites", "fast");
			runFastestAnim = loadImages("/sonicsprites", "fastest");
			bounceUpAnim = loadImages("/sonicsprites", "bounceUp");
			bounceDownAnim = loadImages("/sonicsprites", "bounceDown");
			fallAnim = loadImages("/sonicsprites", "fall");
			jumpAnim = loadImages("/sonicsprites", "jump");
			skidAnim = loadImages("/sonicsprites", "skid");
			spinAnim = loadImages("/sonicsprites", "spin");
			crouchAnim0 = loadImages("/sonicsprites", "crouchDown");
			crouchAnim1 = loadImages("/sonicsprites", "crouchUp");
			spindashAnim = loadImages("/sonicsprites", "spindash");
			spindashChargeAnim = loadImages("/sonicsprites", "charge");
			spindashDustAnim = loadImages("/sonicsprites", "dust");
			spindashChargeDustAnim = loadImages("/sonicsprites", "chargeDust");
			skirtAnim = loadImages("/sonicsprites", "skirt");
			turnAnim = loadImages("/sonicsprites", "turn");
			landAnim = loadImages("/sonicsprites", "land");
			startAnim = loadImages("/sonicsprites", "start");
			trickRightAnim = loadImages("/sonicsprites", "whirl");
			trickUp0Anim = loadImages("/sonicsprites", "trickUp0");
			trickUp1Anim = loadImages("/sonicsprites", "trickUp1");
			rampAnim = loadImages("/sonicsprites", "ramp");
			sonicRotorAnim = loadImages("/sonicsprites", "sonicRotor");
			dashAnim = loadImages("/sonicsprites", "dash");
			
			itemBox = loadImage("/objectsprites/itemBox.png");
			ramp = loadImage("/objectsprites/ramp.png");
			springAnim = loadImages("/objectsprites", "spring");
			ringAnim = loadImages("/hudsprites", "ring");
			sparkleAnim = loadImages("/objectsprites", "effect");
			rotorAnim = loadImages("/objectsprites", "rotor");
			springPoleFastAnim = loadImages("/objectsprites", "springPole");
			springPoleSlowAnim = loadImages("/objectsprites", "springPoleSlow");
			
			jumpSound0 = loadSound("/sonicsounds/jump0.wav", -15.0f);
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
			trickSound = loadSound("/sonicsounds/trick.wav", -10.0f);
			boostSound = loadSound("/sonicsounds/boost.wav", -10.0f);
			dashSound = loadSound("/sonicsounds/dash.wav", -10.0f);
			
			voice3 = loadSound("/voiceclips/3.wav", -10.0f);
			voice2 = loadSound("/voiceclips/2.wav", -10.0f);
			voice1 = loadSound("/voiceclips/1.wav", -10.0f);
			voiceGo = loadSound("/voiceclips/go.wav", -10.0f);
			
			titleScreenMusic = loadSound("/objectsounds/titleScreen.wav", -20.0f);
			leaf1Music = loadSound("/maps/leaf1.wav", -20.0f);
			
			titleSound = loadSound("/objectsounds/title.wav", -10.0f);
			forwardSound = loadSound("/objectsounds/forward.wav", -10.0f);
			backSound = loadSound("/objectsounds/back.wav", -10.0f);
			moveSound = loadSound("/objectsounds/move.wav", -10.0f);
			inaccessibleSound = loadSound("/objectsounds/inaccessible.wav", -10.0f);
			
			ringSound = loadSound("/objectsounds/ring.wav", -10.0f);
			springSound = loadSound("/objectsounds/spring.wav", -10.0f);
			popSound = loadSound("/objectsounds/pop.wav", -10.0f);
			springPoleSound = loadSound("/objectsounds/springPole.wav", -10.0f);
			
			hudRingAnim = loadImages("/hudsprites", "ring");
			
			leafBG0 = loadImage("/maps/bg20.png");
			leafBG1 = loadImage("/maps/bg21.png");
			leafBG2 = loadImage("/maps/bg22.png");
			
			singleplayerWhiteSprite =  loadImage("/menusprites/singleplayerWhite.png");
			singleplayerYellowSprite = loadImage("/menusprites/singleplayerYellow.png");
			multiplayerWhiteSprite =   loadImage("/menusprites/multiplayerWhite.png");
			multiplayerYellowSprite =  loadImage("/menusprites/multiplayerYellow.png");
			gameStartWhiteSprite =     loadImage("/menusprites/gameStartWhite.png");
			gameStartYellowSprite =    loadImage("/menusprites/gameStartYellow.png");
			timeAttackWhiteSprite =    loadImage("/menusprites/timeAttackWhite.png");
			timeAttackYellowSprite =   loadImage("/menusprites/timeAttackYellow.png");
			optionsWhiteSprite =       loadImage("/menusprites/optionsWhite.png");
			optionsYellowSprite =      loadImage("/menusprites/optionsYellow.png");
			
			windowIcon2 = loadImage("/objectsprites/windowIcon2.png");
			windowIcon3 = loadImage("/objectsprites/windowIcon3.png");
			
			start0 = loadImage("/hudsprites/start.png");
			start1 = loadImage("/hudsprites/_1.png");
			start2 = loadImage("/hudsprites/_2.png");
			start3 = loadImage("/hudsprites/_3.png");
			
			pause1 = loadImage("/hudsprites/pause1.png");
			pause2 = loadImage("/hudsprites/pause2.png");
			pause3 = loadImage("/hudsprites/pause3.png");
			
			leafLayer1 = loadImage("/maps/Leaf_Forest_Act_1.png");
			leafLayer2 = loadImage("/maps/Leaf_Forest_Act_1.png");
			
			hud = loadImage("/hudsprites/rings.png");
			time = loadImage("/hudsprites/time.png");
				
			numbers = loadImages("/hudsprites", "");
			redNumbers = loadImages("/hudsprites", "red");
			
			spinnerAnim = loadImages("/badniksprites", "spinner");
			explosionAnim = loadImages("/objectsprites", "explosion");
		}
	}
	
	private ByteBuffer[] loadImages(String dir, String name) {
		int length = 0;
		
		while(true) {
			InputStream is = getClass().getResourceAsStream(dir + "/" + name + length + ".png");
			if(is == null) {break;}
			
			length++;
		}
		
		ByteBuffer[] images = new ByteBuffer[length];
		
		try {
			for(int i = 0; i < length; i++) {
				InputStream is = getClass().getResourceAsStream(dir + "/" + name + i + ".png");
				byte[] bytes = is.readAllBytes();
				
				ByteBuffer imageBuffer = BufferUtils.createByteBuffer(bytes.length);
				imageBuffer.put(bytes);
				imageBuffer.flip();
				
				images[i] = imageBuffer;
			}
		}
		catch(Exception e) {e.printStackTrace();}
		
		return(images);
	}
	public ByteBuffer loadImage(String path) {
		ByteBuffer out = null;
		
		try {
			InputStream is = getClass().getResourceAsStream(path);
			byte[] bytes = is.readAllBytes();
			
			ByteBuffer imageBuffer = BufferUtils.createByteBuffer(bytes.length);
			imageBuffer.put(bytes);
			imageBuffer.flip();
			
			out = imageBuffer;
		}
		catch(Exception e) {e.printStackTrace();}
		
		return(out);
	}
	
	private Clip loadSound(String path, float amp) {
		Clip sound = null;
		try {
			InputStream is = getClass().getResourceAsStream(path);
			BufferedInputStream bis = new BufferedInputStream(is);
			AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
			
			sound = AudioSystem.getClip();
			sound.open(ais);
		}
		catch (Exception e) {
			System.err.println("Faied to load sound '" + path + "'");
			e.printStackTrace();
		}
		FloatControl gainControl = (FloatControl)sound.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(amp);
		
		return(sound);
	}
}