package misc;

import static java.lang.Math.*;
import static functionholders.GraphicsFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import main.Loader;
import objects.Player;

public class BackgroundLayer {
	private final int NONE  = 0;
	private final int COLOR = 1;
	private final int TILE  = 2;
	
	private int index;
	private int size;
	private int tileSize;
	private Color colorTween0;
	private Color colorTween1;
	private BufferedImage tileTween0;
	private BufferedImage tileTween1;
	private int tweenType0;
	private int tweenType1;
	
	private ByteBuffer[][] tiles;
	
	public BackgroundLayer(String path, int index, int size, int scale, int tileSize) {
		this.index = index;
		this.tileSize = tileSize * scale;
		this.size = size;
		
		tweenType0 = NONE;
		tweenType1 = NONE;
		
		try {
			BufferedImage image = ImageIO.read(getClass().getResourceAsStream(path));
			
			int w = image.getWidth() / tileSize;
			int h = image.getHeight() / tileSize;
			//tiles = new BufferedImage[w][h];
			
			//for(int x = 0; x < w; x++) {for(int y = 0; y < h; y++) {tiles[x][y] = scaleImage(image.getSubimage(x * tileSize, y * tileSize, tileSize, tileSize), scale);}}
		}
		catch(Exception e) {e.printStackTrace();}
	}
	
	public void setTween(int tween, Color color) {
		if(tween == 0) {
			tweenType0 = COLOR;
			colorTween0 = color;
		}
		if(tween == 1) {
			tweenType1 = COLOR;
			colorTween1 = color;
		}
	}
	public void setTween(int tween, int x, int y) {
		if(tween == 0) {
			tweenType0 = TILE;
			//tileTween0 = tiles[x][y];
		}
		if(tween == 1) {
			tweenType1 = TILE;
			//tileTween1 = tiles[x][y];
		}
	}
	
	public void draw(int yStart2, int scrollSpeed, Player p, Graphics2D graphics) {
		int yStart = yStart2;
		if(yStart < 0) {yStart = index;}
		
		int screenWidth = Loader.graphicsWidth / tileSize + 1;
		int screenHeight = Loader.graphicsHeight / tileSize + 1;
		
		if(tweenType0 == COLOR && yStart > 0) {
			graphics.setColor(colorTween0);
			graphics.fillRect(0, 0, Loader.graphicsWidth, yStart * tileSize);
		}
		if(tweenType1 == COLOR && yStart + size < screenHeight) {
			graphics.setColor(colorTween1);
			graphics.fillRect(0, (yStart + size) * tileSize, Loader.graphicsWidth, (screenHeight - (yStart + size)) * tileSize);
		}
		
		double xOffset = (p.pos.x / scrollSpeed) % tileSize;
		int iOffset = (int)(p.pos.x / scrollSpeed / tileSize);
		
		for(int i = 0; i < screenWidth; i++) {
			int x = (i + iOffset) % tiles.length;
			while(x < 0) {x += tiles.length;}
			
			//for(int y = 0; y < size; y++) {graphics.drawImage(tiles[x][y], -(int)xOffset + i * tiles[x][y].getWidth(), (y + yStart) * tiles[x][y].getHeight(), null);}
			
			if(tweenType0 == TILE && yStart > 0)                   {for(int y = 0; y < yStart; y++)                     {graphics.drawImage(tileTween0, -(int)xOffset + i * tileTween0.getWidth(), y * tileTween0.getHeight(), null);}}
			if(tweenType1 == TILE && yStart + size < screenHeight) {for(int y = (yStart + size); y < screenHeight; y++) {graphics.drawImage(tileTween1, -(int)xOffset + i * tileTween1.getWidth(), y * tileTween1.getHeight(), null);}}
		}
	}
}