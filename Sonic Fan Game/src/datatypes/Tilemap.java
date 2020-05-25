package datatypes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import main.Loader;
import rendering.Camera;
import rendering.Image;
import rendering.TileRenderBatch;
import rendering.TileRenderer;
import rendering.Shader;
import rendering.SpriteRenderer;

import java.nio.ByteBuffer;

public class Tilemap {
	private Tileset[] tilesets;
	public TiledJSON json;
	
	private TileRenderer[] batches;
	
	public Tilemap(String mapPath, String tilesetsDir) {
		json = new TiledJSON(mapPath);
		tilesets = new Tileset[json.tilesets.length];
		batches = new TileRenderer[json.map.length];
		
		for(int s = 0; s < json.tilesets.length; s++) {tilesets[s] = new Tileset(Loader.get().loadImage(tilesetsDir + "/" + json.tilesets[s] + ".png"), json.tileWidth, json.tileHeight);}
		
		int scaleX = 2;
		int scaleY = 2;
		
		float[] colors = new float[]{
			1.0f, 0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 0.0f, 1.0f
		};
		
		for(int l = 0; l < json.map.length; l++) {
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
						float[] positions = setPositions(x * json.tileWidth * scaleX, y * json.tileHeight * scaleY, json.tileWidth, json.tileHeight, scaleX, scaleY);
						
						if(batches[l] == null) {batches[l] = new TileRenderer(tilesets[s].image.tex);}
						batches[l].add(positions, colors, tilesets[s].uvMaps[index]);
					}
				}
			}
			
			if(batches[l] != null) {batches[l].load();}
		}
	}
	
	public void draw(int layer, int scaleX, int scaleY, Shader shader, Camera camera) {
		int l = layer;
		if(batches[l] != null) {batches[l].draw(shader, camera);}
		
		/*float[] colors = new float[]{
			1.0f, 1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f, 1.0f
		};
		
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
					float[] positions = setPositions(x * json.tileWidth * scaleX, y * json.tileHeight * scaleY, json.tileWidth, json.tileHeight, scaleX, scaleY);
					
					Image image = new Image(tilesets[s].image.tex);
					image.setRawPositions(positions);
					image.setColors(colors);
					image.setUVMap(tilesets[s].uvMaps[index]);
					SpriteRenderer.add(image);
					
					//if(batches[l] == null) {batches[l] = new TileRenderer(tilesets[s].image.tex);}
					//batches[l].add(positions, colors, tilesets[s].uvMaps[index]);
				}
			}
		}*/
	}
	
	public float[] setPositions(double x, double y, double width, double height, double xScale, double yScale) {
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
