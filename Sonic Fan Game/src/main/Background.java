package main;

import static java.lang.Math.*;
import static functionholders.GraphicsFunctions.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import objects.Player;

public class Background {
	private int topX;
	private int topY;
	private int middleX;
	private int middleY;
	private int bottomX;
	private int bottomY;
	private int middleRow;
	
	private BufferedImage image;
	private BufferedImage[][] tiles;
	
	public Background(String path, int scale, int tileSize, int upperMiddleRow, int topX, int topY, int middleX, int middleY, int bottomX, int bottomY) {
		this.topX = topY;
		this.topY = topX;
		this.middleX = middleY;
		this.middleY = middleX;
		this.bottomX = bottomY;
		this.bottomY = bottomX;
		middleRow = upperMiddleRow;
		
		try {image = ImageIO.read(getClass().getResourceAsStream(path));}
		catch(Exception e) {e.printStackTrace();}
		
		int w = image.getWidth() / tileSize;
		int h = image.getHeight() / tileSize;
		tiles = new BufferedImage[w][h];
		
		for(int x = 0; x < w; x++) {for(int y = 0; y < h; y++) {tiles[x][y] = scaleImage(image.getSubimage(x * tileSize, y * tileSize, tileSize, tileSize), scale);}}
	}
	
	public void draw(int scrollSpeed, Player p, Graphics2D graphics) {
		int screenWidth = Loader.graphicsWidth / tiles[0][0].getWidth() + 1;
		int screenHeight = Loader.graphicsHeight / tiles[0][0].getHeight() + 1;
		int topSize = middleRow + 1;
		int bottomSize = tiles[0].length - topSize;
		int remainingSize = screenHeight - tiles[0].length;
		int size0 = remainingSize / 3;
		int size1 = remainingSize / 3 + remainingSize % 3;
		int size2 = remainingSize / 3;
		
		int topTop = size0;
		int bottomTop = size0 + topSize + size1;
		
		double xOffset = (p.pos.x / scrollSpeed) % (tiles.length * tiles[0][0].getWidth());
		
		for(int i = 0; i < screenWidth / tiles.length + 3; i++) {
			for(int x = 0; x < tiles.length; x++) {
				for(int y = 0; y < tiles[x].length; y++) {
					if(y <= middleRow) {graphics.drawImage(tiles[x][y], -(int)xOffset + x * tiles[x][y].getWidth() + i * tiles.length * tiles[topX][topY].getWidth(), (y + topTop) * tiles[x][y].getHeight(), null);}
					else               {graphics.drawImage(tiles[x][y], -(int)xOffset + x * tiles[x][y].getWidth() + i * tiles.length * tiles[topX][topY].getWidth(), (y + - (middleRow) + bottomTop) * tiles[x][y].getHeight(), null);}
				}
			}
			
			for(int x = 0; x < tiles.length; x++) {
				for(int y = 0; y < screenHeight; y++) {
					if(y < topTop)                                   {graphics.drawImage(tiles[topX][topY],       -(int)xOffset + x * tiles[topX][topY].getWidth() + i * tiles.length * tiles[topX][topY].getWidth(),       y *       tiles[topX][topY].getHeight(), null);}
					else if(y >= topTop + topSize && y <= bottomTop) {graphics.drawImage(tiles[middleX][middleY], -(int)xOffset + x * tiles[middleX][middleY].getWidth() + i * tiles.length * tiles[topX][topY].getWidth(), y * tiles[middleX][middleY].getHeight(), null);}
					else if(y >= bottomTop + bottomSize)             {graphics.drawImage(tiles[bottomX][bottomY], -(int)xOffset + x * tiles[bottomX][bottomY].getWidth() + i * tiles.length * tiles[topX][topY].getWidth(), y * tiles[bottomX][bottomY].getHeight(), null);}
				}
			}
		}
	}
}