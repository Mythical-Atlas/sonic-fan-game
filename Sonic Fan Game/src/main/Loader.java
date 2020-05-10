package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import org.lwjgl.BufferUtils;

import datatypes.Animation;
import datatypes.TiledJSON;
import datatypes.Tilemap;
import misc.Background;
import misc.HUD;

import static functionholders.GraphicsFunctions.*;

public class Loader {
	public static final String TITLE = "Sonic Fan Game";
	public static final int TARGET_FPS = 60;
	public static final int SCALE = 1;
	public static final int DEFAULT_FRAME_WIDTH = 1280;
	public static final int DEFAULT_FRAME_HEIGHT = 720;
	public static boolean loadedAssets;
	
	public static Scene currentScene;
	public static boolean fullscreen;
	
	public static int graphicsWidth;
	public static int graphicsHeight;
	
	public static int[][] testMap1;
	public static int[][] testMap2;
	
	public static Tilemap leafForest1Map;
	
	public static ByteBuffer[] idleAnim;
	public static ByteBuffer[] runSlowestAnim;
	public static ByteBuffer[] runSlowAnim;
	public static ByteBuffer[] runNormalAnim;
	public static ByteBuffer[] runFastAnim;
	public static ByteBuffer[] runFastestAnim;
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
	
	public static ByteBuffer[] springAnim;
	public static ByteBuffer[] ringAnim;
	public static ByteBuffer[] sparkleAnim;
	
	public static ByteBuffer leafBG;
	
	public static ByteBuffer[] hudRingAnim;
	public static ByteBuffer hud;
	public static ByteBuffer time;
	public static ByteBuffer[] numbers;
	
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
		new Loader();
		Window.get().run();
	}

	public Loader() {
		if(!loadedAssets) {
			loadedAssets = true;
			
			testMap1 = new TiledJSON("/maps/testMap1.json").map[0];
			testMap2 = new TiledJSON("/maps/testMap2.json").map[0];
			
			leafForest1Map = new Tilemap("/maps/leaf.json", "/maps/");
			
			idleAnim = loadImages("/sonicsprites", "idle");
			runSlowestAnim = loadImages("/sonicsprites", "slowest");
			runSlowAnim    = loadImages("/sonicsprites", "slow");
			runNormalAnim  = loadImages("/sonicsprites", "run");
			runFastAnim    = loadImages("/sonicsprites", "fast");
			runFastestAnim = loadImages("/sonicsprites", "fastest");
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
			
			springAnim = loadImages("/objectsprites", "spring");
			ringAnim = loadImages("/hudsprites", "ring");
			sparkleAnim = loadImages("/objectsprites", "effect");
			
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
			
			hudRingAnim = loadImages("/hudsprites", "ring");
			
			/*leafBG = new Background(new String[]{"/maps/bg0.png", "/maps/bg1.png", "/maps/bg2.png"}, new int[]{0, 5, 2}, new int[]{5, 10, 13}, 2, 16);
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
			catch(Exception e) {e.printStackTrace();}*/
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