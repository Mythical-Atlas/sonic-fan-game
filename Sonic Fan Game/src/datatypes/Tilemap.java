package datatypes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Loader;

public class Tilemap {
	private Tileset[] tilesets;
	public TiledJSON json;
	
	public Tilemap(String mapPath, String tilesetsDir) {
		json = new TiledJSON(mapPath);
		tilesets = new Tileset[json.tilesets.length];
		
		for(int s = 0; s < json.tilesets.length; s++) {tilesets[s] = new Tileset(tilesetsDir + json.tilesets[s] + ".png", json.tileWidth, json.tileHeight);}
	}
	
	public void draw(int layer, Vector offset, int scaleX, int scaleY, Graphics2D graphics) {
		int ox = (int)offset.x;
		int oy = (int)offset.y;
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
					if(
						(int)-ox + x * tw      < Loader.graphicsWidth && (int)-oy + y * th      < Loader.graphicsHeight &&
						(int)-ox + x * tw + tw > 0                    && (int)-oy + y * th + th > 0
					) {graphics.drawImage(tilesets[s].tiles[index], (int)-offset.x + x * tw, (int)-oy + y * th, /*tw, th,*/ null);}
				}
			}
		}
	}
}
