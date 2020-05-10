package main;

import static java.lang.Math.*;
import static functionholders.GraphicsFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import objects.Player;

public class Background {
	private int[] indices;
	private int[] sizes;
	private int scale;
	private int tileSize;
	private BackgroundLayer[] layers;
	
	public Background(String[] paths, int[] indices, int[] sizes, int scale, int tileSize) {
		this.indices = indices;
		this.sizes = sizes;
		this.scale = scale;
		this.tileSize = tileSize * scale;
		
		layers = new BackgroundLayer[paths.length];
		for(int i = 0; i < paths.length; i++) {layers[i] = new BackgroundLayer(paths[i], indices[i], sizes[i], scale, tileSize);}
	}
	
	public void setTween(int layer, int tween, Color color) {layers[layer].setTween(tween, color);}
	public void setTween(int layer, int tween, int x, int y) {layers[layer].setTween(tween, x, y);}
	
	public void draw(int[] scrollSpeeds, Player p, Graphics2D graphics) {
		int screenWidth = Loader.graphicsWidth / tileSize + 1;
		int screenHeight = Loader.graphicsHeight / tileSize + 1;
		
		int[] actualIndices = new int[3];
		actualIndices[0] = 2;
		actualIndices[1] = 4;
		actualIndices[2] = 10;
		
		for(int l = 0; l < layers.length; l++) {
			layers[l].draw(actualIndices[l], scrollSpeeds[l], p, graphics);
		}
	}
}