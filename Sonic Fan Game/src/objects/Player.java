package objects;

import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static functionholders.CollisionFunctions.*;
import static functionholders.GeometryFunctions.*;
import static functionholders.ListFunctions.*;
import static functionholders.DebugFunctions.*;
import static functionholders.MathFunctions.*;
import static java.awt.event.KeyEvent.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedInputStream;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import badniks.Badnik;
import datatypes.Animation;
import datatypes.Shape;
import datatypes.Vector;
import main.KeyListener;
import main.Loader;
import rendering.Camera;
import rendering.Shader;
import shapes.Circle;
import shapes.Rectangle;

public class Player {
	private final double SPRINT_ACCEL 		 	= 2;
	private final double MOVE_ACCEL  		  	= 0.115;
	private final double GROUND_ACCEL_LIMIT  	= 35;
	private final double BOOST_ACCEL_SCALE	 	= 2;
	private final double BOOST_LIMIT_SCALE	  	= 1.5;
	private final double BOOST_START_SPEED	  	= 25;
	private final double BOOST_STOP_SPEED	  	= 10;
	private final int 	 BOOST_TIME			 	= 60 * 3;
	private final double SKID_ACCEL  		  	= 1;
	private final double DRAG_DECEL   		  	= 0.108;
	private final double DEBUG_JUMP_IMPULSE  	= 50;
	private final double JUMP_IMPULSE 		  	= 25;
	private final double JUMP_SWITCH  		  	= 2;
	private final double GRAVITY      		  	= 1;
	private final double GROUND_GRAVITY_ACCEL 	= 0.6;
	private final double GROUND_GRAVITY_DECEL 	= 0.4;
	private final double SPIN_DECEL			  	= 0.025;
	private final double MIN_POTENTIAL_GRAVITY 	= 0.1;
	
	private final double SLOW_MIN_SPEED 	= 10;
	private final double NORMAL_MIN_SPEED 	= 20;
	private final double FAST_MIN_SPEED 	= 30;
	private final double FASTEST_MIN_SPEED 	= 40;
	private final double SKID_MIN_SPEED 	= 5;
	
	private final double SPINDASH_MIN_STRENGTH = 35;
	private final double SPINDASH_CHARGE_SCALE = 5;
	private final double SPINDASH_MAX_STRENGTH = 50;
	
	private final double GROUND_ANGLE_MASK_OFFSET_X  = 0;
	private final double GROUND_ANGLE_MASK_OFFSET_Y  = 1;
	private final double GROUND_ANGLE_MASK_RADIUS    = 50;
	
	private final double STICK_OFFSET_SCALE = 1;
	private final double STICK_MIN_SPEED    = 10;
	
	private final double GROUND_MASK_OFFSET_X  = 0;
	private final double GROUND_MASK_OFFSET_Y  = 1;
	private final double GROUND_MASK_WIDTH     = 100;
	private final double GROUND_MASK_HEIGHT    = 50;
	
	private final double LEDGE_MASK_L_OFFSET_X  = -37.5;
	private final double LEDGE_MASK_L_OFFSET_Y  = 50;
	private final double LEDGE_MASK_L_WIDTH     = 25;
	private final double LEDGE_MASK_L_HEIGHT    = 50;
	
	private final double LEDGE_MASK_R_OFFSET_X  = 37.5;
	private final double LEDGE_MASK_R_OFFSET_Y  = 50;
	private final double LEDGE_MASK_R_WIDTH     = 25;
	private final double LEDGE_MASK_R_HEIGHT    = 50;
	
	private final double LAND_MASK_OFFSET_Y  = 100;
	private final double LAND_MASK_WIDTH     = 100;
	private final double LAND_MASK_HEIGHT    = 100;
	
	private final double MASK_RADIUS = 50;
	
	private final double SCALE 					= 0.5;
	private final int    SPRITE_SCALE 			= 4;
	private final double ANIM_SPEED_SCALE 		= 0.4;
	private final double STEP_SOUND_SPEED		= 100;
	private final double STEP_SPEED_SCALE 		= 1;
	private final double STEP_SPEED_OFFSET		= 2;
	private final double MAX_STEP_SPEED 		= 25;
	
	public boolean        DRAW_MASKS 	= false;
	private final boolean DRAW_SPRITES	= true;
	
	private final int IDLE_ANIM 			= 0;
	private final int RUN_ANIM 				= 1;
	private final int FALL_ANIM 			= 2;
	private final int JUMP_ANIM 			= 3;
	private final int SKID_ANIM 			= 4;
	private final int SPIN_ANIM 			= 5;
	private final int CROUCH_ANIM_0 		= 6;
	private final int CROUCH_ANIM_1 		= 7;
	private final int SPINDASH_ANIM 		= 8;
	private final int SPINDASH_CHARGE_ANIM 	= 9;
	private final int SKIRT_ANIM		 	= 10;
	private final int TURN_ANIM			 	= 11;
	private final int BOUNCING_UP_ANIM		= 12;
	private final int BOUNCING_DOWN_ANIM	= 13;
	private final int LAND_ANIM				= 14;
	private final int START_ANIM			= 15;
	private final int TRICK_RIGHT_ANIM		= 16;
	private final int TRICK_UP_0_ANIM		= 17;
	private final int TRICK_UP_1_ANIM		= 18;
	private final int RAMP_ANIM				= 19;
	private final int SWING_ANIM			= 20;
	private final int DASH_ANIM				= 21;
	
	private final int NO_DUST_ANIM 		= 0;
	private final int REGULAR_DUST_ANIM = 1;
	private final int CHARGE_DUST_ANIM 	= 2;
	
	// keys
	private boolean upArrow;
	private boolean downArrow;
	private boolean leftArrow;
	private boolean rightArrow;
	private boolean spaceBar;
	private boolean shiftKey;
	private boolean controlKey;
	//private boolean trickKey;
	
	// flags
	private boolean skidding;
	private boolean skirting;
	private boolean turning;
	private boolean ground;
	private boolean ledge;
	private boolean jumping;
	private boolean jumpReady;
	private boolean jumpSlowing;
	private boolean spinning;
	private boolean crouching0;
	private boolean crouching1;
	private boolean spindashReady;
	private boolean spindashing;
	private boolean spindashCharge;
	private boolean chargeReady;
	private boolean bouncing;
	private boolean landing;
	private boolean controlKeyReady;
	public boolean starting;
	private boolean trickReady;
	private boolean trickReadyReady;
	public boolean stopCam;
	private boolean groundFlipped;
	private boolean boostMode;
	private boolean boostReady;
	private boolean rampDashing;
	private boolean swinging;
	private boolean justSwang;
	private boolean dashing;
	private boolean dashReady;
	private boolean springPoling;
	
	private double jumpSlowed;
	public double groundSpeed;
	private double spindashStrength;
	private double preSpringPoleXSpeed;
	
	private int trickType;
	private int boostTimer;
	private int swingStartFrame;
	private int swingDirection;
	
	private double stepTimer;
	private int stepIndex;
	private int chargeDustTimer;
	public int layer;
	
	private Shape mask;
	
	private Rotor rotor;
	private SpringPole springPole;
	
	public  Vector pos;
	public  Vector vel;
	private Vector groundAxis;
	
	private Animation idleAnim;
	private Animation runSlowestAnim;
	private Animation runSlowAnim;
	private Animation runNormalAnim;
	private Animation runFastAnim;
	private Animation runFastestAnim;
	private Animation bounceUpAnim;
	private Animation bounceDownAnim;
	private Animation fallAnim;
	private Animation jumpAnim;
	private Animation skidAnim;
	private Animation spinAnim;
	private Animation crouchAnim0;
	private Animation crouchAnim1;
	private Animation spindashAnim;
	private Animation spindashChargeAnim;
	private Animation spindashDustAnim;
	private Animation spindashChargeDustAnim;
	private Animation skirtAnim;
	private Animation turnAnim;
	private Animation landAnim;
	private Animation startAnim;
	private Animation trickRightAnim;
	private Animation trickUp0Anim;
	private Animation trickUp1Anim;
	private Animation rampAnim;
	private Animation swingAnim;
	private Animation dashAnim;
	
	public  int facing;
	public  int anim;
	private int dustAnim;
	
	private Clip jumpSound0;
	private Clip jumpSound1;
	private Clip landSound;
	private Clip skidSound;
	private Clip spinSound;
	private Clip spindashChargeSound;
	private Clip spindashReleaseSound;
	private Clip stepSound0;
	private Clip stepSound1;
	private Clip stepSound2;
	private Clip stepSound3;
	private Clip stepSound4;
	private Clip trickSound;
	private Clip boostSound;
	private Clip dashSound;
	
