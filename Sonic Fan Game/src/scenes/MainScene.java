package scenes;

import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static java.awt.event.KeyEvent.*;
import static functionholders.CollisionFunctions.*;
import static functionholders.DebugFunctions.*;
import static functionholders.ListFunctions.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.nio.ByteBuffer;

import javax.sound.sampled.Clip;

import org.joml.Vector2f;

import badniks.Badnik;
import badniks.Spinner;
import datatypes.Shape;
import datatypes.State;
import datatypes.TiledJSON;
import datatypes.Tilemap;
import datatypes.Vector;
import main.KeyListener;
import main.Loader;
import main.Window;
import misc.Background;
import misc.HUD;
import objects.Item;
import objects.Player;
import objects.Ring;
import objects.Spring;
import rendering.Camera;
import rendering.Image;
import rendering.Shader;
import rendering.SpriteRenderer;
import shapes.Arc;
import shapes.Circle;
import shapes.InverseArc;
import shapes.Rectangle;
import shapes.Triangle;

public class MainScene extends Scene {
	private final int LEFT_CAMERA_LIMIT = 1440;
	private final int LOWER_CAMERA_LIMIT = 4608;
	
	private final int X_MIN_DISTANCE_SCALE = 32;
	private final int Y_MIN_DISTANCE_SCALE = 32;
	private final int LEAD_DISTANCE_SCALE  = 8;
	private final int SCALE = 2;
	
	private Vector initPlayerPos;
	
	private Player player;
	private Shape[] layer0;
	private Shape[] layer1;
	private Shape[] layer2;
	private Shape[] layer1Triggers;
	private Shape[] layer2Triggers;
	private Shape[] platforms;
	
	private boolean showTileMasks;
	
	private boolean toggle0 = true;
	private boolean toggle1 = true;
	
	private Ring[] rings;
	private Spring[] springs;
	
	private Badnik[] badniks;
	private Item[] items;
	
	private HUD hud;
	
	private Tilemap leafForest1Map;
	
	private Image leafLayer1;
	private Image leafLayer2;
	
	private Shader defaultShader;
	private Shader spriteShader;
	
	private Vector camPos;
	
	private Background leafBG;
	
	private Clip leaf1Music;
	
