package player;

public class PlayerConstants {
	public static final double SPRINT_ACCEL 		 	= 2;
	public static final double MOVE_ACCEL  		  		= 0.15;
	public static final double AIR_ACCEL  		  		= 0.15;
	public static final double GROUND_ACCEL_LIMIT  		= 35;
	public static final double BOOST_ACCEL_SCALE	 	= 2;
	public static final double BOOST_LIMIT_SCALE	  	= 1.5;
	public static final double BOOST_START_SPEED	  	= 25;
	public static final double BOOST_STOP_SPEED	  		= 10;
	public static final int    BOOST_TIME			 	= 60 * 3;
	public static final double SKID_ACCEL  		  		= 1;
	public static final double DRAG_DECEL   		  	= 0.108;
	public static final double DEBUG_JUMP_IMPULSE  		= 50;
	public static final double JUMP_IMPULSE 		  	= 25;
	public static final double JUMP_SWITCH  		  	= 2;
	public static final double GRAVITY      		  	= 1;
	public static final double GROUND_GRAVITY_ACCEL 	= 0.6;
	public static final double GROUND_GRAVITY_DECEL 	= 0.4;
	public static final double SPIN_DECEL			  	= 0.025;
	public static final double MIN_POTENTIAL_GRAVITY	= 0.1;
	
	public static final double SLOW_MIN_SPEED 		= 10;
	public static final double NORMAL_MIN_SPEED 	= 20;
	public static final double FAST_MIN_SPEED 		= 30;
	public static final double FASTEST_MIN_SPEED 	= 40;
	public static final double SKID_MIN_SPEED 		= 5;
	
	public static final double SPINDASH_MIN_STRENGTH	= 35;
	public static final double SPINDASH_CHARGE_SCALE 	= 5;
	public static final double SPINDASH_MAX_STRENGTH 	= 50;
	
	public static final double GROUND_ANGLE_MASK_OFFSET_X	= 0;
	public static final double GROUND_ANGLE_MASK_OFFSET_Y  	= 1;
	public static final double GROUND_ANGLE_MASK_RADIUS    	= 50;
	
	public static final double STICK_OFFSET_SCALE	= 1;
	public static final double STICK_MIN_SPEED   	= 10;
	
	public static final double GROUND_MASK_OFFSET_X	= 0;
	public static final double GROUND_MASK_OFFSET_Y	= 1;
	public static final double GROUND_MASK_WIDTH    = 100;
	public static final double GROUND_MASK_HEIGHT   = 50;
	
	public static final double LEDGE_MASK_L_OFFSET_X	= -37.5;
	public static final double LEDGE_MASK_L_OFFSET_Y  	= 50;
	public static final double LEDGE_MASK_L_WIDTH     	= 25;
	public static final double LEDGE_MASK_L_HEIGHT    	= 50;
	
	public static final double LEDGE_MASK_R_OFFSET_X	= 37.5;
	public static final double LEDGE_MASK_R_OFFSET_Y  	= 50;
	public static final double LEDGE_MASK_R_WIDTH     	= 25;
	public static final double LEDGE_MASK_R_HEIGHT    	= 50;
	
	public static final double LAND_MASK_OFFSET_Y	= 100;
	public static final double LAND_MASK_WIDTH    	= 100;
	public static final double LAND_MASK_HEIGHT    	= 100;
	
	public static final double MASK_RADIUS = 50;
	
	public static final double SCALE 				= 0.5;
	public static final int    SPRITE_SCALE 		= 4;
	public static final double ANIM_SPEED_SCALE 	= 0.4;
	public static final double STEP_SOUND_SPEED		= 100;
	public static final double STEP_SPEED_SCALE 	= 1;
	public static final double STEP_SPEED_OFFSET	= 2;
	public static final double MAX_STEP_SPEED 		= 25;
	
	public static boolean       DRAW_MASKS		= false;
	public static final boolean DRAW_SPRITES	= true;
	
	public static final int STATE_DEFAULT				= 0;
	public static final int STATE_JUMPING				= 1;
	public static final int STATE_SPINNING				= 2;
	public static final int STATE_SKIDDING_SLOW			= 3;
	public static final int STATE_SKIDDING_FAST			= 4;
	public static final int STATE_TURNING_SLOW			= 5;
	public static final int STATE_TURNING_FAST			= 6;
	public static final int STATE_CROUCHING_DOWN		= 7;
	public static final int STATE_CROUCHING				= 8;
	public static final int STATE_CROUCHING_UP			= 9;
	public static final int STATE_SPINDASHING			= 10;
	public static final int STATE_BOUNCING				= 12;
	//public static final int STATE_FALLING				= 14;
	public static final int STATE_LANDING				= 15;
	public static final int STATE_STARTING				= 16;
	public static final int STATE_TRICKING_UP			= 17;
	public static final int STATE_TRICKING_FORWARD		= 18;
	public static final int STATE_TRICKING_BACKWARD		= 19;
	public static final int STATE_SMASHING				= 20;
	public static final int STATE_RAMP_DASHING			= 21;
	public static final int STATE_SWINGING				= 22;
	public static final int STATE_DASHING				= 23;
	public static final int STATE_SPRING_POLING			= 24;
	
	public static final int IDLE_ANIM 				= 0;
	public static final int RUN_ANIM 				= 1;
	public static final int FALL_ANIM 				= 2;
	public static final int JUMP_ANIM 				= 3;
	public static final int SKID_ANIM 				= 4;
	public static final int SPIN_ANIM 				= 5;
	public static final int CROUCH_ANIM_0 			= 6;
	public static final int CROUCH_ANIM_1 			= 7;
	public static final int SPINDASH_ANIM 			= 8;
	public static final int SPINDASH_CHARGE_ANIM	= 9;
	public static final int SKIRT_ANIM		 		= 10;
	public static final int TURN_ANIM				= 11;
	public static final int BOUNCING_UP_ANIM		= 12;
	public static final int BOUNCING_DOWN_ANIM		= 13;
	public static final int LAND_ANIM				= 14;
	public static final int START_ANIM				= 15;
	public static final int TRICK_RIGHT_ANIM		= 16;
	public static final int TRICK_UP_0_ANIM			= 17;
	public static final int TRICK_UP_1_ANIM			= 18;
	public static final int RAMP_ANIM				= 19;
	public static final int SWING_ANIM				= 20;
	public static final int DASH_ANIM				= 21;
	
	public static final int NO_DUST_ANIM 		= 0;
	public static final int REGULAR_DUST_ANIM 	= 1;
	public static final int CHARGE_DUST_ANIM 	= 2;
}