	private Clip popSound;
	private Clip ringSound;
	private Clip springSound;
	
	private Clip voice3;
	private Clip voice2;
	private Clip voice1;
	private Clip voiceGo;
	public int voice;
	
	public int rings;
	public int score;
	
	private AfterImage[] afters;
	
	public Player(double x, double y) {
		pos = new Vector(x, y);
		vel = new Vector();
		groundAxis = new Vector(0, 1);
		mask = new Circle(MASK_RADIUS);
		facing = -1;
		layer = 1;
		
		idleAnim = new Animation(Loader.idleAnim, new int[]{6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 12, 6, 6, 6, 12, 8}, 0);
		runSlowestAnim = new Animation(Loader.runSlowestAnim, new int[]{6, 6, 6, 6, 6, 6, 6, 6}, 0);
		runSlowAnim    = new Animation(Loader.runSlowAnim, new int[]{6, 6, 6, 6, 6, 6, 6, 6}, 0);
		runNormalAnim  = new Animation(Loader.runNormalAnim, new int[]{6, 6, 6, 6, 6, 6, 6, 6}, 0);
		runFastAnim    = new Animation(Loader.runFastAnim, new int[]{6, 6, 6, 6, 6, 6, 6, 6}, 0);
		runFastestAnim = new Animation(Loader.runFastestAnim, new int[]{6, 6, 6, 6, 6, 6, 6, 6}, 0);
		bounceUpAnim = new Animation(Loader.bounceUpAnim, new int[]{3, 3, 3, 3}, 1);
		bounceDownAnim = new Animation(Loader.bounceDownAnim, new int[]{2, 2, 2, 3, 3, 4}, 0);
		fallAnim = new Animation(Loader.fallAnim, new int[]{3, 3, 3}, 0);
		jumpAnim = new Animation(Loader.jumpAnim, new int[]{3, 3, 2, 2, 2, 2, 2, 2, 2, 2}, 2);
		skidAnim = new Animation(Loader.skidAnim, new int[]{2, 4, 4}, 1);
		spinAnim = new Animation(Loader.spinAnim, new int[]{2, 2, 2, 2}, 0);
		crouchAnim0 = new Animation(Loader.crouchAnim0, new int[]{1, 1, 1, 1}, 3);
		crouchAnim1 = new Animation(Loader.crouchAnim1, new int[]{1, 3}, 2);
		spindashAnim = new Animation(Loader.spindashAnim, new int[]{2, 2, 2, 2}, 0);
		spindashChargeAnim = new Animation(Loader.spindashChargeAnim, new int[]{2, 2, 2, 3}, 0);
		spindashDustAnim = new Animation(Loader.spindashDustAnim, new int[]{2, 2, 2, 2, 2, 2, 2, 2}, 0);
		spindashChargeDustAnim = new Animation(Loader.spindashChargeDustAnim, new int[]{2, 2, 2, 2, 2, 2, 2, 2}, 0);
		skirtAnim = new Animation(Loader.skirtAnim, new int[]{2, 2, 2, 4}, 0);
		turnAnim = new Animation(Loader.turnAnim, new int[]{1, 3}, 0);
		landAnim = new Animation(Loader.landAnim, new int[]{1, 2, 2, 2}, 1);
		startAnim = new Animation(Loader.startAnim, new int[]{2, 2, 4, 4, 4, 6, 4, 6, 4, 6, 4, 4, 4, 4, 4, 4, 6, 4, 6, 4, 4, 4, 4, 4, 4, 8, 4, 4, 4, 4, 4, 4, 4, 4, 4, 8, 6, 4, 110, 6, 60, 3}, 0);
		rampAnim = new Animation(Loader.rampAnim, new int[]{1, 2, 2, 2}, 1);
		swingAnim = new Animation(Loader.sonicRotorAnim, new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, 0); // not techincally accurate, it sometimes switches to 3 frames, not sure when
		dashAnim = new Animation(Loader.dashAnim, new int[]{2, 2, 2, 2, 2, 2, 2}, 4);
		
		trickRightAnim = new Animation(Loader.trickRightAnim, new int[]{2, 4, 2, 1, 1, 1, 1, 1, 1, 1, 1}, 3);
		trickUp0Anim = new Animation(Loader.trickUp0Anim, new int[]{3, 6, 3, 1, 1, 3, 3, 3, 3}, 5);
		trickUp1Anim = new Animation(Loader.trickUp1Anim, new int[]{3, 3, 3, 2, 2, 2}, 3);
		
		jumpSound0 = Loader.jumpSound0;
		jumpSound1 = Loader.jumpSound1;
		landSound = Loader.landSound;
		skidSound = Loader.skidSound;
		spinSound = Loader.spinSound;
		spindashChargeSound = Loader.spindashChargeSound;
		spindashReleaseSound = Loader.spindashReleaseSound;
		stepSound0 = Loader.stepSound0;
		stepSound1 = Loader.stepSound1;
		stepSound2 = Loader.stepSound2;
		stepSound3 = Loader.stepSound3;
		stepSound4 = Loader.stepSound4;
		trickSound = Loader.trickSound;
		boostSound = Loader.boostSound;
		dashSound = Loader.dashSound;
		
		popSound = Loader.popSound;
		ringSound = Loader.ringSound;
		springSound = Loader.springSound;
		
		voice3 = Loader.voice3;
		voice2 = Loader.voice2;
		voice1 = Loader.voice1;
		voiceGo = Loader.voiceGo;
		
		starting = true;
		ground = true;
		voice = 0;
	}
	
	public void update(float dt, Shape[] layer0, Shape[] layer1, Shape[] layer2, Shape[] layer1Triggers, Shape[] layer2Triggers, Shape[] platforms, Ring[] rings, Spring[] springs, Badnik[] badniks, Item[] items, Ramp[] ramps, Rotor[] rotors, SpringPole[] springPoles) {
		checkKeys();
		
		if(starting) {starting();}
		if(!starting && !stopCam) { // NOT ELSE
			groundSpeed = getRotatedVectorComponents(vel, groundAxis).x;
			vel.translate(groundAxis.getPerpendicular().normalize().scale(groundSpeed));
			
			movement();
			drag();
			jump();
			trick();
			spindash();
			crouch();
			dash();
			gravity();
			
			if(ground) {
				if(!boostMode) {
					if(abs(groundSpeed) >= BOOST_START_SPEED * SCALE) {
						boostReady = true;
						boostTimer--;
						if(boostTimer == 0) {
							boostMode = true;
							if(groundSpeed > 0) {groundSpeed = GROUND_ACCEL_LIMIT * BOOST_LIMIT_SCALE;}
							if(groundSpeed < 0) {groundSpeed = -GROUND_ACCEL_LIMIT * BOOST_LIMIT_SCALE;}
							
							boostSound.stop();
							boostSound.flush();
							boostSound.setFramePosition(0);
							boostSound.start();
						}
					}
					else {
						boostReady = false;
						boostTimer = BOOST_TIME;
					}
				}
				else {
					if(abs(groundSpeed) < BOOST_STOP_SPEED) {boostMode = false;}
				}
			}
		}
		if(stopCam) {vel = new Vector();}
		
		boolean[] platMasks = null;
		if(platforms != null) {platMasks = checkPlatforms(platforms);}
		
		vel.translate(groundAxis.getPerpendicular().normalize().scale(-groundSpeed));
		pos.translate(vel/*.scale(dt / (1.0f / 60.0f))*/);
		
		checkLayer(layer1Triggers, layer2Triggers);
		
		Shape[] shapes = null;
		if(layer == 1) {shapes = combine(layer0, layer1);}
		if(layer == 2) {shapes = combine(layer0, layer2);}
		if(platMasks != null) {shapes = combine(shapes, applyMask(platforms, platMasks));}
		
		if(shapes != null) {
			if(bouncing && vel.y < 0 && !ground) {
				groundAxis = new Vector(0, -1);
				groundFlipped = true;
			}
			else {groundFlipped = false;}
			
			collide(shapes);
			checkLedge(shapes);
			stick(shapes);
		
			checkGround(shapes);
			getGroundAxis(shapes);
			
			checkLanding(shapes);
		}
		
		rings(rings);
		badniks(badniks);
		items(items);
		springs(springs);
		ramps(ramps);
		rotors(rotors);
		springPoles(springPoles);

		afterImages(dt);
	}
	
