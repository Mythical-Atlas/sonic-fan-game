package player;

import static java.lang.Math.*;

import static player.PlayerConstants.*;
import static player.PlayerSounds.*;

import datatypes.Vector;

public class PlayerActions {
	public static void movement(Player p) {
		double moveSpeed;
		if(!p.shiftKey) {moveSpeed = MOVE_ACCEL * SCALE;}
		else          {moveSpeed = SPRINT_ACCEL * SCALE;}
		
		double accelScale = 1;
		double capScale = 1;
		
		if(p.boostMode) {
			accelScale = BOOST_ACCEL_SCALE;
			capScale = BOOST_LIMIT_SCALE;
		}
		
		if(!p.ground && (p.state == STATE_SKIDDING_SLOW || p.state == STATE_TURNING_SLOW || p.state == STATE_TURNING_FAST)) {p.state = STATE_DEFAULT;}
		
		if(p.state != STATE_CROUCHING_DOWN && p.state != STATE_CROUCHING && p.state != STATE_CROUCHING_UP && p.state != STATE_SPINDASHING) {
			if((p.state != STATE_SPINNING || !p.ground) && p.state != STATE_RAMP_DASHING && p.state != STATE_DASHING) { // regular movement
				if(p.leftArrow && !p.rightArrow) {
					if(p.groundSpeed <= 0 || !p.ground) {
						if(!p.ground) {p.facing = -1;}
						if(p.state == STATE_SKIDDING_SLOW) {
							if(p.facing == 1) {p.state = STATE_TURNING_FAST;}
							else {p.state = STATE_DEFAULT;}
						}
						
						if(p.groundSpeed > -GROUND_ACCEL_LIMIT * capScale * SCALE || p.shiftKey) {
							if(p.ground || p.shiftKey) {p.groundSpeed -= moveSpeed * accelScale;}
							if(!p.ground) {p.groundSpeed -= AIR_ACCEL * accelScale;}
							if(p.groundSpeed < -GROUND_ACCEL_LIMIT * capScale * SCALE && !p.shiftKey) {p.groundSpeed = -GROUND_ACCEL_LIMIT * capScale * SCALE;}
						}
					}
					else {
						p.groundSpeed -= SKID_ACCEL * SCALE;
						if(p.groundSpeed >= SKID_MIN_SPEED) {p.state = STATE_SKIDDING_SLOW;}
					}
				}
				if(p.rightArrow && !p.leftArrow) {
					if(p.groundSpeed >= 0 || !p.ground) {
						if(!p.ground) {p.facing = 1;}
						if(p.state == STATE_SKIDDING_SLOW) {
							if(p.facing == -1) {p.state = STATE_TURNING_FAST;}
							else {p.state = STATE_DEFAULT;}
						}
						
						if(p.groundSpeed < GROUND_ACCEL_LIMIT * capScale * SCALE || p.shiftKey) {
							if(p.ground || p.shiftKey) {p.groundSpeed += moveSpeed * accelScale;}
							if(!p.ground) {p.groundSpeed += AIR_ACCEL * accelScale;}
							if(p.groundSpeed > GROUND_ACCEL_LIMIT * capScale * SCALE && !p.shiftKey) {p.groundSpeed = GROUND_ACCEL_LIMIT * capScale * SCALE;}
						}
					}
					else {
						p.groundSpeed += SKID_ACCEL * SCALE;
						if(p.groundSpeed <= -SKID_MIN_SPEED) {p.state = STATE_SKIDDING_SLOW;}
					}
				}
			}
			else { // movement while spinning
				if(p.ground) {
					if(p.leftArrow && !p.rightArrow) {
						if(p.groundSpeed > 0) {
							p.groundSpeed -= SKID_ACCEL * SCALE;
							if(p.groundSpeed <= 0) {
								p.state = STATE_DEFAULT;
								p.facing = -1;
							}
						}
						else {p.facing = -1;}
					}
					if(p.rightArrow && !p.leftArrow) {
						if(p.groundSpeed < 0) {
							p.groundSpeed += SKID_ACCEL * SCALE;
							if(p.groundSpeed >= 0) {
								p.state = STATE_DEFAULT;
								p.facing = 1;
							}
						}
						else {p.facing = 1;}
					}
				}
				else {
					if(p.leftArrow && !p.rightArrow) {
						p.groundSpeed -= MOVE_ACCEL * SCALE;
						if(p.groundSpeed < 0) {p.facing = -1;}
					}
					if(p.rightArrow && !p.leftArrow) {
						p.groundSpeed += MOVE_ACCEL * SCALE;
						if(p.groundSpeed > 0) {p.facing = 1;}
					}
				}
			}
		}
	}
	
