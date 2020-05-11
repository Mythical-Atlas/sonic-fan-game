package datatypes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import main.Camera;
import main.Loader;
import rendering.Shader;

import java.nio.ByteBuffer;

public class Tilemap {
	private Tileset[] tilesets;
	public TiledJSON json;
	
	public Tilemap(String mapPath, String tilesetsDir) {
		json = new TiledJSON(mapPath);
		tilesets = new Tileset[json.tilesets.length];
		
		for(int s = 0; s < json.tilesets.length; s++) {tilesets[s] = new Tileset(Loader.get().loadImage(tilesetsDir + "/" + json.tilesets[s] + ".png"), json.tileWidth, json.tileHeight);}
	}
	
	public void draw(int layer, int scaleX, int scaleY, Shader shader, Camera camera) {
		int tw = json.tileWidth * scaleX;
		int th = json.tileHeight * scaleY;
		int l = layer;
		
		for(int x = 0; x < json.map[l].length; x++) {
			for(int y = 0; y < json.map[l][x].length; y++) {
				int s = -1;
				int index = 0;
				for(int i = 0; i < json.offsets.length; i++) {
					if(json.map[l][x][y] >= json.offsets[i]) {
						s = i;
						index = json.map[l][x][y] - json.offsets[i];
					}
				}
				
				if(s > -1) {
					System.out.println(tilesets[s].tiles[index][2]);
					tilesets[s].image.setUVMap(tilesets[s].tiles[index]);
					tilesets[s].image.draw(x * tw, y * th, (float)((1.0f * tilesets[s].tileWidth) / tilesets[s].image.getWidth()) * scaleX, (float)((1.0f * tilesets[s].tileHeight) / tilesets[s].image.getHeight()) * scaleY, shader, camera);
				}
			}
		}
	}
}