	private void starting() {
		if(anim != START_ANIM) {
			anim = START_ANIM;
			startAnim.reset();
		}
		else {
			// 3 = image 37 frame 4
			// 2 = image 38 frame 60
			// 1 = image 40 frame 4
			// go = finshed
			
			if(startAnim.frame == 37 && startAnim.timer == 3) {
				voice = 3;
				voice3.stop();
				voice3.flush();
				voice3.setFramePosition(0);
				voice3.start();
			}
			if(startAnim.frame == 38 && startAnim.timer == 59) {
				voice = 2;
				voice2.stop();
				voice2.flush();
				voice2.setFramePosition(0);
				voice2.start();
			}
			if(startAnim.frame == 40 && startAnim.timer == 3) {
				voice = 1;
				voice1.stop();
				voice1.flush();
				voice1.setFramePosition(0);
				voice1.start();
			}
			
			startAnim.update(1);
			if(startAnim.finished) {
				voice = 4;
				starting = false;
				facing = 1;
				ground = true;
				jumping = false;
				ledge = false;
				
				vel = new Vector(10, 0);
				
				voiceGo.stop();
				voiceGo.flush();
				voiceGo.setFramePosition(0);
				voiceGo.start();
			}
		}
	}
	
	private void afterImages(float dt) {
		double w = idleAnim.getCurrentSize()[0] * 2;
		double h = idleAnim.getCurrentSize()[1] * 2;
		double t = limitAngle(getAngleOfVector(groundAxis) * -1 - PI / 2);
		double s = -w / 8;
		if(facing == -1) {s = 0;}
		
		if(boostMode) {
			if(anim == SWING_ANIM)                {afters = append(afters, new AfterImage(getCurrentAnim().getCurrentFrame(), pos.x - w / 2 - 32 + 2, pos.y - h / 2 - 32 - 2, pos.x, pos.y, t, -facing * 2, 2, 2, 5));}
			else if(anim == SPIN_ANIM)            {afters = append(afters, new AfterImage(getCurrentAnim().getCurrentFrame(), pos.x - w / 2,          pos.y - h / 2 - 32 - 4, pos.x, pos.y, 0, -facing * 2, 2, 2, 5));}
			else if(anim == SPINDASH_ANIM)        {afters = append(afters, new AfterImage(getCurrentAnim().getCurrentFrame(), pos.x - w / 2 + s,      pos.y - h / 2 - 32 - 4, pos.x, pos.y, t, -facing * 2, 2, 2, 5));}
			else if(anim == SPINDASH_CHARGE_ANIM) {afters = append(afters, new AfterImage(getCurrentAnim().getCurrentFrame(), pos.x - w / 2 + s,      pos.y - h / 2 - 32 - 4, pos.x, pos.y, t, -facing * 2, 2, 2, 5));}
			else                                  {afters = append(afters, new AfterImage(getCurrentAnim().getCurrentFrame(), pos.x - w / 2,          pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, 2, 5));}
		}
		else {afters = null;}
		
		if(afters != null) {
			int[] removals = null;
			for(int i = 0; i < afters.length; i++) {
				afters[i].update(dt);
				if(afters[i].remove) {removals = append(removals, i);}
			}
			if(removals != null) {for(int i = 0; i < removals.length; i++) {afters = removeIndex(afters, removals[i]);}}
		}
	}
	
	private void movement() {
		double moveSpeed;
		if(!shiftKey) {moveSpeed = MOVE_ACCEL * SCALE;}
		else          {moveSpeed = SPRINT_ACCEL * SCALE;}
		
		double accelScale = 1;
		double capScale = 1;
		
		if(boostMode) {
			accelScale = BOOST_ACCEL_SCALE;
			capScale = BOOST_LIMIT_SCALE;
		}
		
		if(!ground) {
			skidding = false;
			skirting = false;
			turning = false;
		}
		
		if(!crouching0 && !spindashing) {
			if(!spinning && !rampDashing && !dashing) { // regular movement
				if(leftArrow && !rightArrow) {
					if(groundSpeed <= 0 || !ground) {
						if(!ground) {facing = -1;}
						if(skidding) {
							skidding = false;
							if(facing == 1) {skirting = true;}
						}
						
						if(groundSpeed > -GROUND_ACCEL_LIMIT * capScale * SCALE || shiftKey) {
							groundSpeed -= moveSpeed * accelScale;
							if(groundSpeed < -GROUND_ACCEL_LIMIT * capScale * SCALE && !shiftKey) {groundSpeed = -GROUND_ACCEL_LIMIT * capScale * SCALE;}
						}
					}
					else {
						groundSpeed -= SKID_ACCEL * SCALE;
						if(groundSpeed >= SKID_MIN_SPEED) {skidding = true;}
					}
				}
				if(rightArrow && !leftArrow) {
					if(groundSpeed >= 0 || !ground) {
						if(!ground) {facing = 1;}
						if(skidding) {
							skidding = false;
							if(facing == -1) {skirting = true;}
						}
						
						if(groundSpeed < GROUND_ACCEL_LIMIT * capScale * SCALE || shiftKey) {
							groundSpeed += moveSpeed * accelScale;
							if(groundSpeed > GROUND_ACCEL_LIMIT * capScale * SCALE && !shiftKey) {groundSpeed = GROUND_ACCEL_LIMIT * capScale * SCALE;}
						}
					}
					else {
						groundSpeed += SKID_ACCEL * SCALE;
						if(groundSpeed <= -SKID_MIN_SPEED) {skidding = true;}
					}
				}
			}
			else { // movement while spinning
				if(ground) {
					if(leftArrow && !rightArrow) {
						if(groundSpeed > 0) {
							groundSpeed -= SKID_ACCEL * SCALE;
							if(groundSpeed <= 0) {
								spinning = false;
								facing = -1;
							}
						}
						else {facing = -1;}
					}
					if(rightArrow && !leftArrow) {
						if(groundSpeed < 0) {
							groundSpeed += SKID_ACCEL * SCALE;
							if(groundSpeed >= 0) {
								spinning = false;
								facing = 1;
							}
						}
						else {facing = 1;}
					}
				}
				else {
					if(leftArrow && !rightArrow) {
						groundSpeed -= MOVE_ACCEL * SCALE;
						if(groundSpeed < 0) {facing = -1;}
					}
					if(rightArrow && !leftArrow) {
						groundSpeed += MOVE_ACCEL * SCALE;
						if(groundSpeed > 0) {facing = 1;}
					}
				}
			}
		}
	}
	
	private void drag() {
		if(!leftArrow && !rightArrow && ground || leftArrow && rightArrow && ground) {
			skidding = false;
			
			if(!spinning && !spindashing) { // regular drag
				     if(groundSpeed > 0) {groundSpeed -= DRAG_DECEL * SCALE;}
				else if(groundSpeed < 0) {groundSpeed += DRAG_DECEL * SCALE;}
				
				if(groundSpeed >= -DRAG_DECEL * SCALE && groundSpeed <= DRAG_DECEL * SCALE) {groundSpeed = 0;}
			}
			else if(spinning) { // spinning drag
			         if(groundSpeed > 0) {groundSpeed -= SPIN_DECEL * SCALE;}
				else if(groundSpeed < 0) {groundSpeed += SPIN_DECEL * SCALE;}
				
				if(groundSpeed >= -SPIN_DECEL * SCALE && groundSpeed <= SPIN_DECEL * SCALE) {
					groundSpeed = 0;
					spinning = false;
				}
			}
			
			if(downArrow && groundSpeed != 0) {
				if(!spinning) {
					spinning = true;
					crouching0 = false;
					crouching1 = false;
					spindashReady = false;
					
					spinSound.stop();
					spinSound.flush();
					spinSound.setFramePosition(0);
					spinSound.start();
				}
			}
		}
		
		if(spindashing) {
		         if(groundSpeed > 0) {groundSpeed -= SKID_ACCEL * SCALE;}
			else if(groundSpeed < 0) {groundSpeed += SKID_ACCEL * SCALE;}
			
			if(groundSpeed >= -SKID_ACCEL * SCALE && groundSpeed <= SKID_ACCEL * SCALE) {groundSpeed = 0;}
		}
	}

