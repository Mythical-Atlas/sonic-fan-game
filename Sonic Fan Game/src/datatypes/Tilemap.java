package datatypes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Loader;
import main.TiledJSONReader;

public class Tilemap {
	public Shape[] solids;
	private BufferedImage image;
	private BufferedImage testImage;
	private BufferedImage[] tileset;
	public TiledJSON json;
	
	public Tilemap(String mapPath, String tilesetPath, String imagePath) {
		json = new TiledJSON(mapPath);
		try {image = ImageIO.read(getClass().getResourceAsStream(tilesetPath));}
		catch(Exception e) {e.printStackTrace();}
		try {testImage = ImageIO.read(getClass().getResourceAsStream(imagePath));}
		catch(Exception e) {e.printStackTrace();}
		
		int tilesetWidth = image.getWidth() / json.tileWidth;
		int tilesetHeight = image.getHeight() / json.tileHeight;
		tileset = new BufferedImage[tilesetWidth * tilesetHeight];
		
		for(int x = 0; x < tilesetWidth; x++) {
			for(int y = 0; y < tilesetHeight; y++) {
				int t = x + (y * tilesetWidth);
				tileset[t] = image.getSubimage(x * json.tileWidth, y * json.tileHeight, json.tileWidth, json.tileHeight);
			}
		}
	}
	
	public void draw(Vector offset, int scaleX, int scaleY, Graphics2D graphics) {
		for(int x = 0; x < json.map.length; x++) {
			for(int y = 0; y < json.map[x].length; y++) {
				if(json.map[x][y] > -1) {
					if((int)-offset.x + x * json.tileWidth * scaleX < Loader.graphicsWidth        && (int)-offset.y + y * json.tileHeight * scaleY < Loader.graphicsHeight &&
					   (int)-offset.x + x * json.tileWidth * scaleX + json.tileWidth * scaleX > 0 && (int)-offset.y + y * json.tileHeight * scaleY + json.tileHeight * scaleY > 0) {
						graphics.drawImage(tileset[json.map[x][y]], (int)-offset.x + x * json.tileWidth * scaleX, (int)-offset.y + y * json.tileHeight * scaleY, json.tileWidth * scaleX, json.tileHeight * scaleY, null);
					}
				}
			}
		}
		
		//graphics.drawImage(testImage, (int)-offset.x, (int)-offset.y, json.width * json.tileWidth * scaleX, json.height * json.tileHeight * scaleY, null);
	}
}
