package datatypes;

import javax.sound.sampled.*;

import java.io.BufferedInputStream;

public class Sound {
	Clip clip;

	public Sound(String s) {
    	try {
            AudioInputStream dais = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getResourceAsStream(s)));
            
            clip = AudioSystem.getClip();
            clip.open(dais);
        }
        catch(Exception e) {e.printStackTrace();}
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }
}