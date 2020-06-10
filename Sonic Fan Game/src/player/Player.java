package player;

import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static functionholders.CollisionFunctions.*;
import static functionholders.GeometryFunctions.*;
import static functionholders.ListFunctions.*;
import static functionholders.DebugFunctions.*;
import static functionholders.MathFunctions.*;
import static java.awt.event.KeyEvent.*;

import static player.PlayerActions.*;
import static player.PlayerConstants.*;
import static player.PlayerObjectHandling.*;
import static player.PlayerSounds.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedInputStream;
import java.nio.ByteBuffer;
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
import objects.AfterImage;
import objects.BlueSpring;
import objects.DashPad;
import objects.Helix;
import objects.Item;
import objects.Rail;
import objects.Ramp;
import objects.Ring;
import objects.Rotor;
import objects.Spring;
import objects.SpringPole;
import rendering.Camera;
import rendering.Renderer;
import rendering.Shader;
import shapes.Circle;
import shapes.Rectangle;

public class Player {
	// keys
	public boolean upArrow;
	public boolean downArrow;
	public boolean leftArrow;
	public boolean rightArrow;
	public boolean spaceBar;
	public boolean shiftKey;
	public boolean controlKey;
	//public boolean trickKey;
	
	// flags
	public boolean ground;
	public boolean ledge;
	public boolean jumpReady;
	public boolean jumpSlowing;
	public boolean spindashReady;
	public boolean chargeReady;
	public boolean controlKeyReady;
	public boolean trickReady;
	public boolean trickReadyReady;
	public boolean stopCam;
	public boolean groundFlipped;
	public boolean boostMode;
	public boolean boostReady;
	public boolean justSwang;
	public boolean dashReady;
	public boolean helixing;
	public boolean spindashCharge;
	public boolean jumpingUp;
	public boolean doubleSpinning;
	public boolean doubleSpinDrawn;
	public boolean doubleShieldDrawn;
	public boolean doubleShieldSpriteActive;
	public boolean doubleSpinReady;
	
	public int state;
	
	public double jumpSlowed;
	public double groundSpeed;
	public double spindashStrength;
	public double preSpringPoleXSpeed;
	
	public int boostTimer;
	public int swingStartFrame;
	public int swingDirection;
	public int bounceType;
	public int helixDir;
	
	public double stepTimer;
	public int stepIndex;
	public int chargeDustTimer;
	public int layer;
	
	public Shape mask;
	
	public Rotor rotor;
	public SpringPole springPole;
	public Helix helix;
	
	public Vector pos;
	public Vector vel;
	public Vector groundAxis;
	
	public Animation idleAnim;
	public Animation runSlowestAnim;
	public Animation runSlowAnim;
	public Animation runNormalAnim;
	public Animation runFastAnim;
	public Animation runFastestAnim;
	public Animation bounceUpAnim;
	public Animation bounceDownAnim;
	public Animation fallAnim;
	public Animation jumpAnim;
	public Animation skidAnim;
	public Animation spinAnim;
	public Animation crouchAnim0;
	public Animation crouchAnim1;
	public Animation spindashAnim;
	public Animation spindashChargeAnim;
	public Animation skirtAnim;
	public Animation turnAnim;
	public Animation landAnim;
	public Animation startAnim;
	public Animation trickRightAnim;
	public Animation trickUp0Anim;
	public Animation trickUp1Anim;
	public Animation rampAnim;
	public Animation swingAnim;
	public Animation dashAnim;
	public Animation doubleSpinAnim;
	public Animation slideAnim;
	public Animation smashStartAnim;
	public Animation smashEndAnim;
	public Animation backflipAnim;
	public Animation helixAnim;
	public Animation grindAnim;
	
	public Animation spindashDustAnim;
	public Animation spindashChargeDustAnim;
	public Animation doubleShieldAnim;
	
	public int facing;
	public int anim;
	public int dustAnim;
	public int voice;
	
	public int rings;
	public int score;
	
	public AfterImage[] afters;
	
