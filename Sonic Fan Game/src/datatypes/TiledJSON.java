package datatypes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TiledJSON {
	public int width;
	public int height;
	public int tileWidth;
	public int tileHeight;
	public int[][] map;
	
	private JSONObject object;
	
	public TiledJSON(String jsonPath) {
		object = null;
		try {object = (JSONObject)new JSONParser().parse(new InputStreamReader(getClass().getResourceAsStream(jsonPath)));}
		catch (Exception e) {e.printStackTrace();}
		
		tileWidth = (int)(long)object.get("tilewidth");
		tileHeight = (int)(long)object.get("tileheight");
		
		JSONArray layers = (JSONArray)object.get("layers");
		JSONObject layer = (JSONObject)layers.get(0);
		JSONArray chunks = (JSONArray)layer.get("chunks");
		
		int startX = (int)(long)layer.get("startx");
		int startY = (int)(long)layer.get("starty");
		width = (int)(long)layer.get("width");
		height = (int)(long)layer.get("height");
		
		map = new int[width][height];
		
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
					map[x + offsetX - startX][y + offsetY - startY] = (int)(long)data.get(t) - 1;
				}
			}
		}
	}
}
