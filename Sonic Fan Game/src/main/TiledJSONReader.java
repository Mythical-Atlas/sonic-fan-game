package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator; 
import java.util.Map; 
  
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;

import datatypes.Shape; 

public class TiledJSONReader {
	/*public static Tile[] readTileMap(InputStream path) throws FileNotFoundException, IOException, ParseException {
		JSONObject object = (JSONObject)new JSONParser().parse(new InputStreamReader(path)); 
		
		JSONArray layersArray = (JSONArray)object.get("layers");
		JSONObject layers = (JSONObject)layersArray.get(0);
        JSONArray data = (JSONArray)layers.get("data");
        
        int w = (int)(long)object.get("width");
		int h = (int)(long)object.get("height");
		int tw = (int)(long)object.get("tilewidth");
		int th = (int)(long)object.get("tileheight");
		
		int[] tilemap = new int[w * h];
        int numTiles = 0;
        
        for(int i = 0; i < w * h; i++) {
        	tilemap[i] = (int)(long)data.get(i);
        	if(tilemap[i] == 2) {numTiles++;}
        }
		
		Tile[] output = new Tile[numTiles];
		int index = 0;
		
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				int i = x + (y * w);
				
				if(tilemap[i] == 2) {
					output[index] = new Tile(x * tw, y * th, tw, th);
					index++;
				}
			}
		}
		
		return(output);
	}*/
	
	/*public static int[] readTileMapInfo(InputStream path) throws FileNotFoundException, IOException, ParseException {
		JSONObject object = (JSONObject)new JSONParser().parse(new InputStreamReader(path));
        
        int w = (int)(long)object.get("width");
		int h = (int)(long)object.get("height");
		
		int tileW = (int)(long)object.get("tilewidth");
		int tileH = (int)(long)object.get("tileheight");
		
		return(new int[]{w, h, tileW, tileH});
	}*/
	
	public static int[][] readTileMap(InputStream path) throws FileNotFoundException, IOException, ParseException {
		JSONObject object = (JSONObject)new JSONParser().parse(new InputStreamReader(path));
		JSONArray layers = (JSONArray)object.get("layers");
		JSONObject layer = (JSONObject)layers.get(0);
		JSONArray chunks = (JSONArray)layer.get("chunks");
		
		int startX = (int)(long)layer.get("startx");
		int startY = (int)(long)layer.get("starty");
		int width = (int)(long)layer.get("width");
		int height = (int)(long)layer.get("height");
		
		int[][] output = new int[width][height];
		
		for(int i = 0; i < chunks.size(); i++) {
			JSONObject chunk = (JSONObject)chunks.get(i);
			JSONArray data = (JSONArray)chunk.get("data");
			
			int offsetX = (int)(long)chunk.get("x");
			int offsetY = (int)(long)chunk.get("y");
	        int w = (int)(long)chunk.get("width");
			int h = (int)(long)chunk.get("height");
			
			for(int y = 0; y < h; y++) {
				for(int x = 0; x < w; x++) {
					int t = x + (y * w);
					output[x + offsetX - startX][y + offsetY - startY] = (int)(long)data.get(t);
				}
			}
		}
		
		return(output);
	}
}
