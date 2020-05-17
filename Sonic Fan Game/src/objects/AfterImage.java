package objects;

import datatypes.Vector;
import rendering.Camera;
import rendering.Image;
import rendering.Shader;

public class AfterImage {
	public boolean remove;
	
	private Image image;
	
	private double x;
	private double y;
	private double ox;
	private double oy;
	private double angle;
	private double xScale;
	private double yScale;
	
	private int birthDelay;
	private int birthCounter;
	private int lifeTime;
	private int lifeCounter;
	
	private boolean born;
	
	public AfterImage(Image image, double x, double y, double ox, double oy, double angle, double xScale, double yScale, int lifeTime, int birthDelay) {
		this.image = new Image(image.tex);
		this.x = x;
		this.y = y;
		this.ox = ox;
		this.oy = oy;
		this.angle = angle;
		this.xScale = xScale;
		this.yScale = yScale;
		this.lifeTime = lifeTime;
		this.birthDelay = birthDelay;
		
		this.image.setColors(getColors(new float[]{0.5f, 1.0f, 2.0f, 1.0f}));
		if(birthDelay == 0) {born = true;}
	}
	
	public void update(float dt) {
		if(!born) {
			for(int f = 1; f < 60.0f / (1.0f / dt); f++) {
				if(birthCounter >= birthDelay) {born = true;}
				birthCounter++
			}
		}
		else {
			for(int f = 1; f < 60.0f / (1.0f / dt); f++) {
				if(lifeCounter >= lifeTime) {remove = true;}
				lifeCounter++;
				born = false;
				birthCounter = 1;
			}
		}
	}
	
	public void draw(float dt, Shader shader, Camera camera) {if(born) {image.draw(x, y, ox, oy, angle, xScale, yScale, shader, camera);}}
	
	private float[] getColors(float[] color) {
		return(
			new float[]{
				color[0], color[1], color[2], color[3],
				color[0], color[1], color[2], color[3],
				color[0], color[1], color[2], color[3],
				color[0], color[1], color[2], color[3]
			}
		);
	}
}
