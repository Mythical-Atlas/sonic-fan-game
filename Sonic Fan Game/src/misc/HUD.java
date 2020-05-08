package misc;

import static java.lang.Math.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import datatypes.Animation;
import main.Loader;
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
		drawNumber(28 * SCALE, 3 * SCALE, p.rings, 3, graphics);

		long change = System.nanoTime() - start;
		
		int ms = (int)(change / 10000000);
		int s = (int)(ms / 100);
		int m = (int)(s / 60);
		
		graphics.drawImage(time, Loader.graphicsWidth / 2 - time.getWidth() / 2, 3 * SCALE, null);
		drawNumber(Loader.graphicsWidth / 2 - time.getWidth() / 2 +  0 * SCALE, 3 * SCALE, m % 10, 1, graphics);
		drawNumber(Loader.graphicsWidth / 2 - time.getWidth() / 2 + 16 * SCALE, 3 * SCALE, s % 60, 2, graphics);
		drawNumber(Loader.graphicsWidth / 2 - time.getWidth() / 2 + 40 * SCALE, 3 * SCALE, ms % 100, 2, graphics);
	}
	
	private void drawNumber(int x, int y, int num, int places, Graphics2D graphics) {
		for(int i = 0; i < places; i++) {
			int n = (int)floor(num / pow(10, places - i - 1)) % 10;
			graphics.drawImage(numbers[n], x + i * 8 * SCALE, y, null);
		}
	}
}
