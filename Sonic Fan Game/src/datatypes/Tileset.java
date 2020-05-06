package datatypes;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Loader;

import static functionholders.GraphicsFunctions.*;

public class Tileset {
	public int tileWidth;
	public int tileHeight;
	private BufferedImage image;
	public BufferedImage[] tiles;
	
	public Tileset(String path, int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		try {image = ImageIO.read(getClass().getResourceAsStream(path));}
		catch(Exception e) {e.printStackTrace();}
		
		int tilesetWidth = image.getWidth() / tileWidth;
		int tilesetHeight = image.getHeight() / tileHeight;
		tiles = new BufferedImage[tilesetWidth * tilesetHeight];
		
		for(int x = 0; x < tilesetWidth; x++) {
			for(int y = 0; y < tilesetHeight; y++) {
				int t = x + (y * tilesetWidth);
				tiles[t] = scaleImage(image.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight), 2);
			}
		}
	}
}
