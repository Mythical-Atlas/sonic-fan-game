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

import main.Image;
import main.Loader;

import static functionholders.GraphicsFunctions.*;

public class Tileset {
	public int tileWidth;
	public int tileHeight;
	public Image image;
	public float[][] tiles;
	
	public Tileset(ByteBuffer imageBuffer, int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		image = new Image(imageBuffer);
		
		int tilesetWidth = image.getWidth() / tileWidth;
		int tilesetHeight = image.getHeight() / tileHeight;
		tiles = new float[tilesetWidth * tilesetHeight][8];
		
		for(int x = 0; x < tilesetWidth; x++) {
			for(int y = 0; y < tilesetHeight; y++) {
				int t = x + (y * tilesetWidth);
				
				float fx = (float)((1.0f * x * tileWidth) / image.getWidth());
				float fy = (float)((1.0f * y * tileHeight) / image.getHeight());
				float fw = (float)((1.0f * tileWidth) / image.getWidth());
				float fh = (float)((1.0f * tileHeight) / image.getHeight());
				
				float[] map = new float[]{
					fx + fw, fy + fh,
					fx,      fy,
					fx + fw, fy,
					fx,      fy + fh
				};
				
				/*map = new float[]{
					1, 1,
					fx + fw, fy,
					1, fy,
					fx + fw, 1
				};*/
				
				tiles[t] = map;
			}
		}
	}
}
