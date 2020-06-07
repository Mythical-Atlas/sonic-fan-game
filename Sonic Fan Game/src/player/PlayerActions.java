package player;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static player.PlayerConstants.*;

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
		
		if(!p.ground) {
			p.skidding = false;
			p.skirting = false;
			p.turning = false;
		}
		
		if(!p.crouching0 && !p.spindashing) {
			if((!p.spinning || !p.ground) && !p.rampDashing && !p.dashing) { // regular movement
				if(p.leftArrow && !p.rightArrow) {
					if(p.groundSpeed <= 0 || !p.ground) {
						if(!p.ground) {p.facing = -1;}
						if(p.skidding) {
							p.skidding = false;
							if(p.facing == 1) {p.skirting = true;}
						}
						
						if(p.groundSpeed > -GROUND_ACCEL_LIMIT * capScale * SCALE || p.shiftKey) {
							if(p.ground || p.shiftKey) {p.groundSpeed -= moveSpeed * accelScale;}
							if(!p.ground) {p.groundSpeed -= AIR_ACCEL * accelScale;}
							if(p.groundSpeed < -GROUND_ACCEL_LIMIT * capScale * SCALE && !p.shiftKey) {p.groundSpeed = -GROUND_ACCEL_LIMIT * capScale * SCALE;}
						}
					}
					else {
						p.groundSpeed -= SKID_ACCEL * SCALE;
						if(p.groundSpeed >= SKID_MIN_SPEED) {p.skidding = true;}
					}
				}
				if(p.rightArrow && !p.leftArrow) {
					if(p.groundSpeed >= 0 || !p.ground) {
						if(!p.ground) {p.facing = 1;}
						if(p.skidding) {
							p.skidding = false;
							if(p.facing == -1) {p.skirting = true;}
						}
						
						if(p.groundSpeed < GROUND_ACCEL_LIMIT * capScale * SCALE || p.shiftKey) {
							if(p.ground || p.shiftKey) {p.groundSpeed += moveSpeed * accelScale;}
							if(!p.ground) {p.groundSpeed += AIR_ACCEL * accelScale;}
							if(p.groundSpeed > GROUND_ACCEL_LIMIT * capScale * SCALE && !p.shiftKey) {p.groundSpeed = GROUND_ACCEL_LIMIT * capScale * SCALE;}
						}
					}
					else {
						p.groundSpeed += SKID_ACCEL * SCALE;
						if(p.groundSpeed <= -SKID_MIN_SPEED) {p.skidding = true;}
					}
				}
			}
			else { // movement while spinning
				if(p.ground) {
					if(p.leftArrow && !p.rightArrow) {
						if(p.groundSpeed > 0) {
							p.groundSpeed -= SKID_ACCEL * SCALE;
							if(p.groundSpeed <= 0) {
								p.spinning = false;
								p.facing = -1;
							}
						}
						else {p.facing = -1;}
					}
					if(p.rightArrow && !p.leftArrow) {
						if(p.groundSpeed < 0) {
							p.groundSpeed += SKID_ACCEL * SCALE;
							if(p.groundSpeed >= 0) {
								p.spinning = false;
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
			p.skidding = false;
			
			if(!p.spinning && !p.spindashing) { // regular drag
				     if(p.groundSpeed > 0) {p.groundSpeed -= DRAG_DECEL * SCALE;}
				else if(p.groundSpeed < 0) {p.groundSpeed += DRAG_DECEL * SCALE;}
				
				if(p.groundSpeed >= -DRAG_DECEL * SCALE && p.groundSpeed <= DRAG_DECEL * SCALE) {p.groundSpeed = 0;}
			}
			else if(p.spinning) { // spinning drag
			         if(p.groundSpeed > 0) {p.groundSpeed -= SPIN_DECEL * SCALE;}
				else if(p.groundSpeed < 0) {p.groundSpeed += SPIN_DECEL * SCALE;}
				
				if(p.groundSpeed >= -SPIN_DECEL * SCALE && p.groundSpeed <= SPIN_DECEL * SCALE) {
					p.groundSpeed = 0;
					p.spinning = false;
				}
			}
			
			if(p.downArrow && p.groundSpeed != 0) {
				if(!p.spinning) {
					p.spinning = true;
					p.crouching0 = false;
					p.crouching1 = false;
					p.spindashReady = false;
					
					p.spinSound.stop();
					p.spinSound.flush();
					p.spinSound.setFramePosition(0);
					p.spinSound.start();
				}
			}
		}
		
		if(p.spindashing) {
		         if(p.groundSpeed > 0) {p.groundSpeed -= SKID_ACCEL * SCALE;}
			else if(p.groundSpeed < 0) {p.groundSpeed += SKID_ACCEL * SCALE;}
			
			if(p.groundSpeed >= -SKID_ACCEL * SCALE && p.groundSpeed <= SKID_ACCEL * SCALE) {p.groundSpeed = 0;}
		}
	}
	
	public static void jump(Player p) {
		if(p.ground && !p.spaceBar) {p.jumpReady = true;}
		if(!p.ground) {p.jumpReady = false;}
		else {p.jumping = false;}
		
		if(p.jumpReady && p.spaceBar && !p.crouching0 && p.ground && !p.spindashing) {
			p.jumpReady = false;
			p.ground = false;
			p.jumping = true;
			p.jumpSlowing = false;
			p.spinning = false;
			p.helixing = false;
			
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
			if(p.jumping) {
				if(!p.spaceBar || p.jumpSlowing) {
					p.vel.translate(0, JUMP_SWITCH * SCALE);
					p.jumpSlowed += JUMP_SWITCH * SCALE;
					p.jumpSlowing = true;
				}
				
				if(p.jumpSlowed >= 0 && (p.anim == JUMP_ANIM || p.anim == LAND_ANIM)) {p.jumping = false;}
			}
		}
	}
	
	public static void trick(Player p) {
		if(p.spaceBar && p.trickReady) {
			if(p.rightArrow || p.leftArrow) {p.trickType = 1;}
			else if(p.upArrow) {p.trickType = 2;}
			
			if(p.trickType != 0) {
				p.trickReady = false;
				p.trickReadyReady = false;
				p.stopCam = true;
				p.vel = new Vector();
				p.groundSpeed = 0;
			}
		}
		if((p.bouncing || p.rampDashing) && !p.spaceBar && p.trickReadyReady) {p.trickReady = true;}
	}
	
	public static void spindash(Player p) {
		if(p.spindashReady && p.spaceBar || p.ground && p.controlKey && p.controlKeyReady && !p.spinning) {
			if(!p.spindashing) {
				p.spindashing = true;
				p.helixing = false;
				p.spindashCharge = false;
				p.chargeReady = false;
				p.chargeDustTimer = 45;
				p.spindashStrength = SPINDASH_MIN_STRENGTH * SCALE;
				
				p.spindashChargeSound.stop();
				p.spindashChargeSound.flush();
				p.spindashChargeSound.setFramePosition(0);
				p.spindashChargeSound.start();
			}
		}
		
		if(!p.spindashing && p.spinning && p.controlKey && p.controlKeyReady) {p.spinning = false;}
		
		p.controlKeyReady = !p.controlKey;
		
		if(p.spindashing) {
			if(p.downArrow || p.controlKey) {
				if(p.spaceBar && p.chargeReady) {
					p.chargeDustTimer = 45;
					p.spindashCharge = true;
					p.spindashStrength += SPINDASH_CHARGE_SCALE * SCALE;
					p.spindashStrength = min(p.spindashStrength, SPINDASH_MAX_STRENGTH);
					
					p.spindashChargeSound.stop();
					p.spindashChargeSound.flush();
					p.spindashChargeSound.setFramePosition(0);
					p.spindashChargeSound.start();
				}
				
				if(!p.spaceBar) {p.chargeReady = true;}
				else {p.chargeReady = false;}
			}
			else {
				p.crouching0 = false;
				p.crouching1 = false;
				p.spindashReady = false;
				p.spindashing = false;
				p.spinning = true;
				p.jumpReady = false;
				p.groundSpeed = p.spindashStrength * p.facing;
				
				p.spindashReleaseSound.stop();
				p.spindashReleaseSound.flush();
				p.spindashReleaseSound.setFramePosition(0);
				p.spindashReleaseSound.start();
			}
		}
	}
	
	public static void crouch(Player p) {
		if(p.ground && p.groundSpeed == 0 && p.downArrow && !p.spindashing) {
			p.crouching0 = true;
			p.crouching1 = true;
			p.spindashReady = false;
		}
		if(p.crouching0 && !p.downArrow) {
			p.crouching1 = false;
			p.spindashReady = false;
		}
		
		if(p.crouching0 && p.groundSpeed != 0) {
			p.crouching0 = false;
			p.crouching1 = false;
			p.spindashReady = false;
			p.spinning = true;
			
			p.spinSound.stop();
			p.spinSound.flush();
			p.spinSound.setFramePosition(0);
			p.spinSound.start();
		}
	}

	public static void dash(Player p) {
		if(p.controlKey && p.dashReady && !p.dashing && !p.bouncing && !p.rampDashing && !p.spinning && !p.spindashing && (p.anim == JUMP_ANIM || p.anim == LAND_ANIM)) {
			p.jumping = false;
			p.jumpSlowing = false;
			p.trickReady = false;
			p.trickReadyReady = false;
			p.dashing = true;
			p.jumping = false;
			
			p.groundSpeed += 15 * SCALE * p.facing;
			if(p.groundSpeed > GROUND_ACCEL_LIMIT * SCALE && !p.boostMode) {p.groundSpeed = GROUND_ACCEL_LIMIT * SCALE;}
			if(p.groundSpeed > GROUND_ACCEL_LIMIT * BOOST_LIMIT_SCALE * SCALE && p.boostMode) {p.groundSpeed = GROUND_ACCEL_LIMIT * BOOST_LIMIT_SCALE * SCALE;}
			p.vel.y = 0;
			
			p.dashSound.stop();
			p.dashSound.flush();
			p.dashSound.setFramePosition(0);
			p.dashSound.start();
		}
		p.dashReady = !p.ground && !p.controlKey;
	}
	
	public static void gravity(Player p) {
		double capScale = 1;
		
		if(p.boostMode) {capScale = BOOST_LIMIT_SCALE;}
		
		if(!p.ground) {
			p.vel.translate(0, GRAVITY * SCALE);
			
			if(p.jumping) {p.jumpSlowed += GRAVITY * SCALE;}
			
			if(p.vel.x < -GROUND_ACCEL_LIMIT * capScale * SCALE && !p.shiftKey && !p.spinning) {p.vel.x = -GROUND_ACCEL_LIMIT * capScale * SCALE;}
			if(p.vel.x > GROUND_ACCEL_LIMIT * capScale * SCALE && !p.shiftKey && !p.spinning) {p.vel.x = GROUND_ACCEL_LIMIT * capScale * SCALE;}
		}
		else {
			Vector tempGrav = new Vector(0, SCALE).project(p.groundAxis.getPerpendicular().normalize());
			Vector accelGrav = new Vector(0, GROUND_GRAVITY_ACCEL * SCALE).project(p.groundAxis.getPerpendicular().normalize());
			Vector decelGrav = new Vector(0, GROUND_GRAVITY_DECEL * SCALE).project(p.groundAxis.getPerpendicular().normalize());
			
			if(abs(p.groundSpeed + p.getRotatedVectorComponents(tempGrav, p.groundAxis).x) >= abs(p.groundSpeed)) {tempGrav = accelGrav;}
			else {tempGrav = decelGrav;}
			
			if(tempGrav.getLength() >= MIN_POTENTIAL_GRAVITY) {p.groundSpeed += p.getRotatedVectorComponents(tempGrav, p.groundAxis).x;}
			
			if(p.groundSpeed < -GROUND_ACCEL_LIMIT * capScale * SCALE && !p.shiftKey && !p.spinning) {p.groundSpeed = -GROUND_ACCEL_LIMIT * capScale * SCALE;}
			if(p.groundSpeed > GROUND_ACCEL_LIMIT * capScale * SCALE && !p.shiftKey && !p.spinning) {p.groundSpeed = GROUND_ACCEL_LIMIT * capScale * SCALE;}
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
						
						p.boostSound.stop();
						p.boostSound.flush();
						p.boostSound.setFramePosition(0);
						p.boostSound.start();
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
}