	public static void drag(Player p) {
		if(!p.leftArrow && !p.rightArrow && p.ground || p.leftArrow && p.rightArrow && p.ground) {
			if(p.state == STATE_SKIDDING_SLOW) {p.state = STATE_DEFAULT;}
			
			if(p.state != STATE_SPINNING && p.state != STATE_SPINDASHING) { // regular drag
				     if(p.groundSpeed > 0) {p.groundSpeed -= DRAG_DECEL * SCALE;}
				else if(p.groundSpeed < 0) {p.groundSpeed += DRAG_DECEL * SCALE;}
				
				if(p.groundSpeed >= -DRAG_DECEL * SCALE && p.groundSpeed <= DRAG_DECEL * SCALE) {p.groundSpeed = 0;}
			}
			else if(p.state == STATE_SPINNING) { // spinning drag
			         if(p.groundSpeed > 0) {p.groundSpeed -= SPIN_DECEL * SCALE;}
				else if(p.groundSpeed < 0) {p.groundSpeed += SPIN_DECEL * SCALE;}
				
				if(p.groundSpeed >= -SPIN_DECEL * SCALE && p.groundSpeed <= SPIN_DECEL * SCALE) {
					p.groundSpeed = 0;
					p.state = STATE_DEFAULT;
				}
			}
			
			if(p.downArrow && p.groundSpeed != 0) {
				if(p.state != STATE_SPINNING) {
					p.state = STATE_SPINNING;
					p.spindashReady = false;
					
					p.ps.playSound(SOUND_SPIN);
				}
			}
		}
		
		if(p.state == STATE_SPINDASHING) {
		         if(p.groundSpeed > 0) {p.groundSpeed -= SKID_ACCEL * SCALE;}
			else if(p.groundSpeed < 0) {p.groundSpeed += SKID_ACCEL * SCALE;}
			
			if(p.groundSpeed >= -SKID_ACCEL * SCALE && p.groundSpeed <= SKID_ACCEL * SCALE) {p.groundSpeed = 0;}
		}
	}
	
	public static void jump(Player p) {
		if(p.ground && !p.spaceBar) {p.jumpReady = true;}
		if(!p.ground) {p.jumpReady = false;}
		else {p.jumpingUp = false;}
		
		if(p.jumpReady && p.spaceBar && p.state != STATE_CROUCHING_UP && p.state != STATE_CROUCHING && p.state != STATE_CROUCHING_DOWN && p.ground && p.state != STATE_SPINDASHING) {
			p.jumpReady = false;
			p.ground = false;
			p.jumpingUp = true;
			p.jumpSlowing = false;
			p.helixing = false;
			p.state = STATE_JUMPING;
			
			if(!p.shiftKey) {
				p.jumpSlowed = p.groundAxis.y * -JUMP_IMPULSE * SCALE;
				p.vel.translate(p.groundAxis.scale(-JUMP_IMPULSE * SCALE));
			}
			else {
				p.jumpSlowed = p.groundAxis.y * -DEBUG_JUMP_IMPULSE * SCALE;
				p.vel.translate(p.groundAxis.scale(-DEBUG_JUMP_IMPULSE * SCALE));
			}
		}
		else {
			if(p.jumpingUp) {
				if(!p.spaceBar || p.jumpSlowing) {
					p.vel.translate(0, JUMP_SWITCH * SCALE);
					p.jumpSlowed += JUMP_SWITCH * SCALE;
					p.jumpSlowing = true;
				}
				
				if(p.jumpSlowed >= 0 && (p.anim == JUMP_ANIM || p.anim == LAND_ANIM)) {p.jumpingUp = false;}
			}
			else {p.jumpSlowing = false;}
		}
	}
	