	private void jump() {
		if(ground && !spaceBar) {jumpReady = true;}
		if(!ground) {jumpReady = false;}
		else {jumping = false;}
		
		if(jumpReady && spaceBar && !crouching0 && ground && !spindashing) {
			jumpReady = false;
			ground = false;
			jumping = true;
			jumpSlowing = false;
			spinning = false;
			
			if(!shiftKey) {
				jumpSlowed = groundAxis.y * -JUMP_IMPULSE * SCALE;
				vel.translate(groundAxis.scale(-JUMP_IMPULSE * SCALE));
			}
			else {
				jumpSlowed = groundAxis.y * -DEBUG_JUMP_IMPULSE * SCALE;
				vel.translate(groundAxis.scale(-DEBUG_JUMP_IMPULSE * SCALE));
			}
		}
		else {
			if(jumping) {
				if(!spaceBar || jumpSlowing) {
					vel.translate(0, JUMP_SWITCH * SCALE);
					jumpSlowed += JUMP_SWITCH * SCALE;
					jumpSlowing = true;
				}
				
				if(jumpSlowed >= 0 && (anim == JUMP_ANIM || anim == LAND_ANIM)) {jumping = false;}
			}
		}
	}
	
	private void trick() {
		if(spaceBar && trickReady) {
			if(rightArrow || leftArrow) {trickType = 1;}
			else if(upArrow) {trickType = 2;}
			
			if(trickType != 0) {
				trickReady = false;
				trickReadyReady = false;
				stopCam = true;
				vel = new Vector();
				groundSpeed = 0;
			}
		}
		if((bouncing || rampDashing) && !spaceBar && trickReadyReady) {trickReady = true;}
	}
	
	private void spindash() {
		if(spindashReady && spaceBar || ground && controlKey && controlKeyReady && !spinning) {
			if(!spindashing) {
				spindashing = true;
				spindashCharge = false;
				chargeReady = false;
				chargeDustTimer = 45;
				spindashStrength = SPINDASH_MIN_STRENGTH * SCALE;
				
				spindashChargeSound.stop();
				spindashChargeSound.flush();
				spindashChargeSound.setFramePosition(0);
				spindashChargeSound.start();
			}
		}
		
		if(!spindashing && spinning && controlKey && controlKeyReady) {spinning = false;}
		
		controlKeyReady = !controlKey;
		
		if(spindashing) {
			if(downArrow || controlKey) {
				if(spaceBar && chargeReady) {
					chargeDustTimer = 45;
					spindashCharge = true;
					spindashStrength += SPINDASH_CHARGE_SCALE * SCALE;
					spindashStrength = min(spindashStrength, SPINDASH_MAX_STRENGTH);
					
					spindashChargeSound.stop();
					spindashChargeSound.flush();
					spindashChargeSound.setFramePosition(0);
					spindashChargeSound.start();
				}
				
				if(!spaceBar) {chargeReady = true;}
				else {chargeReady = false;}
			}
			else {
				crouching0 = false;
				crouching1 = false;
				spindashReady = false;
				spindashing = false;
				spinning = true;
				jumpReady = false;
				groundSpeed = spindashStrength * facing;
				
				spindashReleaseSound.stop();
				spindashReleaseSound.flush();
				spindashReleaseSound.setFramePosition(0);
				spindashReleaseSound.start();
			}
		}
	}
	
	private void crouch() {
		if(ground && groundSpeed == 0 && downArrow && !spindashing) {
			crouching0 = true;
			crouching1 = true;
			spindashReady = false;
		}
		if(crouching0 && !downArrow) {
			crouching1 = false;
			spindashReady = false;
		}
		
		if(crouching0 && groundSpeed != 0) {
			crouching0 = false;
			crouching1 = false;
			spindashReady = false;
			spinning = true;
			
			spinSound.stop();
			spinSound.flush();
			spinSound.setFramePosition(0);
			spinSound.start();
		}
	}

	private void dash() {
		if(controlKey && dashReady && !dashing && !bouncing && !rampDashing && !spinning && !spindashing && (anim == JUMP_ANIM || anim == LAND_ANIM)) {
			jumping = false;
			jumpSlowing = false;
			trickReady = false;
			trickReadyReady = false;
			dashing = true;
			jumping = false;
			
			groundSpeed += 15 * SCALE * facing;
			if(groundSpeed > GROUND_ACCEL_LIMIT * SCALE && !boostMode) {groundSpeed = GROUND_ACCEL_LIMIT * SCALE;}
			if(groundSpeed > GROUND_ACCEL_LIMIT * BOOST_LIMIT_SCALE * SCALE && boostMode) {groundSpeed = GROUND_ACCEL_LIMIT * BOOST_LIMIT_SCALE * SCALE;}
			vel.y = 0;
			
			dashSound.stop();
			dashSound.flush();
			dashSound.setFramePosition(0);
			dashSound.start();
		}
		dashReady = !ground && !controlKey;
	}
	
	private void gravity() {
		double capScale = 1;
		
		if(boostMode) {capScale = BOOST_LIMIT_SCALE;}
		
		if(!ground) {
			vel.translate(0, GRAVITY * SCALE);
			
			if(jumping) {jumpSlowed += GRAVITY * SCALE;}
			
			if(vel.x < -GROUND_ACCEL_LIMIT * capScale * SCALE && !shiftKey && !spinning) {vel.x = -GROUND_ACCEL_LIMIT * capScale * SCALE;}
			if(vel.x > GROUND_ACCEL_LIMIT * capScale * SCALE && !shiftKey && !spinning) {vel.x = GROUND_ACCEL_LIMIT * capScale * SCALE;}
		}
		else {
			Vector tempGrav = new Vector(0, SCALE).project(groundAxis.getPerpendicular().normalize());
			Vector accelGrav = new Vector(0, GROUND_GRAVITY_ACCEL * SCALE).project(groundAxis.getPerpendicular().normalize());
			Vector decelGrav = new Vector(0, GROUND_GRAVITY_DECEL * SCALE).project(groundAxis.getPerpendicular().normalize());
			
			if(abs(groundSpeed + getRotatedVectorComponents(tempGrav, groundAxis).x) >= abs(groundSpeed)) {tempGrav = accelGrav;}
			else {tempGrav = decelGrav;}
			
			if(tempGrav.getLength() >= MIN_POTENTIAL_GRAVITY) {groundSpeed += getRotatedVectorComponents(tempGrav, groundAxis).x;}
			
			if(groundSpeed < -GROUND_ACCEL_LIMIT * capScale * SCALE && !shiftKey && !spinning) {groundSpeed = -GROUND_ACCEL_LIMIT * capScale * SCALE;}
			if(groundSpeed > GROUND_ACCEL_LIMIT * capScale * SCALE && !shiftKey && !spinning) {groundSpeed = GROUND_ACCEL_LIMIT * capScale * SCALE;}
		}
	}

	private void checkLayer(Shape[] layer1Triggers, Shape[] layer2Triggers) {
		mask = new Circle(MASK_RADIUS * SCALE);
		mask.relocate(pos);
		
		if(layer2Triggers != null) {for(int i = 0; i < layer2Triggers.length; i++) {if(checkCollision(mask, layer2Triggers[i])) {layer = 2;}}}
		if(layer1Triggers != null) {for(int i = 0; i < layer1Triggers.length; i++) {if(checkCollision(mask, layer1Triggers[i])) {layer = 1;}}}
	}
	
	private void collide(Shape[] shapes) {
		mask = new Circle(MASK_RADIUS * SCALE);
		mask.relocate(pos);
		Vector dir = clip(mask, shapes);
		
		pos.translate(dir);
		
		if(dir.getLength() != 0) {
			dir = dir.getPerpendicular();
			vel = vel.project(dir);
		}
	}
	
	private boolean[] checkPlatforms(Shape[] shapes) {
		boolean[] out = new boolean[shapes.length];
		
		for(int i = 0; i < shapes.length; i++) {
			out[i] = true;
			for(int p = 0; p < shapes[i].points.length; p++) {if(pos.y + MASK_RADIUS * SCALE > shapes[i].points[p].y) {out[i] = false;}}
		}
		
		return(out);
	}

