package datatypes;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import main.Loader;
import rendering.Image;

import static functionholders.GraphicsFunctions.*;

public class Tileset {
	public int tileWidth;
	public int tileHeight;
	public Image image;
	
	public float[][] uvMaps;
	public float[][][] uvMaps2;
	
	public Tileset(ByteBuffer imageBuffer, int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		image = new Image(imageBuffer);
		
		int tilesetWidth = image.getWidth() / tileWidth;
		int tilesetHeight = image.getHeight() / tileHeight;
		uvMaps = new float[tilesetWidth * tilesetHeight][8];
		uvMaps2 = new float[tilesetWidth][tilesetHeight][8];
		
		for(int x = 0; x < tilesetWidth; x++) {
			for(int y = 0; y < tilesetHeight; y++) {
				int t = x + (y * tilesetWidth);
				
				float fx = (float)((1.0f * x * tileWidth) / (1.0f * image.getWidth()));
				float fy = (float)((1.0f * y * tileHeight) / (1.0f * image.getHeight()));
				float fw = (float)((1.0f * tileWidth) / (1.0f * image.getWidth()));
				float fh = (float)((1.0f * tileHeight) / (1.0f * image.getHeight()));
				
				float[] map = new float[]{
					fx + fw, fy + fh,
					fx,      fy,
					fx + fw, fy,
					fx,      fy + fh
				};
				
				uvMaps[t] = map;
				uvMaps2[x][y] = map;
			}
		}
	}
}