	public static void trick(Player p) {
		if(p.spaceBar && p.trickReady) {
			if(p.rightArrow && p.facing == 1 || p.leftArrow && p.facing == -1) {p.state = STATE_TRICKING_FORWARD;}
			else if(p.upArrow) {p.state = STATE_TRICKING_UP;}
			
			if(p.state == STATE_TRICKING_FORWARD || p.state == STATE_TRICKING_UP) {
				p.trickReady = false;
				p.trickReadyReady = false;
				p.stopCam = true;
				p.vel = new Vector();
				p.groundSpeed = 0;
			}
		}
		if((p.state == STATE_BOUNCING || p.state == STATE_RAMP_DASHING) && !p.spaceBar && p.trickReadyReady) {p.trickReady = true;}
	}
	
	public static void spindash(Player p) {
		if(p.spindashReady && p.spaceBar || p.ground && p.controlKey && p.controlKeyReady && p.state != STATE_SPINNING) {
			if(p.state != STATE_SPINDASHING) {
				p.state = STATE_SPINDASHING;
				p.helixing = false;
				p.spindashCharge = false;
				p.chargeReady = false;
				p.chargeDustTimer = 45;
				p.spindashStrength = SPINDASH_MIN_STRENGTH * SCALE;
				
				p.ps.playSound(SOUND_SPINDASH_CHARGE);
			}
		}
		
		if(p.state != STATE_SPINDASHING && p.state == STATE_SPINNING && p.controlKey && p.controlKeyReady) {p.state = STATE_DEFAULT;}
		
		p.controlKeyReady = !p.controlKey;
		
		if(p.state == STATE_SPINDASHING) {
			if(p.downArrow || p.controlKey) {
				if(p.spaceBar && p.chargeReady) {
					p.chargeDustTimer = 45;
					p.spindashCharge = true;
					p.spindashStrength += SPINDASH_CHARGE_SCALE * SCALE;
					p.spindashStrength = min(p.spindashStrength, SPINDASH_MAX_STRENGTH);
					
					p.ps.playSound(SOUND_SPINDASH_CHARGE);
				}
				
				if(!p.spaceBar) {p.chargeReady = true;}
				else {p.chargeReady = false;}
			}
			else {
				p.spindashReady = false;
				p.jumpReady = false;
				p.groundSpeed = p.spindashStrength * p.facing;
				p.state = STATE_SPINNING;
				
				p.ps.playSound(SOUND_SPINDASH_RELEASE);
			}
		}
	}
	
	public static void crouch(Player p) {
		if(p.ground && p.groundSpeed == 0 && p.downArrow && p.state != STATE_SPINDASHING) {
			p.state = STATE_CROUCHING_DOWN;
			p.spindashReady = false;
		}
		if((p.state == STATE_CROUCHING_DOWN || p.state == STATE_CROUCHING_UP) && !p.downArrow) {
			p.state = STATE_CROUCHING_UP;
			p.spindashReady = false;
		}
		
		if((p.state == STATE_CROUCHING_DOWN || p.state == STATE_CROUCHING || p.state == STATE_CROUCHING_UP) && p.groundSpeed != 0) {
			p.spindashReady = false;
			p.state = STATE_SPINNING;
			
			p.ps.playSound(SOUND_SPIN);
		}
	}

	public static void dash(Player p) {
		if(p.controlKey && p.dashReady && p.state == STATE_JUMPING && (p.anim == JUMP_ANIM || p.anim == LAND_ANIM)) {
			p.jumpingUp = false;
			p.trickReady = false;
			p.trickReadyReady = false;
			p.state = STATE_DASHING;
			
			p.groundSpeed += 15 * SCALE * p.facing;
			if(p.groundSpeed > GROUND_ACCEL_LIMIT * SCALE && !p.boostMode) {p.groundSpeed = GROUND_ACCEL_LIMIT * SCALE;}
			if(p.groundSpeed > GROUND_ACCEL_LIMIT * BOOST_LIMIT_SCALE * SCALE && p.boostMode) {p.groundSpeed = GROUND_ACCEL_LIMIT * BOOST_LIMIT_SCALE * SCALE;}
			p.vel.y = 0;
			
			p.ps.playSound(SOUND_DASH);
		}
		p.dashReady = !p.ground && !p.controlKey;
	}
	
