package misc;

import static java.lang.Math.*;
import static functionholders.GraphicsFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import Player.Player;
import main.Loader;
import rendering.Camera;
import rendering.Renderer;

public class Background {
	private int[] indices;
	private int[] sizes;
	private int scale;
	private int tileSize;
	private BackgroundLayer[] layers;
	
	public Background(ByteBuffer[] images, int[] indices, int[] sizes, int scale, int tileSize) {
		this.indices = indices;
		this.sizes = sizes;
		this.scale = scale;
		this.tileSize = tileSize;
		
		layers = new BackgroundLayer[images.length];
		for(int i = 0; i < images.length; i++) {layers[i] = new BackgroundLayer(images[i], indices[i], sizes[i], scale, tileSize);}
	}
	
	public void setTween(int layer, int tween, float[] color) {layers[layer].setTween(tween, color);}
	public void setTween(int layer, int tween, int x, int y) {layers[layer].setTween(tween, x, y);}
	
	public void draw(int[] scrollSpeeds, Camera cam, Renderer r) {
		//int screenWidth = Loader.graphicsWidth / tileSize + 1;
		//int screenHeight = Loader.graphicsHeight / tileSize + 1;
		
		int[] actualIndices = new int[3];
		actualIndices[0] = (-672 - 600) / 2;
		actualIndices[1] = 150;
		actualIndices[2] = 200;
		
		for(int l = 0; l < layers.length; l++) {
			layers[l].draw(actualIndices[l], scrollSpeeds[l], cam, r);
		}
	}
}