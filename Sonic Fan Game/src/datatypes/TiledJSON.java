package datatypes;

import static java.lang.Math.*;

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
	public int length;
	public int tileWidth;
	public int tileHeight;
	public int[][][] map;
	public int offsets[];
	public String[] tilesets;
	public String[] layers;
	
	public TiledJSON(String jsonPath) {
		JSONObject object = null;
		try {object = (JSONObject)new JSONParser().parse(new InputStreamReader(getClass().getResourceAsStream(jsonPath)));}
		catch (Exception e) {e.printStackTrace();}
		
		tileWidth = (int)(long)object.get("tilewidth");
		tileHeight = (int)(long)object.get("tileheight");
		
		JSONArray firstGids = (JSONArray)object.get("tilesets");
		offsets = new int[firstGids.size()];
		tilesets = new String[firstGids.size()];
		
		for(int i = 0; i < firstGids.size(); i++) {
			JSONObject gid = (JSONObject)firstGids.get(i);
			offsets[i] = (int)(long)gid.get("firstgid");
			tilesets[i] = (String)gid.get("source");
			
			if(tilesets[i].endsWith(".tsx") || tilesets[i].endsWith(".xml")) {tilesets[i] = tilesets[i].substring(0, tilesets[i].length() - 4);}
			else if(tilesets[i].endsWith(".json")) {tilesets[i] = tilesets[i].substring(0, tilesets[i].length() - 5);}
		}
		
		JSONArray layers = (JSONArray)object.get("layers");
		length = layers.size();
		this.layers = new String[length];
		
		int x1 = (int)(long)((JSONObject)layers.get(0)).get("startx");
		int y1 = (int)(long)((JSONObject)layers.get(0)).get("starty");
		int x2 = x1 + (int)(long)((JSONObject)layers.get(0)).get("width");
		int y2 = y1 + (int)(long)((JSONObject)layers.get(0)).get("height");
		
		for(int i = 1; i < length; i++) {
			JSONObject layer = (JSONObject)layers.get(i);
			int tx1 = (int)(long)layer.get("startx");
			int ty1 = (int)(long)layer.get("starty");
			int tx2 = tx1 + (int)(long)layer.get("width");
			int ty2 = ty1 + (int)(long)layer.get("height");
			
			x1 = min(x1, tx1);
			y1 = min(y1, ty1);
			x2 = max(x2, tx2);
			y2 = max(y2, ty2);
		}
		
		width = x2 - x1;
		height = y2 - y1;
		
		map = new int[length][width][height];
		
		for(int l = 0; l < length; l++) {
			JSONObject layer = (JSONObject)layers.get(l);
			this.layers[l] = ((String)layer.get("name"));
			//int lx = (int)(long)layer.get("startx");
			//int ly = (int)(long)layer.get("starty");
			int lw = (int)(long)layer.get("width");
			int lh = (int)(long)layer.get("height");
			
			if(lw * lh > 0) {
				JSONArray chunks = (JSONArray)layer.get("chunks");
				
				for(int i = 0; i < chunks.size(); i++) {
					JSONObject chunk = (JSONObject)chunks.get(i);
					JSONArray data = (JSONArray)chunk.get("data");
					
					int cx = (int)(long)chunk.get("x");
					int cy = (int)(long)chunk.get("y");
			        int cw = (int)(long)chunk.get("width");
					int ch = (int)(long)chunk.get("height");
					
					for(int x = 0; x < cw; x++) {
						for(int y = 0; y < ch; y++) {
							int t = x + (y * cw);
							
							map[l][x + cx - x1][y + cy - y1] = (int)(long)data.get(t);
						}
					}
				}
			}
		}
	}
}