	private void checkLedge(Shape[] shapes) {
		Shape groundMask0 = getRotatedRectangle(pos, LEDGE_MASK_L_WIDTH * SCALE, LEDGE_MASK_L_HEIGHT, LEDGE_MASK_L_OFFSET_X * SCALE, LEDGE_MASK_L_OFFSET_Y * SCALE);
		Shape groundMask1 = getRotatedRectangle(pos, LEDGE_MASK_R_WIDTH * SCALE, LEDGE_MASK_R_HEIGHT, LEDGE_MASK_R_OFFSET_X * SCALE, LEDGE_MASK_R_OFFSET_Y * SCALE);
		
		boolean ground0 = false;
		boolean ground1 = false;
		for(int i = 0; i < shapes.length; i++) {if(checkCollision(groundMask0, shapes[i])) {ground0 = true;}}
		for(int i = 0; i < shapes.length; i++) {if(checkCollision(groundMask1, shapes[i])) {ground1 = true;}}
		
		ledge = !(ground0 && ground1);
		if(ledge) {groundAxis = new Vector(0, 1);}
	}
	
	private void stick(Shape[] shapes) {
		if(ground && !ledge) {
			pos.translate(groundAxis.scale(STICK_OFFSET_SCALE * SCALE * vel.getLength()));
			mask = new Circle(MASK_RADIUS * SCALE);
			mask.relocate(pos);
			
			Vector dir = clip(mask, shapes);
			
			pos.translate(dir);
			
			if(dir.getLength() != 0) {
				dir = dir.getPerpendicular();
				vel = vel.project(dir);
			}
		}
	}

	private void checkGround(Shape[] shapes) {
		boolean oldGround = ground;
		
		Shape groundMask;
		if(!groundFlipped) {groundMask = getRotatedCircle(pos, GROUND_ANGLE_MASK_RADIUS * SCALE, GROUND_ANGLE_MASK_OFFSET_X * SCALE, GROUND_ANGLE_MASK_OFFSET_Y * SCALE);}
		else {groundMask = getRotatedCircle(pos, (GROUND_ANGLE_MASK_RADIUS - 1) * SCALE, GROUND_ANGLE_MASK_OFFSET_X * SCALE, (GROUND_ANGLE_MASK_OFFSET_Y + 1) * SCALE);}
		
		ground = false;
		for(int i = 0; i < shapes.length; i++) {if(checkCollision(groundMask, shapes[i])) {ground = true;}}
		
		if(ground && !oldGround) {
			ledge = false;
			jumping = false;
			trickReady = false;
			trickReadyReady = false;
			trickType = 0;
			bouncing = false;
			rampDashing = false;
			dashing = false;
			
			/*landSound.stop();
			landSound.flush();
			landSound.setFramePosition(0);
			landSound.start();*/
			
			if(downArrow) {
				if(!spinning && vel.x != 0) {
					spinning = true;
					
					spinSound.stop();
					spinSound.flush();
					spinSound.setFramePosition(0);
					spinSound.start();
				}
			}
			else {spinning = false;}
		}
		
	}
	
	private void getGroundAxis(Shape[] shapes) {
		if(ground && !ledge) {
			if(abs(groundSpeed) < STICK_MIN_SPEED * SCALE) {groundAxis = new Vector(0, 1);}
			
			Shape groundMask;
			
			groundMask = getRotatedCircle(pos, GROUND_ANGLE_MASK_RADIUS * SCALE, GROUND_ANGLE_MASK_OFFSET_X * SCALE, GROUND_ANGLE_MASK_OFFSET_Y * SCALE);
			
			groundAxis = new Vector(0, 0);
			groundAxis.translate(clip(groundMask, shapes));
			
			if(groundAxis.getLength() != 0) {groundAxis = groundAxis.scale(-1).normalize();}
			else {groundAxis = new Vector(0, 1);}
		}
		else {groundAxis = new Vector(0, 1);}
	}
	
	private void rings(Ring[] rings) {
		if(rings != null) {
			for(int i = 0; i < rings.length; i++) {
				if(rings[i].destroy == 0) {
					Shape ringMask = new Circle(rings[i].pos, 8 * 2);
					mask.relocate(pos);
					
					if(checkCollision(mask, ringMask)) {
						this.rings++;
						rings[i].destroy = 1;
						ringSound.stop();
						ringSound.flush();
						ringSound.setFramePosition(0);
						ringSound.start();
					}
				}
			}
		}
	}
	
	private void badniks(Badnik[] badniks) {
		if(badniks != null) {
			for(int i = 0; i < badniks.length; i++) {
				if(badniks[i].destroy == 0) {
					Shape badnikMask = new Circle(badniks[i].pos.add(8 * 2, 4 * 2), 8 * 2);
					mask.relocate(pos);
					
					if(checkCollision(mask, badnikMask)) {
						if(spinning || spindashing || (anim == JUMP_ANIM || anim == LAND_ANIM)) {
							score += 100;
							
							if(!ground) {
								vel = vel.project(new Vector(1, 0));
								vel.translate(new Vector(0, -1).scale(10));
							}
							
							badniks[i].destroy();
							
							popSound.stop();
							popSound.flush();
							popSound.setFramePosition(0);
							popSound.start();
						}
					}
				}
			}
		}
	}
	
	private void items(Item[] items) {
		if(items != null) {
			for(int i = 0; i < items.length; i++) {
				if(items[i].destroy == 0) {
					Shape itemMask = new Circle(items[i].pos.add(8 * 2, 4 * 2), 8 * 2);
					mask.relocate(pos);
					
					if(checkCollision(mask, itemMask)) {
						vel = vel.project(new Vector(1, 0));
						vel.translate(new Vector(0, -1).scale(5));
						items[i].destroy();
						
						if(anim != JUMP_ANIM && !spinning && !spindashing) {
							jumpReady = false;
							ground = false;
							jumping = false;
							jumpSlowing = false;
							bouncing = true;
							rampDashing = false;
							dashing = false;
						}
						
						popSound.stop();
						popSound.flush();
						popSound.setFramePosition(0);
						popSound.start();
					}
				}
			}
		}
	}
	
	private void springs(Spring[] springs) {
		if(springs != null) {
			for(int i = 0; i < springs.length; i++) {
				if(!springs[i].bouncing) {
					Shape springMask = new Rectangle(springs[i].pos.add(0, 12), new Vector(28, 21), Color.WHITE);
					mask.relocate(pos);
					
					if(checkCollision(mask, springMask)) {
						vel = vel.project(new Vector(sin(springs[i].angle), cos(springs[i].angle)));
						vel.translate(new Vector(cos(springs[i].angle), -sin(springs[i].angle)).scale(springs[i].strength));
						springs[i].bouncing = true;
						
						jumpReady = false;
						ground = false;
						jumping = false;
						jumpSlowing = false;
						spinning = false;
						bouncing = true;
						trickType = 0;
						trickReadyReady = true;
						rampDashing = false;
						dashing = false;
						
						springSound.stop();
						springSound.flush();
						springSound.setFramePosition(0);
						springSound.start();
					}
				}
			}
		}
	}
	
	private void ramps(Ramp[] ramps) {
		if(ramps != null) {
			for(int i = 0; i < ramps.length; i++) {
				if(groundSpeed >= 10 * ramps[i].direction) {
					Shape rampMask = new Rectangle(ramps[i].pos.add(5 * 8 * 2, -8 * 2), new Vector(8 * 2, 8 * 2), Color.WHITE);
					mask.relocate(pos);
					
					if(checkCollision(mask, rampMask) && !ground) {
						vel = new Vector(cos(ramps[i].angle) * ramps[i].strength, -sin(ramps[i].angle) * ramps[i].strength);
						
						jumpReady = false;
						ground = false;
						jumping = false;
						jumpSlowing = false;
						spinning = false;
						trickType = 0;
						trickReadyReady = true;
						rampDashing = true;
						dashing = false;
						
						boostSound.stop();
						boostSound.flush();
						boostSound.setFramePosition(0);
						boostSound.start();
					}
				}
			}
		}
	}
	
