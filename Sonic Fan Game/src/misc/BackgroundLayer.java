package misc;

import static java.lang.Math.*;
import static functionholders.GraphicsFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import datatypes.Tileset;
import main.Loader;
import objects.Player;
import rendering.TileRenderer;

public class BackgroundLayer {
	private final int NONE  = 0;
	private final int COLOR = 1;
	private final int TILE  = 2;
	
	private int index;
	private int size;
	private int tileSize;
	
	private float[] colorTween0;
	private float[] colorTween1;
	private float[] tileTween0;
	private float[] tileTween1;
	
	private int tweenType0;
	private int tweenType1;
	
	private Tileset tileset;
	private TileRenderer[] batches;
	
	public BackgroundLayer(String path, int index, int size, int scale, int tileSize) {
		this.index = index;
		this.tileSize = tileSize * scale;
		this.size = size;
		
		tweenType0 = NONE;
		tweenType1 = NONE;
		
		tileset = new Tileset(Loader.get().loadImage(path), tileSize, tileSize);
		
		int w = tileset.image.getWidth() / tileSize;
		int h = tileset.image.getHeight() / tileSize;
		//tiles = new BufferedImage[w][h];
		
		//for(int x = 0; x < w; x++) {for(int y = 0; y < h; y++) {tiles[x][y] = scaleImage(image.getSubimage(x * tileSize, y * tileSize, tileSize, tileSize), scale);}}
	}
	
	public void setTween(int tween, float[] color) {
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
			tileTween0 = setPositions(x, y, tileSize, tileSize, 1, 1);
		}
		if(tween == 1) {
			tweenType1 = TILE;
			tileTween1 = setPositions(x, y, tileSize, tileSize, 1, 1);
		}
	}
	
	public void draw(int yStart2, int scrollSpeed, Player p, Graphics2D graphics) {
		/*int yStart = yStart2;
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
		}*/
	}
	
	private float[] setPositions(double x, double y, double width, double height, double xScale, double yScale) {
		float[] vertexArray = new float[12];
		
		vertexArray[ 0] = (float)x + (float)(width * xScale);
		vertexArray[ 1] = (float)y;
		vertexArray[ 2] = 0.0f;
		
		vertexArray[ 3] = (float)x;
		vertexArray[ 4] = (float)y + (float)(height * -yScale);
		vertexArray[ 5] = 0.0f;
		
		vertexArray[ 6] = (float)x + (float)(width * xScale);
		vertexArray[ 7] = (float)y + (float)(height * -yScale);
		vertexArray[ 8] = 0.0f;
		
		vertexArray[ 9] = (float)x;
		vertexArray[10] = (float)y;
		vertexArray[11] = 0.0f;
		
		vertexArray[ 1] += (float)(height * yScale);
		vertexArray[ 4] += (float)(height * yScale);
		vertexArray[ 7] += (float)(height * yScale);
		vertexArray[10] += (float)(height * yScale);
		
		if(xScale < 0) {
			vertexArray[ 0] -= (float)(width * xScale);
			vertexArray[ 3] -= (float)(width * xScale);
			vertexArray[ 6] -= (float)(width * xScale);
			vertexArray[ 9] -= (float)(width * xScale);
		}
		if(yScale < 0) {
			vertexArray[ 1] -= (float)(height * yScale);
			vertexArray[ 4] -= (float)(height * yScale);
			vertexArray[ 7] -= (float)(height * yScale);
			vertexArray[10] -= (float)(height * yScale);
		}
		
		return(vertexArray);
	}
}