	public PlayerSounds ps;
	
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
		skirtAnim = new Animation(Loader.skirtAnim, new int[]{2, 2, 2, 4}, 0);
		turnAnim = new Animation(Loader.turnAnim, new int[]{1, 3}, 0);
		landAnim = new Animation(Loader.landAnim, new int[]{1, 2, 2, 2}, 1);
		startAnim = new Animation(Loader.startAnim, new int[]{2, 2, 4, 4, 4, 6, 4, 6, 4, 6, 4, 4, 4, 4, 4, 4, 6, 4, 6, 4, 4, 4, 4, 4, 4, 8, 4, 4, 4, 4, 4, 4, 4, 4, 4, 8, 6, 4, 110, 6, 60, 3}, 0);
		rampAnim = new Animation(Loader.rampAnim, new int[]{1, 2, 2, 2}, 1);
		swingAnim = new Animation(Loader.sonicRotorAnim, new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, 0); // not techincally accurate, it sometimes switches to 3 frames, not sure when
		dashAnim = new Animation(Loader.dashAnim, new int[]{2, 2, 2, 2, 2, 2, 2}, 4);
		doubleSpinAnim = new Animation(Loader.doubleSpinAnim, new int[]{1, 1, 1, 1, 1, 1, 1, 1}, 0);
		slideAnim = new Animation(Loader.slideAnim, new int[]{2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2}, 11);
		smashStartAnim = new Animation(Loader.smashStartAnim, new int[]{3, 3, 6, 2, 2, 2, 2, 3}, 0);
		smashEndAnim = new Animation(Loader.smashEndAnim, new int[]{2, 2, 2, 2}, 0);
		backflipAnim = new Animation(Loader.backflipAnim, new int[]{7, 4, 3, 2, 2, 2, 2, 2, 3, 3, 3}, 8);
		helixAnim = new Animation(Loader.slideAnim, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 0);
		grindAnim = new Animation(Loader.grindAnim, new int[]{2, 2}, 0);
		
		spindashDustAnim = new Animation(Loader.spindashDustAnim, new int[]{2, 2, 2, 2, 2, 2, 2, 2}, 0);
		spindashChargeDustAnim = new Animation(Loader.spindashChargeDustAnim, new int[]{2, 2, 2, 2, 2, 2, 2, 2}, 0);
		doubleShieldAnim = new Animation(Loader.doubleShieldAnim, new int[]{2, 2, 2, 2, 2, 2, 2, 3}, 0);
		
		trickRightAnim = new Animation(Loader.trickRightAnim, new int[]{2, 4, 2, 1, 1, 1, 1, 1, 1, 1, 1}, 3);
		trickUp0Anim = new Animation(Loader.trickUp0Anim, new int[]{3, 6, 3, 1, 1, 3, 3, 3, 3}, 5);
		trickUp1Anim = new Animation(Loader.trickUp1Anim, new int[]{3, 3, 3, 2, 2, 2}, 3);
		
		ps = new PlayerSounds();
		