	private void rotors(Rotor[] rotors) {
		if(!swinging) {
			if(rotors != null) {
				boolean didSwing = false;
				
				for(int i = 0; i < rotors.length; i++) {
					Shape rampMask = new Circle(rotors[i].pos.add(-32 + 2, -32 - 2), 8 * 2, Color.WHITE);
					mask.relocate(pos);
					
					if(checkCollision(mask, rampMask) && !ground) {
						didSwing = true;
						if(!justSwang) {
							if(pos.y <= rotors[i].pos.y) {
								swingStartFrame = 0;
								
								if(vel.x <= 0) {swingDirection = facing;}
								else {swingDirection = -facing;}
							}
							else {
								swingStartFrame = 6;
								
								if(vel.x >= 0) {swingDirection = facing;}
								else {swingDirection = -facing;}
							}
							
							vel = new Vector();
							groundSpeed = 0;
							pos = new Vector(rotors[i].pos.x, rotors[i].pos.y);
							
							swinging = true;
							stopCam = true;
							jumpReady = false;
							ground = false;
							jumping = false;
							jumpSlowing = false;
							spinning = false;
							trickType = 0;
							trickReady = false;
							trickReadyReady = false;
							rampDashing = false;
							dashing = false;
							
							rotor = rotors[i];
							rotor.facing = -facing;
							
							boostSound.stop();
							boostSound.flush();
							boostSound.setFramePosition(0);
							boostSound.start();
						}
					}
				}
				
				if(!didSwing) {justSwang = false;}
			}
		}
		else {
			if(spaceBar && jumpReady) {
				double swingAngle = 0;
				if(swingAnim.frame ==  9) {swingAngle = 0;}
				if(swingAnim.frame == 10) {swingAngle = PI / 6;}
				if(swingAnim.frame == 11) {swingAngle = PI / 3;}
				if(swingAnim.frame ==  0) {swingAngle = PI / 2;}
				if(swingAnim.frame ==  1) {swingAngle = PI / 2 + PI / 6;}
				if(swingAnim.frame ==  2) {swingAngle = PI / 2 + PI / 3;}
				if(swingAnim.frame ==  3) {swingAngle = PI;}
				if(swingAnim.frame ==  4) {swingAngle = PI + PI / 6;}
				if(swingAnim.frame ==  5) {swingAngle = PI + PI / 3;}
				if(swingAnim.frame ==  6) {swingAngle = PI + PI / 2;}
				if(swingAnim.frame ==  7) {swingAngle = PI + PI / 2 + PI / 6;}
				if(swingAnim.frame ==  8) {swingAngle = PI + PI / 2 + PI / 3;}
				
				double posDistance = MASK_RADIUS * SCALE * 2;
				pos.translate(cos(swingAngle) * posDistance * facing, -sin(swingAngle) * posDistance);
				
				double swingPower = 20 * swingDirection;
				vel = new Vector(-sin(swingAngle) * swingPower * facing, -cos(swingAngle) * swingPower);
				
				rotor.anim.reset();
				rotor.facing = 1;
				swinging = false;
				stopCam = false;
				jumping = true;
				jumpReady = false;
				justSwang = true;
			}
			if(!spaceBar) {jumpReady = true;}
		}
	}
	
	private void springPoles(SpringPole[] springPoles) {
		if(springPoling) {
			if(!springPole.bouncing) {
				springPoling = false;
				stopCam = false;
				vel = new Vector(preSpringPoleXSpeed, -32 * SCALE);
				groundSpeed = preSpringPoleXSpeed;
			}
		}
		else {
			if(springPoles != null) {
				for(int i = 0; i < springPoles.length; i++) {
					springPole = springPoles[i];
					Shape springPoleMask = null;
					if(springPole.direction ==  1) {springPoleMask = new Rectangle(springPoles[i].pos.add(0.5 * 8 * 2, 2 * 8 * 2), new Vector(5.5 * 8 * 2, 8 * 2), Color.WHITE);}
					if(springPole.direction == -1) {springPoleMask = new Rectangle(springPoles[i].pos.add(0, 2 * 8 * 2), new Vector(5.5 * 8 * 2, 8 * 2), Color.WHITE);}
					mask.relocate(pos);
					
					if(checkCollision(mask, springPoleMask) && !ground && vel.y >= 0) {
						preSpringPoleXSpeed = vel.x;
						vel = new Vector();
						groundSpeed = 0;
						springPole.fastBounce();
						
						stopCam = true;
						springPoling = true;
						dashing = false;
						rampDashing = false;
						trickType = 0;
						trickReady = false;
						trickReadyReady = false;
						anim = JUMP_ANIM;
					}
				}
			}
		}
	}
	
	private void checkLanding(Shape[] shapes) {
		Shape landMask = getRotatedRectangle(pos, LAND_MASK_WIDTH * SCALE, LAND_MASK_HEIGHT * SCALE, 0, LAND_MASK_OFFSET_Y * SCALE);
		landing = false;
		for(int i = 0; i < shapes.length; i++) {if(checkCollision(landMask, shapes[i]) && anim == JUMP_ANIM) {landing = true;}}
	}
	