	public void init() {
		SpriteRenderer.reset();
		
		defaultShader = new Shader("/shaders/default.glsl");
		defaultShader.compile();
		
		spriteShader = new Shader("/shaders/spriteBatch.glsl");
		spriteShader.compile();
		
		camera = new Camera(new Vector2f());
		
		leafForest1Map = new Tilemap("/maps/leaf.json", "/maps");
		
		leafLayer1 = new Image(Loader.leafLayer1);
		leafLayer2 = new Image(Loader.leafLayer2);
		
		leafLayer1.setPositions(0, 0, 2, 2);
		leafLayer2.setPositions(0, 0, 2, 2);
		
		interpretMap(leafForest1Map.json);
		//player = new Player((75 + 16) * 96 * SCALE, (10 + 16) * 96 * SCALE);
		//player.starting = false;
		camPos = new Vector(player.pos.x, player.pos.y);
		initPlayerPos = new Vector(player.pos.x, player.pos.y);
		
		rings = null;
		
		placeRing( 2,  0,  3 + 4 * 0,  9 + 1 * 0, 0, 0);
		placeRing( 2,  0,  3 + 4 * 1,  9 + 1 * 1, 0, 0);
		placeRing( 2,  0,  3 + 4 * 2,  9 + 1 * 2, 0, 0);
		
		placeRing(11,  2,  8 + 3 * 0,  7 + 3 * 0, 0, 0);
		placeRing(11,  2,  8 + 3 * 1,  7 + 3 * 1, 0, 0);
		placeRing(11,  2,  8 + 3 * 2,  7 + 3 * 2, 0, 0);
		
		placeRing(28,  4,  2 + 3 * 0,  0 + 3 * 0, 0, 0);
		placeRing(28,  4,  2 + 3 * 1,  0 + 3 * 1, 0, 0);
		placeRing(28,  4,  2 + 3 * 2,  0 + 3 * 2, 0, 0);
		
		placeRing(29,  4, 12 +  7,   7, 0, 0);
		placeRing(29,  4,       3,   7, 0, 0);
		placeRing(29,  4, 12 +  5,   1, 0, 0);
		placeRing(29,  4,       5,   1, 0, 0);
		placeRing(29,  4, 12 + -1,  -1, 0, 0);
		
		placeRing(24,  6,  9 + 4 * 0,  6 + 4 * 0, 0, 0);
		placeRing(24,  6,  9 + 4 * 1,  6 + 4 * 1, 0, 0);
		placeRing(24,  6,  9 + 4 * 2,  6 + 4 * 2, 0, 0);
		
		placeRing(28,  9,  9 + 4 * 0,  0 + 4 * 0, 0, 0);
		placeRing(28,  9,  9 + 4 * 1,  0 + 4 * 1, 0, 0);
		placeRing(28,  9,  9 + 4 * 2,  0 + 4 * 2, 0, 0);
		
		springs = new Spring[]{
			new Spring( 35 * SCALE * 96 +  8 * 8 * SCALE + 16 * SCALE * 96 - 14 * SCALE,  6 * SCALE * 96 + 16 * SCALE * 96 + 0.5 * SCALE * 96 - 33 * SCALE, PI / 2, 30, 0),
			new Spring( 56 * SCALE * 96 + 10 * 8 * SCALE + 16 * SCALE * 96 - 14 * SCALE, 10 * SCALE * 96 + 16 * SCALE * 96 + 0.5 * SCALE * 96 - 33 * SCALE, PI / 2, 25, 0),
			new Spring( 88 * SCALE * 96 + 10 * 8 * SCALE + 16 * SCALE * 96 - 14 * SCALE, 16 * SCALE * 96 + 16 * SCALE * 96 + 0.5 * SCALE * 96 - 33 * SCALE, PI / 2, 25, 0),
			new Spring(125 * SCALE * 96 + 10 * 8 * SCALE + 16 * SCALE * 96 - 14 * SCALE, 18 * SCALE * 96 + 16 * SCALE * 96 + 1   * SCALE * 96 - 33 * SCALE, PI / 2, 25, 0),
		};
		
		badniks = new Badnik[]{
			new Spinner(15 * 96 * SCALE + 16.5 * SCALE * 96,  3 * 96 * SCALE + 16.5 * SCALE * 96),
			new Spinner(18 * 96 * SCALE + 16.5 * SCALE * 96,  3 * 96 * SCALE + 16.5 * SCALE * 96),
			new Spinner(24 * 96 * SCALE + 16.5 * SCALE * 96,  2 * 96 * SCALE + 16.5 * SCALE * 96),
			new Spinner(34 * 96 * SCALE + 16.5 * SCALE * 96,  7 * 96 * SCALE + 16.5 * SCALE * 96),
			new Spinner(42 * 96 * SCALE + 16.5 * SCALE * 96, 11 * 96 * SCALE + 16.5 * SCALE * 96)
		};
		
		items = new Item[]{
			new Item(19 * 96 * SCALE + 48 * SCALE + 13 * SCALE + 16 * 96 * SCALE, 5 * 96 * SCALE + 96 * SCALE - 29 * SCALE + 16 * 96 * SCALE)
		};
		
		hud = new HUD();
		
		leafBG = new Background(new ByteBuffer[]{Loader.leafBG0, Loader.leafBG1, Loader.leafBG2}, new int[]{0, 5, 2}, new int[]{5, 10, 13}, 2, 16);
		leafBG.setTween(0, 0, new float[]{120.0f / 255.0f, 136.0f / 255.0f, 248.0f / 255.0f, 1});
		leafBG.setTween(0, 1, new float[]{128.0f / 255.0f, 160.0f / 255.0f, 248.0f / 255.0f, 1});
		leafBG.setTween(1, 1, 14, 14);
		leafBG.setTween(2, 1, 14, 14);
		
		leaf1Music = Loader.leaf1Music;
		
		leaf1Music.setFramePosition(0);
		leaf1Music.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	private void reset() {
		player = new Player(initPlayerPos.x, initPlayerPos.y);
		camPos = new Vector(player.pos.x, player.pos.y);
		
		rings = null;
		
		placeRing( 2,  0,  3 + 4 * 0,  9 + 1 * 0, 0, 0);
		placeRing( 2,  0,  3 + 4 * 1,  9 + 1 * 1, 0, 0);
		placeRing( 2,  0,  3 + 4 * 2,  9 + 1 * 2, 0, 0);
		
		placeRing(11,  2,  8 + 3 * 0,  7 + 3 * 0, 0, 0);
		placeRing(11,  2,  8 + 3 * 1,  7 + 3 * 1, 0, 0);
		placeRing(11,  2,  8 + 3 * 2,  7 + 3 * 2, 0, 0);
		
		placeRing(28,  4,  2 + 3 * 0,  0 + 3 * 0, 0, 0);
		placeRing(28,  4,  2 + 3 * 1,  0 + 3 * 1, 0, 0);
		placeRing(28,  4,  2 + 3 * 2,  0 + 3 * 2, 0, 0);
		
		placeRing(29,  4, 12 +  7,   7, 0, 0);
		placeRing(29,  4,       3,   7, 0, 0);
		placeRing(29,  4, 12 +  5,   1, 0, 0);
		placeRing(29,  4,       5,   1, 0, 0);
		placeRing(29,  4, 12 + -1,  -1, 0, 0);
		
		placeRing(24,  6,  9 + 4 * 0,  6 + 4 * 0, 0, 0);
		placeRing(24,  6,  9 + 4 * 1,  6 + 4 * 1, 0, 0);
		placeRing(24,  6,  9 + 4 * 2,  6 + 4 * 2, 0, 0);
		
		placeRing(28,  9,  9 + 4 * 0,  0 + 4 * 0, 0, 0);
		placeRing(28,  9,  9 + 4 * 1,  0 + 4 * 1, 0, 0);
		placeRing(28,  9,  9 + 4 * 2,  0 + 4 * 2, 0, 0);
		
		springs = new Spring[]{
			new Spring( 35 * SCALE * 96 +  8 * 8 * SCALE + 16 * SCALE * 96 - 14 * SCALE,  6 * SCALE * 96 + 16 * SCALE * 96 + 0.5 * SCALE * 96 - 33 * SCALE, PI / 2, 30, 0),
			new Spring( 56 * SCALE * 96 + 10 * 8 * SCALE + 16 * SCALE * 96 - 14 * SCALE, 10 * SCALE * 96 + 16 * SCALE * 96 + 0.5 * SCALE * 96 - 33 * SCALE, PI / 2, 25, 0),
			new Spring( 88 * SCALE * 96 + 10 * 8 * SCALE + 16 * SCALE * 96 - 14 * SCALE, 16 * SCALE * 96 + 16 * SCALE * 96 + 0.5 * SCALE * 96 - 33 * SCALE, PI / 2, 25, 0),
			new Spring(125 * SCALE * 96 + 10 * 8 * SCALE + 16 * SCALE * 96 - 14 * SCALE, 18 * SCALE * 96 + 16 * SCALE * 96 + 1   * SCALE * 96 - 33 * SCALE, PI / 2, 25, 0),
		};
		
		badniks = new Badnik[]{
			new Spinner(15 * 96 * SCALE + 16.5 * SCALE * 96,  3 * 96 * SCALE + 16.5 * SCALE * 96),
			new Spinner(18 * 96 * SCALE + 16.5 * SCALE * 96,  3 * 96 * SCALE + 16.5 * SCALE * 96),
			new Spinner(24 * 96 * SCALE + 16.5 * SCALE * 96,  2 * 96 * SCALE + 16.5 * SCALE * 96),
			new Spinner(34 * 96 * SCALE + 16.5 * SCALE * 96,  7 * 96 * SCALE + 16.5 * SCALE * 96),
			new Spinner(42 * 96 * SCALE + 16.5 * SCALE * 96, 11 * 96 * SCALE + 16.5 * SCALE * 96)
		};
		
		items = new Item[]{
			new Item(19 * 96 * SCALE + 48 * SCALE + 13 * SCALE + 16 * 96 * SCALE, 5 * 96 * SCALE + 96 * SCALE - 29 * SCALE + 16 * 96 * SCALE)
		};
		
		hud = new HUD();
		
		leaf1Music.stop();
		leaf1Music.flush();
		leaf1Music.setFramePosition(0);
		leaf1Music.loop(Clip.LOOP_CONTINUOUSLY);
	}
		
	public void update(float dt) {
		checkKeysPressed();
		checkKeysReleased();
		
		for(int f = 1; f < 60.0f / (1.0f / dt); f++) {
			player.update(dt, layer0, layer1, layer2, layer1Triggers, layer2Triggers, platforms, rings, springs, badniks, items);
			
			removeRings();
			removeBadniks();
			removeItems();
			
			if(!player.stopCam) {moveCamera(dt);}
		}
		
		SpriteRenderer.reset();
		leafBG.draw(new int[]{200, 100, 50}, camera);
		SpriteRenderer.draw(spriteShader, camera);
		
		/*if(showTileMasks) {
			if(layer0 != null) {for(int i = 0; i < layer0.length; i++) {layer0[i].draw(graphics, player.pos.add(-Loader.graphicsWidth / 2, -Loader.graphicsHeight / 2));}}
			if(player.layer == 1 && layer1 != null) {for(int i = 0; i < layer1.length; i++) {layer1[i].draw(graphics, player.pos.add(-Loader.graphicsWidth / 2, -Loader.graphicsHeight / 2));}}
			if(player.layer == 2 && layer2 != null) {for(int i = 0; i < layer2.length; i++) {layer2[i].draw(graphics, player.pos.add(-Loader.graphicsWidth / 2, -Loader.graphicsHeight / 2));}}
			if(platforms != null) {for(int i = 0; i < platforms.length; i++) {platforms[i].draw(graphics, player.pos.add(-Loader.graphicsWidth / 2, -Loader.graphicsHeight / 2));}}
		}*/
		
		//leafLayer1.draw(defaultShader, camera);
		//leafLayer2.draw(defaultShader, camera);
		
		SpriteRenderer.reset();
		
		leafForest1Map.draw(1, SCALE, SCALE, defaultShader, camera);
		
		if(springs != null) {for(int i = 0; i < springs.length; i++) {springs[i].draw(SCALE, SCALE, dt, defaultShader, camera);}}
		if(badniks != null) {for(int i = 0; i < badniks.length; i++) {badniks[i].draw(SCALE, SCALE, dt, defaultShader, camera);}}
		if(rings != null) {for(int i = 0; i < rings.length; i++) {rings[i].draw(SCALE, SCALE, dt, defaultShader, camera);}}
		if(items != null) {for(int i = 0; i < items.length; i++) {items[i].draw(SCALE, SCALE, dt, defaultShader, camera);}}
		
		SpriteRenderer.draw(spriteShader, camera);
		SpriteRenderer.reset();
		
		player.draw(dt, defaultShader, camera);
		
		SpriteRenderer.draw(spriteShader, camera);
		
		if(!showTileMasks) {leafForest1Map.draw(2, SCALE, SCALE, defaultShader, camera);}
		
		SpriteRenderer.reset();
		hud.draw(dt, player, defaultShader, camera);
		SpriteRenderer.draw(spriteShader, camera);
	}
	
	public void checkKeysPressed() {
		if(KeyListener.isKeyPressed(GLFW_KEY_F1) && toggle0) {
			toggle0 = false;
			player.DRAW_MASKS = !player.DRAW_MASKS;
		}
		if(KeyListener.isKeyPressed(GLFW_KEY_F2) && toggle1) {
			toggle1 = false;
			showTileMasks = !showTileMasks;
		}
		if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {glfwSetWindowShouldClose(Window.getWindow(), true);}
		if(KeyListener.isKeyPressed(GLFW_KEY_BACKSPACE)) {reset();}
	}
	public void checkKeysReleased() {
		if(KeyListener.isKeyPressed(GLFW_KEY_F1)) {toggle0 = true;}
		if(KeyListener.isKeyPressed(GLFW_KEY_F2)) {toggle1 = true;}
	}
	
	private void moveCamera(float dt) {
		Vector pos = player.pos;
		double lead = player.groundSpeed * LEAD_DISTANCE_SCALE * (Window.getWidth() / 960);
		
		double x = camPos.x;
		double y = camPos.y;
		
		double xMinDist = Window.getWidth() / X_MIN_DISTANCE_SCALE;
		double yMinDist = (Window.getInitHeight() * 2 - Window.getHeight()) / Y_MIN_DISTANCE_SCALE;
		
		x = moveTowards(x, pos.x + lead, xMinDist, 0.1, dt);
		y = moveTowards(y, pos.y, yMinDist, 0.1, dt);
		
		//x = moveTowards(x, pos.x + lead, 0, 0.1, dt);
		
		camPos.x = x;
		camPos.y = y;
		
		x -= Window.getWidth() / 2;
		y -= (Window.getInitHeight() * 2 - Window.getHeight()) / 2;
		
		camera.position = new Vector2f((float)(int)x, (float)(int)y);
		
		if(camera.position.x < LEFT_CAMERA_LIMIT * SCALE) {camera.position.x = LEFT_CAMERA_LIMIT * SCALE;}
		if(camera.position.y > LOWER_CAMERA_LIMIT * SCALE) {reset();}
	}
	
	private double moveTowards(double value0, double value1, double minDist, double interval, float dt) {
		double out = value0;
		double dist = value1 - value0;
		
		if(dist > minDist) {
			out = value0 + abs(dist * interval)/* * (dt / (1.0f / 60.0f))*/;
			if(value1 - out < minDist) {out = value1 - minDist;}
		}
		if(dist < -minDist) {
			out = value0 - abs(dist * interval)/* * (dt / (1.0f / 60.0f))*/;
			if(value1 - out > -minDist) {out = value1 + minDist;}
		}
		
		return(out);
	}
	
	private void placeRing(double xTile, double yTile, double xRing, double yRing, double xOffset, double yOffset) {rings = append(rings, new Ring(xTile * 96 * SCALE + xRing * 8 * SCALE + xOffset * SCALE + 16 * 96 * SCALE, yTile * 96 * SCALE + yRing * 8 * SCALE + yOffset * SCALE + 16 * 96 * SCALE));}
	
	private void removeRings() {
		if(rings != null) {
			int[] removals = null;
			for(int i = 0; i < rings.length; i++) {if(rings[i].destroy == 3) {removals = append(removals, i);}}
			if(removals != null) {for(int i = 0; i < removals.length; i++) {rings = removeIndex(rings, removals[i]);}}
		}
	}
	
	private void removeBadniks() {
		if(badniks != null) {
			int[] removals = null;
			for(int i = 0; i < badniks.length; i++) {if(badniks[i].destroy == 2) {removals = append(removals, i);}}
			if(removals != null) {for(int i = 0; i < removals.length; i++) {badniks = removeIndex(badniks, removals[i]);}}
		}
	}
	private void removeItems() {
		if(items != null) {
			int[] removals = null;
			for(int i = 0; i < items.length; i++) {if(items[i].destroy == 2) {removals = append(removals, i);}}
			if(removals != null) {for(int i = 0; i < removals.length; i++) {items = removeIndex(items, removals[i]);}}
		}
	}
	
	private void interpretMap(TiledJSON json) {
		for(int tx = 0; tx < json.map[2].length; tx++) {
			for(int ty = 0; ty < json.map[2][tx].length; ty++) {
				int tile = json.map[2][tx][ty] - json.offsets[0];
				int w = json.tileWidth * SCALE;
				int h = json.tileHeight * SCALE;
				int x = tx * w;
				int y = ty * h;
				int s1 = w / 12;
				int s00 = 0;
				int s01 = w / 12;
				int s02 = s1 * 2;
				int s03 = s1 * 3;
				int s04 = s1 * 4;
				int s05 = s1 * 5;
				int s06 = s1 * 6;
				int s07 = s1 * 7;
				int s08 = s1 * 8;
				int s09 = s1 * 9;
				int s10 = s1 * 10;
				int s11 = s1 * 11;
				int s12 = s1 * 12;
				
				if(tile == 8) {player = new Player(x, y + s10 + w / 24);}
			}
		}
		
		layer0 = getShapes(json, 3);
		layer1 = getShapes(json, 4);
		layer2 = getShapes(json, 5);
		layer1Triggers = getShapes(json, 6);
		layer2Triggers = getShapes(json, 7);
		platforms = getShapes(json, 8);
	}
	
	private Shape[] getShapes(TiledJSON json, int layer) {
		Shape[] shapes = null;
		
		for(int tx = 0; tx < json.map[layer].length; tx++) {
			for(int ty = 0; ty < json.map[layer][tx].length; ty++) {
				int tile = json.map[layer][tx][ty] - json.offsets[1];
				int w = json.tileWidth * SCALE;
				int h = json.tileHeight * SCALE;
				int w2 = w / 2;
				int h2 = h / 2;
				int w3 = w / 3;
				int h3 = h / 3;
				int w6 = w / 6;
				int h6 = h / 6;
				int w12 = w / 12;
				int h12 = h / 12;
				int w24 = w / 24;
				int h24 = h / 24;
				int s0 = 0;
				int s1 = w / 12;
				int s2 = s1 * 2;
				int s3 = s1 * 3;
				int s4 = s1 * 4;
				int s5 = s1 * 5;
				int s6 = s1 * 6;
				int s7 = s1 * 7;
				int s8 = s1 * 8;
				int s9 = s1 * 9;
				int s00 = 0;
				int s01 = w / 12;
				int s02 = s1 * 2;
				int s03 = s1 * 3;
				int s04 = s1 * 4;
				int s05 = s1 * 5;
				int s06 = s1 * 6;
				int s07 = s1 * 7;
				int s08 = s1 * 8;
				int s09 = s1 * 9;
				int s10 = s1 * 10;
				int s11 = s1 * 11;
				int s12 = s1 * 12;
				
				int x = tx * w;
				int y = ty * h;
				
				
				if(tile == 0) {shapes = append(shapes, new Rectangle(new Vector(x, y), new Vector(w, h), Color.WHITE));}
				if(tile == 1) {shapes = append(shapes, new Rectangle(new Vector(x, y + s1), new Vector(w, h - s1), Color.WHITE));}
				if(tile == 2) {shapes = append(shapes, new Rectangle(new Vector(x, y + s2), new Vector(w, s10), Color.WHITE));}
				if(tile == 3) {shapes = append(shapes, new Rectangle(new Vector(x, y + s4), new Vector(w, s8), Color.WHITE));}
				if(tile == 4) {shapes = append(shapes, new Rectangle(new Vector(x, y + h2), new Vector(w, s6), Color.WHITE));}
				if(tile == 5) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s07),
						new Vector(x + s12, y + s07),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 6) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s08),
						new Vector(x + s12, y + s08),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				
				if(tile == 8) {shapes = append(shapes, new Rectangle(new Vector(x + w2, y + h2), new Vector(w2, h2), Color.WHITE));}
				if(tile == 9) {shapes = append(shapes, new Rectangle(new Vector(x, y + h2), new Vector(w2, h2), Color.WHITE));}
				
				if(tile == 10) {
					shapes = append(shapes, new Rectangle(new Vector(x, y), new Vector(w2, h), Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{new Vector(x + s6, y + s1), new Vector(x + w, y + s3), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));
				}
				if(tile == 11 || tile == 168) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s3), new Vector(x + w, y + s6), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 12 || tile == 169) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s6), new Vector(x + w, y + s9), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 13 || tile == 170) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s9), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}

				if(tile == 24) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s06), new Vector(x + s12, y + s03), new Vector(x + s12, y + s12), new Vector(x, y + s12)}, Color.WHITE));}
				if(tile == 25) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s03), new Vector(x + s12, y), new Vector(x + s12, y + s12), new Vector(x, y + s12)}, Color.WHITE));}
				if(tile == 45) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w, y + h6), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 46) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h6), new Vector(x + w, y + h3), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 66) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h3), new Vector(x + w, y + h6), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 108) {
					shapes = append(shapes, new Shape(new Vector[]{
							new Vector(x + s00, y + s06),
							new Vector(x + s06, y + s06),
							new Vector(x + s06, y + s12),
							new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Arc(
						new Vector(x + s12 - s01, y + s06),
						PI / 2 - PI / 8,
						PI / 2,
						s06,
						PI / 2 - PI / 16,
						PI / 2,
					Color.WHITE));
				}
				if(tile == 109) {
					Arc a = new Arc(
						new Vector(x - s01, y + s06),
						PI / 2 - PI / 8,
						PI / 2,
						s06,
						PI / 2 - PI / 8,
						PI / 2 - PI / 16,
					Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						a.points[0],
						new Vector(x + s12, y + s12),
						new Vector(a.points[0].x, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}
				if(tile == 246) {
					InverseArc a = new InverseArc(
						new Vector(x + s12 + s01, y + s06),
						PI + PI / 2 - PI / 8,
						PI + PI / 2,
						s03,
						PI + PI / 2 - PI / 8,
						PI + PI / 2 - PI / 16,
					Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						a.points[0],
						new Vector(x + s10, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s10, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s10, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}
				if(tile == 21) {
					InverseArc a = new InverseArc(
							new Vector(x + s01, y + s06),
							PI + PI / 2 - PI / 8,
							PI + PI / 2,
							s03,
							PI + PI / 2 - PI / 16,
							PI + PI / 2,
						Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}
				if(tile == 269) {
					Arc a = new Arc(
						new Vector(x + s05 - 2 * SCALE, y + s06),
						PI / 2 - PI / 4,
						PI / 2,
						s05,
					Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						a.points[0],
						new Vector(x + s11, y + s12),
						new Vector(a.points[0].x, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}
				if(tile == 162) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + s12, y + s06), new Vector(x + s12, y + s12), new Vector(x, y + s12)}, Color.WHITE));}
				if(tile == 7) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s08), new Vector(x + s12, y + s04), new Vector(x + s12, y + s12), new Vector(x, y + s12)}, Color.WHITE));}
				if(tile == 163) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s06), new Vector(x + s12, y + s12), new Vector(x, y + s12)}, Color.WHITE));}
				if(tile == 180) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w - w12, y), new Vector(x + w, y + h12), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 181) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s1), new Vector(x + s11, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 172) {
					InverseArc a = new InverseArc(
							new Vector(x + s05, y + s06),
							PI + PI / 2 - PI / 4,
							PI + PI / 2,
							s03,
						Color.WHITE);
						
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s01),
						a.points[0],
						new Vector(a.points[0].x, y + s06),
						new Vector(x + s00, y + s06)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}
				if(tile == 153) {shapes = append(shapes, new Rectangle(new Vector(x, y), new Vector(s12, s03), Color.WHITE));}
				if(tile == 154) {shapes = append(shapes, new Rectangle(new Vector(x, y + s06), new Vector(s12, s03), Color.WHITE));}
				if(tile == 22) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h), new Vector(x + w, y + s9), new Vector(x + w, y + h)}, Color.WHITE));}
				if(tile == 23) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + s9), new Vector(x + w, y + s6), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 249) {
					Arc a = new Arc(
						new Vector(x + s06 - 2 * SCALE, y + s06),
						PI / 2 - PI / 4,
						PI / 2,
						s06,
					Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						a.points[0],
						new Vector(x + s12, y + s12),
						new Vector(a.points[0].x, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}
				if(tile == 40) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 43) {
					InverseArc a = new InverseArc(
						new Vector(x + s12, y + s12),
						PI + PI / 2 - PI / 4,
						PI + PI / 2,
						s03,
						PI + PI / 2 - PI / 4,
						PI + PI / 2 - PI / 8,
					Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						a.points[0],
						new Vector(a.points[0].x, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}
				if(tile == 44) {
					InverseArc a = new InverseArc(
						new Vector(x + s00, y + s12),
						PI + PI / 2 - PI / 4,
						PI + PI / 2,
						s03,
						PI + PI / 2 - PI / 8,
						PI + PI / 2,
					Color.WHITE);
					shapes = append(shapes, a);
				}
				if(tile == 251) {
					Arc a = new Arc(
						new Vector(x + s06 - 2 * SCALE, y + s00),
						PI / 2 - PI / 4,
						PI / 2,
						s06,
					Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						a.points[0],
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(a.points[0].x, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}
				if(tile == 127) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y + h2), new Vector(x + w2, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 120) {shapes = append(shapes, new Shape(new Vector[]{new Vector(x, y), new Vector(x + w2, y), new Vector(x + w, y + h2), new Vector(x + w, y + h), new Vector(x, y + h)}, Color.WHITE));}
				if(tile == 200) {
					InverseArc a = new InverseArc(
						new Vector(x + s09 + s01 / 2, y + s08 + s01 / 2),
						PI + PI / 2,
						PI + PI / 2 + PI / 4,
						s03 + s01 / 2,
					Color.WHITE);
					shapes = append(shapes, a);
					
					InverseArc b = new InverseArc(
						new Vector(x + s02 + s01 / 2, y + s08 + s01 / 2),
						PI + PI / 2 - PI / 4,
						PI + PI / 2,
						s03 + s01 / 2,
					Color.WHITE);
					shapes = append(shapes, b);
				}
				if(tile == 270) {
					InverseArc a = new InverseArc(
						new Vector(x + s06, y + s06),
						PI + PI / 2 - PI / 4,
						PI + PI / 2,
						s03,
					Color.WHITE);
						
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						a.points[0],
						new Vector(a.points[0].x, y + s06),
						new Vector(x + s00, y + s06)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}
				/*if(tile == 193) {
					Arc a = new Arc(
						new Vector(x + s12 - s01 / 2, y + s00),
						PI / 2 - PI / 8,
						PI / 2, 
						s05,
						PI / 2 - PI / 16,
						PI / 2, 
					Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s07, y + s00),
						new Vector(x + s07, y + s12),
						new Vector(x + s00, y + s12),
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s07, y + s00),
						a.points[0],
						new Vector(a.points[0].x, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}*/ 
				if(tile == 228) {
					Arc a = new Arc(
						new Vector(x - s01, y + s00),
						PI / 2 - PI / 8,
						PI / 2, 
						s06,
						PI / 2 - PI / 8,
						PI / 2 - PI / 16, 
					Color.WHITE);
					InverseArc b = new InverseArc(
						new Vector(x + s12 + s01, y + s06),
						PI + PI / 2 - PI / 8,
						PI + PI / 2,
						s03,
						PI + PI / 2 - PI / 8,
						PI + PI / 2 - PI / 16,
					Color.WHITE);
					
					shapes = append(shapes, a);
					shapes = append(shapes, b);
						
					shapes = append(shapes, new Shape(new Vector[]{
						a.points[0],
						b.points[0],
						new Vector(b.points[0].x, y + s12),
						new Vector(a.points[0].x, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, b);
				}
				if(tile == 173) {
					InverseArc a = new InverseArc(
						new Vector(x + s12, y + s06),
						PI + PI / 2,
						2 * PI,
						s07,
					Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}
				if(tile == 167) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s12, y + s03),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 47) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s04),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 48) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s08),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 64) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s08),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 104) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s12, y + s00),
						new Vector(x + s12, y + s06),
						new Vector(x + s00, y + s06)
					}, Color.WHITE));
				}
				if(tile == 72) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s10, y + s00),
						new Vector(x + s12, y + s00),
						new Vector(x + s12, y + s12),
						new Vector(x + s10, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s10, y + s06),
						new Vector(x + s10, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s00, y + s06),
						new Vector(x + s00, y + s06 - s10),
						new Vector(x + s09, y + s00),
						s10,
					Color.WHITE));
				}
				if(tile == 73) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s02, y + s00),
						new Vector(x + s00, y + s00),
						new Vector(x + s00, y + s12),
						new Vector(x + s02, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s12, y + s06),
						new Vector(x + s02, y + s06),
						new Vector(x + s02, y + s12),
						new Vector(x + s12, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s03, y + s00),
						new Vector(x + s12, y + s06 - s10),
						new Vector(x + s12, y + s06),
						s10,
					Color.WHITE));
				}
				if(tile == 51) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s02, y + s00),
						new Vector(x + s02, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s06, y + s00),
						new Vector(x + s12, y + s08),
						new Vector(x + s02, y + s08),
						s10,
					Color.WHITE));
				}
				if(tile == 52) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s10, y + s00),
						new Vector(x + s12, y + s00),
						new Vector(x + s12, y + s12),
						new Vector(x + s10, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s10, y + s08),
						new Vector(x + s00, y + s08),
						new Vector(x + s06, y + s00),
						s10,
					Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s09, y + s12),
						new Vector(x + s00, y + s08),
						new Vector(x + s10, y + s08),
						s10,
					Color.WHITE));
				}
				if(tile == 53) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s02, y + s00),
						new Vector(x + s02, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s06, y + s00),
						new Vector(x + s12, y + s08),
						new Vector(x + s02, y + s08),
					Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s02, y + s08),
						new Vector(x + s12, y + s08),
						new Vector(x + s03, y + s12),
						s10,
					Color.WHITE));
				}
				if(tile == 54) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s10, y + s00),
						new Vector(x + s12, y + s00),
						new Vector(x + s12, y + s12),
						new Vector(x + s10, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s10, y + s08),
						new Vector(x + s00, y + s08),
						new Vector(x + s06, y + s00),
						s10,
					Color.WHITE));
				}
				if(tile == 71) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s02, y + s00),
						new Vector(x + s02, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 34) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s04),
						new Vector(x + s12, y + s04),
						new Vector(x + s12, y + s10),
						new Vector(x + s00, y + s10)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s07, y + s10),
						new Vector(x + s12, y + s10),
						new Vector(x + s12, y + s12),
						new Vector(x + s07, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s06, y + s12),
						new Vector(x + s00, y + s10 + s10),
						new Vector(x + s00, y + s10),
						s10,
					Color.WHITE));
				}
				if(tile == 33) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s04),
						new Vector(x + s12, y + s04),
						new Vector(x + s12, y + s10),
						new Vector(x + s00, y + s10)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s10),
						new Vector(x + s05, y + s10),
						new Vector(x + s05, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new InverseArc(
						new Vector(x + s12, y + s10),
						new Vector(x + s12, y + s10 + s10),
						new Vector(x + s06, y + s12),
						s10,
					Color.WHITE));
				}
				if(tile == 111) {
					shapes = append(shapes, new InverseArc(
						new Vector(x + s02, y + s02),
						new Vector(x + s12, y + s02),
						new Vector(x + s12, y + s12),
						s10,
					Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s02, y + s00),
						new Vector(x + s02, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				
				if(tile == 160) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s12),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
					}, Color.WHITE));
				}
				if(tile == 161) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s00),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12),
					}, Color.WHITE));
				}
				
				if(tile == 106) {
					Arc a = new Arc(
						new Vector(x + s12 + s01, y + s06),
						PI / 2,
						PI / 2 + PI / 8,
						s06,
						PI / 2 + PI / 16,
						PI / 2 + PI / 8,
					Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s12),
						a.points[2],
						new Vector(a.points[2].x, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}
				if(tile == 107) {
					shapes = append(shapes, new Shape(new Vector[]{
							new Vector(x + s06, y + s06),
							new Vector(x + s12, y + s06),
							new Vector(x + s12, y + s12),
							new Vector(x + s06, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Arc(
						new Vector(x + s01, y + s06),
						PI / 2,
						PI / 2 + PI / 8,
						s06,
						PI / 2,
						PI / 2 + PI / 16,
					Color.WHITE));
				}
				
				if(tile == 30) {
					shapes = append(shapes, new Shape(new Vector[]{
							new Vector(x + s00, y + s00),
							new Vector(x + s06, y + s00),
							new Vector(x + s06, y + s12),
							new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Arc(
						new Vector(x + s12 - s01, y + s00),
						PI / 2 - PI / 8,
						PI / 2,
						s06,
						PI / 2 - PI / 16,
						PI / 2,
					Color.WHITE));
				}
				if(tile == 31) {
					shapes = append(shapes, new Shape(new Vector[]{
							new Vector(x + s06, y + s00),
							new Vector(x + s12, y + s00),
							new Vector(x + s12, y + s12),
							new Vector(x + s06, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Arc(
						new Vector(x + s01, y + s00),
						PI / 2,
						PI / 2 + PI / 8,
						s06,
						PI / 2,
						PI / 2 + PI / 16,
					Color.WHITE));
				}
				
				if(tile == 20) {
					InverseArc a = new InverseArc(
							new Vector(x + s12 - s01, y + s06),
							PI + PI / 2,
							PI + PI / 2 + PI / 8,
							s03,
							PI + PI / 2,
							PI + PI / 2 + PI / 16,
						Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
				}
				if(tile == 227) {
					Arc a = new Arc(
						new Vector(x + s12 + s01, y + s00),
						PI / 2,
						PI / 2 + PI / 8, 
						s06,
						PI / 2 + PI / 16,
						PI / 2 + PI / 8, 
					Color.WHITE);
					InverseArc b = new InverseArc(
						new Vector(x - s01, y + s06),
						PI + PI / 2,
						PI + PI / 2 + PI / 8,
						s03,
						PI + PI / 2 + PI / 16,
						PI + PI / 2 + PI / 8,
					Color.WHITE);
					
					shapes = append(shapes, a);
					shapes = append(shapes, b);
						
					shapes = append(shapes, new Shape(new Vector[]{
						b.points[2],
						a.points[2],
						new Vector(a.points[2].x, y + s12),
						new Vector(b.points[2].x, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, b);
				}
				
				if(tile == 229) {
					InverseArc a = new InverseArc(
						new Vector(x + s12, y + s06),
						PI + PI / 2,
						2 * PI,
						s07,
					Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, a);
					
					InverseArc b = new InverseArc(
						new Vector(x + s01, y + s06),
						PI + PI / 2 - PI / 8,
						PI + PI / 2,
						s03,
						PI + PI / 2 - PI / 16,
						PI + PI / 2,
					Color.WHITE);
					shapes = append(shapes, b);
				}
				
				if(tile == 80) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s12, y + s04),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 81) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s04),
						new Vector(x + s12, y + s00),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				
				if(tile == 74) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s10, y + s00),
						new Vector(x + s12, y + s00),
						new Vector(x + s12, y + s12),
						new Vector(x + s10, y + s12)
					}, Color.WHITE));
				}
				if(tile == 114) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s10, y + s00),
						new Vector(x + s12, y + s00),
						new Vector(x + s12, y + s06),
						new Vector(x + s10, y + s06)
					}, Color.WHITE));
				}
				
				if(tile == 146) {
					InverseArc a = new InverseArc(
						new Vector(x + s06, y + s06),
						PI + PI / 2,
						2 * PI,
						s07,
					Color.WHITE);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
							new Vector(x + s06, y + s00),
							new Vector(x + s12, y + s00),
							new Vector(x + s12, y + s06),
							new Vector(x + s06, y + s06)
						}, Color.WHITE));
					shapes = append(shapes, a);
				}
				
				if(tile == 140) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s09, y + s00),
						new Vector(x + s12, y + s03),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 141) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s03),
						new Vector(x + s09, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				
				if(tile == 121) {
					InverseArc a = new InverseArc(
							new Vector(x + s07, y + s12 + s01),
							PI + PI / 4,
							PI + PI / 2,
							s05,
							PI + PI / 4,
							PI + PI / 4 + PI / 8,
						Color.WHITE);
					shapes = append(shapes, a);
						
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						a.points[0],
						new Vector(a.points[0].x, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 193) {
					InverseArc a = new InverseArc(
							new Vector(x + s07, y + s01),
							PI + PI / 4,
							PI + PI / 2,
							s05,
							PI + PI / 4 + PI / 8,
							PI + PI / 2,
						Color.WHITE);
					shapes = append(shapes, a);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s01),
						new Vector(x + s12, y + s01),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				
				if(tile == 230) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s04),
						new Vector(x + s04, y + s08),
						new Vector(x + s04, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s04, y + s08),
						new Vector(x + s12, y + s12),
						new Vector(x + s04, y + s12)
					}, Color.WHITE));
				}
				
				if(tile == 60) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s12),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12)
					}, Color.WHITE));
				}
				
				if(tile == 156) {
					InverseArc a = new InverseArc(
							new Vector(x + s05, y + s05),
							PI / 2,
							PI,
							s12 + s07,
							PI / 2 + PI / 4 - PI / 32,
							PI / 2 + PI / 4 + PI / 32,
						Color.WHITE);
					shapes = append(shapes, a);
				}
				if(tile == 157) {
					InverseArc a = new InverseArc(
							new Vector(x - s12 + s05, y + s05),
							PI / 2,
							PI,
							s12 + s07,
							PI / 2,
							PI / 2 + PI / 4 - PI / 32,
						Color.WHITE);
					shapes = append(shapes, a);
				}
				if(tile == 158) {
					InverseArc a = new InverseArc(
							new Vector(x + s12 + s07, y + s05),
							0,
							PI / 2,
							s12 + s07,
							PI / 4 + PI / 32,
							PI / 2,
						Color.WHITE);
					shapes = append(shapes, a);
				}
				if(tile == 159) {
					InverseArc a = new InverseArc(
							new Vector(x + s07, y + s05),
							0,
							PI / 2,
							s12 + s07,
							PI / 4 - PI / 32,
							PI / 4 + PI / 32,
						Color.WHITE);
					shapes = append(shapes, a);
				}
				if(tile == 179) {
					InverseArc a = new InverseArc(
							new Vector(x + s07, y - s12 + s05),
							0,
							PI / 2,
							s12 + s07,
							0,
							PI / 4 + PI / 32,
						Color.WHITE);
					shapes = append(shapes, a);
				}
				if(tile == 199) {
					InverseArc a = new InverseArc(
							new Vector(x + s07, y + s12 + s07),
							PI + PI / 2,
							2 * PI,
							s12 + s07,
							PI + PI / 2 + PI / 4 + PI / 32,
							2 * PI,
						Color.WHITE);
					shapes = append(shapes, a);
				}
				if(tile == 218) {
					InverseArc a = new InverseArc(
							new Vector(x + s12 + s07, y + s07),
							PI + PI / 2,
							2 * PI,
							s12 + s07,
							PI + PI / 2,
							PI + PI / 2 + PI / 4 - PI / 32,
						Color.WHITE);
					shapes = append(shapes, a);
				}
				if(tile == 219) {
					InverseArc a = new InverseArc(
							new Vector(x + s07, y + s07),
							PI + PI / 2,
							2 * PI,
							s12 + s07,
							PI + PI / 2 + PI / 4 - PI / 32,
							PI + PI / 2 + PI / 4 + PI / 32,
						Color.WHITE);
					shapes = append(shapes, a);
				}
				if(tile == 122) {
					InverseArc a = new InverseArc(
							new Vector(x + s12 + s01, y + s07),
							PI + PI / 4,
							PI + PI / 2,
							s05,
							PI + PI / 4,
							PI + PI / 4 + PI / 8,
						Color.WHITE);
					shapes = append(shapes, a);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s06, y + s00),
						a.points[0],
						new Vector(a.points[0].x, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 252) {
					InverseArc a = new InverseArc(
							new Vector(x + s01, y + s07),
							PI + PI / 4,
							PI + PI / 2,
							s05,
							PI + PI / 4 + PI / 8,
							PI + PI / 2,
						Color.WHITE);
					shapes = append(shapes, a);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s07),
						new Vector(x + s12, y + s07),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
					
					/*InverseArc b = new InverseArc(
							new Vector(x + s12 - s01, y + s07),
							PI + PI / 2,
							PI + PI / 2 + PI / 8,
							s03,
							PI + PI / 2,
							PI + PI / 2 + PI / 16,
						Color.WHITE);
					shapes = append(shapes, b);*/
				}
				if(tile == 176) {
					InverseArc a = new InverseArc(
							new Vector(x + s05, y - s12 + s05),
							PI / 2,
							PI,
							s12 + s07,
							PI / 2 + PI / 4 + PI / 32,
							PI,
						Color.WHITE);
					shapes = append(shapes, a);
				}
				if(tile == 196) {
					InverseArc a = new InverseArc(
							new Vector(x + s05, y + s12 + s07),
							PI,
							PI + PI / 2,
							s12 + s07,
							PI,
							PI + PI / 4 - PI / 32,
						Color.WHITE);
					shapes = append(shapes, a);
				}
				if(tile == 216) {
					InverseArc a = new InverseArc(
							new Vector(x + s05, y + s07),
							PI,
							PI + PI / 2,
							s12 + s07,
							PI + PI / 4 - PI / 32,
							PI + PI / 4 + PI / 32,
						Color.WHITE);
					shapes = append(shapes, a);
				}
				if(tile == 182) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s03),
						new Vector(x + s12, y + s09),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 92) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s09),
						new Vector(x + s06, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 100) {
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s06, y + s00),
						new Vector(x + s12, y + s03),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				if(tile == 240) {
					InverseArc a = new InverseArc(
							new Vector(x + s06, y + s06),
							PI + PI / 2 - PI / 8,
							PI + PI / 2,
							s03,
						Color.WHITE);
					shapes = append(shapes, a);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s03),
						a.points[0],
						new Vector(a.points[0].x, y + s06),
						new Vector(x + s00, y + s06)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s06),
						new Vector(x + s12, y + s06),
						new Vector(x + s12, y + s12),
						new Vector(x + s00, y + s12)
					}, Color.WHITE));
				}
				
				if(tile == 91) {
					InverseArc a = new InverseArc(
							new Vector(x + s05, y + s05),
							PI + PI / 8,
							PI + PI / 2 - PI / 8,
							s04,
						Color.WHITE);
					shapes = append(shapes, a);
					
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, y + s00),
						new Vector(x + s03, y + s00),
						a.points[0],
						new Vector(x, a.points[0].y)
					}, Color.WHITE));
					shapes = append(shapes, new Shape(new Vector[]{
						new Vector(x + s00, a.points[2].y),
						a.points[2],
						new Vector(x + s12, y + s09),
						new Vector(x + s00, y + s09),
					}, Color.WHITE));
				}
			}
		}
		
		return(shapes);
	}
}