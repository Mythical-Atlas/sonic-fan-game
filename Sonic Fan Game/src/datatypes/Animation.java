package datatypes;

import static java.lang.Math.*;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import rendering.Camera;
import rendering.Image;
import rendering.Shader;

import static functionholders.GraphicsFunctions.*;

public class Animation {
	public double timer;
	public int frame;
	public int[] durations;
	public int repeatFrame;
	public boolean finished; // only for first cycle
	public Image[] frames;
	
	public Animation() {}
	
	public Animation(ByteBuffer[] imageBuffers, int[] durations, int repeatFrame) {
		this.durations = durations;
		this.repeatFrame = repeatFrame;

		frames = new Image[imageBuffers.length];
		for(int i = 0; i < frames.length; i++) {frames[i] = new Image(imageBuffers[i]);}
	}
	
	public void reset() {
		timer = 0;
		frame = 0;
		finished = false;
	}
	
	public void update(double speed) {
		timer += speed;
		if(timer >= durations[frame]) {
			timer = 0;
			frame++;
			
			if(frame >= frames.length) {
				frame = repeatFrame;
				
				finished = true;
			}
		}
		
		/*System.out.println("timer = " + timer);
		System.out.println("frame = " + frame);
		System.out.println("length = " + frames.length);*/
	}
	
	public int[] getCurrentSize() {return(new int[]{frames[frame].getWidth(), frames[frame].getHeight()});}
	
	public void draw(double x, double y, int xScale, int yScale, Shader shader, Camera camera) {frames[frame].draw(x, y, xScale, yScale, shader, camera);}
	public void draw(double x, double y, double ox, double oy, double angle, double xScale, double yScale, Shader shader, Camera camera) {frames[frame].draw(x, y, ox, oy, angle, xScale, yScale, shader, camera);}
}