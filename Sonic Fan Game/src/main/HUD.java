package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import datatypes.Animation;
import objects.Player;

public class HUD {
	public static final int SCALE = 3;
	
	private BufferedImage hud;
	private BufferedImage time;
	private BufferedImage[] numbers;
	
	private Animation ring;
	
	public int rings;
	
	private long start;
	
	public HUD() {
		hud = Loader.hud;
		time = Loader.time;
		numbers = Loader.numbers;
		ring = Loader.hudRingAnim;
		
		start = System.nanoTime();
	}
	
	public void draw(Player p, Graphics2D graphics) {
		graphics.drawImage(hud, 1 * SCALE, 3 * SCALE, null);
		ring.draw(graphics, 7 * SCALE, 8 * SCALE);
		ring.update(p.vel.getLength() / 10 + 1);
	}
}