	public static void gravity(Player p) {
		double capScale = 1;
		
		if(p.boostMode) {capScale = BOOST_LIMIT_SCALE;}
		
		if(!p.ground) {
			p.vel.translate(0, GRAVITY * SCALE);
			
			if(p.jumpingUp) {p.jumpSlowed += GRAVITY * SCALE;}
			
			if(p.vel.x < -GROUND_ACCEL_LIMIT * capScale * SCALE && !p.shiftKey && p.state != STATE_SPINNING) {p.vel.x = -GROUND_ACCEL_LIMIT * capScale * SCALE;}
			if(p.vel.x > GROUND_ACCEL_LIMIT * capScale * SCALE && !p.shiftKey && p.state != STATE_SPINNING) {p.vel.x = GROUND_ACCEL_LIMIT * capScale * SCALE;}
		}
		else {
			Vector tempGrav = new Vector(0, SCALE).project(p.groundAxis.getPerpendicular().normalize());
			Vector accelGrav = new Vector(0, GROUND_GRAVITY_ACCEL * SCALE).project(p.groundAxis.getPerpendicular().normalize());
			Vector decelGrav = new Vector(0, GROUND_GRAVITY_DECEL * SCALE).project(p.groundAxis.getPerpendicular().normalize());
			
			if(abs(p.groundSpeed + p.getRotatedVectorComponents(tempGrav, p.groundAxis).x) >= abs(p.groundSpeed)) {tempGrav = accelGrav;}
			else {tempGrav = decelGrav;}
			
			if(tempGrav.getLength() >= MIN_POTENTIAL_GRAVITY) {p.groundSpeed += p.getRotatedVectorComponents(tempGrav, p.groundAxis).x;}
			
			if(p.groundSpeed < -GROUND_ACCEL_LIMIT * capScale * SCALE && !p.shiftKey && p.state != STATE_SPINNING) {p.groundSpeed = -GROUND_ACCEL_LIMIT * capScale * SCALE;}
			if(p.groundSpeed > GROUND_ACCEL_LIMIT * capScale * SCALE && !p.shiftKey && p.state != STATE_SPINNING) {p.groundSpeed = GROUND_ACCEL_LIMIT * capScale * SCALE;}
		}
	}
	
	public static void boost(Player p) {
		if(p.ground) {
			if(!p.boostMode) {
				if(abs(p.groundSpeed) >= BOOST_START_SPEED * SCALE) {
					p.boostReady = true;
					p.boostTimer--;
					if(p.boostTimer == 0) {
						p.boostMode = true;
						if(p.groundSpeed > 0) {p.groundSpeed = GROUND_ACCEL_LIMIT * BOOST_LIMIT_SCALE * SCALE;}
						if(p.groundSpeed < 0) {p.groundSpeed = -GROUND_ACCEL_LIMIT * BOOST_LIMIT_SCALE * SCALE;}
						
						p.ps.playSound(SOUND_BOOST);
					}
				}
				else {
					p.boostReady = false;
					p.boostTimer = BOOST_TIME;
				}
			}
			else {
				if(abs(p.groundSpeed) < BOOST_STOP_SPEED) {p.boostMode = false;}
			}
		}
	}
	
	public static void doubleSpin(Player p) {
		if(!p.ground && p.anim == JUMP_ANIM && !p.doubleSpinning) {
			if(p.spaceBar && p.doubleSpinReady) {
				p.state = STATE_SPINNING;
				p.jumpingUp = false;
				p.doubleSpinning = true;
				p.doubleSpinDrawn = true;
				p.doubleShieldDrawn = true;
				p.ps.playSound(SOUND_SPINDASH_RELEASE);
			}
			
			if(!p.spaceBar) {p.doubleSpinReady = true;}
			else {p.doubleSpinReady = false;}
		}
		else {p.doubleSpinReady = false;}
		
		if(p.ground || p.state != STATE_SPINNING) {p.doubleSpinning = false;}
		if(!p.doubleSpinning) {
			p.doubleSpinDrawn = false;
			p.doubleShieldDrawn = false;
		}
	}
}