		state = STATE_STARTING;
		ground = true;
		voice = 0;
	}
	
	public void update(float dt, Shape[] layer0, Shape[] layer1, Shape[] layer2, Shape[] layer1Triggers, Shape[] layer2Triggers, Shape[] platforms, Ring[] rings, Spring[] springs, Badnik[] badniks, Item[] items, Ramp[] ramps, Rotor[] rotors, SpringPole[] springPoles, Helix[] helixes, DashPad[] dashPads, Rail[] rails, BlueSpring[] blueSprings) {
		checkKeys();
		
		if(state == STATE_STARTING) {starting();}
		if(state != STATE_STARTING && !stopCam && state != STATE_SPRING_POLING) { // NOT ELSE
			groundSpeed = getRotatedVectorComponents(vel, groundAxis).x;
			vel.translate(groundAxis.getPerpendicular().normalize().scale(groundSpeed));
			
			movement(this);
			drag(this);
			jump(this);
			trick(this);
			spindash(this);
			crouch(this);
			dash(this);
			gravity(this);
			boost(this);
			doubleSpin(this);
		}
		
		if(stopCam || state == STATE_SPRING_POLING) {vel = new Vector();}
		if(helixing) {vel.y = 0;}
		
		boolean[] platMasks = null;
		if(platforms != null) {platMasks = checkPlatforms(platforms);}
		
		vel.translate(groundAxis.getPerpendicular().normalize().scale(-groundSpeed));
		pos.translate(vel);
		
		checkLayer(layer1Triggers, layer2Triggers);
		
		Shape[] shapes = null;
		if(layer == 1) {shapes = combine(layer0, layer1);}
		if(layer == 2) {shapes = combine(layer0, layer2);}
		if(platMasks != null) {shapes = combine(shapes, applyMask(platforms, platMasks));}
		
		if(shapes != null) {
			if(state == STATE_BOUNCING && bounceType == 0 && vel.y < 0 && !ground) {
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
		
		rings(this, rings);
		badniks(this, badniks);
		items(this, items);
		springs(this, springs);
		ramps(this, ramps);
		rotors(this, rotors);
		springPoles(this, springPoles);
		helixes(this, helixes);
		dashPads(this, dashPads);
		rails(this, rails);
		blueSprings(this, blueSprings);

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
				ps.playSound(SOUND_VOICE_3);
			}
			if(startAnim.frame == 38 && startAnim.timer == 59) {
				voice = 2;
				ps.playSound(SOUND_VOICE_2);
			}
			if(startAnim.frame == 40 && startAnim.timer == 3) {
				voice = 1;
				ps.playSound(SOUND_VOICE_1);
			}
			
			startAnim.update(1);
			if(startAnim.finished) {
				voice = 4;
				facing = 1;
				ground = true;
				ledge = false;
				state = STATE_DEFAULT;
				
				vel = new Vector(10, 0);
				
				ps.playSound(SOUND_VOICE_GO);
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
			trickReady = false;
			trickReadyReady = false;
			
			if(downArrow) {
				if(state != STATE_SPINNING && vel.x != 0) {
					state = STATE_SPINNING;
					
					ps.playSound(SOUND_SPIN);
				}
			}
			else {state = STATE_DEFAULT;}
		}
		
	}
	
	private void getGroundAxis(Shape[] shapes) {
		if(ground && !ledge) {
			//Vector oldAxis = new Vector(groundAxis.x, groundAxis.y);
			//double oldAngle = getAngleOfVector(oldAxis);
			
			if(abs(groundSpeed) < STICK_MIN_SPEED * SCALE) {groundAxis = new Vector(0, 1);}
			
			Shape groundMask;
			
			groundMask = getRotatedCircle(pos, GROUND_ANGLE_MASK_RADIUS * SCALE, GROUND_ANGLE_MASK_OFFSET_X * SCALE, GROUND_ANGLE_MASK_OFFSET_Y * SCALE);
			
			groundAxis = new Vector(0, 0);
			groundAxis.translate(clip(groundMask, shapes));
			
			if(groundAxis.getLength() != 0) {groundAxis = groundAxis.scale(-1).normalize();}
			else {groundAxis = new Vector(0, 1);}
			
			//double tempAngle = getAngleOfVector(groundAxis);
			//double tempAngle0 = getAngleOfVector(new Vector(0, 1));
			
			//if(getDistanceBetweenAngles(tempAngle, oldAngle) > PI / 4) {groundAxis = new Vector(oldAxis.x, oldAxis.y);}
		}
		else {groundAxis = new Vector(0, 1);}
	}
	
	private void checkLanding(Shape[] shapes) {
		Shape landMask = getRotatedRectangle(pos, LAND_MASK_WIDTH * SCALE, LAND_MASK_HEIGHT * SCALE, 0, LAND_MASK_OFFSET_Y * SCALE);
		for(int i = 0; i < shapes.length; i++) {
			if(checkCollision(landMask, shapes[i]) && anim == JUMP_ANIM && !ground && vel.y > 0) {
				state = STATE_LANDING;
				break;
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
		if(anim == DOUBLE_SPIN_ANIM)     {return(doubleSpinAnim    );}
		
		return(null);
	}

	public void manageAnimations(float dt) {
		if(state != STATE_STARTING) {
			if(state == STATE_SWINGING) {
				if(anim != SWING_ANIM) {
					anim = SWING_ANIM;
					swingAnim.reset();
					rotor.anim.reset();
					rotor.anim.update(swingDirection);
					rotor.anim.update(swingDirection);
					rotor.anim.update(swingDirection);
					rotor.anim.update(swingDirection);
					
					swingAnim.frame = swingStartFrame;
				}
				else {
					swingAnim.update(swingDirection);
					rotor.anim.update(swingDirection);
				}
			}
			else if(state == STATE_TRICKING_FORWARD) {
				if(anim != TRICK_RIGHT_ANIM) {
					anim = TRICK_RIGHT_ANIM;
					trickRightAnim.reset();
					ps.playSound(SOUND_TRICK);
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
			else if(state == STATE_TRICKING_UP) {
				if(anim != TRICK_UP_0_ANIM && anim != TRICK_UP_1_ANIM) {
					anim = TRICK_UP_0_ANIM;
					trickUp0Anim.reset();
					ps.playSound(SOUND_TRICK);
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
			else if(state == STATE_RAMP_DASHING) {
				if(anim != RAMP_ANIM) {
					anim = RAMP_ANIM;
					rampAnim.reset();
				}
				else {rampAnim.update(1);}
			}
			else if(state == STATE_DASHING) {
				if(anim != DASH_ANIM) {
					anim = DASH_ANIM;
					dashAnim.reset();
				}
				else {dashAnim.update(1);}
			}
			else if(state == STATE_SPRING_POLING) {
				if(anim == JUMP_ANIM) {jumpAnim.update(1);}
				if(anim == SPIN_ANIM) {spinAnim.update(1);}
			}
			else if(state == STATE_SPINDASHING) {
				if(!spindashCharge) {
					if(anim == SPINDASH_CHARGE_ANIM) {
						spindashChargeAnim.update(1);
						if(spindashChargeAnim.finished) {
							anim = SPINDASH_ANIM;
							spindashAnim.reset();
						}
					}
					else if(anim == SPINDASH_ANIM) {spindashAnim.update(1);}
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
					if(dustAnim == REGULAR_DUST_ANIM) {spindashDustAnim.update(1);}
					else {
						dustAnim = REGULAR_DUST_ANIM;
						spindashDustAnim.reset();
					}
				}
				else {
					if(dustAnim == CHARGE_DUST_ANIM) {spindashChargeDustAnim.update(1);}
					else {
						dustAnim = CHARGE_DUST_ANIM;
						spindashChargeDustAnim.reset();
					}
					
					chargeDustTimer--;
				}
			}
			else if(state == STATE_CROUCHING_DOWN) {
				if(anim == CROUCH_ANIM_0) {
					crouchAnim0.update(1);
					if(crouchAnim0.finished) {spindashReady = true;}
				}
				else {
					anim = CROUCH_ANIM_0;
					crouchAnim0.reset();
				}
			}
			else if(state == STATE_CROUCHING_UP) {
				if(anim == CROUCH_ANIM_1) {
					crouchAnim1.update(1);
					if(crouchAnim1.finished) {
						anim = IDLE_ANIM;
						idleAnim.reset();
						state = STATE_DEFAULT;
					}
				}
				else {
					anim = CROUCH_ANIM_1;
					crouchAnim1.reset();
				}
			}
			else if(state == STATE_SPINNING) {
				if(doubleSpinDrawn) {
					if(anim != DOUBLE_SPIN_ANIM) {
						anim = DOUBLE_SPIN_ANIM;
						doubleSpinAnim.reset();
					}
					else {
						doubleSpinAnim.update(1);
						if(doubleSpinAnim.finished) {doubleSpinDrawn = false;}
					}
				}
				if(!doubleSpinDrawn) {
					if(anim == SPIN_ANIM) {spinAnim.update(1);}
					else {
						anim = SPIN_ANIM;
						spinAnim.reset();
					}
				}
				
				if(doubleShieldDrawn) {
					if(!doubleShieldSpriteActive) {
						doubleShieldSpriteActive = true;
						doubleShieldAnim.reset();
					}
					else {
						doubleShieldAnim.update(1);
						if(doubleShieldAnim.finished) {doubleShieldDrawn = false;}
					}
				}
				if(!doubleShieldDrawn) {doubleShieldSpriteActive = false;}
			}
			else {
				if(ground) {
					if(groundSpeed == 0 && state != STATE_TURNING_SLOW && state != STATE_TURNING_FAST && state != STATE_SKIDDING_SLOW && state != STATE_SKIDDING_FAST) {
						if(anim == IDLE_ANIM) {idleAnim.update(1);}
						else {
							anim = IDLE_ANIM;
							idleAnim.reset();
						}
					}
					else {
						if(state == STATE_SKIDDING_SLOW || state == STATE_TURNING_FAST) {
							if(state == STATE_SKIDDING_SLOW) {
								if(anim == SKID_ANIM) {skidAnim.update(1);}
								else {
									anim = SKID_ANIM;
									skidAnim.reset();
									ps.playSound(SOUND_SKID);
								}
							}
							else {
								if(state == STATE_TURNING_FAST) {
									if(anim == SKIRT_ANIM) {
										skirtAnim.update(1);
										if(skirtAnim.finished) {
											state = STATE_DEFAULT;
											
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
						else { // not skidding or skirting
							if(facing == 1 && groundSpeed < 0 && leftArrow || facing == -1 && groundSpeed > 0 && rightArrow || state == STATE_TURNING_SLOW) {
								state = STATE_TURNING_SLOW;
								
								if(anim == TURN_ANIM) {
									turnAnim.update(1);
									if(turnAnim.finished) {
										state = STATE_DEFAULT;
										
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
									runSlowestAnim.update((abs(groundSpeed) * ANIM_SPEED_SCALE * SCALE + 0.25));
									runSlowAnim.   update((abs(groundSpeed) * ANIM_SPEED_SCALE * SCALE + 0.25));
									runNormalAnim. update((abs(groundSpeed) * ANIM_SPEED_SCALE * SCALE + 0.25));
									runFastAnim.   update((abs(groundSpeed) * ANIM_SPEED_SCALE * SCALE + 0.25));
									runFastestAnim.update((abs(groundSpeed) * ANIM_SPEED_SCALE * SCALE + 0.25));
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
				else { // not ground
					if(state == STATE_JUMPING) {
						if(anim != JUMP_ANIM) {
							anim = JUMP_ANIM;
							jumpAnim.reset();
							ps.playSound(SOUND_JUMP);
						}
					}
					else {
						if(state == STATE_BOUNCING && bounceType == 0) {
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
									if(bounceDownAnim.finished) {state = STATE_DEFAULT;}
								}
							}
						}
						else if(state == STATE_BOUNCING && bounceType == 1) {if(anim == SPIN_ANIM) {spinAnim.update(1);}}
					}
					
					if(anim == FALL_ANIM) {fallAnim.update(1);}
					else {
						if(anim != JUMP_ANIM && anim != LAND_ANIM && state != STATE_BOUNCING && state != STATE_LANDING && state != STATE_SPRING_POLING) {
							anim = FALL_ANIM;
							fallAnim.reset();
						}
					}
					
					if(anim == LAND_ANIM) {landAnim.update(1);}
					
					if(anim == JUMP_ANIM) {
						jumpAnim.update(1 /** (dt / (1.0f / 60.0f))*/);
						
						if(state == STATE_LANDING && vel.y > 0 && state != STATE_JUMPING) {
							anim = LAND_ANIM;
							landAnim.reset();
						}
					}
				}
			}
		}
		
		if(state != STATE_SPINDASHING) {dustAnim = NO_DUST_ANIM;}
		if(state != STATE_SPINNING) {doubleShieldSpriteActive = false;}
	}
	
	public void draw(float dt, Renderer r) {
		if(afters != null) {for(int i = 0; i < afters.length; i++) {afters[i].draw(dt, r);}}
		
		if(DRAW_SPRITES) {
			double w = idleAnim.getCurrentSize()[0] * 2;
			double h = idleAnim.getCurrentSize()[1] * 2;
			double t = limitAngle(getAngleOfVector(groundAxis) * -1 - PI / 2);
			double s = -w / 8;
			if(facing == -1) {s = 0;}
			
			if(anim == RUN_ANIM) {
				     if(abs(groundSpeed) >= FASTEST_MIN_SPEED * SCALE) {runFastestAnim.draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
				else if(abs(groundSpeed) >= FAST_MIN_SPEED    * SCALE) {runFastAnim.   draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
				else if(abs(groundSpeed) >= NORMAL_MIN_SPEED  * SCALE) {runNormalAnim. draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
				else if(abs(groundSpeed) >= SLOW_MIN_SPEED    * SCALE) {runSlowAnim.   draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
				else                                                   {runSlowestAnim.draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			}
			
			if(anim == START_ANIM)           {startAnim.         draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == IDLE_ANIM)            {idleAnim.          draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == BOUNCING_UP_ANIM)     {bounceUpAnim.      draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == BOUNCING_DOWN_ANIM)   {bounceDownAnim.    draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == FALL_ANIM)            {fallAnim.          draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == SKID_ANIM)            {skidAnim.          draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == SKIRT_ANIM)           {skirtAnim.         draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == TURN_ANIM)            {turnAnim.          draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == CROUCH_ANIM_0)        {crouchAnim0.       draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == CROUCH_ANIM_1)        {crouchAnim1.       draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == JUMP_ANIM)            {jumpAnim.          draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3 + 8) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == LAND_ANIM)            {landAnim.          draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3 + 8) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == TRICK_RIGHT_ANIM)     {trickRightAnim.    draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == TRICK_UP_0_ANIM)      {trickUp0Anim.      draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == TRICK_UP_1_ANIM)      {trickUp1Anim.      draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == RAMP_ANIM)            {rampAnim.          draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == DASH_ANIM)            {dashAnim.          draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3 + 8) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == DOUBLE_SPIN_ANIM)     {doubleSpinAnim.    draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3 + 8) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			
			if(anim == SWING_ANIM) {swingAnim.draw((pos.x - w / 2 - 32 + 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 - 1) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			
			if(anim == SPIN_ANIM) {spinAnim.draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 - 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, 0, -facing * Loader.scale, Loader.scale, r);}
		
			if(anim == SPINDASH_ANIM)                   {spindashAnim.          draw((pos.x - w / 2 + s) / 2 * Loader.scale, (pos.y - h / 2 - 32 - 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(anim == SPINDASH_CHARGE_ANIM)            {spindashChargeAnim.    draw((pos.x - w / 2 + s) / 2 * Loader.scale, (pos.y - h / 2 - 32 - 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(dustAnim == REGULAR_DUST_ANIM && ground) {spindashDustAnim.      draw((pos.x - w / 2 + s) / 2 * Loader.scale, (pos.y - h / 2 - 32 - 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			if(dustAnim == CHARGE_DUST_ANIM  && ground) {spindashChargeDustAnim.draw((pos.x - w / 2 + s) / 2 * Loader.scale, (pos.y - h / 2 - 32 - 3) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
			
			if(doubleShieldSpriteActive) {doubleShieldAnim.draw((pos.x - w / 2) / 2 * Loader.scale, (pos.y - h / 2 - 32 + 3 + 8) / 2 * Loader.scale + 0, pos.x / 2 * Loader.scale, pos.y / 2 * Loader.scale, t, -facing * Loader.scale, Loader.scale, r);}
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
	
	public Shape getRotatedCircle(Vector pos, double radius, double offsetX, double offsetY) {
		Shape groundMask = new Circle(radius);
		groundMask.relocate(pos);
		
		groundMask.translate(groundAxis.normalize().scale(offsetY - radius + MASK_RADIUS * SCALE));
		groundMask.translate(groundAxis.getPerpendicular().normalize().scale(-offsetX));
		
		return(groundMask);
	}
	
	public Shape getRotatedRectangle(Vector pos, double w, double h, double offsetX, double offsetY) {
		Shape groundMask = new Rectangle(w, h);
		groundMask.relocate(pos);
		
		groundMask = rotateRectangle(groundMask, groundAxis);
		groundMask.translate(groundAxis.normalize().scale(offsetY - h / 2 + MASK_RADIUS * SCALE));
		groundMask.translate(groundAxis.getPerpendicular().normalize().scale(-offsetX));
		
		return(groundMask);
	}
	
	public Vector getRotatedVectorComponents(Vector vel, Vector groundAxis) {
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
