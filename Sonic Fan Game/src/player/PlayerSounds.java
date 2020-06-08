package player;

import javax.sound.sampled.Clip;

import main.Loader;

public class PlayerSounds {
	public static final int SOUND_JUMP				= 0;
	public static final int SOUND_SKID				= 1;
	public static final int SOUND_SPIN				= 2;
	public static final int SOUND_SPINDASH_CHARGE	= 3;
	public static final int SOUND_SPINDASH_RELEASE	= 4;
	public static final int SOUND_TRICK				= 5;
	public static final int SOUND_BOOST				= 6;
	public static final int SOUND_DASH				= 7;
	public static final int SOUND_POP				= 8;
	public static final int SOUND_RING				= 9;
	public static final int SOUND_SPRING			= 10;
	public static final int SOUND_SPRING_POLE		= 11;
	public static final int SOUND_VOICE_3			= 12;
	public static final int SOUND_VOICE_2			= 13;
	public static final int SOUND_VOICE_1			= 14;
	public static final int SOUND_VOICE_GO			= 15;
	
	private Clip[] clips;
	
	private boolean[] wasPlaying;
	private boolean paused;
	
	public PlayerSounds() {
		clips = new Clip[]{
			Loader.jumpSound0,
			Loader.skidSound,
			Loader.spinSound,
			Loader.spindashChargeSound,
			Loader.spindashReleaseSound,
			Loader.trickSound,
			Loader.boostSound,
			Loader.dashSound,
			Loader.popSound,
			Loader.ringSound,
			Loader.springSound,
			Loader.springPoleSound,
			Loader.voice3,
			Loader.voice2,
			Loader.voice1,
			Loader.voiceGo
		};
		
		wasPlaying = new boolean[clips.length];
		paused = false;
	}
	
	public void playSound(int sound) {
		clips[sound].stop();
		clips[sound].flush();
		clips[sound].setFramePosition(0);
		clips[sound].start();
	}
	public void stopSound(int sound) {clips[sound].stop();}
	
	public void pauseAllSounds() {
		if(!paused) {
			paused = true;
			for(int i = 0; i < clips.length; i++) {
				if(clips[i].isRunning()) {
					clips[i].stop();
					wasPlaying[i] = true;
				}
				else {wasPlaying[i] = false;}
			}
		}
	}
	public void resumeAllSounds() {
		if(paused) {
			paused = false;
			for(int i = 0; i < clips.length; i++) {
				if(wasPlaying[i]) {clips[i].start();}
			}
		}
	}
}