	private void manageAnimations(float dt) {
		if(!starting) {
			if(swinging) {
				if(anim != SWING_ANIM) {
					anim = SWING_ANIM;
					swingAnim.reset();
					rotor.anim.reset();
					rotor.anim.update(swingDirection);
					
					swingAnim.frame = swingStartFrame;
				}
				else {
					swingAnim.update(swingDirection);
					rotor.anim.update(swingDirection);
				}
			}
			else if(trickType != 0) {
				if(trickType == 1) {
					if(anim != TRICK_RIGHT_ANIM) {
						anim = TRICK_RIGHT_ANIM;
						trickRightAnim.reset();
						
						trickSound.stop();
						trickSound.flush();
						trickSound.setFramePosition(0);
						trickSound.start();
					}
					else {
						trickRightAnim.update(1);
						if(trickRightAnim.frame == trickRightAnim.repeatFrame) {
							if(stopCam) {
								stopCam = false;
								vel = new Vector(15 * facing, 0);
							}
						}
					}
				}
				if(trickType == 2) {
					if(anim != TRICK_UP_0_ANIM && anim != TRICK_UP_1_ANIM) {
						anim = TRICK_UP_0_ANIM;
						trickUp0Anim.reset();
						
						trickSound.stop();
						trickSound.flush();
						trickSound.setFramePosition(0);
						trickSound.start();
					}
					else {
						if(vel.y < 0 || stopCam) {
							trickUp0Anim.update(1);
							if(trickUp0Anim.frame == trickUp0Anim.repeatFrame) {
								if(stopCam) {
									stopCam = false;
									vel = new Vector(0, -15);
								}
							}
						}
						else {
							if(anim != TRICK_UP_1_ANIM) {
								anim = TRICK_UP_1_ANIM;
								trickUp1Anim.reset();
							}
							else {trickUp1Anim.update(1);}
						}
					}
				}
			}
			else {
				if(rampDashing) {
					if(anim != RAMP_ANIM) {
						anim = RAMP_ANIM;
						rampAnim.reset();
					}
					else {rampAnim.update(1);}
				}
				else if(dashing) {
					if(anim != DASH_ANIM) {
						anim = DASH_ANIM;
						dashAnim.reset();
					}
					else {dashAnim.update(1);}
				}
				else if(spindashing) {
					if(!spindashCharge) {
						if(anim == SPINDASH_CHARGE_ANIM) {
							spindashChargeAnim.update(1 /** (dt / (1.0f / 60.0f))*/);
							if(spindashChargeAnim.finished) {
								anim = SPINDASH_ANIM;
								spindashAnim.reset();
							}
						}
						else if(anim == SPINDASH_ANIM) {spindashAnim.update(1 /** (dt / (1.0f / 60.0f))*/);}
						else {
							anim = SPINDASH_ANIM;
							spindashAnim.reset();
						}
					}
					if(spindashCharge) {
						anim = SPINDASH_CHARGE_ANIM;
						spindashChargeAnim.reset();
						spindashCharge = false;
					}
					
					if(chargeDustTimer == 0) {
						if(dustAnim == REGULAR_DUST_ANIM) {spindashDustAnim.update(1 /** (dt / (1.0f / 60.0f))*/);}
						else {
							dustAnim = REGULAR_DUST_ANIM;
							spindashDustAnim.reset();
						}
					}
					else {
						if(dustAnim == CHARGE_DUST_ANIM) {spindashChargeDustAnim.update(1 /** (dt / (1.0f / 60.0f))*/);}
						else {
							dustAnim = CHARGE_DUST_ANIM;
							spindashChargeDustAnim.reset();
						}
						
						chargeDustTimer--;
					}
				}
				else {
					dustAnim = NO_DUST_ANIM;
					
					if(crouching1) {
						if(anim == CROUCH_ANIM_0) {
							crouchAnim0.update(1 /** (dt / (1.0f / 60.0f))*/);
							if(crouchAnim0.finished) {spindashReady = true;}
						}
						else {
							anim = CROUCH_ANIM_0;
							crouchAnim0.reset();
						}
					}
					else if(crouching0) {
						if(anim == CROUCH_ANIM_1) {
							crouchAnim1.update(1 /** (dt / (1.0f / 60.0f))*/);
							if(crouchAnim1.finished) {
								anim = IDLE_ANIM;
								idleAnim.reset();
								crouching0 = false;
							}
						}
						else {
							anim = CROUCH_ANIM_1;
							crouchAnim1.reset();
						}
					}
					else {
						if(spinning) {
							if(anim == SPIN_ANIM) {spinAnim.update(1 /** (dt / (1.0f / 60.0f))*/);}
							else {
								anim = SPIN_ANIM;
								spinAnim.reset();
							}
						}
						else {
							if(ground) {
								if(groundSpeed == 0 && !turning && !skidding && !skirting) {
									if(anim == IDLE_ANIM) {idleAnim.update(1 /** (dt / (1.0f / 60.0f))*/);}
									else {
										anim = IDLE_ANIM;
										idleAnim.reset();
									}
								}
								else {
									if(skidding || skirting) {
										if(skidding) {
											if(anim == SKID_ANIM) {skidAnim.update(1 /** (dt / (1.0f / 60.0f))*/);}
											else {
												anim = SKID_ANIM;
												skidAnim.reset();
												
												skidSound.stop();
												skidSound.flush();
												skidSound.setFramePosition(0);
												skidSound.start();
											}
										}
										else {
											if(skirting) {
												if(anim == SKIRT_ANIM) {
													skirtAnim.update(1 /** (dt / (1.0f / 60.0f))*/);
													if(skirtAnim.finished) {
														skirting = false;
														
														if(groundSpeed == 0) {
															anim = IDLE_ANIM;
															idleAnim.reset();
														}
														else {
															anim = RUN_ANIM;
															runSlowestAnim.reset();
															runSlowAnim.reset();
															runNormalAnim.reset();
															runFastAnim.reset();
															runFastestAnim.reset();
														}
														
														facing *= -1;
													}
												}
												else {
													anim = SKIRT_ANIM;
													skirtAnim.reset();
												}
											}
											else {
												if(groundSpeed == 0) {
													anim = IDLE_ANIM;
													idleAnim.reset();
												}
												else {
													anim = RUN_ANIM;
													runSlowestAnim.reset();
													runSlowAnim.reset();
													runNormalAnim.reset();
													runFastAnim.reset();
													runFastestAnim.reset();
												}
											}
										}
									}
									else {
										if(facing == 1 && groundSpeed < 0 && leftArrow || facing == -1 && groundSpeed > 0 && rightArrow || turning) {
											turning = true;
											
											if(anim == TURN_ANIM) {
												turnAnim.update(1 /** (dt / (1.0f / 60.0f))*/);
												if(turnAnim.finished) {
													turning = false;
													
													if(groundSpeed == 0) {
														anim = IDLE_ANIM;
														idleAnim.reset();
													}
													else {
														anim = RUN_ANIM;
														runSlowestAnim.reset();
														runSlowAnim.reset();
														runNormalAnim.reset();
														runFastAnim.reset();
														runFastestAnim.reset();
													}
													
													facing *= -1;
												}
											}
											else {
												anim = TURN_ANIM;
												turnAnim.reset();
											}
										}
										else {
											if(anim == RUN_ANIM) {
												runSlowestAnim.update((abs(groundSpeed) * ANIM_SPEED_SCALE * SCALE + 0.25)/* * (dt / (1.0f / 60.0f))*/);
												runSlowAnim.   update((abs(groundSpeed) * ANIM_SPEED_SCALE * SCALE + 0.25)/* * (dt / (1.0f / 60.0f))*/);
												runNormalAnim. update((abs(groundSpeed) * ANIM_SPEED_SCALE * SCALE + 0.25)/* * (dt / (1.0f / 60.0f))*/);
												runFastAnim.   update((abs(groundSpeed) * ANIM_SPEED_SCALE * SCALE + 0.25)/* * (dt / (1.0f / 60.0f))*/);
												runFastestAnim.update((abs(groundSpeed) * ANIM_SPEED_SCALE * SCALE + 0.25)/* * (dt / (1.0f / 60.0f))*/);
												
												stepTimer += min(abs(groundSpeed) * SCALE * STEP_SPEED_SCALE + STEP_SPEED_OFFSET, MAX_STEP_SPEED);
												if(stepTimer >= STEP_SOUND_SPEED) {
													stepTimer = 0;
													
													/*switch(stepIndex) {
														case(0): {
															stepSound0.stop();
															stepSound0.flush();
															stepSound0.setFramePosition(0);
															stepSound0.start();
															break;
														}
														case(1): {
															stepSound1.stop();
															stepSound1.flush();
															stepSound1.setFramePosition(0);
															stepSound1.start();
															break;
														}
														case(2): {
															stepSound2.stop();
															stepSound2.flush();
															stepSound2.setFramePosition(0);
															stepSound2.start();
															break;
														}
														case(3): {
															stepSound3.stop();
															stepSound3.flush();
															stepSound3.setFramePosition(0);
															stepSound3.start();
															break;
														}
														case(4): {
															stepSound4.stop();
															stepSound4.flush();
															stepSound4.setFramePosition(0);
															stepSound4.start();
															break;
														}
													}*/
													
													stepIndex++;
													if(stepIndex == 5) {stepIndex = 0;}
												}
											}
											else {
												anim = RUN_ANIM;
												runSlowestAnim.reset();
												runSlowAnim.reset();
												runNormalAnim.reset();
												runFastAnim.reset();
												runFastestAnim.reset();
											}
										}
									}
								}
							}
							else {
								if(jumping) {
									if(anim != JUMP_ANIM) {
										anim = JUMP_ANIM;
										jumpAnim.reset();
										
										jumpSound0.stop();
										jumpSound0.flush();
										jumpSound0.setFramePosition(0);
										jumpSound0.start();
									}
								}
								else {
									if(bouncing) {
										if(vel.y < 0) {
											if(anim != BOUNCING_UP_ANIM) {
												anim = BOUNCING_UP_ANIM;
												bounceUpAnim.reset();
											}
											else {bounceUpAnim.update(1);}
										}
										else {
											if(anim != BOUNCING_DOWN_ANIM) {
												anim = BOUNCING_DOWN_ANIM;
												bounceDownAnim.reset();
											}
											else {
												bounceDownAnim.update(1);
												if(bounceDownAnim.finished) {bouncing = false;}
											}
										}
									}
								}
								
								if(anim == FALL_ANIM) {fallAnim.update(1 /** (dt / (1.0f / 60.0f))*/);}
								else {
									if(anim != JUMP_ANIM && anim != LAND_ANIM && !bouncing && !landing) {
										anim = FALL_ANIM;
										fallAnim.reset();
									}
								}
								
								if(anim == LAND_ANIM) {landAnim.update(1);}
								
								if(anim == JUMP_ANIM) {
									jumpAnim.update(1 /** (dt / (1.0f / 60.0f))*/);
									
									if(landing && vel.y > 0 && !jumping) {
										anim = LAND_ANIM;
										landAnim.reset();
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private Animation getCurrentAnim() {
		if(anim == RUN_ANIM) {
			     if(abs(groundSpeed) >= FASTEST_MIN_SPEED * SCALE) {return(runFastestAnim);}
			else if(abs(groundSpeed) >= FAST_MIN_SPEED    * SCALE) {return(runFastAnim   );}
			else if(abs(groundSpeed) >= NORMAL_MIN_SPEED  * SCALE) {return(runNormalAnim );}
			else if(abs(groundSpeed) >= SLOW_MIN_SPEED    * SCALE) {return(runSlowAnim   );}
			else                                                   {return(runSlowestAnim);}
		}
		
		if(anim == START_ANIM)           {return(startAnim         );}
		if(anim == IDLE_ANIM)            {return(idleAnim          );}
		if(anim == BOUNCING_UP_ANIM)     {return(bounceUpAnim      );}
		if(anim == BOUNCING_DOWN_ANIM)   {return(bounceDownAnim    );}
		if(anim == FALL_ANIM)            {return(fallAnim          );}
		if(anim == SKID_ANIM)            {return(skidAnim          );}
		if(anim == SKIRT_ANIM)           {return(skirtAnim         );}
		if(anim == TURN_ANIM)            {return(turnAnim          );}
		if(anim == CROUCH_ANIM_0)        {return(crouchAnim0       );}
		if(anim == CROUCH_ANIM_1)        {return(crouchAnim1       );}
		if(anim == SPINDASH_ANIM)        {return(spindashAnim      );}
		if(anim == SPINDASH_CHARGE_ANIM) {return(spindashChargeAnim);}
		if(anim == JUMP_ANIM)            {return(jumpAnim          );}
		if(anim == LAND_ANIM)            {return(landAnim          );}
		if(anim == TRICK_RIGHT_ANIM)     {return(trickRightAnim    );}
		if(anim == TRICK_UP_0_ANIM)      {return(trickUp0Anim      );}
		if(anim == TRICK_UP_1_ANIM)      {return(trickUp1Anim      );}
		if(anim == RAMP_ANIM)            {return(rampAnim          );}
		if(anim == SPIN_ANIM)            {return(spinAnim          );}
		if(anim == SWING_ANIM)           {return(swingAnim         );}
		if(anim == DASH_ANIM)            {return(dashAnim          );}
		
		return(null);
	}
	
	public void draw(float dt, Shader shader, Camera camera) {
//		manageAnimations(dt);
		//manageAnimations(dt); // 30fps only
		
		for(int f = 1; f < min(60.0f / (1.0f / dt), 5); f++) {manageAnimations(dt);}
		
		if(afters != null) {for(int i = 0; i < afters.length; i++) {afters[i].draw(dt, shader, camera);}}
		
		/*if(DRAW_MASKS) {
			Shape temp;
			
			temp = getRotatedCircle(new Vector(Loader.graphicsWidth / 2, Loader.graphicsHeight / 2), GROUND_ANGLE_MASK_RADIUS * SCALE, GROUND_ANGLE_MASK_OFFSET_X * SCALE, GROUND_ANGLE_MASK_OFFSET_Y * SCALE);
			temp.color = Color.CYAN;
			temp.draw(graphics, new Vector());
			
			temp = getRotatedRectangle(new Vector(Loader.graphicsWidth / 2, Loader.graphicsHeight / 2), LEDGE_MASK_L_WIDTH * SCALE, LEDGE_MASK_L_HEIGHT * SCALE, LEDGE_MASK_L_OFFSET_X * SCALE, LEDGE_MASK_L_OFFSET_Y * SCALE);
			temp.color = Color.BLUE;
			temp.draw(graphics, new Vector());
			
			temp = getRotatedRectangle(new Vector(Loader.graphicsWidth / 2, Loader.graphicsHeight / 2), LEDGE_MASK_R_WIDTH * SCALE, LEDGE_MASK_R_HEIGHT * SCALE, LEDGE_MASK_R_OFFSET_X * SCALE, LEDGE_MASK_R_OFFSET_Y * SCALE);
			temp.color = Color.BLUE;
			temp.draw(graphics, new Vector());
			
			temp = getRotatedRectangle(new Vector(Loader.graphicsWidth / 2, Loader.graphicsHeight / 2), GROUND_MASK_WIDTH * SCALE, GROUND_MASK_HEIGHT * SCALE, GROUND_MASK_OFFSET_X * SCALE, GROUND_MASK_OFFSET_Y * SCALE);
			temp.color = Color.PINK;
			temp.draw(graphics, new Vector());
			
			temp = new Circle(MASK_RADIUS * SCALE);
			temp.relocate(new Vector(Loader.graphicsWidth / 2, Loader.graphicsHeight / 2));
			if(ground) {temp.color = Color.GREEN;}
			temp.draw(graphics, new Vector());
		}*/
		if(DRAW_SPRITES) {
			double w = idleAnim.getCurrentSize()[0] * 2;
			double h = idleAnim.getCurrentSize()[1] * 2;
			double t = limitAngle(getAngleOfVector(groundAxis) * -1 - PI / 2);
			double s = -w / 8;
			if(facing == -1) {s = 0;}
			
			if(anim == RUN_ANIM) {
				     if(abs(groundSpeed) >= FASTEST_MIN_SPEED * SCALE) {runFastestAnim.draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
				else if(abs(groundSpeed) >= FAST_MIN_SPEED    * SCALE) {runFastAnim.   draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
				else if(abs(groundSpeed) >= NORMAL_MIN_SPEED  * SCALE) {runNormalAnim. draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
				else if(abs(groundSpeed) >= SLOW_MIN_SPEED    * SCALE) {runSlowAnim.   draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
				else                                                   {runSlowestAnim.draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			}
			
			if(anim == START_ANIM)           {startAnim.         draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == IDLE_ANIM)            {idleAnim.          draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == BOUNCING_UP_ANIM)     {bounceUpAnim.      draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == BOUNCING_DOWN_ANIM)   {bounceDownAnim.    draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == FALL_ANIM)            {fallAnim.          draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == SKID_ANIM)            {skidAnim.          draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == SKIRT_ANIM)           {skirtAnim.         draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == TURN_ANIM)            {turnAnim.          draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == CROUCH_ANIM_0)        {crouchAnim0.       draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == CROUCH_ANIM_1)        {crouchAnim1.       draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == JUMP_ANIM)            {jumpAnim.          draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == LAND_ANIM)            {landAnim.          draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == TRICK_RIGHT_ANIM)     {trickRightAnim.    draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == TRICK_UP_0_ANIM)      {trickUp0Anim.      draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == TRICK_UP_1_ANIM)      {trickUp1Anim.      draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == RAMP_ANIM)            {rampAnim.          draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == DASH_ANIM)            {dashAnim.          draw(pos.x - w / 2, pos.y - h / 2 - 32 + 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			
			if(anim == SWING_ANIM) {swingAnim.draw(pos.x - w / 2 - 32 + 2, pos.y - h / 2 - 32 - 2, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			
			if(anim == SPIN_ANIM) {spinAnim.draw(pos.x - w / 2, pos.y - h / 2 - 32 - 4, pos.x, pos.y, 0, -facing * 2, 2, shader, camera);}
		
			if(anim == SPINDASH_ANIM)                   {spindashAnim.          draw(pos.x - w / 2 + s, pos.y - h / 2 - 32 - 4, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(anim == SPINDASH_CHARGE_ANIM)            {spindashChargeAnim.    draw(pos.x - w / 2 + s, pos.y - h / 2 - 32 - 4, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(dustAnim == REGULAR_DUST_ANIM && ground) {spindashDustAnim.      draw(pos.x - w / 2 + s, pos.y - h / 2 - 32 - 4, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
			if(dustAnim == CHARGE_DUST_ANIM  && ground) {spindashChargeDustAnim.draw(pos.x - w / 2 + s, pos.y - h / 2 - 32 - 4, pos.x, pos.y, t, -facing * 2, 2, shader, camera);}
		}
	}

	public void checkKeys() {
		upArrow = KeyListener.isKeyPressed(GLFW_KEY_UP);
		downArrow = KeyListener.isKeyPressed(GLFW_KEY_DOWN);
		leftArrow = KeyListener.isKeyPressed(GLFW_KEY_LEFT);
		rightArrow = KeyListener.isKeyPressed(GLFW_KEY_RIGHT);
		spaceBar = KeyListener.isKeyPressed(GLFW_KEY_C);
		shiftKey = KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT);
		controlKey = KeyListener.isKeyPressed(GLFW_KEY_X);
		//trickKey = KeyListener.isKeyPressed(GLFW_KEY_Z);
	}
	
	private Shape getRotatedCircle(Vector pos, double radius, double offsetX, double offsetY) {
		Shape groundMask = new Circle(radius);
		groundMask.relocate(pos);
		
		groundMask.translate(groundAxis.normalize().scale(offsetY - radius + MASK_RADIUS * SCALE));
		groundMask.translate(groundAxis.getPerpendicular().normalize().scale(-offsetX));
		
		return(groundMask);
	}
	
	private Shape getRotatedRectangle(Vector pos, double w, double h, double offsetX, double offsetY) {
		Shape groundMask = new Rectangle(w, h);
		groundMask.relocate(pos);
		
		groundMask = rotateRectangle(groundMask, groundAxis);
		groundMask.translate(groundAxis.normalize().scale(offsetY - h / 2 + MASK_RADIUS * SCALE));
		groundMask.translate(groundAxis.getPerpendicular().normalize().scale(-offsetX));
		
		return(groundMask);
	}
	
	private Vector getRotatedVectorComponents(Vector vel, Vector groundAxis) {
		Vector testVel = vel.getNew();
		if(!groundAxis.normalize().checkEqual(new Vector(0, 1))) {
			Vector tempGroundAxis = groundAxis.getPerpendicular().normalize();
			tempGroundAxis.x *= -1;
			if(testVel.getLength() != 0) {
				testVel.rotateAroundPointTo(new Vector(0, 0), tempGroundAxis);
			}
		}
		
		return(testVel);
	}
}
